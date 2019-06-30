//package students.yangjun
//
//import java.sql.{Connection, DriverManager, PreparedStatement}
//import java.util.Properties
//
//import com.typesafe.config.{Config, ConfigFactory}
//import dataAnalysis.Bean.Log
//import dataAnalysis.MyUtils.Constant
//import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
//
//object CalCulateUtils {
//
//  def getAreaRpt(cached: DataFrame, sqlContext: SQLContext) = {
//    cached.registerTempTable("t_tmp")
//    sqlContext.udf.register("getInitRequest", getInitRequest)
//    sqlContext.udf.register("getValidRequest", getValidRequest)
//    sqlContext.udf.register("getAdRequest", getAdRequest)
//    sqlContext.udf.register("getJoinPriceCompetition", getJoinPriceCompetition)
//    sqlContext.udf.register("getSuccPriceCompetition", getSuccPriceCompetition)
//    sqlContext.udf.register("getShowNum", getShowNum)
//    sqlContext.udf.register("getClickNum", getClickNum)
//    sqlContext.udf.register("getDSPAdConsume", getDSPAdConsume)
//    sqlContext.udf.register("getDSPAdCost", getDSPAdCost)
//
//    val sqlStr = "select provincename,cityname," +
//      " sum(getInitRequest(requestmode,processnode)) initRequestNum," +
//      "sum(getValidRequest(requestmode,processnode)) validRequestNum," +
//      "sum(getAdRequest(requestmode,processnode)) adRequestNum," +
//      "sum(getJoinPriceCompetition(iseffective,isbilling,isbid,adorderid)) joinPriceCompetitionNum," +
//      "sum(getSuccPriceCompetition(iseffective,isbilling,iswin)) succPriceCompetitionNum," +
//      "sum(getShowNum(requestmode,iseffective)) showNum," +
//      "sum(getClickNum(requestmode,iseffective)) clickNum," +
//      "sum(getDSPAdConsume(iseffective,isbilling,iswin,winprice)) DSPAdConsume," +
//      "sum(getDSPAdCost(iseffective,isbilling,iswin,adpayment)) DSPAdCost" +
//      " from t_tmp group by provincename,cityname"
//
//    val sql: DataFrame = sqlContext.sql(sqlStr)
//    sql
//  }
//
//
//  def getcached(df: DataFrame, sqlContext: SQLContext) = {
//
//    df.registerTempTable("t_tmp_all")
//    val df1: DataFrame = sqlContext.sql("select provincename,cityname,requestmode,processnode,iseffective,isbilling,isbid,iswin,adorderid,winprice,adpayment from t_tmp_all")
//    val cached: DataFrame = df1.cache()
//    cached
//  }
//
//
//  //原始请求
//  var getInitRequest = (requestmode: Int, processnode: Int) => {
//    if (requestmode == 1 && processnode >= 1) {
//      1
//    } else {
//      0
//    }
//    //三元表达式怎么写
//    /*var initRequest = (requestmode == 1 && processnode >= 1) ? 1 : 0*/
//    //scala无三元表达式
//  }
//
//  //有效请求
//  var getValidRequest = (requestmode: Int, processnode: Int) => {
//    if (requestmode == 1 && processnode >= 2) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //广告请求
//  var getAdRequest = (requestmode: Int, processnode: Int) => {
//    if (requestmode == 1 && processnode == 3) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //参与竞价数
//  var getJoinPriceCompetition = (iseffective: Int, isbilling: Int, isbid: Int, adorderid: Int) => {
//    if (iseffective == 1 && isbilling == 1 && isbid == 1 && adorderid != 0) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //getSuccPriceCompetition竞价成功
//  var getSuccPriceCompetition = (iseffective: Int, isbilling: Int, iswin: Int) => {
//    if (iseffective == 1 && isbilling == 1 && iswin == 1) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //展示数
//  var getShowNum = (requestmode: Int, iseffective: Int) => {
//    if (requestmode == 2 && iseffective == 1) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //点击数
//  var getClickNum = (requestmode: Int, iseffective: Int) => {
//    if (requestmode == 3 && iseffective == 1) {
//      1
//    } else {
//      0
//    }
//  }
//
//  //DSP广告消费 跟竞价成功数条件一样:第一次理解错误,不是计数,而是满足条件下取某个字段的值
//  //相对于投放DSP广告的广告主来说满足广告成功展示每次消费WinPrice/1000
//  var getDSPAdConsume = (iseffective: Int, isbilling: Int, iswin: Int, winprice: Double) => {
//    if (iseffective == 1 && isbilling == 1 && iswin == 1) {
//      winprice / 1000
//    } else {
//      0
//    }
//  }
//
//  //DSP广告成本 跟竞价成功数条件一样:第一次理解错误,不是计数,而是满足条件下取某个字段的值
//  //相对于投放DSP广告的广告主来说满足广告成功展示每次成本adpayment/1000
//  var getDSPAdCost = (iseffective: Int, isbilling: Int, iswin: Int, adpayment: Double) => {
//    if (iseffective == 1 && isbilling == 1 && iswin == 1) {
//      adpayment / 1000
//    } else {
//      0
//    }
//  }
//
//
//  def getProvinceAndCity(df: DataFrame, sqlContext: SQLContext) = {
//
//    df.registerTempTable("t_tmp")
//    val sql: DataFrame = sqlContext.sql("select provincename,cityname,count(1) cts from t_tmp group by provincename,cityname")
//    sql
//  }
//
//  def savaToMysql(res: DataFrame) = {
//    val load: Config = ConfigFactory.load()
//    val props: Properties = new Properties()
//    props.setProperty("user", load.getString(Constant.JDBC_USER))
//    props.setProperty("password", load.getString(Constant.JDBC_PASSWORD))
//    //mode不加,没有的表也会创建,只不过,有了的话则会报错,好比SaveMode.ErrorIfExit
//    res.write.mode(SaveMode.Overwrite).jdbc(load.getString(Constant.JDBC_URL), load.getString(Constant.JDBC_TABLE_NAME), props)
//
//  }
//
//
//  def arrToTarget(arr: Array[String]) = {
//    val log: Log = Log(arr)
//    //requestmode	processnode	iseffective	isbilling	isbid	iswin	adorderid winprice adpayment
//    val provincename = log.provincename
//    val cityname = log.cityname
//    val requestmode = log.requestmode
//    val processnode = log.processnode
//    val iseffective = log.iseffective
//    val isbilling = log.isbilling
//    val isbid = log.isbid
//    val iswin = log.iswin
//    val adorderid = log.adorderid
//    val winprice = log.winprice
//    val adpayment = log.adpayment
//
//    val initRequestNum = if (requestmode == 1 && processnode >= 1) 1 else 0
//    val validRequestNum = if (requestmode == 1 && processnode >= 2) 1 else 0
//    val adRequestNum = if (requestmode == 1 && processnode == 3) 1 else 0
//    val joinPriceCompetitionNum = if (iseffective == 1 && isbilling == 1 && isbid == 1 && adorderid != 0) 1 else 0
//    val succPriceCompetitionNum = if (iseffective == 1 && isbilling == 1 && iswin == 1) 1 else 0
//    val showNum = if (requestmode == 2 && iseffective == 1) 1 else 0
//    val clickNum = if (requestmode == 3 && iseffective == 1) 1 else 0
//    val DSPAdConsume = if (iseffective == 1 && isbilling == 1 && iswin == 1) winprice/1000 else 0
//    val DSPAdCost = if (iseffective == 1 && isbilling == 1 && iswin == 1) adpayment/1000 else 0
//  //只有对偶元组或者kv结构的形式才可以reduceByKey,所以指标要括起来
//    ((provincename,cityname),(initRequestNum,validRequestNum,adRequestNum,joinPriceCompetitionNum,succPriceCompetitionNum,showNum,clickNum,DSPAdConsume,DSPAdCost))
//  }
//
//
//  def data2MySQL(it: Iterator[((String, String), (Int, Int, Int, Int, Int, Int, Int, Double, Double))]): Unit = {
//    val load: Config = ConfigFactory.load()
//    val tableName = load.getString(Constant.JDBC_TABLE_NAME2)
//    //一个迭代器代表一个分区，分区中有多条数据
//    //先获得一个JDBC连接
//    val conn: Connection = DriverManager.getConnection(load.getString(Constant.JDBC_URL), load.getString(Constant.JDBC_USER), load.getString(Constant.JDBC_PASSWORD))
//    //将数据通过Connection写入到数据库
//    val pstm: PreparedStatement = conn.prepareStatement(s"INSERT INTO $tableName VALUES (?,?,?,?,?,?,?,?,?,?,?)")
//    //将分区中的数据一条一条写入到MySQL中
//    it.foreach(tp => {
//      pstm.setString(1, tp._1._1)
//      pstm.setString(2, tp._1._2)
//      pstm.setInt(3, tp._2._1)
//      pstm.setInt(4, tp._2._2)
//      pstm.setInt(5, tp._2._3)
//      pstm.setInt(6, tp._2._4)
//      pstm.setInt(7, tp._2._5)
//      pstm.setInt(8, tp._2._6)
//      pstm.setInt(9, tp._2._7)
//      pstm.setDouble(10,tp._2._8)
//      pstm.setDouble(11,tp._2._9)
//      pstm.executeUpdate()
//    })
//    //将分区中的数据全部写完之后，在关闭连接
//    if (pstm != null) {
//      pstm.close()
//    }
//    if (conn != null) {
//      conn.close()
//    }
//  }
//
//}

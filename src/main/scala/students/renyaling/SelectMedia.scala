//package students.renyaling
//
//import cn.dmp.utils.NBF
//import com.edu360.utils.{AreaDistributionUtil, NBF, ToMysqlUtils}
//import org.apache.spark.broadcast.Broadcast
//import org.apache.spark.rdd.RDD
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.{DataFrame, SQLContext}
//
//object SelectMedia {
//  def main(args: Array[String]): Unit = {
//    // 0 校验参数个数
//    if (args.length != 3) {
//      println(
//        """
//          |cn.dmp.tools.Bzip2Parquet
//          |参数：
//          | logInputPath
//          | ruleInputPath
//          | resultOutputPath
//        """.stripMargin)
//      sys.exit()
//    }
//    // 1 接受程序参数
//    val Array(logInputPath,ruleInputPath,resultOutputPath) = args
//    // 2 创建sparkconf->sparkContext
//    val sparkConf = new SparkConf()
//    sparkConf.setAppName(s"${this.getClass.getSimpleName}")
//    sparkConf.setMaster("local[*]")
//    val sc = new SparkContext(sparkConf)
//    // RDD 序列化到磁盘 worker与worker之间的数据传输
//    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    val sQLContext = new SQLContext(sc)
//
//    //取到HDFS中的app规则
//    val rulesLines:RDD[String] = sc.textFile(ruleInputPath)
//
//    //整理app规则数据
//    val appRulesRDD: RDD[(String, String)] = rulesLines.map(line => {
//      val fields = line.split("\t")
//      val appId = fields(4)
//      val appName = fields(1)
//      (appId, appName)
//    })
//
//    //将分散在多个Executor中的部分IP规则收集到Driver端
//    val rulesInDriver: Array[(String, String)] = appRulesRDD.collect()
//
//    //将Driver端的数据广播到Executor
//    //广播变量的引用（还在Driver端）
//    val broadcastRef: Broadcast[Array[(String, String)]] = sc.broadcast(rulesInDriver)
//
//
//    // 3 读取日志数据
//    val rawdata = sc.textFile(logInputPath)
//
//    val filter: RDD[Array[String]] = rawdata
//      .map(line => line.split(",", line.length))
//      .filter(_.length >= 85)
//    val mapped = filter.map(arr => {
//      val rules: Array[(String, String)] = broadcastRef.value
//
//      val appid = arr(13)
//      var appname = arr(14)
//
//      if ("".equals(appname.trim())){
//        appname=ToMysqlUtils.searchAppname(rules,appid)
//      }
//      val requestmode = NBF.toInt(arr(8))
//      val processnode = NBF.toInt(arr(35))
//      val iseffective = NBF.toInt(arr(30))
//      val isbilling = NBF.toInt(arr(31))
//      val isbid = NBF.toInt(arr(39))
//      val adorderid = NBF.toInt(arr(2))
//      val iswin = NBF.toInt(arr(42))
//      val winprice = NBF.toDouble(arr(41))
//      val adpayment = NBF.toInt(arr(75))
//      (appname,
//        (AreaDistributionUtil.selectPrimaryRequest(requestmode, processnode),
//          AreaDistributionUtil.selectValidRequest(requestmode, processnode),
//          AreaDistributionUtil.selectAdvRequest(requestmode, processnode),
//          AreaDistributionUtil.selectJoinBidding(iseffective, isbilling, isbid, adorderid),
//          AreaDistributionUtil.selectSuccessBidding(iseffective, isbilling, iswin),
//          AreaDistributionUtil.selectAdverShow(requestmode, iseffective),
//          AreaDistributionUtil.selectAdverClick(requestmode, iseffective),
//          AreaDistributionUtil.selectDspConsume(iseffective, isbilling, iswin, winprice),
//          AreaDistributionUtil.selectDspCost(iseffective, isbilling, iswin, adpayment)
//        ))
//    })
//    val reduced = mapped.reduceByKey((x,y)=>(x._1+y._1,x._2+y._2,x._3+y._3,x._4+y._4,x._5+y._5,x._6+y._6,x._7+y._7,x._8+y._8,x._9+y._9))
//    reduced.saveAsTextFile(resultOutputPath)
//    sc.stop()
//  }
//}

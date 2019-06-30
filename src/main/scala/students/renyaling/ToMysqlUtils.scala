//package students.renyaling
//
//import java.sql.{Connection, DriverManager, PreparedStatement}
//import java.util.Properties
//import scala.io.{BufferedSource, Source}
//
//import com.edu360.constant.Constant
//import org.apache.spark.sql.DataFrame
//
//object ToMysqlUtils {
//  def dfToSql(df:DataFrame,tableName:String)={
//    val props = new Properties()
//    props.put("user",Constant.USER)
//    props.put("password",Constant.PASSWORD)
//    df.write.mode("append").jdbc(Constant.URL,tableName,props)
//  }
//
//  def data2MySQL(it: Iterator[((String,String), Int)]): Unit = {
//    //一个迭代器代表一个分区，分区中有多条数据
//    //先获得一个JDBC连接
//    val conn: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/day05?characterEncoding=UTF-8", "root", "123456")
//    //将数据通过Connection写入到数据库
//    val pstm: PreparedStatement = conn.prepareStatement("INSERT INTO pccount VALUES (?, ?,?)")
//    //将分区中的数据一条一条写入到MySQL中
//    it.foreach(tp => {
//      pstm.setString(2, tp._1._1)
//      pstm.setString(3, tp._1._2)
//      pstm.setInt(1, tp._2)
//      pstm.executeUpdate()
//    })
//    //将分区中的数据全部写完之后，在关闭连接
//    if(pstm != null) {
//      pstm.close()
//    }
//    if (conn != null) {
//      conn.close()
//    }
//  }
//
//  def readRules(path: String): Array[(String,String)] = {
//    //读取ip规则
//    val bf: BufferedSource = Source.fromFile(path)
//    val lines: Iterator[String] = bf.getLines()
//    //对ip规则进行整理，并放入到内存
//    val rules: Array[(String, String)] = lines.map(line => {
//      val fileds = line.split("\t")
//      val appId = fileds(4)
//      val appName = fileds(1)
//      (appId,appName)
//    }).toArray
//    rules
//  }
//
//  def searchAppname(rules:Array[(String,String)],appid:String):String= {
//    for (a <- rules) {
//      if (appid.equals(a._1)) {
//        return a._2
//      }
//    }
//    return "无名氏"
//  }
//
//}

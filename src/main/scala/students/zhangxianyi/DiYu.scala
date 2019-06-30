package students.zhangxianyi

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object DiYu {
  def main(args: Array[String]): Unit = {
    //0 校验参数个数
    if (args.length != 2) {
      println(
        """
          |xiangmu2.DiYu
          |参数：
          | logInputPath
          | resultOutputPath
        """.stripMargin)
      sys.exit()
    }
    // 1 接受程序参数
    val Array(logInputPath, resultOutputPath) = args

    // 2 创建sparkconf->sparkContext
    val sparkConf = new SparkConf()
    sparkConf.setAppName(s"${this.getClass.getSimpleName}")
    sparkConf.setMaster("local[*]")
//3 使用SparkConf初始化SparkContext对象
    val sc = new SparkContext(sparkConf)
    //4 定义一个SQLContext将sc进一步包装
        val context: SQLContext = new SQLContext(sc)
//5 读取日志数据
val shuju = context.read.parquet(logInputPath)
//6 注册临时表
    shuju.registerTempTable("diyu")
    //7 通过sql语句生成表结构
        val frame: DataFrame = context.sql(
          """
    select provincename,cityname,
    sum(case when requestmode=1 and processnode >=1 then 1 else 0 end) 原始请求,
    sum(case when requestmode=1 and processnode >=2 then 1 else 0 end) 有效请求,
    sum(case when requestmode=1 and processnode =3 then 1 else 0 end) 广告请求,
    sum(case when iseffective=1 and isbilling=1 and isbid=1 and adorderid !=0 then 1 else 0 end) 参与竞价数,
    sum(case when iseffective=1 and isbilling=1 and iswin=1 then 1 else 0 end) 竞价成功数,
    sum(case when requestmode=2 and iseffective=1 then 1 else 0 end) 展示数,
    sum(case when requestmode=3 and iseffective=1 then 1 else 0 end) 点击数,
    sum(case when iseffective=1 and isbilling=1 and iswin=1 then 1.0*adpayment/1000 else 0 end) 广告成本,
    sum(case when iseffective=1 and isbilling=1 and iswin=1 then 1.0*winprice/1000 else 0 end) 广告消费
    from diyu group by provincename,cityname
          """.stripMargin)
    //8 控制台展示
    frame.show()
    //9  配置数据库信息，将结果保存到MySql
        val properties: Properties = new Properties()
    properties.put("user","root")
    properties.put("password","123456")
frame.write.mode("overwrite").jdbc("jdbc:mysql://localhost:3306/qq?characterEncoding=utf-8", "diyu", properties)
    //10 关流
    sc.stop()
  }


}

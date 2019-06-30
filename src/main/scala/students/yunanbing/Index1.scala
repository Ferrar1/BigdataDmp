package students.yunanbing

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object Index1 {
  //SQL形式
  def main(args: Array[String]): Unit = {

    // 1.参数设置
    if(args.length!=2){
      println(
        """
          |参数有两个
          |都要填写
        """.stripMargin)
      return
    }
    val Array(inputpath,outputpath)=args

    val conf: SparkConf = new SparkConf().setAppName("Index1").setMaster("local[*]").set("spark.serializer","org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = new SparkContext(conf)
    val sqlContext: SQLContext = new SQLContext(sc)
    val df: DataFrame = sqlContext.read.parquet(inputpath)
// 创建元数据表
    df.registerTempTable("Data")

    //01原始请求数
    val originalrequest :DataFrame = sqlContext.sql("select provincename,cityname,count(*) as counts from Data where requestmode=1 and processnode>=1 group by provincename,cityname")
     originalrequest.registerTempTable("originalrequest")

    //02有效请求数 valid request
      val validrequest :DataFrame = sqlContext.sql("select provincename,cityname,count(*) as counts from Data where requestmode=1 and processnode>=2 group by provincename,cityname")
    validrequest.registerTempTable("validrequest")

    //03广告请求Ad Request
    val AdRequest :DataFrame = sqlContext.sql("select provincename,cityname,count(*) as counts from Data where requestmode=1 and processnode=3 group by provincename,cityname")
    validrequest.registerTempTable("AdRequest")

    //04参与竞价 participationbidding
     val participationbidding :DataFrame =sqlContext.sql("select provincename,cityname,count(*) as counts from Data where iseffective=1  and isbilling=1 and isbid=1 and adorderid != 0 group by provincename,cityname")
      validrequest.registerTempTable("participationbidding")

    //05竞价成功 biddingsuccess
      val biddingsuccess :DataFrame =sqlContext.sql("select provincename,cityname,count(*) as counts from Data where iseffective=1  and isbilling=1 and iswin=1 group by provincename,cityname")
       validrequest.registerTempTable("biddingsuccess")

    //成功率
    sqlContext.sql("select participationbidding.cityname,participationbidding.counts as ,biddingsuccess.counts as succcounts,biddingsuccess.counts/biddingsuccess.counts as percent from participationbidding join biddingsuccess on participationbidding.cityname=biddingsuccess.cityname")

    //06 07展示数 showNum    点击数hitsNum
    val showNum :DataFrame =sqlContext.sql("select  provincename,cityname,count(*) as counts from Data where  requestmode=2 and  iseffective=1 group by provincename, cityname")
    val hitsNum :DataFrame =sqlContext.sql("select  provincename,cityname,count(*) as counts from Data where  requestmode=3 and  iseffective=1 group by provincename, cityname")

    // 08 09 DSP广告消费Advertising spending  DSP广告成本AdvertisingCost
    val AdvertisingSpending : DataFrame = sqlContext.sql("select provincename,cityname,count(*) as counts from Data where  iseffective=1 and  isbilling=1 and iswin=1 group by provincename, cityname")
    val AdvertisingCost : DataFrame = sqlContext.sql("select provincename,cityname,count(*) as counts from Data where  iseffective=1 and  isbilling=1 and iswin=1 group by provincename, cityname")



    val result = originalrequest.join(validrequest,Seq("provincename","cityname"),"left")
      .join(AdRequest,Seq("provincename","cityname"),"left")
      .join(participationbidding,Seq("provincename","cityname"),"left")
      .join(biddingsuccess,Seq("provincename","cityname"),"left")
      .join(showNum,Seq("provincename","cityname"),"left")
      .join(hitsNum,Seq("provincename","cityname"),"left")
      .join(AdvertisingSpending,Seq("provincename","cityname"),"left")
      .join(AdvertisingCost,Seq("provincename","cityname"),"left")

    result.registerTempTable("result")




    sc.stop()
  }
}

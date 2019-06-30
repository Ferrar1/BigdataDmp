//package students.yangjun
//
//import java.util.Properties
//
//import com.typesafe.config.{Config, ConfigFactory}
//import dataAnalysis.CalculatorTools.CalCulateUtils
//import org.apache.spark.sql.types.StructType
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
//
//object parquetToAreaRpt {
//  /**
//    * 目前想到sql实现有三种方式:
//    *   方式一:UDF,已经实现
//    *   方式二;case when语句,待整理实现
//    *   方式三,先拿一个指标,筛选,按省和市计数.再把所有的指标通过联表查询,省=省,市=市,整理为一张表.待整理实现
//    *       方式三,对应RDD操作实质就是需要把log数据分多次过滤然后聚合,然后join,这样下面的cache那一步就有必要了,
//    *       就是不知道sparkSQL是否会强大的自动优化了转成方式一,应该不可能,所以最好把过滤前结果缓存下,或者直接写入HDFS持久化
//    *       免得每次都去读取path中开始处理
//    *
//    *       要我在过滤和设置为0之间选择我选择设置为0,因为这样无需过滤就和聚合了,而你多次过滤后再聚合则会产生多个stage吧?
//    *       还得缓存来解决速度问题!!甚至可以持久化checkpoint
//    *
//    *       问题:多表合并时,无论是内连接还是左外连接均有丢失数据的风险,有无全外连接么?
//    *
//    * @param args
//    */
//  def main(args: Array[String]): Unit = {
//
//    // 0 校验参数个数
//    if (args.length != 1) {
//      println(
//        """
//          |dataAnalysis.areaRpt.parquetToAreaRpt
//          |参数：
//          | parquetInputPath
//        """.stripMargin)
//      sys.exit()
//    }
//
//    val Array(parquetInputPath) = args
//
//    val sparkConf = new SparkConf()
//    sparkConf.setAppName(s"${this.getClass.getSimpleName}")
//    sparkConf.setMaster("local[*]")
//    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    /**
//      * 问题:数据源是采用snappy压缩方式和parquet序列化机制,以parquet数据格式存入文件的,
//      * 那这个时候读取的话无需配置解压缩和反序列化机制,然后再以parquet的方式读取也不会解析数据失败????
//      *
//      */
//
//    val sc = new SparkContext(sparkConf)
//
//    val sqlContext = new SQLContext(sc)
//
//    // 3 读取parquet数据
//    val df: DataFrame = sqlContext.read.parquet(parquetInputPath)
//
//
//    //因为后面大量计算,先将需要的字段缓存,若是数据特别多可以再缓存之后.checkPoint("")保存到HDFS/本地.防止缓存数据丢失
//    /**
//      * 问:是否可行,有无更好的方式,若多次利用的话?
//      *   中间有个按省和市的分组聚合,通过((省,市),(指标1,指标2,...))一次性就可以分组聚合.不会多次利用,应该无需缓存了
//      */
//    //val cached: DataFrame = CalCulateUtils.getcached(df,sqlContext)
//
//    //val res: DataFrame = CalCulateUtils.getAreaRpt(cached,sqlContext)
//    val res: DataFrame = CalCulateUtils.getAreaRpt(df,sqlContext)
//
//    //res.show()
//
//    /**
//      * 根据配置文件信息,利用dataFrame对象把数据存入mysql
//      */
//    CalCulateUtils.savaToMysql(res)
//
//    sc.stop()
//
//  }
//}

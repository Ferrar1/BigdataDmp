package students.cuibin

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object txtToCsvToParquet {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("txtToCsvToParquet").setMaster("local[*]")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerialization")
    conf.set("spark.sql.parquet.compression.codec", "snappy")
    val sc: SparkContext = new SparkContext(conf)
    val sqlContext: SQLContext = new SQLContext(sc)
    val structType: StructType = StructType(LogToParguet.schemaList)
    val load: DataFrame = sqlContext.read.format("com.databricks.spark.csv").schema(structType).option("delimiter", ",").load(LogToParguet.filename)
    load.write.parquet(LogToParguet.forname)
    sc.stop()

  }
}

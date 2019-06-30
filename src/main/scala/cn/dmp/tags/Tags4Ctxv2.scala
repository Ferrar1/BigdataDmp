package cn.dmp.tags

import cn.dmp.utils.{JedisPools, TagsUtils}
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.collection.mutable.ListBuffer

object Tags4Ctxv2 {
    def main(args: Array[String]): Unit = {
        if (args.length != 4) {
            println(
                """
                  |cn.dmp.tags.Tags4Ctx
                  |参数：
                  | 输入路径
                  | 字典文件路径
                  | 停用词库
                  | 日期
                  | 输出路径
                """.stripMargin)
            sys.exit()
        }

        val Array(inputPath, dictFilePath,day,outputPath) = args

        val spark = SparkSession.builder().appName("JdbcDataSource")
                .master("local[*]")
                .getOrCreate()

        import spark.implicits._
        // 字典文件 appMapping
        val dictMap = spark.read.textFile(dictFilePath).map(line => {
            val fields = line.split("\t", -1)
            (fields(4), fields(1))
        }).collect().toMap

        // 字典文件 stowordsMapping
        val stopWordsMap = spark.read.textFile(dictFilePath).map((_,0)).collect().toMap

        // 将字典数据广播executor
        val broadcastAppDict = spark.sparkContext.broadcast(dictMap)
        val broadcastStopWordsDict = spark.sparkContext.broadcast(stopWordsMap)


        val baseData = spark.read.parquet(inputPath).where(TagsUtils.hasSomeUserIdCondition)


        //读取日志parquet文件
        val uv: RDD[(Long, (ListBuffer[String], List[(String, Int)]))] = baseData.mapPartitions(par => {
            val listBuffer = new collection.mutable.ListBuffer[(Long, (ListBuffer[String], List[(String, Int)]))]()

            par.foreach(row => {
                //行数据进行标签化处理
                val ads = Tags4Ads.makeTags(row)
                val apps = Tags4App.makeTags(row, broadcastAppDict.value)
                val devices = Tags4device.makeTags(row)
                val keywords = Tags4App.makeTags(row, broadcastStopWordsDict.value)

                val allUserId = TagsUtils.getAllUserId(row)
                val tags = (ads ++ apps ++ devices ++ keywords).toList

                //点的集合数据
                listBuffer.append((allUserId(0).hashCode.toLong, (allUserId, tags)))
                listBuffer
            })
            listBuffer.iterator
        }).rdd


        //边集合
        val ue: RDD[Edge[Int]] = baseData.flatMap(row => {
            val allUserId = TagsUtils.getAllUserId(row)
            allUserId.map(uId => Edge(allUserId(0).hashCode.toLong, uId.hashCode.toLong, 0))
        }).rdd

        //创建一个图
        val graph = Graph(uv, ue)
        val cc = graph.connectedComponents().vertices

        cc.join(uv).map{
            case (xxId, (cmId, (uIds, tags))) => (cmId, (uIds, tags))
        }.reduceByKey{
            case (a,b) =>{
                val uIds = a._1 ++ b._1
                val tags = (a._2 ++ b._2).groupBy(_._1).mapValues(_.foldLeft(0)(_ + _._2 )).toList
                (uIds.distinct,tags)
            }
        }.saveAsTextFile(outputPath)

        spark.stop()

    }
}

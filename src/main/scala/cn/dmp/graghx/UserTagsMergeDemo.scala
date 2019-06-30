package cn.dmp.graghx

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object UserTagsMergeDemo {
    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf()
        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
        sparkConf.setMaster("local[*]")
        // RDD 序列化到磁盘 worker与worker之间的数据传输
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val sc = new SparkContext(sparkConf)

        val data: RDD[Array[String]] = sc.textFile(args(0)).map(_.split("\t"))


        //根据数据创建出点的集合
        val uv: RDD[(Long, (String, List[(String,Int)]))] = data.flatMap(arr => {
            //区分人名和标签
            val userNames = arr.filter(_.indexOf(":") == -1)
//            val userTags = arr.filter(_.indexOf(":") != -1).toList

            val userTags = arr.filter(_.indexOf(":") != -1).map(kvs => {
                val kv = kvs.split(":")
                (kv(0), kv(1).toInt)
            }).toList

            userNames.map(name => {
                //解决标签太多，所以只记录第一个
                if (name.equals(userNames(0)))(name.hashCode.toLong, (name, userTags))
                else (name.hashCode.toLong, (name,List.empty[(String, Int)]))
            })
        })
        //根据数据创建出边的集合
        val ue: RDD[Edge[Int]] = data.flatMap(arr => {
            val userNames = arr.filter(_.indexOf(":") == -1)
            userNames.map(name => Edge(userNames(0).hashCode.toLong, name.hashCode.toLong, 0))
        })

        //创建一个图
        val graph = Graph(uv, ue)
        val cc = graph.connectedComponents().vertices       //(id,共同的最小定点id)

        //聚合数据
        cc.join(uv).map{
            case(id,(cmId, (name, tags ))) => (cmId, (Seq(name), tags))
        }.reduceByKey{
            case(t1, t2) => {
                ((t1._1 ++ t2._1), (t1._2 ++ t2._2).groupBy(_._1).mapValues(_.foldLeft(0)(_+_._2)).toList  )
            }
        }.map(t => (t._2._1.toSet,t._2._2)).foreach(println)


        sc.stop()
    }

}

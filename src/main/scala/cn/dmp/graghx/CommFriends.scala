package cn.dmp.graghx

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CommFriends {
    def main(args: Array[String]): Unit = {
        // 2 创建sparkconf->sparkContext
        val sparkConf = new SparkConf()
        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
        sparkConf.setMaster("local[*]")
        // RDD 序列化到磁盘 worker与worker之间的数据传输
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val sc = new SparkContext(sparkConf)

        //点
        val uv: RDD[(VertexId,(String,Int))]= sc.parallelize(Seq(
            (1.toLong, ("和川", 23)),
            (2.toLong, ("汤佳树", 22)),
            (6.toLong, ("晴天", 24)),
            (9.toLong, ("杨洋", 18)),
            (133.toLong, ("罗志祥", 33)),

            (16.toLong, ("王军", 23)),
            (21.toLong, ("黄磊", 25)),
            (44.toLong, ("易烊千玺", 20)),
            (138.toLong, ("何炅", 31)),

            (5.toLong, ("杨颖", 28)),
            (7.toLong, ("迪丽热巴", 19)),
            (158.toLong, ("欧阳娜娜", 26))
        ))

        //边
        val ue: RDD[Edge[Int]] = sc.parallelize(Seq(
            Edge(1, 133, 0),
            Edge(2, 133, 0),
            Edge(9, 133, 0),
            Edge(6, 133, 0),

            Edge(6, 138, 0),
            Edge(16, 138, 0),
            Edge(44, 138, 0),
            Edge(21, 138, 0),

            Edge(5, 158, 0),
            Edge(7, 158, 0)

        ))

        val graph = Graph(uv,ue)
        //连通图

        val commonV = graph.connectedComponents().vertices
//        commonV.map(t => (t._2, List(t._1) )).reduceByKey(_ ++ _).foreach(println)
        /**
          * uv --> (userid,(姓名,年龄))
          * commonV -->(userId.共同的定点) on
          */
        uv.join(commonV).map{
            case (userId, ((name, age), cmId)) => (cmId, List((name,age)) )
        }.reduceByKey(_ ++ _).foreach(println)

        sc.stop()
    }
}

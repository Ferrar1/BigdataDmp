package cn.dmp.tags


import cn.dmp.utils.{JedisPools, RptUtils, TagsUtils}
import com.typesafe.config.ConfigFactory
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object Tags4Ctx {
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


        // 2 创建sparkconf->sparkContext
//        val sparkConf = new SparkConf()
//        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
//        sparkConf.setMaster("local[*]")
//        // RDD 序列化到磁盘 worker与worker之间的数据传输
//        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//
//        val sc = new SparkContext(sparkConf)
//        val sQLContext = new SQLContext(sc)


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

        val load = ConfigFactory.load()
        val hbTableName = load.getString("hbase.table.name")
        
        //  判断hbase中的表是否存在，如果不存在则创建
        val conf = HBaseConfiguration.create()
        //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
        conf.set("hbase.zookeeper.quorum",load.getString("hbase.zookeeper.host"))
        //conf.set(TableInputFormat.INPUT_TABLE, hbTableName)
        // val hbaseAdmin = new HBaseAdmin(hbaseConf)

        val hbConn = ConnectionFactory.createConnection(conf)
        val hbAdmin = hbConn.getAdmin

        if (hbAdmin.tableExists(TableName.valueOf(hbTableName))){
            println(s"$hbTableName 不存在...")
            println(s"正在创建$hbTableName ...")

            val tableDescrptor = new HTableDescriptor(TableName.valueOf(hbTableName))
            val columnDescriptor = new HColumnDescriptor("cf")
            tableDescrptor.addFamily(columnDescriptor)
            hbAdmin.createTable(tableDescrptor)

            //释放连接
            hbAdmin.close()
            hbConn.close()
        }

        //指定key的输出类型
        val jobConf = new JobConf(conf)
        jobConf.setOutputFormat(classOf[TableOutputFormat])
        //指定表的名称
        jobConf.set(TableOutputFormat.OUTPUT_TABLE,hbTableName)


        //读取日志parquet文件
        spark.read.parquet(inputPath).where(TagsUtils.hasSomeUserIdCondition).
                mapPartitions(par => {
            val jedis = JedisPools.getJedis()
            val listBuffer = new collection.mutable.ListBuffer[(String,List[(String,Int)])]()

            par.foreach(row => {
                //行数据进行标签化处理
                val ads = Tags4Ads.makeTags(row)
                val apps = Tags4App.makeTags(row, broadcastAppDict.value)
                val devices = Tags4device.makeTags(row)
                val keywords = Tags4App.makeTags(row, broadcastStopWordsDict.value)

                //商圈的的标签
                val business = Tags4business.makeTags(row, jedis)
                val allUserId = TagsUtils.getAllUserId(row)

                //（mac,0）,(idfa,0)
                val otherUserId = allUserId.slice(1,allUserId.length).map(uId => (uId,0)).toMap

                listBuffer.append((allUserId(0), (ads ++ apps ++ devices ++ keywords ++ business ++ otherUserId).toList))
                listBuffer
            })

            jedis.close()
            listBuffer.iterator
        }).rdd.reduceByKey((a,b) => {
            //List(("电视剧",1),("电视剧",1),("爱奇艺",1)) => groupBy => Map["K电视剧"，list(("电视剧",1),("电视剧",1))]
            //(a ++ b).groupBy(_._1).mapValues(_.foldLeft(0)(_ + _._2)).toList
            (a ++ b).groupBy(_._1).map{
                case (k,sameTags) => (k,sameTags.map(_._2).sum)
            }.toList

        })
//                .map{
//            case (userId, userTags) => {
//                val put = new Put(Bytes.toBytes(userId ))
//                val tags = userTags.map(t => t._1 +":"+ t._2).mkString(",")
//                put.addColumn(Bytes.toBytes("cf") ,Bytes.toBytes(s"day$day"), Bytes.toBytes(tags))
//
//                (new ImmutableBytesWritable(),put)  //ImmutableBytesWritable => rowkey
//            }
//        }.saveAsHadoopDataset(jobConf)
                .saveAsTextFile(outputPath)





        spark.stop()

    }
}

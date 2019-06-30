//package students.hechuan
//
//import scala.collection.mutable
//import scala.io.{BufferedSource, Source}
//
//object RptUtil {
//
//  def main(args: Array[String]): Unit = {
//    val data: mutable.Map[String, String] = ReadDictData("D:\\Program Files\\feiq\\Recv Files\\广告DMP项目\\appdict.txt")
//    println(data.take(2).toBuffer)
//  }
//
//  def ReadDictData(dictPath:String)={
//    //读取字典文件
//    val dataMap = new mutable.HashMap[String,String]()
//    val bs: BufferedSource = Source.fromFile(dictPath)
//    val lines: Iterator[String] = bs.getLines()
//      for (line <- lines){
//      val appName = line.split("[\t]")(1)
//      val appId = line.split("[\t]")(4)
//      dataMap.put(appId, appName)
//      }
//    bs.close()
//    dataMap
//  }
//
//  def GMFiledsCal(split:Array[String])={
//    var primitiveRequest: Int = 0
//    var effectiveRequest: Int = 0
//    var adRequest: Int = 0
//    var pcpBidding: Int = 0
//    var biddingSuccess: Int = 0
//    var impression: Int = 0
//    var clicks: Int = 0
//    var dspAdConsumer: Double = 0.0
//    var dspAdCost: Double = 0.0
//    val requestmode: Int = MyUtils.str2Int(split(8))
//    val processnode = MyUtils.str2Int(split(35))
//    val iseffective = MyUtils.str2Int(split(30))
//    val isbilling = MyUtils.str2Int(split(31))
//    val isbid = MyUtils.str2Int(split(39))
//    val adorderid = MyUtils.str2Int(split(2))
//    val iswin = MyUtils.str2Int(split(42))
//    val winprice = MyUtils.str2Double(split(41))
//    val adpayment = MyUtils.str2Double(split(75))
//
//    val provincename = split(24)
//    val cityname = split(25)
//    //1.原始请求
//    if (requestmode == 1 && processnode >= 1) {
//      primitiveRequest += 1
//    }
//    //2.有效请求
//    if (requestmode == 1 && processnode >= 2) {
//      effectiveRequest += 1
//    }
//    //3.广告请求
//    if (requestmode == 1 && processnode == 3) {
//      adRequest += 1
//    }
//    //4.参与竞价数
//    if (iseffective == 1 && isbilling == 1 && isbid == 1 && adorderid != 0) {
//      pcpBidding += 1
//    }
//    //5.竞价成功数
//    //8.DSP广告消费
//    //9.DSP广告成本
//    if (iseffective == 1 && isbilling == 1 && iswin == 1) {
//      biddingSuccess += 1
//      dspAdConsumer += winprice/1000.0
//      dspAdCost += adpayment/1000.0
//    }
//    //6.展示量
//    if (requestmode == 2 && iseffective == 1) {
//      impression += 1
//    }
//    //7.点击量
//    if (requestmode == 3 && iseffective == 1) {
//      clicks += 1
//    }
//    val zp = ZoneRpt(provincename,cityname,primitiveRequest, effectiveRequest, adRequest, pcpBidding, biddingSuccess, impression, clicks, dspAdConsumer, dspAdCost)
//    (s"${provincename},${cityname}", zp)
//  }
//
//}

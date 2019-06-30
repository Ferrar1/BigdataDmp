package students.cuibin

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField}

object LogToParguet {
  val filename: String = "F:\\log.FINISH"
  val forname: String = "F:\\123"
  val schemaList = List(
    StructField("sessionid", StringType, true),
    StructField("advertisersid", IntegerType, true),
    StructField("adorderid", IntegerType, true),
    StructField("adcreativeid", IntegerType, true),
    StructField("adplatformproviderid", StringType, false),
    StructField("sdkversion", StringType, true),
    StructField("adplatformkey", StringType, true),
    StructField("putinmodeltype", IntegerType, true),
    StructField("requestmode", IntegerType, true),
    StructField("adprice", DoubleType, true),
    StructField("adppprice", DoubleType, true),
    StructField("requestdate", StringType, true),
    StructField("ip", StringType, true),
    StructField("appid", StringType, true),
    StructField("appname", StringType, true),
    StructField("uuid", StringType, true),
    StructField("device", StringType, true),
    StructField("client", IntegerType, true),
    StructField("osversion", StringType, true),
    StructField("density", StringType, true),
    StructField("pw", IntegerType, true),
    StructField("ph", IntegerType, true),
    StructField("long", StringType, true),
    StructField("lat", StringType, true),
    StructField("provincename", StringType, true),
    StructField("cityname", StringType, true),
    StructField("ispid", IntegerType, true),
    StructField("ispname", StringType, true),
    StructField("networkmannerid", IntegerType, true),
    StructField("networkmannername", StringType, true),
    StructField("iseffective", IntegerType, true),
    StructField("isbilling", IntegerType, true),
    StructField("adspacetype", IntegerType, true),
    StructField("adspacetypename", StringType, true),
    StructField("devicetype", IntegerType, true),
    StructField("processnode", IntegerType, true),
    StructField("apptype", IntegerType, true),
    StructField("district", StringType, true),
    StructField("paymode", IntegerType, true),
    StructField("isbid", IntegerType, true),
    StructField("bidprice", DoubleType, true),
    StructField("winprice", DoubleType, true),
    StructField("iswin", IntegerType, true),
    StructField("cur", StringType, true),
    StructField("rate", DoubleType, true),
    StructField("cnywinprice", DoubleType, true),
    StructField("imei", StringType, true),
    StructField("mac", StringType, true),
    StructField("idfa", StringType, true),
    StructField("openudid", StringType, true),
    StructField("androidid", StringType, true),
    StructField("rtbprovince", StringType, true),
    StructField("rtbcity", StringType, true),
    StructField("rtbdistrict", StringType, true),
    StructField("rtbstreet", StringType, true),
    StructField("storeurl", StringType, true),
    StructField("realip", StringType, true),
    StructField("isqualityapp", IntegerType, true),
    StructField("bidfloor", DoubleType, true),
    StructField("aw", IntegerType, true),
    StructField("ah", IntegerType, true),
    StructField("imeimd5", StringType, true),
    StructField("macmd5", StringType, true),
    StructField("idfamd5", StringType, true),
    StructField("openudidmd5", StringType, true),
    StructField("androididmd5", StringType, true),
    StructField("imeisha1", StringType, true),
    StructField("macsha1", StringType, true),
    StructField("idfasha1", StringType, true),
    StructField("openudidsha1", StringType, true),
    StructField("androididsha1", StringType, true),
    StructField("uuidunknow", StringType, true),
    StructField("userid", StringType, true),
    StructField("iptype", IntegerType, true),
    StructField("initbidprice", DoubleType, true),
    StructField("adpayment", DoubleType, true),
    StructField("agentrate", DoubleType, true),
    StructField("lomarkrate", DoubleType, true),
    StructField("adxrate", DoubleType, true),
    StructField("title", StringType, true),
    StructField("keywords", StringType, true),
    StructField("tagid", StringType, true),
    StructField("callbackdate", StringType, true),
    StructField("channelid", StringType, true),
    StructField("mediatype", IntegerType, true)
  )

  def PaseInt(s: String): Int = {
    if ("".equals(s.trim)) {
      0
    } else {
      s.toInt
    }
  }

  def PaseDouble(s: String): Double = {
    if ("".equals(s.trim)) {
      0.0
    } else {
      s.toDouble
    }
  }

  def arrToRow(split: Array[String]) = {
    val row = Row(
      split(0)
      , PaseInt(split(1))
      , PaseInt(split(2))
      , PaseInt(split(3))
      , split(4)
      , split(5)
      , split(6)
      , PaseInt(split(7))
      , PaseInt(split(8))
      , PaseDouble(split(9))
      , PaseDouble(split(10))
      , split(11)
      , split(12)
      , split(13)
      , split(14)
      , split(15)
      , split(16)
      , PaseInt(split(17))
      , split(18)
      , split(19)
      , PaseInt(split(20))
      , PaseInt(split(21))
      , split(22)
      , split(23)
      , split(24)
      , split(25)
      , PaseInt(split(26))
      , split(27)
      , PaseInt(split(28))
      , split(29)
      , PaseInt(split(30))
      , PaseInt(split(31))
      , PaseInt(split(32))
      , split(33)
      , PaseInt(split(34))
      , PaseInt(split(35))
      , PaseInt(split(36))
      , split(37)
      , PaseInt(split(38))
      , PaseInt(split(39))
      , PaseDouble(split(40))
      , PaseDouble(split(41))
      , PaseInt(split(42))
      , split(43)
      , PaseDouble(split(44))
      , PaseDouble(split(45))
      , split(46)
      , split(47)
      , split(48)
      , split(49)
      , split(50)
      , split(51)
      , split(52)
      , split(53)
      , split(54)
      , split(55)
      , split(56)
      , PaseInt(split(57))
      , PaseDouble(split(58))
      , PaseInt(split(59))
      , PaseInt(split(60))
      , split(61)
      , split(62)
      , split(63)
      , split(64)
      , split(65)
      , split(66)
      , split(67)
      , split(68)
      , split(69)
      , split(70)
      , split(71)
      , split(72)
      , PaseInt(split(73))
      , PaseDouble(split(74))
      , PaseDouble(split(75))
      , PaseDouble(split(76))
      , PaseDouble(split(77))
      , PaseDouble(split(78))
      , split(79)
      , split(80)
      , split(81)
      , split(82)
      , split(83)
      , PaseInt(split(84)))
    row
  }
}

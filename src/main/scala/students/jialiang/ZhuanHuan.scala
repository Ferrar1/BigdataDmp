//package students.jialiang
//
//import java.util.Date
//
//
//class ZhuanHuan {
//    var sessionid: String = _ // 会话标识
//    var advertisersid: Int = _ // 广告主id
//    var adorderid: Int = _ // 广告id
//    var adcreativeid: Int = _ // 广告创意id ( >= 200000 : dsp)
//    var adplatformproviderid: Int = _ // 广告平台商id (>= 100000: rtb)
//    var sdkversion: String = _ // sdk 版本号
//    var adplatformkey: String = _ // 平台商key
//    var putinmodeltype: Int = _ // 针对广告主的投放模式 = _   //1：展示量投放2：点击量投放
//    var requestmode: Int = _ // 数据请求方式（1:请求、2:展示、3:点击）
//    var adprice: Double = _ // 广告价格
//    var adppprice: Double = _ // 平台商价格
//    var requestdate: Date = _ // 请求时间 = _   //格式为：yyyy-m-dd hh:mm:ss
//    var ip: String = _ // 设备用户的真实ip 地址
//    var appid: String = _ // 应用id
//    var appname: String = _ // 应用名称
//    var uuid: String = _ // 设备唯一标识
//    var device: String = _ // 设备型号，如htc、iphone
//    var client: Int = _ // 设备类型（1：android 2：ios 3：wp）
//    var osversion: String = _ // 设备操作系统版本
//    var density: String = _ // 设备屏幕的密度
//    var pw: Int = _ // 设备屏幕宽度
//    var ph: Int = _ // 设备屏幕高度
//    var long: String = _ // 设备所在经度
//    var lat: String = _ // 设备所在纬度
//    var provincename: String = _ // 设备所在省份名称
//    var cityname: String = _ // 设备所在城市名称
//    var ispid: Int = _ // 运营商id
//    var ispname: String = _ // 运营商名称
//    var networkmannerid: Int = _ // 联网方式id
//    var networkmannername: String = _ //联网方式名称
//    var iseffective: Int = _ // 有效标识（有效指可以正常计费的）(0：无效1：有效
//    var isbilling: Int = _ // 是否收费（0：未收费1：已收费）
//    var adspacetype: Int = _ // 广告位类型（1：banner 2：插屏3：全屏）
//    var adspacetypename: String = _ // 广告位类型名称（banner、插屏、全屏）
//    var devicetype: Int = _ // 设备类型（1：手机2：平板）
//    var processnode: Int = _ // 流程节点（1：请求量kpi 2：有效请求3：广告请求）
//    var apptype: Int = _ // 应用类型id
//    var district: String = _ // 设备所在县名称
//    var paymode: Int = _ // 针对平台商的支付模式，1：展示量投放(CPM) 2：点击
//    var isbid: Int = _ // 是否rtb
//    var bidprice: Double = _ // rtb 竞价价格
//    var winprice: Double = _ // rtb 竞价成功价格
//    var iswin: Int = _ // 是否竞价成功
//    var cur: String = _ // values:usd|rmb 等
//    var rate: Double = _ // 汇率
//    var cnywinprice: Double = _ // rtb 竞价成功转换成人民币的价格
//    var imei: String = _ // imei
//    var mac: String = _ // mac
//    var idfa: String = _ // idfa
//    var openudid: String = _ // openudid
//    var androidid: String = _ // androidid
//    var rtbprovince: String = _ //rtb 省
//    var rtbcity: String = _ //rtb 市
//    var rtbdistrict: String = _ //rtb 区
//    var rtbstreet: String = _ //rtb 街道
//    var storeurl: String = _ //app 的市场下载地址
//    var realip: String = _ //真实ip
//    var isqualityapp: Int = _ //优选标识
//    var bidfloor: Double = _ //底价
//    var aw: Int = _ //广告位的宽
//    var ah: Int = _ //广告位的高
//    var imeimd5: String = _ //imei_md5
//    var macmd5: String = _ //mac_md5
//    var idfamd5: String = _ //idfa_md5
//    var openudidmd5: String = _ //openudid_md5
//    var androididmd5: String = _ //androidid_md5
//    var imeisha1: String = _ //imei_sha1
//    var macsha1: String = _ //mac_sha1
//    var idfasha1: String = _ //idfa_sha1
//    var openudidsha1: String = _ //openudid_sha1
//    var androididsha1: String = _ //androidid_sha1
//    var uuidunknow: String = _ //uuid_unknow tanx 密文
//
//    var userid: String = _ //平台用户id
//    var iptype: Int = _ //表示ip 类型
//    var initbidprice: Double = _ //初始出价
//    var adpayment: Double = _ //转换后的广告消费
//    var agentrate: Double = _ //代理商利润率
//    var lomarkrate: Double = _ //代理利润率
//    var adxrate: Double = _ //媒介利润率
//    var title: String = _ //标题
//    var keywords: String = _ //关键字
//    var tagid: String = _ //广告位标识(当视频流量时值为视频ID 号)
//    var callbackdate: String = _ //回调时间格式为:YYYY/mm/dd hh:mm:ss
//    var channelid: String = _ //频道ID
//    var mediatype: Int = _ //媒体类型：1 长尾媒体2 视频媒体3 独立媒体默认:1
//
//
//    def bz2par(line: String): ZhuanHuan = {
//
//        val strings = line.split(",")
//        if (strings.length != 85) {
//            return null
//        }
//        val zhuanhuan = new ZhuanHuan()
//        zhuanhuan.sessionid = strings(0)
//        if (StringUtil.isNotBlank(strings(1))) {
//            zhuanhuan.advertisersid = strings(1).toInt
//        }
//        if (StringUtil.isNotBlank(strings(2))) {
//            zhuanhuan.adorderid = strings(2).toInt
//        }
//        if (StringUtil.isNotBlank(strings(3))) {
//            zhuanhuan.adcreativeid = strings(3).toInt
//        }
//        if (StringUtil.isNotBlank(strings(4))) {
//            zhuanhuan.adplatformproviderid = strings(4).toInt
//        }
//        zhuanhuan.sdkversion = strings(5)
//        zhuanhuan.adplatformkey = strings(6)
//        if (StringUtil.isNotBlank(strings(7))) {
//            zhuanhuan.putinmodeltype = strings(7).toInt
//        }
//        if (StringUtil.isNotBlank(strings(8))) {
//            zhuanhuan.requestmode = strings(8).toInt
//        }
//        if (StringUtil.isNotBlank(strings(9))) {
//            zhuanhuan.adprice = strings(9).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(10))) {
//            zhuanhuan.adppprice = strings(10).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(11))) {
//            zhuanhuan.requestdate = DateUtil.toDate(strings(11), "yyyy-m-dd hh:mm:ss")
//        }
//        zhuanhuan.ip = strings(12)
//        zhuanhuan.appid = strings(13)
//        zhuanhuan.appname = strings(14)
//        zhuanhuan.uuid = strings(15)
//        zhuanhuan.device = strings(16)
//        if (StringUtil.isNotBlank(strings(17))) {
//            zhuanhuan.client = strings(17).toInt
//        }
//        zhuanhuan.osversion = strings(18)
//        zhuanhuan.density = strings(19)
//        if (StringUtil.isNotBlank(strings(20))) {
//            zhuanhuan.pw = strings(20).toInt
//        }
//        if (StringUtil.isNotBlank(strings(21))) {
//            zhuanhuan.ph = strings(21).toInt
//        }
//        zhuanhuan.long = strings(22)
//        zhuanhuan.lat = strings(23)
//        zhuanhuan.provincename = strings(24)
//        zhuanhuan.cityname = strings(25)
//        if (StringUtil.isNotBlank(strings(26))) {
//            zhuanhuan.ispid = strings(26).toInt
//        }
//        zhuanhuan.ispname = strings(27)
//        if (StringUtil.isNotBlank(strings(28))) {
//            zhuanhuan.networkmannerid = strings(28).toInt
//        }
//        zhuanhuan.networkmannername = strings(29)
//        if (StringUtil.isNotBlank(strings(30))) {
//            zhuanhuan.iseffective = strings(30).toInt
//        }
//        if (StringUtil.isNotBlank(strings(31))) {
//
//            zhuanhuan.isbilling = strings(31).toInt
//        }
//        if (StringUtil.isNotBlank(strings(32))) {
//
//            zhuanhuan.adspacetype = strings(32).toInt
//        }
//        if (StringUtil.isNotBlank(strings(30))) {
//            zhuanhuan.iseffective = strings(30).toInt
//        }
//        if (StringUtil.isNotBlank(strings(31))) {
//
//            zhuanhuan.isbilling = strings(31).toInt
//        }
//        if (StringUtil.isNotBlank(strings(32))) {
//
//            zhuanhuan.adspacetype = strings(32).toInt
//        }
//        zhuanhuan.adspacetypename = strings(33)
//        if (StringUtil.isNotBlank(strings(34))) {
//            zhuanhuan.devicetype = strings(34).toInt
//        }
//        if (StringUtil.isNotBlank(strings(35))) {
//
//            zhuanhuan.processnode = strings(35).toInt
//        }
//        if (StringUtil.isNotBlank(strings(36))) {
//
//            zhuanhuan.apptype = strings(36).toInt
//        }
//        zhuanhuan.district = strings(37)
//        if (StringUtil.isNotBlank(strings(38))) {
//            zhuanhuan.paymode = strings(38).toInt
//        }
//        if (StringUtil.isNotBlank(strings(39))) {
//
//            zhuanhuan.isbid = strings(39).toInt
//        }
//        if (StringUtil.isNotBlank(strings(40))) {
//
//            zhuanhuan.bidprice = strings(40).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(41))) {
//
//            zhuanhuan.winprice = strings(41).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(42))) {
//
//            zhuanhuan.iswin = strings(42).toInt
//        }
//
//        zhuanhuan.cur = strings(43)
//        if (StringUtil.isNotBlank(strings(44))) {
//            zhuanhuan.rate = strings(44).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(45))) {
//            zhuanhuan.cnywinprice = strings(45).toDouble
//        }
//        zhuanhuan.imei = strings(46)
//        zhuanhuan.mac = strings(47)
//        zhuanhuan.idfa = strings(48)
//        zhuanhuan.openudid = strings(49)
//        zhuanhuan.androidid = strings(50)
//        zhuanhuan.rtbprovince = strings(51)
//        zhuanhuan.rtbcity = strings(52)
//        zhuanhuan.rtbdistrict = strings(53)
//        zhuanhuan.rtbstreet = strings(54)
//        zhuanhuan.storeurl = strings(55)
//        zhuanhuan.realip = strings(56)
//        if (StringUtil.isNotBlank(strings(57))) {
//            zhuanhuan.isqualityapp = strings(57).toInt
//        }
//        if (StringUtil.isNotBlank(strings(58))) {
//            zhuanhuan.bidfloor = strings(58).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(59))) {
//            zhuanhuan.aw = strings(59).toInt
//        }
//        if (StringUtil.isNotBlank(strings(60))) {
//            zhuanhuan.ah = strings(60).toInt
//        }
//        zhuanhuan.imeimd5 = strings(61)
//        zhuanhuan.macmd5 = strings(62)
//        zhuanhuan.idfamd5 = strings(63)
//        zhuanhuan.openudidmd5 = strings(64)
//        zhuanhuan.androididmd5 = strings(65)
//        zhuanhuan.imeisha1 = strings(66)
//        zhuanhuan.macsha1 = strings(67)
//        zhuanhuan.idfasha1 = strings(68)
//        zhuanhuan.openudidsha1 = strings(69)
//        zhuanhuan.androididsha1 = strings(70)
//        zhuanhuan.uuidunknow = strings(71)
//        zhuanhuan.userid = strings(72)
//        if (StringUtil.isNotBlank(strings(73))) {
//            zhuanhuan.iptype = strings(73).toInt
//        }
//        if (StringUtil.isNotBlank(strings(74))) {
//            zhuanhuan.initbidprice = strings(74).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(75))) {
//            zhuanhuan.adpayment = strings(75).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(76))) {
//            zhuanhuan.agentrate = strings(76).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(77))) {
//            zhuanhuan.lomarkrate = strings(77).toDouble
//        }
//        if (StringUtil.isNotBlank(strings(78))) {
//            zhuanhuan.adxrate = strings(78).toDouble
//        }
//        zhuanhuan.title = strings(79)
//        zhuanhuan.keywords = strings(80)
//        zhuanhuan.tagid = strings(81)
//        zhuanhuan.callbackdate = strings(82)
//        zhuanhuan.channelid = strings(83)
//        if (StringUtil.isNotBlank(strings(84))) {
//            zhuanhuan.mediatype = strings(84).toInt
//        }
//
//        zhuanhuan
//
//    }
//
//
//    override def toString = s"log(sessionid=$sessionid, advertisersid=$advertisersid, adorderid=$adorderid, adcreativeid=$adcreativeid, adplatformproviderid=$adplatformproviderid, sdkversion=$sdkversion, adplatformkey=$adplatformkey, putinmodeltype=$putinmodeltype, requestmode=$requestmode, adprice=$adprice, adppprice=$adppprice, requestdate=$requestdate, ip=$ip, appid=$appid, appname=$appname, uuid=$uuid, device=$device, client=$client, osversion=$osversion, density=$density, pw=$pw, ph=$ph, long=$long, lat=$lat, provincename=$provincename, cityname=$cityname, ispid=$ispid, ispname=$ispname, networkmannerid=$networkmannerid, networkmannername=$networkmannername, iseffective=$iseffective, isbilling=$isbilling, adspacetype=$adspacetype, adspacetypename=$adspacetypename, devicetype=$devicetype, processnode=$processnode, apptype=$apptype, district=$district, paymode=$paymode, isbid=$isbid, bidprice=$bidprice, winprice=$winprice, iswin=$iswin, cur=$cur, rate=$rate, cnywinprice=$cnywinprice, imei=$imei, mac=$mac, idfa=$idfa, openudid=$openudid, androidid=$androidid, rtbprovince=$rtbprovince, rtbcity=$rtbcity, rtbdistrict=$rtbdistrict, rtbstreet=$rtbstreet, storeurl=$storeurl, realip=$realip, isqualityapp=$isqualityapp, bidfloor=$bidfloor, aw=$aw, ah=$ah, imeimd5=$imeimd5, macmd5=$macmd5, idfamd5=$idfamd5, openudidmd5=$openudidmd5, androididmd5=$androididmd5, imeisha1=$imeisha1, macsha1=$macsha1, idfasha1=$idfasha1, openudidsha1=$openudidsha1, androididsha1=$androididsha1, uuidunknow=$uuidunknow, userid=$userid, iptype=$iptype, initbidprice=$initbidprice, adpayment=$adpayment, agentrate=$agentrate, lomarkrate=$lomarkrate, adxrate=$adxrate, title=$title, keywords=$keywords, tagid=$tagid, callbackdate=$callbackdate, channelid=$channelid, mediatype=$mediatype)"
//}

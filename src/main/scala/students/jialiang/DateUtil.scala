//package students.jialiang
//
//import java.util.Date
//
//import org.apache.commons.lang.StringUtils
//import org.joda.time.format.DateTimeFormat
//
//object DateUtil {
//  val MAIL_FULL_DATE = "yyyyMMddHHmmss"
//  val MAIL_FULL_DATE2 = "yyyy-MM-dd HH:mm:ss"
//  def toDate(dateStr: String, patten: String): Date = {
//    var date: Date = null
//    if (StringUtils.isNotBlank(dateStr)) {
//      date = DateTimeFormat.forPattern(patten).parseDateTime(dateStr).toDate
//    }
//    date
//  }
//}

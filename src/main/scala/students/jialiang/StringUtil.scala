package students.jialiang

object  StringUtil {
  def isBlank(str: String): Boolean ={
    var flag = false
    if (str == null || str == None || str.trim.length <= 0 || str.trim == "" || str.trim.toUpperCase == "NULL") flag = true
    flag
  }

  def isNotBlank(str: String): Boolean ={
    !isBlank(str)
  }

  def assembly(strs: String*) = {
    var s = ""
    strs.foreach(str => {
      s = s + str
    })
    s
  }

}

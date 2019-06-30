package cn.dmp.tags

trait Tags {
    /**
      *  打标签的方法定义
      * @param args
      * @return
      */
    def makeTags(args:Any*):Map[String,Int]
}

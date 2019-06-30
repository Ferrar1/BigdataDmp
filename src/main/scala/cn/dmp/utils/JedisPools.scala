package cn.dmp.utils

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

object JedisPools {

    //默认存储的是0号库
    // private val jedisPool = new JedisPool(new GenericObjectPoolConfig(), "10.172.50.54", 6379)

    //存储到8号库
    private val jedisPool = new JedisPool(new GenericObjectPoolConfig(), "10.172.50.54", 6379,3000,null,8)


    def getJedis() = jedisPool.getResource

}

package com.itheima.mm.utils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.ResourceBundle;

/**
 * @author ：
 * @date ：
 * @description ：Jedis工具类
 * @version: 1.0
 */
@Slf4j
public class JedisUtils {
	//连接池和连接池配置
	private static JedisPoolConfig poolConfig = null;
	private static JedisPool jedisPool = null;
	private static Integer maxTotal = null;
	private static Integer maxIdle = null;
	private static String host = null;
	private static Integer port = null;

	public static void init(ResourceBundle rb){
		//读取配置文件 获得参数值
		//ResourceBundle rb = ResourceBundle.getBundle("jedis");
		maxTotal = Integer.parseInt(rb.getString("jedis.maxTotal"));
		maxIdle = Integer.parseInt(rb.getString("jedis.maxIdle"));
		port = Integer.parseInt(rb.getString("jedis.port"));
		host = rb.getString("jedis.host");
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxIdle(maxIdle);
		jedisPool = new JedisPool(poolConfig,host,port);
		log.info("init jedis resource,host:{},port:{}",host,port);
	}

	//判断redis是否可用
	public static boolean isUsed(){
		try{
			//先判断连接池是否存在
			if(jedisPool == null){
				return false;
			}
			Jedis jedis = jedisPool.getResource();
			//再判断是否能获取连接
			if(jedis == null){
				return  false;
			}
			jedis.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static Jedis getResource(){
		return jedisPool.getResource();
	}
}

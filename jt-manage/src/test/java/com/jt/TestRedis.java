package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

/**
 *  该测试类，测试redis全部操作方法
 * @author TY
 *  项目打包时会将测试类测试成功后再打包
 * 
 * 	报错：1.检查防火墙
 * 			2.redis-server redis.conf
 * 			3.配置文件修改问题  3处
 *
 */
public class TestRedis {
	@Test
	public void testString() {
		String host = "192.168.83.129"; // ip地址
		int port = 6379;				// 端口号
		Jedis jedis = new Jedis(host, port);
		jedis.set("1705", "redis学习");
		System.out.println("获取redis的数据：" + jedis.get("1705"));
	}
	
	/**
	 *  为参数设定超时的时间
	 *  
	 */
	@Test
	public void testStringTime() {
		String host = "192.168.83.129"; // ip地址
		int port = 6379;				// 端口号
		Jedis jedis = new Jedis(host, port);
		// jedis.set("1705", "redis学习");
		// 表示数据存活10秒
		// jedis.expire("1705", 10);
		jedis.setex("1705", 10, "redis学习");
		System.out.println("获取redis的数据：" + jedis.get("1705"));
	}
	
	/**
	 * 需求：
	 * 	如果redis中已经存在这个key，则不允许修改
	 * 用法： 当作标识  开关 boolean flag = true/false
	 */
	@Test
	public void testString_nx() {
		String host = "192.168.83.129"; // ip地址
		int port = 6379;				// 端口号
		Jedis jedis = new Jedis(host, port);
//		String value = jedis.get("1705");
//		if (value==null || value.length()==0) {
//			jedis.set("1705", "快毕业了");	
//		}
		Long f = jedis.setnx("1705", "快毕业了");
		Long flag = jedis.setnx("1705", "出任CEO了");
		System.out.println("redis中的数据：" + jedis.get("1705"));
		System.out.println("f:" + f +"-----------flag:" + flag);	
	}
	
	/**
	 *  不允许修改数据，同时设定超时时间
	 *  分布式锁：同步锁 LOK锁 数据库锁 redis锁 zookeeper锁
	 */
	@Test
	public void testString_ex_nx() {
		String host = "192.168.83.129"; // ip地址
		int port = 6379;				// 端口号
		Jedis jedis = new Jedis(host, port);
		jedis.set("1905", "666", "NX", "EX", 30);
		System.out.println("操作成功");
	}
	
	private Jedis jedis;
	@Before  // 当执行@test方法时，先执行该方法
	public void init() {
		String host = "192.168.83.129"; // ip地址
		int port = 6379;				// 端口号
		jedis = new Jedis(host, port);
	}
	@Test
	public void testHash() {
		jedis.hset("user", "id", "101");
		jedis.hset("user", "name", "tomcat猫");
		System.out.println(jedis.hgetAll("user"));
	}
	/**
	 *  操作list类型
	 *  面试题
	 */
	@Test
	public void testList() {
		jedis.lpush("list", "1","2","3","4","5");
		System.out.println(jedis.rpop("list"));
	}
	/**
	 *  redis事务控制 AOP操作
	 */
	@Test
	public void testTx() {
		// 开启事务
		Transaction transaction = jedis.multi();
		try {
			transaction.set("aa", "aa");
			// 事务提交
			transaction.exec();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
		
	}
	
	/**
	 *  实现redis分片测试 6379/6380/6381
	 */
	@Test
	public void testShards() {
		String host = "192.168.83.129";
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, 6379));
		shards.add(new JedisShardInfo(host, 6380));
		shards.add(new JedisShardInfo(host, 6381));
		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("1905", "redis分片学习");
		System.out.println(jedis.get("1905"));
		jedis.close();
	}
	
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.83.129:26379");
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("1905", "哨兵随时待命");
		System.out.println(jedis.get("1905"));
		pool.close();
	}
	
	@Test
	public void testCluster() {
		String host = "192.168.83.129";
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort(host, 7000));
		nodes.add(new HostAndPort(host, 7001));
		nodes.add(new HostAndPort(host, 7002));
		nodes.add(new HostAndPort(host, 7003));
		nodes.add(new HostAndPort(host, 7004));
		nodes.add(new HostAndPort(host, 7005));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("1905", "为什么我追的剧都烂尾");
		System.out.println(jedisCluster.get("1905"));
	}

}

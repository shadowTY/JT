package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

@Configuration  // 标识配置类
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	@Value("${redis.nodes}")
	private String nodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		String[] nodes = this.nodes.split(",");
		for (String node : nodes) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			set.add(hostAndPort);	
		}
		return new JedisCluster(set);
	}
	
//	@Bean
//	public JedisSentinelPool jedisSentinelPool() {
//		Set<String> sentinels = new HashSet<>();
//		sentinels.add(nodes);
//		return new JedisSentinelPool("mymaster",sentinels);
//	}
//	
//	
//	@Bean
//	@Scope("prototype")
//	public Jedis jedis(JedisSentinelPool pool) {
//		return pool.getResource();
//	}
	
	
	/*
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private int port;
	 * 
	 * // 方法的返回值交给容器管理
	 * 
	 * @Bean
	 * 
	 * @Lazy // 懒加载 public Jedis jedis() { return new Jedis(host,port); }
	 */
	/*
	 * @Value("${redis.nodes}") private String nodes;
	 * 
	 * @Bean public ShardedJedis shardedJedis() { List<JedisShardInfo> shards = new
	 * ArrayList<JedisShardInfo>(); String[] node = nodes.split(","); for (String s
	 * : node) { String host = s.split(":")[0]; int port =
	 * Integer.parseInt(s.split(":")[1]); JedisShardInfo info = new
	 * JedisShardInfo(host, port); shards.add(info); } return new
	 * ShardedJedis(shards); }
	 */
}

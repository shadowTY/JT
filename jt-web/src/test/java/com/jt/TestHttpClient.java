package com.jt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private HttpClientService httpService;
	
	@Test
	public void testUtil() {
		String url = "https://item.jd.com/5236335.html?dist=jd";
		String result = httpService.doGet(url); 
		System.out.println(result);
	}
	
	@Test
	public void TestHttp() throws ClientProtocolException, IOException {
		String url = "https://item.jd.com/5236335.html?dist=jd";
		HttpPost httpHost = new HttpPost(url);
		CloseableHttpResponse response = httpClient.execute(httpHost);
		if (200 == response.getStatusLine().getStatusCode()) {
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		}
	}
	
	
	/**
	 *  测试httpClient
	 *   1. 实例化请求对象
	 *   2.确定url地址
	 *   3.定义请求对象
	 *   4.发起请求，获取响应结果
	 *   5.（200/404/406 服务器返回数据参数异常，
	 *   	500：后台服务器异常  400 客户端向服务器传递数据异常）
	 *   校验服务器信息，获取数据
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testHttpClient() throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "https://item.jd.com/5236335.html?dist=jd";
		HttpPost httpHost = new HttpPost(url);
		CloseableHttpResponse response = httpClient.execute(httpHost);
		if (200 == response.getStatusLine().getStatusCode()) {
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		}
	}

}

package com.jt.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

/**
 *  该类是封装HttpClient的工具API
 * @author TY
 *
 */

@Service
public class HttpClientService {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;
	
	/**
	 *  用户调用工具方法， 通过工具方法返回服务器数据（json串）给用户
	 *  
	 *  参数: url:用户通过工具api访问的网址
	 *  	params：利用map集合封装数据
	 *  	charset: 定义字符集编码
	 */
	
	public String doGet(String url,Map<String, String> params,String charset) {
		// 1. 校验字符集编码是否为空
		if (StringUtils.isEmpty(charset)) {
			charset = "UTF-8";
		}
		
		// 2. 校验参数是否为空
		if (params != null) {
			// 如果不为null，需要拼接字符串
			url += "?";
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				url += key + "=" + value + "&";
			}
			// 去除最后一个字符
			url = url.substring(0, url.length()-1);
		}
		
		// 3. 发起请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		String result = null;
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			// 校验用户请求是否正确
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(httpResponse.getEntity(),charset);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public String doGet(String url) {
		return doGet(url,null,null);
	}
	
	public String doGet(String url,Map<String, String> params) {
		return doGet(url,params,null);
	}

}

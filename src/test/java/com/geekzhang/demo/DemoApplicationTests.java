package com.geekzhang.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20170825.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void ss() throws Exception{
			//请替换成你自己的accessKeyId、accessKeySecret
			IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIBZSIcH7YPU3l", "gT1a5eTm99mpnIeBJnEvRXTitcrLOt");
			DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
			IAcsClient client = new DefaultAcsClient(profile);
			TextScanRequest textScanRequest = new TextScanRequest();
			textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
//			textScanRequest.setContentType(FormatType.JSON);
			textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
			textScanRequest.setEncoding("UTF-8");
			textScanRequest.setRegionId("cn-shanghai");
			List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
			Map<String, Object> task1 = new LinkedHashMap<String, Object>();
			task1.put("dataId", UUID.randomUUID().toString());
			task1.put("content", "你好");
			tasks.add(task1);
			JSONObject data = new JSONObject();
			/**
			 * 文本垃圾检测： antispam
			 * 关键词检测： keyword
			 **/
			data.put("scenes", Arrays.asList("antispam"));
			data.put("tasks", tasks);
			System.out.println(JSON.toJSONString(data, true));
			textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
			/**
			 * 请务必设置超时时间
			 */
			textScanRequest.setConnectTimeout(3000);
			textScanRequest.setReadTimeout(6000);
			try {
				HttpResponse httpResponse = client.doAction(textScanRequest);
				if(httpResponse.isSuccess()){
					JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
					System.out.println(JSON.toJSONString(scrResponse, true));
					if (200 == scrResponse.getInteger("code")) {
						JSONArray taskResults = scrResponse.getJSONArray("data");
						for (Object taskResult : taskResults) {
							if(200 == ((JSONObject)taskResult).getInteger("code")){
								JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
								for (Object sceneResult : sceneResults) {
									String scene = ((JSONObject)sceneResult).getString("scene");
									String suggestion = ((JSONObject)sceneResult).getString("suggestion");
									//根据scene和suggetion做相关的处理
									//do something
									System.out.println("args = [" + scene + "]");
									System.out.println("args = [" + suggestion + "]");
								}
							}else{
								System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
							}
						}
					} else {
						System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
					}
				}else{
					System.out.println("response not success. status:" + httpResponse.getStatus());
				}
			} catch (ClientException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

}

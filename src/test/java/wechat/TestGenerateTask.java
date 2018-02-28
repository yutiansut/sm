package wechat;

import cn.mdni.commons.http.HttpUtils;
import com.google.common.collect.Maps;

import java.util.Map;

public class TestGenerateTask {

	public static void main(String[] args) {
		String reqJson = "[{\"customerName\":\"张三\",\"customerMobile\":\"13111111111\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"官网\"},"
				+ "{\"customerName\":\"李四\",\"customerMobile\":\"13222222222\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"朋友圈\"},"
				+ "{\"customerName\":\"王五\",\"customerMobile\":\"13333333333\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"朋友圈\"},"
				+ "{\"customerName\":\"马六\",\"customerMobile\":\"13444444444\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"朋友圈\"},"
				+ "{\"customerName\":\"猴七\",\"customerMobile\":\"13555555555\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"朋友圈\"},"
				+ "{\"customerName\":\"鬼八\",\"customerMobile\":\"13666666666\","
				+ "\"callTime\":null,\"callAnswered\":null,\"store\":\"BJ\",\"promoteSource\":\"朋友圈\"}]";
		String url = "http://crmtest.mdni.net.cn/open/api/task/newTask";
		// String url = "http://localhost:14089/open/api/task/newTask";
		Map<String, Object> param = Maps.newHashMap();
		param.put("reqJson", reqJson);
		param.put("callId", "jiy789667_yhlpfaq3");
		String mess = HttpUtils.post(url, param);
		System.out.println(mess);
	}
}

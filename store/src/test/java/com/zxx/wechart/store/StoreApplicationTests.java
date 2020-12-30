package com.zxx.wechart.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxx.wechart.store.controller.UserController;
import com.zxx.wechart.store.domain.user.User;
import com.zxx.wechart.store.mapper.UserMapper;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.HttpUtil;
import com.zxx.wechart.store.utils.WexinUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StoreApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private UserMapper userMapper;



	@Test
	public void testQueryUser() {
		User user = userMapper.queryUserInfoByOpenId("232121");
		System.out.println("==============" + user.toString());
	}

	@Test
	public void testLogin() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		String content = "{\"code\":\"2323\"}";
		String responseString = mockMvc.perform(
				MockMvcRequestBuilders.post("/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		System.out.println("retult =====  "+responseString);
	}

	@Test
	public void testDownVoice() {
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=40_3g76rOfgchwPzOLC_x9KtB3jEFNJF-eyEt3uH5z0kGdC--gruJ05vpgexGw6c3huJ9HwIOapeOs7VsBLWk4ocqw-Zg9jWZDNH3twNCAp77RhJAs57lSeDEhSjMAOGF1UW4KZI6Xp2eAvf5QrFSShAEAOBB&media_id=HgnqAqlkPjUlupXh3XbnpZvaGzBJdEIEt6JWu-7QpzgwMPyq1JJhFRwtAXuyv25";
			WexinUtil.getFile(url, "F://");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

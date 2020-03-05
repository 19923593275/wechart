package com.zxx.wechart.store;

import com.zxx.wechart.store.common.CodeConstant;
import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.domain.about.Music;
import com.zxx.wechart.store.domain.user.User;
import com.zxx.wechart.store.mapper.UserMapper;
import com.zxx.wechart.store.service.AboutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class StoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AboutService aboutService;

	class EnjoyMusic {
		private int music_id;
		private String user_open_id;

		public int getMusic_id() {
			return music_id;
		}

		public void setMusic_id(int music_id) {
			this.music_id = music_id;
		}

		public String getUser_open_id() {
			return user_open_id;
		}

		public void setUser_open_id(String user_open_id) {
			this.user_open_id = user_open_id;
		}
	}

	@Test
	public void testQueryUser() {
		User user = userMapper.queryUserInfoByOpenId("232121");

		System.out.println("==============" + user);
	}

	@Test
	public void tesQueryMusic() throws ServiceException {
		try{
			Response res = aboutService.getAllMusic("zxx952887");
			if (res.getStateCode() == 0) {
				Map<String, Object> data = (Map<String, Object>) res.getData();
				List<Music> phb = (List<Music>) data.get("phb");
				List<Music> tj = (List<Music>) data.get("tj");
				List<Music> like = (List<Music>) data.get("like");
				System.err.println("=======排行榜========");
				phb.forEach(music -> {
					System.err.println(music.toString());
				});
				System.err.println("=======推荐========");
				tj.forEach(music -> {
					System.err.println(music.toString());
				});
				System.err.println("=======喜欢========");
				like.forEach(music -> {
					System.err.println(music.toString());
				});
			} else {

			}
		}catch (Exception e) {
			System.err.println("异常=======");
		}
	}

	@Test
	public void testAddEnjoyMusic() {
		Response response = null;
		try {
			response = aboutService.addEnjoyMusic("", "");
		} catch (ServiceException e1) {
			response = e1.toResponse();
		} catch (Exception e) {
			response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
		}
		System.err.println("result " + response.getStateCode() + "," + response.getMessage());
	}

	@Test
	public void testQueryComment() {
		Response response = null;
		try {
			response = aboutService.findPageQueryMusicComment("", 17, 1, 5);
		} catch (ServiceException e1) {
			response = e1.toResponse();
		} catch (Exception e) {
			response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
		}
		System.err.println("result " + response.getStateCode() + "," + response.getMessage());
	}

	@Test
	public void testListFilter() {
		List<EnjoyMusic> enjoyList = new ArrayList<>();
		EnjoyMusic enjoyMusic1 = new EnjoyMusic();
		enjoyMusic1.music_id = 1;
		enjoyMusic1.user_open_id = "zxx9527";
		EnjoyMusic enjoyMusic2 = new EnjoyMusic();
		enjoyMusic2.music_id = 2;
		enjoyMusic2.user_open_id = "zxx9527";
		enjoyList.add(enjoyMusic1);
		enjoyList.add(enjoyMusic2);
		List<Music> comList = new ArrayList<>();
		Music music1 = new Music();
		music1.setMusic_id(1);
		music1.setMusic_name("左右为难");
		Music music2 = new Music();
		music2.setMusic_id(2);
		music2.setMusic_name("世界这么大还是遇见你");
		Music music3 = new Music();
		music3.setMusic_id(3);
		music3.setMusic_name("夜曲");
		comList.add(music1);
		comList.add(music2);
		comList.add(music3);
		for (int i = 0; i < comList.size(); i++) {
			Music indexMusic = comList.get(i);
			indexMusic.setXh(i);
		}
		comList.forEach(music -> {
			for (EnjoyMusic enjoyMusic: enjoyList) {
				if (music.isMyEnjoy()) {
					continue;
				}
				if(music.getMusic_id() == enjoyMusic.getMusic_id()) {
					music.setMyEnjoy(true);
				} else {
					music.setMyEnjoy(false);
				}
			}
		});
		for (Music music: comList) {
			System.err.println("id= " + music.getMusic_id() + "enjoy = " + music.isMyEnjoy() + music.getXh());
		}
	}

}

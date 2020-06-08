package com.zxx.wechart.store.service.impl;

import com.zxx.wechart.store.common.CodeConstant;
import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.domain.about.CommentMusic;
import com.zxx.wechart.store.domain.about.EnjoyMusic;
import com.zxx.wechart.store.domain.about.Music;
import com.zxx.wechart.store.mapper.AboutMapper;
import com.zxx.wechart.store.service.AboutService;
import com.zxx.wechart.store.utils.RoundNumUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/28 19:04
 * @Description: about相关接口的实现
 */
@Service("AboutService")
public class AboutServiceImpl implements AboutService {

    Logger logger = LoggerFactory.getLogger(AboutServiceImpl.class);

    @Autowired
    private AboutMapper aboutMapper;

    @Autowired
    private RoundNumUtil roundNumUtil;

    @Override
    public Response getAllMusic(String openId) throws ServiceException {
        Response response = null;
        Map<String, Object> data = new HashMap<>();
        try {
            List<Music> phbList = aboutMapper.getPhbMusic();//音乐排行榜
            for (int i = 0; i < phbList.size(); i++) {
                Music indexMusic = phbList.get(i);
                indexMusic.setXh(i);
                indexMusic.setMyEnjoy(false);
            }
            List<EnjoyMusic> enjoyList = aboutMapper.getEnJoyMusicByOpenId(openId);//查询当前人喜欢的音乐id
            List<Music> likeList = new ArrayList<>();//喜欢的音乐 data
            if (enjoyList.size() > 0) {
                phbList.forEach(music -> {
                    for (EnjoyMusic enjoyMusic: enjoyList) {
                        if (music.isMyEnjoy()) {
                            continue;
                        }
                        if(music.getMusic_id() == enjoyMusic.getMusic_id()) {
                            music.setMyEnjoy(true);
                        }
                    }
                });
                likeList.addAll(phbList.stream().filter(x -> x.isMyEnjoy()).collect(Collectors.toList()));
                for (int i = 0; i < likeList.size(); i++) {
                    Music indexMusic = likeList.get(i);
                    indexMusic.setXh(i);
                }
            }
            List<Music> tjList = new ArrayList<>();//音乐推荐
            int tjxh = 0;
            for (int i = 0; i < phbList.size(); i++) {
                Music indexMusic = phbList.get(i);
                if (indexMusic.getIs_chosen().equals("1")) {
                    Music tjMusic = new Music();
                    tjMusic.setMusic_id(indexMusic.getMusic_id());
                    tjMusic.setMusic_name(indexMusic.getMusic_name());
                    tjMusic.setXh(tjxh);
                    tjMusic.setMyEnjoy(indexMusic.isMyEnjoy());
                    tjMusic.setComment_sum(indexMusic.getComment_sum());
                    tjMusic.setIs_chosen(indexMusic.getIs_chosen());
                    tjMusic.setLike_sum(indexMusic.getLike_sum());
                    tjMusic.setMusic_lyric_url(indexMusic.getMusic_lyric_url());
                    tjMusic.setMusic_url(indexMusic.getMusic_url());
                    tjMusic.setSinger(indexMusic.getSinger());
                    tjMusic.setSinger_img_url(indexMusic.getSinger_img_url());
                    tjxh++;
                    tjList.add(tjMusic);
                }
                indexMusic.setXh(i);
            }
            data.put("phb", phbList);
            data.put("tj", tjList);
            data.put("like", likeList);
            response = Response.success(data);
        } catch (Exception e) {
            logger.error("AboutServiceIpml.getAllMusic 查询所有音乐异常", e);
            response = Response.error(CodeConstant.WECHART_SQL_ERR.getValue(), CodeConstant.WECHART_SQL_ERR.getMessage());
        }
        return response;
    }

    @Override
    public Response addEnjoyMusic(String openId, String musicId) throws ServiceException {
        Response response = null;
        try {
            if (StringUtils.isEmpty(musicId)) {
                throw new ServiceException(CodeConstant.WECHAT_MUSICID_NULL, CodeConstant.WECHAT_MUSICID_NULL.getMessage());
            }
            aboutMapper.addEnjoyMusic(openId, musicId, new Date());
            response = Response.success();
        } catch (Exception e) {
            logger.error("AboutServiceIpml.addEnjoyMusic 添加喜欢音乐异常", e);
            response = Response.error(CodeConstant.WECHART_SQL_ERR.getValue(), CodeConstant.WECHART_SQL_ERR.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelEnjoyMusic(String openId, String musicId) throws ServiceException {
        Response response = null;
        try {
            if (StringUtils.isEmpty(musicId)) {
                throw new ServiceException(CodeConstant.WECHAT_MUSICID_NULL, CodeConstant.WECHAT_MUSICID_NULL.getMessage());
            }
            aboutMapper.cancleEnjoyMusic(openId, musicId);
            response = Response.success();
        } catch (Exception e) {
            logger.error("AboutServiceIpml.cancelEnjoyMusic 取消喜欢音乐异常", e);
            response = Response.error(CodeConstant.WECHART_SQL_ERR.getValue(), CodeConstant.WECHART_SQL_ERR.getMessage());
        }
        return response;
    }

    @Override
    public Response findPageQueryMusicComment(String openId, int musicId, int indexPage, int pageSize) throws ServiceException {
        Response response = null;
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isEmpty(musicId + "")) {
                throw new ServiceException(CodeConstant.WECHAT_MUSICID_NULL, CodeConstant.WECHAT_MUSICID_NULL.getMessage());
            }
            int totalPage = 1;
            List<CommentMusic> list = aboutMapper.findPageQueryMusicComment(musicId);
            List<CommentMusic> newList = new ArrayList<CommentMusic>();
            if (list.size() > 0) {
                totalPage = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize) + 1;
                System.err.println("totalPage = " + totalPage + ",indexPage == " + indexPage);
                if (indexPage < totalPage) {
                    newList = list.subList((indexPage-1) * pageSize, indexPage * pageSize);
                } else if (indexPage == totalPage) {
                    newList = list.subList((indexPage-1) * pageSize, list.size());
                } else {
                    throw new ServiceException(CodeConstant.WECHAT_PAGE_QUERY_ERR, CodeConstant.WECHAT_PAGE_QUERY_ERR.getMessage());
                }
            }
            result.put("indexPage", indexPage);
            result.put("pageSize", pageSize);
            result.put("totalPage", totalPage);
            result.put("list", newList);
            response = Response.success(result);
        } catch (Exception e) {
            logger.error("AboutServiceIpml.findPageQueryMusicComment 分页查询音乐评论异常", e);
            response = Response.error(CodeConstant.WECHART_SQL_ERR.getValue(), CodeConstant.WECHART_SQL_ERR.getMessage());
        }
        return response;
    }

    @Override
    public Response addMusicComment(String openId, String musicId, String content) throws ServiceException {
        Response response = null;
        try {
            if (StringUtils.isEmpty(musicId + "")) {
                throw new ServiceException(CodeConstant.WECHAT_MUSICID_NULL, CodeConstant.WECHAT_MUSICID_NULL.getMessage());
            }
            if (StringUtils.isEmpty(content)) {
                throw new ServiceException(CodeConstant.WECHAT_COMMENT_NULL, CodeConstant.WECHAT_COMMENT_NULL.getMessage());
            }
            String commentId = roundNumUtil.createNumCode(16);
            Date createDate = new Date();
            aboutMapper.addMusicComment(commentId, openId, musicId, createDate, content);
            response = Response.success(null);
        } catch (Exception e) {
            logger.error("AboutServiceIpml.addMusicComment 新增音乐评论异常", e);
            response = Response.error(CodeConstant.WECHART_SQL_ERR.getValue(), CodeConstant.WECHART_SQL_ERR.getMessage());
        }
        return response;
    }
}

package com.zxx.wechart.store.controller;

import com.zxx.wechart.store.common.CodeConstant;
import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.common.UserCache;
import com.zxx.wechart.store.service.AboutService;
import com.zxx.wechart.store.utils.CheckUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/28 17:15
 * @Description: 用于用户about功能的接口入口
 */
@RestController
@RequestMapping("/about")
public class AboutController {

    Logger logger = LoggerFactory.getLogger(AboutController.class);

    @Autowired
    private CheckUserUtil checkUserUtil;

    @Autowired
    private AboutService aboutService;

    @RequestMapping(value = "/get-music", method = RequestMethod.POST)
    public Response getAllMusic(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try {
            Response checkResponse = checkUserUtil.checkUser(request, false);
            int userRet = checkResponse.getStateCode();
            if (userRet != 0) {
                return checkResponse;
            }
            UserCache userCache = (UserCache) checkResponse.getData();
            response = aboutService.getAllMusic(userCache.getUser_open_id());
        } catch (Exception err) {
            logger.error("AboutController.getAllMusic 查询音乐失败 err", err);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/add-enjoy-music", method = RequestMethod.POST)
    public Response addEnjoyMusic(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try {
            Response checkResponse = checkUserUtil.checkUser(request, false);
            int userRet = checkResponse.getStateCode();
            if (userRet != 0) {
                return checkResponse;
            }
            UserCache userCache = (UserCache) checkResponse.getData();
            String musicId = params.get("musicId");
            response = aboutService.addEnjoyMusic(userCache.getUser_open_id(), musicId);
        } catch (ServiceException e) {
            logger.error("AboutController.addEnjoyMusic 添加喜欢音乐失败 ServiceException", e);
            response = e.toResponse();
        } catch (Exception err) {
            logger.error("AboutController.addEnjoyMusic 添加喜欢音乐失败 err", err);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/cancel-enjoy-music", method = RequestMethod.POST)
    public Response cancelEnjoyMusic(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try {
            Response checkResponse = checkUserUtil.checkUser(request, false);
            int userRet = checkResponse.getStateCode();
            if (userRet != 0) {
                return checkResponse;
            }
            UserCache userCache = (UserCache) checkResponse.getData();
            String musicId = params.get("musicId");
            response = aboutService.cancelEnjoyMusic(userCache.getUser_open_id(), musicId);
        } catch (ServiceException e) {
            logger.error("AboutController.cancelEnjoyMusic 取消喜欢音乐失败 ServiceException", e);
            response = e.toResponse();
        } catch (Exception err) {
            logger.error("AboutController.cancelEnjoyMusic 取消喜欢音乐失败 err", err);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/findPageQuery-musicComment", method = RequestMethod.POST)
    public Response findPageQueryMusicComment(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Response response = null;
        try {
            Response checkResponse = checkUserUtil.checkUser(request, false);
            int userRet = checkResponse.getStateCode();
            if (userRet != 0) {
                return checkResponse;
            }
            UserCache userCache = (UserCache) checkResponse.getData();
            int pageSize = (int) params.get("pageSize");
            int indexPage = (int) params.get("indexPage");
            int musicId = (int) params.get("musicId");
            String openId = userCache.getUser_open_id();
            response = aboutService.findPageQueryMusicComment(openId, musicId, indexPage, pageSize);
        } catch (ServiceException e) {
            logger.error("AboutController.findPageQueryMusicComment 分页查询音乐评论失败 ServiceException", e);
            response = e.toResponse();
        } catch (Exception err) {
            logger.error("AboutController.findPageQueryMusicComment 分页查询音乐评论失败 err", err);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/add-musicComment", method = RequestMethod.POST)
    public Response addMusicComment(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try {
            Response checkResponse = checkUserUtil.checkUser(request, false);
            int userRet = checkResponse.getStateCode();
            if (userRet != 0) {
                return checkResponse;
            }
            UserCache userCache = (UserCache) checkResponse.getData();
            String openId = userCache.getUser_open_id();
            String musicId = params.get("musicId");
            String content = params.get("content");
            response = aboutService.addMusicComment(openId, musicId, content);
        } catch (ServiceException e) {
            logger.error("AboutController.addMusicComment 新增音乐评论失败 ServiceException", e);
            response = e.toResponse();
        } catch (Exception err) {
            logger.error("AboutController.addMusicComment 新增音乐评论失败 err", err);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    public static void main(String[] args) {
        int tot = (int) Math.ceil(1/5);
        System.err.println(tot);
        System.err.println(new Date());
    }

}

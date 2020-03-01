package com.zxx.wechart.store.service;

import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.ServiceException;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/28 19:01
 * @Description: about相关接口定义
 */
public interface AboutService {

    Response getAllMusic(String openId) throws ServiceException;

    Response addEnjoyMusic(String openId, String musicId) throws ServiceException;

    Response cancelEnjoyMusic(String openId, String musicId) throws ServiceException;
}

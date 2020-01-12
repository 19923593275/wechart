package com.zxx.wechart.store.service;

import com.zxx.wechart.store.common.UserRsp;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 周星星
 * @DateTime: 2020/1/12 0012 14:26
 * @Description: User接口
 */
@Service
public interface UserService {

    UserRsp login(HttpServletRequest request, String code);

}

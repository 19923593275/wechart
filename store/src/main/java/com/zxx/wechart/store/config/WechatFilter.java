package com.zxx.wechart.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Author ： 周星星
 * @Date ： 2020/6/9 16:47
 * @DES : 过滤器
 */
@WebFilter(filterName = "wechatFilter", urlPatterns = "/*")
public class WechatFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info(" --------------- >>> filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("*");
        logger.info(" --------------- >>> filter doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info(" --------------- >>> filter destroy");
    }
}

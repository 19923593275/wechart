package com.zxx.wechart.store.controller;

import com.zxx.wechart.store.common.*;
import com.zxx.wechart.store.config.WechatConfig;
import com.zxx.wechart.store.queue.QRCodeQueue;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.MenuUtil;
import com.zxx.wechart.store.utils.MessageUtil;
import com.zxx.wechart.store.utils.SignUtil;
import com.zxx.wechart.store.utils.XMLUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final long serialVersionUID = 1L;

    private SignUtil signUtil;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try{
            String code = params.get("code");
            if(StringUtils.isEmpty(code)) {
                response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
                return response;
            }
            UserRsp userRsp = userService.login(request, code);
            response = Response.success(userRsp);
        }catch (ServiceException e){
            logger.error("UserController.login 用户登陆失败 ServiceException", e);
            response = e.toResponse();
        }catch (Exception e) {
            logger.error("UserController.login 用户登陆失败 Exception", e);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request) {
        return "你好! 我是周星星!";
    }

    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    public String sayHello(HttpServletRequest request) {
        try{
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            if(signUtil.checkSignature(signature, timestamp, nonce)){
                return echostr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR1");
        }
        return "你好! 我是周星星!";
    }

    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

    @RequestMapping(value = "/wechat", method = RequestMethod.POST, produces = {"application/xml;charset=utf-8"})
    public String doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("===进来了");
        //将xml转为Map
        Map<String,String> map = XMLUtil.getMap(request.getInputStream());
//        PrintWriter writer = response.getWriter();
        //这里不要弄混了，微信推过来的信息是用户发过来的，所以ToUserName是我们的公众号，FromUserName是用户的微信openid
        //所以我们既然要回复过去，就要颠倒过来
        String fromUser = map.get("ToUserName");
        String toUser = map.get("FromUserName");
        System.out.println("userId = " + toUser + "\n公众号 = " + fromUser);
        String content = "";
        String msgId = map.get("MsgId");
        if (map.get("MsgType").equals("event")){
            //如果是被关注事件，向用户回复内容，只需要将整理好的XML文本参数返回给微信即可
            if (map.get("Event").equals("subscribe")){
                //如果没有EventKey，说明是普通关注，否则是扫码关注事件
                String eventKey = map.get("EventKey");
                if (eventKey == null || eventKey.equals("")) {
                    content = "欢迎关注周星星公众号！";
                } else {
                    String param = eventKey.substring(eventKey.indexOf("_")+1);
                    //为了简单，这里直接返回一句话，实际业务场景要更复杂
                    content = "您是由openid为"+param+"的用户引进来的，我们已对其进行了奖励，您也可以生成海报，分享给朋友，可获得奖励";
                }
            } else if (map.get("Event").equals("CLICK")){
                //点击菜单事件
                if (map.get("EventKey").equals("CREATE_POSTER")) {
                    //这里不返回空串了，没必要，因为所有的IO操作我们都是通过阻塞队列异步实现的
                    content = "正在为您制作海报，请稍等";
                    //写入到阻塞队列
                    try {
                        QRCodeQueue.codeQueue.put(toUser);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (map.get("Event").equals("SCAN")) {
                content = "您已关注我们的公众号，此活动仅限首次关注时参加一次";
            }
        } else if (map.get("MsgType").equals("text")) {
            //如果是普通文本消息，先拿到用户发送过来的内容，模拟自动答疑的场景
            String text = map.get("Content");
            System.out.println("发送的消息为 ======= " + text);
            if (text.equals("1")){
                content = "你个铁憨憨1！";
            }else if (text.equals("2")){
                content = "你个铁憨憨2！";
            }else if (text.equals("3")){
                content = "你个铁憨憨3！";
            }else {
                //否则，不管用户输入什么，都返回给ta这个列表，这也是最常见的场景
                content = "请输入您遇到的问题编号：\n" +
                        "1、如何查看退款进度？\n" +
                        "2、我的订单在哪里查看？\n" +
                        "3、其他问题";
            }
        }
        //把数据包返回给微信服务器，微信服务器再推给用户
//        writer.print(MessageUtil.setMessage(fromUser,toUser, new String(content.getBytes(),"ISO-8859-1"), msgId));
        System.out.println("已发送到微信服务器 msg = " + content);
//        writer.close();
        return MessageUtil.setMessage(fromUser,toUser, content, msgId);
    }

    @RequestMapping(value = "/config-menu", method = RequestMethod.GET)
    public String configMencu(HttpServletRequest request, HttpServletResponse response) {
        try {
            MenuUtil.createMenu();
        } catch (Exception e) {
            e.printStackTrace();
            return"创建菜单失败 token = " + WechatConfig.getToken();
        }
        return "创建菜单成功 token = " + WechatConfig.getToken();
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:hh:ss");
        String str = sdf.format(new Date());
        System.err.println("ddd "+ str);
    }
}

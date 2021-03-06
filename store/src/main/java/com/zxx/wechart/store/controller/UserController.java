package com.zxx.wechart.store.controller;

import com.zxx.wechart.store.common.*;
import com.zxx.wechart.store.config.WechatConfig;
import com.zxx.wechart.store.queue.QRCodeQueue;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private static final long serialVersionUID = 1L;

    private SignUtil signUtil;

    @Autowired
    private CheckUserUtil checkUserUtil;

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
        logger.info("login return stateCode = "+ response.getStateCode() +",data = " + response.getData() );
        return response;
    }
    @RequestMapping(value = "/login/test", method = RequestMethod.POST)
    public Response login(HttpServletRequest request, @RequestBody String code) {
        System.out.println("code :====="+code);
        Response response = Response.success(code);
        return response;
    }

    /**
     * 发送验证码
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/send-code", method = RequestMethod.POST)
    public Response sendCode(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try{
            String serviceType = params.get("serviceType");
            if(StringUtils.isEmpty(serviceType)) {
                response = Response.error(CodeConstant.WECHART_BUSITYPE_NULL.getValue(), CodeConstant.WECHART_BUSITYPE_NULL.getMessage());
                return response;
            }
            String code = userService.sendCode(request, serviceType);
            if (code == null) {
                return Response.error(CodeConstant.WECHART_CODE_ERR.getValue(), CodeConstant.WECHART_CODE_ERR.getMessage());
            }
            response = Response.success(code);
        }catch (ServiceException e){
            logger.error("UserController.sendCode 验证码发送失败 ServiceException", e);
            response = e.toResponse();
        }catch (Exception e) {
            logger.error("UserController.sendCode 验证码发送失败 Exception", e);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/bind-phone", method = RequestMethod.POST)
    public Response bindPhone(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try{
            response = checkUserUtil.checkUser(request, false);
            int userRet = response.getStateCode();
            if (userRet != 0) {
                return Response.error(response.getStateCode(), response.getMessage());
            }
            UserCache userCache = (UserCache) response.getData();
            String serviceType = params.get("serviceType");
            String phone = params.get("phone");
            String code = params.get("code");
            if (StringUtils.isEmpty(phone)) {
                response = Response.error(CodeConstant.WECHAT_BIND_PHONE_NULL.getValue(), CodeConstant.WECHAT_BIND_PHONE_NULL.getMessage());
                return response;
            }
            if (StringUtils.isEmpty(code)) {
                response = Response.error(CodeConstant.WECHAT_BIND_CODE_NULL.getValue(), CodeConstant.WECHAT_BIND_CODE_NULL.getMessage());
                return response;
            }
            if (StringUtils.isEmpty(serviceType)) {
                response = Response.error(CodeConstant.WECHART_BUSITYPE_NULL.getValue(), CodeConstant.WECHART_BUSITYPE_NULL.getMessage());
                return response;
            }
            Response phoneNum = userService.bindPhone(request, userCache, phone, code, serviceType);
            System.err.println("phoneNum ========== " + phoneNum.getStateCode() + phoneNum.getData() + phoneNum.getMessage());
            if (phoneNum.getStateCode() != 0) {
                return phoneNum;
            }
            response = Response.success(phoneNum.getData());
        }catch (ServiceException e){
            logger.error("UserController.bindPhone 绑定手机号码失败 ServiceException", e);
            response = e.toResponse();
        }catch (Exception e) {
            logger.error("UserController.bindPhone 绑定手机号码失败 Exception", e);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "get-wxconfig", method = RequestMethod.POST)
    public Response getWxConfig(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Response response = null;
        try {
            String url = params.get("url");
            if (StringUtils.isEmpty(url)) {
                logger.info("UserController.getWxConfig url参数为空");
                response = Response.error(CodeConstant.WECHAET_CONFIG_URL_NULL.getValue(), CodeConstant.WECHAET_CONFIG_URL_NULL.getMessage());
                return response;
            }
            response = signUtil.signJssdk(url);
        } catch (Exception e) {
            logger.error("UserController.getWxConfig 验证JSSKD失败 Exception", e);
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
    @RequestMapping(value = "/getVoice", method = RequestMethod.POST)
    public void getVoice(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> params) {
        String mediaId= params.get("media-id");
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        HttpsURLConnection httpUrlConn = null;
        try {
            String requrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=40_CpuemLKljYtkjt8F8NnJ3gyp1KRiNGdibiDogqCzFAf2EDtD41LhEWbqdveC1ZprCp5aiOqkR8mPHv9mIhxVyxO_DStH-nRIMenWRkqabpU6G0aYkQurxnnX0owTadY-gV7vxu_0gVQVuKY-QWVeAIAMSR&media_id=" + mediaId;
            URL url = new URL(requrl);
            httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();
            String dispoSition = httpUrlConn.getHeaderField("Content-disposition");
            String contentType = httpUrlConn.getHeaderField("Content-Type");
            String contentLength = httpUrlConn.getHeaderField("Content-Length");
            System.err.println("dispoSition = " + dispoSition + "\ncontentType = " + contentType  + "\ncontentLength = " + contentLength);
            if (!StringUtils.isEmpty(dispoSition)) {
                response.setHeader("Content-disposition", dispoSition);
            }
            if (!StringUtils.isEmpty(contentType)) {
                response.setHeader("Content-Type", contentType);
            }
            if (!StringUtils.isEmpty(contentLength)) {
                response.setHeader("Content-Length", contentLength);
            }
            // 获取微信返回的输入流
            inputStream = httpUrlConn.getInputStream();
            outputStream = response.getOutputStream();
            int len;
            int bytesum=0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                bytesum += len;
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(httpUrlConn != null) {
                    httpUrlConn.disconnect();
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:hh:ss");
        String str = sdf.format(new Date());
        System.err.println("ddd "+ str);
        String phone = "19923593275";
        int a = Integer.parseInt(phone);
        System.err.println("num "+ a);
    }
}

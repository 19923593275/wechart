package com.zxx.wechart.store.controller;

import com.zxx.wechart.store.utils.SignUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final long serialVersionUID = 1L;

    private SignUtil signUtil;

    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    public String sayHello(HttpServletRequest request) {
//        try{
//            String signature = request.getParameter("signature");
//            String timestamp = request.getParameter("timestamp");
//            String nonce = request.getParameter("nonce");
//            String echostr = request.getParameter("echostr");
//            if(signUtil.checkSignature(signature, timestamp, nonce)){
//                return echostr;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("ERROR1");
//        }
        return "ERROR";
    }

    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();

        if(signUtil.checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

}

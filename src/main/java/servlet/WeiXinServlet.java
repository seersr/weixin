package servlet;

import org.dom4j.DocumentException;
import utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Map;

public class WeiXinServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String signature = req.getParameter("signature");//微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String timestamp = req.getParameter("timestamp");//时间戳
        String nonce = req.getParameter("nonce");//随机数
        String echostr = req.getParameter("echostr");//随机字符串

        PrintWriter printWriter = resp.getWriter();
        //校验 微信签名  成功则返回随机数
        if (LinkWeiXinUtils.checkSign(signature, timestamp, nonce)) {
            printWriter.write(echostr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置字符串格式
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        try {
            Map<String, String> map = MessageUtils.xmlToMapText(req);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            //这个给微信返回
            String message = null;
            if (MessageUtils.MESSAGE_TEXT.equals(msgType)) {//文本消息
                if ("1".equals(content)) {
                    message = MessageUtils.initText(toUserName, fromUserName, MessageUtils.firstText());
                } else if ("2".equals(content)) {
                    message = MessageUtils.initText(toUserName, fromUserName, MessageUtils.secondText());
                } else if ("?".equals(content) || "？".equals(content)) {
                    message = MessageUtils.initText(toUserName, fromUserName, MessageUtils.menuText());
                } else if("3".equals(content)){
                    message = MessageUtils.initNewsMessage(toUserName,fromUserName); //给用户返回图文消息
                }else if("4".equals(content)){
                    message = MessageUtils.initImageMessage(toUserName,fromUserName); //返回图片消息
                }else if("5".equals(content)){
                    message = MessageUtils.initMusicMessage(toUserName,fromUserName); //返回音乐消息
                }
            } else if (MessageUtils.MESSAGE_EVENT.equals(msgType)) {//事件推送
                String event = map.get("Event");
                if (MessageUtils.MESSAGE_SUBSCRIBE.equals(event)) { //关注
                    message = MessageUtils.initText(toUserName, fromUserName, MessageUtils.menuText());
                }else if(MessageUtils.MESSAGE_CLICK.equals(event)){ //点击事件
                    message = MessageUtils.initText(toUserName, fromUserName, MessageUtils.menuText());
                }else if(MessageUtils.MESSAGE_VIEW.equals(event)){//view事件
                    String url = map.get("EventKey");
                    message = MessageUtils.initText(toUserName, fromUserName, url+"?openid="+fromUserName);
                }
            }else if(MessageUtils.MESSAGE_IMAGE.equals(msgType)){//图片
                message = MessageUtils.initText(toUserName, fromUserName, "你发图片干嘛");
            }else if(MessageUtils.MESSAGE_LOCATION.equals(msgType)){
                String label = map.get("Label");
                message = MessageUtils.initText(toUserName, fromUserName, label);
            }

            System.out.println(message);
            //返回给微信后台
            writer.write(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}

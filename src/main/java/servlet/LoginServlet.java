package servlet;

import utils.WeiXinUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取code
        String logurl = URLEncoder.encode("http://sb.ngrok.xiaomiqiu.cn/wxdemo_war/callback");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ WeiXinUtils.APPID +
                "&redirect_uri="+ logurl +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        resp.sendRedirect(url);
        return;
    }
}

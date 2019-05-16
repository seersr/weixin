package servlet;

import com.google.gson.Gson;
import entity.Code;
import utils.HttpRequestUtils;
import utils.WeiXinUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CallBackServlet extends HttpServlet {

    private Code fromJson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String code = req.getParameter("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ WeiXinUtils.APPID +
                "&secret=" + WeiXinUtils.APPSECRET  +
                "&code="+ code +
                "&grant_type=authorization_code";
        String sendGet = HttpRequestUtils.sendGet(url);
        if(sendGet!=null){
            fromJson = gson.fromJson(sendGet, Code.class);
            System.out.println(fromJson);
        }
//        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+ fromJson.getAccess_token() +
//                "&openid=" + fromJson.getOpenid() +
//                "&lang=zh_CN";
//
//        String sendGet1 = HttpRequestUtils.sendGet(infoUrl);
//        System.out.println(sendGet1);
        resp.sendRedirect("http://192.168.2.87:8082/#/oldUserBind?openid="+fromJson.getOpenid());
    }
}

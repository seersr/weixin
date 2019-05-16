import com.google.gson.Gson;
import entity.AccessToken;
import menu.MenuButton;
import utils.WeiXinUtils;

import java.io.IOException;

public class WeiXinTest {
    public static void main(String[] args) {
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        System.out.println(accessToken.getAccess_token()+"==token");
        System.out.println(accessToken.getExpires_in()+"==expires_in");

//        String image = null;
//        try {
//            image = WeiXinUtils.upload("D:\\wxdemo\\src\\main\\webapp\\img\\timg.jpg", accessToken.getAccess_token(), "thumb");
//            System.out.println(image+"========");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Gson gson = new Gson();
        MenuButton menubutton = WeiXinUtils.initMenu();
        String menu = gson.toJson(menubutton);
        System.out.println(menu);
        int resout = WeiXinUtils.createMenu(accessToken.getAccess_token(), menu);
        if(resout == 0){
            System.out.println("菜单创建成功！");
        }else {
            System.out.println("错误码:"+resout);
        }

    }
}

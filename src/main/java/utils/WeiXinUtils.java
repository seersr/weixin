package utils;

import com.google.gson.Gson;
import entity.AccessToken;
import entity.ErrorMessage;
import entity.FileMessage;
import menu.BaseButton;
import menu.ClickButton;
import menu.MenuButton;
import menu.ViewButton;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeiXinUtils {
    private static Gson gson = new Gson();
    public static final String APPID = "wx000828e38ef1d46c";
    public static final String APPSECRET = "ba8110ef1e9800bbcbeffad3dfecba73";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //上传
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    //自定义菜单 创建
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
     * 获取accessToken
     * @return
     */
    public static AccessToken getAccessToken(){
        AccessToken accessToken = null;
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        String result = HttpRequestUtils.sendGet(url);
        if(result!=null){
            accessToken = gson.fromJson(result, AccessToken.class);
        }
        return accessToken;
    }

    /**
     * 文件上传
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     */
    public static String upload(String filePath, String accessToken,String type) throws IOException{
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        FileMessage fileMessage = gson.fromJson(result, FileMessage.class);
        System.out.println(fileMessage);
        String typeName = "media_id";
        String mediaId = null;
        if("image".equals(type)){
            typeName = type + "_media_id";
            mediaId = fileMessage.getMedia_id();
        }else if("thumb".equals(type)){
            mediaId = fileMessage.getThumbmedia_id();
        }

        return mediaId;
    }

    /**
     * 组装菜单
     * @return
     */
    public static MenuButton initMenu(){
        MenuButton menu = new MenuButton();

        ClickButton button11 = new ClickButton();
        button11.setName("投贷奖");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("微网站");
        button21.setType("view");
        button21.setUrl("http://192.168.2.87:8082/#/index");

        ViewButton button22 = new ViewButton();
        button22.setName("快速登录");
        button22.setType("view");
        button22.setUrl("http://192.168.2.87:8082/#/");

        ViewButton button31 = new ViewButton();
        button31.setName("新用户注册");
        button31.setType("view");
        button31.setUrl("http://192.168.2.87:8082/#/register");

        ViewButton button32 = new ViewButton();
        button32.setName("老用户绑定");
        button32.setType("view");
        button32.setUrl("http://sb.ngrok.xiaomiqiu.cn/wxdemo_war/wxlogin");

        BaseButton baseButton = new BaseButton();
        baseButton.setName("快速注册");
        baseButton.setSub_button(new BaseButton[]{button31,button32});

        BaseButton baseButtonMi = new BaseButton();
        baseButtonMi.setName("微服务");
        baseButtonMi.setSub_button(new BaseButton[]{button21,button22});

        menu.setButton(new BaseButton[]{button11,baseButton,baseButtonMi});
        return menu;
    }

    public static int createMenu(String token,String menu){
        int result = 1;
        ErrorMessage errorMessage = null;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        String post = HttpRequestUtils.sendPost(url, menu);
        if(post != null){
            errorMessage = gson.fromJson(post, ErrorMessage.class);
            result = errorMessage.getErrcode();
        }
        return result;
    }


}

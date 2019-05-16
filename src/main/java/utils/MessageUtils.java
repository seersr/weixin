package utils;

import com.thoughtworks.xstream.XStream;
import entity.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageUtils {
    public static final String MESSAGE_TEXT = "text";   //文本消息
    public static final String MESSAGE_IMAGE = "image"; //图片消息
    public static final String MESSAGE_VOICE = "voice"; //语音消息
    public static final String MESSAGE_VIDEO = "video"; //视频消息
    public static final String MESSAGE_MUSIC = "music";//音乐消息
    public static final String MESSAGE_LINK = "link";   //链接消息
    public static final String MESSAGE_LOCATION = "location"; //地理位置消息
    public static final String MESSAGE_EVENT = "event"; //事件推送
    public static final String MESSAGE_SUBSCRIBE = "subscribe"; //关注
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe"; //取消关注
    public static final String MESSAGE_CLICK = "CLICK"; //菜单点击
    public static final String MESSAGE_VIEW = "VIEW";   //菜单点击

    public static final String MESSAGE_NEWS = "news";   //图文消息


    /**
     * XML转成map集合
     */
    public static Map<String, String> xmlToMapText(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader saxReader = new SAXReader();

        InputStream inputStream = request.getInputStream();
        Document doc = saxReader.read(inputStream);

        Element root = doc.getRootElement();
        List<Element> document = root.elements();

        for (Element ele : document) {
            map.put(ele.getName(), ele.getText());
        }
        inputStream.close();//关流
        return map;
    }

    /**
     * text转成xml
     */
    public static String textToXml(MessageText messageText) {
        XStream xStream = new XStream();
        //根节点 替换成xml
        xStream.alias("xml", messageText.getClass());
        return xStream.toXML(messageText);
    }

    /**
     * 图文消息转为xml
     * @param newsMessage
     * @return
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new NewsItem().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 图片消息转为xml
     * @param imageMessage
     * @return
     */
    public static String imageMessageToXml(ImageMessage imageMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", imageMessage.getClass());
        return xstream.toXML(imageMessage);
    }

    /**
     * 音乐消息转为xml
     * @param musicMessage
     * @return
     */
    public static String musicMessageToXml(MusicMessage musicMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * 拼接主菜单
     */
    public static String menuText() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("欢迎关注DJ懒洋洋，请按照下面操作\n\n");
        buffer.append("1:业务介绍\n");
        buffer.append("2:本人介绍\n");
        buffer.append("?:调出此页面\n");
        return buffer.toString();
    }

    /**
     * 拼接消息
     */
    public static String initText(String toUserName, String fromUserName, String content) {
        MessageText messageText = new MessageText();
        messageText.setFromUserName(toUserName);
        messageText.setToUserName(fromUserName);
        messageText.setMsgType(MessageUtils.MESSAGE_TEXT);
        messageText.setCreateTime(new Date().getTime() + "");
        messageText.setContent(content);
        return textToXml(messageText);
    }
    /**
     * 1 的回复
     * */
    public static String firstText(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("还没有想好呢");
        return buffer.toString();
    }
    /**
     * 2 的回复
     * */
    public static String secondText(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("我叫史少斌 哈哈~~~");
        return buffer.toString();
    }

    /**
     * 图文消息的组装
     */
    public static String initNewsMessage(String toUserName,String fromUserName){
        String message = null;
        List<NewsItem> newsList = new ArrayList<NewsItem>();
        NewsMessage newsMessage = new NewsMessage();

        NewsItem news = new NewsItem();
        news.setTitle("大型IT公司如何防止运维偷窥和篡改数据");
        news.setDescription("持续发酵的西安奔驰维权事件让金融服务费成为众矢之的。");
        news.setPicUrl("http://sb.ngrok.xiaomiqiu.cn/wxdemo_war/img/2019.jpg");
        news.setUrl("http://192.168.2.115:8081/jinfu/f/r");
        newsList.add(news);

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        newsMessage.setCreateTime(sdf.format(new Date()));
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        message = newsMessageToXml(newsMessage);
        return message;
    }

    /**
     * 组装图片消息
     */
    public static String initImageMessage(String toUserName,String fromUserName){
        String message = null;
        ImageItem image = new ImageItem();
        image.setMediaId("d-Ho8IOEiCDxfjJsVW2sU2wu5E-7t9Mbw5jN-g4gxLtzZJHNAE_1d9EJlm8EICk6");
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        imageMessage.setCreateTime(sdf.format(new Date()));
        imageMessage.setImage(image);
        message = imageMessageToXml(imageMessage);
        return message;
    }

    /**
     * 组装音乐消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initMusicMessage(String toUserName,String fromUserName){
        String message = null;
        MusicItem music = new MusicItem();
        music.setThumbMediaId("x-cR2ZT8EBfPiewxK4_5OPyN2qQTUWgtZDS3ZcZfxPxUW-2Z9MEwiOoCHDy18dur");
        music.setTitle("告白の夜");
        music.setDescription("有太多的话想对你说却不知从何说起，我会待在最能守护的距离，随时都可以过去");
        music.setMusicUrl("http://www.170mv.com/kw/se.sycdn.kuwo.cn/resource/n3/16/1/1691243077.mp3");
        music.setHQMusicUrl("http://www.170mv.com/kw/se.sycdn.kuwo.cn/resource/n3/16/1/1691243077.mp3");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        musicMessage.setCreateTime(sdf.format(new Date()));
        musicMessage.setMusic(music);
        message = musicMessageToXml(musicMessage);
        return message;
    }
}

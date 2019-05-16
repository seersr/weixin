package utils;

import java.security.MessageDigest;
import java.util.Arrays;

public class LinkWeiXinUtils {
    private static final String token = "weixintoken";

    //校验签名   signature签名  ==  token + timestamp时间戳 + nonce随机字符串
    public static boolean checkSign(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        StringBuffer sortBuffer = new StringBuffer();
        //排序
        Arrays.sort(arr);
        //拼接 生成字符串
        for (int i = 0; i < arr.length; i++) {
            sortBuffer.append(arr[i]);
        }
        //SHA1加密
        String temp = getSha1(sortBuffer.toString());

        return temp.equals(signature);
    }

    /**
     * 加密SHA1
     */
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigest[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA1");
            instance.update(str.getBytes("UTF-8"));

            byte[] md = instance.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigest[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigest[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
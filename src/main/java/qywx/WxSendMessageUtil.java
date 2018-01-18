package qywx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class WxSendMessageUtil {


    /**
     * 发送消息
     *
     * @param mobile  电话号码
     * @param message 消息内容
     * @return
     */
    public String sendMessageByMobile(String mobile, String message) {
        String ac = this.getAccessToken("", "");
        String s = this.getUserName(mobile, "1", ac);
        return this.sendMessage(this.getMessageContent(message, "1000003", 0, s), ac);
    }

    /**
     * @param content     消息体
     * @param accessToken 微信授权码
     * @return
     */
    public String sendMessage(String content, String accessToken) {
        String sendUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        return new HttpRequester().postBody(sendUrl, content);
    }

    /**
     * @param messege 消息内容
     * @param agentId 应用id
     * @param type    类型 1，发送给部门  0发送给用户
     * @param userId  用户id 或者部门id
     * @return s
     */
    public String getMessageContent(String messege, String agentId, int type, String userId) {
        String toWho = "   \"touser\": \"" + userId + "\",";
        if (type == 1) {
            toWho = "   \"toparty\": \"" + userId + "\",";
        }
        return "{" +
                toWho +
                "   \"msgtype\": \"text\"," +
                "   \"agentid\": " + agentId + "," +
                "   \"text\": {" +
                "       \"content\": \"" + messege + "\"" +
                "   }," +
                "   \"safe\":0" +
                "}";

    }

    /**
     * @param departMentId 部门id
     * @param accessToken  微信授权码
     * @return s
     */
    public Map<String, String> getUserMap(String departMentId, String accessToken) {

        String userUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/list?department_id=" + departMentId + "&fetch_child=1&access_token=" + accessToken;

        try {
            HttpRespons respons = new HttpRequester().sendGet(userUrl);
            JSONObject jsonObject = JSON.parseObject(respons.getContent());
            String userlist = jsonObject.getString("userlist");
            List<UserModel> li = JSON.parseArray(userlist, UserModel.class);
            return li.stream().collect(
                    Collectors.toMap(UserModel::getMobile, UserModel::getUserid));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>(0);
    }

    /**
     * 根据电话号码获取企业微信用户的username
     *
     * @param mobile       电话号码
     * @param departMentId 部门id
     * @param accessToken  微信授权码
     * @return username
     */
    public String getUserName(String mobile, String departMentId, String accessToken) {

        String userUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/list?department_id=" + departMentId + "&fetch_child=1&access_token=" + accessToken;
        try {
            HttpRespons respons = new HttpRequester().sendGet(userUrl);
            JSONObject jsonObject = JSON.parseObject(respons.getContent());
            String userlist = jsonObject.getString("userlist");
            List<UserModel> li = JSON.parseArray(userlist, UserModel.class);
            return li.stream().filter(user -> mobile.equals(user.getMobile())).map(UserModel::getUserid).collect(Collectors.toList()).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param corpId   企业号id
     * @param secretId 应用密钥
     * @return 微信授权码
     */
    public String getAccessToken(String corpId, String secretId) {

        String tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpsecret=" + secretId + "&corpid=" + corpId;
        HttpRespons httpRespons;
        try {
            httpRespons = new HttpRequester().sendGet(tokenUrl);
            return JSON.parseObject(httpRespons.getContent()).getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package qywx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpMessageCorpconversationAsyncsendRequest;
import com.dingtalk.api.response.CorpMessageCorpconversationAsyncsendResponse;
import com.taobao.api.ApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huqi
 * @create 2018-01-17 16:47
 **/
public class DtalkSendMessage {

    private String corpId = "";

    private String secretId = "";


    /**
     * 发送消息
     *
     * @param phone        电话
     * @param messageModel 消息内容
     * @return
     */
    public long sendMessage(String phone, MessageModel messageModel) {

        String acessToken = getAccessToken(corpId, secretId);

        List<Integer> orgList = getOrgList(acessToken);

        String userId = getUserId(orgList, acessToken, phone);

        if("".equals(userId)){
            throw  new NullPointerException("当前用户不存在");
        }

        return sendMessage(acessToken, userId, messageModel);

    }

    /**
     * @param corpId   企业号id
     * @param secretId 应用密钥
     * @return 钉钉授权码
     */
    private String getAccessToken(String corpId, String secretId) {

        String tokenUrl = "https://oapi.dingtalk.com/gettoken?corpid=" + corpId + "&corpsecret=" + secretId;
        HttpRespons httpRespons;
        try {
            httpRespons = new HttpRequester().sendGet(tokenUrl);
            return JSON.parseObject(httpRespons.getContent()).getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取部门id列表
     *
     * @param accessToken
     * @return
     */
    public List<Integer> getOrgList(String accessToken) {

        String orgUrl = "https://oapi.dingtalk.com/department/list?access_token=" + accessToken;

        try {
            HttpRespons respons = new HttpRequester().sendGet(orgUrl);
            JSONObject jsonObject = JSON.parseObject(respons.getContent());
            String orglist = jsonObject.getString("department");
            List<OrgModel> li = JSON.parseArray(orglist, OrgModel.class);
            return li.parallelStream().filter(orgModel -> orgModel.getParentid() != null).map(OrgModel::getId).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取usermap
     *
     * @param departMentId 部门id
     * @param accessToken  钉钉授权码
     * @return s
     */
    public Map<String, String> getUserMap(String departMentId, String accessToken) {

        String userUrl = "https://oapi.dingtalk.com/user/list?department_id=" + departMentId + "&access_token=" + accessToken;

        try {
            HttpRespons respons = new HttpRequester().sendGet(userUrl);
            JSONObject jsonObject = JSON.parseObject(respons.getContent());
            String userlist = jsonObject.getString("userlist");
            List<UserModel> li = JSON.parseArray(userlist, UserModel.class);
            return li.parallelStream().collect(
                    HashMap::new, (m, v) -> m.put(v.getMobile(), v.getUserid()), HashMap::putAll);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>(0);
    }


    /**
     * 获取用户id
     *
     * @param departMentIds 部门id
     * @param accessToken   钉钉授权码
     * @return 用户id
     */
    public String getUserId(List<Integer> departMentIds, String accessToken, String phone) {

        String userUrl = "";

        Map<String, String> map = new HashMap<>(64);
        for (Integer i : departMentIds) {
            userUrl = "https://oapi.dingtalk.com/user/list?department_id=" + i.toString() + "&access_token=" + accessToken;
            try {
                HttpRespons respons = new HttpRequester().sendGet(userUrl);
                JSONObject jsonObject = JSON.parseObject(respons.getContent());
                String userlist = jsonObject.getString("userlist");
                List<UserModel> li = JSON.parseArray(userlist, UserModel.class);
                map = li.stream().collect(
                        HashMap::new, (m, v) -> m.put(v.getMobile(), v.getUserid()), HashMap::putAll);
                if (map.get(phone) != null) {
                    return map.get(phone);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取所有成员
     *
     * @param acessToken
     * @return usermap<phone                                                               ,                                                               userid></>
     */
    public Map<String, String> getAllUserMap(String acessToken) {

        List<Integer> list = getOrgList(acessToken);

        Map<String, String> userMap = new HashMap<>(512);
        for (Integer i : list) {
            userMap.putAll(getUserMap(i.toString(), acessToken));
        }

        return userMap;
    }

    /**
     * 发送消息
     *
     * @param access_token
     * @param userId
     * @param messageModel
     */
    public long sendMessage(String access_token, String userId, MessageModel messageModel) {
        DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
        CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
        req.setMsgtype(messageModel.getMessageType());
        req.setAgentId(messageModel.getAgentId());
        req.setUseridList(userId);
        req.setMsgcontentString(messageModel.getMessageContent());
        CorpMessageCorpconversationAsyncsendResponse res = null;
        try {
            res = client.execute(req, access_token);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return res.getResult().getDingOpenErrcode();
    }

}

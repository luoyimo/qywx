package qywx;

/**
 * @author huqi
 * @create 2018-01-18 10:05
 **/
public class MessageModel {

    /**
     * 应用id
     */
    private long agentId = 159465458L;
    /**
     * 消息跳转地址
     */
    private String url = "http://bg.imlaidian.com/operationMaintenance/";
    /**
     * 消息头
     */
    private String text = "运维通知";
    /**
     * 消息标题
     */
    private String title = "运维消息";
    /**
     * 消息内容
     */
    private String content = "运维消息通知";
    /**
     * 消息发送者
     */
    private String from = "admin";
    /**
     * 消息类型
     */
    private String messageType = "OA";

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getMessageContent() {

        String s = "{\"message_url\": \"" + url + "\"," +
                "\"head\": {\"bgcolor\": \"FFBBBBBB\"," +
                "\"text\":\"" + text + "\"}," +
                "\"body\": {\"title\": \"" + title + "\"," +
                "\"content\":\" " + content + "\"," +
                "\"author\":\" " + from + "\"}}";

        return s;

//        return "{\"message_url\": " + url + "," +
//                "\"head\": {\"bgcolor\": \"FFBBBBBB\",\"text\": " + text + "}," +
//                "\"body\": {\"title\": " + title + "," +
//                "\"content\": " + content + "," +
//                "\"author\": " + from + "}}";
    }
}

package ins.platform.socketIO.msg;

/**
 * @author yanzhaohu
 * @date 2018/10/9
 * 消息对象
 **/
public class MessageInfo {

    private String userCode;
    private String msgContent;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}

package websocketLab.yakov.laptev;

public class Message {
    private String msg;
    private String sessionUserId;

    public Message(String msg, String sessionUserId) {
        this.msg = msg;
        this.sessionUserId = sessionUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSessionUserId() {
        return sessionUserId;
    }

    public void setSessionUserId(String sessionUserId) {
        this.sessionUserId = sessionUserId;
    }
}

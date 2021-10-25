package hk.edu.cuhk.ie.iems5722.a2_1155162650;

public class ChatMsgEntity {
    private String message;
    private String date;
    private String user;

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMsgEntity chatMsgEntity = (ChatMsgEntity) o;
        if (!date.equals(chatMsgEntity.date) || !message.equals(chatMsgEntity.message) || !user.equals(chatMsgEntity.user)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode() + user.hashCode() + message.hashCode();
        result = 16 * result + date.hashCode();
        return result;
    }
}

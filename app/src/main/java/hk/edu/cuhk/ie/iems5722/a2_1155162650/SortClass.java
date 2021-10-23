package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import java.util.Comparator;

public class SortClass implements Comparator {

    @Override
    public int compare(Object o, Object t1) {
        ChatMsgEntity chatMsgEntity1 = (ChatMsgEntity) o;
        ChatMsgEntity chatMsgEntity2 = (ChatMsgEntity) t1;
        int flag = chatMsgEntity1.getDate().compareTo(chatMsgEntity2.getDate());
        return flag;
    }
}
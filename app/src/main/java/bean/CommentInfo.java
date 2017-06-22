package bean;

import java.util.List;

/**
 * Author: wangzixiong
 * Created by: ModelGenerator on 2017/5/25
 */
public class CommentInfo {
    private String message;    // OK
    private int status;    // 0
    private List<CommentItem> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CommentItem> getData() {
        return data;
    }

    public void setData(List<CommentItem> data) {
        this.data = data;
    }
}
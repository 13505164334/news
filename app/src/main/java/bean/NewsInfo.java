package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/16.
 */

public class NewsInfo implements Serializable{
    private String summary;
    private String icon;
    private String stamp;
    private String title;
    private int nid;
    private String link;
    private int type;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", stamp='" + stamp + '\'' +
                ", title='" + title + '\'' +
                ", nid=" + nid +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }
}

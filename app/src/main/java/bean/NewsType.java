package bean;

/**
 * Created by Administrator on 2017/5/15.
 */

public class NewsType {

    private String subgroup;
    private int subid;

    public NewsType(String subgroup, int subid) {
        this.subgroup = subgroup;
        this.subid = subid;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }

    @Override
    public String toString() {
        return "NewsType{" +
                "subgroup='" + subgroup + '\'' +
                ", subid=" + subid +
                '}';
    }
}

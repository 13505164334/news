package bean;

import java.util.List;

/**
 * Created by wangzixiong on 2017/5/23.
 */

public class UserInfo {
    private String uid, portrait;
    private int integration, comnum;
    private List<Loginlog> loginlog;

    public class Loginlog {
        private String time, address;
        private int device;

        @Override
        public String toString() {
            return "Loginlog{" +
                    "time='" + time + '\'' +
                    ", address='" + address + '\'' +
                    ", device=" + device +
                    '}';
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public List<Loginlog> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<Loginlog> loginlog) {
        this.loginlog = loginlog;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", portrait='" + portrait + '\'' +
                ", integration=" + integration +
                ", comnum=" + comnum +
                ", loginlog=" + loginlog +
                '}';
    }
}

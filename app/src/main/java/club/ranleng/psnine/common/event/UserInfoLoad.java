package club.ranleng.psnine.common.event;

import com.blankj.utilcode.util.LogUtils;

public class UserInfoLoad {

    private String name;
    private String icon;
    private Boolean dao;
    private Boolean msg;

    public UserInfoLoad() {

    }

    public UserInfoLoad(String name, String icon, Boolean dao, Boolean msg) {
        setName(name);
        setIcon(icon);
        setDao(dao);
        setMsg(msg);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDao() {
        return dao;
    }

    public void setDao(Boolean dao) {
        this.dao = dao;
    }

    public Boolean getMsg() {
        return msg;
    }

    public void setMsg(Boolean msg) {
        this.msg = msg;
    }
}

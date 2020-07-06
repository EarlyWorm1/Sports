package dhu.cst.yinqingbo416.sports.Entry;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class News extends DataSupport implements Serializable {
    private int id;//消息编号
    private int imgkind;//图标来源
    private String title;//标题
    private String introduce;//简介
    private String detail;//详情
    private boolean isNew;//是否是新消息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgkind() {
        return imgkind;
    }

    public void setImgkind(int imgkind) {
        this.imgkind = imgkind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}

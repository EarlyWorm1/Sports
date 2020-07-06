package dhu.cst.yinqingbo416.sports.Entry;

import java.io.Serializable;

public class ActivityInfo implements Serializable {
    private int id;//编号
    private String name;//名称
    private String []dept;//举办部门
    private String time;//开始时间
    private String endTime;//结束时间
    private String site;//地点
    private String award;//奖励
    private String [] awardSign;
    private String details;//详情
    private String remark;//备注
    private int kind;
    private int people;//活动人数
    private int man_people;//男生人数
    private int woman_people;//女生人数
    private int bench_people;//替补人数
    private int bench_man_people;//替补男生人数
    private int bench_woman_people;//替补女生人数
    private int number;//活动人数
    private int man_number;//男生人数
    private int woman_number;//女生人数

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMan_number() {
        return man_number;
    }

    public void setMan_number(int man_number) {
        this.man_number = man_number;
    }

    public int getWoman_number() {
        return woman_number;
    }

    public void setWoman_number(int woman_number) {
        this.woman_number = woman_number;
    }

    public int getBench_number() {
        return bench_number;
    }

    public void setBench_number(int bench_number) {
        this.bench_number = bench_number;
    }

    public int getBench_man_number() {
        return bench_man_number;
    }

    public void setBench_man_number(int bench_man_number) {
        this.bench_man_number = bench_man_number;
    }

    public int getBench_woman_number() {
        return bench_woman_number;
    }

    public void setBench_woman_number(int bench_woman_number) {
        this.bench_woman_number = bench_woman_number;
    }

    private int bench_number;//替补人数
    private int bench_man_number;//替补男生人数
    private int bench_woman_number;//替补女生人数
    private int status;//活动状态
    private String range;//活动的参与范围

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
    public ActivityInfo() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDept() {
        return dept;
    }

    public void setDept(String[] dept) {
        this.dept = dept;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public void setMan_people(int man_people) {
        this.man_people = man_people;
    }

    public void setWoman_people(int woman_people) {
        this.woman_people = woman_people;
    }

    public void setBench_people(int bench_people) {
        this.bench_people = bench_people;
    }

    public void setBench_man_people(int bench_man_people) {
        this.bench_man_people = bench_man_people;
    }

    public void setBench_woman_people(int bench_woman_people) {
        this.bench_woman_people = bench_woman_people;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSite() {
        return site;
    }

    public String getAward() {
        return award;
    }

    public String getDetails() {
        return details;
    }

    public String getRemark() {
        return remark;
    }

    public int getPeople() {
        return people;
    }

    public int getMan_people() {
        return man_people;
    }

    public int getWoman_people() {
        return woman_people;
    }

    public int getBench_people() {
        return bench_people;
    }

    public int getBench_man_people() {
        return bench_man_people;
    }

    public int getBench_woman_people() {
        return bench_woman_people;
    }
    public String[] getAwardSign() {
        return awardSign;
    }

    public void setAwardSign(String[] awardSign) {
        this.awardSign = awardSign;
    }

    public int getStatus() {
        return status;
    }
}

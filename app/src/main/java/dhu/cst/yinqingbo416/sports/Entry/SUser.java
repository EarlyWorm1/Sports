package dhu.cst.yinqingbo416.sports.Entry;

import java.io.Serializable;

public class SUser implements Serializable {
    private String studentId;
    private String collegeName;
    private String collegeId;
    private String className;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private String passWord;
    private String salt;

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public SUser(String studentId, String collegeName, String collegeId , String className, String name, String sex, String phone, String email, String passWord, String salt) {
        this.studentId = studentId;
        this.collegeName = collegeName;
        this.collegeId = collegeId;
        this.className = className;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.passWord = passWord;
        this.salt = salt;
    }
    public SUser(){}

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}

package dhu.cst.yinqingbo416.sports.Utils;

import java.util.regex.Pattern;

public class CheckInput {
    //判断是否符合学号的规范：9位数字
    public static String isStudentid(String s){
        if(s.length() == 0){
            return "学号不能为空";
        }else if(s.length() < 9 || s.length() > 9){
            return "学号应为9位数字";
        }else {
            String str = "[0-9]*";
            Pattern pattern = Pattern.compile(str);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "学号应为9位数字";
            }
        }
    }
    //判断密码长度是否在8-18之间
    public static String isPassWordLength(String s){
        if(s.length() == 0){
            return "密码不能为空";
        }else {
            return " ";
        }
    }
    //判断班级是否符合2-6位汉字+4位数字的规范
    public static String isClassName(String s){
        if(s.length() == 0){
            return "班级不能为空";
        }else {
            String str = "[\\u4e00-\\u9fa5]{2,6}[1-9][0-9]{3}";
            Pattern pattern = Pattern.compile(str);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "班级格式错误";
            }
        }
    }
    //判断名字是否符合名字规范
    public static String isUserName(String s){
        if(s.length() == 0){
            return "姓名不能为空";
        }else if(s.length() > 80){
            return "姓名过长";
        }else {
            String rex = "[A-Za-z·\\-\\u4e00-\\u9fa5]+";
            Pattern pattern = Pattern.compile(rex);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "姓名不能含有特殊符号";
            }
        }
    }
    //判断是否是国内手机号码
    public static String isPhonrNumber(String s){
        if(s.length() == 0){
            return "手机号码不能为空";
        }else {
            String rex = "0?(13|14|15|18|17)[0-9]{9}";
            Pattern pattern = Pattern.compile(rex);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "手机号码错误";
            }
        }
    }
    //判断是否符合邮箱格式
    public static String isEmail(String s){
        if(s.length() == 0){
            return "邮箱不能为空";
        }else {
            String rex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
            Pattern pattern = Pattern.compile(rex);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "邮箱格式错误";
            }
        }
    }
    //判断是否符合密码格式：8-18位，必须由字母+数字组成，区分大小写
    public static String isPassWord(String s){
        if(s.length() == 0){
            return "密码不能为空";
        }else {
            String rex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$";
            Pattern pattern = Pattern.compile(rex);
            if(pattern.matcher(s).matches()){
                return " ";
            }else {
                return "密码8-18位且必须由数字和字母组成";
            }
        }
    }
    //判断确认密码是否为空
    public static String isSurePassWord(String s){
        if(s.length() == 0){
            return "确认密码不能为空";
        }else {
            return " ";
        }
    }
}

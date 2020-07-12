package dhu.cst.yinqingbo416.sports.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dhu.cst.yinqingbo416.sports.Entry.News;
import dhu.cst.yinqingbo416.sports.R;

public class Tools {
    static private Map<String,String>map1 = new HashMap<String,String>(){
        {
            put("纺织学院","1");
            put("服装与艺术设计学院","2");
            put("旭日工商管理学院","3");
            put("机械工程学院","4");
            put("信息科学与技术学院","5");
            put("计算机科学与技术学院","6");
            put("化学化工与生物工程学院","7");
            put("材料科学与工程学院","8");
            put("环境科学与工程学院","9");
            put("人文学院","a");
            put("理学院","b");
            put("外语学院","c");
            put("马克思主义学院","d");
            put("上海国际时尚创意学院","e");
            put("国际文化交流学院","f");
        }
    };
    static public List<Integer>applyActId = new ArrayList<>();//用于储存用户已报名的活动
    public static String userName;//储存用户的姓名
    public static String userId;//储存用户的学号
    public static String password;//储存密码
    public static String collegeId;//学院号
    public static boolean fingerprint;//是否开启指纹登录
    public static boolean currentFingerprint;//当前的指纹点击状态
    public static boolean [] loadPage = {false,false};//标记活动信息页面和已报名活动页面数据是否已经加载
    public static int currentPageNum = 1;//标记当前fragment的页码
    //用collegeName得到collegeId
    static public String getCollegeId(String collegeName){
        return map1.get(collegeName);
    }
    //重置密码
    public static boolean resetPWD(String id,String name,String email){
        //生成随机密码
        String pwd = producePWD();
        //发送邮件
        sendEmail(email,name,pwd);
        //修改密码
        boolean flag = DBUtils.updatePWD(id,pwd);
        return flag;
    }
    //产生新随机密码
    public static String producePWD(){
        String pwd = "";
        Random random = new Random();
        for(int i = 0;i < 16;i++){//生成16位密码
            int charOrNum = random.nextInt(2);
            if(charOrNum == 0){
                int temp = random.nextInt(2) == 1? 65:97;
                pwd += (char)(random.nextInt(26)+temp);
            }else{
                pwd += random.nextInt(10);
            }
        }
        return pwd;
    }
    //发送邮件
    public static void sendEmail(String email,String name,String pwd){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.126.com");
        Session session = Session.getInstance(props, null);
        try{
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("mianpaoxingdong@126.com", "免跑行动", "UTF-8"));
            msg.setRecipients(Message.RecipientType.TO, email);
            msg.setSubject("您的密码已重置");
            msg.setSentDate(new Date());
            name += "。您的密码已经重置为："+pwd+"。请重新登陆并修改您的密码！";
            msg.setText(name);
            Transport.send(msg, "mianpaoxingdong@126.com", "KKHBYXWAIXWGHRIK");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    //获得已报名的活动序列
    public static List<Integer> getApplyActId() {
        return applyActId;
    }
    //设置已报名的活动序列
    public static void setApplyActId(List<Integer> applyActId) {
        Tools.applyActId = applyActId;
    }
    //添加消息列表的测试数据
    public static void initTestDB(){
        Log.d("Tools", "initTestDB:被调用");
        News news = new News();
        news.setId(1);
        news.setImgkind(1);
        news.setTitle("比赛时间推迟");
        news.setIntroduce("因为天气原因，比赛时间推迟");
        news.setDetail("因为预计4月20号会有大到暴雨，原定4月20号举办的新生篮球赛推迟，具体时间另行通知");
        news.setNew(false);
        news.save();
        News news1 = new News();
        news1.setId(2);
        news1.setImgkind(2);
        news1.setTitle("比赛地点更改");
        news1.setIntroduce("因为天气原因，比赛地点更改");
        news1.setDetail("因为预计4月21号会有大到暴雨，原定4月21号在风雨体育场举办的新生羽毛球赛更改到松江体育馆举办");
        news1.setNew(false);
        news1.save();
        News news2 = new News();
        news2.setId(3);
        news2.setImgkind(3);
        news2.setTitle("免跑单核对通知");
        news2.setIntroduce("请及时核对免跑单录入信息！");
        news2.setDetail("恭喜你在新生乒乓球赛中取得佳绩，你将获得10张免跑单的奖励，请在赛事群（QQ:123456789）或学院领队群中查看免跑单录入表，及时核对你的免跑单信息。" +
                "如有错误请在6月1号前联系工作人员（1425114974@qq.com）进行修改，逾期不予修改");
        news2.setNew(true);
        news2.save();
        News news3 = new News();
        news3.setId(4);
        news3.setImgkind(4);
        news3.setTitle("免跑单核对通知");
        news3.setIntroduce("请及时核对免跑单录入信息！");
        news3.setDetail("恭喜你在旱地冰球赛中取得佳绩，你将获得10张免跑单的奖励，请在赛事群（QQ:123456789）或学院领队群中查看免跑单录入表，及时核对你的免跑单信息。" +
                "如有错误请在6月1号前联系工作人员（1425114974@qq.com）进行修改，逾期不予修改");
        news3.setNew(true);
        news3.save();
        News news4 = new News();
        news4.setId(5);
        news4.setImgkind(1);
        news4.setTitle("比赛时间通知");
        news4.setIntroduce("请及时查看修改后的比赛时间！");
        news4.setDetail("因为预计4月20号会有大到暴雨，原定4月20号举办的新生篮球赛推迟到5月1号13:00-6月7号15:00。");
        news4.setNew(true);
        news4.save();
    }
    //获取部门图标
    public static Drawable getDrawable(Context context,int k){
        Drawable img;
        switch (k){
            case 1:
                img = ContextCompat.getDrawable(context, R.drawable.lanqiu);
                break;
            case 2:
                img = ContextCompat.getDrawable(context,R.drawable.yumao);
                break;
            case 3:
                img = ContextCompat.getDrawable(context,R.drawable.jisuanji);
                break;
            case 4:
                img = ContextCompat.getDrawable(context,R.drawable.renwen);
                break;
            case 5:
                img = ContextCompat.getDrawable(context,R.drawable.waiyu);
                break;
            default:
                img = ContextCompat.getDrawable(context,R.drawable.icon);
                break;
        }
        return img;
    }
    //获取部门名称
    public static String getDeptName(int k){
        String str;
        switch (k){
            case 1:
                str = "篮球社";
                break;
            case 2:
                str = "羽毛秋社";
                break;
            case 3:
                str = "计算机学院文体中心";
                break;
            case 4:
                str = "人文学院体育部";
                break;
            case 5:
                str = "外语学院体育部";
                break;
            default:
                str = "null";
                break;
        }
        return str;
    }

}

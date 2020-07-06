package dhu.cst.yinqingbo416.sports.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.Entry.SUser;
import dhu.cst.yinqingbo416.sports.Entry.User;

public class DBUtils {
    private static String DRIVER = "com.mysql.jdbc.Driver";//mysql驱动
    private static String URL = "jdbc:mysql://服务器ip:端口号/数据库名称?characterEncoding=utf-8&useSSL=false";//mysql连接url
    private static String USER = "用户名";
    private static String PASSWD = "密码";
    private static String TAG = "DBUtils";
    //获取数据库连接
    private static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL,USER,PASSWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    //检查账号密码能否登录
    public static int checkLogin(Context context, User user){
        Connection connection = getConnection();
        if(connection == null)
            return 0;//未连接上服务器
        String sql = "select * from user where id = '"+user.getUserName()+"' and passWord = '"+user.getPassWd()+"';";
        Statement statement = null;
        ResultSet res = null;
        try {
            statement=connection.createStatement();
            res = statement.executeQuery(sql);
            if(res.next()){
                String studentId = res.getString("id");
                String name = res.getString("name");
                String collegeId = res.getString("college_id");
                String collegeName = res.getString("collegeName");
                String sex = res.getString("sex");
                String phone = res.getString("phone");
                SharedPreferences.Editor editor = context.getSharedPreferences("stuData",Context.MODE_PRIVATE).edit();
                editor.putString("id",studentId);//学号
                editor.putString("name",name);//姓名
                editor.putString("collegeId",collegeId);//学院编号
                editor.putString("collegeName",collegeName);//学院名称
                editor.putString("sex",sex);//性别
                editor.putString("phone",phone);
                editor.apply();
                res.close();
                statement.close();
                connection.close();
                return 1;//成功登录
            }else {
                res.close();
                statement.close();
                connection.close();
                return 2;//账号密码错误
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;//数据库异常
        }
    }
    //向数据库插入数据
    public static int insert(SUser sUser){
        Connection connection = getConnection();
        if(connection == null){
            return 0;//没有连接上服务器
        }
        int ret = checkUser(sUser.getStudentId());
        if(ret == 1){
            return 4;//该用户已经注册
        }else if(ret == 3){
            return 3;//数据库异常
        }
        String sql = "insert into user(id,collegeName,class,name,sex,phone,email,passWord,salt,college_id) values(?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,sUser.getStudentId());
            ps.setString(2,sUser.getCollegeName());
            ps.setString(3,sUser.getClassName());
            ps.setString(4,sUser.getName());
            ps.setString(5,sUser.getSex());
            ps.setString(6,sUser.getPhone());
            ps.setString(7,sUser.getEmail());
            ps.setString(8,sUser.getPassWord());
            ps.setString(9,sUser.getSalt());
            ps.setString(10,sUser.getCollegeId());
            int result = ps.executeUpdate();
            if(result != 0){
                ps.close();
                connection.close();
                return 1;//注册成功
            }else {
                ps.close();
                connection.close();
                return 2;//注册失败
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;//数据库异常
        }
    }
    //检查数据库中是否已经有了该用户名
    public static  int checkUser(String id){
        Connection connection = getConnection();
        String sql = "select id from user where id = '" + id + "';";
        try{
            Statement statement=connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if(res.next()){//存在该用户
                res.close();
                statement.close();
                connection.close();
                return 1;
            }else {
                res.close();
                statement.close();
                connection.close();
                return 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;//数据库异常
        }
    }
    //得到用户的邮箱
    public static String checkEmail(SUser user){
        Connection conn = getConnection();
        //Log.d("TAG", "得到数据库连接");
        if(conn == null){
            return "数据连接中断，请检查网络连接";//数据连接中断
        }
        String sql = "select name from user where id = '"+user.getStudentId()+"' and email = '"+user.getEmail()+"';";
        try {
            Statement sta = conn.createStatement();
            ResultSet res = sta.executeQuery(sql);
            if(res.next()){
                String ret = "Hi,";
                ret += res.getString("name");
                res.close();
                sta.close();
                conn.close();
                return ret;
            }else {
                res.close();
                sta.close();
                conn.close();
                return "学号或注册邮箱错误";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "系统异常，请联系系统管理员";
        }
    }
    //修改用户密码
    public static boolean updatePWD(String id,String newPWd){
        Connection conn = getConnection();
        String sql = "update user set password = '"+newPWd+"' where id = '"+id+"'";
        try{
            Statement sta = conn.createStatement();
            int n = sta.executeUpdate(sql);
            if(n == 1){
                sta.close();
                conn.close();
                return true;
            }else {
                sta.close();
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //补全用户能够参加的活动信息
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<ActivityInfo> getRegsInfo(List<Integer>actId,String code,List<ActivityInfo>datas){
        String sql = "";
        int k = 0;
        for(int i:actId){
            if(sql.equals("")){
                sql += "select number,man_number,woman_number,bench_number,bench_woman_number,bench_man_number from registration where col_code in ('g','"+code+"') " + "and act_id in (";
                sql += i;
            }else {
                sql += (","+i);
            }
        }
        sql += ")";
        Connection conn = getConnection();
        if(conn == null){
            return null;
        }
        try{
            Statement sta = conn.createStatement();
            ResultSet res = sta.executeQuery(sql);
            while (res.next()){
                datas.get(k).setNumber(res.getInt("number"));
                datas.get(k).setMan_number(res.getInt("man_number"));
                datas.get(k).setWoman_number(res.getInt("woman_number"));
                datas.get(k).setBench_number(res.getInt("bench_number"));
                datas.get(k).setBench_man_number(res.getInt("bench_man_number"));
                datas.get(k).setBench_woman_number(res.getInt("bench_woman_number"));
                k++;
            }
            res.close();
            sta.close();
            conn.close();
            return datas;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取活动报名人数信息
    public static ActivityInfo updateActInfo(ActivityInfo act,String collegeCode){
        Connection conn = getConnection();
        if(conn == null){
            //Log.d(TAG, "Connection:null");
            return null;
        }
        if(act.getKind() <= 2){
            collegeCode = "g";
        }
        String contain = "people,man_people,woman_people,bench_people,bench_man_people,bench_woman_people,number,man_number,woman_number,bench_number,bench_man_number,bench_woman_number";
        String sql = "select "+contain+" from activity join registration on activity.id = registration.act_id where activity.id = "+act.getId()+" and registration.col_code = '"+collegeCode+"'";
        //Log.d(TAG, "sql:"+sql);
        try {
            Statement sta = conn.createStatement();
            //Log.d(TAG, "创建Statement");
            ResultSet res = sta.executeQuery(sql);
            if(res.next()){
                act.setPeople(res.getInt("people"));
                act.setMan_people(res.getInt("man_people"));
                act.setWoman_people(res.getInt("woman_people"));
                act.setBench_people(res.getInt("bench_people"));
                act.setBench_man_people(res.getInt("bench_man_people"));
                act.setBench_woman_people(res.getInt("bench_woman_people"));
                act.setNumber(res.getInt("number"));
                act.setMan_number(res.getInt("man_number"));
                act.setWoman_number(res.getInt("woman_number"));
                act.setBench_number(res.getInt("bench_number"));
                act.setBench_man_number(res.getInt("bench_man_number"));
                act.setBench_woman_number(res.getInt("bench_woman_number"));
                return act;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "error");
            return null;
        }
        return null;
    }
    //报名活动
    public static boolean applyAct(int actId,boolean actKind,SUser user){//报名活动
        Connection conn = getConnection();
        if(conn == null){
            return false;
        }
        String sql = "insert into application(act_id,stu_id,college,stu_name,stu_sex,phone,apply_kind,col_code) values(?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,actId);
            ps.setString(2,user.getStudentId());
            ps.setString(3,user.getCollegeName());
            ps.setString(4,user.getName());
            ps.setString(5,user.getSex());
            ps.setString(6,user.getPhone());
            ps.setBoolean(7,actKind);
            ps.setString(8,user.getCollegeId());
            int res = ps.executeUpdate();
            return res != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //获取进行中的活动数据
    public static List<ActivityInfo> getActData(String collegeId){
        Connection conn = getConnection();
        if(conn == null){
            return null;
        }
        String sql = "select * from activity join registration on activity.id = registration.act_id where status = 1 and registration.col_code in ('g','"+collegeId+"')";
        List<ActivityInfo> data = new ArrayList<>();
        try {
            Statement sta = conn.createStatement();
            ResultSet res = sta.executeQuery(sql);
            while (res.next()){
                ActivityInfo act = new ActivityInfo();
                act.setId(res.getInt("id"));
                act.setName(res.getString("name"));
                act.setDept(res.getString("dept").split(" "));
                act.setTime(res.getString("time"));
                @SuppressLint("SimpleDateFormat")
                String str_end_time = new SimpleDateFormat("y年M月d号 HH:mm").format(res.getTimestamp("endTime"));
                act.setEndTime(str_end_time);
                act.setSite(res.getString("site"));
                act.setAward(res.getString("award"));
                act.setAwardSign(res.getString("award_sign").split(" "));
                act.setDetails(res.getString("details"));
                act.setRemark(res.getString("remark"));
                act.setKind(res.getInt("act_kind"));
                act.setPeople(res.getInt("people"));
                act.setMan_people(res.getInt("man_people"));
                act.setWoman_people(res.getInt("woman_people"));
                act.setBench_people(res.getInt("bench_people"));
                act.setBench_man_people(res.getInt("bench_man_people"));
                act.setBench_woman_people(res.getInt("bench_woman_people"));
                act.setStatus(res.getInt("status"));
                act.setRange(res.getString("range"));
                act.setNumber(res.getInt("number"));
                act.setMan_number(res.getInt("man_number"));
                act.setWoman_number(res.getInt("woman_number"));
                act.setBench_number(res.getInt("bench_number"));
                act.setBench_man_number(res.getInt("bench_man_number"));
                act.setBench_woman_number(res.getInt("bench_woman_number"));
                data.add(act);
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取用户报名的活动id
    public static List<Integer> getApplyActId(String stuId,List<Integer>list){
        Connection conn = getConnection();
        if(conn == null){
            return null;
        }
        String ret = "";
        for(int i = 0;i < list.size();i++){
            if(!ret.equals("")){
                ret += ","+list.get(i);
            }else {
                ret += ""+list.get(i);
            }
        }
        String sql = "select act_id from application where stu_id = '"+stuId+"' and act_id in ("+ret+")";
        List<Integer> data = new ArrayList<>();
        try{
            Statement sta = conn.createStatement();
            ResultSet res = sta.executeQuery(sql);
            while (res.next()){
                data.add(res.getInt("act_id"));
            }
            res.close();
            sta.close();
            conn.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //取消报名活动
    public static boolean cancelAct(int actId,String stuId){
        Connection conn = getConnection();
        if(conn == null){
            return false;
        }
        String sql = "delete from application where act_id = "+actId+" and stu_id = '"+stuId+"'";
        //Log.d(TAG, "sql:"+sql);
        try {
            Statement sta = conn.createStatement();
            int n = sta.executeUpdate(sql);
            sta.close();
            conn.close();
            if(n == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //获取用户报名的所有活动信息
    public static List<ActivityInfo> getUserApplyActivities(String stuId,String collegeId){
        Connection conn = getConnection();
        if(conn == null){
            //Log.d(TAG, "conn为null,数据库连接失败！");
            return null;
        }
        String sql = "select * from activity join registration on activity.id = registration.act_id where id in ( select act_id from application where stu_id = "+stuId+") and registration.col_code in ('g','"+collegeId+"')";
        Log.d(TAG, "sql:"+sql);
        try {
            Statement sta = conn.createStatement();
            ResultSet res = sta.executeQuery(sql);
            List<ActivityInfo>list = new ArrayList<>();
            while (res.next()){
                ActivityInfo act = new ActivityInfo();
                act.setId(res.getInt("id"));
                act.setName(res.getString("name"));
                act.setDept(res.getString("dept").split(" "));
                act.setTime(res.getString("time"));
                @SuppressLint("SimpleDateFormat")
                String str_end_time = new SimpleDateFormat("y年M月d号 HH:mm").format(res.getTimestamp("endTime"));
                act.setEndTime(str_end_time);
                act.setSite(res.getString("site"));
                act.setAward(res.getString("award"));
                act.setAwardSign(res.getString("award_sign").split(" "));
                act.setDetails(res.getString("details"));
                act.setRemark(res.getString("remark"));
                act.setKind(res.getInt("act_kind"));
                act.setPeople(res.getInt("people"));
                act.setMan_people(res.getInt("man_people"));
                act.setWoman_people(res.getInt("woman_people"));
                act.setBench_people(res.getInt("bench_people"));
                act.setBench_man_people(res.getInt("bench_man_people"));
                act.setBench_woman_people(res.getInt("bench_woman_people"));
                act.setStatus(res.getInt("status"));
                act.setRange(res.getString("range"));
                act.setNumber(res.getInt("number"));
                act.setMan_number(res.getInt("man_number"));
                act.setWoman_number(res.getInt("woman_number"));
                act.setBench_number(res.getInt("bench_number"));
                act.setBench_man_number(res.getInt("bench_man_number"));
                act.setBench_woman_number(res.getInt("bench_woman_number"));
               list.add(act);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

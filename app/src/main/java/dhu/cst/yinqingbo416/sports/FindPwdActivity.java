package dhu.cst.yinqingbo416.sports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dhu.cst.yinqingbo416.sports.Utils.DBUtils;
import dhu.cst.yinqingbo416.sports.Utils.CheckInput;
import dhu.cst.yinqingbo416.sports.Entry.SUser;
import dhu.cst.yinqingbo416.sports.Utils.Tools;
import dhu.cst.yinqingbo416.sports.CustomControl.CustomTextInputLayout;

public class FindPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText email;
    private ImageView back_btn;
    private TextView find_btn;
    private CustomTextInputLayout userNameMessage;
    private CustomTextInputLayout emailMessage;
    private boolean []flag = {false,false};
    private boolean []sign = {false,false};
    private String errorMessage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        setStatusBarTransparent();//设置透明状态栏
        userName = findViewById(R.id.find_pwd_input_username);
        email = findViewById(R.id.find_pwd_input_email);
        back_btn = findViewById(R.id.find_pwd_back);
        find_btn = findViewById(R.id.find_pwd_btn);
        userNameMessage = findViewById(R.id.find_pwd_textInputLayout1);
        emailMessage = findViewById(R.id.find_pwd_textInputLayout2);
        back_btn.setOnClickListener(this);
        find_btn.setOnClickListener(this);
        id_preprocessor();
        email_preprocessor();
    }
    //设置透明状态栏
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_pwd_back:
                Intent intent = new Intent(FindPwdActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.find_pwd_btn:
                final String str_user = userName.getText().toString();
                final String str_email = email.getText().toString();
                errorMessage = "";
                boolean flog = true;
                for(int i = 0;i < 2;i++){
                    sign[i] = true;
                    if(!flag[i]){
                        showError(i);
                        flog = false;
                    }
                }
                if(flog){
                    final SUser user = new SUser();
                    user.setStudentId(str_user);
                    user.setEmail(str_email);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String name = DBUtils.checkEmail(user);
                            if(name.contains("Hi")){
                                Tools.resetPWD(str_user,name,str_email);
                                Looper.prepare();
                                Toast.makeText(FindPwdActivity.this,"重置密码已发送到你的邮箱，请查收！",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else {
                                Looper.prepare();
                                Toast.makeText(FindPwdActivity.this,name,Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }else {
                    errorMessage += "格式错误";
                    Toast.makeText(FindPwdActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //对学号输入进行预处理
    public void id_preprocessor(){
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isStudentid(s.toString());
                flag[0] = ret.equals(" ");
                if(sign[0]){
                    userNameMessage.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[0] = true;
                    String str = userName.getText().toString();
                    userNameMessage.setError(CheckInput.isStudentid(str));
                }
            }
        });
    }
    //对邮箱输入进行预处理
    public void email_preprocessor(){
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isEmail(s.toString());
                flag[1] = ret.equals(" ");
                if(sign[1]){
                    emailMessage.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[1] = true;
                    String ret = email.getText().toString();
                    emailMessage.setError(CheckInput.isEmail(ret));
                }
            }
        });
    }
    //显示错误信息
    public void showError(int i) {
        if(i == 0){
            String s1 = userName.getText().toString();
            errorMessage += "学号";
            userNameMessage.setError(CheckInput.isStudentid(s1));
        }else {
            String s2 = email.getText().toString();
            if(errorMessage.equals("")){
                errorMessage += "邮箱";
            }else {
                errorMessage += "、邮箱";
            }
            emailMessage.setError(CheckInput.isEmail(s2));
        }
    }
}

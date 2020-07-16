package dhu.cst.yinqingbo416.sports;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import dhu.cst.yinqingbo416.sports.Fragment.FingerprintDialogFragment;
import dhu.cst.yinqingbo416.sports.Fragment.FingerprintDialogFragment2;
import dhu.cst.yinqingbo416.sports.Utils.DBUtils;
import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.Utils.CheckInput;
import dhu.cst.yinqingbo416.sports.Entry.User;
import dhu.cst.yinqingbo416.sports.CustomControl.mAlertDialog;
import dhu.cst.yinqingbo416.sports.Utils.PassWordUtils;
import dhu.cst.yinqingbo416.sports.Utils.Tools;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText passWord;
    private TextView btnLogin;
    private ImageView back;
    private TextView signIn;
    private TextView fingerprint;
    private TextView forgetPassWord;
    private QMUITipDialog progressDialog;
    private TextInputLayout textInputLayout_user;
    private TextInputLayout textInputLayout_passWord;
    private boolean []flag = {false,false};
    private boolean []sign = {false,false};
    private String errorMessage = "";
    private KeyStore keyStore;
    private static final String DEFAULT_KEY_NAME = "default_key";
    private FingerprintDialogFragment2 fingerprintDialogFragment2;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        id_preprocessor();
        passWord_preprocessor();
        //状态栏初始化
        setStatusBarTransparent();
    }
    //初始换函数
    public void init(){
        userName = findViewById(R.id.login_input_layout_username);
        passWord = findViewById(R.id.login_input_layout_password);
        btnLogin = findViewById(R.id.login_btnLogin);
        back = findViewById(R.id.login_title_back);
        signIn = findViewById(R.id.login_title_signIn);
        fingerprint = findViewById(R.id.login_fingerprint);
        forgetPassWord = findViewById(R.id.login_forget);
        textInputLayout_user = findViewById(R.id.login_textInputLayout1);
        textInputLayout_passWord = findViewById(R.id.login_textInputLayout2);
        btnLogin.setOnClickListener(this);
        back.setOnClickListener(this);
        signIn.setOnClickListener(this);
        fingerprint.setOnClickListener(this);
        forgetPassWord.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btnLogin:
                String strUserName = userName.getText().toString();
                String strPassWord = passWord.getText().toString();
                progressDialog = new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在登录")
                        .create();
                boolean flog = true;
                errorMessage = "";
                for(int i = 0;i < 2;i++){
                    sign[i] = true;
                    if(!flag[i]){
                        showError(i);
                        flog = false;
                    }
                }
                if(flog){
                    progressDialog.show();
                    User user = new User(strUserName,strPassWord);
                    checkLogin(user);
                }else {
                    errorMessage += "格式错误";
                    Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_title_back:
                finish();
                break;
            case R.id.login_title_signIn:
                Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_forget:
                Intent intent1 = new Intent(LoginActivity.this,FindPwdActivity.class);
                startActivity(intent1);
                break;
            case R.id.login_fingerprint:
                //开启指纹登录页面
                startFingerprint();
                break;
        }
    }
    //连接数据库检查账号能否登录
    public void checkLogin(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret = DBUtils.checkLogin(LoginActivity.this,user);
                if(ret == 0){
                    progressDialog.cancel();
                    Looper.prepare();
                    mAlertDialog dialog = new mAlertDialog(LoginActivity.this,"提醒",getApplication().getString(R.string.attention1));
                    dialog.show();
                    Looper.loop();
                }else if(ret == 1){
                    progressDialog.cancel();
                    Tools.userId = userName.getText().toString();
                    Tools.password = passWord.getText().toString();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(ret == 2){
                    progressDialog.cancel();
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,getApplication().getString(R.string.attention3),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else if(ret == 3){
                    progressDialog.cancel();
                    Looper.prepare();
                    mAlertDialog dialog = new mAlertDialog(LoginActivity.this,"提醒",getApplication().getString(R.string.attention4));
                    dialog.show();
                    Looper.loop();
                }else {
                    progressDialog.cancel();
                }
            }
        }).start();
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
    //对学号输入进行预处理
    public void id_preprocessor(){
        //监听学号输入
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isStudentid(s.toString());
                flag[0] = ret.equals(" ");
                if(sign[0]){
                    textInputLayout_user.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //监听学号焦点
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String s = userName.getText().toString();
                    sign[0] = true;
                    textInputLayout_user.setError(CheckInput.isStudentid(s));
                }
            }
        });
    }
    //对密码输入进行预处理
    public void passWord_preprocessor(){
        //监听密码输入
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ret = CheckInput.isPassWordLength(s.toString());
                flag[1] = ret.equals(" ");
                if(sign[1]){
                    textInputLayout_passWord.setError(ret);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //监听密码焦点
        passWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sign[1] = true;
                    String s = passWord.getText().toString();
                    textInputLayout_passWord.setError(CheckInput.isPassWordLength(s));
                }
            }
        });
    }
    //显示错误信息
    public void showError(int i){
        switch (i){
            case 0:
                String s1 = userName.getText().toString();
                textInputLayout_user.setError(CheckInput.isStudentid(s1));
                if(errorMessage.equals("")){
                    errorMessage += "学号";
                }else {
                    errorMessage += "、学号";
                }
                break;
            case 1:
                String s2 = passWord.getText().toString();
                textInputLayout_passWord.setError(CheckInput.isPassWordLength(s2));
                if(errorMessage.equals("")){
                    errorMessage += "密码";
                }else {
                    errorMessage += "、密码";
                }
                break;
        }
    }
    //开启指纹识别
    public void startFingerprint(){
        if (supportFingerprint()) {
            initKey();
            initCipher();
        }
    }
    //检查是否支持指纹识别
    public boolean supportFingerprint(){
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "您的系统版本过低，不支持指纹功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(this, "您的手机不支持指纹功能", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(this, "您还未设置锁屏，请先设置锁屏并添加一个指纹", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "您至少需要在系统设置中添加一个指纹", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    //初始化对称密钥
    @TargetApi(23)
    private void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //初始化Cipher
    @TargetApi(23)
    private void initCipher() {
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            showFingerPrintDialog(cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //显示指纹识别UI
    private void showFingerPrintDialog(Cipher cipher) {
        fingerprintDialogFragment2 = new FingerprintDialogFragment2();
        fingerprintDialogFragment2.setCipher(cipher);
        fingerprintDialogFragment2.show(getFragmentManager(), "fingerprint");
    }
    //指纹认证成功
    public void onAuthenticated(){
        String [] userInfo = PassWordUtils.readPassword(this,"FingerprintPoint");
        if(userInfo[0] == "null"){
            Toast.makeText(this,"尚未开启指纹登录",Toast.LENGTH_SHORT).show();
        }else {
            User user = new User(userInfo[0],userInfo[1]);
            checkLogin(user);
        }
    }
}

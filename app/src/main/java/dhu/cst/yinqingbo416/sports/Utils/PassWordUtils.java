package dhu.cst.yinqingbo416.sports.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PassWordUtils {

    public static String TAG = "PasswordUtils";
    /**
     * 保存账号密码
     * @param context
     * @param number
     * @param password
     */
    public static void savePassword(Context context, String number, String password,String name) {
        Log.d(TAG, "savePassword: 加密前的账号："+number);
        Log.d(TAG, "savePassword: 加密前的密码："+password);
        //对得到number和password进行加密
        try {
            number = getDES(number,"YQBNumber");
            password = getDES(password,"YQBPassWord");
        } catch (Exception e) {
            Log.d(TAG, "savePassword: "+e.toString());
        }
        Log.d(TAG, "savePassword: 加密后的账号："+number);
        Log.d(TAG, "savePassword: 加密后的密码："+password);
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number", number);
        editor.putString("password", password);
        editor.commit();
    }

    //读取账号密码
    public static String[] readPassword(Context context,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String str[] = new String[]{sharedPreferences.getString("number", ""), sharedPreferences.getString("password", "")};
        Log.d(TAG, "readPassword: 解密前的账号："+str[0]);
        Log.d(TAG, "readPassword: 解密前的密码："+str[1]);
        try {
            str[0] = getDESOri(str[0],"YQBNumber");
            str[1] = getDESOri(str[1],"YQBPassWord");
        } catch (Exception e) {
            Log.d(TAG, "savePassword: "+e.toString());
        }
        Log.d(TAG, "readPassword: 解密后的账号："+str[0]);
        Log.d(TAG, "readPassword: 解密后的密码："+str[1]);
        return str;
    }
    //获取加密密钥key
    public static Key getKey(String keyString) {
        byte[] keyStringByte = keyString.getBytes();
        byte[] keyByte = new byte[8];
        for(int i = 0; i<keyStringByte.length && i < keyByte.length; i++) {
            keyByte[i] = keyStringByte[i];
        }
        Key key = new SecretKeySpec(keyByte,"DES");
        return key;
    }
    //将byte数组转换成16进制String
    public static String byteArr2HexStr(byte[] bytes) throws Exception {
        StringBuffer sb = new StringBuffer(bytes.length*2);
        for(int i = 0; i < bytes.length; i++){
            if((bytes[i] & 0xFF) < 0x10)
                sb.append("0");
            sb.append(Integer.toHexString(bytes[i]&0xFF));
        }
        return sb.toString();
    }
    //将16进制string转换成byte数组
    public static byte[] hexStr2ByteArr(String str) throws Exception {

        byte[] bytes = str.getBytes();

        int len = bytes.length;
        byte[] arr = new byte[len/2];
        for(int i = 0; i < len; i=i+2) {
            String tmp = new String(bytes,i,2);
            arr[i/2] = (byte) Integer.parseInt(tmp,16);
        }
        return arr;
    }
    //对字符串进行DES加密
    public static String getDES(String val, String key) throws Exception {
        if(val == null || key == null)
            return null;
        Cipher encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE,getKey(key));
        byte[] cipherByte = encryptCipher.doFinal(val.getBytes());

        return byteArr2HexStr(cipherByte);
    }
    //对DES加密后的16进制字符串进行解密
    public static String getDESOri(String val, String key) throws Exception {
        if(val == null || key == null)
            return null;
        Cipher decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE,getKey(key));
        byte[] originalByte = decryptCipher.doFinal(hexStr2ByteArr(val));
        return new String(originalByte);
    }
}
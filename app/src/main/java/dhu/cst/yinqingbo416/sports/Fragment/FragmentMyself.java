package dhu.cst.yinqingbo416.sports.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import dhu.cst.yinqingbo416.sports.MainActivity;
import dhu.cst.yinqingbo416.sports.R;
import dhu.cst.yinqingbo416.sports.Utils.PassWordUtils;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class FragmentMyself extends Fragment {
    private QMUIGroupListView groupListView;
    private MainActivity mainActivity;
    private QMUICommonListItemView item6;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself,container,false);
        groupListView = view.findViewById(R.id.fm_groupListView);
        initGroupListView();
        return view;
    }
    private void initGroupListView(){
        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height)+30;
        int size = QMUIDisplayHelper.dp2px(getContext(), 35);
        //item点击事件
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"你点击了item",Toast.LENGTH_SHORT).show();
            }
        };
        Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.personal_info);
        QMUICommonListItemView item1 = groupListView.createItemView(img,"个人信息",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.interest_sign);
        QMUICommonListItemView item2 = groupListView.createItemView(img,"兴趣标签",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.club);
        QMUICommonListItemView item3 = groupListView.createItemView(img,"社团",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.leave_words);
        QMUICommonListItemView item4 = groupListView.createItemView(img,"留言",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.run);
        QMUICommonListItemView item5 = groupListView.createItemView(img,"课外锻炼查询",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.fingerprint);
        item6 = groupListView.createItemView("指纹登录");
        item6.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        item6.setImageDrawable(img);
        item6.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        item6.getSwitch().setChecked(Tools.fingerprint);
        Tools.currentFingerprint = Tools.fingerprint;
        item6.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Tools.currentFingerprint == Tools.fingerprint){
                    mainActivity = (MainActivity) getActivity();
                    mainActivity.startFingerprint();
                }
                Tools.currentFingerprint = isChecked;
            }
        });
        img = ContextCompat.getDrawable(getContext(),R.drawable.setting);
        QMUICommonListItemView item7 = groupListView.createItemView(img,"账号设置",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        QMUIGroupListView.newSection(getContext())
                .addItemView(item1,onClickListener)
                .addItemView(item2,onClickListener)
                .addItemView(item3,onClickListener)
                .addItemView(item4,onClickListener)
                .addItemView(item5,onClickListener)
                .addItemView(item6, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .addItemView(item7,onClickListener)
                .setTitle(null).setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addTo(groupListView);
    }
    public void fingerprintSwitch(){//指纹识别成功
        if(!Tools.fingerprint){//开启指纹识别
            //Toast.makeText(getContext(), "指纹登录已开启", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("stuData", Context.MODE_PRIVATE).edit();
            editor.putBoolean("fingerprint",true);
            editor.apply();
            Tools.fingerprint = true;
            //储存用户名+密码
            PassWordUtils.savePassword(getContext(),Tools.userId,Tools.password,"FingerprintPoint");
        }else {//关闭指纹识别
            //Toast.makeText(getContext(), "指纹登录已关闭", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("stuData", Context.MODE_PRIVATE).edit();
            editor.putBoolean("fingerprint",false);
            editor.apply();
            Tools.fingerprint = false;
            //清除用户名+密码
            PassWordUtils.savePassword(getContext(),"null","null","FingerprintPoint");
        }
    }
    public void setFingerprintSwitch(boolean fingerprintSwitch){
        item6.getSwitch().setChecked(fingerprintSwitch);
    }
}

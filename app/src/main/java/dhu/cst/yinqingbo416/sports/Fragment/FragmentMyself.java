package dhu.cst.yinqingbo416.sports.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import dhu.cst.yinqingbo416.sports.R;

public class FragmentMyself extends Fragment {
    private QMUIGroupListView groupListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myself,container,false);
        groupListView = view.findViewById(R.id.fm_groupListView);
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
        img = ContextCompat.getDrawable(getContext(),R.drawable.push);
        QMUICommonListItemView item5 = groupListView.createItemView(img,"消息推送",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.fingerprint);
        QMUICommonListItemView item6 = groupListView.createItemView(img,"指纹登录",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        img = ContextCompat.getDrawable(getContext(),R.drawable.setting);
        QMUICommonListItemView item7 = groupListView.createItemView(img,"账号设置",null,
                QMUICommonListItemView.HORIZONTAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
        QMUIGroupListView.newSection(getContext())
                .addItemView(item1,onClickListener)
                .addItemView(item2,onClickListener)
                .addItemView(item3,onClickListener)
                .addItemView(item4,onClickListener)
                .addItemView(item5,onClickListener)
                .addItemView(item6,onClickListener)
                .addItemView(item7,onClickListener)
                .setTitle(null).setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addTo(groupListView);
        return view;
    }
}

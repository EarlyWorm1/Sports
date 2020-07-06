package dhu.cst.yinqingbo416.sports.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

import dhu.cst.yinqingbo416.sports.Entry.News;
import dhu.cst.yinqingbo416.sports.NewsActivity;
import dhu.cst.yinqingbo416.sports.R;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class FragmentNews extends Fragment {
    private QMUIGroupListView groupListView;
    private TextView userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        userName = view.findViewById(R.id.fn_userName);
        userName.setText("Hi,"+Tools.userName);
        groupListView = view.findViewById(R.id.fn_groupListView);
        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height)+50;
        int size = QMUIDisplayHelper.dp2px(getContext(), 40);
        final List<News>newsList = DataSupport.order("id desc").find(News.class);
        if(newsList == null || newsList.size() == 0){
            Toast.makeText(getContext(),"暂无消息通知",Toast.LENGTH_SHORT).show();
        }else {
            QMUIGroupListView.Section section = QMUIGroupListView.newSection(getContext());
            View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v instanceof QMUICommonListItemView){
                        ((QMUICommonListItemView) v).showNewTip(false);
                        int key = (int) v.getTag();
                        News news = newsList.get(key-1);
                        news.setNew(false);
                        news.save();
                        Intent intent = new Intent(getContext(), NewsActivity.class);
                        intent.putExtra("news",news);
                        startActivity(intent);
                        //Toast.makeText(getContext(),news.getTitle(),Toast.LENGTH_SHORT).show();
                    }
                }
            };
            int key = 1;
            for(News news:newsList){
                Drawable img = Tools.getDrawable(getContext(),news.getImgkind());
                QMUICommonListItemView item = groupListView.createItemView(img,news.getTitle(),news.getIntroduce(),
                        QMUICommonListItemView.VERTICAL,QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,height);
                item.setTipPosition(QMUICommonListItemView.TIP_POSITION_LEFT);
                item.showNewTip(news.isNew());
                item.setTag(key);
                key++;
                section.addItemView(item,mOnClickListener);
            }
            section.setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT).addTo(groupListView);
        }
        return view;
    }
}

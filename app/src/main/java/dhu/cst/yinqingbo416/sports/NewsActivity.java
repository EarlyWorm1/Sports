package dhu.cst.yinqingbo416.sports;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import dhu.cst.yinqingbo416.sports.Entry.News;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class NewsActivity extends AppCompatActivity {
    TextView title;
    ImageView img;
    TextView dept;
    TextView detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        News news = (News) getIntent().getSerializableExtra("news");
        title = findViewById(R.id.news_act_title);
        img = findViewById(R.id.news_act_dept_icon);
        dept = findViewById(R.id.news_act_dept_name);
        detail = findViewById(R.id.news_act_detail);
        title.setText(news.getTitle());
        Drawable drawable = Tools.getDrawable(this,news.getImgkind());
        img.setImageDrawable(drawable);
        dept.setText(Tools.getDeptName(news.getImgkind()));
        detail.setText(news.getDetail());
    }
}
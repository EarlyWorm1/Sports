package dhu.cst.yinqingbo416.sports.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dhu.cst.yinqingbo416.sports.ApplyActivity;
import dhu.cst.yinqingbo416.sports.CustomControl.TextViewBorder;
import dhu.cst.yinqingbo416.sports.Entry.ActivityInfo;
import dhu.cst.yinqingbo416.sports.R;
import dhu.cst.yinqingbo416.sports.Utils.Tools;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private List<ActivityInfo>mActivityList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView site;
        TextView people;
        LinearLayout awardContainer;
        TextViewBorder sign;
        TextViewBorder sign1;
        TextViewBorder sign2;
        TextViewBorder sign3;
        TextViewBorder sign4;
        TextViewBorder sign5;
        TextViewBorder sign6;
        ImageView applySign;
        View listItemView;
        TextView line;
        TextView peopleIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            listItemView = itemView;
            title = itemView.findViewById(R.id.activity_title);
            time = itemView.findViewById(R.id.activity_time);
            site = itemView.findViewById(R.id.activity_site);
            people = itemView.findViewById(R.id.activity_people);
            awardContainer = itemView.findViewById(R.id.activity_award_container);
            sign = itemView.findViewById(R.id.activity_title_icon);
            sign1 = itemView.findViewById(R.id.activity_award_sign1);
            sign2 = itemView.findViewById(R.id.activity_award_sign2);
            sign3 = itemView.findViewById(R.id.activity_award_sign3);
            sign4 = itemView.findViewById(R.id.activity_award_sign4);
            sign5 = itemView.findViewById(R.id.activity_award_sign5);
            sign6 = itemView.findViewById(R.id.activity_award_sign6);
            applySign = itemView.findViewById(R.id.apply_sign);
            peopleIcon = itemView.findViewById(R.id.activity_people_icon);
            line = itemView.findViewById(R.id.activity_line);
        }
    }
    public ActivityAdapter(List<ActivityInfo>ActivityList){
        mActivityList = ActivityList;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ActivityInfo act = mActivityList.get(position);
                if(act.getStatus() == 1){
                    Intent intent = new Intent(v.getContext(), ApplyActivity.class);
                    intent.putExtra("Position",position);
                    intent.putExtra("Act",act);
                    ((Activity)v.getContext()).startActivityForResult(intent,1);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityInfo activityInfo = mActivityList.get(position);
        if(activityInfo.getStatus() == 0){
            int grayColor = Color.parseColor("#c0c0c0");
            holder.title.setTextColor(grayColor);
            holder.sign.setBorderColor(grayColor);
            holder.sign.setTextColor(grayColor);
            holder.sign1.setTextColor(grayColor);
            holder.sign1.setBorderColor(grayColor);
            holder.sign2.setTextColor(grayColor);
            holder.sign2.setBorderColor(grayColor);
            holder.sign3.setTextColor(grayColor);
            holder.sign3.setBorderColor(grayColor);
            holder.sign4.setTextColor(grayColor);
            holder.sign4.setBorderColor(grayColor);
            holder.sign5.setTextColor(grayColor);
            holder.sign5.setBorderColor(grayColor);
            holder.sign6.setTextColor(grayColor);
            holder.sign6.setBorderColor(grayColor);
            holder.people.setTextColor(grayColor);
            holder.time.setTextColor(grayColor);
            holder.site.setTextColor(grayColor);
            holder.peopleIcon.setTextColor(grayColor);
            holder.line.setTextColor(grayColor);
        }
        int kind = activityInfo.getKind();
        String str_people = "";
        if(kind == 1 || kind == 3){//1、不分学院，不分性别 3、分学院，不分性别，不分替补
            str_people = activityInfo.getPeople()+"/"+activityInfo.getNumber();
        }else if(kind == 2 || kind == 4){//2、不分学院，分性别 4、分学院，分性别
            str_people = "男:"+activityInfo.getMan_people()+"/"+activityInfo.getMan_number() + "  女:"+activityInfo.getWoman_people()+"/"+activityInfo.getWoman_number();
        }else if(kind == 5){//分学院，不分性别，分替补
            str_people = "首发:"+activityInfo.getPeople()+"/"+activityInfo.getNumber() + "  替补:"+activityInfo.getBench_people()+"/"+activityInfo.getBench_number();
        }else if(kind == 6){//6、分学院 分性别，分替补
            str_people = "首发(男:"+activityInfo.getMan_people()+"/"+activityInfo.getMan_number()+" 女:"+activityInfo.getWoman_people()+"/"+activityInfo.getWoman_number()
                         +")  替补:"+activityInfo.getBench_people()+"/"+activityInfo.getBench_number();
        }else if(kind == 7){//7、分学院，分性别，分替补，替补分性别
            str_people = "首发(男:"+activityInfo.getMan_people()+"/"+activityInfo.getMan_number()+" 女:"+activityInfo.getWoman_people()+"/"+activityInfo.getWoman_number()
                    +")\t替补(男:"+activityInfo.getBench_man_people()+"/"+activityInfo.getBench_man_number()+" 女:"+activityInfo.getBench_woman_people()+"/"+activityInfo.getBench_woman_number()+")";
        }
        holder.people.setText(str_people);
        holder.time.setText(activityInfo.getTime());
        holder.title.setText(activityInfo.getName());
        holder.site.setText(activityInfo.getSite());
        String [] award = activityInfo.getAwardSign();
        int i = 0;
        if(award.length != 0){//设置奖励标签
            holder.awardContainer.setVisibility(View.VISIBLE);
            for(i = 1;i <= award.length;i++){
                switch (i){
                    case 1:
                        holder.sign1.setText(award[0]);
                        holder.sign1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.sign2.setText(award[1]);
                        holder.sign2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        holder.sign3.setText(award[2]);
                        holder.sign3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        holder.sign4.setText(award[3]);
                        holder.sign4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        holder.sign5.setText(award[4]);
                        holder.sign5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        holder.sign6.setText(award[5]);
                        holder.sign6.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
            for(int k = i;k <= 6;k++){//确保关闭没有奖励的标签
                switch (k){
                    case 1:
                        holder.sign1.setVisibility(View.GONE);
                        break;
                    case 2:
                        holder.sign2.setVisibility(View.GONE);
                        break;
                    case 3:
                        holder.sign3.setVisibility(View.GONE);
                        break;
                    case 4:
                        holder.sign4.setVisibility(View.GONE);
                        break;
                    case 5:
                        holder.sign5.setVisibility(View.GONE);
                        break;
                    case 6:
                        holder.sign6.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
            //获取已报名的活动id
            List<Integer> applyActId = Tools.getApplyActId();
            if(applyActId.contains(activityInfo.getId())){
                holder.applySign.setVisibility(View.VISIBLE);
            }else {
                holder.applySign.setVisibility(View.GONE);
            }
        }else {
            holder.awardContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}

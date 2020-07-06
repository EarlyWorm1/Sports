package dhu.cst.yinqingbo416.sports.CustomControl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import dhu.cst.yinqingbo416.sports.R;

public class mAlertDialog {
    private Context context;
    private String title;
    private String message;
    private AlertDialog.Builder dialog;
    private AlertDialog dia;

    public mAlertDialog(Context context,String title,String message) {
        this.context = context;
        this.title = title;
        this.message = message;
        dialog = new AlertDialog.Builder(context);
    }
    public mAlertDialog(Context context,String message){
        this.context = context;
        this.message = message;
        dialog = new AlertDialog.Builder(context);
    }
    public void show(){//简单提示信息+确定按钮
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dia = dialog.show();
    }
    public void progressbar_show(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.progress_dialog,null);
        dialog.setView(view);
        TextView textView = view.findViewById(R.id.dialog_message);
        textView.setText(message);
        dialog.setCancelable(false);
        dia = dialog.show();
    }
    public void cancel(){
        dia.dismiss();
    }
}

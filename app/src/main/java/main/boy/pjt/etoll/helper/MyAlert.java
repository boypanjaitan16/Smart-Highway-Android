package main.boy.pjt.etoll.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import main.boy.pjt.etoll.R;


/**
 * Created by Boy Panjaitan on 01/12/2017.
 */

public class MyAlert {

    private Context context;
    private AlertDialog.Builder builder;
    private boolean cancelable;
    private ProgressDialog progressDialog;

    public MyAlert(Context context, boolean cancelable){
        this.context    = context;
        this.builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
    }

    public void loaderStart(){
        progressDialog  = new ProgressDialog(context);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void loaderStop(){
        progressDialog.dismiss();
    }

    public void alertInfo(String message){
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertInfoTitle(String title, String message){
        builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_info_small)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void alertInfoTitle(String title, String message, int icon){
        builder = new AlertDialog.Builder(context);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void alertWithConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
        builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_help_small)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ya", positive)
                .setNegativeButton("Tidak", negative);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertWithConfirm(String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("Ya", positive)
                .setNegativeButton("Tidak", negative);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertWithConfirm(String title, String message,int icon, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
        builder = new AlertDialog.Builder(context);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ya", positive)
                .setNegativeButton("Tidak", negative);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

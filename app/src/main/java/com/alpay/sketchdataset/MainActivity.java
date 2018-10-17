package com.alpay.sketchdataset;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alpay.sketchdataset.view.InkView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    InkView inkView;
    String[] popUpTexts;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareInkView();
        popUpTexts = getResources().getStringArray(R.array.popUpTexts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDialogPopUp(popUpTexts[cnt]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                inkView.clear();
                return true;
            case R.id.menu_draw:
                inkView.setColor(Color.BLACK);
                return true;
            case R.id.menu_erase:
                inkView.setColor(Color.WHITE);
                return true;
            case R.id.menu_send:
                if(cnt < popUpTexts.length-1){
                    cnt++;
                    setDialogPopUp(popUpTexts[cnt]);
                    saveBitmap();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareInkView(){
        inkView = (InkView) findViewById(R.id.ink);
        inkView.hasFlag(InkView.FLAG_INTERPOLATION);
        inkView.addFlag(InkView.FLAG_RESPONSIVE_WIDTH);
        inkView.setBackgroundColor(Color.WHITE);
    }

    private void saveBitmap(){
        Bitmap bitmap = inkView.getBitmap();
        Bitmap bmp = inkView.getBitmap();
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, 0, 0, null);
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Sketchpad";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "sketchpad" + System.currentTimeMillis() + ".png");
        try{
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inkView.clear();
    }

    private void setDialogPopUp(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
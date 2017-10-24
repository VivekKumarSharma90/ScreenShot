package com.vivek.android2.screenshot;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    TextView hello_word;

    //xkhkzh
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello_word = (TextView) findViewById(R.id.hello_word);
        hello_word.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    screeShot();
                    return;
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(hello_word, "ljsflsjflsf", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    });
                } else
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            screeShot();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void screeShot() {
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "screen_shot1" + ".png";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            //
            /*Bitmap new_bitmap = getOverlayBitmap(MainActivity.this, bitmap,"sjkl");*/

            //Bitmap bitmap2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shahrukh_icon), 120, 120, false);
            //Bitmap bitmap1 = overlay(bitmap, bitmap2);

            //new_bitmap.recycle();
            //
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
            //shareImage(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "DO like my app. It is very interesting. DO like my app. It is very interesting. " +
                "DO like my app. It is very interesting. DO like my app. It is very interesting. DO like my app. It is very interesting.");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap getOverlayBitmap(Context context, Bitmap bitmap, String text) {
        Bitmap result = bitmap.copy(bitmap.getConfig(), true);
        float scale = context.getResources().getDisplayMetrics().density;
        Canvas canvas = new Canvas(result);
        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setTextSize((int) (16 * scale));
        mTextPaint.setColor(Color.RED);
        mTextPaint.setAlpha(38);
        StaticLayout mTextLayout = new StaticLayout(text, mTextPaint, canvas.getWidth() + 700, Layout.Alignment.ALIGN_CENTER, 1.1f, 0.3f, true);
        canvas.save();

        float textX = -200;
        float textY = -10;

        canvas.translate(textX, textY);
        mTextLayout.draw(canvas);
        canvas.restore();
        return result;
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        //canvas.drawBitmap(bmp2, new Matrix(), null);
        canvas.drawBitmap(bmp2, 100, getWindow().getWindowManager().getDefaultDisplay().getWidth(), null);
        return bmOverlay;
    }
}

package com.example.image_editor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap originalBitmap;
      int desiredWidth = 100;
      int desiredHeight = 200;

    private boolean isCropped = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        imageView.setImageBitmap(originalBitmap);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isCropped) {
                        imageView.setImageBitmap(originalBitmap);
                    }
                    else {
                        float touchX = event.getX();
                        float touchY = event.getY();
                        Bitmap croppedBitmap = performCropping(originalBitmap, touchX,touchY);
                        Bitmap resizedBitmap = performResizing(croppedBitmap, desiredWidth, desiredHeight);
                        imageView.setImageBitmap(resizedBitmap);
                    }
                    isCropped = !isCropped;
                }
                return true;
            }
        });
    }

    private Bitmap performResizing(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private Bitmap performCropping(Bitmap originalBitmap, float touchX, float touchY) {

        float scaleFactorX = (float) originalBitmap.getWidth() / imageView.getWidth();
        float scaleFactorY = (float) originalBitmap.getHeight() / imageView.getHeight();

        float bitmapTouchX = touchX * scaleFactorX;
        float bitmapTouchY = touchY * scaleFactorY;

        int left = (int) (bitmapTouchX - (desiredWidth / 2));
        int top = (int) (bitmapTouchY - (desiredHeight / 2));

        left = Math.max(0, left);
        top = Math.max(0, top);
        left = Math.min(left, originalBitmap.getWidth() - desiredWidth);
        top = Math.min(top, originalBitmap.getHeight() - desiredHeight);

        return Bitmap.createBitmap(originalBitmap, left, top, desiredWidth, desiredHeight);
    }

}

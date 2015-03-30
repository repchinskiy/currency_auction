package com.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.example.currency_auction.R;

import java.util.Random;

/**
 * Created by Bizon on 29.03.2015.
 */
public class GraphicActivity extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.graphic, container, false);
        initContent();
        return rootView;
    }

    private void initContent() {
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int w = rootView.getMeasuredWidth();
                    int h = rootView.getMeasuredHeight();
                    drawGraphic(w, h);
                    System.out.println("BIZON viewTreeObserver rootView: " + rootView.getWidth() + ", " + rootView.getHeight());
                }
            });
        }
    }

    private int sign() {
        Random random = new Random();
        int sign = random.nextInt(4) - 2;
        if (sign == 0) {
            return sign();
        }
//        while (sign == 0) {
//            sign = random.nextInt(4) - 2;
//        }

        return sign;
    }

    private float[] generateMockPoints(int l, int t, int w, int h, int size, int epsilon) {
        float[] points = new float[2 * size];
        Random random = new Random();
        points[0] = l;
        points[1] = random.nextInt((int) (1.2f * h));
        int step = w / size;

        for (int i = 2; i < 2 * size; i++) {
            int mod4 = i % 4;
            if (mod4 == 0 || mod4 == 1) {
                points[i] = points[i - 2];
            } else {
                if (i % 2 == 0) {
                    points[i] = points[i - 2] + step;
                } else {
                    float candidate = points[i - 2] + sign() * random.nextInt(epsilon);
                    if (candidate > h) candidate = 0.7f * h;
                    if (candidate < t) candidate = 150;
                    points[i] = candidate;
                }

            }
        }

        return points;
    }

    private void drawGrid(Paint paint, Canvas canvas, float l, float t, float w, float step) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0.5f);
        canvas.drawRect(l, t, l + w, t + 2 * w, paint);

        int size = (int) (2 * w / step);

        float top = t;
        for (int i = 0; i < size + 1; i++) {
            canvas.drawLine(l, top, l + w, top, paint);
            top += step;
        }
    }

    private void drawGraphic(int w, int h) {
        Paint paint = new Paint();
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        int epsilon = 50;
        int size = 70;
        int wMax = (int) (w * 0.607f);
        int hMax = (int) (h * 1.3f);

        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
//        paint.setStrokeWidth(6f);
//        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);


        paint.setColor(Color.BLUE);
        canvas.drawLines(generateMockPoints(50, 50, wMax, hMax, size, epsilon), paint);
        paint.setColor(Color.GREEN);
        canvas.drawLines(generateMockPoints(50, 50, wMax, hMax, size, epsilon), paint);
        paint.setColor(Color.RED);
        canvas.drawLines(generateMockPoints(50, 50, wMax, hMax, size, epsilon), paint);

//        paint.setColor(Color.GRAY);
//        paint.setStrokeWidth(1f);
//        canvas.drawLine(0f, 0f, 0f, (float) h, paint);

        drawGrid(paint, canvas, 50, 50, w * 0.3f, 50f);

        if (Build.VERSION.SDK_INT >= 16) {
            rootView.setBackground(new BitmapDrawable(getResources(), bg));
        } else {
            rootView.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
        }
    }
}

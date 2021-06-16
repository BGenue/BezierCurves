package com.exam.beziercurves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView bezier1;
    ImageView bezier2;
    ImageView bezier3;

    TextView bezier1Text;

    int index = 100;

    int width = 0;
    int height = 0;

    float startX = 0;
    float startY = 0;
    float middleX = 0;
    float middleY = 0;
    float len = 0;
    float endX = 0;
    float endY = 0;

    float time1 = 0f;
    float time2 = 0f;
    float time3 = 0f;

    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Canvas canvas1;
    Canvas canvas2;
    Canvas canvas3;
    Paint tmpPaint;
    Paint progressPaint;
    Paint startPaint;
    Paint EndPaint;

    Path path2;

    Handler handler;

    float[] t;

    boolean task1Finished = false;

    MyAsyncTasks1 task1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bezier1 = findViewById(R.id.bezier1);
        bezier2 = findViewById(R.id.bezier2);
        bezier3 = findViewById(R.id.bezier3);
        bezier1Text = findViewById(R.id.bezier1Text);

        handler = new Handler();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        width = bezier1.getWidth();
        height = bezier1.getHeight();

        startX = (width / 10);
        startY = (height / 10) * 9;
        endX = (width / 10) * 9;
        endY = (height / 10) * 9;

        startPaint = new Paint();
        startPaint.setColor(Color.BLUE);

        tmpPaint = new Paint();
        tmpPaint.setColor(Color.LTGRAY);
        tmpPaint.setStrokeWidth(3);

        progressPaint = new Paint();
        progressPaint.setColor(Color.BLUE);
        progressPaint.setStrokeWidth(3);

        path2 = new Path();


        EndPaint = new Paint();
        EndPaint.setColor(Color.LTGRAY);

        initBezier1();
        initBezier2();
//        initBezier3();
    }

    public void initBezier1() {
        t = doDivide(startX, endX);
        MyAsyncTasks1 task = new MyAsyncTasks1(this);
        task.execute();
    }

    public void initBezier2() {
        MyAsyncTasks2 task2 = new MyAsyncTasks2(this);
        task2.execute();
    }

    public void initBezier3() {
        MyAsyncTasks3 task3 = new MyAsyncTasks3(this);
        task3.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public float[] doDivide(float start, float end) {
        float[] after = new float[index];
        float len = (end - start) / index;
        for (int i = 0; i < index; i++) {
            after[i] = start + len * (i + 1);
        }
        return after;
    }


    private class MyAsyncTasks1 extends AsyncTask<Float, Float, Float> {
        Context mcontext;
        int current;

        public MyAsyncTasks1(Context context) {
            mcontext = context;
        }

        @Override
        protected void onPreExecute() {
            //백그라운드 실행 전 메인에서 호출되는 메서드
            //ui 초기화 작업
            Log.d(">>>>", "onPreExecute");
            time1 = 0;
            current = 0;
            task1Finished = false;
            bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas1 = new Canvas(bitmap1);
            canvas1.drawColor(Color.BLACK);

//            Path path = new Path();
//            path.moveTo(startX, startY);
//            path.lineTo(endX, endY);

            canvas1.drawLine(startX, startY, endX, endY, tmpPaint);
            canvas1.drawCircle(startX, startY, 5, startPaint);
            canvas1.drawCircle(endX, endY, 5, EndPaint);

            bezier1.setImageBitmap(bitmap1);
            bezier1Text.setText(String.format("%.2f", time1));

//            len = (endX - startX) / index;
//            middleX = startX;
//            middleY = startY;

//            path2.moveTo(startX, startY);

        }

        @Override
        protected Float doInBackground(Float[] floats) {
            //백그라운드 실행
            //ui 제어 금지
            Log.d(">>>>", "여기는");
            while (current < index) {
                Log.d(">>>>", "doInBackground");
                time1 = (float) ((current + 1) / index + (current + 1) % index * 0.01);
                publishProgress();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current++;
//                current++;
            }
//            while (time1 < 10) {
//                middleX += len;
//                time1 += 0.1;
//                Log.d(">>>>", "startX " + startX + " middleX " + middleX + " middleY " + middleY);
//                publishProgress();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            task1Finished = true;

            Log.d(">>>>", "끝내");
            return 1f;
        }


        @Override
        protected void onPostExecute(Float result) {
            //백그라운드 실행 결과
            Log.d(">>>>", "onPostExecute");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initBezier1();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            //ui 갱신
            Log.d(">>>>", "onProgressUpdate " + current);
            if (!task1Finished) {
                canvas1 = new Canvas(bitmap1);
                canvas1.drawColor(Color.BLACK);

                canvas1.drawLine(startX, startY, endX, endY, tmpPaint);
                canvas1.drawCircle(startX, startY, 5, startPaint);
                canvas1.drawCircle(endX, endY, 5, EndPaint);

                canvas1.drawLine(startX, startY, t[current], startY, progressPaint);
                canvas1.drawCircle(t[current], startY, 5, progressPaint);
                bezier1.setImageBitmap(bitmap1);
                bezier1Text.setText(String.format("%.2f", time1));
            }
        }
    }


    private class MyAsyncTasks2 extends AsyncTask<Float, Float, Float> {
        Context mcontext;

        public MyAsyncTasks2(Context context) {
            mcontext = context;
        }

        @Override
        protected void onPreExecute() {
            //백그라운드 실행 전 메인에서 호출되는 메서드
            //ui 초기화 작업
            time2 = 0;
            bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas2 = new Canvas(bitmap2);
            canvas2.drawColor(Color.BLACK);

            canvas2.drawCircle(startX, startY, 5, startPaint);
            canvas2.drawCircle(endX, endY, 5, EndPaint);

            bezier2.setImageBitmap(bitmap2);
        }

        @Override
        protected Float doInBackground(Float[] floats) {
            //백그라운드 실행
            //ui 제어 금지
            while (time2 < 10) {
                time2++;
            }

//            while (time1 < 10) {
//                middleX += len;
//                time1 += 0.1;
//                Log.d(">>>>", "startX " + startX + " middleX " + middleX + " middleY " + middleY);
//                publishProgress();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            return null;
        }


        @Override
        protected void onPostExecute(Float result) {
            //백그라운드 실행 결과
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initBezier2();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            //ui 갱신
//            canvas1.drawLine(startX, startY, middleX, middleY, progressPaint);
//            bezier1.setImageBitmap(bitmap1);
//            bezier1Text.setText(String.format("%.1f", time1));
        }
    }

    private class MyAsyncTasks3 extends AsyncTask<Float, Float, Float> {
        Context mcontext;

        public MyAsyncTasks3(Context context) {
            mcontext = context;
        }

        @Override
        protected void onPreExecute() {
            //백그라운드 실행 전 메인에서 호출되는 메서드
            //ui 초기화 작업
            time3 = 0;

            bitmap3 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas3 = new Canvas(bitmap3);
            canvas3.drawColor(Color.BLACK);

            canvas3.drawCircle(startX, startY, 5, startPaint);
            canvas3.drawCircle(endX, endY, 5, EndPaint);

            bezier3.setImageBitmap(bitmap3);
        }

        @Override
        protected Float doInBackground(Float[] floats) {
            //백그라운드 실행
            //ui 제어 금지
//            while (time1 < 10) {
//                middleX += len;
//                time1 += 0.1;
//                Log.d(">>>>", "startX " + startX + " middleX " + middleX + " middleY " + middleY);
//                publishProgress();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            return null;
        }


        @Override
        protected void onPostExecute(Float result) {
            //백그라운드 실행 결과
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initBezier3();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            //ui 갱신
//            canvas1.drawLine(startX, startY, middleX, middleY, progressPaint);
//            bezier1.setImageBitmap(bitmap1);
//            bezier1Text.setText(String.format("%.1f", time1));
        }
    }
}
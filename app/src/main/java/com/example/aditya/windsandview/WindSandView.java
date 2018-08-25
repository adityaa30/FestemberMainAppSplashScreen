package com.example.aditya.windsandview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class WindSandView extends View implements Runnable {

    private Context mContext;
    private LeafSprite mLeaf;
    private ParticleSprite mSandCenter;
    private ParticleSprite mSandTop;
    private ParticleSprite mSandBottom;

    private final static int LEAF_COUNT = 5;

    public WindSandView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRGB(255, 255, 255);
            mLeaf.draw(canvas);
            //mSandTop.draw(canvas);
            //mSandBottom.draw(canvas);
            mSandCenter.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
            }
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
        }
    }

    private void initialize(){

        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;

        Bitmap leafBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.leaf);
        leafBitmap = getResizedBitmap(leafBitmap, leafBitmap.getWidth()/70, leafBitmap.getHeight()/70);
        mLeaf = new LeafSprite(leafBitmap,  10,-leafBitmap.getWidth() ,500, 3000);
        mLeaf.setSpeedRange(-0.06f, 0.64f, -0f, 0.025f);
        mLeaf.setAccelerationRange(-0.00001f,0.00001f, 30, 30);
        mLeaf.initialize();

        Bitmap sandBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dust);
        sandBitmap = getResizedBitmap(sandBitmap, sandBitmap.getWidth()/1.5f, sandBitmap.getHeight()/1.5f);
        mSandCenter = new ParticleSprite(sandBitmap,  200, -sandBitmap.getWidth(), 4*screenHeight/10,3000);
        mSandCenter.setSpeedRange(-0.06f, 0.48f, -0.125f, 0.125f);
        mSandCenter.setAccelerationRange(-0.00001f,0.00001f, 0, 180);
        mSandCenter.initialize();

        /*mSandTop = new ParticleSprite(sandBitmap, 20, -sandBitmap.getWidth(), screenHeight/10, 3000);
        mSandTop.setSpeedRange(-0.06f, 0.64f, -0.125f, 0.125f);
        mSandTop.setAccelerationRange(-0.00001f,0.00001f, 30, 30);
        mSandTop.initialize();

        mSandBottom = new ParticleSprite(sandBitmap, 20, -sandBitmap.getWidth(), 7*screenHeight/10, 3000);
        mSandBottom.setSpeedRange(-0.06f, 0.64f, -0.125f, 0.125f);
        mSandBottom.setAccelerationRange(-0.00001f,0.00001f, 0, 180);
        mSandBottom.initialize(); */


    }

    private Bitmap getResizedBitmap(Bitmap bm, float newWidth, float newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (newWidth) / width;
        float scaleHeight = (newHeight) / height;
        // Create a matrix for manipulation
        Matrix matrix = new Matrix();
        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // "Recreate" the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}

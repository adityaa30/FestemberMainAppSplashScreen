package com.example.aditya.windsandview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class LeafSprite {

    private Bitmap mLeafBitmap;

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private int mX; // Initial x coordinate would be same for every bitmap
    private int mY; // Initial y coordinate would be same for every bitmap

    private ArrayList<Float> x = new ArrayList<>();
    private ArrayList<Float> y = new ArrayList<>();
    private ArrayList<Float> xAcceleration = new ArrayList<>();
    private ArrayList<Float> yAcceleration = new ArrayList<>();

    private ArrayList<Float> xVelocity = new ArrayList<>();
    private ArrayList<Float> yVelocity = new ArrayList<>();

    private ArrayList<Float> mLeafRaise = new ArrayList<>();

    private int mParticleCount;

    private ArrayList<Long> mStartTimeMillisecond = new ArrayList<>();
    private ArrayList<Boolean> mAnimationRunning = new ArrayList<>();

    //private ArrayList<Integer> mParticleFadeOutTimeProgress = new ArrayList<>();
    private long mFadeOutTime;
    private ArrayList<Paint> mFadeOutPaint = new ArrayList<>();

    private Random random = new Random();

    private float mSpeedMinX;
    private float mSpeedMaxX;
    private float mSpeedMinY;
    private float mSpeedMaxY;
    private float mAccelerationMin;
    private float mAccelerationMax;
    private int mMinAngle;
    private int mMaxAngle;


    public LeafSprite(Bitmap particleBitmap, int particleCount, int x, int y, long fadeOutTimeMillisecond) {
        mLeafBitmap = particleBitmap;
        mX = x;
        mY = y;
        mParticleCount = particleCount;
        mFadeOutTime = fadeOutTimeMillisecond;
    }

    public void initialize(){
        for (int i = 0; i <= mParticleCount; i++){
            xVelocity.add(getRandomXSpeed());
            yVelocity.add(getRandomYSpeed());
            xAcceleration.add((float) (getRandomAcceleration() * Math.cos(getAngleInRads())));
            yAcceleration.add((float) (getRandomAcceleration() * Math.sin(getAngleInRads())));
            mStartTimeMillisecond.add(0L);
            mLeafRaise.add(getLeafRaise(250f,300f));
            mAnimationRunning.add(false);
            mFadeOutPaint.add(new Paint());
            mFadeOutPaint.get(i).setAlpha(255);
            mFadeOutPaint.get(i).setColor(Color.TRANSPARENT);
            this.x.add(mX + 0f);
            this.y.add(i*screenHeight/mParticleCount + 0f);
        }
    }

    public void draw(Canvas canvas){
        for (int i = 0; i <= mParticleCount; i++){
            update(i);
            canvas.drawBitmap(mLeafBitmap, x.get(i), y.get(i), mFadeOutPaint.get(i));
        }
    }

    private void update(int i){
        if (!mAnimationRunning.get(i)){
            mStartTimeMillisecond.set(i, System.currentTimeMillis());
            mAnimationRunning.set(i, true);
        }
        long realTimeMillisecond = System.currentTimeMillis() - mStartTimeMillisecond.get(i);
        //mCurrentX = mInitialX+mSpeedX*realMiliseconds+mAccelerationX*realMiliseconds*realMiliseconds;
        //mCurrentY = mInitialY+mSpeedY*realMiliseconds+mAccelerationY*realMiliseconds*realMiliseconds;

        x.set(i, mX + xVelocity.get(i)*realTimeMillisecond + xAcceleration.get(i)*realTimeMillisecond*realTimeMillisecond);
        //y.set(i, mY + yVelocity.get(i)*realTimeMillisecond + yAcceleration.get(i)*realTimeMillisecond*realTimeMillisecond);
        y.set(i, i*screenHeight/mParticleCount + (float)Math.sin(Math.PI * realTimeMillisecond/(mFadeOutTime)) * mLeafRaise.get(i));
        Log.v("ABCDEFGHIJKL", realTimeMillisecond + "");
        mFadeOutPaint.get(i).setAlpha(255 - (int)((realTimeMillisecond + 0f)/(mFadeOutTime + 0f) * 255));

        if (x.get(i) > screenWidth || y.get(i) > screenHeight || realTimeMillisecond > mFadeOutTime) {
            x.set(i, mX + 0f);
            y.set(i , i*screenHeight/mParticleCount + 0f);
            xVelocity.set(i, getRandomXSpeed());
            yVelocity.set(i, getRandomYSpeed());
            xAcceleration.set(i, (float) (getRandomAcceleration() * Math.cos(getAngleInRads())));
            yAcceleration.set(i, (float) (getRandomAcceleration() * Math.sin(getAngleInRads())));
            mStartTimeMillisecond.set(i, 0L);
            mAnimationRunning.set(i, false);
            mFadeOutPaint.get(i).setAlpha(255);

        }
    }

    public float getLeafRaise(float minRaise, float maxRaise){
        return random.nextFloat()*(maxRaise - minRaise)+minRaise;
    }


    public void setSpeedRange(float speedMinX, float speedMaxX, float speedMinY, float speedMaxY){
        mSpeedMinX = speedMinX;
        mSpeedMaxX = speedMaxX;
        mSpeedMinY = speedMinY;
        mSpeedMaxY = speedMaxY;
    }

    public void setAccelerationRange(float minAcceleration, float maxAcceleration, int minAngle, int maxAngle){
        mAccelerationMin = minAcceleration;
        mAccelerationMax = maxAcceleration;
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    private float getRandomXSpeed(){
        return random.nextFloat()*(mSpeedMaxX-mSpeedMinX)+mSpeedMinX;
    }

    private float getRandomYSpeed(){
        return random.nextFloat()*(mSpeedMaxY-mSpeedMinY)+mSpeedMinY;
    }

    private float getRandomAcceleration(){
        return random.nextFloat()*(mAccelerationMax - mAccelerationMin)+mAccelerationMin;
    }

    private float getAngleInRads(){
        float angle = mMinAngle;
        if (mMaxAngle != mMinAngle) {
            angle = random.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
        }
        return (float) (angle*Math.PI/180f);
    }


}

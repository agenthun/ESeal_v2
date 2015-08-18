package com.agenthun.material.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import in.srain.cube.R;

public class MenuAndBackButton extends View {

    public static final int DURATION = 500;
    private float density;
    private boolean isInArrowState = false;
    private Paint linePaint = new Paint();
    private PointF one1 = new PointF();
    private PointF one2 = new PointF();
    private PointF two1 = new PointF();
    private PointF two2 = new PointF();
    private PointF three1 = new PointF();
    private PointF three2 = new PointF();

    public MenuAndBackButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public MenuAndBackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public MenuAndBackButton(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    private float density(float paramFloat) {
        return paramFloat * this.density;
    }

    private void gotToPositions(int startDelay, TimeInterpolator timeInterpolator, final PointF pointF, PointF[] arrayOfPointF) {
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(), arrayOfPointF);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(timeInterpolator);
        animator.setDuration(500);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                pointF.x = ((PointF) animation.getAnimatedValue()).x;
                pointF.y = ((PointF) animation.getAnimatedValue()).y;
                invalidate();
            }
        });
        animator.start();
    }

    private void init() {
        linePaint.setColor(getResources().getColor(R.color.cube_mints_white));
        density = getResources().getDisplayMetrics().density;
        linePaint.setStrokeWidth(1.3f * this.density);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        one1 = new PointF(density(13.0f), density(17.0f));
        one2 = new PointF(density(38.0f), density(17.0f));
        two1 = new PointF(density(13.0f), density(24.0f));
        two2 = new PointF(density(38.0f), density(24.0f));
        three1 = new PointF(density(13.0f), density(31.0f));
        three2 = new PointF(density(38.0f), density(31.0f));
    }

    public void fadeToGray() {
        Paint localPaint = this.linePaint;
        ArgbEvaluator localArgbEvaluator = new ArgbEvaluator();
        Object[] arrayOfObjects = new Object[2];
        arrayOfObjects[0] = Integer.valueOf(this.linePaint.getColor());
        arrayOfObjects[1] = Integer.valueOf(getResources().getColor(R.color.cube_mints_main_color));
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofObject(
                localPaint, "Color", localArgbEvaluator, arrayOfObjects);
        localObjectAnimator.addUpdateListener(new getUpdateListener());
        localObjectAnimator.setDuration(250).start();
    }

    public void fadeToWhite() {
        Paint localPaint = this.linePaint;
        ArgbEvaluator localArgbEvaluator = new ArgbEvaluator();
        Object[] arrayOfObjects = new Object[2];
        arrayOfObjects[0] = Integer.valueOf(this.linePaint.getColor());
        arrayOfObjects[1] = Integer.valueOf(-1);
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofObject(
                localPaint, "Color", localArgbEvaluator, arrayOfObjects);
        localObjectAnimator.addUpdateListener(new getUpdateListener());
        localObjectAnimator.setDuration(250).start();
    }

    private class getUpdateListener implements
            AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
            invalidate();
        }
    }

    public void goToArrow() {
        if (!this.isInArrowState) {
            this.isInArrowState = true;
            AccelerateInterpolator accelerateInterpolator1 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF1 = this.one1;
            PointF[] arrayOfPointFs1 = new PointF[2];
            arrayOfPointFs1[0] = this.one1;
            arrayOfPointFs1[1] = new PointF(density(28.0f), density(15.0f));
            gotToPositions(0, accelerateInterpolator1, localPointF1,
                    arrayOfPointFs1);

            AccelerateInterpolator accelerateInterpolator2 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF2 = this.one2;
            PointF[] arrayOfPointFs2 = new PointF[2];
            arrayOfPointFs2[0] = this.one2;
            arrayOfPointFs2[1] = new PointF(density(38.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator2, localPointF2,
                    arrayOfPointFs2);

            AccelerateInterpolator accelerateInterpolator3 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF3 = this.two1;
            PointF[] arrayOfPointFs3 = new PointF[2];
            arrayOfPointFs3[0] = this.two1;
            arrayOfPointFs3[1] = new PointF(density(13.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator3, localPointF3,
                    arrayOfPointFs3);

            AccelerateInterpolator accelerateInterpolator4 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF4 = this.two2;
            PointF[] arrayOfPointFs4 = new PointF[2];
            arrayOfPointFs4[0] = this.two2;
            arrayOfPointFs4[1] = new PointF(density(38.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator4, localPointF4,
                    arrayOfPointFs4);

            AccelerateInterpolator accelerateInterpolator5 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF5 = this.three1;
            PointF[] arrayOfPointFs5 = new PointF[2];
            arrayOfPointFs5[0] = this.three1;
            arrayOfPointFs5[1] = new PointF(density(28.0f), density(33.0f));
            gotToPositions(0, accelerateInterpolator5, localPointF5,
                    arrayOfPointFs5);

            AccelerateInterpolator accelerateInterpolator6 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF6 = this.three2;
            PointF[] arrayOfPointFs6 = new PointF[2];
            arrayOfPointFs6[0] = this.three2;
            arrayOfPointFs6[1] = new PointF(density(38.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator6, localPointF6,
                    arrayOfPointFs6);

            Property<View, Float> property = View.ROTATION;
            float[] arrayOfFloat = new float[2];
            arrayOfFloat[0] = 0.0f;
            arrayOfFloat[1] = 180.0f;
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, property,
                    arrayOfFloat).setDuration(500);
            animator.setInterpolator(new QuintInOut());
            animator.start();
        }
    }

    public void goToMenu() {
        if (this.isInArrowState) {
            this.isInArrowState = false;
            AccelerateInterpolator accelerateInterpolator1 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF1 = this.one1;
            PointF[] arrayOfPointFs1 = new PointF[2];
            arrayOfPointFs1[0] = this.one1;
            arrayOfPointFs1[1] = new PointF(density(13.0f), density(17.0f));
            gotToPositions(0, accelerateInterpolator1, localPointF1,
                    arrayOfPointFs1);

            AccelerateInterpolator accelerateInterpolator2 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF2 = this.one2;
            PointF[] arrayOfPointFs2 = new PointF[2];
            arrayOfPointFs2[0] = this.one2;
            arrayOfPointFs2[1] = new PointF(density(38.0f), density(17.0f));
            gotToPositions(0, accelerateInterpolator2, localPointF2,
                    arrayOfPointFs2);

            AccelerateInterpolator accelerateInterpolator3 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF3 = this.two1;
            PointF[] arrayOfPointFs3 = new PointF[2];
            arrayOfPointFs3[0] = this.two1;
            arrayOfPointFs3[1] = new PointF(density(13.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator3, localPointF3,
                    arrayOfPointFs3);

            AccelerateInterpolator accelerateInterpolator4 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF4 = this.two2;
            PointF[] arrayOfPointFs4 = new PointF[2];
            arrayOfPointFs4[0] = this.two2;
            arrayOfPointFs4[1] = new PointF(density(38.0f), density(24.0f));
            gotToPositions(0, accelerateInterpolator4, localPointF4,
                    arrayOfPointFs4);

            AccelerateInterpolator accelerateInterpolator5 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF5 = this.three1;
            PointF[] arrayOfPointFs5 = new PointF[2];
            arrayOfPointFs5[0] = this.three1;
            arrayOfPointFs5[1] = new PointF(density(13.0f), density(31.0f));
            gotToPositions(0, accelerateInterpolator5, localPointF5,
                    arrayOfPointFs5);

            AccelerateInterpolator accelerateInterpolator6 = new AccelerateInterpolator(
                    1.9f);
            PointF localPointF6 = this.three2;
            PointF[] arrayOfPointFs6 = new PointF[2];
            arrayOfPointFs6[0] = this.three2;
            arrayOfPointFs6[1] = new PointF(density(38.0f), density(31.0f));
            gotToPositions(0, accelerateInterpolator6, localPointF6,
                    arrayOfPointFs6);

            Property<View, Float> property = View.ROTATION;
            float[] arrayOfFloat = new float[2];
            arrayOfFloat[0] = getRotation();
            arrayOfFloat[1] = 360.0f;
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, property,
                    arrayOfFloat).setDuration(500);
            animator.setInterpolator(new QuintInOut());
            animator.start();
        }
    }

    public boolean isInBackState() {
        return this.isInArrowState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawLine(this.one1.x, this.one1.y, this.one2.x, this.one2.y,
                this.linePaint);
        canvas.drawLine(this.two1.x, this.two1.y, this.two2.x, this.two2.y,
                this.linePaint);
        canvas.drawLine(this.three1.x, this.three1.y, this.three2.x,
                this.three2.y, this.linePaint);
    }
}

package com.tetris.testsurfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by yubo on 2016/7/31.
 */
public class Ball {

    public static final int WIDTH = 50;
    private static Paint paint;

    private int posX;
    private int posY;
    private int speedX = 20;
    private int speedY = 20;

    private int startX;
    private int startY;

    private OnNextMoveListener onNextMoveListener;

    static {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#6699FF"));
    }

    public Ball(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

        this.startX = posX;
        this.startY = posY;
    }

    public void reset() {
        this.posX = this.startX;
        this.posY = this.startY;

        speedX = 20;
        speedY = 20;
    }

    public void drawMe(Canvas canvas) {
        if(canvas != null) {
            canvas.drawOval(new RectF(posX, posY, posX + WIDTH, posY + WIDTH), paint);
        }
    }

    public void move(int gameViewWidth, int gameViewHeight) {
        posX += speedX;
        posY += speedY;
        if(posX < 0) {
            posX = 0;
            speedX = -speedX;
        }
        if(posY < 0) {
            posY = 0;
            speedY = -speedY;
        }
        if(posX + WIDTH >= gameViewWidth) {
            posX = gameViewWidth - WIDTH;
            speedX = -speedX;
        }
        if(posY + WIDTH >= gameViewHeight) {
            posY = gameViewHeight - WIDTH;
            speedY = -speedY;
        }
        if(onNextMoveListener != null) {
            onNextMoveListener.onNextMove(posX + WIDTH / 2, posY + WIDTH);
        }
    }

    public void reverseSpeedY() {
        this.speedY = -this.speedY;
    }

    public interface OnNextMoveListener {
        /**
         * 小球下次移动时的监听器
         * @param ballCenterX 小球下次移动时的水平中点位置
         * @param ballBottomY 小球下次移动时的最底下的纵坐标
         */
        void onNextMove(int ballCenterX, int ballBottomY);
    }

    public void setOnNextMoveListener(OnNextMoveListener listener) {
        if(listener != null) {
            onNextMoveListener = listener;
        }
    }

    public Rect getRect() {
        return new Rect(posX, posY, posX + WIDTH, posY + WIDTH);
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
}

package com.tetris.testsurfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by yubo on 2016/7/31.
 */
public class Board {

    private static Paint paint;

    public static final int WIDTH = 200;
    public static final int HEIGHT = 30;
    private int posX;
    private int posY;

    static {
        paint = new Paint();
        paint.setColor(Color.parseColor("#6699FF"));
    }

    public Board(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void drawMe(Canvas canvas) {
        if(canvas != null) {
            canvas.drawRect(posX, posY, posX + WIDTH, posY + HEIGHT, paint);
        }
    }

    public Rect getRect() {
        return new Rect(posX, posY, posX + WIDTH, posY + HEIGHT);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}

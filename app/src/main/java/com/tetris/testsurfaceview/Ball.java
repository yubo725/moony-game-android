package com.tetris.testsurfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by yubo on 2016/7/31.
 */
public class Ball {

    private static final int WIDTH = 50;
    private static Paint paint;

    private int posX;
    private int posY;
    private int speedX = 20;
    private int speedY = 20;

    static {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#6699FF"));
    }

    public Ball(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
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
    }

}

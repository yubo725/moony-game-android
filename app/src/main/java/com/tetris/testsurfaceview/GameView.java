package com.tetris.testsurfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by yubo on 2016/7/31.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Paint paint;
    private Paint resetPaint;

    private int gameViewWidth;
    private int gameViewHeight;

    private boolean canPaint = true;

    private Ball ball;
    private Board board;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);

        resetPaint = new Paint();
        resetPaint.setColor(Color.parseColor("#FFFFFF"));

        paint = new Paint();
        paint.setColor(Color.parseColor("#FF0000"));
        paint.setAntiAlias(true);

        ball = new Ball(100, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        gameViewWidth = getMeasuredWidth();
        gameViewHeight = getMeasuredHeight();
        board = new Board((gameViewWidth - Board.WIDTH) / 2, gameViewHeight - Board.HEIGHT * 5);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        canPaint = false;
    }

    private void clearBackground(Canvas canvas) {
        if(canvas != null) {
            canvas.drawRect(0, 0, gameViewWidth, gameViewHeight, resetPaint);
        }
    }

    @Override
    public void run() {
        while(canPaint) {
            Canvas canvas = holder.lockCanvas();
            clearBackground(canvas);
            ball.drawMe(canvas);
            board.drawMe(canvas);
            holder.unlockCanvasAndPost(canvas);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ball.move(gameViewWidth, gameViewHeight);
        }
    }
}

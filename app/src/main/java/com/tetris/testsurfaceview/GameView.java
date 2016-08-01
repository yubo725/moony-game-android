package com.tetris.testsurfaceview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by yubo on 2016/7/31.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Paint paint;
    private Paint resetPaint;

    private int gameViewWidth;
    private int gameViewHeight;

    private boolean isGameOver = true;

    private Ball ball;
    private Board board;

    private int score = 0; //得分
    private AlertDialog alertDialog;

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
        paint.setTextSize(30);

        ball = new Ball(100, 100);
        ball.setOnNextMoveListener(new Ball.OnNextMoveListener() {
            @Override
            public void onNextMove(int ballCenterX, int ballBottomY) {
                if(board != null) {
                    if(ballBottomY >= board.getPosY()) {
                        if(ballCenterX >= board.getPosX() && ballCenterX <= board.getPosX() + board.WIDTH) {
                            //在板上反弹
                            ball.setPosY(board.getPosY() - ball.WIDTH);
                            ball.reverseSpeedY();
                            score += 10;
                        }else {
                            //game over
                            isGameOver = true;
                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showGameOverDialog();
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    /**
     * 显示游戏结束对话框
     */
    private void showGameOverDialog() {
        if(alertDialog == null) {
            alertDialog = new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("游戏结束! 得分: " + score).setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isGameOver = false;
                    score = 0;
                    ball.reset();
                    new Thread(GameView.this).start();
                }
            }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity)getContext()).finish();
                }
            }).create();
        }
        alertDialog.show();
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
        isGameOver = false;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isGameOver = true;
    }

    /**
     * 清除背景
     * @param canvas
     */
    private void clearBackground(Canvas canvas) {
        if(canvas != null) {
            canvas.drawRect(0, 0, gameViewWidth, gameViewHeight, resetPaint);
        }
    }

    /**
     * 画出分数
     * @param canvas
     */
    private void drawScore(Canvas canvas) {
        if(canvas != null) {
            String str = String.format("得分: %d", score);
            int strLen = (int) paint.measureText(str);
            canvas.drawText(str, (gameViewWidth - strLen) / 2, 40 + getTop(), paint);
        }
    }

    @Override
    public void run() {
        while(!isGameOver) {
            ball.move(gameViewWidth, gameViewHeight);
            Canvas canvas = holder.lockCanvas();
            clearBackground(canvas);
            drawScore(canvas);
            ball.drawMe(canvas);
            board.drawMe(canvas);
            holder.unlockCanvasAndPost(canvas);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX();
            int posX = (int) (moveX - Board.WIDTH / 2);
            if(posX + Board.WIDTH > gameViewWidth) {
                posX = gameViewWidth - Board.WIDTH;
            }
            if(posX < 0) {
                posX = 0;
            }
            if(board != null) {
                board.setPosX(posX);
            }
        }
        return true;
    }
}

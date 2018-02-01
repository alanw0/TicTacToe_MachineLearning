package com.abqappthu.ttt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimatedView extends ImageView {
    private Context mContext;

    private int viewWidth = 0;
    private int viewHeight = 0;

    private TextView resultView = null;
    private TextView rawPriceView = null;
    private TextView investPriceView = null;
    private TextView infoLabel = null;

    private TouchHandler touchHandler = null;
    private Handler threadLauncher = new Handler();

    private Paint blueLinePaint;
    private Paint redLinePaint;
    private Paint greenLinePaint;
    private Paint blackLinePaint;
    private Paint blackDottedLinePaint;
    private Paint blackFontPaint;

    private GameState gameState = new GameState();

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupResources();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    private void setupResources() {
        setupPaintObjects();
    }

    private void setupPaintObjects() {
        setupFontPaints();
        setupShapePaints();
    }

    private void setupShapePaints() {
        blueLinePaint = setupLinePaint(Color.BLUE, Paint.Style.FILL, 3.0f);
        redLinePaint = setupLinePaint(Color.RED, Paint.Style.FILL, 6.0f);
        greenLinePaint = setupLinePaint(Color.GREEN, Paint.Style.FILL, 6.0f);
        blackLinePaint = setupLinePaint(Color.BLACK, Paint.Style.FILL, 10.0f);
        blackDottedLinePaint = setupLinePaint(Color.BLACK, Paint.Style.STROKE, 2.0f);
        blackDottedLinePaint.setAlpha(220);
        blackDottedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }

    private void setupFontPaints() {
        blackFontPaint = setupFontPaint(Color.BLACK, Paint.Style.FILL, 260, true);
    }

    private Paint setupFontPaint(int color, Paint.Style style, float textSize, boolean boldText) {
        Paint fontPaint = setupPaint(color, style);
        fontPaint.setTextSize(textSize);
        fontPaint.setFakeBoldText(boldText);
        return fontPaint;
    }
    private Paint setupLinePaint(int color, Paint.Style style, float strokeWidth) {
        Paint linePaint = setupPaint(color, style);
        linePaint.setStrokeWidth(strokeWidth);
        return linePaint;
    }
    private Paint setupPaint(int color, Paint.Style style) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        return paint;
    }

    private Runnable call_invalidate = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        threadLauncher.post(call_invalidate);
    }

    protected void onDraw(Canvas c) {
        setViewDimensions();
        drawGrid(c);
        for(int i=0; i<gameState.values.length; ++i) {
            String letter = " ";
            if (gameState.values[i] == Value.O) {
                letter = "O";
            }
            if (gameState.values[i] == Value.X) {
                letter = "X";
            }
            draw(letter, i, c);
        }
    }

    private void draw(String letter, int pos, Canvas c) {
        int width = viewWidth/3;
        int height = viewHeight/3;
        int col = pos%3;
        int row = pos/3;
        int x = width*col;
        int y = height*row;
        c.drawText(letter, x+30, y+getTextHeight(blackFontPaint)+30, blackFontPaint);
    }
    private void drawGrid(Canvas c) {
        int x1 = viewWidth/3;
        int x2 = x1*2;
        int y1 = viewHeight/3;
        int y2 = y1*2;
        c.drawLine(x1,0,x1,viewHeight, blackLinePaint);
        c.drawLine(x2,0,x2,viewHeight, blackLinePaint);
        c.drawLine(0,y1,viewWidth,y1, blackLinePaint);
        c.drawLine(0,y2,viewWidth,y2, blackLinePaint);
    }
    private int getTextHeight(Paint fontPaint) {
        Rect bounds = new Rect();
        fontPaint.getTextBounds("8888",0, 4, bounds);
        return Math.abs(bounds.bottom - bounds.top);
    }
    private int getTextWidth(String txt, Paint fontPaint) {
        Rect bounds = new Rect();
        fontPaint.getTextBounds(txt, 0, txt.length(), bounds);
        return Math.abs(bounds.right-bounds.left);
    }

    public void setViewDimensions() {
        if (viewWidth == 0 || viewHeight == 0) {
            viewWidth = getWidth();
            viewHeight = getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            processTouchEvent(e);
        }
        return true;
    }

    private void processTouchEvent(MotionEvent e) {
        if (touchHandler != null) {
            float x = e.getX();
            float y = e.getY();
            int col = (int)((x*3)/viewWidth);
            int row = (int)((y*3)/viewHeight);
            touchHandler.handleTouch(row*3 + col);
        }
    }

    public void setInfoLabel(TextView infoLabel) {
        this.infoLabel = infoLabel;
    }

}

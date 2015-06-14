package sk.svb.lgg3.svb_circlecase_rocket.view;

import sk.svb.lgg3.svb_circlecase_rocket.logic.Blocks;
import sk.svb.lgg3.svb_circlecase_rocket.logic.Stars;
import sk.svb.lgg3.svb_circlecase_rocket.logic.Stars.Star;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class AccelerometerView extends SurfaceView implements Callback {

	public static final String TAG = AccelerometerView.class.getName();

	public static final int GAME_LR = 0;
	public static final int GAME_RT = 1;
	private CanvasThread canvasThread;

	Activity act;
	Paint c_red, c_bl, c_wh, c_wht;
	int x = 0;
	int y = 0;

	int rx = -1000,ry = -1000;

	// rocket size
	int R_SIZE_W = 30;
	int R_SIZE_H = 30;

	int W = 1046;
	int H = 1046;

	private Stars stars;
	private Blocks block;
	private int gameType = GAME_LR;

	public int points = 0;
	int countDown = 20;
	private boolean first = false;

	public AccelerometerView(Context context) {
		super(context);
		// TODO Auto-generated method stub
	}

	public AccelerometerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initPaints();
		this.getHolder().addCallback(this);
		this.canvasThread = new CanvasThread(getHolder());
		this.setFocusable(true);
		setWillNotDraw(false);
		stars = new Stars(W, H);
		block = new Blocks(W, H);

	}

	public AccelerometerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated method stub
	}

	private void initPaints() {
		c_red = new Paint();
		c_red.setColor(Color.RED);

		c_bl = new Paint();
		c_bl.setColor(Color.BLACK);

		c_wh = new Paint();
		c_wh.setStrokeWidth(4);
		c_wh.setColor(Color.WHITE);

		c_wht = new Paint();
		c_wht.setColor(Color.WHITE);
	}

	public void setActivity(Activity act) {
		this.act = act;
	}
	public void setGameType(int gameType){
		this.gameType = gameType;
	}

	protected void myDraw(Canvas canvas) {

		int xx = (int) ((double) canvas.getWidth() / 80 * x);
		int yy = (int) ((double) canvas.getHeight() / 80 * y);
		int rx = canvas.getWidth() / 2 + xx;
		int ry = canvas.getHeight() / 2 + yy;

		// detect quick move
		// Log.d(TAG, "diff: " + Math.abs(Math.abs(this.ry) - Math.abs(ry)));
		if (this.ry != -1000
				&& Math.abs(Math.abs(this.ry) - Math.abs(ry))>100
				&& countDown<=0 ) {
			rx = this.rx;
			ry = this.ry;
		}

		this.rx = rx;
		this.ry = ry;

		// draw black bg
		canvas.drawARGB(255, 0, 0, 0);

		// draw start
		drawStars(canvas);

		// draw rocket
		drawRocket(canvas, rx, ry);

		// countdown at startup
		if (countDown <= 0){
			// draw gates
			drawGates(canvas, rx, ry);
		}else{
			countDown --;
		}

	}

	private void drawStars(Canvas canvas){
		// draw stars
		for (Star s : stars.getStarList()) {
			canvas.drawCircle(s.x, s.y, s.size, c_wht);
		}
		stars.updateStars();
	}

	private void drawRocket(Canvas canvas, int rx, int ry){
		canvas.drawLine(rx, ry - R_SIZE_H, rx + R_SIZE_W, ry + R_SIZE_H, c_wh);
		canvas.drawLine(rx + R_SIZE_W, ry + R_SIZE_H, rx - R_SIZE_W, ry
				+ R_SIZE_H, c_wh);
		canvas.drawLine(rx - R_SIZE_W, ry + R_SIZE_H, rx, ry - R_SIZE_H, c_wh);
	}

	private void drawGates(Canvas canvas, int rx, int ry){
		canvas.drawRect(block.getRect1(), c_wht);
		canvas.drawRect(block.getRect2(), c_wht);
		block.update(points, gameType);
		if (block.throughGate(rx, ry)) {
			points++;
			updateScore();
			doVibrate(act, 20);
		}
		if (block.detectCollision(rx - R_SIZE_W, ry - R_SIZE_H)
				|| block.detectCollision(rx + R_SIZE_W, ry + R_SIZE_H)){
			doVibrate(act, 500);
			endGame();
		}
	}

	public void updateScore() {
		Intent i = new Intent("game_update");
		i.putExtra("score", true);
		act.sendBroadcast(i);
	}

	public void endGame() {
		Intent i = new Intent("game_update");
		i.putExtra("end", true);
		act.sendBroadcast(i);
	}

	public void setCoords(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public Point getCoords() {
		return new Point(this.x, this.y);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	public void startDrawImage() {
		canvasThread.setRunning(true);
		canvasThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		canvasThread.setRunning(false);
		while (retry) {
			try {
				canvasThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class CanvasThread extends Thread {
		private SurfaceHolder surfaceHolder;
		private boolean isRun = false;

		public CanvasThread(SurfaceHolder holder) {
			this.surfaceHolder = holder;
		}

		public void setRunning(boolean run) {
			this.isRun = run;
		}

		public boolean isRunning() {
			return this.isRun;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Canvas c;

			while (isRun) {
				c = null;
				try {
					c = this.surfaceHolder.lockCanvas(null);
					if (c != null) {
						synchronized (this.surfaceHolder) {
							AccelerometerView.this.myDraw(c);
						}
					}
				} finally {
					if (c != null)
						this.surfaceHolder.unlockCanvasAndPost(c);
				}

				try {
					sleep(50); // 20 redraw times a second
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void doVibrate(Context ctx, long milis) {
		Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(milis);
	}
}

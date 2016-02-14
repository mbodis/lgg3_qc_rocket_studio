package sk.svb.lgg3.svb_circlecase_rocket.activity.game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.activity.CurrentScoreActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import sk.svb.lgg3.svb_circlecase_rocket.logic.QcActivity;
import sk.svb.lgg3.svb_circlecase_rocket.view.AccelerometerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class QcAccelerometerRtActivity extends QcActivity implements
		SensorEventListener {

	private static final String TAG = QcAccelerometerRtActivity.class.getName();

	// sensor
	private SensorManager mSensorManager;

	// view
	private AccelerometerView mAccelerometerView;

	private GameStats gs;
	public TextView scoreTextView;

	int initZ = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_accelerometer);
		scoreTextView = (TextView) findViewById(R.id.actual_score);

		onCreateQcActivity();

		mAccelerometerView = (AccelerometerView) findViewById(R.id.sv);
		mAccelerometerView.setActivity(this);
		mAccelerometerView.setGameType(AccelerometerView.GAME_RT);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometerView.startDrawImage();

		gs = new GameStats();
		gs.setType(GameStats.GAME_RT);
		gs.setScore(-1);
		updateScore();

		Date date = new Date(System.currentTimeMillis());
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
		gs.setDate(time);

	}

	public void updateScore() {
		gs.setScore(gs.getScore() + 1);
		scoreTextView.setText(getString(R.string.actual_score) + ": "
				+ gs.getScore());
	}

	@Override
	public void onResume() {
		super.onResume();
		registerSensor();
		registerReceiver(mIntentReceiver, new IntentFilter("game_update"));
	}

	@Override
	public void onPause() {
		unregisterSensor();
		unregisterReceiver(mIntentReceiver);
		gs.save();
		super.onStop();
	}

	private void endGame() {
		Intent i = new Intent(getApplicationContext(),
				CurrentScoreActivity.class);
		i.putExtra("score", gs.getScore());
		startActivity(i);
		finish();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// float mAccelX = 0 - event.values[2];
		// float mAccelY = 0 - event.values[1];
		// float mAccelZ = event.values[0];

		if (initZ == 999) {
			initZ = (int) event.values[0];
		}		

		int z = (int) ((initZ - event.values[0]) ) * 2;
		if (z > 39) {
			z = 38;
		}
		if (z < -39) {
			z = -38;
		}

		mAccelerometerView.setCoords(-z, (int) (Math.sqrt((35 * 35 - z * z))));
	}

	private void registerSensor() {

		// showAllSensors();
		List<Sensor> sensorList = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(this, sensorList.get(0),
				SensorManager.SENSOR_DELAY_GAME);
	}

	private void unregisterSensor() {
		mSensorManager.unregisterListener(this);
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction() != null
					&& intent.getAction().equals("game_update")) {
				if (intent.getBooleanExtra("score", false)) {
					updateScore();
				}
				if (intent.getBooleanExtra("end", false)) {
					endGame();
				}
			}

		}
	};
}

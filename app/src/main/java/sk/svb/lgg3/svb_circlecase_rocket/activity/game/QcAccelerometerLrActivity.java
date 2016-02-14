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

public class QcAccelerometerLrActivity extends QcActivity implements
		SensorEventListener {

	// sensor
	private SensorManager mSensorManager;

	// view
	private AccelerometerView mAccelerometerView;

	private GameStats gs;
	public TextView scoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_accelerometer);
		scoreTextView = (TextView) findViewById(R.id.actual_score);

		onCreateQcActivity();

		mAccelerometerView = (AccelerometerView) findViewById(R.id.sv);
		mAccelerometerView.setActivity(this);
		mAccelerometerView.setGameType(AccelerometerView.GAME_LR);
		if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("start_calibration")){
			mAccelerometerView.setServoCalibrationState(AccelerometerView.CALIBRATION_CENTER);
		}else{
			mAccelerometerView.setServoCalibrationState(AccelerometerView.CALIBRATION_FINISH);
		}

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometerView.startDrawImage();

		gs = new GameStats();
		gs.setType(GameStats.GAME_LR);
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
		super.onPause();
		unregisterSensor();
		unregisterReceiver(mIntentReceiver);
		gs.save();
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
		// mAccelX = 0 - event.values[2];
		// mAccelY = 0 - event.values[1];
		// mAccelZ = event.values[0];

		if (event == null)
			return;

		int forwBack = (int) (event.values[1]);
		if (forwBack > 40) {
			forwBack = 40;
		}
		if (forwBack < -40) {
			forwBack = -40;
		}

		int lefRig = (int) (event.values[2]);
		if (lefRig > 40) {
			lefRig = 40;
		}
		if (lefRig < -40) {
			lefRig = -40;
		}
		
		mAccelerometerView.setCoords(-lefRig, -forwBack);
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

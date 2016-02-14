package sk.svb.lgg3.svb_circlecase_rocket.activity;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.UsbDevice.ServoController;
import sk.svb.lgg3.svb_circlecase_rocket.activity.game.QcAccelerometerLrActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.QcActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.ServoLimits;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QCircleMainActivity extends QcActivity {

	private static final String TAG = QCircleMainActivity.class.getName();

	private View backBtn;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qcircle);

		ServoController.findDeviceAndStartService(getApplicationContext());
		onCreateQcActivity();
		initializeBackButton();
		initListView();
	}

	private void initializeBackButton() {

		backBtn = findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QCircleMainActivity.this.finish();
			}
		});

	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.qc_lv);
		mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.qc_list_row,
				new String[]{getString(R.string.play_lr), getString(R.string.lr_calibration)}));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				switch (position) {
					case 0:
						finish();
						ServoController.sendMessage(getApplicationContext(), ServoLimits.LOCATION_CENTER, ServoLimits.SPEED_NORMAL);
						startActivity(new Intent(getApplicationContext(),
								QcAccelerometerLrActivity.class));

						break;
					case 1:
						finish();
						ServoController.sendMessage(getApplicationContext(), ServoLimits.LOCATION_CENTER, ServoLimits.SPEED_NORMAL);
						Intent mIntent = new Intent(getApplicationContext(), QcAccelerometerLrActivity.class);
						mIntent.putExtra("start_calibration", true);
						startActivity(mIntent);
						break;
				}

			}
		});
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ServoController.sendMessage(getApplicationContext(), ServoLimits.LOCATION_CENTER, ServoLimits.SPEED_NORMAL);
				return false;
			}
		});
	}

}

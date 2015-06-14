package sk.svb.lgg3.svb_circlecase_rocket.activity;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.game.QcAccelerometerLrActivity;
import sk.svb.lgg3.svb_circlecase_rocket.game.QcAccelerometerRtActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.QcActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class QCircleMainActivity extends QcActivity {

	View backBtn;
	ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qcircle);

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
				new String[] { getString(R.string.play_lr), getString(R.string.play_rt)}));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					finish();
					startActivity(new Intent(getApplicationContext(),
							QcAccelerometerLrActivity.class));

					break;
				case 1:
					finish();
					startActivity(new Intent(getApplicationContext(),
							QcAccelerometerRtActivity.class));

					break;	
				}

			}
		});
	}

}

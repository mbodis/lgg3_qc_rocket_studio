package sk.svb.lgg3.svb_circlecase_rocket.activity;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.logic.QcActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CurrentScoreActivity extends QcActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_current_score);
		onCreateQcActivity();

		if (getIntent() != null) {
			((TextView) findViewById(R.id.score)).setText(""
					+ getIntent().getIntExtra("score", 0));
		}

		((View) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
						startActivity(new Intent(getApplicationContext(),
								QCircleMainActivity.class));

					}
				});
	}
}

package sk.svb.lgg3.svb_circlecase_rocket.logic;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.activity.FullScreenActivity;
import sk.svb.lgg3.svb_circlecase_rocket.iface.MyQcInterface;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public abstract class QcActivity extends Activity implements MyQcInterface{	

	// [START]declared in LGIntent.java of LG Framework
	public static final int EXTRA_ACCESSORY_COVER_OPENED = 0;
	public static final int EXTRA_ACCESSORY_COVER_CLOSED = 1;
	public static final String EXTRA_ACCESSORY_COVER_STATE = "com.lge.intent.extra.ACCESSORY_COVER_STATE";
	public static final String ACTION_ACCESSORY_COVER_EVENT = "com.lge.android.intent.action.ACCESSORY_COVER_EVENT";
	// [END]declared in LGIntent.java of LG Framework

	// [START] QuickCover Settings DB
	public static final String QUICKCOVERSETTINGS_QUICKCOVER_ENABLE = "quick_view_enable";
	// [END] QuickCover Settings DB

	// QuickCover Type
	public static final int QUICKCOVERSETTINGS_QUICKCIRCLE = 3;

	// [START] QuickCircle info.
	static boolean quickCircleEnabled = false;
	static int quickCaseType = 0;
	static boolean quickCircleClosed = true;
	int circleWidth = 0;
	int circleHeight = 0;
	int circleXpos = 0;
	int circleYpos = 0;
	int circleDiameter = 0;
	// [END] QuickCircle info.

	// -------------------------------------------------------------------------------
	private final boolean DEBUG = true;
	private final String TAG = "[QCircleSamepleCode]";
	private int mQuickCoverState = 0;
	private Context mContext;
	private Window win = null;
	private ContentResolver contentResolver = null;	

	// Query the Device model
	String device = android.os.Build.DEVICE;
	Boolean isG3 = false;
	
	int id_layout = -1;

	
	
	@Override
	public void onCreateQcActivity() {
		// Retrieve a view for the QuickCircle window.
		final View circlemainView = findViewById(R.id.cover_main_view);

		// Is this G3?
		Log.d(TAG, "device:" + device);
		isG3 = device.equals("g3") || device.equals("tiger6");
		Log.d(TAG, "isG3:" + isG3);

		// Get application context
		mContext = getApplicationContext();

		// Get content resolver
		contentResolver = getContentResolver();

		// Register an IntentFilter and a broadcast receiver
		registerIntentReceiver();

		// Set window flags
		setQuickCircleWindowParam();

		// Get QuickCircle window information
		initializeViewInformationFromDB();		

		// Crops a layout for the QuickCircle window
		setCircleLayoutParam(circlemainView);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext.unregisterReceiver(mIntentReceiver);
	}

	private void registerIntentReceiver() {

		IntentFilter filter = new IntentFilter();
		// Add QCircle intent to the intent filter
		filter.addAction(ACTION_ACCESSORY_COVER_EVENT);
		// Register a broadcast receiver with the system
		mContext.registerReceiver(mIntentReceiver, filter);

	}

	private void setQuickCircleWindowParam() {
		win = getWindow();
		if (win != null) {
			// Show the sample application view on top
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_FULLSCREEN
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	void setCircleLayoutParam(View view) {

		RelativeLayout layout = (RelativeLayout) view;
		RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) layout
				.getLayoutParams();

		// Set layout size same as a circle window size
		layoutParam.width = circleDiameter;
		layoutParam.height = circleDiameter;

		if (circleXpos < 0) {

			// Place a layout to the center
			layoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.CENTER_IN_PARENT);
		} else {
			layoutParam.leftMargin = circleXpos;
		}

		// Set top margin to the offset
		if (isG3) {
			layoutParam.topMargin = circleYpos;
			Log.i(TAG, "topMargin :" + circleYpos);
		} else {
			layoutParam.topMargin = circleYpos
					+ (circleHeight - circleDiameter) / 2;
			Log.i(TAG, "topMargin :"
					+ (circleYpos + (circleHeight - circleDiameter) / 2));
		}

		layout.setLayoutParams(layoutParam);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

	}

	void initializeViewInformationFromDB() {

		Log.d(TAG, "initializeViewInformationFromDB");
		if (contentResolver == null) {
			return;
		}

		Log.d(TAG, "initializeViewInformationFromDB");

		// Check the availability of the case
		quickCircleEnabled = Settings.Global.getInt(contentResolver,
				QUICKCOVERSETTINGS_QUICKCOVER_ENABLE, 0) == 0 ? true : false;
		if (DEBUG) {
			Log.d(TAG, "quickCircleEnabled:" + quickCircleEnabled);
		}

		// Get a case type
		quickCaseType = Settings.Global
				.getInt(contentResolver, "cover_type", 0/* default value */);

		// [START] Get the QuickCircle window information
		int id = getResources().getIdentifier("config_circle_window_width",
				"dimen", "com.lge.internal");
		circleWidth = getResources().getDimensionPixelSize(id);
		if (DEBUG) {
			Log.d(TAG, "circleWidth:" + circleWidth);
		}

		id = getResources().getIdentifier("config_circle_window_height",
				"dimen", "com.lge.internal");
		circleHeight = getResources().getDimensionPixelSize(id);
		if (DEBUG) {
			Log.d(TAG, "circleHeight:" + circleHeight);
		}

		id = getResources().getIdentifier("config_circle_window_x_pos",
				"dimen", "com.lge.internal");
		circleXpos = getResources().getDimensionPixelSize(id);
		if (DEBUG) {
			Log.d(TAG, "circleXpos:" + circleXpos);
		}

		id = getResources().getIdentifier("config_circle_window_y_pos",
				"dimen", "com.lge.internal");
		circleYpos = getResources().getDimensionPixelSize(id);
		if (DEBUG) {
			Log.d(TAG, "circleYpos:" + circleYpos);
		}

		id = getResources().getIdentifier("config_circle_diameter", "dimen",
				"com.lge.internal");
		circleDiameter = getResources().getDimensionPixelSize(id);
		if (DEBUG) {
			Log.d(TAG, "circleDiameter:" + circleDiameter);
		}
		// [END]
	}


	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action == null) {
				return;
			}

			// Receives a LG QCirle intent for the cover event
			if (ACTION_ACCESSORY_COVER_EVENT.equals(action)) {

				if (DEBUG) {
					Log.d(TAG, "ACTION_ACCESSORY_COVER_EVENT");
				}

				// Gets the current state of the cover
				mQuickCoverState = intent.getIntExtra(
						EXTRA_ACCESSORY_COVER_STATE,
						EXTRA_ACCESSORY_COVER_OPENED);

				if (DEBUG) {
					Log.d(TAG, "mQuickCoverState:" + mQuickCoverState);
				}

				if (mQuickCoverState == EXTRA_ACCESSORY_COVER_CLOSED) { // closed
					// Set window flags
					setQuickCircleWindowParam();
				} else if (mQuickCoverState == EXTRA_ACCESSORY_COVER_OPENED) { // opened
					// Call FullScreenActivity
					Intent callFullscreen = new Intent(mContext,
							FullScreenActivity.class);
					startActivity(callFullscreen);

					// Finish QCircleActivity
					QcActivity.this.finish();
				}
			}
		}
	};
	

}

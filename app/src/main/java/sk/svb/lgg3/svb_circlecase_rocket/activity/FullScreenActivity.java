package sk.svb.lgg3.svb_circlecase_rocket.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.adapter.GameStatsAdapter;
import sk.svb.lgg3.svb_circlecase_rocket.dialog.DeleteAllScores;
import sk.svb.lgg3.svb_circlecase_rocket.dialog.HighScoreNameFragmentDialog;
import sk.svb.lgg3.svb_circlecase_rocket.fragments.HighScoreLRFragment;
import sk.svb.lgg3.svb_circlecase_rocket.fragments.HighScoreRTFragment;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;


import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FullScreenActivity extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_high_scores);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		refreshList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_delete) {

			DeleteAllScores f = DeleteAllScores.newInstance();
			f.show(getFragmentManager(), DeleteAllScores.TAG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void refreshList(){
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Class<? extends Fragment>[] fragClasses = new Class[]{
				HighScoreLRFragment.class,
				HighScoreRTFragment.class
		};

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = new HighScoreLRFragment();
			switch (position) {
				case 0:
					fragment = new HighScoreLRFragment();
					break;

				case 1:
					fragment = new HighScoreRTFragment();
					break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			return fragClasses.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return getString(R.string.high_score_lr);
				case 1:
					return getString(R.string.high_score_rt);
			}
			return null;
		}
	}

}

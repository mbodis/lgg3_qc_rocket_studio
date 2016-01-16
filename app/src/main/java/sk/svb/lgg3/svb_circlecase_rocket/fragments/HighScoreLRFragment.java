package sk.svb.lgg3.svb_circlecase_rocket.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.adapter.GameStatsAdapter;
import sk.svb.lgg3.svb_circlecase_rocket.dialog.HighScoreNameFragmentDialog;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;

/**
 * Created by mbodis on 6/14/15.
 */
public class HighScoreLRFragment extends Fragment {
    public static final String TAG = HighScoreLRFragment.class.getName();

    public HighScoreLRFragment() {
    }

    private ListView mListView;
    private TextView emptyList;
	List<GameStats> list = new ArrayList<GameStats>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_high_socre, container, false);
        mListView = (ListView)rootView.findViewById(R.id.list);
        emptyList = (TextView)rootView.findViewById(R.id.no_score);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), getString(R.string.use_long_click), Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				GameStats gs = (GameStats) parent.getItemAtPosition(position);
				HighScoreNameFragmentDialog f = HighScoreNameFragmentDialog
						.newInstance(gs.getName(), gs.getId());
				f.show(getActivity().getFragmentManager(), HighScoreNameFragmentDialog.TAG);
				return false;
			}
		});
        refreshList();

        return rootView;
    }

    public void refreshList() {
		list = GameStats.findWithQuery(GameStats.class,
                "select * from game_stats where type=? order by score desc limit 10",
                String.valueOf(GameStats.GAME_LR));
        mListView.setAdapter(new GameStatsAdapter(getActivity(), R.layout.adapter_gamestats,
                list));
        emptyList.setVisibility((mListView.getAdapter().getCount() == 0) ? View.VISIBLE : View.GONE);
	}

}

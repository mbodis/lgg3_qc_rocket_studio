package sk.svb.lgg3.svb_circlecase_rocket.adapter;

import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameStatsAdapter extends ArrayAdapter<GameStats> {

	public static final String TAG = GameStatsAdapter.class.getName();
	public Activity act;

	public GameStatsAdapter(Activity act, int textViewResourceId,
			List<GameStats> list) {
		super(act, textViewResourceId, list);
		this.act = act;
	}

	static class ViewHolder {
		public TextView highScore;
		public TextView date;
		public TextView name;
		public TextView type;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView != null) {
			Object o = convertView.getTag();
			if (o != null) {
				holder = (ViewHolder) o;
			}
		}

		if ((convertView == null) || (holder == null)) {

			LayoutInflater inflater = (LayoutInflater) act
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.adapter_gamestats, parent,
					false);
			holder = new ViewHolder();
			holder.highScore = ((TextView) convertView.findViewById(R.id.score));
			holder.date = ((TextView) convertView.findViewById(R.id.timestamp));
			holder.name = ((TextView) convertView.findViewById(R.id.name));
			holder.type = ((TextView) convertView.findViewById(R.id.gtype));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GameStats gs = getItem(position);
		holder.date.setText(gs.getDate());
		if (gs.getName() != null) {
			holder.name.setText(gs.getName());
		}
		holder.type.setText(gs.getType() == GameStats.GAME_LR ? act
				.getString(R.string.game_lr) : act.getString(R.string.game_rt));
		holder.highScore.setText(act.getString(R.string.actual_score) + ": "
				+ gs.getScore());

		return convertView;

	}

}

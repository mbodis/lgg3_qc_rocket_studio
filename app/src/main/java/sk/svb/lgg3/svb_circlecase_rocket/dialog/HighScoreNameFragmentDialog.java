package sk.svb.lgg3.svb_circlecase_rocket.dialog;

import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.activity.FullScreenActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HighScoreNameFragmentDialog extends DialogFragment {

	public static final String TAG = HighScoreNameFragmentDialog.class
			.getName();

	public static HighScoreNameFragmentDialog newInstance(String oldName, long id) {
		HighScoreNameFragmentDialog frag = new HighScoreNameFragmentDialog();
		Bundle args = new Bundle();
		args.putString("name", oldName);
		args.putLong("id", id);
		frag.setArguments(args);

		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final String oldName = getArguments().getString("name");
		final Long recordId = getArguments().getLong("id");

		final View dialogView = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_high_score_name, null);
		final EditText nameET = ((EditText) dialogView
				.findViewById(android.R.id.edit));

		if (oldName != null) {
			nameET.setText(oldName);
		}

		final AlertDialog d = new AlertDialog.Builder(getActivity())
				.setView(dialogView)
				.setTitle(getActivity().getString(R.string.high_score_title))
				.setPositiveButton(R.string.save, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						List<GameStats> l = GameStats.find(GameStats.class,
								"id = ?", String.valueOf(recordId));
						if (l.size() == 1) {
							GameStats gs = l.get(0);
							gs.setName(nameET.getText().toString());
							gs.save();
							Toast.makeText(getActivity(),
									getString(R.string.saved),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(),
									getString(R.string.error_save),
									Toast.LENGTH_SHORT).show();
						}
						((FullScreenActivity)getActivity()).refreshList();

					}
				}).setNegativeButton(R.string.cancel, null).create();

		return d;
	}

}
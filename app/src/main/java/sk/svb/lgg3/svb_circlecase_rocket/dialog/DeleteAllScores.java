package sk.svb.lgg3.svb_circlecase_rocket.dialog;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.activity.FullScreenActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteAllScores extends DialogFragment {

	public static final String TAG = DeleteAllScores.class.getName();

	public static DeleteAllScores newInstance() {
		DeleteAllScores frag = new DeleteAllScores();

		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final AlertDialog d = new AlertDialog.Builder(getActivity())

		.setTitle(R.string.delete_all_score_title)
				.setMessage(R.string.delete_all_score_message)
				.setPositiveButton(R.string.delete, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						GameStats.deleteAll(GameStats.class);
						Toast.makeText(getActivity(),
								getString(R.string.data_removed),
								Toast.LENGTH_SHORT).show();
						((FullScreenActivity) getActivity()).refreshList();

					}
				}).setNegativeButton(R.string.cancel, null).create();

		return d;
	}
}
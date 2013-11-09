package ch.openech.mj.android;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Space;
import ch.openech.mj.toolkit.ClientToolkit.ConfirmDialogType;
import ch.openech.mj.toolkit.ClientToolkit.DialogListener;
import ch.openech.mj.toolkit.ClientToolkit.DialogListener.DialogResult;

public class AndroidHelper {

	public static Space createSpace(Context context, int width) {
		Space space = new Space(context);
		space.setMinimumWidth(width);
		return space;
	}

	public static AlertDialog createAlertDialog(Context context, String title,
			String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		return builder.create();
	}

	public static AlertDialog createConfirmationDialog(Context context,
			String title, String message, ConfirmDialogType dialogType,
			DialogListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		updateAlertDialogOptions(builder, dialogType, listener);
		return builder.create();

	}

	public static void updateAlertDialogOptions(AlertDialog.Builder builder,
			ConfirmDialogType dialogType, final DialogListener listener) {

		builder.setPositiveButton("Yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.close(DialogResult.YES);
			}
		});

		builder.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.close(DialogResult.NO);
			}
		});

		if (dialogType == ConfirmDialogType.YES_NO_CANCEL) {
			builder.setNeutralButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					listener.close(DialogResult.CANCEL);
				}
			});
		}
	}
	
	public static String createRandomString(int length) {
		Random r = new Random();
		int randomLength = r.nextInt(length) + 1;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < randomLength; i++) {
			builder.append((char) (r.nextInt(26) + 65));
		}
		return builder.toString();
	}
	
	public static String createRandomString(String prefix, int length) {
		return prefix + createRandomString(length);
	}
	
	
	

}
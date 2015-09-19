package com.creativedrewy.wearss.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Helper methods related to Android dialogs
 */
public class AlertHelper {

    /**
     * Show a dialog that has one button - OK
     * @param context Context to grab string resources
     * @param titleRes String resource id to be put in the title
     * @param msgRes String resource id to be put in the message
     */
    public static void showOkDialog(Context context, int titleRes, int msgRes) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(titleRes))
                .setMessage(context.getString(msgRes))
                .setNegativeButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    /**
     * Show a yes/no dialog, providing the yes listener function
     */
    public static void showYesNoDialog(Context context, int titleRes, int msgRes, DialogInterface.OnClickListener posListener) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(titleRes))
                .setMessage(context.getString(msgRes))
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(android.R.string.yes, posListener)
                .show();
    }
}

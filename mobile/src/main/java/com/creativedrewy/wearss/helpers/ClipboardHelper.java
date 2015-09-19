package com.creativedrewy.wearss.helpers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Utility method for interacting with the clipboard
 */
public class ClipboardHelper {
    private Context mContext;
    private ClipboardManager mClipboard;

    /**
     * Constructor
     */
    public ClipboardHelper(Context context) {
        mContext = context;
        mClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * Get the clipboard string data; return empty string if there isn't anything relevant
     */
    public String getClipboardURL() {
        String returnString = "";

        try {
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            CharSequence pasteText = item.getText();

            if (pasteText != null) {
                returnString = pasteText.toString();
            } else {
                Uri pasteUri = item.getUri();
                if (pasteUri != null) {
                    returnString = pasteUri.toString();
                }
            }
        }
        //NOTE: We intentionally do nothing here; fail silently
        catch (Exception e) { }

        return returnString;
    }
}

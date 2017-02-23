package com.griper.griperapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.griper.griperapp.BaseApplication;

/**
 * Created by Sarthak on 20-02-2017
 */

public class Utils {

    public static Typeface getThisTypeFace(Context context, int type) {
        Typeface typeface;
        switch (type) {

            case AppConstants.OPEN_SANS_REGULAR:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
            case AppConstants.PT_SANS_WEB_REGULAR:
                typeface = FontCache.getFont(context, "fonts/PT_Sans-Web-Regular.ttf");
                break;
            default:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
        }
        return typeface;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public static void showToast(Context context, String msgString) {
        Toast.makeText(context, msgString, Toast.LENGTH_SHORT).show();
    }

    public static boolean isApplicationInBackGround(Context context) {
        return ((BaseApplication) context).getResumeCount()
                == ((BaseApplication) context).getPauseCount();
    }
}

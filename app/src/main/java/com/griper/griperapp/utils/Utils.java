package com.griper.griperapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.griper.griperapp.BaseApplication;
import com.griper.griperapp.dbmodels.UserPreferencesData;
import com.griper.griperapp.dbmodels.UserProfileData;

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

    public static void showSnackBar(View viewById, String msgString) {
        Snackbar.make(viewById, msgString, Snackbar.LENGTH_SHORT).show();
    }

    public static int convertDipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

    public static boolean isApplicationInBackGround(Context context) {
        return ((BaseApplication) context).getResumeCount()
                == ((BaseApplication) context).getPauseCount();
    }

    public static void deleteDbTables() {
        UserPreferencesData.deleteUserPreferencesData();
        UserProfileData.deleteUserData();
    }
}

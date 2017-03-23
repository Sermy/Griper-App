package com.griper.griperapp.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/*
 *   Created by Sarthak on 12-03-2017
 */

public class CrashlyticsTree extends Timber.Tree {
    @Override
    public void v(String message, Object... args) {
        logMessage(message, args);
    }

    @Override
    public void v(Throwable t, String message, Object... args) {
        logMessage(message, args);
        // NOTE: We are explicitly not sending the exception to Crashlytics here.
    }

    @Override
    public void i(String message, Object... args) {
        logMessage(message, args);
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        logMessage(message, args);
        // NOTE: We are explicitly not sending the exception to Crashlytics here.
    }

    @Override
    public void w(String message, Object... args) {
        logMessage("WARN: " + message, args);
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        logMessage("WARN: " + message, args);
        // NOTE: We are explicitly not sending the exception to Crashlytics here.
    }

    @Override
    public void e(String message, Object... args) {
        logMessage("ERROR: " + message, args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        logMessage("ERROR: " + message, args);
        Crashlytics.logException(t);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
        log(priority, tag, message, t);
        if (t != null) {
            if (priority == Log.ERROR || priority == Log.WARN) {
                logException(t);
            }
        }
    }

    private void logMessage(String message, Object... args) {
        try {
            Crashlytics.log(String.format(message, args));
        } catch (Exception e) {
            //catch this as CrashlyticsTree throws exception if any special character is logged
            Timber.e(Log.getStackTraceString(e));
        }
    }

    private void logException(Throwable t) {
        try {
            Crashlytics.logException(t);
        } catch (Exception e) {
            //catch this as CrashlyticsTree throws exception if any special character is logged
            Timber.e(Log.getStackTraceString(e));
        }
    }

}

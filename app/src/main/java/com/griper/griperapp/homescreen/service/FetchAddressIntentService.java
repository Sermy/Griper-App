package com.griper.griperapp.homescreen.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;


import com.griper.griperapp.utils.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class FetchAddressIntentService extends IntentService {

    private String TAG = "FetchAddressIntentService";
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                AppConstants.LOCATION_DATA_EXTRA);
        mReceiver = intent.getParcelableExtra(AppConstants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        if (location == null) {
            errorMessage = "No location Data Provided";
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage, errorMessage);
            return;
        }

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Address not found!";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid Latitude Lonitude Used";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No Address Found";
            }
            deliverResultToReceiver(AppConstants.FAILURE_RESULT, errorMessage, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            String completeAddress = "";

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
                Log.i("Address", addressFragments.get(i));
                if (i == 0) {
                    continue;
                }
                if (i != (address.getMaxAddressLineIndex() - 1)) {
                    completeAddress += addressFragments.get(i) + ", ";
                } else {
                    completeAddress += addressFragments.get(i);
                }
            }
            Timber.i("Complete Address: " + completeAddress.concat("|" + address.getPostalCode()));
            deliverResultToReceiver(AppConstants.SUCCESS_RESULT, completeAddress, address.getPostalCode());

        }
    }

    private void deliverResultToReceiver(int resultCode, String message, String postCode) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.RESULT_DATA_KEY, message);
        bundle.putString(AppConstants.RESULT_DATA_POSTCODE_KEY, postCode);
        mReceiver.send(resultCode, bundle);
    }
}

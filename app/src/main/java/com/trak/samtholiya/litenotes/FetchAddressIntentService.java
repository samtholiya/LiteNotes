package com.trak.samtholiya.litenotes;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG="GeoCoder";

    public final class Constants {
            public static final int SUCCESS_RESULT = 0;
            public static final int FAILURE_RESULT = 1;
            public static final String PACKAGE_NAME =
                "com.google.android.gms.location.sample.locationaddress";
            public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
            public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
            public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }

    private Geocoder geocoder;
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        geocoder = new Geocoder(this, Locale.getDefault());
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);
        mReceiver=intent.getParcelableExtra(Constants.RECEIVER);
        String errorMessage = "";
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }

       protected ResultReceiver mReceiver;

        private void deliverResultToReceiver(int resultCode, String message) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.RESULT_DATA_KEY, message);
                    mReceiver.send(resultCode, bundle);
         }

}

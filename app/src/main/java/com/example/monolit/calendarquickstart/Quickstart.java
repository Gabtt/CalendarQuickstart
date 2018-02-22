package com.example.monolit.calendarquickstart;

/**
 * Created by gabriel_batistell on 22/02/18.
 */


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.EventLog;
import android.widget.Toast;

import com.example.monolit.calendarquickstart.calendar_connections.EventCalendarCreate;
import com.example.monolit.calendarquickstart.calendar_connections.EventCreate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Calendar;

import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Quickstart {
    Activity context;
    GoogleAccountCredential mCredential;


    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int REQUEST_ACCOUNT_PICKER = 1000;

    public Quickstart(Activity context, GoogleAccountCredential credential) {
        this.mCredential = credential;
        this.context = context;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void createEvent(Event event, String calendarId,Quickstart.OnEventCreated listener){
        if(canGetResultsFromApi()){
            new EventCreate(mCredential, calendarId, event, listener).execute();
        }
    }


    void createCalendar(Calendar calendar, Quickstart.OnCalendarCreated listener){
        if(canGetResultsFromApi()){
            new EventCalendarCreate(mCredential, calendar, listener).execute();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    boolean canGetResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            return false;
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
            return false;
        } else {
            return true;
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                context, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = ((Activity) context).getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            } else {
                // Start a dialog from which the user can choose an account
                context.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    context,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ((MainActivity) context),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }



    public interface OnEventUpdated{
        void onUpdated(String eventId);
    }
    public interface OnEventCreated{
        void onCreated(String eventId);
    }
    public interface OnCalendarCreated{
        void onCreated(String calendarId);
    }
}

package com.example.monolit.calendarquickstart.calendar;

/**
 * Created by gabriel_batistell on 22/02/18.
 */


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.example.monolit.calendarquickstart.MainActivity;
import com.example.monolit.calendarquickstart.calendar.calendar_connections.EventCalendarCreate;
import com.example.monolit.calendarquickstart.calendar.calendar_connections.EventCreate;
import com.example.monolit.calendarquickstart.calendar.calendar_connections.EventDelete;
import com.example.monolit.calendarquickstart.calendar.calendar_connections.EventUpdate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential ;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Calendar;

import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CalendarApi {
    Activity activity;

    private   final String TAG = "CreateEventDebug";

    GoogleAccountCredential mCredential;
    private   final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private   final String PREF_ACCOUNT_NAME = "accountName";

      final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
      final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
      final int REQUEST_ACCOUNT_PICKER = 1000;

    public CalendarApi(Activity activity,GoogleAccountCredential credential) {
        this.mCredential = credential;
        this.activity = activity;
    }

      void acquireGooglePlayServices() {
        Log.d(TAG, "acquireGooglePlayServices: adquire google play service");
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(activity, connectionStatusCode);
        }
    }

      public void createEvent(Event event, String calendarId,final CalendarApi.OnEventCreated listener){
        if(canGetResultsFromApi()){

            Log.d(TAG, "createEvent: entrooou, pode pegar resultados da api, agora vai criar objeto EventCreate");
            new EventCreate( mCredential, calendarId, event, activity ,  new OnEventCreated() {
                @Override
                public void onCreated(Event event) {
                    listener.onCreated(event);

                }
            }).execute();
        }
    }

      void updateEvent(Event eventUpdated,String calendarId , final CalendarApi.OnEventUpdated listener){
        if(canGetResultsFromApi()){
// TODO: 07/03/2018 REMOVER SAPODA DAQUI E ADICIONAR NO OBJETO EVENTUPDATE
            new EventUpdate(mCredential, calendarId, eventUpdated, new OnEventUpdated() {
                @Override
                public void onUpdated(Event event) {
                    listener.onUpdated(event);
                }
            }).execute();
        }
    }

    public void deleteEvent(String eventId, String calendarId, final CalendarApi.OnEventDeleted listener){
        if (canGetResultsFromApi()){
            new EventDelete(mCredential, calendarId, eventId, listener);
        }
    }


    public void createCalendar(Calendar calendar, CalendarApi.OnCalendarCreated listener){
        if(canGetResultsFromApi()){
            new EventCalendarCreate(mCredential, calendar, listener).execute();
        }
    }

      boolean isGooglePlayServicesAvailable() {
        Log.d(TAG, "isGooglePlayServicesAvailable: ve se o googlePlayService esta disponivel");
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    private boolean canGetResultsFromApi( ) {
        Log.d(TAG, "canGetResultsFromApi: ve se pode pegar resultados da api");
        Log.d(TAG, "canGetResultsFromApi: criou credencial dnv");

        if (!isGooglePlayServicesAvailable()) {
            Log.d(TAG, "canGetResultsFromApi: Google Play service não esta disponivel");
            acquireGooglePlayServices();
            return false;
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d(TAG, "canGetResultsFromApi: GPlayS esta disponivel, mas accountName não esta salva");
            chooseAccount();
            return false;
        } else {
            Log.d(TAG, "canGetResultsFromApi: pode pegar resultados da api, deu true");
            return true;
        }
    }

    private void showGooglePlayServicesAvailabilityErrorDialog(Context context,final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ((MainActivity) context),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
      void chooseAccount() {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.GET_ACCOUNTS)) {

            String accountName = ((Activity) activity).getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }



    public   interface OnEventUpdated{
        void onUpdated(Event event);
    }
    public interface OnEventCreated{
        void onCreated(Event event );
    }
    public interface OnEventDeleted{
        void onDeleted(Void executou);
    }
    public interface OnGetEvent{
        void onGet(Event event);
    }
    public interface OnCalendarCreated{
        void onCalendarCreated(String calendarId);
    }

}

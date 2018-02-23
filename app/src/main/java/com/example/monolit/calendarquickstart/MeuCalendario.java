package com.example.monolit.calendarquickstart;

/**
 * Created by gabriel_batistell on 22/02/18.
 */


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.example.monolit.calendarquickstart.calendar_connections.EventCalendarCreate;
import com.example.monolit.calendarquickstart.calendar_connections.EventCreate;
import com.example.monolit.calendarquickstart.calendar_connections.EventGet;
import com.example.monolit.calendarquickstart.calendar_connections.EventUpdate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Calendar;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MeuCalendario {
    Activity context;
    GoogleAccountCredential mCredential;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    static final int REQUEST_ACCOUNT_PICKER = 1000;

    private static List<Event> eventList = new ArrayList<>();
    
    public MeuCalendario(Activity context, GoogleAccountCredential credential) {
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

    void createEvent(Event event, String calendarId, final MeuCalendario.OnEventCreated listener){
        if(canGetResultsFromApi()){
           
            new EventCreate(mCredential, calendarId, event,  new OnEventCreated() {
                @Override
                public void onCreated(Event event) {
                    saveEvent(event);
                    listener.onCreated(event);

                }
            }).execute();
        }
    }

    void updateEvent(Event eventUpdated, String eventId , String calendarId , final MeuCalendario.OnEventUpdated listener){
        if(canGetResultsFromApi()){
            
            new EventUpdate(mCredential, calendarId, eventId, eventUpdated, new OnEventUpdated() {
                @Override
                public void onUpdated(Event event) {
                    listener.onUpdated(event);
                }
            }).execute();
        }
    }

    void createCalendar(Calendar calendar, MeuCalendario.OnCalendarCreated listener){
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
    private boolean canGetResultsFromApi() {
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

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ((MainActivity) context),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

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

    public void saveEvent(Event event){
        eventList.add(event);
    }
    public void removeEvent(Event event){
        eventList.remove(event);
    }
    public static void updateEvent(Event eventToUpdate, Event eventUpdated){
        eventList.set(eventList.indexOf(eventToUpdate), eventUpdated);
    }

    public static void setEventList(List<Event> eventList) {
        MeuCalendario.eventList = eventList;
    }
    public static List<Event> getEventList() {
        return eventList;
    }


    public interface OnEventUpdated{
        void onUpdated(Event event);
    }
    public interface OnEventCreated{
        void onCreated(Event event );
    }
    public interface OnGetEvent{
        void onGet(Event event);
    }
    public interface OnCalendarCreated{
        void onCreated(String calendarId);
    }
}

package com.example.monolit.calendarquickstart.calendar_connections;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gabriel_batistell on 22/02/18.
 */

@Deprecated
class EventsGetLast extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    GoogleAccountCredential credential;

    EventsGetLast(GoogleAccountCredential credential) {
        this.credential = credential;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();


        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            calListEvents();
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    void calListEvents() throws IOException {
        // Build a new authorized API client mService.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        // List the next 10 events from the primary calendar.

        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }


    private List<String> getDataFromApi() throws IOException {

        List<String> eventStrings = new ArrayList<String>();
        eventStrings.add("");

        return eventStrings;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(List<String> output) {
    }

    @Override
    protected void onCancelled() {
    }
}

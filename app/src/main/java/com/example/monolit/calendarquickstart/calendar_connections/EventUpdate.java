package com.example.monolit.calendarquickstart.calendar_connections;

import android.os.AsyncTask;

import com.example.monolit.calendarquickstart.Quickstart.OnEventCreated;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.Event;


import java.io.IOException;

/**
 * Created by gabriel_batistell on 22/02/18.
 */

public class EventUpdate extends AsyncTask<Void, Void, String> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private OnEventCreated listener;
    String calendarId;

    private Event event;
    private GoogleAccountCredential credential;

    public EventUpdate(GoogleAccountCredential credential, String calendar_id, Event event, OnEventCreated listener) {
        this.listener = listener;
        this.event = event;
        this.credential = credential;
        this.calendarId = calendar_id;

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return callUpdateEvent();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    String callUpdateEvent() throws IOException {


// Retrieve the event from the API
        Event event = mService.events().get("primary", "eventId").execute();

// Make a change
        event.setSummary("Appointment at Somewhere");

// Update the event
        Event updatedEvent = mService.events().update("primary", event.getId(), event).execute();

        event = mService.events().insert(calendarId, event).execute();
        return event.getId();

    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String output) {
        listener.onCreated(output);

    }

    @Override
    protected void onCancelled() {
    }
}

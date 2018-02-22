package com.example.monolit.calendarquickstart.calendar_connections;

import android.os.AsyncTask;

import com.example.monolit.calendarquickstart.Quickstart;
import com.example.monolit.calendarquickstart.Quickstart.OnEventCreated;
import com.example.monolit.calendarquickstart.Quickstart.OnEventUpdated;
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
    private OnEventUpdated listener;
    String eventId;
    String calendarId;

    private Event event;
    private GoogleAccountCredential credential;

    public EventUpdate(GoogleAccountCredential credential, String calendar_id, String eventId, Event event, OnEventUpdated listener) {
        this.listener = listener;
        this.event = event;
        this.eventId = eventId;
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

        Event updatedEvent = mService.events().update(calendarId, eventId, event).execute();
        return updatedEvent.getId();

    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String output) {
        listener.onUpdated(output);

    }

    @Override
    protected void onCancelled() {
    }
}

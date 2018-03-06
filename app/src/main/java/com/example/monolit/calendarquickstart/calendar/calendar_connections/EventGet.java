package com.example.monolit.calendarquickstart.calendar.calendar_connections;

import android.os.AsyncTask;

import com.example.monolit.calendarquickstart.calendar.MeuCalendario;
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

public class EventGet extends AsyncTask<Void, Void, Event> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private MeuCalendario.OnGetEvent listener;
    String calendarId;

    private String eventId;

    public EventGet (GoogleAccountCredential credential, String calendar_id, String eventId, MeuCalendario.OnGetEvent listener) {
        this.listener = listener;
        this.eventId = eventId;
        this.calendarId = calendar_id;

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    @Override
    protected Event doInBackground(Void... params) {
        try {
            return callGetEvent();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }


    Event callGetEvent() throws IOException {
        Event event = mService.events().get(calendarId, eventId).execute();
        return event;

    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Event output) {
        listener.onGet(output);

    }

    @Override
    protected void onCancelled() {
    }
}

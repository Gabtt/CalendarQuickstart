package com.example.monolit.calendarquickstart.calendar.calendar_connections;

import android.os.AsyncTask;

import com.example.monolit.calendarquickstart.calendar.MeuCalendario.OnCalendarCreated;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


import java.io.IOException;

/**
 * Created by gabriel_batistell on 22/02/18.
 */

public class EventCalendarCreate extends AsyncTask<Void, Void, String> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    com.google.api.services.calendar.model.Calendar calendar;
    OnCalendarCreated listener;

    public EventCalendarCreate(GoogleAccountCredential credential, com.google.api.services.calendar.model.Calendar calendar, OnCalendarCreated listener) {
        this.calendar = calendar;
        this.listener = listener;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return callCreateCalendar();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    private String callCreateCalendar() throws IOException {
        com.google.api.services.calendar.model.Calendar createdCalendar = mService.calendars().insert(calendar).execute();
        return createdCalendar.getId();
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String output) {
        listener.onCalendarCreated(output);
    }

    @Override
    protected void onCancelled() {
    }
}


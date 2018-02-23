package com.example.monolit.calendarquickstart.calendar_connections;

import android.os.AsyncTask;

import com.example.monolit.calendarquickstart.MeuCalendario.OnEventCreated;
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


public class EventCreate extends AsyncTask<Void, Void, Event> {
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private OnEventCreated listener;
    String calendarId;

    private Event event;
    private GoogleAccountCredential credential;

    public EventCreate(GoogleAccountCredential credential,String calendar_id,Event event, OnEventCreated listener) {
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
    protected Event doInBackground(Void... params) {
        try {
            return callCreateEvent();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    Event callCreateEvent() throws IOException {
        //DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
        //EventDateTime start = new EventDateTime()
        //        .setDateTime(startDateTime)
        //        .setTimeZone("America/Los_Angeles");
        //event.setStart(start);

        //DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
        //EventDateTime end = new EventDateTime()
        //        .setDateTime(endDateTime)
        //        .setTimeZone("America/Los_Angeles");
        //event.setEnd(end);

        //String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        //event.setRecurrence(Arrays.asList(recurrence));EventAttendee[] attendees = new EventAttendee[]{
        //        new EventAttendee().setEmail("lpage@example.com"),
        //        new EventAttendee().setEmail("sbrin@example.com"),
        //};
        //event.setAttendees(Arrays.asList(attendees));

        //EventReminder[] reminderOverrides = new EventReminder[]{
        //        new EventReminder().setMethod("email").setMinutes(24 * 60),
        //        new EventReminder().setMethod("popup").setMinutes(10),
        //};
        //Event.Reminders reminders = new Event.Reminders()
        //        .setUseDefault(false)
        //        .setOverrides(Arrays.asList(reminderOverrides));
        //event.setReminders(reminders);

        event = mService.events().insert(calendarId, event).execute();
        return event;

    }




    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Event output) {
        listener.onCreated(output);

    }

    @Override
    protected void onCancelled() {
    }
}

package com.example.monolit.calendarquickstart.calendar.calendar_connections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.monolit.calendarquickstart.calendar.MeuCalendario.OnEventCreated;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;


import java.io.IOException;
import java.util.Arrays;

/**
 * Created by gabriel_batistell on 22/02/18.
 */


public class EventCreate extends AsyncTask<Void, Void, Event> {
    private static final String TAG = "CreateEventDebug";
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private OnEventCreated listener;
    String calendarId;

    private Event event;

    public EventCreate(String calendar_id, Event event, Context context, OnEventCreated listener) {
        Log.d(TAG, "EventCreate: construindo o EventCreate");
        final String[] SCOPES = {CalendarScopes.CALENDAR};
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context.getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());

        Log.d(TAG, "EventCreate: pediu credencial dnv");

        this.listener = listener;
        this.event = event;
        this.calendarId = calendar_id;

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
        Log.d(TAG, "EventCreate: criou o mService e deu build");
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
        Log.d(TAG, "callCreateEvent: deu execute() no role pra inserir o evento.");
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

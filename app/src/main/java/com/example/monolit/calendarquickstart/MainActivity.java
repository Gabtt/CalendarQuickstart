package com.example.monolit.calendarquickstart;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    private Button btCallApiNewCalendar;
    private Button btCallApiNewEvent;
    ProgressDialog mProgress;
    Quickstart quickstart;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCredential();
        quickstart= new Quickstart(this, mCredential);
        initViews();

    }


    void createCredential() {
        // TODO: 22/02/18 criar escope e deletar se gor trocar pra outro.
        String[] scope = SCOPES;
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(scope)).setBackOff(new ExponentialBackOff());
    }

    public void initViews() {
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallApiButton = new Button(this);
        mCallApiButton.setText(BUTTON_TEXT);
        btCallApiNewEvent = new Button(this);
        btCallApiNewEvent.setText("New Event");
        btCallApiNewCalendar = new Button(this);
        btCallApiNewCalendar.setText("New Calendar");



        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                mCallApiButton.setEnabled(true);
            }
        });
        btCallApiNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Event event = new Event()
                        .setSummary("Google I/O 2015")
                        .setLocation("800 Howard St., San Francisco, CA 94103")
                        .setDescription("A chance to hear more about Google's developer products.");

                DateTime startDateTime = new DateTime("2018-02-22T09:00:00-07:00");
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setStart(start);

                DateTime endDateTime = new DateTime("2018-02-22T17:00:00-07:00");
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setEnd(end);

                String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
                event.setRecurrence(Arrays.asList(recurrence));

                    quickstart.createEvent(event,"primary",new Quickstart.OnEventCreated() {
                        @Override
                        public void onCreated(Event event) {

                            event.setSummary("ETA NOVINHA TU TA REBOLANDO BEM");
                            event.setDescription("Ai MEU PIRUUUUU");

                            quickstart.updateEvent(event, event.getId(), "primary", new Quickstart.OnEventUpdated() {
                                @Override
                                public void onUpdated(Event event) {
                                    Toast.makeText(MainActivity.this, "DEUBOM"+event.getId(),Toast.LENGTH_LONG).show();

                                }
                            });


                        }
                    });

            }
        });
        btCallApiNewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCallApiNewCalendar.setEnabled(false);
                com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
                calendar.setSummary("calendarSummary");
                calendar.setTimeZone("America/Los_Angeles");

                quickstart.createCalendar(calendar,new Quickstart.OnCalendarCreated() {
                    @Override
                    public void onCreated(String calendarId) {
                        Toast.makeText(MainActivity.this,calendarId,Toast.LENGTH_LONG).show();
                    }
                });
                mOutputText.setText("");
                btCallApiNewCalendar.setEnabled(true);
            }
        });

        activityLayout.addView(btCallApiNewEvent);
        activityLayout.addView(mCallApiButton);
        activityLayout.addView(btCallApiNewCalendar);


        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(
                "Click the \'" + BUTTON_TEXT + "\' button to test the API.");
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        setContentView(activityLayout);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                }
                break;
        }
    }

}
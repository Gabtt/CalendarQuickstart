package com.example.monolit.calendarquickstart;

import com.example.monolit.calendarquickstart.calendar.MeuCalendario;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "CreateEventDebug";
    private TextView tvEventName;
    private TextView tvEventDescription;
    private Button btCallApiNewEvent;
    ProgressDialog mProgress;
    MeuCalendario meuCalendario;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;


    private static final String PREF_ACCOUNT_NAME = "accountName";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();
    }



    public void initViews() {
        tvEventName = findViewById(R.id.event_name);
        tvEventDescription = findViewById(R.id.event_description);

        btCallApiNewEvent = findViewById(R.id.event_button);
        btCallApiNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: cliquei");
                final Event event = new Event()
                        .setSummary(tvEventName.getText().toString() + "bb")
                        .setDescription(tvEventDescription.getText().toString()+ "bb");

                DateTime startDateTime = new DateTime(System.currentTimeMillis());
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setStart(start);

                DateTime endDateTime = new DateTime(System.currentTimeMillis()+360000);
                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone("America/Los_Angeles");
                event.setEnd(end);

                Log.d(TAG, "onClick: criou o objeto evento com as infos da tela");
                MeuCalendario.createEvent(event, "primary", MainActivity.this, new MeuCalendario.OnEventCreated() {
                    @Override
                    public void onCreated(Event event) {


                        Toast.makeText(MainActivity.this, "DEU BOM", Toast.LENGTH_LONG);


                    }
                });

            }
        });


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");


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

                } else {
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                // TODO: 07/03/2018 conferir se esta salvando a poha do account name 
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

                        final String[] SCOPES = {CalendarScopes.CALENDAR};
                        GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());

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
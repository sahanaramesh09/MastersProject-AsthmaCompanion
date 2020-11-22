package com.sjsu.respiratoryhelper;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sjsu.respiratoryhelper.appconfig.BaseHelper;

import static com.sjsu.respiratoryhelper.MyBroadCastReceiver.NOTIFICATION_ID_BROADCAST;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    NotificationManager notificationManager;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //closes notification if it's there
        notificationManager.cancel(NOTIFICATION_ID_BROADCAST);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Start the background polling
        startFetchingDataAtInterval();
    }

    private void startFetchingDataAtInterval() {
        Log.d(TAG, "@@@@ Start Fetching data in background : " + System.currentTimeMillis());
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MyBroadCastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.disclaimer:
                showDisclaimer();
                return true;
            case R.id.stopNotification:
                stopNotification();
                return true;
            case R.id.resetEmergency:
                resetEmergencyNo();
                return true;
            case R.id.share:
                shareApplication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Disclaimer PopUp
     */
    private void showDisclaimer() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_disclaimer, null);
        Button mAgree = mView.findViewById(R.id.btAgree);

        mBuilder.setTitle("Disclaimer ");
        mBuilder.setMessage("As per our understanding, Our app entirely new way to use your health " +
                "and fitness information. The new Health app gives you an easy to read " +
                "dashboard of your health and fitness data.\n\n And we've created a new tool " +
                "for developers called HealthKit, which allows all the incredible health and " +
                "fitness apps to work together, and work harder, for you. It just might be " +
                "the beginning of a health rebellion.\n\n In case of any error, please contact us " +
                "immediately, so we can make proper corrections");
        mBuilder.setView(mView);

        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    /**
     * Stop Notification PopUp
     */
    private void stopNotification() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_stop_notification, null);
        Button mAgree = mView.findViewById(R.id.btAgree);

        mBuilder.setTitle("Don't show Notification");
        mBuilder.setMessage("Your App will Stop Showing Notification." +
                "This can mean missing out on important health information.");
        mBuilder.setView(mView);

        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopFetchingDataAtInterval();
                mDialog.dismiss();
            }
        });
    }


    private void stopFetchingDataAtInterval() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(), "Notification Cancelled", Toast.LENGTH_LONG).show();
    }

    /**
     * Reset emergency phn no
     */
    private void resetEmergencyNo() {
        SharedPreferences mSharedPreferences = getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(BaseHelper.EMERGENCY_PHN_NO, "6692268385");
        mEditor.putBoolean(BaseHelper.EMERGENCY_NO_FLAG, false);
        mEditor.apply();
    }

    /**
     * Share Application - Menu item implementation
     */
    private void shareApplication() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "respiratoryAPp");
            String shareMessage = "Hey, Let me recommend you this application\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception when shareApplication clicked: " + e.getMessage());
        }
    }

}
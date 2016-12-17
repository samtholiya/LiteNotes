package com.trak.samtholiya.litenotes;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.trak.samtholiya.litenotes.dummy.MainContent;

public class LiteDetailActivity extends AppCompatActivity implements OnFragmentInteractionListener  {

    LiteDetailFragment liteDetail;
    public static final String LITEDATA = "LITEDATA";
    public static final String LITEDATABASE = "LITEDATABASE";
    MainContent.MainItem item;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onResume() {
        super.onResume();
        if (liteDetail.saveCurrentData()) {
            MainActivity.dummyContent.getLiteBookAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(),R.string.data_not_saved,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_detail);
        //Log.d("called me once ot","-------------------------------------------notice me");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        item = (MainContent.MainItem) getIntent().getSerializableExtra(LITEDATA);
        liteDetail = LiteDetailFragment.newInstance(item);
        if (Build.VERSION.SDK_INT >= 21) {
            Log.d("her1 1","------------------------------------ok hrer_-------------new");
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.LEFT);
            slideTransition.setDuration(getApplicationContext().getResources().getInteger(R.integer.anim_duration_long));
            liteDetail.setEnterTransition(slideTransition);
            liteDetail.setExitTransition(slideTransition);
            liteDetail.setReenterTransition(slideTransition);
        }
        getFragmentManager().beginTransaction().replace(R.id.frame_1, liteDetail).commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

/*
    @Override
    public void onClick(View v) {

        if (liteDetail.saveCurrentData()) {
            MainActivity.dummyContent.getLiteBookAdapter().notifyDataSetChanged();
            Snackbar snack = Snackbar.make(v, R.string.data_saved, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            view.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPositive));
            snack.show();

        } else {
            Snackbar.make(v, "Error in saving data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }
*/

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LiteDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_lite_detail, menu);
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (liteDetail.saveCurrentData()) {
            MainActivity.dummyContent.getLiteBookAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(),R.string.data_not_saved,Toast.LENGTH_LONG).show();
        }
        overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("dfgh","dcfghjkl;edrftghjkdfghjk 88888888888888888 "+uri);
    }
}

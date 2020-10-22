package com.example.genrerecognitionjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.IACRCloudListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements IACRCloudListener{

    private ACRCloudClient mClient;
    private ACRCloudConfig mConfig;

    private TextView mVolume, mResult, tv_time;


    private boolean mProcessing = false;
    private boolean initState = false;

    private String path = "";

    private long startTime = 0;
    private long stopTime = 0;
    long maxid = 0;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public ListView mList;

    private static final int REQUEST_MIC = 1;
    FloatingActionButton fab1,fab2,fab3, fab0, fabCancel, fabSearch;
    Animation fab_open, fab_close, rotate_forward, rotate_backward, fab_open2, fab_close2, rotate_forward2, rotate_backward2;
    boolean isOpen=false;
    DatabaseReference reff;
    String ytSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = Environment.getExternalStorageDirectory().toString()
                + "/acrcloud/model";

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        fabSearch = findViewById(R.id.fabSearch);
        fabCancel =  findViewById(R.id.fabCancel);
        mVolume = (TextView) findViewById(R.id.volume);
        mResult = (TextView) findViewById(R.id.result);
        tv_time = (TextView) findViewById(R.id.time);
        fab0 = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);


        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        fab_open2 = AnimationUtils.loadAnimation(this, R.anim.fab_open2);
        fab_close2 = AnimationUtils.loadAnimation(this, R.anim.fab_close2);
        rotate_forward2 = AnimationUtils.loadAnimation(this, R.anim.rotate_forward2);
        rotate_backward2 = AnimationUtils.loadAnimation(this, R.anim.rotate_backward2);
        reff = FirebaseDatabase.getInstance().getReference().child("saved items/songs/");



        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Listener And Recognition Stuff
        fabSearch.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        searchYouTube();
                    }
                });

        fabCancel.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        animateFab2();
                        cancel();
                    }
                });

        //My Stuff


        fab0.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab2();
                requestMic();
                start();
            }
        });


        fab1.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view){
                animateFab();

            }
        });

        fab2.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                voiceinputbuttons();
                startVoiceRecognitionActivity();
            }
        });
        fab3.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                openSaved();
            }
        });
        this.mConfig = new ACRCloudConfig();
        this.mConfig.acrcloudListener = this;

        // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
        //this.mConfig.acrcloudResultWithAudioListener = this;

        this.mConfig.context = this;
        this.mConfig.host = "identify-eu-west-1.acrcloud.com";
        this.mConfig.dbPath = path; // offline db path, you can change it with other path which this app can access.
        this.mConfig.accessKey = "d75b31800bc154cec2989c22e9cae0cd";
        this.mConfig.accessSecret = "TdjOeR8TbUFe2v1eqQTadDhBnOLONU6o4spQqqXS";
        this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTPS; // PROTOCOL_HTTP
        this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;

        this.mClient = new ACRCloudClient();
        // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
        // the function initWithConfig is used to load offline db, and it may cost long time.
        this.initState = this.mClient.initWithConfig(this.mConfig);
        if (this.initState) {
//            this.mClient.startPreRecord(3000); //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
        }
        }



    public void voiceinputbuttons() {
        fab2 = findViewById(R.id.fab2);
        mList = findViewById(R.id.mListVC);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    private void requestMic(){
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_MIC);
                } else{
                    Toast.makeText(MainActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
                }
            }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MIC){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestMic();
            } else{
                Toast.makeText(MainActivity.this,"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void start() {
        if (!this.initState) {
            Toast.makeText(this, "init error", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mProcessing) {
            mProcessing = true;
            mVolume.setText("");
            mResult.setText("");
            if (this.mClient == null || !this.mClient.startRecognize()) {
                mProcessing = false;
                mResult.setText("Start error!");
            }
            startTime = System.currentTimeMillis();
        }
    }


    protected void cancel() {
        if (mProcessing && this.mClient != null) {
            mProcessing = false;
            this.mClient.cancel();
            tv_time.setText("");
            mResult.setText("");
            mVolume.setText("");
            Toast.makeText(MainActivity.this, "Cancelled.", Toast.LENGTH_SHORT).show();
        }
    }
    // Old api
    @Override
    public void onResult(String result) {
        if (this.mClient != null) {
            this.mClient.cancel();
            mProcessing = false;
        }


        String tres = "";


        try {
            int i = 0;
            JSONObject j = new JSONObject(result);
            JSONObject j1 = j.getJSONObject("status");
            int j2 = j1.getInt("code");
            if(j2 == 0){
                JSONObject metadata = j.getJSONObject("metadata");
                //

                if (metadata.has("music")) {
                    JSONArray musics = metadata.getJSONArray("music");
                    JSONObject tt = (JSONObject) musics.get(i);
                    String title = tt.getString("title");
                    JSONArray artistt = tt.getJSONArray("artists");
                    JSONObject art = (JSONObject) artistt.get(0);
                    String artist = art.getString("name");
                    tres = tres +  title + "    By:   " + artist;
                   // for(int i=0; i<musics.length(); i++) {
                   //     JSONObject tt = (JSONObject) musics.get(i);
                   //     String title = tt.getString("title");
                   //     JSONArray artistt = tt.getJSONArray("artists");
                   //     JSONObject art = (JSONObject) artistt.get(0);
                   //     String artist = art.getString("name");
                   //     tres = tres +  "  Title: " + title + "    Artist: " + artist;
                   // }
                }
            }
        } catch (JSONException e) {
            tres = result;
            e.printStackTrace();
        }

        mResult.setText("\n" + tres);
        if (TextUtils.isEmpty(tres)){
            Toast.makeText(MainActivity.this, "Could not recognise song, try again!", Toast.LENGTH_LONG).show();
            animateFab2();
        }
        else  {
            reff.child(String.valueOf( maxid +1));
            if (maxid >= 20){
                ytSearch = tres;
                Toast.makeText(MainActivity.this, "Could not save item!", Toast.LENGTH_SHORT).show();
                fabSearch.setClickable(true);
                fabSearch.setVisibility(View.VISIBLE);
            }else {
            tres = tres.replaceAll("[^a-zA-Z0-9:,() ]", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("saved items/songs/song" + maxid);
            animateFab2();
            myRef.setValue(tres);
            Toast.makeText(MainActivity.this, "Item Saved!", Toast.LENGTH_SHORT).show();}
        }

    }
    public void searchYouTube() {
        Uri webpage = Uri.parse("https://www.youtube.com/results?search_query=" + ytSearch);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(webIntent);
        }
        fabSearch.setClickable(false);
        fabSearch.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
            if (matches.contains("what song is this")){
                animateFab2();
                requestMic();
                start();
            } else if (matches.contains("what am I listening to")){
                animateFab2();
                requestMic();
                start();
            } else if (matches.contains("what song is on")){
                animateFab2();
                requestMic();
                start();
            }else if (matches.contains("what song is this")) {
                animateFab2();
                requestMic();
                start();

            }else if (matches.contains("go to saved items")){
                openSaved();
            }
            else if (matches.contains("go to saved")){
                openSaved();
            }
            else if (matches.contains("open saved items")){
                openSaved();
            }
            else if (matches.contains("open saved")){
                openSaved();
            }
        }
    }


            @Override
            public void onVolumeChanged ( double volume){
                long time = (System.currentTimeMillis() - startTime) / 1000;
                mVolume.setText(getResources().getString(R.string.volume) + volume + "\n\nRecord Time: " + time + " s");
            }


            @Override
            protected void onDestroy () {
                super.onDestroy();
                Log.e("MainActivity", "release");
                if (this.mClient != null) {
                    this.mClient.release();
                    this.initState = false;
                    this.mClient = null;
                }
            }


            public void openSaved () {
                Intent openSaved = new Intent(this, savedItems.class);
                startActivity(openSaved);
            }

            private void animateFab () {
                if (isOpen) {
                    fab1.startAnimation(rotate_forward);
                    fab2.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab2.setClickable(false);
                    fab3.setClickable(false);
                    isOpen = false;
                } else {
                    fab1.startAnimation(rotate_backward);
                    fab2.startAnimation(fab_open);
                    fab3.startAnimation(fab_open);
                    fab2.setClickable(true);
                    fab3.setClickable(true);
                    isOpen = true;
                }
            }
            private void animateFab2 () {
                if (isOpen) {
                    fab0.startAnimation(rotate_forward2);
                    fabCancel.startAnimation(fab_close2);
                    fabCancel.setClickable(false);
                    isOpen = false;
                } else {
                    fab0.startAnimation(rotate_backward2);
                    fabCancel.startAnimation(fab_open2);
                    fabCancel.setClickable(true);
                    isOpen = true;
                }
            }



        }

package com.example.genrerecognitionjava;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class savedItems extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    //Database Stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;




    private ListView mListView;
    private String Songs;
    private TextView mTextView;
    private ArrayAdapter adapter;
    private Switch playlistSwitch;
    private int plCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        playlistSwitch = findViewById(R.id.playlistSearch);
        mTextView = findViewById(R.id.songsTB);
        mListView = findViewById(R.id.songList);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Songs = myRef.child("saved items/songs/").toString();
        plCheck = 0;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showSong(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    private void showSong(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            songInformation sInfo = new songInformation();
            //set songs from DB
            sInfo.setSong0(ds.child("songs").getValue(songInformation.class).getSong0());
            sInfo.setSong1(ds.child("songs").getValue(songInformation.class).getSong1());
            sInfo.setSong2(ds.child("songs").getValue(songInformation.class).getSong2());
            sInfo.setSong3(ds.child("songs").getValue(songInformation.class).getSong3());
            sInfo.setSong4(ds.child("songs").getValue(songInformation.class).getSong4());
            sInfo.setSong5(ds.child("songs").getValue(songInformation.class).getSong5());
            sInfo.setSong6(ds.child("songs").getValue(songInformation.class).getSong6());
            sInfo.setSong7(ds.child("songs").getValue(songInformation.class).getSong7());
            sInfo.setSong8(ds.child("songs").getValue(songInformation.class).getSong8());
            sInfo.setSong9(ds.child("songs").getValue(songInformation.class).getSong9());
            sInfo.setSong10(ds.child("songs").getValue(songInformation.class).getSong10());
            sInfo.setSong11(ds.child("songs").getValue(songInformation.class).getSong11());
            sInfo.setSong12(ds.child("songs").getValue(songInformation.class).getSong12());
            sInfo.setSong13(ds.child("songs").getValue(songInformation.class).getSong13());
            sInfo.setSong14(ds.child("songs").getValue(songInformation.class).getSong14());
            sInfo.setSong15(ds.child("songs").getValue(songInformation.class).getSong15());
            sInfo.setSong16(ds.child("songs").getValue(songInformation.class).getSong16());
            sInfo.setSong17(ds.child("songs").getValue(songInformation.class).getSong17());
            sInfo.setSong18(ds.child("songs").getValue(songInformation.class).getSong18());
            sInfo.setSong19(ds.child("songs").getValue(songInformation.class).getSong19());
            sInfo.setSong20(ds.child("songs").getValue(songInformation.class).getSong20());
            sInfo.setSong21(ds.child("songs").getValue(songInformation.class).getSong21());



            //log info
            Log.d(TAG, "Show song: " + sInfo.getSong0());
            Log.d(TAG, "Show song: " + sInfo.getSong1());
            Log.d(TAG, "Show song: " + sInfo.getSong2());
            //mTextView.setText(sInfo.getSong0());

            final ArrayList<String> array = new ArrayList<>();
            if(sInfo.getSong0() == null){
            }else {
                array.add(sInfo.getSong0());
            }
            if(sInfo.getSong1() == null){
            }else {
                array.add(sInfo.getSong1());
            }
            if(sInfo.getSong2() == null){
            }else {
                array.add(sInfo.getSong2());
            }
            if(sInfo.getSong3() == null){
            }else {
                array.add(sInfo.getSong3());
            }
            if(sInfo.getSong4() == null){
            }else {
                array.add(sInfo.getSong4());
            }
            if(sInfo.getSong5() == null){
            }else {
                array.add(sInfo.getSong5());
            }
            if(sInfo.getSong6() == null){
            }else {
                array.add(sInfo.getSong6());
            }
            if(sInfo.getSong7() == null){
            }else {
                array.add(sInfo.getSong7());
            }
            if(sInfo.getSong8() == null){
            }else {
                array.add(sInfo.getSong8());
            }if(sInfo.getSong9() == null){
            }else {
                array.add(sInfo.getSong9());
            }if(sInfo.getSong10() == null){
            }else {
                array.add(sInfo.getSong10());
            }if(sInfo.getSong11() == null){
            }else {
                array.add(sInfo.getSong11());
            }
            if(sInfo.getSong12() == null){
            }else {
                array.add(sInfo.getSong12());
            }if(sInfo.getSong13() == null){
            }else {
                array.add(sInfo.getSong13());
            }
            if(sInfo.getSong14() == null){
            }else {
                array.add(sInfo.getSong14());
            }
            if(sInfo.getSong15() == null){
            }else {
                array.add(sInfo.getSong15());
            }
            if(sInfo.getSong16() == null){
            }else {
                array.add(sInfo.getSong16());
            }
            if(sInfo.getSong17() == null){
            }else {
                array.add(sInfo.getSong17());
            }
            if(sInfo.getSong18() == null){
            }else {
                array.add(sInfo.getSong18());
            }
            if(sInfo.getSong19() == null){
            }else {
                array.add(sInfo.getSong19());
            }
            if(sInfo.getSong20() == null){
            }else {
                array.add(sInfo.getSong20());
            }
            if(sInfo.getSong21() == null){
            }else {
                array.add(sInfo.getSong21());
            }

            playlistSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        plCheck = 1;
                    }
                    else{
                        plCheck = 0;
                    }
                }
            });
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(savedItems.this, "You clicked: " + array.get(position), Toast.LENGTH_SHORT).show();
                    if (plCheck == 1){
                        Uri webpage = Uri.parse("https://www.youtube.com/results?search_query=" + array.get(position)+"&sp=EgIQAw%253D%253D");
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        // Verify it resolves
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
                        boolean isIntentSafe = activities.size() > 0;


// Start an activity if it's safe
                        if (isIntentSafe) {
                            startActivity(webIntent);
                        }
                    } else {
                        Uri webpage = Uri.parse("https://www.youtube.com/results?search_query=" + array.get(position));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        // Verify it resolves
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
                        boolean isIntentSafe = activities.size() > 0;


// Start an activity if it's safe
                        if (isIntentSafe) {
                            startActivity(webIntent);
                        }
                    }

                }
            });



        }



    }



}

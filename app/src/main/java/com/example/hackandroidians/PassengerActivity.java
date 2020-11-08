package com.example.hackandroidians;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PassengerActivity extends AppCompatActivity {

    public Spinner spinner, spinner_source;
    public FirebaseFirestore firebaseFirestore;
    public ArrayList<String> stations;
    public ArrayList<LatLong> latLongs;
    public String Destination;
    public String Source;
    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        spinner = findViewById(R.id.spinner_passenger);
        spinner_source = findViewById(R.id.spinner_passenger_destination);
        firebaseFirestore = FirebaseFirestore.getInstance();
        button = findViewById(R.id.button);

        stations = new ArrayList<>();
        latLongs = new ArrayList<>();
        stations.add("Enter");
        Task<QuerySnapshot> documentSnapshotTask;
        documentSnapshotTask = firebaseFirestore.collection("stops").get();
        documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments())
                {
                    //getting the information about the stops
                    latLongs.add(new LatLong(documentSnapshot.get("Name").toString(), documentSnapshot.getDouble("Lat"), documentSnapshot.getDouble("Long")));
                    stations.add(documentSnapshot.get("Name").toString());
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stations);
        spinner.setAdapter(adapter);
        spinner_source.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Destination =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerActivity.this, WaitingActivity.class);
                intent.putExtra("Source", Source);
                intent.putExtra("Destination", Destination);
                startActivity(intent);
            }
        });


        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Source =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
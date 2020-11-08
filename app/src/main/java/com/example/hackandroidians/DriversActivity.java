package com.example.hackandroidians;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;
import java.util.ArrayList;
import java.util.List;

public class DriversActivity extends AppCompatActivity
{
    public String[] strings;
    public LatLng latLng;
    public FirebaseFirestore firebaseFirestore;
    public Spinner spinner, spinnerIn, spinnerDestination, sD, sE, sF;
    public List<String> stations;
    public List<DocumentSnapshot> stopList;
    public TextView textView;
    public String route;
    public CollectionReference collectionReference;
    public Button continue_1, passenger;
    public ArrayList<LatLong> latLongs;
    public ArrayList<SourceString> driverGivenAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);
        route = "";

        driverGivenAddress = new ArrayList<>();
        latLongs = new ArrayList<>();
        stopList = new ArrayList<DocumentSnapshot>();

        firebaseFirestore = FirebaseFirestore.getInstance();

        collectionReference = firebaseFirestore.collection("bus");
        stations = new ArrayList<>();
        textView = findViewById(R.id.text);
        Task<QuerySnapshot> documentSnapshotTask;
        stations.add("Stop");
        documentSnapshotTask = firebaseFirestore.collection("stops").get();
        documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                stopList = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot: stopList)
                {
                    //getting the information about the stops
                    latLongs.add(new LatLong(documentSnapshot.get("Name").toString(), documentSnapshot.getDouble("Lat"), documentSnapshot.getDouble("Long")));
                    stations.add(documentSnapshot.get("Name").toString());
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stations);

        spinner = findViewById(R.id.spinner);
        spinnerIn = findViewById(R.id.spinnerIn);
        continue_1 = findViewById(R.id.continue_1);
        sD = findViewById(R.id.spinnerDestinationD);
        sE = findViewById(R.id.spinnerDestinationE);
        sF = findViewById(R.id.spinnerDestinationF);
        sD.setAdapter(adapter);
        sE.setAdapter(adapter);
        sF.setAdapter(adapter);


        sE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        continue_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBusData();
                startActivity(new Intent(DriversActivity.this, LocationActivity.class));
            }
        });


        spinner.setAdapter(adapter);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        spinnerDestination.setAdapter(adapter);
        spinnerIn.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String In =  parent.getItemAtPosition(position).toString();
                driverGivenAddress.add(new SourceString(In, 0, 1, 0));
//                toast(In);
//                route = "";
                route = route.concat(In.substring(0, 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String In =  parent.getItemAtPosition(position).toString();
                toast(In);
                driverGivenAddress.add(new SourceString(In, 0, 0, 1));
                route = route.concat(In.substring(0, 1));
                toast(route);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String In =  parent.getItemAtPosition(position).toString();
                route = route.concat(In.substring(0, 1));
                driverGivenAddress.add(new SourceString(In, 1, 0, 0));
                toast(In);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void add(String In)
    {
        route = route.concat(In.substring(0, 1));
        driverGivenAddress.add(new SourceString(In, 0, 0, 0));
    }

    public void setBusData()
    {
        //variable to be updated at specific interval busLat
        BusLat busLat = new BusLat(route, 0 ,0);

        //setting bus latLong
        //route means bus name
        firebaseFirestore.collection("bus").document(route).set(busLat);
        ArrayList<LatLong> finalLatLong = new ArrayList<>();
        for(SourceString sourceString: driverGivenAddress)
        {
            String string = sourceString.Name;
            for(LatLong latLong: latLongs)
            {
                //finding latlong for drivers enter address
                if(string.equals(latLong.Name) == true)
                {
                    finalLatLong.add(latLong);
                    Log.d("LatLong", ""+latLong.lat+" "+latLong.Longitude);
                }
            }
        }
        Common.route = route;

        for(LatLong latLong: finalLatLong)
        {
            collectionReference.document(route).collection("Stops").document(latLong.Name).set(latLong);
        }
    }

    public void toast(String str)
    {
    //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
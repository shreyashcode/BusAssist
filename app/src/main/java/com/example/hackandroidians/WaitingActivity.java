package com.example.hackandroidians;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaitingActivity extends AppCompatActivity {

    public String Source;
    public String Destination;
    public FirebaseFirestore firebaseFirestore;
    public ArrayList<BusLat> busLats;
    public CollectionReference collectionReference;
    public List<DocumentSnapshot> stopList;
    public int flag_source = 0;
    public LatLong source, destination;
    public int flag_destination = 0;
    public ArrayList<String> finalBus;
    public Button button;
    public TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        button = findViewById(R.id.button2);
        textView = findViewById(R.id.time);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //stopList = new ArrayList<>();

        finalBus = new ArrayList<>();
        busLats = new ArrayList<>();
        Source = getIntent().getStringExtra("Source");
        Destination = getIntent().getStringExtra("Destination");
        Log.d("Source", Source+" "+Destination);

        firebaseFirestore.collection("stops").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> stopListNew = new ArrayList<>();
                //getting source destination latlong
                stopListNew = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot: stopListNew)
                {
                    if(documentSnapshot.get("Name").toString().equals(Source) == true)
                    {
                        source = new LatLong(documentSnapshot.get("Name").toString()
                                ,documentSnapshot.getDouble("Lat")
                                ,documentSnapshot.getDouble("Long"));

                        Log.d("SourceS", documentSnapshot.get("Name")+" "+documentSnapshot.getDouble("Lat")+" "+documentSnapshot.getDouble("Long"));
                    }
                    if(documentSnapshot.get("Name").toString().equals(Destination) == true)
                    {
                        destination = new LatLong(documentSnapshot.get("Name").toString()
                                    ,documentSnapshot.getDouble("Lat")
                                    ,documentSnapshot.getDouble("Long"));
                        Log.d("SourceD", documentSnapshot.get("Name")+""+documentSnapshot.getDouble("Lat")+" "+documentSnapshot.getDouble("Long"));
                    }
                }
            }
        });

        getFavourableBus();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDistance();
            }
        });


    }

    public void getDistance()
    {

       // float[] distance = new float[6];
        Log.d("Source", finalBus.size()+"data");
        for(String string: finalBus)
        {
            Log.d("Source", "loop"+string+"data");
            firebaseFirestore.collection("bus").document(string).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    //getting present location of bus i.e lat, long_

                    double lat = documentSnapshot.getDouble("Lat");
                    double long_ = documentSnapshot.getDouble("Longi");

                    Location locationA = new Location("LocationA");
                    locationA.setLatitude(lat);
                    locationA.setLongitude(long_);

                    Location locationB = new Location("LocationB");
                    locationB.setLatitude(source.lat);
                    locationB.setLongitude(source.Longitude);

                    Log.d("Distance ", source.lat+" "+source.Longitude);
                    float distance = locationA.distanceTo(locationB);

                    Log.d("Distance", "distance is"+ distance);
                    Log.d("Source", "Document "+lat+" "+long_);
                    double time = (distance/30000.0);
                    time = time*60.0;
                    int time1 = (int) time;
                    textView.setText("Wait time: "+time1+" mins");
                }
            });

        }
        Log.d("Source", "LOoper");
    }


    public void getFavourableBus()
    {
        collectionReference = firebaseFirestore.collection("bus");

        Task<QuerySnapshot> documentSnapshotTask;
        documentSnapshotTask = collectionReference.get();
        documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                //getting all documents i.e all buses

                stopList = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot: stopList)
                {
                    Log.d("Stations", "nameIs"+documentSnapshot.get("Name").toString());
                    BusLat busLat = new BusLat(documentSnapshot.get("Name").toString()
                            ,Double.parseDouble(documentSnapshot.get("Lat").toString())
                            ,Double.parseDouble(documentSnapshot.get("Longi").toString()));
                    busLats.add(busLat);
                }
                Log.d("stations", busLats.size()+"size");

                for(BusLat busLat: busLats)
                {
                    Log.d("stations", "buslat");
                    //getting bus details
                    collectionReference.document(busLat.Name).collection("Stops").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> stopList_1 = new ArrayList<>();
                                    stopList_1 = queryDocumentSnapshots.getDocuments();

                                    String newStop;

                                    for(DocumentSnapshot documentSnapshot: stopList_1)
                                    {
                                        Log.d("Stations", documentSnapshot.get("Name").toString()+"data");
                                newStop = documentSnapshot.get("Name").toString();
                                if(newStop.equals(Source) == true && flag_source == 0)
                                {
                                    flag_source = 1;
                                    finalBus.add(busLat.Name);
                                    Log.d("stations", "size_final"+finalBus.size());
                                    break;
                                }
//                                if(newStop.equals(Destination) == true)
//                                {
//                                    if(flag_source == 1)
//                                    {
//                                        finalBus.add(busLat.Name);
//                                    }
//                                }
                                    }
                                }
                            });
                }

            }
        });

        Log.d("stations", busLats.size()+"size");

    }
}
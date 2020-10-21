package com.kushpel.trackensuretest.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kushpel.trackensuretest.R;
import com.kushpel.trackensuretest.database.repository.StatisticsRepository;
import com.kushpel.trackensuretest.database.repository.StationRepository;
import com.kushpel.trackensuretest.models.Statistics;
import com.kushpel.trackensuretest.models.Station;
import com.kushpel.trackensuretest.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.kushpel.trackensuretest.utils.Constants.MAPVIEW_BUNDLE_KEY;
import static com.kushpel.trackensuretest.utils.Constants.MAP_ZOOM;
import static com.kushpel.trackensuretest.utils.Constants.TEST_LATITUDE;
import static com.kushpel.trackensuretest.utils.Constants.TEST_LONGITUDE;

public class RedactorMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private Context context;
    private EditText name;
    private Spinner typeOfFuel;
    private EditText quantity;
    private EditText cost;
    private Statistics statisticsToSave;
    private Station stationOld;
    private boolean isEdit;
    private StatisticsRepository statisticsRepository;
    private StationRepository stationRepository;
    private Toolbar toolbar;

    protected ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redactor_map);
        init();

        statisticsRepository = new StatisticsRepository();
        stationRepository = new StationRepository();
        isEdit = false;
        stationOld = null;

        getData();
        initGoogleMap(savedInstanceState);
        context = this;
    }


    private void getData(){
        try {
            stationOld = getIntent().getExtras().getParcelable(Constants.IS_EDIT_KEY);
            isEdit = true;
            name.setText(stationOld.getName());
            quantity.setText(String.valueOf(stationOld.getQuantity()));
            cost.setText(String.valueOf(stationOld.getPrice()));
            statisticsRepository.getByIdTask((int) stationOld.getStatisticsId()).observe(this,
                    new Observer<Statistics>() {
                        @Override
                        public void onChanged(Statistics statistics) {
                            statisticsToSave = statistics;
                        }
                    });
        } catch (Exception ignored) {}
    }

    public void init(){
        mapView = findViewById(R.id.mapView);
        name = findViewById(R.id.name);
        typeOfFuel = findViewById(R.id.type_of_fuel);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RedactorMapActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.modes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfFuel.setAdapter(myAdapter);
        quantity = findViewById(R.id.quantity);
        cost = findViewById(R.id.cost);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RedactorMapActivity.this.setTitle("");
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    private String getAddressFromCoordinates (Context context, double lat, double lng){
        String address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList.size() > 0)
                address = addressList.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(TEST_LATITUDE, TEST_LONGITUDE), MAP_ZOOM));
        statisticsRepository.getAllTask().observe(this, new Observer<List<Statistics>>() {
            @Override
            public void onChanged(List<Statistics> StatisticsList) {
                if (StatisticsList.size() > 0){
                    for (int i = 0; i < StatisticsList.size(); i++) {
                        Statistics Statistics = StatisticsList.get(i);
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Statistics.getLatitude(), Statistics.getLongitude()))
                                .title(Statistics.getName()));
                    }
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }


    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkmark_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.save: {
                if (statisticsToSave != null) {
                    if (!name.getText().toString().isEmpty() &&
                            !quantity.getText().toString().isEmpty() &&
                            !cost.getText().toString().isEmpty()) {
                        Station Station = new Station(
                                name.getText().toString(),
                                typeOfFuel.getSelectedItem().toString(),
                                Double.parseDouble(quantity.getText().toString()),
                                Double.parseDouble(cost.getText().toString()),
                                statisticsToSave.getId());
                        insertStationToDB(Station);
                        finish();
                    } else {
                        Toast.makeText(context, "Fill in all the fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Select gas station on the map", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    private void insertStationToDB(Station station){
        if (isEdit){
            station.setId(stationOld.getId());
            station.setSynced(false);
            stationRepository.updateTask(station);

        } else {
            stationRepository.insertTask(station);

        }
    }
    @Override
    public void onMapClick(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the gas station");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(Constants.POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String StatisticsAddress = getAddressFromCoordinates(context, latLng.latitude, latLng.longitude);
                if (StatisticsAddress != null) {
                    statisticsToSave = new Statistics(
                            String.valueOf(input.getText()),
                            StatisticsAddress,
                            latLng.latitude,
                            latLng.longitude);
                    insertStatisticsToDB(statisticsToSave);

                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(String.valueOf(input.getText())));
                }
            }
        });

        builder.setNegativeButton(Constants.NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void insertStatisticsToDB(Statistics statistics){
        statisticsRepository.insertTask(statistics);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        statisticsRepository.getByNameTask(marker.getTitle()).observe(this,
                new Observer<Statistics>() {
                    @Override
                    public void onChanged(Statistics statistics) {
                        statisticsToSave = statistics;
                    }
                });
        return false;
    }


}

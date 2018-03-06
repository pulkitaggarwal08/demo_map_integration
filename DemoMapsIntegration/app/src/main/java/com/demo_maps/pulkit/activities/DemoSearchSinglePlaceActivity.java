package com.demo_maps.pulkit.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo_maps.pulkit.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class DemoSearchSinglePlaceActivity extends AppCompatActivity implements View.OnClickListener
        , NavigationView.OnNavigationItemSelectedListener
        , OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private Toolbar toolbar;

    private TextView tv_locality, tv_latitude, tv_longitude, tv_snippet;
    private EditText editText_location;
    private String search_location;
    private View view;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    public NavigationView navigationView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mgoogleApiClient;
    private LocationRequest mlocationRequest;
    private Location mlocation;
    private Marker mMarker;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_single_place);

        findIds();
        init();
    }

    /*Find all ids here*/
    private void findIds() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        editText_location = (EditText) findViewById(R.id.editText_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        /*Navigation Drawer*/
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

    }

    MenuItem action_settings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        action_settings = menu.findItem(R.id.action_settings);
//
//        action_settings.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()) {
//            case R.id.action_settings:
        return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (mMap == null) {
            return true;
        }

        if (id == R.id.nav_your_places) {

            //Todo: Call all places
            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_your_timeline) {

            //Todo: Call all timelines
            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_traffic) {

            updateTraffic(item);

        } else if (id == R.id.nav_building) {

            updateBuildings(item);

        } else if (id == R.id.nav_indoor) {

            updateIndoor(item);

        } else if (id == R.id.nav_normal) {

            mMap.setMapType(MAP_TYPE_NORMAL);
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_hybrid) {

            mMap.setMapType(MAP_TYPE_HYBRID);
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_satellite) {

            mMap.setMapType(MAP_TYPE_SATELLITE);
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_terrian) {

            mMap.setMapType(MAP_TYPE_TERRAIN);
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_tips) {

            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_settings) {

            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_help) {

            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();

        } else if (id == R.id.nav_terms_services) {

            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*Update Traffic*/
    private void updateTraffic(MenuItem item) {

        if (mMap.isTrafficEnabled()) {
            mMap.setTrafficEnabled(false);
            item.setCheckable(false);
            item.setChecked(false);
        } else {
            mMap.setTrafficEnabled(true);
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    /*Update Building*/
    private void updateBuildings(MenuItem item) {

        if (mMap.isBuildingsEnabled()) {
            mMap.setBuildingsEnabled(false);
            item.setCheckable(false);
            item.setChecked(false);
        } else {
            mMap.setBuildingsEnabled(true);
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    /*Update Indoors*/
    private void updateIndoor(MenuItem item) {

        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
            item.setCheckable(false);
            item.setChecked(false);
        } else {
            mMap.setIndoorEnabled(true);
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        onStartAppPermission();

        if (mMap != null) {

            /*Drag Marker*/
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                }
            });

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    view = getLayoutInflater().inflate(R.layout.marker_custom_info_window, null);

                    tv_locality = view.findViewById(R.id.tv_locality);
                    tv_latitude = view.findViewById(R.id.tv_latitude);
                    tv_longitude = view.findViewById(R.id.tv_longitude);
                    tv_snippet = view.findViewById(R.id.tv_snippet);

                    LatLng latLng = marker.getPosition();

                    tv_locality.setText(marker.getTitle());
                    tv_latitude.setText("Latitude:" + latLng.latitude);
                    tv_longitude.setText("Longitude:" + latLng.longitude);
                    tv_snippet.setText(marker.getSnippet());

                    return view;
                }
            });
        }
    }

    private void onStartAppPermission() {
        /*check permissions*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private synchronized void buildGoogleApiClient() {
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        /*update location in every 1 sec.*/
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(1000);
        mlocationRequest.setFastestInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, mlocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mlocation = location;

       /*Place Current Location*/
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        /*Marker Options for marker*/
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMarker = mMap.addMarker(markerOptions);

        /*move map camera*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        /*stop location update*/
        if (mgoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mgoogleApiClient, this);
        }

        editText_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

    }

    private void performSearch() {

        editText_location.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText_location.getWindowToken(), 0);

        search_location = editText_location.getText().toString();
        if (!search_location.equalsIgnoreCase("")) {
            Geocoder geocoder = new Geocoder(this);
            try {

                List<Address> addressList = geocoder.getFromLocationName(search_location, 10);
                Address address = addressList.get(0);

                String locality = address.getLocality();
                String subLocality = address.getSubLocality();

                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                goToLocationZoom(latitude, longitude, 9.5f);

                setMarker(locality, latitude, longitude);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {

        LatLng latLng = new LatLng(latitude, longitude);

        /*Update Camera*/
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(cameraUpdate);
    }

    private void setMarker(String locality, double latitude, double longitude) {

        if (mMarker != null) {
//            removeLocation();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
//                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .position(new LatLng(latitude, longitude))
                .snippet("I am here");
        mMarker = mMap.addMarker(markerOptions);

        circle = drawCircle(new LatLng(latitude, longitude));
    }

    private Circle drawCircle(LatLng latLng) {

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(600)
                .fillColor(0x70A4C2FD)
                .strokeColor(Color.BLUE)
                .strokeWidth(1);

        return mMap.addCircle(circleOptions);
    }

    private void removeLocation() {

        mMarker.remove();
        mMarker = null;

        circle.remove();
        circle = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onStartAppPermission();

        } else {
            onStartAppPermission();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

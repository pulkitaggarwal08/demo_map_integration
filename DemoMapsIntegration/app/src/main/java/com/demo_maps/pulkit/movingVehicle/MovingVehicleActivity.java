package com.demo_maps.pulkit.movingVehicle;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo_maps.pulkit.R;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.demo_maps.pulkit.R.id.map;
import static com.demo_maps.pulkit.R.id.start;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MovingVehicleActivity extends AppCompatActivity implements View.OnClickListener
        , NavigationView.OnNavigationItemSelectedListener
        , OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener
        , RoutingListener {

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

    private static Marker marker;
    private Circle circle;

    private LatLng start_latLng, end_latLng;
    double end_latitude, end_longitude, start_latitude, start_longitude;

    private static final int[] COLORS = new int[]{R.color.light_blue, R.color.medium_grey, R.color.green,
            R.color.colorAccent, R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        findIds();
        init();
    }

    /*Find all ids here*/
    private void findIds() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        editText_location = (EditText) findViewById(R.id.editText_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
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
        start_latitude = location.getLatitude();
        start_longitude = location.getLongitude();
        start_latLng = new LatLng(start_latitude, start_longitude);

        /*Marker Options for marker*/
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(start_latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMarker = mMap.addMarker(markerOptions);

        /*move map camera*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start_latLng));
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

                end_latitude = address.getLatitude();
                end_longitude = address.getLongitude();

                goToLocationZoom(end_latitude, end_longitude, 9.5f);

                setMarker(locality, end_latitude, end_longitude);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void goToLocationZoom(double end_latitude, double end_longitude, float zoom) {

        end_latLng = new LatLng(30.7096285, 76.68813419999999);

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(end_latitude, end_longitude), new LatLng(start_latitude, start_longitude))
                .build();
        routing.execute();

    }

    private void setMarker(String locality, double end_latitude, double end_longitude) {

        if (mMarker != null) {
//            removeLocation();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
//                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .position(new LatLng(end_latitude, end_longitude))
                .snippet("I am here");
        mMarker = mMap.addMarker(markerOptions);

        circle = drawCircle(new LatLng(end_latitude, end_longitude));
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

    /*Routes start from here*/
    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    private Handler handler;
    private int index, next;

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        List<Polyline> polylines = new ArrayList<>();
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(start_latitude, start_longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        Polyline polyline = null;

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions().geodesic(true);
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }

        /*Add polylines list from last to first in LatLng list,
        otherwise animation will start from end point to start point,
        because polyines are adding in the list from start point to end point, and list give data using LIFO Algo.
        */

        List<LatLng> latLngList = new ArrayList<>();
        if (polylines != null) {
            for (int j = polylines.get(0).getPoints().size(); j > 0; j--) {
                int k = j - 1;
                latLngList.add(polylines.get(0).getPoints().get(k));
            }
        }

        /*for animation*/
        Bitmap bitmap_car = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_car);
        setAnimation(mMap, latLngList, Bitmap.createScaledBitmap(bitmap_car, 50, 50, false));

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(start_latitude, start_longitude));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(new LatLng(end_latitude, end_longitude));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }

    public void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {

        if (marker != null) {
            marker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .position(directionPoint.get(0))
                .flat(true);

        marker = mMap.addMarker(markerOptions);

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 15));

        animateMarker(myMap, marker, directionPoint, false);
    }


    private void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 300000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                if (i < directionPoint.size()) {

                    Location prev_location = new Location("");

                    try {

                        prev_location.setLatitude(directionPoint.get(i).latitude);
                        prev_location.setLongitude(directionPoint.get(i).longitude);

                        LatLng end_location = directionPoint.get(i);

//                        marker.setPosition(end_location);
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setRotation(getBearing(directionPoint.get(0), end_location));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(end_location)
                                .zoom(15.5f)
                                .build()));
                        i++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (t < 1.0) {
                    // Post again 1ssec later.
                    handler.postDelayed(this, 1000);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


}

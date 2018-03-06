package com.demo_maps.pulkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.demo_maps.pulkit.activities.DemoLayersActivity;
import com.demo_maps.pulkit.activities.DemoSearchSinglePlaceActivity;
import com.demo_maps.pulkit.activities.GetUserLocationActivity;
import com.demo_maps.pulkit.directions.DirectionsActivity;
import com.demo_maps.pulkit.distance.GetDistanceDurationActivity;
import com.demo_maps.pulkit.movingVehicle.MovingVehicleActivity;
import com.demo_maps.pulkit.nearbyplaces.GetNearPlacesActivity;

public class MainActivity extends AppCompatActivity {

    TextView tv_layers, tv_user_location, tv_search_single_place, tv_distance_duration, tv_near_places,
            tv_directions, tv_moving_vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findIds();
        init();
    }

    private void findIds() {

        tv_layers = (TextView) findViewById(R.id.tv_layers);
        tv_user_location = (TextView) findViewById(R.id.tv_user_location);
        tv_search_single_place = (TextView) findViewById(R.id.tv_search_single_place);
        tv_distance_duration = (TextView) findViewById(R.id.tv_distance_duration);
        tv_near_places = (TextView) findViewById(R.id.tv_near_places);
        tv_directions = (TextView) findViewById(R.id.tv_directions);
        tv_moving_vehicle = (TextView) findViewById(R.id.tv_moving_vehicle);

    }

    private void init() {

        tv_layers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DemoLayersActivity.class);
                startActivity(intent);
            }
        });

        tv_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetUserLocationActivity.class);
                startActivity(intent);
            }
        });

        tv_search_single_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DemoSearchSinglePlaceActivity.class);
                startActivity(intent);
            }
        });

        tv_distance_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetDistanceDurationActivity.class);
                startActivity(intent);
            }
        });

        tv_near_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetNearPlacesActivity.class);
                startActivity(intent);
            }
        });

        tv_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
                startActivity(intent);
            }
        });

        tv_moving_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MovingVehicleActivity.class);
                startActivity(intent);
            }
        });

    }

}

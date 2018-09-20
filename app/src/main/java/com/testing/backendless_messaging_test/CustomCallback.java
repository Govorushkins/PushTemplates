package com.testing.backendless_messaging_test;

import com.backendless.geo.geofence.IGeofenceCallback;
import com.testing.backendless_messaging_test.entities.FenceLogger;
import com.testing.backendless_messaging_test.entities.FenceLoggerBuilder;

/**
 * Created by who on 4/22/15.
 */
public class CustomCallback implements IGeofenceCallback {

    @Override
    public void geoPointEntered(String geofenceName, String geofenceId, double latitude, double longitude) {

        FenceLogger fenceLogger = new FenceLoggerBuilder()
                .fenceName(geofenceName)
                .fenceId( geofenceId )
                .latitude( latitude )
                .longitude( longitude )
                .ruleName( "onEnter" ).build();

        fenceLogger.save();
    }

    @Override
    public void geoPointStayed(String geofenceName, String geofenceId, double latitude, double longitude) {
        FenceLogger fenceLogger = new FenceLoggerBuilder()
                .fenceName(geofenceName)
                .fenceId( geofenceId )
                .latitude( latitude )
                .longitude( longitude )
                .ruleName( "onStay" ).build();

        fenceLogger.save();
    }

    @Override
    public void geoPointExited(String geofenceName, String geofenceId, double latitude, double longitude) {
        FenceLogger fenceLogger = new FenceLoggerBuilder()
                .fenceName(geofenceName)
                .fenceId( geofenceId )
                .latitude( latitude )
                .longitude( longitude )
                .ruleName( "onExit" ).build();

        fenceLogger.save();
    }
}

package com.testing.backendless_messaging_test.entities;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by who on 4/24/15.
 */
public class FenceLogger {

    private String fenceName;
    private String fenceId;
    private Double latitude;
    private Double longitude;
    private String ruleName;

    public FenceLogger() {
        //
    }

    FenceLogger( FenceLoggerBuilder fenceLoggerBuilder )
    {
        this.fenceName = fenceLoggerBuilder.getFenceName();
        this.fenceId = fenceLoggerBuilder.getFenceId();
        this.latitude = fenceLoggerBuilder.getLatitude();
        this.longitude = fenceLoggerBuilder.getLongitude();
        this.ruleName = fenceLoggerBuilder.getRuleName();
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void save()
    {
        Backendless.Data.save( this, new AsyncCallback<FenceLogger>() {
            @Override
            public void handleResponse(FenceLogger response) {
                System.out.println( "FenceLogger saved" );
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                System.out.println( "FenceLogger failed" );
            }
        });
    }
}

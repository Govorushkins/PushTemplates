package com.testing.backendless_messaging_test.entities;

/**
 * Created by who on 4/24/15.
 */
public class FenceLoggerBuilder
{
    private String fenceName;
    private String fenceId;
    private Double latitude;
    private Double longitude;
    private String ruleName;

    public FenceLoggerBuilder fenceName( final String fenceName )
    {
        this.fenceName = fenceName;
        return this;
    }

    public FenceLoggerBuilder fenceId( final String fenceId )
    {
        this.fenceId = fenceId;
        return this;
    }

    public FenceLoggerBuilder latitude( final Double latitude )
    {
        this.latitude = latitude;
        return this;
    }

    public FenceLoggerBuilder longitude( final Double longitude )
    {
        this.longitude = longitude;
        return this;
    }

    public FenceLoggerBuilder ruleName( final String ruleName )
    {
        this.ruleName = ruleName;
        return this;
    }

    public String getFenceName() {
        return fenceName;
    }

    public String getFenceId() {
        return fenceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getRuleName() {
        return ruleName;
    }

    public FenceLogger build()
    {
        return new FenceLogger( this );
    }
}

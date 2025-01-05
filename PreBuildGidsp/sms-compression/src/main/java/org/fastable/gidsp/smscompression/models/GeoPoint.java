package org.fastable.gidsp.smscompression.models;

import java.util.Objects;

public class GeoPoint
{
    private float latitude;

    private float longitude;

    public GeoPoint( float latitude, float longitude )
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude( float latitude )
    {
        this.latitude = latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude( float longitude )
    {
        this.longitude = longitude;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        GeoPoint gp = (GeoPoint) o;

        return Objects.equals( latitude, gp.latitude ) && Objects.equals( longitude, gp.longitude );
    }
}

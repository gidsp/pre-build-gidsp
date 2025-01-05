package org.fastable.gidsp.smscompression.models;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.utils.IdUtil;

public class Uid
{
    private String uid;

    private int hash;

    private MetadataType type;

    public Uid( String uid, MetadataType type )
    {
        this.uid = uid;
        this.type = type;
    }

    public Uid( String uid, int hash, MetadataType type )
    {
        this.uid = uid;
        this.hash = hash;
        this.type = type;
    }

    public int getHash()
    {
        return hash;
    }

    public String getUid()
    {
        return uid;
    }

    public MetadataType getType()
    {
        return type;
    }

    public String getHashAsBase64()
    {
        return ("#" + IdUtil.hashAsBase64( this ));
    }

    @Override
    public String toString()
    {
        if ( uid == null )
            return getHashAsBase64();
        return uid;
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
        Uid u = (Uid) o;
        return uid.equals( u.uid ) && (type == u.type);
    }

    @Override
    public int hashCode()
    {
        return uid.hashCode();
    }

}

package org.fastable.gidsp.smscompression;

import java.util.ArrayList;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.models.Uid;
import org.junit.Assert;
import org.junit.Test;

public class TestErrors
{

    @Test
    public void testPrintSmsResponse()
    {
        Uid ouid = new Uid( "ooooooooooo", MetadataType.ORGANISATION_UNIT );
        Uid programid = new Uid( "ppppppppppp", MetadataType.PROGRAM );
        String errorMsg = SmsResponse.OU_NOTIN_PROGRAM.set( ouid, programid ).toString();
        String expectedMsg = "302:ooooooooooo,ppppppppppp:Organisation unit [ooooooooooo] is not assigned to program [ppppppppppp]";
        Assert.assertEquals( expectedMsg, errorMsg );
    }

    @Test
    public void testPrintSmsResponseList()
    {
        Uid ouid = new Uid( "ooooooooooo", MetadataType.ORGANISATION_UNIT );
        Uid programid = new Uid( "ppppppppppp", MetadataType.PROGRAM );
        ArrayList<Object> uidList = new ArrayList<>();
        uidList.add( ouid );
        uidList.add( programid );
        String errorMsg = SmsResponse.OU_NOTIN_PROGRAM.setList( uidList ).toString();
        String expectedMsg = "302:ooooooooooo,ppppppppppp:Organisation unit [ooooooooooo] is not assigned to program [ppppppppppp]";
        Assert.assertEquals( expectedMsg, errorMsg );
    }

}

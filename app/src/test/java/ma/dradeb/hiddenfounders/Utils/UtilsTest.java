package ma.dradeb.hiddenfounders.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Youness on 10/07/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {
    Utils u ;

    @Before
    public void create()
    {
        u = mock(Utils.class);
        when(u.fbDateToSimpleFormat("2010-07-29T12:03:50+0000")).thenReturn("29-07-2010");
    }




    @Test
    public void fbDateToSimpleFormat() throws Exception {

        Assert.assertEquals(u.fbDateToSimpleFormat("2010-07-29T12:03:50+0000"),"29-07-2010");
//            assertSame(,"29-07-2010");
    }

}
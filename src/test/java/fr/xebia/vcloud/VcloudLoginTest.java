package fr.xebia.vcloud;

import fr.xebia.vcloud.answer.VcloudAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 */
@RunWith(PowerMockRunner.class)
public class VcloudLoginTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {
        //Vcloud vcloud = PowerMock.createMock(Vcloud.class);
        VcloudAnswer vcloud = new VcloudAnswer();
        vcloud.login("url","username","password");
        Assert.assertNotNull(vcloud);
    }
}

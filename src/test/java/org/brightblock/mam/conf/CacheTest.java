package org.brightblock.mam.conf;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.brightblock.mam.services.webrtc.TokenModel;
import org.brightblock.mam.services.webrtc.WebrtcService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.opentok.Session;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    @Autowired
    private WebrtcService webrtcService;

    @Test
    public void getHello() throws Exception {
		Session session = webrtcService.getSession("1");
		TokenModel tm1 = webrtcService.getToken(session, "1", "harry");
    		assertEquals(tm1.getToken(), tm1.getToken());
    		TokenModel tm2 = webrtcService.getToken(session, "1", "harry");
        assertNotEquals(tm2.getToken(), tm1.getToken());
        TokenModel tm3 = webrtcService.getToken(session, "1", "harry");
        assertEquals(tm3.getToken(), tm2.getToken());
    }
}

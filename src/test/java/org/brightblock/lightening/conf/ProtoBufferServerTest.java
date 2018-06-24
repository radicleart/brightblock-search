package org.brightblock.lightening.conf;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.brightblock.temp.proto.BrightblockProto.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProtoBufferServerTest {

	@LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void courseAccessRequest() {
        ResponseEntity<Course> course = restTemplate.getForEntity("http://localhost:" + port + "/courses/1", Course.class);
        assertResponse(course.toString());
    }
    
    private void assertResponse(String response) {
		assertThat(response, containsString("id"));
		assertThat(response, containsString("course_name"));
		assertThat(response, containsString("REST with Spring"));
		assertThat(response, containsString("student"));
		assertThat(response, containsString("first_name"));
		assertThat(response, containsString("last_name"));
		assertThat(response, containsString("email"));
		assertThat(response, containsString("john.flow@gmail.com"));
		assertThat(response, containsString("pete.fletcher@gmail.com"));
		assertThat(response, containsString("janet.barnet@gmail.com"));
		assertThat(response, containsString("phone"));
		assertThat(response, containsString("number"));
		assertThat(response, containsString("type"));
    	}
}

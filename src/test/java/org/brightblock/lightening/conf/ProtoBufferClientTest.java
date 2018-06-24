package org.brightblock.lightening.conf;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.brightblock.temp.proto.BrightblockProto.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.googlecode.protobuf.format.JsonFormat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProtoBufferClientTest {

	@LocalServerPort
    private int port;

    @Test
    public void whenUsingHttpClient_thenSucceed() throws IOException {
        InputStream responseStream = executeHttpRequest("http://localhost:" + port + "/courses/1");
        String jsonOutput = convertProtobufMessageStreamToJsonString(responseStream);
        assertResponse(jsonOutput);
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
    
    private InputStream executeHttpRequest(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(request);
        return httpResponse.getEntity().getContent();
    }
    
    private String convertProtobufMessageStreamToJsonString(InputStream protobufStream) throws IOException {
        JsonFormat jsonFormat = new JsonFormat();
        Course course = Course.parseFrom(protobufStream);
        return jsonFormat.printToString(course);
    }

}

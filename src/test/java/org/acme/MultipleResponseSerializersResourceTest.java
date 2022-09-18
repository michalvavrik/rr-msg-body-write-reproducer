package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.acme.MultipleResponseSerializersResource.APPLY_RESPONSE_SERIALIZER_PARAM_FLAG;
import static org.acme.MultipleResponseSerializersResource.MULTIPLE_RESPONSE_SERIALIZERS_PATH;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MultipleResponseSerializersResourceTest {

    @Test
    public void testMediaTypePassedToMessageBodyWriter() {
        // Accepted Media Type must be passed to 'MessageBodyWriter'
        // 'MessageBodyWriter' then returns passed Media Type for a verification
        assertAcceptedMediaTypeEqualsResponseBody(APPLICATION_JSON);
        assertAcceptedMediaTypeEqualsResponseBody(TEXT_HTML);
        assertAcceptedMediaTypeEqualsResponseBody(TEXT_PLAIN);
        assertAcceptedMediaTypeEqualsResponseBody(APPLICATION_OCTET_STREAM);
    }

    private void assertAcceptedMediaTypeEqualsResponseBody(String acceptedMediaType) {
            given()
                .accept(acceptedMediaType)
                .queryParam(APPLY_RESPONSE_SERIALIZER_PARAM_FLAG, Boolean.TRUE)
                .log().all().filter(new ResponseLoggingFilter())
                .get(MULTIPLE_RESPONSE_SERIALIZERS_PATH)
                .then()
                .body(Matchers.is(acceptedMediaType));
    }
}
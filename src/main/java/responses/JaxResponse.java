package responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;

public class JaxResponse {
    public static Response checkObjectResponse(ObjectResponse response) throws JsonProcessingException {
        if(response.getObject() != null) {
            return Response.status(response.getCode()).entity(new ObjectMapper().writeValueAsString(response.getObject())).build();
        }

        return Response.status(response.getCode()).entity(response.getMessage()).build();
    }
}

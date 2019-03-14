package responses;

import javax.ws.rs.core.Response;

public class JaxResponse {
    public static Response checkObjectResponse(ObjectResponse response) {
        if(response.getObject() != null) {
            return Response.status(response.getCode()).entity(response.getObject()).build();
        }

        return Response.status(response.getCode()).entity(response.getMessage()).build();
    }
}

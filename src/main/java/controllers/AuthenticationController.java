package controllers;

import authentication.TokenProvider;
import domain.User;
import dtos.users.*;
import org.modelmapper.ModelMapper;
import responses.HttpStatusCodes;
import responses.JaxResponse;
import responses.ObjectResponse;
import service.UserService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/auth")
public class AuthenticationController {
    @Inject
    private UserService userService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(LoginRequestObject loginViewModel) {
        try {
            ObjectResponse<User> response = userService.login(loginViewModel.getUsername(), loginViewModel.getPassword());

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ObjectResponse<String> tokenGeneration = TokenProvider.generate(Integer.toString(response.getObject().getId()), TokenProvider.ISSUER, response.getObject().getEmail(), TokenProvider.TIME_TO_LIVE);

            if(tokenGeneration.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(tokenGeneration);
            }

            TokenDto token = new TokenDto();
            token.setToken(tokenGeneration.getObject());

            ModelMapper mapper = new ModelMapper();
            UserCleanDto userDto = mapper.map(response.getObject(), UserCleanDto.class);

            token.setUser(userDto);

            return Response.status(response.getCode()).entity(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response create(CreateUserRequestObject request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setBiography(request.getBiography());
            user.setWebsite(request.getWebsite());
            user.setLongitude(request.getLongitude());
            user.setLatitude(request.getLatitude());

            ObjectResponse<User> response = userService.create(user);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(response.getObject(), UserDto.class);

            return Response.status(response.getCode()).entity(userDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.LoginViewModel;
import controllers.viewModels.UserViewModel;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

@Stateless
@Path("/auth")
public class AuthenticationController {
    @Inject
    private UserService userService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(LoginViewModel loginViewModel) {
        try {
            User user = userService.login(loginViewModel.getUsername(), loginViewModel.getPassword());
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(user)).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response create(UserViewModel request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setBiography(request.getBiography());
            user.setWebsite(request.getWebsite());
            user.setLongitude(request.getLongitude());
            user.setLatitude(request.getLatitude());
            userService.create(user);
            return Response.status(Response.Status.CREATED).entity(new ObjectMapper().writeValueAsString(user)).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (CreationFailedException | NoSuchAlgorithmException | JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (NameNotUniqueException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}

/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package controller;

import controller.requests.UserRegistrationRequest;
import model.User;
import service.UserService;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getAllUsers(@Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        return userService.getUsers();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response create(UserRegistrationRequest request) {
        User user = new User(request.getUsername(), request.getPassword(), request.getBiography(), request.getLocationLongitude(), request.getLocationLatitude(), request.getWebsite());

        try {
            userService.create(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }

        User created = userService.getUserByUsername(user.getUsername());

        if(created != null) {
            return Response.status(Response.Status.CREATED).entity(user).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong").build();
    }
}

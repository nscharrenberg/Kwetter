package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.FollowViewModel;
import controllers.viewModels.UserRoleViewModel;
import controllers.viewModels.UserViewModel;
import domain.Role;
import domain.User;
import exceptions.*;
import exceptions.NotFoundException;
import responses.JaxResponse;
import responses.ObjectResponse;
import service.RoleService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
@Path("/users")
public class UserController {
    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        try {
            ObjectResponse<List<User>> response = userService.all();
            return JaxResponse.checkObjectResponse(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            ObjectResponse<User> response = userService.getById(id);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/username/{username}")
    public Response getByUsername(@PathParam("username") String username) {
        try {
            ObjectResponse<User> response = userService.getByUsername(username);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email/{email}")
    public Response getByEmail(@PathParam("email") String email) {
        try {
            ObjectResponse<User> response = userService.getByEmail(email);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, UserViewModel request) {
        try {
            ObjectResponse<User> response = userService.getById(id);

            if(response.getObject() == null) {
                return JaxResponse.checkObjectResponse(response);
            }

            response.getObject().setUsername(request.getUsername() != null ? request.getUsername() : response.getObject().getUsername());
            response.getObject().setEmail(request.getEmail() != null ? request.getEmail() : response.getObject().getEmail());
            response.getObject().setBiography(request.getBiography() != null ? request.getBiography() : response.getObject().getBiography());
            response.getObject().setWebsite(request.getWebsite() != null ? request.getWebsite() : response.getObject().getWebsite());
            response.getObject().setLongitude(request.getLongitude() != null ? request.getLongitude() : response.getObject().getLatitude());
            response.getObject().setLatitude(request.getLatitude() != null ? request.getLatitude() : response.getObject().getLatitude());

            ObjectResponse<User> result = userService.update(response.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            ObjectResponse<User> response = userService.getById(id);

            if(response.getObject() == null) {
                return JaxResponse.checkObjectResponse(response);
            }

            ObjectResponse<User> result = userService.delete(response.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/role")
    public Response changeRole(@PathParam("id") int id, UserRoleViewModel request) {
        try {
            if(id != request.getUserId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());
            ObjectResponse<Role> getRoleByIdResponse = roleService.getById(request.getRoleId());

            if(getUserByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserByIdResponse);
            }

            if(getRoleByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getRoleByIdResponse);
            }

            ObjectResponse<User> result = userService.changeRole(getUserByIdResponse.getObject(), getRoleByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/follow")
    public Response follow(@PathParam("id") int id, FollowViewModel request) {
        try {
            if(id != request.getUserToFollowId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserToFollowByIdResponse = userService.getById(request.getUserToFollowId());
            ObjectResponse<User> getFollowerByIdResponse = userService.getById(request.getUserId());

            if(getUserToFollowByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserToFollowByIdResponse);
            }

            if(getFollowerByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getFollowerByIdResponse);
            }

            ObjectResponse<User> result = userService.follow(getFollowerByIdResponse.getObject(), getUserToFollowByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/unfollow")
    public Response unfollow(@PathParam("id") int id, FollowViewModel request) {
        try {
            if(id != request.getUserToFollowId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserToFollowByIdResponse = userService.getById(request.getUserToFollowId());
            ObjectResponse<User> getFollowerByIdResponse = userService.getById(request.getUserId());

            if(getUserToFollowByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserToFollowByIdResponse);
            }

            if(getFollowerByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getFollowerByIdResponse);
            }

            ObjectResponse<User> result = userService.unfollow(getFollowerByIdResponse.getObject(), getUserToFollowByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

package controllers;

import controllers.viewModels.FollowViewModel;
import controllers.viewModels.RoleViewModel;
import controllers.viewModels.UserRoleViewModel;
import controllers.viewModels.UserViewModel;
import domain.Role;
import domain.User;
import exceptions.ActionForbiddenException;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
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
    public List<User> all() {
        return userService.all();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UserViewModel request) {
        try {
            User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getBiography(), request.getWebsite(), request.getLongitude(), request.getLatitude());
            userService.create(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (CreationFailedException | NoSuchAlgorithmException e) {
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            User user = userService.getById(id);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/username/{username}")
    public Response getByUsername(@PathParam("username") String username) {
        try {
            User user = userService.getByUsername(username);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email/{email}")
    public Response getByEmail(@PathParam("email") String email) {
        try {
            User user = userService.getByEmail(email);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, UserViewModel request) {
        try {
            User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getBiography(), request.getWebsite(), request.getLongitude(), request.getLatitude());
            user.setId(id);
            userService.update(user);
            return Response.status(Response.Status.OK).entity(request).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (NameNotUniqueException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            User user = userService.getById(id);
            userService.delete(user);
            return Response.status(Response.Status.OK).entity("User " + user.getUsername() + " has been deleted").build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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

            User user = userService.getById(request.getUserId());
            Role role = roleService.getById(request.getRoleId());

            userService.changeRole(user, role);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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

            User userToFollow = userService.getById(request.getUserToFollowId());
            User follower = userService.getById(request.getUserId());

            userService.follow(follower, userToFollow);
            return Response.status(Response.Status.OK).entity(userToFollow).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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

            User userToUnFollow = userService.getById(request.getUserToFollowId());
            User follower = userService.getById(request.getUserId());

            userService.unfollow(follower, userToUnFollow);
            return Response.status(Response.Status.OK).entity(userToUnFollow).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


}
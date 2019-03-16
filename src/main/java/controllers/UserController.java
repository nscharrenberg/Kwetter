package controllers;

import domain.Role;
import domain.User;
import dtos.users.EditUserRequestObject;
import dtos.users.FollowRequestObject;
import dtos.users.RoleRequestObject;
import dtos.users.UserDto;
import org.modelmapper.ModelMapper;
import responses.ObjectResponse;
import service.RoleService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto[] userDto = mapper.map(response.getObject(), UserDto[].class);

            return Response.status(response.getCode()).entity(userDto).build();
        } catch (Exception e) {
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(response.getObject(), UserDto.class);

            return Response.status(response.getCode()).entity(userDto).build();
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(response.getObject(), UserDto.class);

            return Response.status(response.getCode()).entity(userDto).build();
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(response.getObject(), UserDto.class);

            return Response.status(response.getCode()).entity(userDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, EditUserRequestObject request) {
        try {
            ObjectResponse<User> response = userService.getById(id);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            response.getObject().setUsername(request.getUsername() != null ? request.getUsername() : response.getObject().getUsername());
            response.getObject().setEmail(request.getEmail() != null ? request.getEmail() : response.getObject().getEmail());
            response.getObject().setBiography(request.getBiography() != null ? request.getBiography() : response.getObject().getBiography());
            response.getObject().setWebsite(request.getWebsite() != null ? request.getWebsite() : response.getObject().getWebsite());
            response.getObject().setLongitude(request.getLongitude() != null ? request.getLongitude() : response.getObject().getLatitude());
            response.getObject().setLatitude(request.getLatitude() != null ? request.getLatitude() : response.getObject().getLatitude());

            ObjectResponse<User> result = userService.update(response.getObject());

            if(result.getObject() == null) {
                return Response.status(result.getCode()).entity(result.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(result.getObject(), UserDto.class);

            return Response.status(result.getCode()).entity(userDto).build();
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
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ObjectResponse<User> result = userService.delete(response.getObject());

            if(result.getObject() == null) {
                return Response.status(result.getCode()).entity(result.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            UserDto userDto = mapper.map(result.getObject(), UserDto.class);

            return Response.status(result.getCode()).entity(userDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/role")
    public Response changeRole(@PathParam("id") int id, RoleRequestObject request) {
        try {
            if(id != request.getUserId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            ObjectResponse<Role> getRoleByIdResponse = roleService.getById(request.getRoleId());

            if(getRoleByIdResponse.getObject() == null) {
                return Response.status(getRoleByIdResponse.getCode()).entity(getRoleByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> response = userService.changeRole(getUserByIdResponse.getObject(), getRoleByIdResponse.getObject());

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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/follow")
    public Response follow(@PathParam("id") int id, FollowRequestObject request) {
        try {
            if(id != request.getToFollowId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserToFollowByIdResponse = userService.getById(request.getToFollowId());

            if(getUserToFollowByIdResponse.getObject() == null) {
                return Response.status(getUserToFollowByIdResponse.getCode()).entity(getUserToFollowByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> getFollowerByIdResponse = userService.getById(request.getUserId());

            if(getFollowerByIdResponse.getObject() == null) {
                return Response.status(getFollowerByIdResponse.getCode()).entity(getFollowerByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> response = userService.follow(getFollowerByIdResponse.getObject(), getUserToFollowByIdResponse.getObject());

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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/unfollow")
    public Response unfollow(@PathParam("id") int id, FollowRequestObject request) {
        try {
            if(id != request.getToFollowId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserToFollowByIdResponse = userService.getById(request.getToFollowId());

            if(getUserToFollowByIdResponse.getObject() == null) {
                return Response.status(getUserToFollowByIdResponse.getCode()).entity(getUserToFollowByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> getFollowerByIdResponse = userService.getById(request.getUserId());

            if(getFollowerByIdResponse.getObject() == null) {
                return Response.status(getFollowerByIdResponse.getCode()).entity(getFollowerByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> response = userService.unfollow(getFollowerByIdResponse.getObject(), getUserToFollowByIdResponse.getObject());

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

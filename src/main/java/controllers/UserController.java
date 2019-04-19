package controllers;

import authentication.AuthenticationProvider;
import authentication.PermissionEnum;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import domain.Role;
import domain.User;
import dtos.users.EditUserRequestObject;
import dtos.users.FollowRequestObject;
import dtos.users.RoleRequestObject;
import dtos.users.UserDto;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.modelmapper.ModelMapper;
import responses.HttpStatusCodes;
import responses.JaxResponse;
import responses.ObjectResponse;
import service.RoleService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/users")
public class UserController {
    @Inject
    private UserService userService;

    @Inject
    private AuthenticationProvider authenticationProvider;

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
    public Response update(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, EditUserRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.UPDATE_USERS.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

            ObjectResponse<User> response = userService.getById(id);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            User user = new User();
            user.setId(request.getId());
            user.setUsername(request.getUsername() != null ? request.getUsername() : response.getObject().getUsername());
            user.setEmail(request.getEmail() != null ? request.getEmail() : response.getObject().getEmail());
            user.setBiography(request.getBiography() != null ? request.getBiography() : response.getObject().getBiography());
            user.setWebsite(request.getWebsite() != null ? request.getWebsite() : response.getObject().getWebsite());
            user.setLongitude(request.getLongitude() != null ? request.getLongitude() : response.getObject().getLatitude());
            user.setLatitude(request.getLatitude() != null ? request.getLatitude() : response.getObject().getLatitude());
            user.setFirstname(request.getFirstname() != null ? request.getFirstname() : response.getObject().getFirstname());
            user.setLastname(request.getLastname() != null ? request.getLastname() : response.getObject().getLastname());
            user.setAvatar(request.getAvatar() != null ? request.getAvatar() : response.getObject().getAvatar());
            user.setPassword(request.getPassword() != null && !request.getPassword().replaceAll(" ", "").isEmpty() ? request.getPassword() : response.getObject().getPassword());
            user.setRole(response.getObject().getRole());

            ObjectResponse<User> result = userService.update(user);

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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/follow")
    public Response follow(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, FollowRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.FOLLOW_USER.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }


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
    public Response unfollow(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, FollowRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.UNFOLLOW_USER.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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

    @GET
    @Path("/randomUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response randomUsers(@HeaderParam("Authorization") String authentication, @QueryParam("limitTo") int limitTo) {
        try {
            ObjectResponse<List<User>> response = userService.all();

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            List<UserDto> userDto = new ArrayList<>(Arrays.asList(mapper.map(response.getObject(), UserDto[].class)));

            ObjectResponse<User> loggedIn = authenticationProvider.authenticate(authentication);

            if(loggedIn.getCode() == HttpStatusCodes.OK) {
                UserDto user = Iterables.tryFind(userDto, input -> loggedIn.getObject().getId() == input.getId()).orNull();

                if(user != null) {
                    userDto.remove(user);
                }
            }

            Collections.shuffle(userDto);

            userDto.stream().limit(limitTo).collect(Collectors.toList());

            return Response.status(response.getCode()).entity(userDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

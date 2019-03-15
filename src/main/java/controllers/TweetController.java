package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.LikeViewModel;
import controllers.viewModels.TweetViewModel;
import controllers.viewModels.UserViewModel;
import domain.Role;
import domain.Tweet;
import domain.User;
import exceptions.*;
import exceptions.NotFoundException;
import responses.JaxResponse;
import responses.ObjectResponse;
import service.TweetService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
@Path("/tweets")
public class TweetController {
    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        try {
            ObjectResponse<List<Tweet>> response = tweetService.all();
            return JaxResponse.checkObjectResponse(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TweetViewModel request) {
        try {
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getAuthor());

            if(getUserByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserByIdResponse);
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserByIdResponse.getObject());

            ObjectResponse<Tweet> response = tweetService.create(tweet);

            return JaxResponse.checkObjectResponse(response);
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
            ObjectResponse<Tweet> response = tweetService.getById(id);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/name/{name}")
    public Response getByUsername(@PathParam("name") String username) {
        try {
            ObjectResponse<List<Tweet>> response = tweetService.getByAuthorName(username);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/{id}")
    public Response getByAuthorId(@PathParam("id") int id) {
        try {
            ObjectResponse<List<Tweet>> response = tweetService.getByAuthorId(id);

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
    public Response update(@PathParam("id") int id, TweetViewModel request) {
        try {
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getAuthor());

            if(getUserByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserByIdResponse);
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserByIdResponse.getObject());
            tweet.setId(id);

            ObjectResponse<Tweet> response = tweetService.update(tweet);

            return JaxResponse.checkObjectResponse(response);
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
            ObjectResponse<Tweet> response = tweetService.getById(id);

            if(response.getObject() == null) {
                return JaxResponse.checkObjectResponse(response);
            }

            ObjectResponse<Tweet> result = tweetService.delete(response.getObject());

            return JaxResponse.checkObjectResponse(result);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/like")
    public Response like(@PathParam("id") int id, LikeViewModel request) {
        try {
            ObjectResponse<Tweet> getTweetByIdResponse = tweetService.getById(request.getTweetId());
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getTweetByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getTweetByIdResponse);
            }

            if(getUserByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserByIdResponse);
            }

            ObjectResponse<Tweet> result = tweetService.like(getTweetByIdResponse.getObject(), getUserByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/unlike")
    public Response unlike(@PathParam("id") int id, LikeViewModel request) {
        try {
            ObjectResponse<Tweet> getTweetByIdResponse = tweetService.getById(request.getTweetId());
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getTweetByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getTweetByIdResponse);
            }

            if(getUserByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getUserByIdResponse);
            }

            ObjectResponse<Tweet> result = tweetService.unlike(getTweetByIdResponse.getObject(), getUserByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

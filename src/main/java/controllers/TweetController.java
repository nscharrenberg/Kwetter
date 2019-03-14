package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.LikeViewModel;
import controllers.viewModels.TweetViewModel;
import controllers.viewModels.UserViewModel;
import domain.Tweet;
import domain.User;
import exceptions.*;
import exceptions.NotFoundException;
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
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweetService.all())).build();
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
            ObjectResponse<User> getUserById = userService.getById(request.getAuthor());

            if(getUserById.getObject() == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User could not be found").build();
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserById.getObject());
            tweetService.create(tweet);
            return Response.status(Response.Status.CREATED).entity(new ObjectMapper().writeValueAsString(tweet)).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (CreationFailedException | JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }  catch (NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Tweet tweet = tweetService.getById(id);
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweet)).build();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/name/{name}")
    public Response getByUsername(@PathParam("name") String username) {
        try {
            List<Tweet> tweets = tweetService.getByAuthorName(username);
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweets)).build();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/{id}")
    public Response getByAuthorId(@PathParam("id") int id) {
        try {
            List<Tweet> tweets = tweetService.getByAuthorId(id);
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweets)).build();
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

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, TweetViewModel request) {
        try {
            ObjectResponse<User> getUserById = userService.getById(request.getAuthor());

            if(getUserById.getObject() == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User could not be found").build();
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserById.getObject());
            tweet.setId(id);
            tweetService.update(tweet);
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweet)).build();
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            Tweet tweet = tweetService.getById(id);
            tweetService.delete(tweet);
            return Response.status(Response.Status.OK).entity("Tweet by " + tweet.getAuthor().getUsername() + " has been deleted").build();
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
    @Path("/{id}/like")
    public Response like(@PathParam("id") int id, LikeViewModel request) {
        try {
            if(id != request.getTweetId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            Tweet tweet = tweetService.getById(request.getTweetId());

            tweetService.like(tweet, getUserByIdResponse.getObject());
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweet)).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ActionForbiddenException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (JsonProcessingException e) {
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
            if(id != request.getTweetId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            Tweet tweet = tweetService.getById(request.getTweetId());

            tweetService.unlike(tweet, getUserByIdResponse.getObject());
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(tweet)).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ActionForbiddenException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

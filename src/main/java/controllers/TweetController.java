package controllers;

import controllers.viewModels.LikeViewModel;
import controllers.viewModels.TweetViewModel;
import controllers.viewModels.UserViewModel;
import domain.Tweet;
import domain.User;
import exceptions.*;
import exceptions.NotFoundException;
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
    public List<Tweet> all() {
        return tweetService.all();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TweetViewModel request) {
        try {
            Tweet tweet = new Tweet(request.getMessage(), userService.getById(request.getAuthor()));
            tweetService.create(tweet);
            return Response.status(Response.Status.CREATED).entity(tweet).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (CreationFailedException e) {
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
            return Response.status(Response.Status.OK).entity(tweet).build();
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
    @Path("/author/name/{name}")
    public Response getByUsername(@PathParam("name") String username) {
        try {
            List<Tweet> tweets = tweetService.getByAuthorName(username);
            return Response.status(Response.Status.OK).entity(tweets).build();
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
    @Path("/author/{id}")
    public Response getByUsername(@PathParam("id") int id) {
        try {
            List<Tweet> tweets = tweetService.getByAuthorId(id);
            return Response.status(Response.Status.OK).entity(tweets).build();
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
    public Response update(@PathParam("id") int id, TweetViewModel request) {
        try {
            Tweet tweet = new Tweet(request.getMessage(), userService.getById(request.getAuthor()));
            tweet.setId(id);
            tweetService.update(tweet);
            return Response.status(Response.Status.OK).entity(request).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
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
    public Response follow(@PathParam("id") int id, LikeViewModel request) {
        try {
            if(id != request.getTweetId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            Tweet tweet = tweetService.getById(request.getTweetId());
            User user = userService.getById(request.getUserId());

            tweetService.like(tweet, user);
            return Response.status(Response.Status.OK).entity(tweet).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ActionForbiddenException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/unlike")
    public Response unfollow(@PathParam("id") int id, LikeViewModel request) {
        try {
            if(id != request.getTweetId()) {
                return Response.status(Response.Status.FORBIDDEN).entity("URL id is not the same as the body").build();
            }

            Tweet tweet = tweetService.getById(request.getTweetId());
            User user = userService.getById(request.getUserId());

            tweetService.unlike(tweet, user);
            return Response.status(Response.Status.OK).entity(tweet).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (exceptions.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ActionForbiddenException e) {
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }
}

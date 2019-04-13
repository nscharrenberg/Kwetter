package controllers;

import domain.Tweet;
import domain.User;
import dtos.tweets.CreateTweetRequestObject;
import dtos.tweets.EditTweetRequestObject;
import dtos.tweets.LikeRequestObject;
import dtos.tweets.TweetDto;
import org.modelmapper.ModelMapper;
import responses.ObjectResponse;
import service.TweetService;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/tweets")
public class TweetController {
    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@QueryParam("resultPerPage") int resultPerPage, @QueryParam("pageNumber") int pageNumber) {
        try {
            ObjectResponse<List<Tweet>> response = null;

            if(resultPerPage > 0 && pageNumber > 0) {
                // Make sure the 1st param is pageNumber and 2nd is pageSize
                response = tweetService.all(pageNumber, resultPerPage);
            } else {
                response = tweetService.all();
            }

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto[] tweetDto = mapper.map(response.getObject(), TweetDto[].class);

            return Response.status(response.getCode()).entity(tweetDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateTweetRequestObject request) {
        try {
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getAuthor());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserByIdResponse.getObject());

            ObjectResponse<Tweet> response = tweetService.create(tweet);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(response.getObject(), TweetDto.class);

            return Response.status(response.getCode()).entity(tweetDto).build();
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(response.getObject(), TweetDto.class);

            return Response.status(response.getCode()).entity(tweetDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/name/{name}")
    public Response getByUsername(@PathParam("name") String username, @QueryParam("resultPerPage") int resultPerPage, @QueryParam("pageNumber") int pageNumber) {
        try {
            ObjectResponse<List<Tweet>> response = null;

            if(resultPerPage > 0 && pageNumber > 0) {
                // Make sure the 1st param is pageNumber and 2nd is pageSize
                response = tweetService.getByAuthorName(username, pageNumber, resultPerPage);
            } else {
                response = tweetService.getByAuthorName(username);
            }

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto[] tweetDto = mapper.map(response.getObject(), TweetDto[].class);

            return Response.status(response.getCode()).entity(tweetDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/author/{id}")
    public Response getByAuthorId(@PathParam("id") int id, @QueryParam("resultPerPage") int resultPerPage, @QueryParam("pageNumber") int pageNumber) {
        try {
            ObjectResponse<List<Tweet>> response = null;

            if(resultPerPage > 0 && pageNumber > 0) {
                // Make sure the 1st param is pageNumber and 2nd is pageSize
                response = tweetService.getByAuthorId(id, pageNumber, resultPerPage);
            } else {
                response = tweetService.getByAuthorId(id);
            }

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto[] tweetDto = mapper.map(response.getObject(), TweetDto[].class);

            return Response.status(response.getCode()).entity(tweetDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/timeline/{id}")
    public Response getTimeline(@PathParam("id") int id, @QueryParam("resultPerPage") int resultPerPage, @QueryParam("pageNumber") int pageNumber) {
        try {
            ObjectResponse<List<Tweet>> response = null;

            if(resultPerPage > 0 && pageNumber > 0) {
                // Make sure the 1st param is pageNumber and 2nd is pageSize
                response = tweetService.getTimeLine(id, pageNumber, resultPerPage);
            } else {
                response = tweetService.getTimeLine(id);
            }

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto[] tweetDto = mapper.map(response.getObject(), TweetDto[].class);

            return Response.status(response.getCode()).entity(tweetDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, EditTweetRequestObject request) {
        try {
            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getAuthor());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            Tweet tweet = new Tweet();
            tweet.setMessage(request.getMessage());
            tweet.setAuthor(getUserByIdResponse.getObject());
            tweet.setId(id);

            ObjectResponse<Tweet> response = tweetService.update(tweet);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(response.getObject(), TweetDto.class);

            return Response.status(response.getCode()).entity(tweetDto).build();
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
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ObjectResponse<Tweet> result = tweetService.delete(response.getObject());

            if(result.getObject() == null) {
                return Response.status(result.getCode()).entity(result.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(result.getObject(), TweetDto.class);

            return Response.status(result.getCode()).entity(tweetDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/like")
    public Response like(@PathParam("id") int id, LikeRequestObject request) {
        try {
            ObjectResponse<Tweet> getTweetByIdResponse = tweetService.getById(request.getTweetId());

            if(getTweetByIdResponse.getObject() == null) {
                return Response.status(getTweetByIdResponse.getCode()).entity(getTweetByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            ObjectResponse<Tweet> result = tweetService.like(getTweetByIdResponse.getObject(), getUserByIdResponse.getObject());

            if(result.getObject() == null) {
                return Response.status(result.getCode()).entity(result.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(result.getObject(), TweetDto.class);

            return Response.status(result.getCode()).entity(tweetDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/unlike")
    public Response unlike(@PathParam("id") int id, LikeRequestObject request) {
        try {
            ObjectResponse<Tweet> getTweetByIdResponse = tweetService.getById(request.getTweetId());

            if(getTweetByIdResponse.getObject() == null) {
                return Response.status(getTweetByIdResponse.getCode()).entity(getTweetByIdResponse.getMessage()).build();
            }

            ObjectResponse<User> getUserByIdResponse = userService.getById(request.getUserId());

            if(getUserByIdResponse.getObject() == null) {
                return Response.status(getUserByIdResponse.getCode()).entity(getUserByIdResponse.getMessage()).build();
            }

            ObjectResponse<Tweet> result = tweetService.unlike(getTweetByIdResponse.getObject(), getUserByIdResponse.getObject());

            if(result.getObject() == null) {
                return Response.status(result.getCode()).entity(result.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            TweetDto tweetDto = mapper.map(result.getObject(), TweetDto.class);

            return Response.status(result.getCode()).entity(tweetDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

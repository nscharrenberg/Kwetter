package com.nscharrenberg.kwetter.controllers;

import com.nscharrenberg.kwetter.authentication.AuthenticationProvider;
import com.nscharrenberg.kwetter.authentication.PermissionEnum;
import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.dtos.tweets.CreateTweetRequestObject;
import com.nscharrenberg.kwetter.dtos.tweets.EditTweetRequestObject;
import com.nscharrenberg.kwetter.dtos.tweets.LikeRequestObject;
import com.nscharrenberg.kwetter.dtos.tweets.TweetDto;
import org.modelmapper.ModelMapper;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.JaxResponse;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.TweetService;
import com.nscharrenberg.kwetter.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
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

    @Inject
    private AuthenticationProvider authenticationProvider;

    @Context
    private UriInfo uriInfo;

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

            List<TweetDto> tweetDtos = new ArrayList<>(Arrays.asList(tweetDto));

            Response.ResponseBuilder responseBuilder = Response.status(response.getCode());

            if(tweetDtos.size() > 0) {
                tweetDtos.forEach(t -> {
                    Link selfLink = Link.fromUri(uriInfo.getBaseUriBuilder().path(TweetController.class).path(TweetController.class, "getById").build(t.getId())).rel("self").build();
                    Link authorLink = Link.fromUri(uriInfo.getBaseUriBuilder().path(UserController.class, "getByUsername").build(t.getId())).rel("author").build();
                    t.getLinks().add(selfLink);
                    t.getLinks().add(authorLink);

                    responseBuilder.links(selfLink, authorLink);
                });
            }

            return responseBuilder.entity(tweetDtos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization") String authentication, CreateTweetRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.CREATE_TWEET.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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
    public Response update(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, EditTweetRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.UPDATE_TWEET.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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
    public Response delete(@HeaderParam("Authorization") String authentication, @PathParam("id") int id) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.DELETE_TWEET.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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
    public Response like(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, LikeRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.LIKE_TWEET.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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
    public Response unlike(@HeaderParam("Authorization") String authentication, @PathParam("id") int id, LikeRequestObject request) {
        try {
            ObjectResponse<User> loggedIn = authenticationProvider.authenticationWithPermission(authentication, PermissionEnum.UNLIKE_TWEET.getValue());

            if(loggedIn.getCode() != HttpStatusCodes.OK) {
                return JaxResponse.checkObjectResponse(loggedIn);
            }

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

package com.nscharrenberg.kwetter.controllers;

import com.nscharrenberg.kwetter.domain.Hashtag;
import com.nscharrenberg.kwetter.dtos.hashtags.HashtagDto;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.HashtagService;
import org.modelmapper.ModelMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/hashtags")
public class HashtagController {
    @Inject
    private HashtagService hashtagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        try {
            ObjectResponse<List<Hashtag>> response;

            response = hashtagService.all();

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            HashtagDto[] hashtagDto = mapper.map(response.getObject(), HashtagDto[].class);

            return Response.status(response.getCode()).entity(hashtagDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/top/{number}")
    public Response getTopX(@PathParam("number") int number) {
        try {
            ObjectResponse<List<Hashtag>> response;
            response = hashtagService.findTopx(number);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            HashtagDto[] hashtagDto = mapper.map(response.getObject(), HashtagDto[].class);

            return Response.status(response.getCode()).entity(hashtagDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/name/{name}")
    public Response getByName(@PathParam("name") String name) {
        try {
            ObjectResponse<Hashtag> response;
            response = hashtagService.getByName(name);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            HashtagDto hashtagDto = mapper.map(response.getObject(), HashtagDto.class);

            return Response.status(response.getCode()).entity(hashtagDto).build();
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
            ObjectResponse<Hashtag> response;
            response = hashtagService.getById(id);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            HashtagDto hashtagDto = mapper.map(response.getObject(), HashtagDto.class);

            return Response.status(response.getCode()).entity(hashtagDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

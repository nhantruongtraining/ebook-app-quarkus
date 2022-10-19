package com.axonactive.rest.api;

import com.axonactive.entity.Ebook;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ebooks")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class EbookResource {

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Ebook[] getAllEbooks () {
        return em.createNamedQuery("Ebooks.findAll", Ebook.class).getResultList().toArray(new Ebook[0]);
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEbook(Ebook ebook) {
        em.persist(ebook);
        return Response.ok(ebook).status(201).build();
    }


}

package com.axonactive.rest.api;

import com.axonactive.entity.EbookEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ebooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EbookEntityResource {

    @Inject
    EntityManager em;

    @GET
    public EbookEntity[] getAllEbooks () {
        return em.createNamedQuery("Ebooks.findAll", EbookEntity.class).getResultList().toArray(new EbookEntity[0]);
    }
    @GET
    @Path("/{id}")
    public EbookEntity getSingleEbook(Integer id) {
        EbookEntity ebook = em.find(EbookEntity.class, id);
        if (ebook == null) {
            throw new WebApplicationException("Ebook with id of " + id + "does not exist.", 404);
        }
        return ebook;
    }

    @POST
    @Transactional
    public Response createEbook(EbookEntity ebook) {
        if (ebook.getId() != null) {
            throw new WebApplicationException("ID was invalidly set on request.", 422);
        }
        em.persist(ebook);
        return Response.ok(ebook).status(201).build();
    }

    @PUT
    @Transactional
    public EbookEntity updateEbook(EbookEntity ebook) {
        EbookEntity updatedEbook = em.find(EbookEntity.class, ebook.getId());
        if (updatedEbook == null) {
            throw new WebApplicationException("Ebook with id of " + ebook.getId() + " does not exist.", 404);
        }
        updatedEbook.setTitle(ebook.getTitle());
        updatedEbook.setAuthor(ebook.getAuthor());
        updatedEbook.setPublishedYear(ebook.getPublishedYear());

        return updatedEbook;
    }

    @DELETE
    @Transactional
    public Response deleteEbook(EbookEntity ebook) {
        EbookEntity ebookToBeDeleted = em.find(EbookEntity.class, ebook.getId());
        em.remove(ebookToBeDeleted);
        return Response.status(204).build();
    }
}

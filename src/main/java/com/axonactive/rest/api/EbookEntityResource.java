package com.axonactive.rest.api;

import com.axonactive.entity.EbookEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/ebooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ebook REST endpoint")
public class EbookEntityResource {

    @Inject
    EntityManager em;

    @GET
    @Operation(summary = "Retrieve all Ebooks")
    @APIResponse(responseCode = "200", description = "List of all ebooks displayed")
    public EbookEntity[] getAllEbooks () {
        return em.createNamedQuery("Ebooks.findAll", EbookEntity.class).getResultList().toArray(new EbookEntity[0]);
    }
    @GET
    @Path("/{id}")
    @Operation(summary = "Find an Ebook by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ebook found by ID"),
            @APIResponse(responseCode = "404", description = "Ebook does not exist")
    })
    public EbookEntity getSingleEbook(Integer id) {
        EbookEntity ebook = em.find(EbookEntity.class, id);
        if (ebook == null) {
            throw new WebApplicationException("Ebook with id of " + id + " does not exist.", 404);
        }
        return ebook;
    }

    @POST
    @Transactional
    @Operation(summary = "Add a new Ebook")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Ebook created successfully"),
            @APIResponse(responseCode = "422", description = "ID was invalidly set on request")
    })
    public Response createEbook(EbookEntity ebook) {
        if (ebook.getId() != null) {
            throw new WebApplicationException("ID was invalidly set on request.", 422);
        }
        em.persist(ebook);
        return Response.ok(ebook).status(201).build();
    }

    @PUT
    @Transactional
    @Operation(summary = "Update an Ebook")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ebook updated successfully"),
            @APIResponse(responseCode = "422", description = "Ebook ID was not set on request"),
            @APIResponse(responseCode = "404", description = "Ebook does not exist")
    })
    public EbookEntity updateEbook(EbookEntity ebook) {
        if (ebook.getId() == null) {
            throw new WebApplicationException("Ebook ID was not set on request.", 422);
        }
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
    @Operation(summary = "Delete an Ebook")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Ebook deleted successfully"),
            @APIResponse(responseCode = "404", description = "Ebook does not exist")
    })
    public Response deleteEbook(EbookEntity ebook) {
        EbookEntity ebookToBeDeleted = em.find(EbookEntity.class, ebook.getId());
        if (ebookToBeDeleted == null) {
            throw new WebApplicationException("Ebook with id of " + ebook.getId() + " does not exist.", 404);
        }
        em.remove(ebookToBeDeleted);
        return Response.status(204).build();
    }
}

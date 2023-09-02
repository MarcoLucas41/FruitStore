package org.acme.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.acme.DTOs.UserDTO;
import org.acme.entities.User;
import org.jboss.logging.Logger;

import java.util.List;
@ApplicationScoped
@Path("users")
@Produces("application/json")
@Consumes("application/json")

public class UserResource
{

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

    @Inject
    EntityManager entityManager;

    @GET
    public List<User> getUsers() {
        return entityManager.createNamedQuery("Users.findAll", User.class)
                .getResultList();
    }


    @GET
    @Path("{id}")
    public User getSingle(Integer id) {
        User entity = entityManager.find(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("org.acme.entities.User with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response createUser(UserDTO user) {
        User userEntity = new User(user.getName(),user.getAge(), user.getEmail());

        entityManager.persist(userEntity);
        return Response.ok(user).status(201).build();
    }




    @PUT
    @Path("{id}")
    @Transactional
    public User update(Integer id, User user) {
        if (user.getName() == null) {
            throw new WebApplicationException("org.acme.entities.Fruit Name was not set on request.", 422);
        }
        if (user.getAge() == 0) {
            throw new WebApplicationException("org.acme.entities.Fruit Age was not set on request.", 422);
        }
        if (user.getEmail() == null) {
            throw new WebApplicationException("org.acme.entities.Fruit Email was not set on request.", 422);
        }

        User entity = entityManager.find(User.class, id);

        if (entity == null) {
            throw new WebApplicationException("org.acme.entities.Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setName(user.getName());
        entity.setAge(user.getAge());
        entity.setEmail(user.getEmail());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        User entity = entityManager.getReference(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("org.acme.entities.User with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}



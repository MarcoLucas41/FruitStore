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

import org.acme.DTOs.AccountDTO;
import org.acme.entities.Account;
import org.jboss.logging.Logger;

import java.util.List;
@ApplicationScoped
@Path("accounts")
@Produces("application/json")
@Consumes("application/json")

public class AccountResource
{

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

    @Inject
    EntityManager entityManager;

    @GET
    public List<Account> getUsers() {
        return entityManager.createNamedQuery("Accounts.findAll", Account.class)
                .getResultList();
    }


    @GET
    @Path("{id}")
    public Account getSingle(Integer id) {
        Account entity = entityManager.find(Account.class, id);
        if (entity == null) {
            throw new WebApplicationException("org.acme.entities.User with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response createUser(AccountDTO user) {
        Account accountEntity = new Account(user.getName(),user.getAge(), user.getEmail());

        entityManager.persist(accountEntity);
        return Response.ok(user).status(201).build();
    }




    @PUT
    @Path("{id}")
    @Transactional
    public Account update(Integer id, Account account) {
        if (account.getName() == null) {
            throw new WebApplicationException("org.acme.entities.Fruit Name was not set on request.", 422);
        }
        if (account.getAge() == 0) {
            throw new WebApplicationException("org.acme.entities.Fruit Age was not set on request.", 422);
        }
        if (account.getEmail() == null) {
            throw new WebApplicationException("org.acme.entities.Fruit Email was not set on request.", 422);
        }

        Account entity = entityManager.find(Account.class, id);

        if (entity == null) {
            throw new WebApplicationException("org.acme.entities.Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setName(account.getName());
        entity.setAge(account.getAge());
        entity.setEmail(account.getEmail());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        Account entity = entityManager.getReference(Account.class, id);
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



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Person;
import exceptions.GenericExceptionMapper;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import exceptions.PersonNotFoundExceptionMapper;
import facades.PersonFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        try {
            List<PersonDTO> personDTOs = FACADE.getAllPersons();
            return GSON.toJson(personDTOs);
        } catch (PersonNotFoundException ex) {
            return ex.getMessage();
        }
    }

    @Path("alldto")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsDTO() {
        List<Person> persons = FACADE.getAllPersonsAssDTOAll();
        return GSON.toJson(new PersonsDTO(persons));
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) {
        try {
            PersonDTO personDTO = FACADE.getPerson(id);
            return GSON.toJson(personDTO);
        } catch (PersonNotFoundException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Using PersonNotFoundExceptionMapper!!!!
     *
     * @param id
     * @return
     */
    @Path("id2/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonById1(@PathParam("id") int id) {
        try {
            PersonDTO personDTO = FACADE.getPerson(id);
            return Response.ok(personDTO).build();
        } catch (PersonNotFoundException ex) {
            return new PersonNotFoundExceptionMapper().toResponse(ex);
        }
    }

    /**
     * Using GenericExceptionMapper!!!!
     *
     * @param id
     * @return
     */
    @Path("id3/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonById2(@PathParam("id") int id) {
        try {
            PersonDTO personDTO = FACADE.getPerson(id);
            return Response.ok(personDTO).build();
        } catch (PersonNotFoundException ex) {
            return new GenericExceptionMapper().toResponse(ex);
        }
    }
    

    /**
     * ALTERNATIV BRUGE EXCEPTIONS PÅ!!!!
     *
     * @param id
     * @return
     */
    @Path("id4/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonById4(@PathParam("id") int id) throws Exception {
        PersonDTO personDTO = FACADE.getPersonWithException(id);
        return Response.ok(personDTO).build();
    }
    
    /**
     * ALTERNATIV BRUGE EXCEPTIONS PÅ!!!!
     *
     * @param id
     * @return
     */
    @Path("id5/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersonById5(@PathParam("id") int id) throws Exception{
        PersonDTO personDTO = FACADE.getPerson1(id);
        return Response.ok(personDTO).build();
    }

    /**
     * Post produce
     *
     * @return
     */
    @Path("post-test1")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String postDemo() {
        return "{\"msg\":\"POST is working\"}";
    }

    /**
     * Post produce
     *
     * @param person
     * @return
     */
    @Path("post-dbtest")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePerson(String person) {
        try {
            //System.out.println(person);
            PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
            personDTO = FACADE.addPerson(personDTO.getFirstName(), personDTO.getLastName(), personDTO.getPhone());
            return Response.ok(personDTO).build();
        } catch (PersonNotFoundException ex) {
            return Response.ok(ex.getMessage()).build();
        } catch (MissingInputException ex) {
            return Response.ok(ex.getMessage()).build();
        }
    }

    /**
     * Put
     *
     * @param person
     * @return
     * @throws exceptions.PersonNotUpdatedException
     */
    @Path("change/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPerson(@PathParam("id") int id, String person) {
        try {
            //System.out.println(person);
            PersonDTO personDTOEditInfo = GSON.fromJson(person, PersonDTO.class); //manual conversion
            Person personToEdit = new Person(id, personDTOEditInfo.getFirstName(), personDTOEditInfo.getLastName(), personDTOEditInfo.getPhone());
            PersonDTO personDTO = new PersonDTO(personToEdit);
            personDTO = FACADE.editPerson(personDTO);
            return Response.ok(personDTO).build();
        } catch (PersonNotFoundException ex) {
            return Response.ok(ex.getMessage()).build();
        } catch (MissingInputException ex) {
            return Response.ok(ex.getMessage()).build();
        }
    }

    /**
     * Delete
     */
    @Path("delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCar(@PathParam("id") int id) {
        try {
            PersonDTO personDTO = FACADE.deletePerson(id);
            System.out.println(personDTO);
            return "{\"status\": \"removed\"}";
        } catch (PersonNotFoundException ex) {
            return ex.getMessage();
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import exceptions.PersonNotDeletedException;
import exceptions.PersonNotFoundException;
import exceptions.PersonNotUpdatedException;
import facades.FacadeExample;
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
    public String getAllCars() {
        List<PersonDTO> personDTOs = FACADE.getAllPersons();
        return GSON.toJson(personDTOs);
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCarById(@PathParam("id") int id) {
        try {
            PersonDTO personDTO = FACADE.getPerson(id);
            return GSON.toJson(personDTO);
        } catch (PersonNotFoundException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"status\": \"Person not found :(\"}";
        }  
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
     * @param person
     * @return 
     */
    @Path("post-dbtest")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePerson(String person) {
        //System.out.println(person);
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
        personDTO = FACADE.addPerson(personDTO.getFirstName(), personDTO.getLastName(), personDTO.getPhone());
        return Response.ok(personDTO).build();
    }
    
    /**
     * Put
     * @param person
     * @return 
     * @throws exceptions.PersonNotUpdatedException
     */
    @Path("change/{id}")
    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    public Response editPerson(String person) {
        //System.out.println(person);
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
        try {
            personDTO = FACADE.editPerson(personDTO);
            return Response.ok(personDTO).build();
        } catch (PersonNotUpdatedException ex) {
            //Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.notModified().build();
        }
        
    }
    
    
    /**
     * Delete
     */
    @Path("delete/{id}")
    @DELETE
    public String deleteCar(@PathParam("id") int id){
        try {
            PersonDTO personDTO = FACADE.deletePerson(id);
            System.out.println(personDTO);
            return "{\"status\": \"removed\"}";
        } catch (PersonNotDeletedException ex) {
            //Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"status\": \"Not removed :(\"}";
        }catch (PersonNotFoundException ex) {
            //Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"status\": \"Person not found :(\"}";
        }
    }
}

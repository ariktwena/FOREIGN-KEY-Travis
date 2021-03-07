/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.Person;
import entities.RenameMe;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class PersonFacadeTest {
    
    private static EntityManagerFactory EMF;
    private static PersonFacade FACADE;
    Person person1;
    Person person2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       EMF = EMF_Creator.createEntityManagerFactoryForTest();
       FACADE = PersonFacade.getPersonFacade(EMF);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        person1 = new Person("First 1", "Last 1", "11111111"); 
        person2 = new Person("First 2", "Last 2", "22222222"); 
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(person1);
            em.persist(person2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testDBSize() {
        try {
            assertEquals(2, FACADE.getAllPersons().size(), "Expects two rows in the database");
        } catch (PersonNotFoundException ex) {
            //Logger.getLogger(PersonFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testEditInfo(){
        person1.setFirst_name("changed");
        PersonDTO person1DTO = new PersonDTO(person1);
        try {
            person1DTO = FACADE.editPerson(person1DTO);
        } catch (PersonNotFoundException | MissingInputException ex) {
            //Logger.getLogger(PersonFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Logger.getLogger(PersonFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        assertEquals("changed", person1DTO.getFirstName());
    }
    
    @Test
    public void testDelete(){
        PersonDTO person1DTO = new PersonDTO(person1);
        try {
            person1DTO = FACADE.deletePerson(person1DTO.getId());
        } catch (PersonNotFoundException ex) {
            //Logger.getLogger(PersonFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            assertEquals(1, FACADE.getAllPersons().size(), "Expects 1 rows in the database");
        } catch (PersonNotFoundException ex) {
            //Logger.getLogger(PersonFacadeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("First 1", person1DTO.getFirstName(), "First name of deltede: 'First 1'");
    }
    
}

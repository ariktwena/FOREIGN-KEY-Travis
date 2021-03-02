/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import exceptions.PersonNotFoundExceptionMapper;
import interfaces.IPersonFacade;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Tweny
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) throws PersonNotFoundException, MissingInputException {
        EntityManager em = emf.createEntityManager();
        if(fName == null || lName ==null){
            throw new MissingInputException("{\"code\": 500, \"message\": \"First Name and/or Last Name is missing\"}");
        }
        try {
            Person person = new Person(fName, lName, phone);
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } catch (RuntimeException ex) {
            throw new PersonNotFoundException("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}");
        } finally {
            em.close();
        }  
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        int rowCount;
        EntityManager em = emf.createEntityManager();
        try {
            PersonDTO personDTO = new PersonDTO(em.find(Person.class, id));
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Person person WHERE person.id = :id").setParameter("id", personDTO.getId());
            rowCount = query.executeUpdate();
            em.getTransaction().commit();
            if (rowCount == 1) {
                return personDTO;
            } else {
                return null;
            }
        } catch (NullPointerException ex) {
            throw new PersonNotFoundException("{\"code\": 404, \"message\": \"Could not delete, provided id does not exist\"}");
        } catch (RuntimeException ex) {
            throw new PersonNotFoundException("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}");
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException, RuntimeException {
        EntityManager em = emf.createEntityManager();
        try {
            PersonDTO personDTO = new PersonDTO(em.find(Person.class, id));
            return personDTO;
        } catch (NullPointerException ex) {
            throw new PersonNotFoundException("{\"code\": 404, \"message\": \"No person with provided id found\"}");
        } catch (RuntimeException ex) {
            throw new PersonNotFoundException("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}");
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getAllPersons() throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT person FROM Person person", Person.class);
            List<Person> persons = query.getResultList();
            System.out.println(persons.size());
            return PersonDTO.convertPersonsTopersonDTOs(persons);
        } catch (RuntimeException ex) {
            throw new PersonNotFoundException("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}");
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException{
        int updateCount;
        if(p.getFirstName() == null || p.getLastName() ==null){
            throw new MissingInputException("{\"code\": 500, \"message\": \"First Name and/or Last Name is missing\"}");
        }
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("UPDATE Person person SET person.first_name = :fName, person.last_name"
                    + " = :lName, person.phone = :phone, person.lastEdited = :lastEdited "
                    + "WHERE person.id = :id");
            updateCount = query.setParameter("fName", p.getFirstName())
                    .setParameter("lName", p.getLastName())
                    .setParameter("phone", p.getPhone())
                    .setParameter("lastEdited", new Date())
                    .setParameter("id", p.getId())
                    .executeUpdate();
            em.getTransaction().commit();

            if (updateCount == 1) {
                return p;
            } else {
                return null;
            }
        } catch (RuntimeException ex) {
            throw new PersonNotFoundException("{\"code\": 500, \"message\": \"Internal Server Problem. We are sorry for the inconvenience\"}");
        } finally {
            em.close();
        }

    }

}

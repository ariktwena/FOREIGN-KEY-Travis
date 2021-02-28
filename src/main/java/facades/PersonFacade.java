/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import dtos.RenameMeDTO;
import entities.Person;
import entities.RenameMe;
import exceptions.PersonNotDeletedException;
import exceptions.PersonNotFoundException;
import exceptions.PersonNotUpdatedException;
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
    public PersonDTO addPerson(String fName, String lName, String phone) {
        Person person = new Person(fName, lName, phone);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO deletePerson(int id) {
        int rowCount;
        EntityManager em = emf.createEntityManager();
        PersonDTO personDTO = getPerson(id);
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Person person WHERE person.id = :personDTO").setParameter("personDTO", personDTO.getId());
            rowCount = query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        if (rowCount == 1) {
            return personDTO;
        } else {
            return null;
        }
    }

    @Override
    public PersonDTO getPerson(int id) {
        EntityManager em = emf.createEntityManager();
        return new PersonDTO(em.find(Person.class, id));
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT person FROM Person person", Person.class);
        List<Person> persons = query.getResultList();
        System.out.println(persons.size());
        return PersonDTO.convertPersonsTopersonDTOs(persons);
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) {
        int updateCount;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("UPDATE Person person SET person.first_name = :fName, person.last_name"
                    + " = :lName, person.phone = :phone, person.lastEdited = :created "
                    + "WHERE person.id = :id");
            updateCount = query.setParameter("fName", p.getFirstName())
                    .setParameter("lName", p.getLastName())
                    .setParameter("phone", p.getPhone())
                    .setParameter("created", new Date())
                    .setParameter("id", p.getId())
                    .executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        if (updateCount == 1) {
            return p;
        } else {
            return null;
        }
    }

}

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class PersonPopulator {

    public static void populate() {
//        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
//        FacadeExample fe = FacadeExample.getFacadeExample(emf);
//        fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
//        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
//        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade pf = PersonFacade.getPersonFacade(emf);
        PersonDTO personDTO1 = pf.addPerson("Name 1", "Last 1", "111111");
        PersonDTO personDTO2 = pf.addPerson("Name 2", "Last 2", "222222");
        PersonDTO personDTO3 = pf.addPerson("Name 3", "Last 3", "333333");

        System.out.println(pf.getPerson(personDTO1.getId()).toString());

        personDTO1.setFirstName("Name 4");
        personDTO1.setLastName("Last 4");

        personDTO1 = pf.editPerson(personDTO1);

        System.out.println(pf.getPerson(personDTO1.getId()).toString());

        personDTO1 = pf.deletePerson(personDTO1.getId());
        System.out.println("Person was deleted :)");
        System.out.println(personDTO1.toString());

    }

    public static void main(String[] args) {
        populate();
    }
}

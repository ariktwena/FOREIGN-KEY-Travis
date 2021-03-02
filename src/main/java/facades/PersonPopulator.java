/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class PersonPopulator {

    public static void populate() {
        try {
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
        } catch (PersonNotFoundException ex) {
            //Logger.getLogger(PersonPopulator.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Code : 404, message: No person with provided id found");
        } catch(MissingInputException ex){
             System.out.println("You need first and last name");
        }

    }

    public static void main(String[] args) {
        populate();
    }
}

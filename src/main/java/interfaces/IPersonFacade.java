/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import dtos.PersonDTO;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.List;

/**
 *
 * @author Tweny
 */
public interface IPersonFacade {

    public PersonDTO addPerson(String fName, String lName, String phone) throws PersonNotFoundException, MissingInputException;

    public PersonDTO deletePerson(int id) throws PersonNotFoundException;

    public PersonDTO getPerson(int id) throws PersonNotFoundException, RuntimeException;

    public List<PersonDTO> getAllPersons() throws PersonNotFoundException;

    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException;

}

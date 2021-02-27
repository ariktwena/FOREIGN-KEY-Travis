/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author Tweny
 */
@Entity
public class SwimStyle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String styleName;
    
    //***************Many to Many****************
    //Husk at lave en this.persons = new ArrayList<Person>(); i konstruktøren
    //Man behøver kun at anføre @ManyToMany her
    @ManyToMany
    private List<Person> persons;

    public void addPersons(Person persons) {
        if(persons != null){
            this.persons.add(persons);
        }  
    }
    
    public List<Person> getPersons() {
        return persons;
    }
    //*******************************************

    public SwimStyle() {
    }

    public SwimStyle(String styleName) {
        this.styleName = styleName;
        this.persons = new ArrayList<Person>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }


    
}

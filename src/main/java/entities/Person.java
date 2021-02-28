/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tweny
 */
@Entity
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String first_name;
    private String last_name;
    private String phone;
    @Temporal(TemporalType.DATE)
    private Date created;
    @Temporal(TemporalType.DATE)
    private Date lastEdited;
    
    //***************One to One****************
    //CascadeType.PERSIST = Dette gør at vi ikke behøver pasister en adresse først, men systemet gør det selv, hvis Person indholder en adresse
    @OneToOne(cascade = CascadeType.PERSIST) 
    private Address address;
    
    //For at relationen kan gå tilbage, bliver Person sat via "this"
    public void setAddress(Address address) {
        this.address = address;
        if(address != null){
            address.setPerson(this);
        }
    }
    
    public Address getAddress() {
        return address;
    }
    //*****************************************
    
    
    //***************One to Many****************
    //mappedBy fortæller vilken fremmednøgle/variablenavn den skal mappes til i Fee objektet (private Person person)
    //Det er Person der ejer relationen, da det er ONE der ejer en ONE-TO-MANY relation
    //Husk at lave en this.fees = new ArrayList<Fee>(); i konstruktøren
    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST) 
    private List<Fee> fees;

     public void addFee(Fee fee) {
        this.fees.add(fee);
        if(fee != null){
            fee.setPerson(this);
        }
    }
    
    public List<Fee> getFees() {
        return fees;
    }
    //*****************************************

    
    //***************Many to Many****************
    //mappedBy fortæller vilken fremmednøgle/variablenavn den skal mappes til i SwimStyle objektet (private List<Person> persons)
    //Husk at lave en this.persons = new ArrayList<Person>(); i konstruktøren
    @ManyToMany(mappedBy = "persons", cascade = CascadeType.PERSIST) 
    private List<SwimStyle> swimStyles;

    public void addSwimStyles(SwimStyle swimStyle) {
        if(swimStyle != null){
            this.swimStyles.add(swimStyle);
            //Vi tilføjer denner Person til den aktuelle swimStyle
            swimStyle.getPersons().add(this);
        }
    }
    
     public void removeSwimStyles(SwimStyle swimStyle) {
        if(swimStyle != null){
            this.swimStyles.remove(swimStyle);
            //Vi tilføjer denner Person til den aktuelle swimStyle
            swimStyle.getPersons().remove(this);
        }
    }
    
    public List<SwimStyle> getSwimStyles() {
        return swimStyles;
    }
    //*******************************************
    
    public Person() {
    }

    public Person(String first_name, String last_name, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.created = new Date();
        this.lastEdited = new Date();
        this.fees = new ArrayList<Fee>();
        this.swimStyles = new ArrayList<SwimStyle>();
    }

    public Person(int id, String first_name, String last_name, String phone) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.created = new Date();
        this.lastEdited = new Date();
        this.fees = new ArrayList<Fee>();
        this.swimStyles = new ArrayList<SwimStyle>();
    }
    
    
    
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", phone=" + phone + ", created=" + created + ", lastEdited=" + lastEdited + ", address=" + address + ", fees=" + fees + ", swimStyles=" + swimStyles + '}';
    }

   

   

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Tweny
 */
@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String street;
    private int zip;
    private String city;

    //***************One to One hvor vi peger tilbage ****************
    //mappedBy fortæller vilken fremmednøgle/variablenavn den skal mappes til i Person objektet
    //Det er Person der ejer relationen
    @OneToOne(mappedBy = "address")
    private Person person;

    //Bruges til at sætte den Person som "Ejer" adressen i Person Entity
    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
    //****************************************************************

    public Address() {
    }

    public Address(String street, int zip, String city) {
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    @Override
//    public String toString() {
//        return "Address{" + "id=" + id + ", street=" + street + ", zip=" + zip + ", city=" + city + ", person=" + person + '}';
//    }

}

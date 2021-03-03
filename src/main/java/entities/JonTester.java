/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class JonTester {
    public static void main(String[] args) {
        
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        Person p3 = new Person("Jens", "last", "345364");
        Person p4 = new Person("Ole", "last", "345364");
        Person p5 = new Person("Bente", "last", "345364");
        Person p6 = new Person("Dennis", "last", "345364");
        Person p7 = new Person("Ida", "last", "345364");
        Person p8 = new Person("Mette", "last", "345364");
        Person p9 = new Person("Kaj", "last", "345364");
        Person p10 = new Person("Finn", "last", "345364");
        Person p11 = new Person("Charlotte", "last", "345364");
        Person p12 = new Person("Karin", "last", "345364");
        Person p13 = new Person("Gitte", "last", "345364");
        Person p14 = new Person("Hans", "last", "345364");
        
        Address a1 = new Address("Storegade 10", 2323, "Nr. Søby");
        Address a2 = new Address("Bredgade 14", 1212, "København K");
        Address a3 = new Address("Lillegade 1", 2323, "Nr. Søby");
        Address a4 = new Address("Damvej", 1212, "København K");
        Address a5 = new Address("Ndr Frihavnsgade 12", 2100, "Kbh Ø");
        Address a6 = new Address("Østerbrogade 85", 1212, "København K");
        Address a7 = new Address("Nørregade 4", 2200, "Nr. Søby");
        Address a8 = new Address("Nørregade 5", 2200, "København K");
        Address a9 = new Address("Odensegade 64", 2323, "Nr. Søby");
        Address a10 = new Address("Århusgade 29", 2300, "København S");
        
        p3.setAddress(a1);
        p4.setAddress(a2);
        p5.setAddress(a3);
        p6.setAddress(a4);
        p7.setAddress(a5);
        p8.setAddress(a6);
        p9.setAddress(a7);
        p10.setAddress(a8);
        p11.setAddress(a9);
        p12.setAddress(a10);
        p13.setAddress(a1);
        p14.setAddress(a2);
        
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
       
        p3.addFee(f1);
        p4.addFee(f3);
        p5.addFee(f2);
        p6.addFee(f1);
        p7.addFee(f3);
        p8.addFee(f2);
        p9.addFee(f1);
        p10.addFee(f3);
        p11.addFee(f2);
        p12.addFee(f1);
        p13.addFee(f3);
        p14.addFee(f2);
        
        em.getTransaction().begin();
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            em.persist(p6);
            em.persist(p7);
            em.persist(p8);
            em.persist(p9);
            em.persist(p10);
            em.persist(p11);
            em.persist(p12);
            em.persist(p13);
            em.persist(p14);
        em.getTransaction().commit();
    }
    
    
    
    //SELECT person.first_name, person.last_name, fee.amount FROM Person person JOIN Fee fee
    //SELECT person.first_name, COUNT(swim) FROM Person person JOIN SwimStyle swim GROUP BY person
    //SELECT person.first_name FROM Person person JOIN SwimStyle swim WHERE swim.styleName = "Crawl"
    //SELECT person.first_name FROM Person person JOIN SwimStyle swim WHERE LOWER(swim.styleName) = "crawl"
    //SELECT SUM(fee.amount) FROM Fee fee
    //SELECT MAX(fee.amount) FROM Fee fee
    //SELECT MIN(fee.amount) FROM Fee fee
}

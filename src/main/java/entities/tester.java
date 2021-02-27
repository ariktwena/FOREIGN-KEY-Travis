/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class tester {
    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        Person person1 = new Person("First_name_1", "Last_name_1");
        Person person2 = new Person("First_name_2", "Last_name_2");
        
        //Address one to one
        Address address1 = new Address("vej_1", 1001, "City_1");
        Address address2 = new Address("vej_2", 1002, "City_2");
        person1.setAddress(address1);
        person2.setAddress(address2);
        
        
        //Fee one to many
        Fee fee1 = new Fee(200);
        Fee fee2 = new Fee(300);
        Fee fee3 = new Fee(150);
        person1.addFee(fee1);
        person1.addFee(fee2);
        person2.addFee(fee3);
        
        
        //Swimstyle many to many
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("ButterFly");
        SwimStyle s3 = new SwimStyle("Breast Stroke");
        person1.addSwimStyles(s1);
        person1.addSwimStyles(s2);
        person2.addSwimStyles(s3);
        
        
        
        em.getTransaction().begin();
        //Pga CascadeType.PERSIST behøver vi ikke pasister disse først, da Person indholder en adresse
        //em.persist(address1);
        //em.persist(address2);
        em.persist(person1);
        em.persist(person2);
        em.getTransaction().commit();
        
        //Da den stadig er managed og ikke lukket, kan vi se om den bliver fjernet fra Person i DB
        em.getTransaction().begin();
        person1.removeSwimStyles(s2);
        em.getTransaction().commit();
        
        
        
        System.out.println(person1.toString());
        //System.out.println(address1.toString());
        System.out.println(address1.getPerson().getFirst_name());
        System.out.println(fee1.getPerson().getFirst_name());
        System.out.println(fee2.getPerson().getFirst_name());
        //System.out.println(fee2.toString());
        
        System.out.println("");
        System.out.println(person2.toString());
        //System.out.println(address2.toString());
        System.out.println(address2.getPerson().getFirst_name());
        System.out.println(fee3.getPerson().getFirst_name());
        
        System.out.println("");
        System.out.println("Hvad er der blevet betalt?");
        TypedQuery<Fee> query = em.createQuery("SELECT fee FROM Fee fee", Fee.class);
        List<Fee> fees = query.getResultList();
        for(Fee fee : fees){
            System.out.println(fee.getPerson().getFirst_name() + ": " + fee.getAmount() + " - " + fee.getPayDate());
        }
        
        
    }
}

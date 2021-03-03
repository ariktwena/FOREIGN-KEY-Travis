/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import facades.PersonFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class Tester2 {
    public static void main(String[] args) {
        Fee fee1 = new Fee(500);
        System.out.println(fee1);
        Person person1 = new Person("String first", "String last", "5555");
        System.out.println(person1);
        
        //person1.addFee(fee1);
        System.out.println(person1);
        System.out.println(fee1);
        
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
         em.getTransaction().begin();
        
        em.persist(person1);
        
        
        em.getTransaction().commit();
    }
}

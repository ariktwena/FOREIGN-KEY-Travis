/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tweny
 */
@Entity
public class Fee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long amount;
    @Temporal(TemporalType.DATE)
    private Date payDate;
    
    //***************Many to One hvor vi peger tilbage****************
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public Person getPerson() {
        return person;
    }
    //***************************************************************

    public Fee() {
    }

    public Fee(long amount) {
        this.amount = amount;
        this.payDate = new Date();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getPayDate() {
        return payDate;
    }

    
    
//    @Override
//    public String toString() {
//        return "Fee{" + "id=" + id + ", amount=" + amount + ", payDate=" + payDate + ", person=" + person + '}';
//    }
    
//    @Override
//    public String toString() {
//        return "Fee{" + "id=" + id + ", amount=" + amount +   '}';
//    }

    @Override
    public String toString() {
        return "Fee{" + "id=" + id + ", amount=" + amount + ", payDate=" + payDate + ", person=" + personInfo(person) + '}';
    }

    private String personInfo(Person person){
        return person != null?person.getFirst_name():"Person not set jet";
    }
    
}

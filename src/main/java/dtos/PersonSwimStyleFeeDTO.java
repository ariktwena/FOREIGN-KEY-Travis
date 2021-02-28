/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Tweny
 */
public class PersonSwimStyleFeeDTO {
    
    private int id;
    private String first_name;
    private String last_name;
    private String style_name;
    private long amount;

    public PersonSwimStyleFeeDTO(int id, String first_name, String last_name, String style_name, long amount) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.style_name = style_name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStyle_name() {
        return style_name;
    }

    public void setStyle_name(String style_name) {
        this.style_name = style_name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    
    
}

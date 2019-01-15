/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movieverse.movieverse.shipment.session;

import com.movieverse.entities.Customer;
import com.movieverse.entities.Person;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Child of abstractFacade which provides the generic functionality of a data acess object with extedned queries specific to
 * the entity being queried
 * 
 * @author mghnm
 */
@Stateless
public class UserBean extends AbstractFacade<Customer> {
    
    @PersistenceContext(unitName="movieversePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Person getUserByEmail(String email) {
        Query createNamedQuery = getEntityManager().createNamedQuery("Person.findByEmail");
        
        createNamedQuery.setParameter("email", email);
        
        return (Person) createNamedQuery.getSingleResult();
    }
    
    public UserBean() {
        super(Customer.class);
    }

}

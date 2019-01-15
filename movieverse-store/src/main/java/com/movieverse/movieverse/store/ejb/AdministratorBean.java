/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.ejb;

import com.movieverse.entities.Administrator;
import com.movieverse.entities.Groups;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Child of abstractFacade which provides the generic functionality of a data acess object with extedned queries specific to
 * the entity being queried
 * 
 * @author mghnm
 * @author ievans
 */
@Stateless
public class AdministratorBean extends AbstractFacade<Administrator> {
    @PersistenceContext(unitName = "movieversePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorBean() {
        super(Administrator.class);
    }
    
    @Override
    public void create(Administrator admin) {
        Groups adminGroup = (Groups) em.createNamedQuery("Groups.findByName")
                .setParameter("name", "Administrator")
                .getSingleResult();
        admin.getGroupsList().add(adminGroup);
        adminGroup.getPersonList().add(admin);
        em.persist(admin);
        em.merge(adminGroup);
    }
    
    @Override
    public void remove(Administrator admin) {
        Groups adminGroup = (Groups) em.createNamedQuery("Groups.findByName")
                .setParameter("name", "Administrator")
                .getSingleResult();
        adminGroup.getPersonList().remove(admin);
        em.remove(em.merge(admin));
        em.merge(adminGroup);
    }
    
}

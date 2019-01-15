/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.movieverse.movieverse.store.ejb;

import com.movieverse.entities.Category;
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
public class CategoryBean extends AbstractFacade<Category> {
    @PersistenceContext(unitName="movieversePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryBean() {
        super(Category.class);
    }

}

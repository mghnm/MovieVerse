/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.ejb;

import com.movieverse.entities.Category;
import com.movieverse.entities.Product;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 * Child of abstractFacade which provides the generic functionality of a data acess object with extedned queries specific to
 * the entity being queried
 * 
 * @author mghnm
 * 
 */
@Stateless
public class ProductBean extends AbstractFacade<Product> {

    private static final Logger LOGGER = 
            Logger.getLogger(ProductBean.class.getCanonicalName());
    
    @PersistenceContext(unitName="movieversePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductBean() {
        super(Product.class);
    }

    /**
     * Example usage of JPA CriteriaBuilder. You can also use NamedQueries
     * @param range
     * @param categoryId
     * @return 
     */
    public List<Product> findByCategory(int[] range, int categoryId) {       
         Category cat = new Category();
         cat.setId(categoryId);
         
         CriteriaBuilder qb = em.getCriteriaBuilder();
         CriteriaQuery<Product> query = qb.createQuery(Product.class);
         Root<Product> product = query.from(Product.class);
         query.where(qb.equal(product.get("category"), cat));

         List<Product> result = this.findRange(range, query);
         
         LOGGER.log(Level.FINEST, "Product List size: {0}", result.size());
         
        return result;
    }
    
   //Enables full text searching for the search bar in moveiverse-store via native sql query due to the complexity of this query. The productMapping
   //Is and sql mapping done from the result list to the class Product see product entity for details
    public List<Product> findByFulltextSearch(String searchQuery){
       Query q = em.createNativeQuery("select p.ID,p.NAME,p.PRICE,p.DESCRIPTION,p.IMG,p.CATEGORY_ID,p.IMG_SRC from product p " +
       "inner join " +
       "category c on c.ID = p.CATEGORY_ID " +
       "WHERE " +
            "MATCH(p.NAME, p.DESCRIPTION) AGAINST (? IN NATURAL LANGUAGE MODE) " +
        "OR " +
            "MATCH(c.NAME, c.TAGS) AGAINST (? IN NATURAL LANGUAGE MODE);", "ProductMapping");
        q.setParameter(1, searchQuery);
        q.setParameter(2, searchQuery);
      
       List<Product> result = q.getResultList();
       
       return result;
        
    }
    
    
    
}

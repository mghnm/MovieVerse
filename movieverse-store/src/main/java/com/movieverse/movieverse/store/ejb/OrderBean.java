/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.ejb;

import com.movieverse.entities.CustomerOrder;
import com.movieverse.entities.OrderStatus;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * OrderBean is an EJB exposed as RESTful service Provides methods to manipulate
 * order status and query orders based on specific status.
 *
 * @author mghnm
 */
@Stateless
@Path("/orders")
public class OrderBean extends AbstractFacade<CustomerOrder> implements Serializable {

    private static final Logger logger = 
            Logger.getLogger(ShoppingCart.class.getCanonicalName());
    private static final long serialVersionUID = -2407971550575800416L;
    @PersistenceContext(unitName = "movieversePU")
    private EntityManager em;
    CustomerOrder order;
    @EJB
    OrderStatusBean statusBean;

    public OrderBean() {
        super(CustomerOrder.class);
    }

    /**
     * **************************************************************************
     * Business methods
     * ***************************************************************************
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<CustomerOrder> getOrderByCustomerId(Integer id) {
        Query createNamedQuery = getEntityManager().createNamedQuery("CustomerOrder.findByCustomerId");

        createNamedQuery.setParameter("id", id);

        return createNamedQuery.getResultList();
    }
    
    public CustomerOrder getOrderById(Integer id) {
        Query createNamedQuery = getEntityManager().createNamedQuery("CustomerOrder.findById");
        createNamedQuery.setParameter("id", id);
        return (CustomerOrder) createNamedQuery.getSingleResult();
    }
  
    @GET
    @Produces({MediaType.APPLICATION_XML/*, "application/json"*/})
    public List<CustomerOrder> getOrderByStatus(@QueryParam("status") int status) {

        Query createNamedQuery = getEntityManager().createNamedQuery("CustomerOrder.findByStatus");

        OrderStatus result = statusBean.find(status);

        createNamedQuery.setParameter("status", result.getStatus());
        List<CustomerOrder> orders = createNamedQuery.getResultList();

        return orders;
    }

    @PUT
    @Path("{orderId}")
    @Produces({"application/xml", "application/json"})
    public void setOrderStatus(@PathParam("orderId") int orderId, String newStatus) { 
        
        logger.log(Level.INFO, "Order id:{0} - Status:{1}", new Object[]{orderId, newStatus});

        try {
            order = this.find(orderId);

            if (order != null) {
                logger.log(Level.FINEST, "Updating order {0} status to {1}", new Object[]{order.getId(), newStatus});

                OrderStatus oStatus = statusBean.find(new Integer(newStatus));
                order.setOrderStatus(oStatus);

                em.merge(order);

                logger.info("Order Updated!");
            }

        } catch (Exception ex) {

            logger.log(Level.SEVERE, ex.getMessage());
        }

    }
    
     /**
     * ***************************************************************************
     * Status orders mapped to ENUM
     */
    public enum Status {

        PENDING_PAYMENT(2),
        READY_TO_SHIP(3),
        SHIPPED(4),
        CANCELLED_PAYMENT(5),
        CANCELLED_MANUAL(6);
        private int status;

        private Status(int pStatus) {
            status = pStatus;
        }

        public int getStatus() {
            return status;
        }
    }
}

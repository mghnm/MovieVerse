/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.handlers;

import com.movieverse.movieverse.store.ejb.OrderBean;
import com.movieverse.movieverse.store.ejb.OrderJMSManager;
import com.movieverse.entities.CustomerOrder;
import com.movieverse.events.MainOrderEvent;
import com.movieverse.movieverse.store.qualifiers.Paid;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

/**
 *
 * Order handler which checks the Paid qualifier of an order event. If that is the case then update the order status
 * @author mghnm
 * @author markito
 */
@Stateless
public class DeliveryHandler implements IOrderHandler, Serializable {

    private static final Logger LOGGER = Logger.getLogger(DeliveryHandler.class.getCanonicalName());
    private static final long serialVersionUID = 4346750267714932851L;
    
    @EJB 
    OrderBean orderBean;
    @EJB
    OrderJMSManager orderJMSManager;
    
    @Override
    @Asynchronous
    public void onNewOrder(@Observes @Paid MainOrderEvent event) {
        
        LOGGER.log(Level.FINEST, "{0} Event being processed by DeliveryHandler", Thread.currentThread().getName());
       
        try {           
            LOGGER.log(Level.INFO, "Order #{0} has been paid in the amount of {1}. Order is now ready for delivery!", new Object[]{event.getOrderID(), event.getAmount()});
                                    
            orderBean.setOrderStatus(event.getOrderID(), String.valueOf(OrderBean.Status.READY_TO_SHIP.getStatus()));
            CustomerOrder order = orderBean.getOrderById(event.getOrderID());
            if (order != null) {
                orderJMSManager.sendMessage(order);
               
            } else {
                throw new Exception("The order does not exist");
            }
        } catch (Exception jex) {
            LOGGER.log(Level.SEVERE, null, jex);
        }
    }
}

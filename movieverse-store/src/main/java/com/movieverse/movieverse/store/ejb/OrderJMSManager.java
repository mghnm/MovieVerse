/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.ejb;

import com.movieverse.entities.CustomerOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 * Sends orders to the movieverse-shipment project to be managed by the authorized personell
 * jms/OrderQueue internally specified in glassfish 5.
 * @author mghan
 * @author ievans
 */
@Stateless
public class OrderJMSManager {

    private static final Logger LOGGER = Logger.getLogger(OrderJMSManager.class.getCanonicalName());
    @Inject
    private JMSContext context;
    
    @Resource(lookup = "jms/OrderQueue")
    private Queue queue;
    private QueueBrowser browser;

    /**
     *
     * @param customerOrder
     */
    public void sendMessage(CustomerOrder customerOrder) {
        ObjectMessage msgObj = context.createObjectMessage();

        try {
            msgObj.setObject(customerOrder);
            msgObj.setStringProperty("OrderID", String.valueOf(customerOrder.getId()));

            context.createProducer().send(queue, msgObj);
        } catch (JMSException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteMessage(int orderID) throws Exception {
        
        JMSConsumer consumer = context.createConsumer(queue, "OrderID='" + orderID + "'") ;
        
        CustomerOrder order = consumer.receiveBody(CustomerOrder.class, 1);
        
        if (order != null)
            LOGGER.log(Level.INFO, "Order {0} removed from queue.", order.getId());
        else {
            LOGGER.log(Level.SEVERE, "Order {0} was not removed from queue!", orderID); 
            throw new Exception("Order not removed from queue");
        }
        
    }
}

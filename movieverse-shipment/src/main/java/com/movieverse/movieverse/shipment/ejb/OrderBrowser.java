/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.shipment.ejb;

import com.movieverse.entities.CustomerOrder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/*
* Order browser done via reading orders of of an order queue and consuming them as customer order entities once they are processed.
* The order queue was specified internally on GlassFIsh 5 and utalized the jms.
* @author markito
* @author mghan
*
*
*/

@Stateless
public class OrderBrowser {

    private static final Logger LOGGER = Logger.getLogger(OrderBrowser.class.getCanonicalName());
    @Inject
    private JMSContext context;
    @Resource(lookup = "jms/OrderQueue")
    private Queue queue;
    private QueueBrowser queueBrowser;

    public Map<String, CustomerOrder> getOrders() {
        queueBrowser = context.createBrowser(queue);
        Enumeration msgs;
        try {
            msgs = queueBrowser.getEnumeration();

            if (!msgs.hasMoreElements()) {
                LOGGER.log(Level.INFO, "No messages on the queue!");
            } else {

                Map<String, CustomerOrder> result = new LinkedHashMap<>();
                while (msgs.hasMoreElements()) {
                    Message msg = (Message) msgs.nextElement();

                    LOGGER.log(Level.INFO, "Message ID: {0}", msg.getJMSMessageID());
                    CustomerOrder order = msg.getBody(CustomerOrder.class);
                    result.put(msg.getJMSMessageID(), order);
                }
                return result;
            }
        } catch (JMSException ex) {
            Logger.getLogger(OrderBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public CustomerOrder processOrder(String OrderMessageID) {

        LOGGER.log(Level.INFO, "Processing Order {0}", OrderMessageID);
        JMSConsumer consumer = context.createConsumer(queue, "JMSMessageID='" + OrderMessageID + "'");

        CustomerOrder order = consumer.receiveBody(CustomerOrder.class, 1);
        return order;
    }
}

/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.ejb;

import com.movieverse.events.MainOrderEvent;
import com.movieverse.movieverse.store.qualifiers.New;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * 
 * Creates a main order event.
 * 
 * @author mghnm
 * @author ievans
 */
@Named("EventDisptacherBean")
@Stateless
public class EventDispatcherBean {

     private static final Logger LOGGER = Logger.getLogger(EventDispatcherBean.class.getCanonicalName());

    
    @Inject @New
    Event<MainOrderEvent> eventManager;

    @Asynchronous
    public void publish(MainOrderEvent event) {
        LOGGER.log(Level.FINEST, "{0} Sending event from EJB", Thread.currentThread().getName());
        eventManager.fire(event);
    }
}

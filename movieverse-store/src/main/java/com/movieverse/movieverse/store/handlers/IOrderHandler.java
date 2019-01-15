/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.movieverse.movieverse.store.handlers;

import com.movieverse.events.MainOrderEvent;

/**
 * Handler interface implement by both Delivery and Payment handlers for updating the order
 *
 * @author markito
 * @author mghan
 */
public interface IOrderHandler  {
    
    public void onNewOrder(MainOrderEvent event);
    
}

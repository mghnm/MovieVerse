/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movieverse.movieverse.shipment.web;

import com.movieverse.entities.CustomerOrder;
import com.movieverse.movieverse.shipment.ejb.OrderBrowser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Restful client which will use the order restful service in movieverse-store to get an XML encoded order.
 * Used for updating the order of a status and listing completed orders in the MoivieVerse-Shipment project.
 * @author mghan
 */

@Named
@RequestScoped
public class ShippingBean implements Serializable {

    private static final Logger LOGGER =
            Logger.getLogger(ShippingBean.class.getCanonicalName());
    private static final String SERVICE_ENDPOINT =
            "http://localhost:8080/movieverse-store/services/orders";
    private static final String MEDIA_TYPE = MediaType.APPLICATION_XML/*MediaType.APPLICATION_JSON*/;
    private static final long serialVersionUID = -2526289536313985021L;
    protected Client client;
    @EJB
    OrderBrowser orderBrowser;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    private void clean() {
        client.close();
    }
    private Map<String, CustomerOrder> orders;

    /**
     * @return the orders
     */
    public Map<String, CustomerOrder> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(Map<String, CustomerOrder> orders) {
        this.orders = orders;
    }

    public enum Status {

        PENDING_PAYMENT(2),
        READY_TO_SHIP(3),
        SHIPPED(4),
        CANCELLED_PAYMENT(5),
        CANCELLED_MANUAL(6);
        private int status;

        private Status(final int paymentStatus) {
            status = paymentStatus;
        }

        public int getStatus() {
            return status;
        }
    }

    public String getEndpoint() {
        return SERVICE_ENDPOINT;
    }

    public List<CustomerOrder> listByStatus(Status completedOrder) {

        LOGGER.log(Level.SEVERE, "before entity generated");
        // List only completed orders status id 4
        List<CustomerOrder> entity = (List<CustomerOrder>) client.target(SERVICE_ENDPOINT)
                .queryParam("status", 4/*String.valueOf(status.getStatus())*/)
                .request(MEDIA_TYPE)
                .get(new GenericType<List<CustomerOrder>>() {
        });
        
        LOGGER.log(Level.SEVERE, "after entity generated");

        return entity;
    }

    public void updateOrderStatus(final String messageID, Status status) {
        // consume message
        CustomerOrder order = orderBrowser.processOrder(messageID);

        // call order service to update db in Store
        Response response = client.target(SERVICE_ENDPOINT)
                .path("/" + order.getId())
                .request(MEDIA_TYPE)
                .put(Entity.text(String.valueOf(status.getStatus())));

        LOGGER.log(Level.FINEST, "PUT Status response: {0}", response.getStatus());
    }

    /**
     * @return the orders
     */
    public List<String> getPendingOrders() {
        Map<String, CustomerOrder> pendingOrders = orderBrowser.getOrders();

        if (pendingOrders == null) {
            return null;
        } else {
            // update current pending orders map
            setOrders(pendingOrders);
            return new ArrayList<>(getOrders().keySet());
        }
    }

    public List<CustomerOrder> getCompletedOrders() {
        return listByStatus(Status.SHIPPED);
    }
}

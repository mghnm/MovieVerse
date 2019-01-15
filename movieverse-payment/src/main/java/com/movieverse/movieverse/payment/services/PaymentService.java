/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movieverse.movieverse.payment.services;

import com.movieverse.events.MainOrderEvent;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


/*
* Emulation of payment service with custom simple logic of accepting payment for any order below 600 SEK in value.
*
*/

@Path("/pay")
public class PaymentService {

    private static final Logger logger = Logger.getLogger("PaymentService");
    
    public PaymentService() { }
    
    @POST
    @Consumes("application/xml")
    public Response processPayment(MainOrderEvent myOrder) {
        logger.info("Amount: "+myOrder.getAmount());
        if (myOrder.getAmount() < 600) {
            return Response.ok().build();
        } else {
            return Response.status(401).build();
        }
    }
    
    @GET
    @Produces("text/html")
    public String getHtml() {
        return "PaymentService";
    }
}

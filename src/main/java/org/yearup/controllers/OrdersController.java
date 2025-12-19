package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Order;
import org.yearup.service.CheckoutService;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("orders")
public class OrdersController {

    private CheckoutService checkoutService;

    public OrdersController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Order checkout(Principal principal) {
        try
        {
            return checkoutService.checkout(principal.getName());
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


}

package org.ecommerce.orderservice.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(Integer qty)
    {
        super("Insufficient product quantity "+qty);
    }
}

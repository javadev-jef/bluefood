package br.com.bluefood.domain.pedido;

/**
 * EXCEPTION
 */
@SuppressWarnings("serial")
public class RestauranteDiferenteException extends Exception
{

    public RestauranteDiferenteException() 
    {
        super();
    }

    public RestauranteDiferenteException(String message) 
    {
        super(message);
    } 
}
package br.com.bluefood.service;

/**
 * EXCEPTION
 */
@SuppressWarnings("serial")
public class ValidationException extends Exception
{
    public ValidationException(String message)
    {
        super(message);
    }   
}
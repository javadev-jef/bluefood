package br.com.bluefood.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Scanner;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * STRING UTILS
 */
public class StringUtils 
{
    public static boolean isEmpty(final String str)
    {
        if(str == null)
        {
            return true;
        }

        return str.trim().length() == 0;
    }   

    public static String encrypt(final String rawString)
    {
        if(isEmpty(rawString))
        {
            return null;
        }

        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        return encoder.encode(rawString);
    }

    public static String trim(String value)
    {
        // FIXED BUG TEXTAREA 
        try(Scanner scanner = new Scanner(value))
        {
            value = scanner.delimiter().matcher(value.trim()).replaceAll("");
        }

        return value;
    }

    public static boolean trimAndEqualsIgnoreCase(String value1, String value2)
    {
        return trim(value1).equalsIgnoreCase(trim(value2));
    }

    public static String getDate() throws Exception
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String concatenate(Collection<String> strings)
    {
        if(strings == null || strings.size() == 0)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String delimiter = ", ";
        boolean first = true;

        for(String string : strings)
        {
            if(!first)
            {
                sb.append(delimiter);
            }

            sb.append(string);
            first = false;
        }

        return sb.toString();
    }
}
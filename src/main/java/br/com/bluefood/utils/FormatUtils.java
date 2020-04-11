package br.com.bluefood.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * FORMAT UTILS
 */
public class FormatUtils 
{
    private static final Locale LOCALE_BRAZIL = new Locale("pt", "BR");

    public static NumberFormat newCurrencyFormat()
    {
        NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_BRAZIL);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false);

        return nf;
    }    

    public static String formatCurrency(BigDecimal number)
    {
        if(number == null)
        {
            return null;
        }

        return newCurrencyFormat().format(number);
    }

    public static String newDateFormatted(LocalDateTime date)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE_BRAZIL);
        return date.format(dtf);
    }
}
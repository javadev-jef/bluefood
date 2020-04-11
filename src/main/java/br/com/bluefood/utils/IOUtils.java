package br.com.bluefood.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * IOUtils
 */
public class IOUtils 
{
    public static void copy(InputStream in, String fileName, String outputDir) throws IOException
    {
        Files.copy(in, Paths.get(outputDir, fileName), StandardCopyOption.REPLACE_EXISTING);
    }    

    public static void delete(String dir, String fileName) throws IOException
    {
        if(dir != null && fileName != null)
        {
            Files.deleteIfExists(Paths.get(dir, fileName));
        }
    }

    public static byte[] getBytes(Path path) throws IOException
    {
        return Files.readAllBytes(path);
    }
}
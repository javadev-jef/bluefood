package br.com.bluefood.utils;

import lombok.Getter;

/**
 * FILE TYPE
 */
@Getter
public enum FileType 
{
    PNG("image/png", "png"),
    JPG("image/jpeg", "jpg");

    String mimeType;
    String extension;

    FileType(String mimeType, String extension)
    {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public boolean sameOf(String mimeType)
    {
        return this.mimeType.equalsIgnoreCase(mimeType);
    }

    public static FileType of(String mimeType)
    {
        for(FileType fileType: values())
        {
            if(fileType.sameOf(mimeType))
            {
                return fileType;
            }
        }

        return null;
    }
}
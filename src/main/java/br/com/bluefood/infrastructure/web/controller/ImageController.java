package br.com.bluefood.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluefood.service.ImageService;

/**
 * IMAGE CONTROLLER
 */
@Controller
public class ImageController 
{
    @Autowired
    private ImageService imageService;

    @GetMapping(path = "/images/remote/{type}/{imgName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getBytes(@PathVariable("type") String type, @PathVariable("imgName") String imgName)
    {
        return imageService.getBytes(type, imgName);
    }
}
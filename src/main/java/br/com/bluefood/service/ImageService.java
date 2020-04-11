package br.com.bluefood.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.utils.IOUtils;

/**
 * IMAGE SERVICE
 */
@Service
public class ImageService 
{
    @Value("${bluefood.files.logotipo}")
    private String logotiposDir;

    @Value("${bluefood.files.categoria}")
    private String categoriasDir;

    @Value("${bluefood.files.categoria.capas}")
    private String capaCategoriaDir;

    @Value("${bluefood.files.comida}")
    private String comidasDir;

    @Value("${bluefood.files.comida.capas}")
    private String capaComidasDir;

    @Value("${bluefood.files.restaurante}")
    private String restauranteDir;

    @Value("${bluefood.files.banners}")
    private String bannersDir;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    public void uploadLogotipo(MultipartFile multipartFile, String fileName) 
    {
        try
        {
            IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir);
        } 
        catch (IOException e) 
        {
            throw new ApplicationServiceException(e);
        }
    }

    public void uploadBanner(MultipartFile multipartFile, String fileName) 
    {
        try
        {
            IOUtils.copy(multipartFile.getInputStream(), fileName, bannersDir);
        } 
        catch (IOException e) 
        {
            throw new ApplicationServiceException(e);
        }
    }

    public void uploadComida(MultipartFile multipartFile, String fileName) 
    {
        try
        {
            IOUtils.copy(multipartFile.getInputStream(), fileName, comidasDir);
        } 
        catch (IOException e) 
        {
            throw new ApplicationServiceException(e);
        }
    }

    public void deleteComida(Integer itemId)
    {
        try
        {
            ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();

            IOUtils.delete(comidasDir, itemCardapio.getImagem());
            IOUtils.delete(capaComidasDir, itemCardapio.getCapa());
        } 
        catch (IOException e) 
        {
            throw new ApplicationServiceException(e);
        }
        
    }

    public void uploadCapaComida(MultipartFile multipartFile, String fileName) 
    {
        try
        {
            IOUtils.copy(multipartFile.getInputStream(), fileName, capaComidasDir);
        } 
        catch (IOException e) 
        {
            throw new ApplicationServiceException(e);
        }
    }
    
    public byte[] getBytes(String type, String imgName)
    {
        try 
        {
            String dir;
            if("comida".equals(type))
            {
                dir = comidasDir;
            }
            else if("comida-capa".equals(type))
            {
                dir = capaComidasDir;
            }
            else if("logotipo".equals(type))
            {
                dir = logotiposDir;
            }
            else if("categoria-comidas".equals(type))
            {
                dir = categoriasDir;
            }
            else if("categoria-comidas-capa".equals(type))
            {
                dir = capaCategoriaDir;
            }
            else if("categoria-restaurante".equals(type))
            {
                dir = restauranteDir;
            }
            else if("banner-restaurante".equals(type))
            {
                dir = bannersDir;
            }
            else
            {
                throw new Exception(type + " não é um tipo de imagem válido");
            }

           return IOUtils.getBytes(Paths.get(dir, imgName));
        } 
        catch (Exception e) 
        {
            throw new ApplicationServiceException(e);
        }
    }
}
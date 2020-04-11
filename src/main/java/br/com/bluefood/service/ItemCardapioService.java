package br.com.bluefood.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;

/**
 * MENU ITEM SERVICE
 */
@Service
public class ItemCardapioService 
{
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private ImageService imageService;

    public ItemCardapio findInCollection(Integer itemCardapioId, List<ItemCardapio> itensCardapio) 
    {
        for(ItemCardapio itemCardapio : itensCardapio)
        {
            if(itemCardapio.getId().equals(itemCardapioId))
            {
                CategoriaRestaurante categoria = itemCardapio.getCategoria();
                
                if(itemCardapio.getCapa() == null)
                {
                    itemCardapio.setCapa("/images/remote/categoria-comidas-capa/"+categoria.getUrlCapa());
                }
                else
                {
                    itemCardapio.setCapa("/images/remote/comida-capa/"+itemCardapio.getCapa());
                }

                return itemCardapio;
            }
        }

        return null;
    }

    @Transactional
    public void save(ItemCardapio itemCardapio) throws ValidationException
    {
        ItemCardapio itemCardapioBD = itemCardapio.getId() != null ? itemCardapioRepository.findById(itemCardapio.getId()).orElseThrow() : null;

        if(itemCardapioBD == null && itemCardapio.getImagemFile().isEmpty())
        {
            throw new ValidationException("Nenhuma imagem selecionada");
        }

        if(itemCardapioBD != null)
        {
            // VERIFY IMAGEM-FILE
            if(itemCardapio.getImagemFile().isEmpty())
            {
                itemCardapio.setImagem(itemCardapioBD.getImagem());
            }
            else{
                itemCardapio.setImagemFileName();
                imageService.uploadComida(itemCardapio.getImagemFile(), itemCardapio.getImagem());
            }

            // VERIFY CAPA-FILE
            if(itemCardapio.getCapaFile().isEmpty())
            {
                itemCardapio.setCapa(itemCardapioBD.getCapa());
            }
            else{
                itemCardapio.setCapaFileName();
                imageService.uploadCapaComida(itemCardapio.getCapaFile(), itemCardapio.getCapa());
            }

            itemCardapioRepository.save(itemCardapio);
        }
        else{
            itemCardapioRepository.save(itemCardapio);
            itemCardapio.setImagemFileName();
            imageService.uploadComida(itemCardapio.getImagemFile(), itemCardapio.getImagem());
            
            // VERIFY CAPA-FILE
            if(!itemCardapio.getCapaFile().isEmpty())
            {
                itemCardapio.setCapaFileName();
                imageService.uploadCapaComida(itemCardapio.getCapaFile(), itemCardapio.getCapa());
            }
        }
    }

    @Transactional
    public void delete(Integer itemId) throws NoSuchElementException
    {
        imageService.deleteComida(itemId);
        itemCardapioRepository.deleteById(itemId);
    }
}
package br.com.bluefood.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.endereco.EnderecoRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteComparator;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.restaurante.SearchFilter;
import br.com.bluefood.domain.restaurante.SearchFilter.Options;
import br.com.bluefood.utils.SecurityUtils;

/**
 * RESTAURANT SERVICE
 */
@Service
public class RestauranteService 
{
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public Restaurante saveRestaurante(Restaurante restaurante) throws ValidationException
    {
        if(!validateEmail(restaurante.getEmail(), restaurante.getId()))
        {
            throw new ValidationException("O e-mail informado já possui cadastro");
        }

        if(restaurante.getId() != null)
        {
            Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow();

            if(restaurante.isAlterarSenha())
            {
                restaurante.encryptPassword();
            }
            else{
                restaurante.setSenha(restauranteDB.getSenha());
            }

            if(restaurante.getLogotipoFile() == null || restaurante.getLogotipoFile().getSize() == 0)
            {
                restaurante.setLogotipo(restauranteDB.getLogotipo());
            }
            else{
                restaurante.setLogotipoFileName();
                imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
            }

            if(restaurante.getBannerFile() == null || restaurante.getBannerFile().getSize() == 0)
            {
                restaurante.setBanner(restauranteDB.getBanner());
            }
            else{
                restaurante.setBannerFileName();
                imageService.uploadBanner(restaurante.getBannerFile(), restaurante.getBanner());
            }
            
            restaurante = restauranteRepository.save(restaurante);
        }
        else{
            restaurante.encryptPassword();
            enderecoRepository.save(restaurante.getEndereco());
            restaurante = restauranteRepository.save(restaurante);
            restaurante.setLogotipoFileName();
            imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
            
            if(!restaurante.getBannerFile().isEmpty())
            {
                restaurante.setBannerFileName();
                imageService.uploadBanner(restaurante.getBannerFile(), restaurante.getBanner());
            }
        }

        return restaurante;
    }

    private boolean validateEmail(String email, Integer id)
    {
        Cliente cliente = clienteRepository.findByEmail(email);

        if(cliente != null)
        {
            return false;
        }
        
        Restaurante restaurante = restauranteRepository.findByEmail(email);

        if(restaurante != null)
        {
            if(id == null)
            {
                return false;
            }

            if(!restaurante.getId().equals(id))
            {
                return false;
            }
        }

        return true;
    }

    public List<Restaurante> search(SearchFilter filter)
    {
        List<Restaurante> restaurantes = new ArrayList<>();

        switch (filter.getSearchType()) 
        {
            case TEXTO:
            {
                restaurantes = restauranteRepository.findByRazaoIgnoreCaseContaining(filter.getTexto());
                break;
            }
            case CATEGORIA:
            {
                restaurantes = restauranteRepository.findByCategorias_Id(filter.getIdCategoria());
                break;
            }
            default:
            {
                throw new IllegalStateException("O tipo de busca "+filter.getSearchType()+" não é suportado");
            }
        }

        restaurantes = validateOptionsFilter(filter, restaurantes);
        return restaurantes;
    }

    private List<Restaurante> validateOptionsFilter(SearchFilter filter, List<Restaurante> restaurantes)
    {
        Iterator<Restaurante> it = restaurantes.iterator();

        while(it.hasNext())
        {
            Restaurante restaurante = it.next();
            double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();

            if(filter.getOption() == Options.EntregaGratis && taxaEntrega >0)
            {
                it.remove();
            }
        }

        RestauranteComparator comparator = new RestauranteComparator(filter, SecurityUtils.loggedCliente().getEndereco().getCep());
        restaurantes.sort(comparator);

        return restaurantes;
    }
}
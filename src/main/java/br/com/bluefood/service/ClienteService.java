package br.com.bluefood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.endereco.EnderecoRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;

/**
 * CLIENT SERVICE
 */
@Service
public class ClienteService 
{
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Transactional
    public Cliente saveCliente(Cliente cliente) throws ValidationException
    {
        if(!validateEmail(cliente.getEmail(), cliente.getId()))
        {
            throw new ValidationException("O e-mail informado já possui cadastro");
        }

        if(cliente.getId() != null)
        {
            Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElseThrow();

            if(cliente.isAlterarSenha())
            {
                cliente.encryptPassword();
            }
            else{
                cliente.setSenha(clienteDB.getSenha());
            }
        }
        else{
            cliente.encryptPassword();
        }

        enderecoRepository.save(cliente.getEndereco());
        return clienteRepository.save(cliente);
    }

    private boolean validateEmail(String email, Integer id)
    {
        Restaurante restaurante = restauranteRepository.findByEmail(email);

        if(restaurante != null)
        {
            return false;
        }

        Cliente cliente = clienteRepository.findByEmail(email);

        if(cliente != null)
        {
            if(id == null)
            {
                return false;
            }

            if(!cliente.getId().equals(id))
            {
                return false;
            }
        }

        return true;
    }
}
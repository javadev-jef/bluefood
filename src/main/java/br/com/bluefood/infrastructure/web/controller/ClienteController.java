package br.com.bluefood.infrastructure.web.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.cliente.ClienteRepository;
import br.com.bluefood.domain.estados.EstadosRepository;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.restaurante.CategoriaRepository;
import br.com.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.restaurante.SearchFilter;
import br.com.bluefood.service.ClienteService;
import br.com.bluefood.service.ItemCardapioService;
import br.com.bluefood.service.RestauranteService;
import br.com.bluefood.service.ValidationException;
import br.com.bluefood.utils.SecurityUtils;



/**
 * CLIENT CONTROLLER
 */
@Controller
@RequestMapping(path = "/cliente")
@SessionAttributes("year")
public class ClienteController 
{
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private ItemCardapioService itemCardapioService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @ModelAttribute("yearFooter")
    public Integer getYearOfCurrentDate() 
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @GetMapping(path = "/home")
    public String clienteHome(Model model)
    {
        List<CategoriaRestaurante> categorias = categoriaRepository.findAll(Sort.by("descricao"));
        model.addAttribute("categorias", categorias);
        model.addAttribute("searchFilter", new SearchFilter());

        Object[] bannersRestaurante = restauranteRepository.getIdAndBanner();
        model.addAttribute("bannersRestaurantes", bannersRestaurante);

        List<Pedido> pedidos = pedidoRepository.listPedidosbyIdCliente(SecurityUtils.loggedCliente().getId());
        model.addAttribute("pedidos", pedidos);

        return "cliente-home";
    }

    @GetMapping("/edit")
    public String edit(Model model) 
    {
        Integer clienteId = SecurityUtils.loggedCliente().getId();
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
        model.addAttribute("cliente", cliente);
        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);

        return "cliente-cadastro";
    }

    @PostMapping("/save")
    public String saveCliente(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            try
            {
                cliente = clienteService.saveCliente(cliente);
                model.addAttribute("msg", "Dados alterados com sucesso!");
                model.addAttribute("invalidSession", true);
                model.addAttribute("validationError", false);
            }
            catch(ValidationException e)
            {
                errors.rejectValue("email", null, e.getMessage());
                model.addAttribute("msg", "Ops!");
                model.addAttribute("validationError", true);
            }
        }
        else{
            model.addAttribute("msg", "Ops!");
            model.addAttribute("validationError", true);
        }

        model.addAttribute("cliente", cliente);
        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);

        return "cliente-cadastro";
    }

    @GetMapping(path = "/search")
    public String search(@ModelAttribute("searchFilter") SearchFilter filter, Model model) 
    {
        filter.processFilter();

        List<Restaurante> restaurantes = restauranteService.search(filter);
        model.addAttribute("restaurantes", restaurantes);

        ControllerHelper.addCategoriasToRequest(categoriaRepository, model);

        model.addAttribute("searchFilter", filter);
        return "cliente-busca";
    } 

    @GetMapping(path = "/restaurante")
    public String viewRestaurante(Model model, @RequestParam("idRestaurante") Integer idRestaurante, @RequestParam(value = "categoriaId", required = false) Integer categoriaId, @RequestParam(value = "itemCardapioId", required = false) Integer itemCardapioId)
    {
        model.addAttribute("searchFilter", new SearchFilter());

        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElseThrow();
        model.addAttribute("restaurante", restaurante);

        Set<CategoriaRestaurante> categorias = restaurante.getCategorias();
        model.addAttribute("categorias", categorias);

        List<ItemCardapio> itensCardapio;

        if(categoriaId != null)
        {
            itensCardapio = itemCardapioRepository.findByCategoria_IdAndRestaurante_Id(categoriaId, idRestaurante);
        }
        else
        {
            itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(idRestaurante);
        }

        if(itemCardapioId != null)
        {
            ItemCardapio itemSelecionado = itemCardapioService.findInCollection(itemCardapioId, itensCardapio);
            model.addAttribute("itemCardapioSelected", itemSelecionado);
        }

        model.addAttribute("itensCardapio", itensCardapio);

        return "cliente-restaurante";
    }
}
package br.com.bluefood.infrastructure.web.controller;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bluefood.domain.estados.EstadosRepository;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.RelatorioItemFaturamento;
import br.com.bluefood.domain.pedido.RelatorioItemFilter;
import br.com.bluefood.domain.pedido.RelatorioPedidoFilter;
import br.com.bluefood.domain.restaurante.CategoriaRepository;
import br.com.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.domain.restaurante.SearchFilter;
import br.com.bluefood.service.ItemCardapioService;
import br.com.bluefood.service.RelatorioService;
import br.com.bluefood.service.RestauranteService;
import br.com.bluefood.service.ValidationException;
import br.com.bluefood.utils.SecurityUtils;

/**
 * RESTAURANT CONTROLLER
 */
@Controller
@RequestMapping(path = "/restaurante")
@SessionAttributes("year")
public class RestauranteController 
{
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private ItemCardapioService itemCardapioService;

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private RelatorioService relatorioService;

    @ModelAttribute("yearFooter")
    public Integer getYearOfCurrentDate() 
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    
    @GetMapping(path = "/home")
    public String home(Model model)
    {
        List<CategoriaRestaurante> categorias = categoriaRepository.findAll(Sort.by("descricao"));
        model.addAttribute("categorias", categorias);
        model.addAttribute("searchFilter", new SearchFilter());

        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        List<Pedido> pedidos = pedidoRepository.findByRestaurante_IdOrderByDataDesc(restauranteId);
        model.addAttribute("pedidos", pedidos);
        
        return "restaurante-home";
    }

    @GetMapping(path = "/home-pedidos")
    public String homeOpenPedidos(Model model)
    {
        model.addAttribute("openPedidoTab", true);
        return home(model);
    }

    @GetMapping("/edit")
    public String edit(Model model) 
    {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
        model.addAttribute("restaurante", restaurante);

        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);
        ControllerHelper.addCategoriasToRequest(categoriaRepository, model);

        return "restaurante-cadastro";
    }

    @PostMapping("/save")
    public String saveRestaurante(@ModelAttribute("restaurante") @Valid Restaurante restaurante, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            try
            {
                restaurante = restauranteService.saveRestaurante(restaurante);
                model.addAttribute("msg", "Dados alterados com sucesso!");
                model.addAttribute("invalideSession", true);
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

        model.addAttribute("restaurante", restaurante);
        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);
        ControllerHelper.addCategoriasToRequest(categoriaRepository, model);

        return "restaurante-cadastro";
    }

    @GetMapping(path = "/comidas")
    public String viewComidas(Model model)
    {
        Restaurante restaurante = ControllerHelper.getLoggedRestaurante(restauranteRepository);
        model.addAttribute("restaurante", restaurante);

        model.addAttribute("itemCardapio", new ItemCardapio());

        ControllerHelper.addItensCardapioToRequest(itemCardapioRepository, model);

        model.addAttribute("categorias", restaurante.getCategorias());

        return "restaurante-comidas";
    }

    @GetMapping(path = "/comidas/remover")
    public String remover(@RequestParam("itemId") Integer itemId, Model model, RedirectAttributes redirect)
    {
        try
        {
            itemCardapioService.delete(itemId);
            redirect.addFlashAttribute("validationError", false);
            redirect.addFlashAttribute("msg", "Item excluido com sucesso!");
        }
        catch(NoSuchElementException e)
        {
            redirect.addFlashAttribute("validationError", true);
            redirect.addFlashAttribute("msg", "O item selecionado não existe ou já foi excluido por algum usuário.");
        }

        return "redirect:/restaurante/comidas";
    }

    @GetMapping(path = "/comidas/editar")
    public String editar(@RequestParam("itemId") Integer itemId, Model model, RedirectAttributes redirect)
    {
        Restaurante restaurante = ControllerHelper.getLoggedRestaurante(restauranteRepository);
        model.addAttribute("restaurante", restaurante);

        ControllerHelper.addItensCardapioToRequest(itemCardapioRepository, model);
        model.addAttribute("categorias", restaurante.getCategorias());

        try
        {
            ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
            model.addAttribute("itemCardapio", itemCardapio);
        }
        catch(NoSuchElementException e)
        {
            redirect.addFlashAttribute("validationError", true);
            redirect.addFlashAttribute("msg", "O item a ser editado não existe no banco de dados ou foi excluido por algum usuário.");
            return "redirect:/restaurante/comidas";
        }

        return "restaurante-comidas";
    }

    @PostMapping("/comidas/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("itemCardapio") ItemCardapio itemCardapio,
        Errors errors, Model model, RedirectAttributes redirect)
    {
        if(!errors.hasErrors())
        {
            Integer itemId = itemCardapio.getId();
            redirect.addFlashAttribute("msg", itemId == null ? "Item cadastrado com sucesso!" : "Alteração realizada com sucesso"); 
 
            try
            {
                redirect.addFlashAttribute("validationError", false);
                itemCardapioService.save(itemCardapio);
                
                return "redirect:/restaurante/comidas";
            }
            catch(ValidationException e)
            {
                errors.rejectValue("imagemFile", null, e.getMessage());
            }
        }

        Restaurante restaurante = ControllerHelper.getLoggedRestaurante(restauranteRepository);
        model.addAttribute("restaurante", restaurante);

        ControllerHelper.addItensCardapioToRequest(itemCardapioRepository, model);
        model.addAttribute("categorias", restaurante.getCategorias());
        model.addAttribute("msg", "Campos obrigatórios não preenchidos");
        model.addAttribute("validationError", true);

        return "restaurante-comidas";
    }

    @GetMapping(path = "/pedido")
    public String viewPedido(@RequestParam("pedidoId") Integer pedidoId, Model model)
    {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
        model.addAttribute("pedido", pedido);

        return "restaurante-pedido";
    }

    @PostMapping("/pedido/proximoStatus")
    public String nextStatus(@RequestParam("pedidoId") Integer pedidoId, Model model)
    {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
        pedido.definirProximoStatus();
        pedidoRepository.save(pedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("msg", "Status alterado com sucesso!");

        return "redirect:/restaurante/pedido"+"?pedidoId="+pedidoId;
    }

    @GetMapping("/relatorio/pedidos")
    public String relatorioPedidos(@ModelAttribute("relatorioPedidoFilter") @Valid RelatorioPedidoFilter filter, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
            List<Pedido> pedidos = relatorioService.listPedidos(restauranteId, filter);

            model.addAttribute("pedidos", pedidos);
        }

        model.addAttribute("filter", filter);
        
        return "restaurante-relatorio-pedidos";
    }

    @GetMapping("/relatorio/itens")
    public String relatorioItens(@ModelAttribute("relatorioItemFilter") @Valid RelatorioItemFilter filter, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
            List<RelatorioItemFaturamento> itensCalculados = relatorioService.calcularFaturamentoItens(restauranteId, filter);
            model.addAttribute("itensCalculados", itensCalculados);
        }

        model.addAttribute("filter", filter);
        return "restaurante-relatorio-itens";
    }
}

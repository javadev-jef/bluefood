package br.com.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluefood.service.ClienteService;
import br.com.bluefood.service.RestauranteService;
import br.com.bluefood.service.ValidationException;
import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.estados.EstadosRepository;
import br.com.bluefood.domain.restaurante.CategoriaRepository;
import br.com.bluefood.domain.restaurante.Restaurante;

/**
 * PUBLIC CONTROLLER
 */
@Controller
@RequestMapping(path = "/public")
public class PublicController
{
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private CategoriaRepository categoriaRepositoy;

    @GetMapping("/cliente/new")
    public String newCliente(Model model)
    {
        model.addAttribute("cliente", new Cliente());
        ControllerHelper.setEditMode(model, false);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);

        return "cliente-cadastro";
    }  

    @GetMapping("/restaurante/new")
    public String newRestaurante(Model model)
    {
        model.addAttribute("restaurante", new Restaurante());
        ControllerHelper.setEditMode(model, false);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);
        ControllerHelper.addCategoriasToRequest(categoriaRepositoy, model);

        return "restaurante-cadastro";
    } 

    @PostMapping(path = "restaurante/save")
    public String saveRestaurante(@ModelAttribute("restaurante") @Valid Restaurante restaurante, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            try
            {
                if(!restaurante.getLogotipoFile().isEmpty())
                {
                    restauranteService.saveRestaurante(restaurante);
                    model.addAttribute("restaurante", new Restaurante());
                    model.addAttribute("msg", "Restaurante cadastrado com sucesso!");
                    model.addAttribute("validationError", false);
                }
                else
                {
                    errors.rejectValue("logotipoFile", null, "Nenhum logotipo informado");
                    model.addAttribute("validationError", true);
                    model.addAttribute("msg", "Ops!");
                }
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

        ControllerHelper.setEditMode(model, false);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);
        ControllerHelper.addCategoriasToRequest(categoriaRepositoy, model);
        return "restaurante-cadastro";
    }

    @PostMapping(path = "cliente/save")
    public String saveCliente(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model)
    {
        if(!errors.hasErrors())
        {
            try
            {
                clienteService.saveCliente(cliente);
                model.addAttribute("cliente", new Cliente());
                model.addAttribute("msg", "Cliente cadastrado com sucesso!");
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

        ControllerHelper.setEditMode(model, false);
        ControllerHelper.addEstadosToRequest(estadosRepository, model);
        return "cliente-cadastro";
    }
}
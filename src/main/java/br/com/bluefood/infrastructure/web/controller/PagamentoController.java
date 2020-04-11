package br.com.bluefood.infrastructure.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bluefood.domain.pagamento.CartaoCredito;
import br.com.bluefood.domain.pedido.Carrinho;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.service.PedidoService;
import me.pagar.model.PagarMeException;


/**
 * PAYMENT CONTROLLER
 */
@Controller
@RequestMapping("/cliente/pagamento")
@SessionAttributes("carrinho")
public class PagamentoController 
{
    @Autowired
    private PedidoService pedidoService;

    @PostMapping(path="/pagar")
    public String pagar(@ModelAttribute("cartaoCredito") @Valid CartaoCredito cartaoCredito, Errors errors, RedirectAttributes redirect, Model model, @ModelAttribute("carrinho") Carrinho carrinho, SessionStatus sessionStatus) 
    {
        if(!errors.hasErrors())
        {
            try
            {
                Pedido pedido = pedidoService.criarEPagar(carrinho, cartaoCredito);
                sessionStatus.setComplete();
                redirect.addFlashAttribute("msg", "Pagamento realizado com sucesso!");
                redirect.addFlashAttribute("validationError", false);
                return "redirect:/cliente/pedido/view?pedidoId="+pedido.getId();
            }
            catch(PagarMeException e)
            {
                model.addAttribute("msg", e.getMessage());
                model.addAttribute("validationError", true);
                errors.reject(null);
            }
        }
        else
        {
            model.addAttribute("msg", "Ops!");
            model.addAttribute("validationError", true);
        }
        
        return "cliente-pagamento";
    } 
}
package br.com.bluefood.infrastructure.web.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bluefood.domain.pagamento.CartaoCredito;
import br.com.bluefood.domain.pedido.Carrinho;
import br.com.bluefood.domain.pedido.ItemPedido;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.RestauranteDiferenteException;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;

/**
 * SHOPPING CART CONTROLLER
 */
@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes(names = {"carrinho", "year"})
public class CarrinhoController 
{
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @ModelAttribute("carrinho")
    public Carrinho carrinho() 
    {
        return new Carrinho();
    }

    @GetMapping(value = "/adicionar")
    public String addItem(@RequestParam("itemId") Integer itemId, @RequestParam("restauranteId") Integer restauranteId, @RequestParam("qtd") Integer qtd, @RequestParam("observacao") String observacao, @ModelAttribute("carrinho") Carrinho carrinho, Model model, RedirectAttributes redirect) {

        ItemCardapio itemCardapio;
        try 
        {
            itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
            carrinho.addItem(itemCardapio, qtd, observacao);
            redirect.addFlashAttribute("validationError", false);
            redirect.addFlashAttribute("msg", "Item adicionado ao carrinho");
        }
        catch(NoSuchElementException e)
        {
            redirect.addFlashAttribute("validationError", true);
            redirect.addFlashAttribute("msg", "O item a ser adicionado ao carrinho não está mais cadastrado no sistema");
        }
        catch (RestauranteDiferenteException e) 
        {
            redirect.addFlashAttribute("validationError", true);
            redirect.addFlashAttribute("msg", "Não é possivel misturar comidas de restaurantes diferentes");
            return "redirect:/cliente/restaurante?itemCardapioId=&idRestaurante="+restauranteId;
        }

        return "redirect:/cliente/restaurante?itemCardapioId=&idRestaurante="+restauranteId;
    }

    @GetMapping(value = "/visualizar")
    public String viewCarrinho()
    {
        return "cliente-carrinho";
    }

    @GetMapping(value = "subtrairQtdItem")
    public String subtrairItemPedido(@ModelAttribute("carrinho") Carrinho carrinho, @RequestParam("index") Integer indexItem)
    {
        carrinho.updateQtdItem(indexItem, -1);

        return "redirect:/cliente/carrinho/visualizar";
    }

    @GetMapping(value = "somarQtdItem")
    public String somarItemPedido(@ModelAttribute("carrinho") Carrinho carrinho, @RequestParam("index") Integer indexItem)
    {
        carrinho.updateQtdItem(indexItem, +1);

        return "redirect:/cliente/carrinho/visualizar";
    }

    @GetMapping(value = "/remover")
    public String removerItem(
        @RequestParam("itemId") Integer itemId, @RequestParam("index") Integer indexItem, SessionStatus sessionStatus,
        @ModelAttribute("carrinho") Carrinho carrinho, Model model) {

        ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
        carrinho.removeItem(itemCardapio, indexItem);

        if(carrinho.isVazio())
        {
            sessionStatus.setComplete();
        }

        return "redirect:/cliente/carrinho/visualizar";
    }

    @GetMapping(value="/pagamento")
    public String viewPagamento(Model model, @ModelAttribute("carrinho") Carrinho carrinho) 
    {
        if(carrinho == null || carrinho.getItens().size() == 0)
        {
            return "redirect:/cliente/carrinho/visualizar";
        }
        
        model.addAttribute("cartaoCredito", new CartaoCredito());
        return "cliente-pagamento";
    }

    @GetMapping(path = "/refazerCarrinho")
    public String refazerCarrinho(@RequestParam("pedidoId") Integer pedidoId, 
        @ModelAttribute("carrinho") Carrinho carrinho, Model model)
    {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
        carrinho.limpar();

        for(ItemPedido itemPedido : pedido.getItens())
        {
            carrinho.adicionarItem(itemPedido);
        }

        return "cliente-carrinho";
    }

    @GetMapping(path = "/limparCarrinho")
    public String limparCarrinho(@ModelAttribute("carrinho") Carrinho carrinho)
    {
        carrinho.limpar();
        return "redirect:/cliente/carrinho/visualizar";
    } 
}
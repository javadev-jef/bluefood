package br.com.bluefood.infrastructure.web.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import br.com.bluefood.domain.estados.EstadosRepository;
import br.com.bluefood.domain.estados.EstadosBrasileiros;
import br.com.bluefood.domain.restaurante.CategoriaRepository;
import br.com.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.domain.restaurante.RestauranteRepository;
import br.com.bluefood.utils.SecurityUtils;

/**
 * CONTROLLER HELPER
 */
public class ControllerHelper
{
    public static void setEditMode(Model model, boolean isEdit)
    {
        model.addAttribute("editMode", isEdit);
    }

    public static void addEstadosToRequest(EstadosRepository repository, Model model)
    {
        List<EstadosBrasileiros> estados = repository.findAll(Sort.by("sigla"));
        model.addAttribute("estados", estados);
    }

    public static void addCategoriasToRequest(CategoriaRepository repository, Model model)
    {
        List<CategoriaRestaurante> categorias = repository.findAll(Sort.by("descricao"));
        model.addAttribute("categorias", categorias);
    }

    public static void addItensCardapioToRequest(ItemCardapioRepository repository, Model model)
    {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        List<ItemCardapio> itensCardapio = repository.findByRestaurante_IdOrderByNome(restauranteId);
        model.addAttribute("itensCardapio", itensCardapio);
    }

    public static Restaurante getLoggedRestaurante(RestauranteRepository repository)
    {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        return repository.findById(restauranteId).orElseThrow();
    }

    public static Integer getYearOfCurrentDate()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
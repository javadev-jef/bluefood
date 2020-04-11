package br.com.bluefood.infrastructure.web.controller;

import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.bluefood.infrastructure.web.security.Role;
import br.com.bluefood.utils.SecurityUtils;

/**
 * LOGIN CONTROLLER
 */
@Controller
public class LoginController 
{   
    @GetMapping(path = {"/login", "/"})
    public String pageLogin(Model model)
    {
        if(SecurityUtils.loggedUser() == null)
        {
            model.addAttribute("yearFooter", Calendar.getInstance().get(Calendar.YEAR));
            return "login";
        }
        else{
            return verifyLoggedUserAndRedirect();
        }
    }

    @GetMapping("/login-error")
    public String loginError(Model model)
    {
        if(SecurityUtils.loggedUser() == null)
        {
            model.addAttribute("msg", "Usuário ou senha inválidos");
            model.addAttribute("yearFooter", Calendar.getInstance().get(Calendar.YEAR));
            return "login";
        }
        else{
            return verifyLoggedUserAndRedirect();
        }
    }

    private String verifyLoggedUserAndRedirect()
    {
        if(SecurityUtils.loggedUser().getRole() == Role.CLIENTE)
        {
            return "redirect:/cliente/home";
        }
        else if(SecurityUtils.loggedUser().getRole() == Role.RESTAURANTE)
        {
            return "redirect:/restaurante/home";
        }

        throw new IllegalStateException("Erro na autenticação");
    }
}
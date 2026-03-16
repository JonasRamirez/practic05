package com.spring.practica1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.practica1.service.AdminUserService;

@Controller  // ¡IMPORTANTE! Debe ser @Controller, no @RestController
@RequestMapping("/admin/users-view")  // Esta es la URL base
public class UserViewController {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @GetMapping  // Para /admin/users-view
    public String listUsers(Model model) {
        model.addAttribute("users", adminUserService.getAllUsers());
        return "admin/users/list";  // Busca en /templates/admin/users/list.html
    }
    
    @GetMapping("/new")  // Para /admin/users-view/new
    public String showCreateForm(Model model) {
        model.addAttribute("roles", adminUserService.getAllRoles());
        return "admin/users/create";
    }
    
    @GetMapping("/edit/{id}")  // Para /admin/users-view/edit/1
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminUserService.getUserById(id));
        model.addAttribute("roles", adminUserService.getAllRoles());
        return "admin/users/edit";
    }
    
    @GetMapping("/view/{id}")  // Para /admin/users-view/view/1
    public String showViewForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminUserService.getUserById(id));
        return "admin/users/view";
    }
}
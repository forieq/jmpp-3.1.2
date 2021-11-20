package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", roleService.getAllRoles());
        return "/add";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                          @RequestParam("role") List<Integer> role) {
        if (bindingResult.hasErrors()) {
            return "/add";
        }
        user.setRoles(role.stream().map(roleService::getRoleById).collect(Collectors.toSet()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam("role") List<Integer> role) {
        if (bindingResult.hasErrors()) {
            return "/edit";
        }
        user.setRoles(role.stream().map(roleService::getRoleById).collect(Collectors.toSet()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

}

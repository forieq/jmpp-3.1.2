package web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

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

    @GetMapping()
    public String showUsers(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("role") List<Integer> role) {
        user.setRoles(role.stream().map(roleService::getRoleById).collect(Collectors.toSet()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user,
                         @RequestParam("role") List<Integer> role) {
        user.setRoles(role.stream().map(roleService::getRoleById).collect(Collectors.toSet()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}

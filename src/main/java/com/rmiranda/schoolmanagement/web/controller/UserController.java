package com.rmiranda.schoolmanagement.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.rmiranda.schoolmanagement.model.entity.Role;
import com.rmiranda.schoolmanagement.model.entity.User;
import com.rmiranda.schoolmanagement.service.RoleService;
import com.rmiranda.schoolmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @ModelAttribute("roles")
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("")
    public ModelAndView index(@RequestParam(name = "page", defaultValue = "0") int pageNumber, ModelAndView mv) {
        Pageable page = PageRequest.of(pageNumber, 10);
        Page<User> users = userService.getAllUSers(page);
        mv.addObject("users", users);
        mv.setViewName("users/index");
        return mv;
    }

    @GetMapping("/create")
    public ModelAndView create(ModelAndView mv, User user, @ModelAttribute("roles") List<Role> roles) {
        mv.addObject("user", user);
        mv.setViewName("users/create");
        return mv;
    }

    @PostMapping("/create")
    public ModelAndView store(@Valid User user, BindingResult result, ModelAndView mv,
            @ModelAttribute(name = "roles") List<Role> roles, RedirectAttributes attr) {

        List<Role> userRoles = new ArrayList<Role>();

        for (long roleId : user.getRoleIds()) {
            Role role = roleService.getRoleById(roleId);
            userRoles.add(role);
        }

        user.setRoles(userRoles);

        if (result.hasErrors()) {
            mv.setViewName("users/create");
            return mv;
        }

        userService.addUSer(user);

        attr.addAttribute("created", "1");

        mv.setViewName("redirect:/users");

        return mv;
    }

    @GetMapping("/{userId}/edit")
    public ModelAndView edit(@PathVariable(name = "userId") long userId, User user, ModelAndView mv,
            @ModelAttribute(name = "roles") List<Role> roles) {

        mv.setViewName("users/edit");

        user = userService.getUserById(userId).orElse(null);

        if (user == null) {
            mv.setViewName("redirect:/users");
        }

        long[] userRoleIds = new long[user.getRoles().size()];

        int i = 0;
        for (Role userRole : user.getRoles()) {
            userRoleIds[i] = userRole.getId();
            i++;
        }

        user.setRoleIds(userRoleIds);

        mv.addObject("user", user);

        return mv;
    }

    @PostMapping("/{userId}/edit")
    public ModelAndView update(@PathVariable(name = "userId") long userId, @Valid User user, BindingResult result,
            @ModelAttribute(name = "roles") List<Role> roles, ModelAndView mv, RedirectAttributes attr) {

        List<Role> userRoles = new ArrayList<Role>();

        for (long roleId : user.getRoleIds()) {
            Role userrole = roleService.getRoleById(roleId);
            userRoles.add(userrole);
        }

        user.setRoles(userRoles);

        if (result.hasErrors()) {
            mv.setViewName("users/edit");
            return mv;
        }

        userService.updateUser(user);

        attr.addAttribute("updated", "1");
        mv.setViewName("redirect:/users");

        return mv;
    }

    @GetMapping("/{userId}/delete")
    public ModelAndView showDelete(@PathVariable(name = "userId") long userId, User user, ModelAndView mv) {
        user = userService.getUserById(userId).orElse(null);

        if (user == null) {
            mv.setViewName("redirect:/users");
            return mv;
        }

        mv.addObject("user", user);
        mv.setViewName("users/delete");
        return mv;
    }

    @PostMapping("/{userId}/delete")
    public ModelAndView delete(@PathVariable(name = "userId") long userId, User user, ModelAndView mv, RedirectAttributes attr) {

        if (userId > 0) {
            userService.deleteUserById(userId);
        }

        attr.addAttribute("deleted", "1");
        mv.setViewName("redirect:/users");

        return mv;
    }

    @GetMapping("/{userId}/password")
    public ModelAndView showResetPassword(@PathVariable(name = "userId") long userId, ModelAndView mv) {
        User user = userService.getUserById(userId).orElse(null);
        mv.addObject("user", user);
        mv.setViewName("users/password");
        return mv;
    }

    @PostMapping("/{userId}/password")
    public ModelAndView resetPassword(@PathVariable(name = "userId") long userId, @Valid User user, BindingResult result, ModelAndView mv) {

        userService.resetPassword(userId, user.getPassword());

        mv.setViewName("redirect:/users");

        return mv;
    }

    @PostMapping("/{userId}/unlock")
    public ModelAndView unlockUser(@PathVariable(name = "userId") long userId, RedirectAttributes attr, ModelAndView mv) {
        userService.unlockUser(userId);
        attr.addAttribute("unlocked", "1");
        mv.setViewName("redirect:/users");
        return mv;
    }

}
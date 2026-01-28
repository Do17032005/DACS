package com.example.Clothesshoponline.controller;

import com.example.Clothesshoponline.model.Address;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.service.AddressService;
import com.example.Clothesshoponline.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String addressPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        List<Address> addresses = addressService.getUserAddresses(user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("user", user);
        return "address";
    }

    @PostMapping("/save")
    public String saveAddress(@Valid @ModelAttribute Address address,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (result.hasErrors()) {
            List<Address> addresses = addressService.getUserAddresses(user);
            model.addAttribute("addresses", addresses);
            model.addAttribute("user", user);
            model.addAttribute("showModal", true);
            return "address";
        }

        addressService.saveAddress(address, user);
        redirectAttributes.addFlashAttribute("success", "Địa chỉ đã được lưu thành công!");
        return "redirect:/address";
    }

    @GetMapping("/edit/{id}")
    public String editAddress(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        Address address = addressService.getAddressById(id);
        List<Address> addresses = addressService.getUserAddresses(user);
        model.addAttribute("address", address);
        model.addAttribute("addresses", addresses);
        return "address";
    }

    @PostMapping("/set-default/{id}")
    public String setDefaultAddress(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        addressService.setDefaultAddress(id, user);
        return "redirect:/address";
    }

    @PostMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "redirect:/address";
    }
}

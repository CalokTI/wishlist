package com.example.demo.controllers;

import com.example.demo.models.Wish;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.WishlistRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class WishController {

    WishlistRepository wishlistRepository = new WishlistRepository();
    UserRepository userRepository = new UserRepository();

    @GetMapping(value = {"/", "/index"})
    public String renderIndex() {
        return "index.html";
    }

    @PostMapping("/login")
    public String submitLogin(@RequestParam(name = "username") String username, HttpServletRequest request) {

        HttpSession session = request.getSession();

        int userID = userRepository.doesUserExist(username);
        if (userID == -1) {
            userRepository.createNewUser(username);
            userID = userRepository.getLastUserId();
        }

        session.setAttribute("userID", userID);

        return "redirect:/makewish";
    }

    @GetMapping("/makewish")
    public String renderMakeWish(Model model, HttpServletRequest request){

        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");

        ArrayList<Wish> wishList = wishlistRepository.getSingleUserWishlist(userID);
        model.addAttribute("wishList", wishList);
        model.addAttribute("userID", userID);

        return "makeWish.html";
    }

    @PostMapping("/submit")
    public String submitWish(@RequestParam(name = "description") String description, @RequestParam(name = "price") String price, HttpServletRequest request){

        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");

        wishlistRepository.insertWish(userID, description, price);

        return "redirect:/makewish";
    }


    @GetMapping("/wishlist")
    public String renderWishList(@RequestParam(name = "userID") int userID, Model model, HttpServletRequest request) {

        ArrayList<Wish> wishList = wishlistRepository.getSingleUserWishlist(userID);
        model.addAttribute("wishList", wishList);

        model.addAttribute("userID", userID);

        HttpSession session = request.getSession();

        int sessionUserID = -1;
        if (null != session.getAttribute("userID")){
            sessionUserID = (int) session.getAttribute("userID");
        }
        session.setAttribute("wishListUserID", userID);

        model.addAttribute("sessionUserID", sessionUserID);

        return "wishList.html";
    }

    @PostMapping("/deleteWish")
    public String deleteWish(@RequestParam(name = "wishID") int wishID){
        wishlistRepository.deleteWish(wishID);
        return "redirect:/makewish";
    }

    @PostMapping("/reserveWish")
    public String reserveWish(@RequestParam(name = "wishID") int wishID, HttpServletRequest request, RedirectAttributes redirectAttributes){

        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");

        wishlistRepository.addReservation(wishID, userID);

        int wishListUserID = (int) session.getAttribute("wishListUserID");
        redirectAttributes.addAttribute("userID", wishListUserID);

        return "redirect:/wishlist";
    }

    @PostMapping("/removeReservation")
    public String removeReservation(@RequestParam(name = "wishID") int wishID, HttpServletRequest request, RedirectAttributes redirectAttributes){

        HttpSession session = request.getSession();

        wishlistRepository.removeReservation(wishID);

        int wishListUserID = (int) session.getAttribute("wishListUserID");
        redirectAttributes.addAttribute("userID", wishListUserID);

        return "redirect:/wishlist";
    }



}

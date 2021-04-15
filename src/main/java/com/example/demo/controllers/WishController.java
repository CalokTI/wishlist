package com.example.demo.controllers;

import com.example.demo.models.Wish;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.WishlistRepository;
import com.example.demo.services.DatabaseWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class WishController {

    WishlistRepository wishlistRepository = new WishlistRepository();
    DatabaseWriter databaseWriter = new DatabaseWriter();
    UserRepository userRepository = new UserRepository();

    @GetMapping(value= {"/", "/index"})
    public String renderIndex(){
        return "index.html";
    }

    @PostMapping("/login")
    public String submitLogin(@RequestParam(name = "username") String username, HttpServletRequest request){

        HttpSession session = request.getSession();

        int userID = userRepository.doesUserExist(username);
        if(userID == -1) {
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

        return "makeWish.html";
    }

    @PostMapping("/submit")
    public String submitWish(@RequestParam(name = "description") String description, @RequestParam(name = "price") String price, HttpServletRequest request){

        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");

        databaseWriter.insertWish(userID, description, price);

        return "redirect:/makewish";
    }

    @GetMapping("/wishlist")
    public String renderWishList(){

        //todo show wishlist but not for user?? share link with userID ?userID=x
        return "wish_view.html";
    }

}
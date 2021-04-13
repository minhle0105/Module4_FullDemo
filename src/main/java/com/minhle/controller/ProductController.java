package com.minhle.controller;

import com.minhle.model.Product;
import com.minhle.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/list")
    public ModelAndView showAll() {
        Iterable<Product> products = productService.findAll();
        ModelAndView modelAndView = new ModelAndView("/product/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new Product()); // what does this line do?
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createProduct(@ModelAttribute Product product) {
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        ModelAndView modelAndView;
        if (product.isPresent()) {
            modelAndView = new ModelAndView("product/update");
            modelAndView.addObject("product", product);
        }
        else {
            modelAndView = new ModelAndView("product/error-404");
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteConfirmation(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        ModelAndView modelAndView;
        if (product.isPresent()) {
            modelAndView = new ModelAndView("/product/delete");
            modelAndView.addObject("product", product);
        }
        else {
            modelAndView = new ModelAndView("/product/error-404");
        }
        return modelAndView;
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute Product product) {
        productService.remove(product.getId());
        return "redirect:/products/list";
    }

    @GetMapping("/search")
    public ModelAndView searchProductByName(@RequestParam("productNameToSearch") String name) {
        Iterable<Product> searchResult = productService.findByName(name);
        ModelAndView modelAndView = new ModelAndView("product/searchResult");
        modelAndView.addObject("searchResult", searchResult);
        return modelAndView;
    }
}

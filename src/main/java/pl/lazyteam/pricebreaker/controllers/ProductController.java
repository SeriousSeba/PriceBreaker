package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductDAO productDAO;

    @GetMapping("products/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable(value="id") Long id){
        ProductInfo productInfo=productDAO.findOne(id);
        if(productInfo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(productInfo);
    }

    @GetMapping("/products/all")
    public List<ProductInfo> getAllProducts(){
        return productDAO.findAll();
    }



}

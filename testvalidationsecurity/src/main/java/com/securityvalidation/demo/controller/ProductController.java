package com.securityvalidation.demo.controller;

import com.securityvalidation.demo.dtos.ProductDTO;
import com.securityvalidation.demo.servicies.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
        dto = service.insert(dto);
        return ResponseEntity.ok().body(dto);
    }

}

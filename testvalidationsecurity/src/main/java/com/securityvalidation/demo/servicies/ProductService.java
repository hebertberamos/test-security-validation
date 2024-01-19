package com.securityvalidation.demo.servicies;

import com.securityvalidation.demo.dtos.ProductDTO;
import com.securityvalidation.demo.entities.Product;
import com.securityvalidation.demo.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional
    public List<ProductDTO> findAll(){
        List<Product> list = repository.findAll();
        return list.stream().map(prod -> new ProductDTO(prod)).collect(Collectors.toList());
    }

    public ProductDTO insert(ProductDTO dto){
        Product prod = new Product();
        prod.setId(dto.getId());
        prod.setName(dto.getName());
        prod.setPrice(dto.getPrice());

        prod = repository.save(prod);
        return new ProductDTO(prod);
    }

}

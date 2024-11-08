package com.example.prjCRUDSpringBoot.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.prjCRUDSpringBoot.dtos.ProductRecordDto;
import com.example.prjCRUDSpringBoot.models.Products;
import com.example.prjCRUDSpringBoot.repositories.ProductsRepository;

import jakarta.validation.Valid;


@RestController
public class ProductController {
	
	@Autowired
	ProductsRepository productsRepository;
	
	@PostMapping("/products")
	public ResponseEntity<Products> saveProducts(@RequestBody @Valid ProductRecordDto productRecordDto){
		var products = new Products();
		BeanUtils.copyProperties(productRecordDto, products);
		return ResponseEntity.status(HttpStatus.CREATED).body(productsRepository.save(products));	
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Products>> getAllProducts(){
		List<Products> list = productsRepository.findAll();
		if (!list.isEmpty()) {
			for(Products product : list) {
				UUID id = product.getIdProduct();
				product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
			}
	    }
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}	
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id) {
		Optional<Products> productFind = productsRepository.findById(id);
		if (productFind.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		productFind.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(productFind.get());
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id,
												@RequestBody @Valid ProductRecordDto productRecordDto){
		Optional<Products> prod = productsRepository.findById(id);
		if (prod.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
		}
		var products = prod.get();
		BeanUtils.copyProperties(productRecordDto, products);
		return ResponseEntity.status(HttpStatus.OK).body(productsRepository.save(products));
	}
	
	@DeleteMapping("products/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id){
		Optional<Products> prod = productsRepository.findById(id);
		if (prod.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		productsRepository.delete(prod.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
	}
}

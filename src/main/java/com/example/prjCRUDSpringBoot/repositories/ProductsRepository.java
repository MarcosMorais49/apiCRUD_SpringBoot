package com.example.prjCRUDSpringBoot.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prjCRUDSpringBoot.models.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, UUID>{

}

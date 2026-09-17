package com.curso.petshop.repository;
import com.curso.petshop.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PetRepository extends JpaRepository<Pet, Long> {}

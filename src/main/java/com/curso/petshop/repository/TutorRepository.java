package com.curso.petshop.repository;
import com.curso.petshop.domain.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    boolean existsByEmail(String email);
}

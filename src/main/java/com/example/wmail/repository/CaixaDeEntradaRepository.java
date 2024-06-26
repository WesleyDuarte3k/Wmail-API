package com.example.wmail.repository;

import com.example.wmail.controller.CaixaDeEntrada;
import com.example.wmail.controller.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaixaDeEntradaRepository extends JpaRepository<CaixaDeEntrada, Long> {
		Optional<CaixaDeEntrada> findByEmailAddress(String email);
}

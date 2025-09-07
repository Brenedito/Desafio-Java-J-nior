package com.breno.DesafioJunior.Repositories;


import com.breno.DesafioJunior.Models.LoanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, Long> {
}

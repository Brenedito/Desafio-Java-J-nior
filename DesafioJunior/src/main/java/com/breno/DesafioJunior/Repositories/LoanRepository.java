package com.breno.DesafioJunior.Repositories;


import com.breno.DesafioJunior.Models.LoanModel;
import com.breno.DesafioJunior.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanModel, Long> {

    @Query("SELECT COUNT(books) FROM LoanModel books WHERE books.user.user_id = :user_id")
    Integer countByUserId(@Param("user_id") Long userid);

    @Query("SELECT loan FROM LoanModel loan WHERE loan.user.user_id = :userid")
    List<LoanModel> FindAllLoansByUserId(@Param("userid") Long userid);
}

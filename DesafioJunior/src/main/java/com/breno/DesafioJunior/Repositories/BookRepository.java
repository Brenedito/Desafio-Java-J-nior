package com.breno.DesafioJunior.Repositories;

import com.breno.DesafioJunior.Models.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Long> {
}

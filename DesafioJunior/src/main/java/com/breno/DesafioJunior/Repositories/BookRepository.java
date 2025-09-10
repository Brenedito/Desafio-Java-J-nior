package com.breno.DesafioJunior.Repositories;

import com.breno.DesafioJunior.Models.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Long> {

    @Query("SELECT books FROM BookModel books ORDER BY books.book_id ASC")
    List<BookModel> findByOrderByIdAsc(@Param("pageable") Pageable pageable);

    @Query("SELECT books FROM BookModel books WHERE books.book_id > :after ORDER BY books.book_id ASC")
    List<BookModel> findByBookIdGreaterThanOrderByIdAsc(@Param("after") Long after,@Param("pageable") Pageable pageable);
}

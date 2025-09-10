package com.breno.DesafioJunior.Repositories;

import com.breno.DesafioJunior.Models.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("SELECT users FROM UserModel users ORDER BY users.user_id ASC")
    List<UserModel> findByOrderByIdAsc(@Param("pageable") Pageable pageable);

    @Query("SELECT users FROM UserModel users WHERE users.user_id > :after ORDER BY users.user_id ASC")
    List<UserModel> findByUserIdGreaterThanOrderByIdAsc(@Param("after") Long after, @Param("pageable") Pageable pageable);
}

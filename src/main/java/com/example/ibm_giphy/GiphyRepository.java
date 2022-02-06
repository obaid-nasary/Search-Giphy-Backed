package com.example.ibm_giphy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiphyRepository extends JpaRepository<Giphy, Long> {



    @Query("SELECT s FROM Giphy s WHERE s.username = ?1 and s.password = ?2")
    Optional<Giphy> findBy(String username, String password);

}

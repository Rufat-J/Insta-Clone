package com.code.instaclone.repository;


import com.code.instaclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM User u " +
        "WHERE u.username = :username AND u.password = :password", nativeQuery = true)
boolean isValidUser(String username, String password);

}

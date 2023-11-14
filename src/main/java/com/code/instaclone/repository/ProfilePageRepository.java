package com.code.instaclone.repository;


import com.code.instaclone.model.ProfilePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePageRepository extends JpaRepository<ProfilePage, Integer> {
    @Query(value = "SELECT * FROM profile_pages p WHERE p.user_id = :userId", nativeQuery = true)
    Optional<ProfilePage> findByUserId(int userId);
}
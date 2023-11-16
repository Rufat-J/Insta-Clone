package com.code.instaclone.repository;


import com.code.instaclone.model.ProfilePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePageRepository extends JpaRepository<ProfilePage, Integer> {
    @Query(value = "SELECT * FROM profile_pages p WHERE p.user_id = :userId", nativeQuery = true)
    Optional<ProfilePage> findByUserId(int userId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM profile_pages p " +
            "WHERE p.user_id = :userId", nativeQuery = true)
    boolean isProfilePageBelongingToUser(int userId);

    @Query("SELECT pp FROM ProfilePage pp " +
            "JOIN FETCH pp.user u " +
            "LEFT JOIN FETCH pp.images i " +
            "WHERE u.id = :userId")
    Optional<ProfilePage> getUserProfileInfo(@Param("userId") int userId);

}

package com.code.instaclone.repository;

import com.code.instaclone.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true else false END FROM images i " +
            "WHERE i.profile_page_id = :profilePageId AND i.image_id = :imageId", nativeQuery = true)
    boolean isImageBelongingToProfilePage(int profilePageId, int imageId);

    @Query(value = "SELECT * FROM images i WHERE i.image_id = :imageId", nativeQuery = true)
    Optional<Image> findByImageId(int imageId);

    @Query(value = "SELECT name FROM images i WHERE i.image_id = :imageId", nativeQuery = true)
    Optional<String> findNameByImageId(int imageId);
}
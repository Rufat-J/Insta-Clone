package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "mock_images")
public class MockImage {

    @Id
    @Column(name = "mock_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "profile_page_id")
    private ProfilePage profilePage;

    public MockImage(String description, ProfilePage profilePage) {
        this.description = description;
        this.profilePage = profilePage;
    }
}

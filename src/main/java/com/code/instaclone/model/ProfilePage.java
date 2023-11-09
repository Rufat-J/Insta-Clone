package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "profile_pages")
public class ProfilePage {

    @Id
    @Column(name = "profile_page_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profilePageId;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "profilePage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public ProfilePage(String description) {
        this.description = description;
    }
}

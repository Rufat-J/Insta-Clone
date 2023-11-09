package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "profile_pages")
public class ProfilePage {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profilePageId;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "profilePage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MockImage> images;

    public ProfilePage(String description) {
        this.description = description;
    }
}

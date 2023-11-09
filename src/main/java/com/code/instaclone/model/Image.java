package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name="data", nullable = false)
    private byte[] data;

    @Column(name="size")
    private long size;

    @ManyToOne
    @JoinColumn(name = "profile_page_id")
    private ProfilePage profilePage;

    public Image(String description, String name, byte[] data, long size, ProfilePage profilePage) {
        this.description = description;
        this.name = name;
        this.data = data;
        this.size = size;
        this.profilePage = profilePage;
    }
}

package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "images", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Column(name="image_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(name="data", nullable = false)
    private byte[] data;

    @Column(name="size")
    private long size;

    @Column(name="description")
    private String description;

    /*
    @ManyToOne
    @JoinColumn(name="profile_id")
    @Column(name="profile_page", nullable = false)
    private Profilepage profilePage;

     */

    public Image(byte[] data, long size, String description) {
        this.data = data;
        this.size = size;
        this.description = description;
    }
}

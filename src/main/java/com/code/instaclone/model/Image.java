package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

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
    @Basic(fetch = FetchType.LAZY)
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

    public Map<String, Object> toJson() {
        Map<String, Object> resultAsJson = new LinkedHashMap<>();
        resultAsJson.put("image_id", this.id);
        resultAsJson.put("filename", this.name);
        resultAsJson.put("href", "http://localhost:8080/image/download/" + this.id);
        return resultAsJson;
    }
}

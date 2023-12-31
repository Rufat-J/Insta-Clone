package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "size")
    private long size;

    @ManyToOne
    @JoinColumn(name = "profile_page_id")
    private ProfilePage profilePage;

    public Image(MultipartFile file, ProfilePage profilePage) throws IOException {
        this.name = file.getOriginalFilename();
        this.data = file.getBytes();
        this.size = file.getSize();
        this.profilePage = profilePage;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("image_id", this.id);
        result.put("filename", this.name);
        result.put("href", "http://localhost:8080/image/download/" + this.id);
        return result;
    }
}

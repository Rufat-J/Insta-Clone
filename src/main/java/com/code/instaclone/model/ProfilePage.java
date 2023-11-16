package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

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

    public ProfilePage(User user) {
        this.user = user;
        this.description = "This is my profile page";
    }

    public Map<String, Object> toJson() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("profile_page_id", this.profilePageId);
        result.put("description", this.description);
        result.put("user", this.user.toJson());
        result.put("images", getImagesAsJson());
        return result;
    }

    private List<Map<String, Object>> getImagesAsJson() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Image image : this.images) {
            result.add(image.toJson());
        }
        return result;
    }

}

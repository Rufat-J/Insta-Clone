package com.code.instaclone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public ProfilePage(User user) {
        this.user = user;
        this.description = "This is my profile page";
    }

    public Map<String, Object> toJson() {
        Map<String, Object> resultAsJson = new LinkedHashMap<>();
        resultAsJson.put("profile_page_id", this.profilePageId);
        resultAsJson.put("description", this.description);
        resultAsJson.put("user", this.user.toJson());
        resultAsJson.put("images", this.images);
        return resultAsJson;
    }

}

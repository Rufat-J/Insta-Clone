package com.code.instaclone.dto;

import com.code.instaclone.model.Image;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
public class DownloadImageData {

    private Resource resource;
    private Image image;

    public DownloadImageData(Resource resource, Image image) {
        this.resource = resource;
        this.image = image;
    }
}

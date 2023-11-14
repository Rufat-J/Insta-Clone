package com.code.instaclone.dto;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;

public record DeletedImageDto(HttpHeaders headers, ByteArrayResource resource) {
}

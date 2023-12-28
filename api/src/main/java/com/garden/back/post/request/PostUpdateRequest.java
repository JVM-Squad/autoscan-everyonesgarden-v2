package com.garden.back.post.request;

import com.garden.back.post.service.request.PostUpdateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostUpdateRequest(
    String title,
    String content,
    List<String> deleteImages
) {

    public PostUpdateServiceRequest toServiceDto(List<MultipartFile> images) {
        return new PostUpdateServiceRequest(images, title, content, deleteImages);
    }
}

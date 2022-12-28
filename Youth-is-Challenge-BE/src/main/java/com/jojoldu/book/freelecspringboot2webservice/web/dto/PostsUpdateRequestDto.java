package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private Boolean deleteFlag;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String deleteFlag) {
        this.title = title;
        this.content = content;
        this.deleteFlag = Objects.equals(deleteFlag, "true");

    }
}

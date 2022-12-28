package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

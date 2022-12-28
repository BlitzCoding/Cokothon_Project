package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;
    private List<CommentsResponseDto> comments;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor().getName();
        this.modifiedDate = entity.getLastModifiedDate();
        this.comments = entity.getComments().stream().map(CommentsResponseDto::new).collect(Collectors.toList());
    }
}

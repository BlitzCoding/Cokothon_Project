package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import com.jojoldu.book.freelecspringboot2webservice.domain.comments.Comments;

import java.time.LocalDateTime;

public class CommentsResponseDto {
    private Long id;
    private String comment;
    private String name;
    private Long postId;
    private Long userId;
    private LocalDateTime modifiedDate;

    public CommentsResponseDto(Comments comments) {
        this.id = comments.getId();
        this.comment = comments.getComment();
        this.name = comments.getUser().getName();
        this.postId = comments.getPosts().getId();
        this.userId = comments.getUser().getId();
        this.modifiedDate = comments.getLastModifiedDate();
    }
}

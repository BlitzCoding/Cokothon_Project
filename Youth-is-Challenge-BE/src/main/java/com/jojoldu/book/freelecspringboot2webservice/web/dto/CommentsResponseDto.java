package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import com.jojoldu.book.freelecspringboot2webservice.domain.comments.Comments;

import java.time.LocalDateTime;

public class CommentsResponseDto {
    private final Long id;
    private final String comment;
    private final String name;
    private final Long postId;
    private final Long userId;
    private final LocalDateTime modifiedDate;

    public CommentsResponseDto(Comments comments) {
        this.id = comments.getId();
        this.comment = comments.getComment();
        this.name = comments.getUser().getName();
        this.postId = comments.getPosts().getId();
        this.userId = comments.getUser().getId();
        this.modifiedDate = comments.getLastModifiedDate();
    }
}

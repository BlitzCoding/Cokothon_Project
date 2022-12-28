package com.jojoldu.book.freelecspringboot2webservice.web.dto;

import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {
    //특정 id 게시글 조회할 때 게시글 정보 담을 dto
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final String filePath; //img 태그 src에 매핑시켜 로컬 파일 사용
    private final Long userId;
    private final List<CommentsResponseDto> comments;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor().getName();
        this.filePath = entity.getFilePath();
        this.userId = entity.getAuthor().getId();
        this.comments = entity.getComments().stream().map(CommentsResponseDto::new).collect(Collectors.toList());
    }
}

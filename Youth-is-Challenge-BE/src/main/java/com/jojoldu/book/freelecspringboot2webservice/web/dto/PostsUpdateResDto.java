package com.jojoldu.book.freelecspringboot2webservice.web.dto;

        import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
        import lombok.Getter;

@Getter
public class PostsUpdateResDto {
    //게시글 수정페이지에 랜더링할 data 담을 dto

    private final Long id;
    private final String title;
    private final String content;
    private final String fileName;


    public PostsUpdateResDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.fileName = entity.getFileName();
    }
}


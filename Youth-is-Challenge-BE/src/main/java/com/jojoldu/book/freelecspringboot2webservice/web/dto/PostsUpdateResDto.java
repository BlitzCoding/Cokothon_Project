package com.jojoldu.book.freelecspringboot2webservice.web.dto;

        import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
        import lombok.Getter;

@Getter
public class PostsUpdateResDto {
    //게시글 수정페이지에 랜더링할 data 담을 dto

    private Long id;
    private String title;
    private String content;
    private String fileName;


    public PostsUpdateResDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.fileName = entity.getFileName();
    }
}


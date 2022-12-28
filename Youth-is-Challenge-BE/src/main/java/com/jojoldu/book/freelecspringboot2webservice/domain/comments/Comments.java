package com.jojoldu.book.freelecspringboot2webservice.domain.comments;

import com.jojoldu.book.freelecspringboot2webservice.domain.BaseTimeEntity;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comments extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // 댓글 수정을 위한 setter
    public void update(String comment) {
        this.comment = comment;
    }
}

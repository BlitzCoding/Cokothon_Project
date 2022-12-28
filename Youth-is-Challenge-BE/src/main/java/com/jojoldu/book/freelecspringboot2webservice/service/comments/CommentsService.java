package com.jojoldu.book.freelecspringboot2webservice.service.comments;

import com.jojoldu.book.freelecspringboot2webservice.domain.comments.CommentRepository;
import com.jojoldu.book.freelecspringboot2webservice.domain.comments.Comments;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.PostRepository;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.UserRepository;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.CommentsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentsService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long commentSave(Long id, CommentsRequestDto commentsRequestDto, Long userId) {
        User user = userRepository.getById(userId);
        Posts posts = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."+ id));

        commentsRequestDto.setUser(user);
        commentsRequestDto.setPosts(posts);

        Comments comments = commentsRequestDto.toEntity();
        commentRepository.save(comments);

        return commentsRequestDto.getId();
    }

    @Transactional
    public void commentUpdate(Long id, CommentsRequestDto commentsRequestDto) {
        Comments comments = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."+ id));
        comments.update(commentsRequestDto.getComment());
    }

    @Transactional
    public void commentDelete(Long id) {
        Comments comments = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."+ id));
        commentRepository.delete(comments);
    }
}

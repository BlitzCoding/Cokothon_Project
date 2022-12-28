package com.jojoldu.book.freelecspringboot2webservice.web;

import com.jojoldu.book.freelecspringboot2webservice.config.auth.LoginUser;
import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.service.posts.PostsService;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsListResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostsApiController {

    private final PostsService postsService;

    //게시판 등록 기능
    @PostMapping("/save")
    public Long save(@RequestPart(name = "p") PostsSaveRequestDto requestDto, @RequestPart(name = "f", required = false) MultipartFile file
            ,@LoginUser SessionUser user) throws IOException {
        return postsService.save(requestDto, file, user);
    }

    //게시판 목록 조회
    @GetMapping
    public List<PostsListResponseDto> getPostsList(){
        return postsService.findAllDesc();
    }

    //게시글 수정
    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestPart(name = "p") PostsUpdateRequestDto requestDto,
                       @RequestPart(name = "f", required = false) MultipartFile file,
                       @LoginUser SessionUser accessUser) {
        try{
            postsService.update(id,requestDto,file,accessUser);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    //게시판 조회 기능
    @GetMapping("/{id}")
    public PostsResponseDto read(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }
}

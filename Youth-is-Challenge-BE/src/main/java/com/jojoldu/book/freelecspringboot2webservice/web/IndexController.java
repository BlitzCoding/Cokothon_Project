package com.jojoldu.book.freelecspringboot2webservice.web;

import com.jojoldu.book.freelecspringboot2webservice.config.auth.LoginUser;
import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.service.posts.PostsService;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.CommentsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsResponseDto;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.PostsUpdateResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/mentoring")
    public String mentoring() {
        return "mentoring";
    }
    @GetMapping("/mentoring2")
    public String mentoring2() {
        return "mentoring2";
    }
    @GetMapping("/mentoring3")
    public String mentoring3() {
        return "mentoring3";
    }
    @GetMapping("/community")
    public String communityPage(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        //findAllDesc()로 가져온 결과를 posts 객체로 -> index에 전달

        if (user != null) { //세션에 저장된 값이 있을 경우에만 model에 userName 등록
            model.addAttribute("loginUserName", user.getName());
        }

        return "community";
    }

    @GetMapping("/information")
    public String informationPage() {
        return "information";
    }

    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/posts/save")
    public String postSave(){
        return "post-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, @LoginUser SessionUser user, Model model) {
        PostsUpdateResDto dto = postsService.findForUpdate(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/posts/read/{id}")
    public String postRead(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        PostsResponseDto postsResponseDto = postsService.findById(id);
        List<CommentsResponseDto> comments = postsResponseDto.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        if (user != null) {
            model.addAttribute("user", user);

            if (postsResponseDto.getUserId().equals(user.getUserId())) {
                model.addAttribute("writer", true);
            }
        }
        model.addAttribute("post", postsResponseDto);
        return "posts-read";
    }

}

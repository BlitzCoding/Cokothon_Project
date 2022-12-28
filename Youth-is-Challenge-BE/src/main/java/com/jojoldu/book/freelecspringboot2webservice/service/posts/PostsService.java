package com.jojoldu.book.freelecspringboot2webservice.service.posts;


import com.jojoldu.book.freelecspringboot2webservice.config.auth.dto.SessionUser;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.PostRepository;
import com.jojoldu.book.freelecspringboot2webservice.domain.posts.Posts;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.User;
import com.jojoldu.book.freelecspringboot2webservice.domain.user.UserRepository;
import com.jojoldu.book.freelecspringboot2webservice.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.h2.mvstore.FreeSpaceBitSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, MultipartFile file ,SessionUser user) throws IOException {
        User author = userRepository.getById(user.getUserId());
        String saveName = saveFile(file);

        return postRepository.save(Posts.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(author)
                .filePath("/images/" + saveName)
                .fileName(saveName)
                .build()).getId();
    }

    @Transactional
    public void update(Long id, PostsUpdateRequestDto requestDto, MultipartFile file, SessionUser accessUser) throws IOException {
        Posts posts = postRepository.findById(id) //수정할 객체 id로 찾아서
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        System.out.println(requestDto.getDeleteFlag());
        //내용 먼저 업데이트
        posts.update_content(requestDto.getTitle(),requestDto.getContent());
        //3. file이 null인지 확인
        if (requestDto.getDeleteFlag()) {
            if(fileDelete(posts)) { //실제 파일 삭제(성공)
                if (file == null){
                    //엔티티 업데이트(이미지 제거)
                    posts.update_File(null, null);
                }
                else{ //변경할 파일이 같이 넘어온 경우
                    String fileName = saveFile(file);
                    posts.update_File("/images/"+fileName, fileName);
                }
            }}
        else {//flag = false
            if(file == null){

            }
            else{//새로운 파일 존재 = 기존파일 삭제
                fileDelete(posts);
                String fileName = saveFile(file);
                posts.update_File("/images/"+fileName, fileName);
            }
        }
    }

    public Boolean fileDelete(Posts post){
        File file = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\static\\images\\" + post.getFileName());
        if (file.exists()) {
            file.delete();
            return true;
        }
        else{
            System.out.println("파일 없음"+post.getFileName());
            return false;
        }

    }

    public String saveFile(MultipartFile file) throws IOException{
        String storePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images";
        File storeFolder = new File(storePath);
        //헤당 폴더 없는 경우에만 생성
        if (!storeFolder.exists()) {
            try {
                storeFolder.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        //고유 식별자 생성
        UUID uuid = UUID.randomUUID();
        String fileName = null;
        if (file != null) { //파일을 첨부된 경우만 이름 꺼냄
            fileName = uuid + "_"+ file.getOriginalFilename();
            file.transferTo(new File(storePath, fileName));
        }
        return fileName;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        //찾은 엔티티 data Dto에 담아 반환 (view에 뿌릴거니까 Dto)
        return new PostsResponseDto(entity);
    }

    public PostsUpdateResDto findForUpdate (Long id) {
        Posts entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        //찾은 엔티티 data Dto에 담아 반환 (view에 뿌릴거니까 Dto)
        return new PostsUpdateResDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postRepository.findById(id) //id로 해당 data 찾기
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));
                //게시글 없을 경우 예외처리
        if (posts.getFileName() != null) {
            fileDelete(posts);
        }
        postRepository.delete(posts);
    }

    //글 수정할 때 작성자인지 확인
//    public Boolean isAuthor(Long userId, Long postId){
//        Posts post = postRepository.findById(postId)
//                .orElseThrow(()-> new NullPointerException());
//        if (userId.equals(post.getAuthor().getId())) {
//            //System.out.println("1열람자"+userId+"     1글 작성자: "+post.getAuthor().getId());
//            return true;
//        }
//        //System.out.println("2열람자"+userId+"    2글 작성자: "+post.getAuthor().getId());
//        return false;
//    }
}

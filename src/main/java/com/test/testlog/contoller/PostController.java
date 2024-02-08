package com.test.testlog.contoller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.testlog.request.PostCreate;
import com.test.testlog.request.PostEdit;
import com.test.testlog.request.PostSearch;
import com.test.testlog.response.PostResponse;
import com.test.testlog.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 2024.01.25 데이터를 검증하는 이유 -> 검증을 하면 안전하게 믿고 저장할 수 있다.
 * 1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
 * 2. client bug로 값이 누락될 수 있다.
 * 3. 외부에 나쁜 사람이 값을 의미로 조작해서 보낼 수 있다.
 * 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
 * 5. 서버 개발자의 편안함을 위해
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    /**
     * Response 응답 형태
     * case1. 저장한 데이터 Entity
     * case2. 저장한 데이터의 primary_id
     *  -> Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
     * case3. 응답 필요 없음 -> 클라이언트에서 모든 글 데이터 context를 잘 관리함
     * BAD CASE : 서버에서 @@@ 이렇게 할겁니다! 라고 고정하지 말기
     *  -> 서버에서 차라리 유연하게 대응하는 것이 좋은데 대신 코드를 잘 짜야한다 ^^;
     *  -> 한번에 일고라적으로 잘 처리되는 케이스는 없다. => 잘 관리하는 형태가 중요하다 !
     */
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request/*, BindingResult result*/) {
        log.info("request={}", request);

        postService.write(request);
        /*
        String title = request.getTitle();
        if (title == null || title.equals("")) {  // 빈 String 값은 ? 더 검증해야 할게 생각보다 더 있을걸?
            // 이런 형태의 검증은 검증할 데이터가 많아지면 똑같은 코드가 너무 많이 들어가게 도니다.
            // -> 무언가 3번 이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닐지 의심한다. (개발 tip)
            // -> 필드가 100개가 된다고 하면 검증 누락 가능성이 있다.
            // -> 생각보다 검증해야 할게 많다. (꼼꼼하지 않을 수 있다.)
            // -> 중요? 뭔가 개발자스럽지가 않다 ㅋㅋ
            throw new IllegalArgumentException("Title 값은 필수입니다.");
        }
        String content = request.getContent();
        if (content == null || content.equals("")) {
            throw new IllegalArgumentException("");
        }
         */

        /*
         1. 매번 메서드마다 값을 검증해야 한다.
         -> 개발자가 까먹을 수 있다.
         -> 검증 부분에서 버그가 발생할 여지가 높다.
         -> 지겹다 ^^;
         2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
         3. 여러개의 에러 처리 힘듬
         4. 3번 이상의 반복작업은 피해야 한다!! -> 자동화 고려
         ->  코드 && 개발에 관한 모든 것
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);

            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();
            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }
         */

    }

    /**
     * /posts/{postId} : 글 한개 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     * 조회 API
     * 여러 개의 글을 조회하는 API
     *
     * @return
     */
    @GetMapping("/posts")
    public List<PostResponse> getList() {
        return postService.getList();
    }

    @GetMapping("/v2/posts")
    public List<PostResponse> getList(@RequestParam int page) {
        return postService.getList(page);
    }

    @GetMapping("/v3/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }
    
    /**
     * 게시글 수정
     * @param postId
     * @param postEdit
     * @return
     */
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit)
    {
        return postService.edit(postId, postEdit);
    }
    
    /**
     * 게시글 삭제
     * @param postId
     */
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId)
    {
        postService.delete(postId);
    }

    // TODO 인증, 예외 처리...


}

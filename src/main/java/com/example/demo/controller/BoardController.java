package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Image;
import com.example.demo.service.etc.ImageService;
import com.example.demo.entity.Board;
import com.example.demo.entity.Member;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import com.example.demo.service.etc.IpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final IpService ipService;

    /**
     * 글 목록보기
     */
    @RequestMapping(method = RequestMethod.GET)
    public String board(
            Model model,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BoardDto.getQuery> page = boardService.getBoardList(pageable);

        List<BoardDto.getQuery> boardDTOList = page.getContent();

        BoardDto.page boardPageDto = BoardDto.page.builder()
                .total(page.getTotalPages())
                .page(page.getNumber())
                .build();

        model.addAttribute("boardDTOList", boardDTOList);
        model.addAttribute("boardPageDto", boardPageDto);
        return "board/board";
    }

    /**
     * 글 상세보기
     */
    @RequestMapping(value = "/getBoard/{id}", method = RequestMethod.GET)
    public String getBoard(
            @PathVariable(name = "id") Long id,
            Model model) throws Exception {
        //id 로 board 를 찾고 getBoardDto 에 필요한 값을 넣고 생성한다.
        Board board = boardService.findById(id);
        String email;
        if (Optional.ofNullable(board.getMember()).isEmpty()) email = null;
        else email = board.getMember().getEmail();

        BoardDto.get getBoardDto = BoardDto.get.builder().
                id(board.getId()).
                email(board.getEmail()).
                title(board.getTitle()).
                content(board.getContent()).
                nickname(board.getNickname()).build();

        //board 의 commentList 를 구하고 board 에 댓글이 있을 경우에만 실행
        List<Comment> commentList = board.getCommentList();
        List<CommentDto.get> getCommentDtoList = new ArrayList<>();
        BoardDto.page pageDto = new BoardDto.page();
        if (!commentList.isEmpty()) {
            //pages = 0 : startId, 1 : limit, 2 : totalPage
            List<Integer> pages = findCommentStartIdAndLimit(0, 50, board.getCommentList());
            //댓글이 startId 에서 시작해서 limit 로 끝나는 Query 로 filter_commentList 를 구한다.
            List<Comment> filter_commentList = commentService.getCommentList(pages.get(0), pages.get(1), board);
            //filter_commentList 를 getCommentDtoList 로 바꾸는 작업을 한다.(또한 댓글들을 올바르게 정렬)
            getCommentDtoList = convertGetCommentListDto(filter_commentList);
            pageDto.setTotal(pages.get(2));
        }

        model.addAttribute("getBoardDto", getBoardDto);
        model.addAttribute("commentList", getCommentDtoList);
        model.addAttribute("commentDto", new CommentDto.get());
        model.addAttribute("commentPageDto", pageDto);
        model.addAttribute("boardId", id);

        return "board/getBoard";
    }

    @RequestMapping(value = "/getBoard/{id}/{commentPage}", method = RequestMethod.GET)
    public String getBoardCommentPagination(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "commentPage") int page,
            Model model) throws Exception {
        if (page < 0) page = 0;
        //id 로 board 를 찾고 getBoardDto 에 필요한 값을 넣고 생성한다.
        Board board = boardService.findById(id);

        BoardDto.get getBoardDto = BoardDto.get.builder().
                id(board.getId()).
                email(board.getEmail()).
                title(board.getTitle()).
                content(board.getContent()).
                nickname(board.getNickname()).build();

        //board 의 commentList 를 구하고 board 에 댓글이 있을 경우에만 실행
        List<Comment> commentList = board.getCommentList();
        List<CommentDto.get> getCommentDtoList = new ArrayList<>();
        BoardDto.page pageDto = new BoardDto.page();
        if (!commentList.isEmpty()) {
            //pages = 0 : startId, 1 : limit, 2 : totalPage
            List<Integer> pages = findCommentStartIdAndLimit(page, 50, board.getCommentList());
            //댓글이 startId 에서 시작해서 limit 로 끝나는 Query 로 filter_commentList 를 구한다.
            List<Comment> filter_commentList = commentService.getCommentList(pages.get(0), pages.get(1), board);
            //filter_commentList 를 getCommentDtoList 로 바꾸는 작업을 한다.(또한 댓글들을 올바르게 정렬)
            getCommentDtoList = convertGetCommentListDto(filter_commentList);
            pageDto.setPage(page);
            pageDto.setTotal(pages.get(2));
        }

        model.addAttribute("getBoardDto", getBoardDto);
        model.addAttribute("commentList", getCommentDtoList);
        model.addAttribute("commentDto", new CommentDto.get());
        model.addAttribute("commentPageDto", pageDto);
        model.addAttribute("boardId", id);
        return "board/getBoard";
    }

    /**
     * 글쓰기
     */
    @RequestMapping("/post")
    public String post(Model model, HttpServletRequest request) {
        String id = UUID.randomUUID().toString();
        BoardDto.post postBoardDto = new BoardDto.post();
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (Optional.ofNullable(inputFlashMap).isPresent()) {
            id = (String) inputFlashMap.get("id");
            postBoardDto = (BoardDto.post) inputFlashMap.get("postBoardDto");
        }

        model.addAttribute("id", id);
        model.addAttribute("postBoardDto", postBoardDto);
        return "board/post";
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String addPost(
            @Validated @ModelAttribute BoardDto.post postBoardDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal Member member,
            @RequestParam String id,
            RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        //로그인 했다면 닉네임, 패스워드에 검증이 통과할 수 있도록 값을 넣어준다.
        if (Optional.ofNullable(member).isPresent()) {
            postBoardDto.setNickname(member.getNickname());
            postBoardDto.setPassword(UUID.randomUUID().toString());
        }
        //에러가 생길 수 있는 필드와 그에 맞는 메시지를 DTO 를 만들고 거기에 저장한다.
        Map<String, String> errors = new HashMap<>();
        errors.put("title", "제목 값이 비어있습니다.");
        errors.put("nickname", "닉네임 값이 비어있습니다.");
        errors.put("password", "비밀번호 값이 비어있습니다.");
        errors.put("content", "내용 값이 비어있습니다.");
        //그 필드 검증에 에러가 생기면 errorMessage 를 redirect 해서 다시 보낸다.
        for (String key : errors.keySet()) {
            if (bindingResult.hasFieldErrors(key)) {
                redirectErrorMessage(redirectAttributes, errors.get(key), postBoardDto);
                redirectAttributes.addFlashAttribute("id", id);
                return "redirect:/board/post";
            }
        }
        //로그인을 안했다면 닉네임 옆에 IP를 붙여준다.
        if (Optional.ofNullable(member).isEmpty()) {
            postBoardDto.setNickname(postBoardDto.getNickname() + "(" + ipService.getClientIP(request) + ")");
        }

        //글쓰기 성공
        List<Image> imageList = imageService.findListByUUID(id);
        Board board = boardService.findById(boardService.save(postBoardDto, member, id));
        imageService.imageListSetBoard(board, imageList);
        return "redirect:/board";
    }

    /**
     * 글수정
     */
    @RequestMapping("/edit/{boardId}")
    public String edit(
            @PathVariable(name = "boardId") Long boardId,
            @AuthenticationPrincipal Member member,
            Model model, HttpServletRequest request) throws Exception {
        Board board = boardService.findById(boardId);

        //회원이 쓴 글이면 로그인이 안되있거나 글을 쓴 회원과 동일아이디가 아닐 경우 에러발생
        if (Optional.ofNullable(board.getEmail()).isPresent()) {
            if (Optional.ofNullable(member).isEmpty() || !member.getEmail().equals(board.getEmail())) {
                return "error/403";
            }
        }
        //nickname ip 제거
        String nickname = ipService.deleteClientIP(board.getNickname());
        //redirect 인 경우 현재 글을 가져오고, 아닌 경우 데이터베이스에 있는 board 데이터를 가지고 온다.
        BoardDto.post postBoardDto;
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (Optional.ofNullable(inputFlashMap).isPresent()) {
            postBoardDto = (BoardDto.post) inputFlashMap.get("postBoardDto");
        } else {
            postBoardDto = BoardDto.post.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .uuid(board.getUuid())
                    .nickname(nickname)
                    .password(board.getPassword())
                    .build();
        }

        model.addAttribute("postBoardDto", postBoardDto);
        model.addAttribute("boardId", boardId);

        return "board/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editPost(
            @Validated @ModelAttribute BoardDto.post postBoardDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal Member member,
            @RequestParam Long boardId,
            Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        Board board = boardService.findById(boardId);
        postBoardDto.setUuid(board.getUuid());

        Map<String, String> errors = new HashMap<>();
        errors.put("title", "제목 값이 비어있습니다.");
        errors.put("nickname", "닉네임 값이 비어있습니다.");
        errors.put("password", "비밀번호 값이 비어있습니다.");
        errors.put("content", "내용 값이 비어있습니다.");
        //그 필드 검증에 에러가 생기면 errorMessage 를 redirect 해서 다시 보낸다.
        for (String key : errors.keySet()) {
            if (bindingResult.hasFieldErrors(key)) {
                redirectErrorMessage(redirectAttributes, errors.get(key), postBoardDto);
                return "redirect:/board/edit/" + boardId;
            }
        }

        //자신의 글이 맞는지 확인, 로그인이 안돼있으면 비밀번호 확인
        if (StringUtils.isEmpty(board.getEmail())) {
            if (!board.getPassword().equals(postBoardDto.getPassword())) {
                redirectErrorMessage(redirectAttributes, "비밀번호가 맞지 않습니다.", postBoardDto);
                return "redirect:/board/edit/" + boardId;
            }
        } else {
            if (Optional.ofNullable(member).isEmpty() || !board.getEmail().equals(member.getEmail())) {
                redirectErrorMessage(redirectAttributes, "글을 수정할 권한이 없습니다.", postBoardDto);
                return "error/403";
            }
        }

        //login 안했으면 nickname 에 ip 부여
        if (Optional.ofNullable(member).isEmpty()) {
            postBoardDto.setNickname(postBoardDto.getNickname() + "(" + ipService.getClientIP(request) + ")");
        }

        //글수정 성공
        boardService.putBoard(postBoardDto, board);
        List<Image> imageList = imageService.findListByUUID(board.getUuid());
        imageService.imageListSetDeleted(imageList);
        imageService.imageListSetBoard(board, imageList);
        return "redirect:/board";
    }

    /**
     * 글삭제
     */
    @RequestMapping(value = "/delete/{boardId}")
    public String delete(
            @PathVariable(name = "boardId") Long boardId,
            @AuthenticationPrincipal Member member,
            Model model) throws Exception {
        Board board = boardService.findById(boardId);

        //자신의 글이 맞는지 확인
        if (Optional.ofNullable(board.getEmail()).isPresent()) {
            if (Optional.ofNullable(member).isEmpty()) {
                if (!member.getEmail().equals(board.getEmail())) {
                    model.addAttribute("errorMessage", "글을 삭제할 권한이 없습니다");
                    return "error/403";
                }
            }
        }

        model.addAttribute("boardId", boardId);
        model.addAttribute("boardEmail", board.getEmail());
        model.addAttribute("deleteBoardDto", new BoardDto.delete());
        return "board/delete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deletePost(
            @ModelAttribute BoardDto.delete deleteBoardDto,
            @RequestParam Long boardId,
            @AuthenticationPrincipal Member member,
            Model model, RedirectAttributes redirectAttributes) throws Exception {
        Board board = boardService.findById(boardId);

        //자신의 글이 맞는지 확인
        if (Optional.ofNullable(board.getEmail()).isPresent()) {
            if (Optional.ofNullable(member).isEmpty()) {
                if (!member.getEmail().equals(board.getEmail())) {
                    model.addAttribute("errorMessage", "글을 삭제할 권한이 없습니다");
                    return "error/403";
                }
            }
        } else {
            if (!board.getPassword().equals(deleteBoardDto.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 맞지 않습니다.");
                return "redirect:/board/delete/" + boardId;
            }
        }

        boardService.deleteBoard(board);
        return "redirect:/board";
    }

    /**
     * Controller 로직 부분
     */
    //errorMessage 를 redirect 속성에 담는 메서드
    private void redirectErrorMessage(
            RedirectAttributes redirectAttributes,
            String errorMessage, Object o) {
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        redirectAttributes.addFlashAttribute("postBoardDto", o);
    }
    /**
     * comment 의 startId 와 limit 를 구하는 메소드
     * @param page  현재 page
     * @param size  현재 size
     * @param commentList 현재 board 안에 들어있는 commentList
     * @return List<Integer> 0 : startId, 1 : limit
     */
    private List<Integer> findCommentStartIdAndLimit(int page, int size, List<Comment> commentList) {
        if (commentList.isEmpty()) return null;
        Map<Long, Integer> commentCount = new LinkedHashMap<>();
        //대댓글이 아닌 댓글을 표시하는 groupId 를 key, 그 안에 대댓글이 얼마나 들어 있는지 세는 count 를 value 로 놓는다.
        for (Comment comment : commentList) {
            commentCount.put(comment.getGroupId(), commentCount.getOrDefault(comment.getGroupId(), 0) + 1);
        }
        //map 을 key 로 정렬한다.
        Map<Long, Integer> result = sortMapByKey(commentCount);
        //앞에서 구한 map 을 가지고 시작할 commentId, Limit 를 가진 List 를 반환한다.
        return getCommentStartIdAndLimit(page, size, result);
    }

    private static LinkedHashMap<Long, Integer> sortMapByKey(Map<Long, Integer> map) {
        List<Map.Entry<Long, Integer>> entries = new LinkedList<>(map.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        LinkedHashMap<Long, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private List<Integer> getCommentStartIdAndLimit(int page, int size, Map<Long, Integer> result) {
        int presentPage = -1;
        int presentLimit = size;
        int sum = 0;
        Long startId = null;

        for (Map.Entry<Long, Integer> entry : result.entrySet()) {
            //page 에서 처음 시작했을때 prentPage 를 올리고 현재 페이지인 경우 첫번째 id 를 가져온다.
            if (sum == 0) {
                presentPage++;
                if (presentPage == page) {
                    startId = entry.getKey();
                }
            }
            //조건이 만족할 때 까지 sum 에 대댓글이 포함된 댓글의 수를 더한다.
            sum += entry.getValue();
            //현재 page 의 limit 를 sum 이 넘으면 page 를 추가한다.
            if (size <= sum) {
                //현재 page 인 경우 presentLimit 를 현 페이지의 댓글 수인 sum 만큼 limit 를 지정한다.
                if (presentPage == page) {
                    presentLimit = sum;
                }
                sum = 0;
            }
            //끝까지 돌리는 이유는 totalPage 를 구하기 위해서다.
        }
        List<Integer> output = new ArrayList<>();
        output.add(startId.intValue());
        output.add(presentLimit);
        output.add(presentPage);

        return output;
    }

    /**
     * Comment 를 GetCommentDto 로 바꾸는 메소드
     * @param commentList Comment 객체가 담겨 있는 리스트
     * @return GetCommentDto 객체가 담겨 있는 리스트
     */
    private List<CommentDto.get> convertGetCommentListDto(List<Comment> commentList) throws Exception {

        List<CommentDto.get> result = new ArrayList<>();
        //Comment 를 GetCommentDto 로 바꾸고 GetCommentDto.children 에 자식 댓글들을 저장해 중첩 구조로 만든다.
        //comments 에 대댓글이 아닌 댓글들이 저장되어 있다.
        List<CommentDto.get> comments = createNestedStructure(commentList);
        //comments 에서 재귀함수로 댓글들을 순서대로 result 에 넣는다.
        recursionNestedComment(comments, result);
        return result;
    }

    private List<CommentDto.get> createNestedStructure(List<Comment> commentList) throws Exception {
        List<CommentDto.get> result = new ArrayList<>();
        Map<Long, CommentDto.get> map = new HashMap<>();

        for (Comment comment : commentList) {
            CommentDto.get commentDto = new CommentDto.get();
            commentDto.convertFrom(comment);

            map.put(commentDto.getId(), commentDto);
            //대댓글이 아닌 경우 result list 에 추가한다. 맞을 경우 부모 댓글 dto 의 children 에 저장한다
            if (Optional.ofNullable(comment.getParentComment()).isPresent()) {
                map.get(comment.getParentComment().getId()).getChildren().add(commentDto);
            } else result.add(commentDto);
        }
        //depth 0 인 대댓글이 아닌 댓글들을 저장한 리스트를 반환한다.
        return result;
    }

    private void recursionNestedComment(List<CommentDto.get> commentList, List<CommentDto.get> result) {
        for (CommentDto.get comment : commentList) {
            result.add(comment);
            if (!Optional.ofNullable(comment.getChildren()).isEmpty()) {
                recursionNestedComment(comment.getChildren(), result);
            }
        }
    }
}

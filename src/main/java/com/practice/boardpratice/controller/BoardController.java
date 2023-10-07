package com.practice.boardpratice.controller;

import com.practice.boardpratice.Weather.Weather;
import com.practice.boardpratice.domain.Board;
import com.practice.boardpratice.domain.Comment;
import com.practice.boardpratice.domain.Member;
import com.practice.boardpratice.dto.MemberForm;
import com.practice.boardpratice.dto.PrincipalDetails;
import com.practice.boardpratice.dto.WeatherInfo;
import com.practice.boardpratice.service.BoardService;
import com.practice.boardpratice.service.CommentService;
import com.practice.boardpratice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.minidev.json.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;
    private final Weather weather;

    public BoardController(BoardService boardService, Weather weather, UserService userService, CommentService commentService) {
        this.boardService = boardService;
        this.userService = userService;
        this.commentService = commentService;
        this.weather = weather;
    }

    //메인화면
    @GetMapping("/")
    public String home(Model model, Authentication authentication,
                       @RequestParam(value = "page", defaultValue = "1") int page) throws IOException, ParseException {
        Page<Board> boards = boardService.getAllBoard(page-1);
        model.addAttribute("boards", boards);

        if(authentication != null) {
            Member member = memberInfo(authentication);
            model.addAttribute("nickname", member.getNickname());
        }
        List<String> weather = Weather.lookUpWeather();
        weather.toArray();

        ArrayList<String> tmp = new ArrayList<>();
        ArrayList<String> pcp = new ArrayList<>();
        ArrayList<WeatherInfo> weatherInfo = new ArrayList<>();
        WeatherInfo weatherInfo1 = new WeatherInfo();

        for (int i = 1; i < weather.size(); i++) {
            if (i % 2 == 0) {
                weatherInfo1.setPcp(weather.get(i-1));
                weatherInfo1.setTmp(weather.get(i));
                weatherInfo.add(weatherInfo1);
            }
        }

        model.addAttribute("weatherInfo", weatherInfo);
        return "main/home";
    }

    @GetMapping("login")
    public String login() {
        return "main/login";
    }

    @GetMapping("login/google")
    public String loginGoogle() {
        return "main/login";
    }

    @GetMapping("register")
    public String register(MemberForm memberForm) {
        return "main/register";
    }

    @PostMapping("register")
    public String register(@Valid MemberForm memberForm,
                           Errors errors,
                           Model model){

        if (errors.hasErrors()) {
            model.addAttribute("memberForm", memberForm);
            // 유효성 통과 못한 필드의 에러메세지를 리턴함
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "main/register";
        }

        System.out.println(memberForm.getEmail());

        userService.UserRegister(memberForm);

        return "redirect:/";
    }

    @GetMapping("create-board")
    public String createBoard() {
        return "board/createBoard";
    }

    @PostMapping("create-board")
    public String createBoard(@Valid @RequestParam String title,
                           @Valid @RequestParam String word,
                              Authentication authentication) throws IOException {

        Member member = memberInfo(authentication);

        boardService.createBoard(title, word, member);

        return "redirect:/";
    }

    @GetMapping("boardDetail")
    public String boardDetail(@RequestParam("bno") long bno, Model model,
                              Authentication authentication) {
        Board board = boardService.findById(bno).orElseThrow();
        model.addAttribute("board", board);

        Member member = memberInfo(authentication);
        model.addAttribute("nickname", member.getNickname());

        List<Comment> comment = commentService.findAllCommentByBoardId(bno);
        model.addAttribute("comment", comment);

        return "board/boardDetail";
    }

    @PostMapping("write-comment")
    public String boardDetail(@RequestParam("comment") String comment,
                              @RequestParam long bno,
                              Authentication authentication) {
        Member member = memberInfo(authentication);
        Board board = boardService.findById(bno).orElseThrow();

        commentService.saveComment(comment, member, board);

        return "redirect:/boardDetail?bno="+bno;
    }

    @GetMapping("modify-board")
    public String modifyBoard(@RequestParam long bno, Model model) {
        Board board = boardService.findById(bno).orElseThrow();
        model.addAttribute("board", board);

        return "board/modifyBoard";
    }

    @PostMapping("modify-board")
    public String modifyBoard(@Valid @RequestParam String title,
                              @Valid @RequestParam String word,
                              @RequestParam long bno) throws IOException {
        boardService.modifyBoard(bno, title, word);

        return "redirect:boardDetail?bno="+bno;
    }

    @GetMapping("delete-board")
    public String deleteBoard(@RequestParam long bno) {
        boardService.deleteBoard(bno);
        return "redirect:/";
    }

    @GetMapping("delete-comment")
    public String deleteComment(@RequestParam long cno, HttpServletRequest request) {
        commentService.deleteComment(cno);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("search")
    public String search(@RequestParam String search, Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                         Authentication authentication) {
        Page<Board> boards = boardService.findByBoardName(search, page-1);
        model.addAttribute("boards", boards);

        System.out.println(boards);

        if(authentication != null) {
            Member member = memberInfo(authentication);
            model.addAttribute("nickname", member.getNickname());
        }

        return "main/home";
    }


    @GetMapping("logingo")
    public String hi(Principal principal, Model model) {
        int a = 0;
        return "main/hi";
    }

    private Member memberInfo(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = userService.findByUserEmail(principal.getUsername());
        return member;
    }
}

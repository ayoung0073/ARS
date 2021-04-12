package com.may.ars.controller.api;

import com.may.ars.dto.problem.ProblemRegisterDto;
import com.may.ars.model.entity.member.Member;
import com.may.ars.utils.AuthCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProblemApiController {

    @AuthCheck
    @PostMapping("/problem")
    public String saveProblem(@RequestBody ProblemRegisterDto registerDto, Member member) {
        log.info("글 등록");
        log.info("글 작성자 : " + member.getNickname());
        log.info(registerDto.toString());

        return "redirect:/";
    }
}

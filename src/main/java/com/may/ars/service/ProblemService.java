package com.may.ars.service;

import com.may.ars.domain.member.Member;
import com.may.ars.dto.problem.ProblemRegisterDto;
import com.may.ars.domain.problem.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.may.ars.response.ErrorMessage.NOT_EXIST_PROBLEM;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;
    private final ProblemTagRepository problemTagRepository;

    /**
     * 알고리즘 문제 등록
     */
    @Transactional
    public void registerProblem(Problem problem, ProblemRegisterDto registerDto) {
        problemRepository.save(problem);
        reviewRepository.save(registerDto.toReviewEntity(problem));

        List<ProblemTag> problemTagList = new ArrayList<>();

        for (String tagName: registerDto.getTagList()) {
            Optional<Tag> tagOptional = tagRepository.findByTagName(tagName);
            Tag tag;
            if (tagOptional.isEmpty()) { // 태그 새로 등록해야 하는 경우
                tag = new Tag(tagName);
                tagRepository.save(tag);
            }
            else {
                tag = tagOptional.get();
            }
            problemTagList.add(new ProblemTag(problem, tag));
        }
        problemTagRepository.saveAll(problemTagList);
    }

    @Transactional(readOnly = true)
    public List<Problem> getProblemListByMember(Member member) {
        return problemRepository.findAllByWriterIdOrderByCreatedDateDesc(member.getId());
    }

    @Transactional(readOnly = true)
    public Problem getProblemById(Long problemId) {
        return problemRepository.findById(problemId).orElseThrow(
                () -> {throw new IllegalArgumentException(NOT_EXIST_PROBLEM);}
        );
    }


}

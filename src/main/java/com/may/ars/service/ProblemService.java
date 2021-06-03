package com.may.ars.service;

import com.may.ars.dto.member.MemberDto;
import com.may.ars.dto.problem.ProblemRegisterDto;
import com.may.ars.dto.problem.ReviewRegisterDto;
import com.may.ars.model.entity.problem.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;
    private final ProblemTagRepository problemTagRepository;

    /**
     * 알고리즘 문제 등록
     *
     * @param registerDto 등록한 알고리즘 문제 정보
     */
    @Transactional
    public void registerProblem(ProblemRegisterDto registerDto) {
        Problem problem = registerDto.toProblemEntity();
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
    public List<Problem> getProblemListByMember(MemberDto member) {
        return problemRepository.findAllByWriterIdOrderByCreatedDateDesc(member.getMemberId());
    }

    @Transactional(readOnly = true)
    public Problem getProblemById(Long problemId) {
        return problemRepository.findById(problemId).get();
    }

    @Transactional
    public void registerReview(Long problemId, ReviewRegisterDto registerDto, MemberDto member) {
        Problem problem = problemRepository.findById(problemId).get();
        if (!problem.getWriter().getId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("권한 없다.");
        }
        Review review = registerDto.toEntity(problem);
        reviewRepository.save(review);
    }
}

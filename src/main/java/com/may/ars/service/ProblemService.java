package com.may.ars.service;

import com.may.ars.common.advice.exception.EntityNotFoundException;
import com.may.ars.domain.member.Member;
import com.may.ars.domain.review.ReviewRepository;
import com.may.ars.dto.problem.ProblemRequestDto;
import com.may.ars.domain.problem.*;
import com.may.ars.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;
    private final ProblemTagRepository problemTagRepository;

    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public List<Problem> getProblemListByMember(Member member) {
        return problemRepository.findAllByWriterIdOrderByCreatedDateDesc(member.getId());
    }

    @Transactional(readOnly = true)
    public Problem getProblemById(Long problemId) {
        return problemRepository.findById(problemId).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 알고리즘 문제 등록
     */
    @Transactional
    public void registerProblem(Problem problem, ProblemRequestDto registerDto) {
        problemRepository.save(problem);
        log.info(problem.toString());
        reviewRepository.save(reviewMapper.toEntity(problem, registerDto));

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

    @Transactional
    public void updateProblem(Problem problem) {
        Problem updateProblem = checkValidUser(problem.getId(), problem.getWriter());
        updateProblem.setNotificationDate(problem.getNotificationDate());
        problemRepository.save(updateProblem);
    }

    @Transactional
    public void updateStep(Long problemId, Member member, int step) {
        Problem updateProblem = checkValidUser(problemId, member);
        updateProblem.setStep(step);
        problemRepository.save(updateProblem);
    }

    @Transactional
    public void deleteProblem(Long problemId, Member member) {
        checkValidUser(problemId, member);
        problemRepository.deleteById(problemId);
    }

    private Problem checkValidUser(Long problemId, Member member) {
        log.info(problemId + " ");
        return problemRepository.findProblemByIdAndWriter(problemId, member).orElseThrow(EntityNotFoundException::new);
    }
}

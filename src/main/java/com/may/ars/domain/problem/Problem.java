package com.may.ars.domain.problem;

import com.may.ars.domain.BaseEntity;
import com.may.ars.domain.member.Member;
import com.may.ars.domain.review.Review;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Entity
@Builder
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", updatable = false)
    private Member writer;

    private String title;

    private String link;

    private int step;

    private LocalDate notificationDate;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createdDate desc")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProblemTag> tagList = new ArrayList<>();

}

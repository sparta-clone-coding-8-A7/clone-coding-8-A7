package hanghaeclone8a7.twotead.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghaeclone8a7.twotead.domain.QCompany;
import hanghaeclone8a7.twotead.domain.QJobDetail;
import hanghaeclone8a7.twotead.domain.QJobPost;
import hanghaeclone8a7.twotead.dto.response.JobPostResponseDto;
import hanghaeclone8a7.twotead.dto.response.QJobPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobPostCustomRePository {

    private final JPAQueryFactory jpaQueryFactory;
    QJobPost jobPost = QJobPost.jobPost;
    QCompany company = QCompany.company;
    QJobDetail jobDetail = QJobDetail.jobDetail;

//
//    public List<JobPostResponseDto> findJobPostsByNoSearch(Long lastPostId, int size){
//        return jpaQueryFactory.select(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.likes))
//                .from(jobPost, company, jobDetail)
//                .where(jobPost.company.id.eq(company.id)
//                        .and(jobPost.jobDetailId.eq(jobDetail.code))
//                        .and(jobPost.invalid.eq(true)))
//                .orderBy(jobPost.likes.desc())
//                .limit(size).offset(lastPostId)
//                .fetch();
//    }
//
//
//
//    public List<JobPostResponseDto> findJobPostsBySearch(String query, Long lastPostId, int size){
//
//        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.likes))
//                .from(jobPost, company, jobDetail)
//                .where(jobPost.company.id.eq(company.id)
//                        .and(jobPost.jobDetailId.eq(jobDetail.code))
//                        .and(jobPost.invalid.eq(true))
//                                .and(jobPost.position.contains(query)
//                                        .or(jobPost.content.contains(query)
//                                                .or(company.name.contains(query)
//                                                        .or(company.location.contains(query)))))
//                )
//                .orderBy(jobPost.likes.desc())
//                .limit(size).offset(lastPostId)
//                .fetch();
//
//    }

    // 메인페이지 검색+페이징
    public List<JobPostResponseDto> findJobPosts(String query, Long lastPostId, int size){

        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.heart))
                .from(jobPost, company, jobDetail)
                .where(jobPost.company.id.eq(company.id)
                        .and(jobPost.jobDetailId.eq(jobDetail.code))
                        .and(jobPost.invalid.eq(true))
                        .and(queryEq(query))
                )
                .orderBy(jobPost.heart.desc())
                .limit(size).offset(lastPostId)
                .fetch();

    }

    // 채용공고 검색페이징 BooleanExpression
    private BooleanExpression queryEq(String query) {
        if (query == null) {
            return null;
        }
        return jobPost.position.contains(query)
                .or(jobPost.content.contains(query)
                        .or(company.name.contains(query)
                                .or(company.location.contains(query))));
    }

    // 검색 결과 전체 개수 가져오기
    public List<JobPostResponseDto> findJobPostsCount(String query){

        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.heart))
                .from(jobPost, company, jobDetail)
                .where(jobPost.company.id.eq(company.id)
                        .and(jobPost.jobDetailId.eq(jobDetail.code))
                        .and(jobPost.invalid.eq(true))
                        .and(queryEq(query))
                ).fetch();

    }
}

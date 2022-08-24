package hanghaeclone8a7.twotead.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghaeclone8a7.twotead.domain.QCompany;
import hanghaeclone8a7.twotead.domain.QJobDetail;
import hanghaeclone8a7.twotead.domain.QJobPost;
import hanghaeclone8a7.twotead.dto.response.JobPostResponseDto;
import hanghaeclone8a7.twotead.dto.response.QJobPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class JobPostCustomRePository {

    private final JPAQueryFactory jpaQueryFactory;
    QJobPost jobPost = QJobPost.jobPost;
    QCompany company = QCompany.company;
    QJobDetail jobDetail = QJobDetail.jobDetail;

    // 메인페이지 검색+페이징
    public List<JobPostResponseDto> findJobPosts(String query, int size, String cursor){

        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.heart, jobPost.createdAt))
                .from(jobPost, company, jobDetail)
                .where(jobPost.company.id.eq(company.id)
                        .and(jobPost.jobDetailId.eq(jobDetail.code))
                        .and(jobPost.invalid.eq(true))
                        .and(queryEq(query))
                        .and(customCursor(cursor))
                )
                .orderBy(jobPost.heart.desc(), jobPost.createdAt.desc())
                .limit(size)
                .fetch();

    }

//    // 메인페이지 검색+페이징
//    public List<JobPostResponseDto> findJobPosts(String query, Long lastPostId, int size){
//
//        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.heart))
//                .from(jobPost, company, jobDetail)
//                .where(jobPost.company.id.eq(company.id)
//                        .and(jobPost.jobDetailId.eq(jobDetail.code))
//                        .and(jobPost.invalid.eq(true))
//                        .and(queryEq(query))
//                )
//                .orderBy(jobPost.heart.desc(), jobPost.createdAt.desc())
//                .limit(size).offset(lastPostId)
//                .fetch();
//
//    }


    private BooleanExpression customCursor(String customCursor){

        if (customCursor == null) {
            return null;
        }

        StringTemplate stringTemplate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                jobPost.createdAt,
                ConstantImpl.create("%y%m%d%H%i%s")
        );
        System.out.println("ㅂ빚다겁ㅈ디ㅏ거 = " + StringExpressions.lpad(jobPost.heart.stringValue(),6,'0').concat(StringExpressions.lpad(stringTemplate, 12, '0')
                .concat(StringExpressions.lpad(jobPost.id.stringValue(),8,'0')))
                .lt(customCursor));
        return StringExpressions.lpad(jobPost.heart.stringValue(),6,'0').concat(StringExpressions.lpad(stringTemplate, 12, '0')
                .concat(StringExpressions.lpad(jobPost.id.stringValue(),8,'0')))
                .lt(customCursor);
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

        return jpaQueryFactory.selectDistinct(new QJobPostResponseDto(jobPost.id, jobPost.position, company.name, company.location, jobPost.imgUrl, jobPost.heart, jobPost.createdAt))
                .from(jobPost, company, jobDetail)
                .where(jobPost.company.id.eq(company.id)
                        .and(jobPost.jobDetailId.eq(jobDetail.code))
                        .and(jobPost.invalid.eq(true))
                        .and(queryEq(query))
                ).fetch();

    }
}

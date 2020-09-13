package jpa.jin.service;

import jpa.jin.domain.Member;
import jpa.jin.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

    /**
     * [생성자 주입] - 필드 인젝션 보다 좋은 이유
     * null 을 주입하지 않는 한 NullPointerException 은 발생하지 않는다.
     * 의존관계 주입을 하지 않은 경우에는 Controller 객체를 생성할 수 없다.
     * 즉, 의존관계에 대한 내용을 외부로 노출시킴으로써 컴파일 타임에 오류를 잡아낼 수 있다.
     *
     * final 을 사용할 수 있다. final 로 선언된 레퍼런스타입 변수는 반드시 선언과 함께 초기화가 되어야 하므로
     * setter 주입시에는 의존관계 주입을 받을 필드에 final 을 선언할 수 없다.
     * final 의 장점은 누군가가 Controller 내부에서 service 객체를 바꿔치기 할 수 없다는 점이다.
     *
     * */

//    @RequiredArgsConstructor 으로 인해 주석 처리
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @Transactional // (readOnly = false) : default
    public Long join(Member member) {
        // 중복 회원 검증
        validateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원!");
        }
    }

    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}

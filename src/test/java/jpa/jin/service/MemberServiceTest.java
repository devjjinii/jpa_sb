package jpa.jin.service;

import jpa.jin.domain.Member;
import jpa.jin.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 기본적으로 rollback ( Rolled back transaction for test )
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setUsername("jin");

        Long savedId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test // (expected = IllegalStateException.class)
    public void 중복_회원가입() throws Exception {
        Member member1 = new Member();
        member1.setUsername("jin1");

        Member member2 = new Member();
        member2.setUsername("jin1");

        memberService.join(member1);
        try {
            memberService.join(member2); // 예외발생!
        } catch( IllegalStateException e) {
            return;
        }

        fail("예외가 발생해야 한다."); // 위에서 걸려서 return, 현재줄 발생하면 안됨

    }
}
package jpabook.jpashop.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;
	
	@Test
	//@Rollback(false)
	public void ȸ������() throws Exception {
		
		//given
		Member member = new Member();
		member.setName("kim");
		
		//when
		Long savedId = memberService.join(member);
		
		//then
		//em.flush();
		assertEquals(member, memberRepository.findOne(savedId));
		
	}
	
	@Test
	public void �ߺ�_ȸ��_����() throws Exception {
		
		//given
		Member member1 = new Member();
		member1.setName("kim");
		
		Member member2 = new Member();
		member2.setName("kim");
		
		//when
		memberService.join(member1);
		memberService.join(member2); //���ܹ߻�
		
		//then
		Assert.fail("���ܰ� �߻��ؾ� �Ѵ�.");
	}

}

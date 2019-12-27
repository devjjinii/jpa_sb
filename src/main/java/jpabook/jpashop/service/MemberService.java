package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // 읽기는 readOnly = true , 스프링에서 제공하는 
@RequiredArgsConstructor // 필드에 있는 것만 생성자를 만들어줌(lombok)
public class MemberService {

	private final MemberRepository memberRepository;

	/**
	 * 회원 가입
	 */
	@Transactional  //(readOnly = false)
	public Long join(Member member) {
		//중복회원 확인
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		//EXCEPTION
		List<Member> findeMembers = memberRepository.findByName(member.getName());
		if(!findeMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	/**
	 * 회원 전체 조회
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}

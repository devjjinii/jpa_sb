package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // �б�� readOnly = true , ���������� �����ϴ� 
@RequiredArgsConstructor // �ʵ忡 �ִ� �͸� �����ڸ� �������(lombok)
public class MemberService {

	private final MemberRepository memberRepository;

	/**
	 * ȸ�� ����
	 */
	@Transactional  //(readOnly = false)
	public Long join(Member member) {
		//�ߺ�ȸ�� Ȯ��
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		//EXCEPTION
		List<Member> findeMembers = memberRepository.findByName(member.getName());
		if(!findeMembers.isEmpty()) {
			throw new IllegalStateException("�̹� �����ϴ� ȸ���Դϴ�.");
		}
	}
	
	/**
	 * ȸ�� ��ü ��ȸ
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}

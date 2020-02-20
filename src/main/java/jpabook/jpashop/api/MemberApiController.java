package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
//@RequestMapping
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;
	
	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}
	
	@GetMapping("/api/v2/members")
	public Result memberV2() {
		List<Member> findMembers = memberService.findMembers();
		List<MemberDto> collect = findMembers.stream()
			.map(m -> new MemberDto(m.getName()))
			.collect(Collectors.toList());
		
		//return new Result(collect);
		return new Result(collect.size(),collect);
	}
	
	@Data
	@AllArgsConstructor
	static class Result<T> {
		private int count;
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String name;
	}
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		// 실무에서는 entity 를 노출 , param으로 받지 않기 XXXX
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	// 별도의 DTO 생성
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member();  //CreateMemberRequest 를 생성하서 사용
		member.setName(request.getName());
		
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
									@RequestBody @Valid UpdateMemberRequest request) {
		
		memberService.update(id,request.getName());
		Member findMember =  memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}
	
	@Data
	static class UpdateMemberRequest {
		private String name;
	}
	
	@Data
	@AllArgsConstructor  // 모든 파라미터를 넘기는 
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}
	
	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
	}
	
	@Data
	static class CreateMemberResponse {
		private Long id;
		
		public CreateMemberResponse(Long id) {
			this.id = id;
		}

	}
}

package jpa.jin.repository;

import jpa.jin.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
//    @Autowired
    private final EntityManager em;

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }
//
    // 저장
    public void save(Member member) {
        em.persist(member);
    }

    // 아이디로 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 이름 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.username = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}

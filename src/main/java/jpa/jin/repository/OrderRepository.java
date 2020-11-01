package jpa.jin.repository;

import jpa.jin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 동적쿼리.. JPA criteria  실무에서는 잘.... * JPA 스펙 참조

        // Querydsl로 처리
        return em.createQuery("select o from Order o join o.member m"+
                " where o.status = :status" +
                " and m.username like : username", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("username", orderSearch.getMemberName())
                .getResultList();

    }
}

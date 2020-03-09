package jpabook.jpashop.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

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
	
	/*public List<Order> findAll(OrderSearch orderSearch) {
		// �������� ó�� ..... 
		return em.createQuery("select o from Order o join o.member m" +
						"where o.status = :status" +
						"and m.name like :name" , Order.class)
				.setParameter("status", orderSearch.getOrderStatus())
				.setParameter("name", orderSearch.getMemberName())
				//.setFristResult(100) 100����
				.setMaxResults(1000) // �ִ� 1000����
				.getResultList();
	}*/
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		
		Root<Order> o = cq.from(Order.class);
		Join<Order, Member> m = o.join("member", JoinType.INNER); //ȸ���� ����
		List<Predicate> criteria = new ArrayList<>();
		
		//�ֹ� ���� �˻�
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}
		
		//ȸ�� �̸� �˻�
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //�ִ� 1000��
		
		return query.getResultList();
	}

	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery("select o from Order o" +
						" join fetch o.member m" +
						" join fetch o.delivery d", Order.class)
		.getResultList();
	}
	
	// ����ȭ�Ǽ� ���� ����( �ֹ� �Ǽ�, �ߺ������� ����)
	public List<Order> findAllWithMemberDelivery(int offset, int limit ) {
		// select o from Order o 
		return em.createQuery("select o from Order o" +
						" join fetch o.member m" +
						" join fetch o.delivery d", Order.class)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
	}

	// fetch join���� sql 1���� ����
	// �ϴ�ٸ� fetch �ϴ� ����, ����¡ ó�� �Ұ���
	
	// ������ �ѹ��� �������� �ߺ������Ͱ� ���� �����Ѵ�.
	public List<Order> findAllWithItem() {
		return em.createQuery("select distinct o from Order o" +
							" join fetch o.member m" +
							" join fetch o.delivery d" +
							" join fetch o.orderItems oi" +
							" join fetch oi.item i", Order.class)
				//.setFirstResult(1)
				//.setMaxResults(100)
				.getResultList();
	}

}

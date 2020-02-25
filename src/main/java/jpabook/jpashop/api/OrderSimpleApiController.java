package jpabook.jpashop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

/*
 * xToOne(ManyToOne, OneToOne) ���� ����ȭ
 * Order
 * Order -> Member
 * Order -> Delivery
 * 
 * */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	
	// ���ѷ��� ..... ������� ��� @JsonIgnore 
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
		for(Order order : all) { 
			order.getMember().getName();  // Lazy ���� �ʱ�ȭ
			order.getDelivery().getAddress();  // Lazy ���� �ʱ�ȭ
		}
		return all;
	}
}

package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository; 
	
	@GetMapping("/api/v1/orders")    // api entity 노출시키면 안됨!!
	public List<Order> orderV1() {
		List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
		for (Order order : all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			
			List<OrderItem>	 orderItems = order.getOrderItems();
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return all;
	}
	
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
		List<OrderDto> result = orders.stream()
			.map(o -> new OrderDto(o))
			.collect(Collectors.toList());
		
		return result;
	}
	
	// 데이터 중복 발생 ( 데이터의 양이 많아지면 속도,성능 문제 발생)
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithItem();
		
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
			
			return result;
	}
	
	// 제일 최적화 된 api ( 페이징도 처리 )
	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3_page(
			@RequestParam(value = "offset", defaultValue = "0" ) int offset,
			@RequestParam(value = "limit", defaultValue = "100" ) int limit ){
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
		
		List<OrderDto> result = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
			
			return result;
	}
	
	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}
	
	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5() {
		return orderQueryRepository.findAllByDto_optimization();
	}
	
	@GetMapping("/api/v6/orders")
	public List<OrderFlatDto> ordersV6() {
		return orderQueryRepository.findAllByDto_flat();
	}
	
	//orderDTo로 변환
	@Getter
	static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItem;
		
		//값 셋팅
		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			/*order.getOrderItems().stream().forEach(o -> o.getItem().getName());
			orderItem = order.getOrderItems();*/
			// 
			orderItem = order.getOrderItems().stream()
											//orderItemDto로 변환
						.map(orderItem -> new OrderItemDto(orderItem))
						.collect(Collectors.toList());
		}
	}
	
	@Getter
	static class OrderItemDto {

		private String itemName; // 상품명
		private int orderPrice; // 주문 가격
		private int count; // 주문 . 수량
		
		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
}

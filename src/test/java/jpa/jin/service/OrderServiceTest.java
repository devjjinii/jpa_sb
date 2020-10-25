package jpa.jin.service;

import jpa.jin.domain.Address;
import jpa.jin.domain.Member;
import jpa.jin.domain.Order;
import jpa.jin.domain.OrderStatus;
import jpa.jin.domain.item.Book;
import jpa.jin.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","한강","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");
    }

    @Test
    public void 주문취소() throws Exception {

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {

    }

}
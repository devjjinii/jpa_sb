package jpa.jin.service;

import jpa.jin.domain.Address;
import jpa.jin.domain.Member;
import jpa.jin.domain.Order;
import jpa.jin.domain.OrderStatus;
import jpa.jin.domain.item.Book;
import jpa.jin.domain.item.Item;
import jpa.jin.exception.NotEnoughStockException;
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
        Member member = getMember();
        Book book = getBook("JPA",10000,10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(),"주문한 상품 종류 수.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice()," 가격 * 수량 ");
        assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 -.");
    }

    @Test
    public void 주문취소() throws Exception {
        // cmd+ shift + T
        Member member = getMember();
        Book item = getBook("JPA",10000,10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),"주문 취소 CANCEL");
        assertEquals(10, item.getStockQuantity(), "주문 취소시 재고 증가");
    }

    @Test  // (expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //재고 없음
        Member member = getMember();
        Item item = getBook("JPA", 10000, 10);

        int orderCount = 11;

        orderService.order(member.getId(), item.getId(), orderCount);

        fail("재고 없음");

    }

    private Book getBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","한강","123-123"));
        em.persist(member);
        return member;
    }

}
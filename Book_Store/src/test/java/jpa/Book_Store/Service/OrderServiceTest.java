package jpa.Book_Store.Service;

import jakarta.persistence.EntityManager;
import jpa.Book_Store.Domain.Address;
import jpa.Book_Store.Domain.Item.Book;
import jpa.Book_Store.Domain.Member.Member;
import jpa.Book_Store.Domain.Order.Order;
import jpa.Book_Store.Domain.Order.OrderStatus;
import jpa.Book_Store.Exception.NotEnoughStockException;
import jpa.Book_Store.Repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @Rollback(value = false)
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("회원1");
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 3;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOnd(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류수가 정확해야 함.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격을 가격 * 수량", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 함.", 7, book.getStockQuantity());
    }

    @Test
    @Rollback(value = false)
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("회원1");
        Book book = createBook("JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOnd(orderId);
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 됨.", 10, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("회원1");
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 10; //예외가 일어나게끔 선언

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족으로 예외가 발생해야 함.");
    }

    //Item 엔티티의 removeStock 단위 테스트가 필요함

    private Member createMember(String name){
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강가", "123=123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}
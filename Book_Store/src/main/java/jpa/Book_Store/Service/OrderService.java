package jpa.Book_Store.Service;

import jpa.Book_Store.Domain.Delivery.Delivery;
import jpa.Book_Store.Domain.Delivery.DeliveryStatus;
import jpa.Book_Store.Domain.Item.Item;
import jpa.Book_Store.Domain.Member.Member;
import jpa.Book_Store.Domain.Order.Order;
import jpa.Book_Store.Domain.Order.OrderItem;
import jpa.Book_Store.Repository.ItemRepository;
import jpa.Book_Store.Repository.MemberRepository;
import jpa.Book_Store.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOnd(orderId);
        order.cancle(); //도메인 모델 패턴
    }

}

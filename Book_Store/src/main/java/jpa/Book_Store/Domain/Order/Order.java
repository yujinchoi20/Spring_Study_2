package jpa.Book_Store.Domain.Order;

import jakarta.persistence.*;
import jpa.Book_Store.Domain.Delivery.Delivery;
import jpa.Book_Store.Domain.Delivery.DeliveryStatus;
import jpa.Book_Store.Domain.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.MetaMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //Member Entity와의 다대일 연관관계 주인
    @JoinColumn(name = "member_id")
    private Member member; //주문 회원

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //Delivery Entity와의 일대일 연관관계 주인
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송 정보

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING) //enum type
    private OrderStatus status; //주문 상태

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //생성 메서드 : 주문회원, 배송정보, 주문상품의 정보를 받아서 실제 주문 엔티티를 생성한다.
    public static Order createOrder (Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //비지니스 로직
    //주문 취소 : 주문상태를 취소로 변경하고, 주문상품에 주문취소를 알린다. 이미 배송완료된 상품은 예외처리
    public void cancle() {
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료되어 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //조회 로직
    //전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }
}

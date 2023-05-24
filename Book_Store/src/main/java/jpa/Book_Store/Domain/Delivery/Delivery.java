package jpa.Book_Store.Domain.Delivery;

import jakarta.persistence.*;
import jpa.Book_Store.Domain.Address;
import jpa.Book_Store.Domain.Order.Order;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address; //city, street, zipcode를 임베디드 타입으로 선언

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}

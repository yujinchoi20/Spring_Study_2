package jpa.Book_Store.Domain.Item;

import jakarta.persistence.*;
import jpa.Book_Store.Domain.Category.Category;
import jpa.Book_Store.Domain.Category.Item_Category;
import jpa.Book_Store.Exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED) //조인 전략
@DiscriminatorColumn(name = "dtype")
public abstract class Item { //상속을 위해 추상 클래스로 선언

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<Item_Category> item_categories = new ArrayList<>();

    //비지니스 로직 추가
    public void addStock(int quantity) { //재고 추가(주무 취소)
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) { //재고 감소(주문 접수)
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) { //재고 부족 시 예외 발생
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
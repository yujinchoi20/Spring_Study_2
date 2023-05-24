package jpa.Book_Store.Domain.Item;

import jakarta.persistence.*;
import jpa.Book_Store.Domain.Category.Category;
import jpa.Book_Store.Domain.Category.Item_Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //단일 테이블 전략 (이후에 조인전략으로 변경할 예정)
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
}

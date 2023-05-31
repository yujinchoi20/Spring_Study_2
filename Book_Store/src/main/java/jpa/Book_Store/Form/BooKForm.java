package jpa.Book_Store.Form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BooKForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}

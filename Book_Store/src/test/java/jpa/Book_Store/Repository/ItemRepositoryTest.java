package jpa.Book_Store.Repository;

import jpa.Book_Store.Domain.Item.Book;
import jpa.Book_Store.Domain.Item.Item;
import jpa.Book_Store.Domain.Item.Movie;
import jpa.Book_Store.Service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    @Rollback(value = false)
    public void 상품_추가() throws Exception{
        //given
        Book book = new Book();
        book.setName("IBK Volleyball");
        book.setAuthor("choi");
        book.setIsbn("minky");
        book.setPrice(10000);
        book.setStockQuantity(10);

        //when
        Long saveItemId = itemService.saveItem(book);

        //then
        Assertions.assertThat(itemRepository.findOne(saveItemId)).isEqualTo(book);
    }

    @Test
    @Rollback(value = false)
    public void 상품_조회() throws Exception {
        //given
        Book book = new Book();
        book.setName("JPA 정복");
        book.setAuthor("kim");

        //when
        Long saveId = itemService.saveItem(book);

        //then
        String bookName = itemService.findOne(saveId).getName();
        System.out.println("Book Name : " + bookName);
    }

    @Test
    @Rollback(value = false)
    public void 상품_전체_조회() {
        //given
        Movie movie = new Movie();
        movie.setName("너의 이름은");
        movie.setActor("미츠하");
        movie.setPrice(15000);
        movie.setStockQuantity(10);

        Book book = new Book();
        book.setName("JPA 정복");
        book.setAuthor("kim");
        book.setPrice(30000);
        book.setStockQuantity(100);

        //when
        itemService.saveItem(movie);
        itemService.saveItem(book);

        //then
        List<Item> items = itemService.findItems();
        for(Item item : items) {
            System.out.println("ITEM NAME: " + item.getName());
        }
    }
}
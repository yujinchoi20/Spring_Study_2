package jpa.Book_Store.Repository;

import jpa.Book_Store.Domain.Item.Book;
import jpa.Book_Store.Service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

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
}
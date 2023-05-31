package jpa.Book_Store.Controller;

import jpa.Book_Store.Domain.Item.Book;
import jpa.Book_Store.Domain.Item.Item;
import jpa.Book_Store.Form.BooKForm;
import jpa.Book_Store.Service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //상품 등록
    @GetMapping(value = "/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BooKForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(BooKForm form) {

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";
    }

    //상품 목록
    @GetMapping(value = "/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    //상품 수정
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        //@PathVariable: url 경로의 일부를 매개변수로 바인딩하는 데 사용됨.
        Book item = (Book) itemService.findOne(itemId);

        BooKForm form = new BooKForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form); //저장된 상품의 정보를 form 으로 넘겨준다.

        return "items/updateItemForm";
    }

    @PostMapping(value = "/items/{items}/edit")
    public String updateItem(@ModelAttribute("form") BooKForm form) {
        //@ModelAttribute: HTTP 요청의 파라미터 값을 해당 메서드의 매개변수 또는 메서드의 리턴 타입에 바인딩하는 데 사용됨.
        //주로 HTML 폼 데이터를 객체로 변환하거나 모델 객체를 뷰에 전달할 때 사용됨.

        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book); //수정된 상품정보를 다시 저장한다. -> update
        //itemRepository -> save -> if(item.getId() != null) em.merge(item)
        //em.merge()는 추후 변경감지로 변경 예정!

        return "redirect:/items";
    }
}

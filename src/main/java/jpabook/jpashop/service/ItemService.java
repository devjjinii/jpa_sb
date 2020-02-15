package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	
	@Transactional    //false
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	/*@Transactional   //준영속 엔티티 수정, 1. 변경 감지 기능
	public void updateItem(Long itemId, Book param) {
		Item findItem = itemRepository.findOne(itemId);
		findItem.setPrice(param.getPrice());
		findItem.setName(param.getName());
		findItem.setStockQuantity(param.getStockQuantity());
	}*/
	
	@Transactional  // merge를 사용하지 않는다, null.. (실무)
	// * 엔티티를 변경할때는 항상 변경 감지를 이용 !        // -> UpdateItemDto 사용해도 된다.
	public void updateItem(Long itemId, String name, int price, int stockQuantity ) {
		Item findItem = itemRepository.findOne(itemId);
		findItem.setName(name);
		findItem.setPrice(price);
		findItem.setStockQuantity(stockQuantity);
		//return findItem;
	}
	
	
	public List<Item> findItem() {
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}

package jpa.jin.domain.item;

import jpa.jin.domain.Category;
import jpa.jin.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    // setter 를 가지고 계산하는게 아니라 필요한 계산을 만들어서 한다.
    //재고 수량 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 재고 수량 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("재고 없음");
        }
        this.stockQuantity = restStock;
    }
}

package jpa.jin.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// Entity 에는 Setter를 사용하지 않는다.
// EAGER XX , LAZY OO
// XToMany 는 기본이 LAZY,
// ManyToX 는 LAZY 로 무조건
@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //ready, comp
}

package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Address {

	private String city;
	private String street;
	private String zipcode;
	
	protected Address() {
		// JPA ½ºÆå»ó »ý¼º
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}
}

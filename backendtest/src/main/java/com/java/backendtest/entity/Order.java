package com.java.backendtest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "order_tbl")
@ToString(onlyExplicitlyIncluded = true)
public class Order {
	
	@Id
	@Column(name = "order_no")
	@ToString.Include
	private String orderNo;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Include
	private Item item;
    
    @ToString.Include
	private Long qty;
    
    @ToString.Include
	private Long price;

}

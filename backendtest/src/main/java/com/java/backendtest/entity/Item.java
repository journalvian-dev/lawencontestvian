package com.java.backendtest.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "item")
@ToString(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ToString.Include
    private String name;

    @ToString.Include
	private Long price;
	
    @OneToMany(mappedBy = "item")
    @ToString.Exclude
    private List<Inventory> inventories;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "item")
    private List<Order> orders;
}
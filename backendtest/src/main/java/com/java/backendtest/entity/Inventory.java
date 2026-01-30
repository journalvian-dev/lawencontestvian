package com.java.backendtest.entity;

import com.java.backendtest.enums.InventoryType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "inventory")
@ToString(onlyExplicitlyIncluded = true)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    @ToString.Include
    private Long qty;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private InventoryType type;

    @ToString.Include(name = "itemId")
    private Long getItemIdForToString() {
        return item != null ? item.getId() : null;
    }
}

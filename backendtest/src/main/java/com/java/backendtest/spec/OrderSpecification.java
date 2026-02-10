package com.java.backendtest.spec;


import org.springframework.data.jpa.domain.Specification;

import com.java.backendtest.entity.Order;

public class OrderSpecification {
	
    public static Specification<Order> hasItemId(Long itemId){
        return (root, query, cb) -> {

            if(itemId == null){
                return null;
            }

            return cb.equal(
            		root.get("item").get("id"),
            		itemId);
        };
    }
}

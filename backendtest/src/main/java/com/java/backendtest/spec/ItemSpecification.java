package com.java.backendtest.spec;


import org.springframework.data.jpa.domain.Specification;

import com.java.backendtest.entity.Item;

public class ItemSpecification {

    public static Specification<Item> hasName(String name){
        return (root, query, cb) -> {

            if(name == null || name.isBlank()){
                return null;
            }

            return cb.like(
                cb.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
            );
        };
    }
}


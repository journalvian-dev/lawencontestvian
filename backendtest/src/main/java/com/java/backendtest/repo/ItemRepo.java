package com.java.backendtest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.backendtest.entity.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>{
	
	
	@Query("select i from Item i where lower(i.name) like lower(concat('%', :name, '%'))")
	Page<Item> findByName(@Param("name") String name, Pageable pageable);
	
}

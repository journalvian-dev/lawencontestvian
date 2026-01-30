package com.java.backendtest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.backendtest.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, String>{
	
	@Query("select o from Order o where o.item.id = :itemid")
	Page<Order> findByItemId(@Param("itemid") Long itemId, Pageable pageable);

    @Query("""
            select o.orderNo
            from Order o
            order by cast(substring(o.orderNo, 2) as integer) desc
        """)
        Page<String> findLastOrderNo(Pageable pageable);
}

package com.java.backendtest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.backendtest.entity.Inventory;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory>{

	@Query("select i from Inventory i where i.item.id = :itemid")
	Page<Inventory> findByItemId(@Param("itemid") Long itemId, Pageable pageable);
	
	@Query("""
		    select sum(
		        case 
		            when i.type = com.java.backendtest.enums.InventoryType.T then i.qty
		            when i.type = com.java.backendtest.enums.InventoryType.W then -i.qty
		        end
		    )
		    from Inventory i
		    where i.item.id = :itemId
		""")
		Long calculateStock(@Param("itemId") Long itemId);
}

package com.fms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fms.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
//	List<ItemEntity> findAllByUser_UserName(String userName);
	
	@Query("SELECT i FROM ItemEntity i WHERE i.user.userName = :name")
	Page<ItemEntity> findByUserName(@Param("name") String userName, Pageable pageable);
}

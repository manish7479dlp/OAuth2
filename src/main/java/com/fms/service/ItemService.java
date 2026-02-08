package com.fms.service;


import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fms.dto.ItemRequestDTO;
import com.fms.dto.ItemResponseDTO;
import com.fms.entity.ItemEntity;
import com.fms.entity.UserEntity;
import com.fms.repository.ItemRepository;
import com.fms.repository.UserRepository;
import com.fms.util.Utils;


@Service
public class ItemService {
	
   private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
   private final ItemRepository itemRepository;
   private final ModelMapper modelMapper;
   private final UserRepository userRepository;
   
   public ItemService(ItemRepository itemRepository,
					   ModelMapper modelMapper,
					   UserRepository userRepository) {
	   this.itemRepository = itemRepository;
	   this.modelMapper = modelMapper;
	   this.userRepository = userRepository;
   }
   
   
//   public List<Page<ItemResponseDTO>> getByUserName(String userName, Pageable pageable) {
//	   List<Page<ItemEntity>> items = itemRepository.findByUserName(userName, pageable); 
//	    
//	    return items.stream()
//	        .map(item -> {
//	            ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
//	            // Manually set the username if the auto-mapping misses it
//	            if (item.getUser() != null) {
//	                dto.setUserName(item.getUser().getUserName());
//	            }
//	            return dto;
//	        })
//	        .toList();
//	}
   
   public Page<ItemResponseDTO> getByUserName(String userName, Pageable pageable) {
	    // 1. Get the single Page object from the repository
	    Page<ItemEntity> itemPage = itemRepository.findByUserName(userName, pageable); 
	    
	    // 2. Use the Page's built-in map method
	    return itemPage.map(item -> {
	        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
	        
	        // Ensure the username is set (if your DTO needs it)
	        if (item.getUser() != null) {
	            dto.setUserName(item.getUser().getUserName());
	        }
	        
	        return dto;
	    });
	}
   
   public ItemResponseDTO add(String userName, ItemRequestDTO itemRequestDTO) {
	   
	   ItemEntity itemToInsert = modelMapper.map(itemRequestDTO, ItemEntity.class);
	   UserEntity dbUser = userRepository.findById(userName).get();
	   
	   itemToInsert.setUser(dbUser);
	   
	   ItemEntity insertedItem = itemRepository.save(itemToInsert);
	   ItemResponseDTO itemResponseDTO =  modelMapper.map(insertedItem, ItemResponseDTO.class);
	   itemResponseDTO.setUserName(userName);
	   
	   return itemResponseDTO;
   }
   
   public boolean deleteByID(Long id, String userName) throws BadRequestException {
	    
	    // 1. Use orElseThrow to handle "Not Found" cases gracefully
	    ItemEntity existingEntity = itemRepository.findById(id)
	        .orElseThrow(() -> new BadRequestException("Item with id " + id + " not found"));
	    
	    UserEntity owner = existingEntity.getUser();
	    
	    // 2. Check if the logged-in user is the owner
	    // Note: Use .equals() for string comparison and handle potential nulls
	    if (owner != null && !owner.getUserName().equals(userName)) {
	        throw new BadRequestException("User " + userName + " does not have permission to delete this item."); 
	    }
	    
	    itemRepository.deleteById(id);
	    return true;
	}
   public ItemResponseDTO updateByID(Long id, String authUserName, ItemRequestDTO itemRequestDTO) throws BadRequestException {
	    // 1. Fetch the existing entity safely
	    ItemEntity existingItem = itemRepository.findById(id)
	        .orElseThrow(() -> new BadRequestException("Item with id " + id + " not found"));

	    // 2. Security Check: Only the owner can update
	    // We assume ItemEntity has a UserEntity relationship named 'user'
	    if (existingItem.getUser() == null || !existingItem.getUser().getUserName().equals(authUserName)) {
	        throw new BadRequestException("User " + authUserName + " is not authorized to update this item");
	    }

	    // 3. Map updates from DTO to Entity
	    // Using modelMapper.map(source, destination) updates the existing object
	    modelMapper.map(itemRequestDTO, existingItem);

	    // 4. Manual updates (if needed) 
	    // If your modelMapper isn't configured for deep mapping, ensure the ID isn't changed
	    existingItem.setId(id); 
	    
	    // 5. Save and Return the mapped response
	    ItemEntity updatedItem = itemRepository.save(existingItem);
	    
	    return modelMapper.map(updatedItem, ItemResponseDTO.class);
	}
   
   
 
}

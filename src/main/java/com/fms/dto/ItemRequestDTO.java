package com.fms.dto;

public class ItemRequestDTO {
    private long groupId;
    private String name;
    private long price;
    
    public ItemRequestDTO() {}
    
	public ItemRequestDTO(long groupId, String name, long price) {
		super();
		this.groupId = groupId;
		this.name = name;
		this.price = price;
	}
	
	public long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
}

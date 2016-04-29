package com.git.cs309.mmoclient.gui.interfaces;

import java.util.ArrayList;

import com.git.cs309.mmoclient.items.ItemStack;

public class ClientShop {
	
	//private static final long serialVersionUID;
	private final String shopName;
	private final int id;
	//private item[] shopItems; //change to array list
	ArrayList<ItemStack> shopItems = new ArrayList<ItemStack>(0);
	
	//murchents can have multiple shops and
	//several murchents could access the same shop 
	
	public ClientShop(String shopName, int id) {
		this.shopName = shopName;
		this.id = id;
	}

	public String getName()
	{
		return shopName;
	}
	
	public int getShopID()
	{
		return id;
	}
	
	public void addItem(ItemStack itemToAdd)
	{
		shopItems.add(itemToAdd);
	}
	
	public void addItemToIndex(int index, ItemStack itemToAdd)
	{
		shopItems.set(index, itemToAdd);
	}
	
	public ItemStack getItem(int indexOfItem)
	{
		return shopItems.get(indexOfItem);
	}
	
	public void removeItem(int index)
	{
		shopItems.set(index, null);
	}
	
	public String getItemName(int index)
	{
		return shopItems.get(index).getItemName();
	}
	
	public int getNumberOfItems()
	{
		return shopItems.size();
	}
	
	

}

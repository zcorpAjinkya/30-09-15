package com.zcorp.yielloh.model;

public class NavDrawerItem {
	
	private String title,text;
	private int icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon,String text){
		this.title = title;
		this.icon = icon;
		this.text=text;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count,String text){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.text=text;
	}
	
	public String getTitle(){
		return this.title;
	}
	public String getText(){
		return this.text;
	}
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}

	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	public void setText(String text){
		this.text = text;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}

package com.momsdhaba.user;

public class Userdata {

	private String name;
	private String description;
	private String date;
	private String foodtype;
	private String price;
	private String foodname;
	private String foodid;
	private String foodquantity;
	private String image;
	private String profile;
	private boolean upSelected;
	private int num;
	private boolean downSelected;

	public Userdata() {
		// TODO Auto-generated constructor stub
	}

	public Userdata(String name, String description, String date,
			String foodtype, String price, String foodname, String image,
			String foodid, String foodquantity, String profile) {
		super();
		this.name = name;
		this.description = description;
		this.date = date;
		this.foodtype = foodtype;
		this.price = price;
		this.foodname = foodname;
		this.foodid = foodid;
		this.foodquantity = foodquantity;
		this.image = image;
		this.profile = profile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFoodtype() {
		return foodtype;
	}

	public void setFoodtype(String foodtype) {
		this.foodtype = foodtype;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFoodName() {
		return foodname;
	}

	public void setFoodName(String foodname) {
		this.foodname = foodname;
	}

	public String getFoodId() {
		return foodid;
	}

	public void setFoodId(String foodid) {
		this.foodid = foodid;
	}

	public String getFoodQuantity() {
		return foodquantity;
	}

	public void setFoodQuantity(String foodquantity) {
		this.foodquantity = foodquantity;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setUpSelected(boolean upSelected) {
		this.upSelected = upSelected;
	}

	public boolean isUpSelected() {
		return upSelected;
	}

	public void setDownSelected(boolean downSelected) {
		this.downSelected = downSelected;
	}

	public boolean isDownSelected() {
		return downSelected;
	}
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

}

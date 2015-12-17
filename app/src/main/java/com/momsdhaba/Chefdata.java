package com.momsdhaba;

public class Chefdata {

	private String name;
	private String description;
	private String date;
	private String foodtype;
	private String price;
	private String foodname;
	private String foodid;
	private String foodquantity;
	private String image;
	private String foodorder;


	public Chefdata() {
		// TODO Auto-generated constructor stub
	}

	public Chefdata(String name, String description, String date,
			String foodtype, String price, String foodname, String image,
			String foodid, String foodquantity) {
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
		
		// this.foodorder =foodorder; //String foodorder
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

	
	// public String getOrder() {
	// return foodorder;
	// }
	//
	// public void setOrder(String image) {
	// this.foodorder = foodorder;
	// }

}

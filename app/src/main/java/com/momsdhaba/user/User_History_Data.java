package com.momsdhaba.user;

public class User_History_Data {

	private String Foodname;
	private String Foodcount;
	private String Totalamount;
	private String Date;
	private String Foodid;
	private String Orderid;
	private String OrderItemid;
	public User_History_Data() {
		// TODO Auto-generated constructor stub
	}

	public User_History_Data(String Foodname, String Foodcount,
			String Totalamount, String Date ,String Foodid ,String Orderid ,String OrderItemid) {
		super();
		this.Foodname = Foodname;
		this.Foodcount = Foodcount;
		this.Totalamount = Totalamount;
		this.Date = Date;
		this.Foodid = Foodid;
		this.Orderid = Orderid;
		this.OrderItemid = OrderItemid;
	}

	public String getFoodName() {
		return Foodname;
	}

	public void setFoodName(String Foodname) {
		this.Foodname = Foodname;
	}

	public String getFoodcount() {
		return Foodcount;
	}

	public void setFoodcount(String Foodcount) {
		this.Foodcount = Foodcount;
	}

	public String getTotal() {
		return Totalamount;
	}

	public void setTotal(String Totalamount) {
		this.Totalamount = Totalamount;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}
	public String getFoodid() {
		return Foodid;
	}

	public void setFoodid(String Foodid) {
		this.Foodid = Foodid;
	}
	public String getOrderid() {
		return Orderid;
	}

	public void setOrderid(String Orderid) {
		this.Orderid = Orderid;
	}
	public String getOrderItemid() {
		return OrderItemid;
	}

	public void setOrderItemid(String OrderItemid) {
		this.OrderItemid = OrderItemid;
	}

}

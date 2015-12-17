package com.momsdhaba.history;

public class Chef_History_Data {

	private String Foodname;
	private String Foodcount;
	private String Totalamount;
	private String Date;

	public Chef_History_Data() {
		// TODO Auto-generated constructor stub
	}

	public Chef_History_Data(String Foodname, String Foodcount, String Date) {
		super();
		this.Foodname = Foodname;
		this.Foodcount = Foodcount;
		this.Date = Date;
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

	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}

}

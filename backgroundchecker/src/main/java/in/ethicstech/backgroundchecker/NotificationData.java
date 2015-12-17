package in.ethicstech.backgroundchecker;

public class NotificationData {

	private String Foodname;
	private String Foodcount;
	private String Totalamount;
	private String Date;
	private String foodid;
	private String status;
	private String orderid;

	public NotificationData() {
		// TODO Auto-generated constructor stub
	}

	public NotificationData(String Foodname, String Foodcount,
							String Totalamount, String status, String foodid, String orderid, String Date) {
		super();
		this.Foodname = Foodname;
		this.Foodcount = Foodcount;
		this.Totalamount = Totalamount;
		this.Date = Date;
		this.foodid = foodid;
		this.status = status;
		this.orderid = orderid;
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

	public String getFoodId() {
		return foodid;
	}

	public void setFoodId(String foodid) {
		this.foodid = foodid;
	}

	public String getOrderId() {
		return orderid;
	}

	public void setOrderId(String orderid) {
		this.orderid = orderid;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
	
}

package alan.share.officialstrategy.model;

import java.io.Serializable;
/**
 * 地点pojo映射
 * @author Salu
 *
 */
public class TourismAttraction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tourismid;
	private String locationid;
	private String tourismname;
	private TourismCategory tourismCategory;
	private String tourismforeignname;
	private String tourismdescription;
	private Float tourismprice;
	private String tourismopendesc;
	private String tourismaddress;
	private String tourismguide;
	private String tourismphone;
	private String tourismurl;
	private String tourismactivity;
	private String tourismtype;
	private String currencytype;
	private City city;
	
	//self defined
	private Location location;
	
	
	public String getCurrencytype() {
		return currencytype;
	}
	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}
	public String getTourismtype() {
		return tourismtype;
	}
	public void setTourismtype(String tourismtype) {
		this.tourismtype = tourismtype;
	}
	public String getTourismid() {
		return tourismid;
	}
	public void setTourismid(String tourismid) {
		this.tourismid = tourismid;
	}

	public String getLocationid() {
		return locationid;
	}
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getTourismname() {
		return tourismname;
	}
	public void setTourismname(String tourismname) {
		this.tourismname = tourismname;
	}
	public TourismCategory getTourismCategory() {
		return tourismCategory;
	}
	public void setTourismCategory(TourismCategory tourismCategory) {
		this.tourismCategory = tourismCategory;
	}
	public String getTourismforeignname() {
		return tourismforeignname;
	}
	public void setTourismforeignname(String tourismforeignname) {
		this.tourismforeignname = tourismforeignname;
	}
	public String getTourismdescription() {
		return tourismdescription;
	}
	public void setTourismdescription(String tourismdescription) {
		this.tourismdescription = tourismdescription;
	}
	public Float getTourismprice() {
		return tourismprice;
	}
	public void setTourismprice(Float tourismprice) {
		this.tourismprice = tourismprice;
	}
	public String getTourismopendesc() {
		return tourismopendesc;
	}
	public void setTourismopendesc(String tourismopendesc) {
		this.tourismopendesc = tourismopendesc;
	}
	public String getTourismaddress() {
		return tourismaddress;
	}
	public void setTourismaddress(String tourismaddress) {
		this.tourismaddress = tourismaddress;
	}
	public String getTourismguide() {
		return tourismguide;
	}
	public void setTourismguide(String tourismguide) {
		this.tourismguide = tourismguide;
	}
	public String getTourismphone() {
		return tourismphone;
	}
	public void setTourismphone(String tourismphone) {
		this.tourismphone = tourismphone;
	}
	public String getTourismurl() {
		return tourismurl;
	}
	public void setTourismurl(String tourismurl) {
		this.tourismurl = tourismurl;
	}
	public String getTourismactivity() {
		return tourismactivity;
	}
	public void setTourismactivity(String tourismactivity) {
		this.tourismactivity = tourismactivity;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}

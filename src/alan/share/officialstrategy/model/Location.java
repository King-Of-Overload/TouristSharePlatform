package alan.share.officialstrategy.model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 位置信息pojo映射
 * @author Salu
 *
 */
public class Location implements Serializable{
	private static final long serialVersionUID = 1L;
	private String locationid;
	private BigDecimal longitude;
	private BigDecimal latitude;
	public String getLocationid() {
		return locationid;
	}
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	

}

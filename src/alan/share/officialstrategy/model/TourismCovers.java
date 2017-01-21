package alan.share.officialstrategy.model;

import java.io.Serializable;
/**
 * 地点封面pojo
 * @author Salu
 *
 */
public class TourismCovers implements Serializable{
	private String tourismcoverid;
	private String tourismurl;
	
	private TourismAttraction tourismAttraction;

	public String getTourismcoverid() {
		return tourismcoverid;
	}

	public void setTourismcoverid(String tourismcoverid) {
		this.tourismcoverid = tourismcoverid;
	}

	public String getTourismurl() {
		return tourismurl;
	}

	public void setTourismurl(String tourismurl) {
		this.tourismurl = tourismurl;
	}

	public TourismAttraction getTourismAttraction() {
		return tourismAttraction;
	}

	public void setTourismAttraction(TourismAttraction tourismAttraction) {
		this.tourismAttraction = tourismAttraction;
	}
	
	

}

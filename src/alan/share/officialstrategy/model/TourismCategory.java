package alan.share.officialstrategy.model;

import java.io.Serializable;
/**
 * 地点分类对象
 * @author Salu
 *
 */
public class TourismCategory implements Serializable{
	private static final long serialVersionUID = 1L;
	private int tmcategoryid;
	private String tmcategorytype;
	private String tmcategoryname;
	public int getTmcategoryid() {
		return tmcategoryid;
	}
	public void setTmcategoryid(int tmcategoryid) {
		this.tmcategoryid = tmcategoryid;
	}
	public String getTmcategorytype() {
		return tmcategorytype;
	}
	public void setTmcategorytype(String tmcategorytype) {
		this.tmcategorytype = tmcategorytype;
	}
	public String getTmcategoryname() {
		return tmcategoryname;
	}
	public void setTmcategoryname(String tmcategoryname) {
		this.tmcategoryname = tmcategoryname;
	}
	

}

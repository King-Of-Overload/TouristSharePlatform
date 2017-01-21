package alan.share.officialstrategy.model;

import java.io.Serializable;
/**
 * 大洲分类
 * @author Salu
 *
 */
public class Destination implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int desid;
	private String desname;
	public int getDesid() {
		return desid;
	}
	public void setDesid(int desid) {
		this.desid = desid;
	}
	public String getDesname() {
		return desname;
	}
	public void setDesname(String desname) {
		this.desname = desname;
	}
	
}

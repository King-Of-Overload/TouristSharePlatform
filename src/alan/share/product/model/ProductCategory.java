package alan.share.product.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 商城一级分类
 * @author Alan
 *
 */
public class ProductCategory implements Serializable {
	private int pcategoryid;
	private String pcategoryname;
	
	private transient List<ProductSecondCategory> secondCategories=new ArrayList<>();
	
	public List<ProductSecondCategory> getSecondCategories() {
		return secondCategories;
	}
	public void setSecondCategories(List<ProductSecondCategory> secondCategories) {
		this.secondCategories = secondCategories;
	}
	public int getPcategoryid() {
		return pcategoryid;
	}
	public void setPcategoryid(int pcategoryid) {
		this.pcategoryid = pcategoryid;
	}
	public String getPcategoryname() {
		return pcategoryname;
	}
	public void setPcategoryname(String pcategoryname) {
		this.pcategoryname = pcategoryname;
	}
	public ProductCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

package alan.share.officialstrategy.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import alan.share.officialstrategy.model.City;
import alan.share.officialstrategy.model.CityIndexRecommend;
import alan.share.officialstrategy.model.Cuision;
import alan.share.officialstrategy.model.Destination;
import alan.share.officialstrategy.model.LocalInfomation;
import alan.share.officialstrategy.model.Location;
import alan.share.officialstrategy.model.OfficialStrategy;
import alan.share.officialstrategy.model.Provinces;
import alan.share.officialstrategy.model.Routes;
import alan.share.officialstrategy.model.TourismAttraction;
import alan.share.officialstrategy.model.TourismCategory;
import alan.share.officialstrategy.model.TourismCovers;
/**
 * 官方攻略持久层
 * @author Alan
 *
 */
public class OfficialStrategyDao extends HibernateDaoSupport{
/**
 * 查询所有官方攻略
 * @return
 */
	@SuppressWarnings("unchecked")
	public List<OfficialStrategy> queryAllOfficialStrategy() {
		String hql="from OfficialStrategy order by clickednum";
		try{
			List<OfficialStrategy> list=this.getHibernateTemplate().find(hql);
			if(list!=null){
				return list;
			}else{
				return null;
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
/**
 * 查询与省份有关的官方攻略
 * @param provinceid
 * @return
 */
  @SuppressWarnings("unchecked")
public List<OfficialStrategy> queryStrategyByPid(int provinceid) {
	  String provinceHQL="from Provinces where provinceid=?";
	  String hql="from OfficialStrategy where provinces=?";
	  try {
		 Provinces province=null; 
		List<Provinces> p=this.getHibernateTemplate().find(provinceHQL,provinceid);
		if(p!=null){
			province=p.get(0);
		}
		List<OfficialStrategy> list=this.getHibernateTemplate().find(hql,province);
		if(list!=null){
			return list;
		}else {
			return null;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
/**
 * 查询与城市相关的攻略
 * @param cityid
 * @return
 */
  @SuppressWarnings("unchecked")
public List<OfficialStrategy> queryStrategyByCid(int cityid) {
	  String hql="from OfficialStrategy where cityid=?";
	  try {
		List<OfficialStrategy> list=this.getHibernateTemplate().find(hql,cityid);
		if(list!=null){
			return list;
		}else{
			return null;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
  /**
   * 查询城市的信息
   * @return
   */
  @SuppressWarnings("unchecked")
public City getCity(String cityName){
	  String hql="FROM City WHERE cityname=?";
	  City result=null;
	  try{
		  List<City> cityList= this.getHibernateTemplate().find(hql,cityName);
		  if(cityList!=null&&cityList.size()>0){
			 result=cityList.get(0); 
		  }
	  }catch(Exception e){
		e.printStackTrace();  
	  }
	  return result;
  }

  /**
   * 通过关键字查找官方攻略
   * @param searchKey
   * @return
   */
@SuppressWarnings("unchecked")
public List<OfficialStrategy> findOStrategyBySearchValue(String searchKey) {
	String hql="FROM OfficialStrategy WHERE ostrategyname LIKE '%"+searchKey+"%' OR ostrategybref LIKE '%"+searchKey+"%' "
			+ "OR provinces.provincename LIKE '%"+searchKey+"%'";
	List<OfficialStrategy> results=null;
	try {
		List<OfficialStrategy> list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 官方攻略根据点击量降序
 * @return
 */
@SuppressWarnings("unchecked")
public List<OfficialStrategy> findAllOStrategyDesc() {
	String hql="FROM OfficialStrategy ORDER BY clickednum DESC";
	List<OfficialStrategy> results=null;
	try {
		List<OfficialStrategy> list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 查找所有的大洲分类
 * @return
 */
@SuppressWarnings("unchecked")
public List<Destination> findAllDestinations() {
	List<Destination> results=new ArrayList<>();
	String hql="FROM Destination";
	try {
		List<Destination> list=this.getHibernateTemplate().find(hql);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 *  根据城市id查找城市首页信息
 * @param cityId 城市id
 * @return
 */
@SuppressWarnings("unchecked")
public LocalInfomation findLocalInformationByCityid(int cityId) {
	String hql="FROM LocalInfomation WHERE cityid=?";
	LocalInfomation infomation=null;
	try {
		List<LocalInfomation> list=this.getHibernateTemplate().find(hql,cityId);
		if(list.size()>0){
			infomation=list.get(0);
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return infomation;
}

/**
 * 保存地点主页信息对象
 * @param infomation
 * @return
 */
public Boolean saveLocalInformation(LocalInfomation infomation) {
	Boolean result=false;
	try {
		this.getHibernateTemplate().save(infomation);
		result=true;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}

/**
 * 更新地点主页信息
 * @param infomation
 * @return
 */
public Boolean updateLocalInformation(LocalInfomation infomation) {
	Boolean result=false;
	try {
		this.getHibernateTemplate().update(infomation);
		result=true;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}

/**
 * 根据城市id查询城市对象
 * @param cityid
 * @return
 */
@SuppressWarnings("unchecked")
public City findCityByCityId(int cityid) {
	City city=new City();
	String hql="FROM City WHERE cityid=?";
	try {
		List<City> list=this.getHibernateTemplate().find(hql,cityid);
		if(list.size()>0){
			city=list.get(0);
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return city;
}
/**
 * 根据城市对象与类型查询周边地点集合
 * @param city
 * @param type
 * @return
 */
@SuppressWarnings("unchecked")
public List<TourismAttraction> findTourismAttractionListByCityAndType(City city, String type) {
	List<TourismAttraction> results=new ArrayList<>();
	String hql="FROM TourismAttraction WHERE city=? AND tourismtype=?";
	try {
		List<TourismAttraction> list=this.getHibernateTemplate().find(hql,city,type);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 根据type获取地点分类数据
 * @param type
 * @return
 */
@SuppressWarnings("unchecked")
public List<TourismCategory> findTourismCategoryByType(String type) {
	List<TourismCategory> results=new ArrayList<>();
	String hql="FROM TourismCategory WHERE tmcategorytype=?";
	try {
		List<TourismCategory> list=this.getHibernateTemplate().find(hql,type);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 保存地理位置
 * @param location
 */
public void saveLocation(Location location) {
	try {
		this.getHibernateTemplate().save(location);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

/**
 * 根据id查询地点分类对象
 * @param categoryid
 * @return
 */
@SuppressWarnings("unchecked")
public TourismCategory findTourismCategoryById(int categoryid) {
	TourismCategory category=new TourismCategory();
	String hql="FROM TourismCategory WHERE tmcategoryid=?";
	try {
		List<TourismCategory> list=this.getHibernateTemplate().find(hql,categoryid);
		if(list.size()>0){
			category=list.get(0);
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return category;
}

/**
 * 保存地点对象
 * @param attraction
 */
public void saveTourismAttraction(TourismAttraction attraction) {
	try {
		this.getHibernateTemplate().save(attraction);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

/**
 * 根据id查找地点对象
 * @param attractionid
 * @return
 */
@SuppressWarnings("unchecked")
public TourismAttraction findTourismAttractionById(String attractionid) {
	TourismAttraction attraction=new TourismAttraction();
	String hql="FROM TourismAttraction WHERE tourismid=?";
	try {
		List<TourismAttraction> list=this.getHibernateTemplate().find(hql,attractionid);
		if(list.size()>0){
			attraction=list.get(0);
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return attraction;
}

/**
 * 保存地点图片对象
 * @param covers
 */
public void saveTourismCovers(TourismCovers covers) {
	try {
		this.getHibernateTemplate().save(covers);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

/**
 * 根据美食对象查询相关的菜品
 * @param attraction
 * @return
 */
@SuppressWarnings("unchecked")
public List<Cuision> findAllCuisionData(TourismAttraction attraction) {
	List<Cuision> results=new ArrayList<>();
	String hql="FROM Cuision WHERE attraction=?";
	try {
		List<Cuision> list=this.getHibernateTemplate().find(hql,attraction);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 保存菜品对象
 * @param cuision
 */
public void saveCuisionObject(Cuision cuision) {
	try {
		this.getHibernateTemplate().save(cuision);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

/**
 * 根据城市对象查询路线
 * @param city
 * @return
 */
@SuppressWarnings("unchecked")
public List<Routes> findRoutesByCity(City city) {
	List<Routes> results=new ArrayList<>();
	String hql="FROM Routes WHERE city=?";
	try {
		List<Routes> list=this.getHibernateTemplate().find(hql,city);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 保存推荐路线对象
 * @param route
 */
public void saveRoute(Routes route) {
	try {
		this.getHibernateTemplate().save(route);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

/**
 * 根据城市对象查询推荐信息
 * @param city
 * @return
 */
@SuppressWarnings("unchecked")
public List<CityIndexRecommend> findCityRecommendsByCity(City city) {
	List<CityIndexRecommend> results=new ArrayList<>();
	String hql="FROM CityIndexRecommend WHERE city=?";
	try {
		List<CityIndexRecommend> list=this.getHibernateTemplate().find(hql,city);
		if(list.size()>0){
			results=list;
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}

/**
 * 保存城市主页推荐对象
 * @param recommend
 * @return
 */
public Boolean saveCityIndexRecommend(CityIndexRecommend recommend) {
	Boolean result=false;
	try {
		this.getHibernateTemplate().save(recommend);
		result=true;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


	
	
}

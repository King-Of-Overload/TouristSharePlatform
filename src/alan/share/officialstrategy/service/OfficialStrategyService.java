package alan.share.officialstrategy.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import alan.share.officialstrategy.dao.OfficialStrategyDao;
import alan.share.officialstrategy.model.City;
import alan.share.officialstrategy.model.CityIndexRecommend;
import alan.share.officialstrategy.model.Cuision;
import alan.share.officialstrategy.model.Destination;
import alan.share.officialstrategy.model.LocalInfomation;
import alan.share.officialstrategy.model.Location;
import alan.share.officialstrategy.model.OfficialStrategy;
import alan.share.officialstrategy.model.Routes;
import alan.share.officialstrategy.model.TourismAttraction;
import alan.share.officialstrategy.model.TourismCategory;
import alan.share.officialstrategy.model.TourismCovers;
import alan.share.utils.UUIDUtils;
import sun.security.krb5.internal.crypto.Des;

public class OfficialStrategyService {
	private OfficialStrategyDao officialStrategyDao;

	public OfficialStrategyDao getOfficialStrategyDao() {
		return officialStrategyDao;
	}

	public void setOfficialStrategyDao(OfficialStrategyDao officialStrategyDao) {
		this.officialStrategyDao = officialStrategyDao;
	}

	//获取所有官方攻略信息
	public List<OfficialStrategy> getAllOfficialStrategy() {
		return officialStrategyDao.queryAllOfficialStrategy();
	}

	//根据省份id查询与该省份有关的攻略
	public List<OfficialStrategy> findStrategyByPid(int provinceid) {
		return officialStrategyDao.queryStrategyByPid(provinceid);
	}
    //根据城市id查询攻略
	public List<OfficialStrategy> findStrategyByCid(int cityid) {
		return officialStrategyDao.queryStrategyByCid(cityid);
	}
	/**
	 * 查找城市
	 * @param cityName
	 * @return
	 */
	public City getCity(String cityName){
		return officialStrategyDao.getCity(cityName);
	}

	/**
	 * 通过关键字查找官方攻略
	 * @param searchKey
	 * @return
	 */
	public List<OfficialStrategy> findOStrategyBySearchValue(String searchKey) {
		return officialStrategyDao.findOStrategyBySearchValue(searchKey);
	}

	/**
	 * 查出所有官方攻略数据降序
	 * @return
	 */
	public List<OfficialStrategy> findAllOStrategyDesc() {
		return officialStrategyDao.findAllOStrategyDesc();
	}

	/**
	 * 查找所有的大洲分类
	 * @return
	 */
	public List<Destination> findAllDestinations() {
		List<Destination> result=new ArrayList<>();
		try {
			result=officialStrategyDao.findAllDestinations();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 保存或修改城市首页信息
	 * @param localCover
	 * @param local_coverFileName
	 * @param cityId
	 * @param localBref
	 * @param notmissContent
	 * @param notmissText
	 * @param howToArrive
	 * @param traffic
	 * @param guide
	 * @param tips
	 * @return
	 */
	public Boolean saveLocalInformation(File localCover, String local_coverFileName, int cityId, String localBref,
			String notmissContent, String notmissText, String howToArrive, String traffic, String guide, String tips) {
		Boolean result=false;
		try {
			LocalInfomation existInformation=officialStrategyDao.findLocalInformationByCityid(cityId);
			LocalInfomation infomation=new LocalInfomation();
			if(null!=existInformation){//更新
				infomation=existInformation;
			}else{
				infomation.setLocalid(UUIDUtils.getUUID());
			}
			infomation.setCityid(cityId);
			infomation.setLocalarrive(howToArrive);
			infomation.setLocalbref(localBref);
			infomation.setLocalcannotmisscontent(notmissContent);
			infomation.setLocalcannotmissplaintext(notmissText);
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			infomation.setLocaldate(format.parse(format.format(new Date())));
			infomation.setLocalguide(guide);
			infomation.setLocaltips(tips);
			infomation.setLocaltraffic(traffic);
			format=new SimpleDateFormat("yyyyMMddHHmmss");
			String myFileName=format.format(new Date())+local_coverFileName;
			String storagePath=ServletActionContext.getServletContext().getRealPath("/images/localcover");
			if(null!=localCover){
				File saveFile=new File(new File(storagePath),myFileName);
				if(!saveFile.getParentFile().exists()){
					saveFile.getParentFile().mkdirs();
					FileUtils.copyFile(localCover, saveFile);
				}else{
					FileUtils.copyFile(localCover, saveFile);
				}
				infomation.setLocalcover("images/localcover/"+myFileName);
			}
			if(null!=existInformation){//更新
				result=officialStrategyDao.updateLocalInformation(infomation);
			}else{
				result=officialStrategyDao.saveLocalInformation(infomation);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

	/**
	 * 根据城市名获得城市主要信息
	 * @param cityid
	 * @return
	 */
	public LocalInfomation findLocalInformationByCityId(int cityid) {
		LocalInfomation infomation=null;
		try {
			infomation=officialStrategyDao.findLocalInformationByCityid(cityid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return infomation;
	}

	/**
	 * 根据类型与城市id查询周边地点集合
	 * @param type 类型 
	 * @param cityid 城市id
	 * @return
	 */
	public List<TourismAttraction> findTourismAttractionListByCityIdAndType(String type, int cityid) {
		List<TourismAttraction> results=null;
		try {
			City city=officialStrategyDao.findCityByCityId(cityid);
			results=officialStrategyDao.findTourismAttractionListByCityAndType(city,type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	/**
	 * 添加地点信息
	 * @param view
	 * @param cityid
	 * @param tourismname
	 * @param tourismforeignname
	 * @param tourismprice
	 * @param currencytype
	 * @param tourismphone
	 * @param tourismdescription
	 * @param tourismopendesc
	 * @param tourismaddress
	 * @param tourismguide
	 * @param tourismurl
	 * @param tourismactivity
	 * @param longitude
	 * @param latitude
	 * @param categoryid
	 * @return
	 */
	public TourismAttraction saveTourismAttractions(String view, int cityid, String tourismname,
			String tourismforeignname, Float tourismprice, String currencytype, String tourismphone,
			String tourismdescription, String tourismopendesc, String tourismaddress, String tourismguide,
			String tourismurl, String tourismactivity, BigDecimal longitude, BigDecimal latitude,int categoryid) {
		TourismAttraction attraction=new TourismAttraction();
		try {
			City city=officialStrategyDao.findCityByCityId(cityid);
			attraction.setCity(city);
			attraction.setCurrencytype(currencytype);
			Location location=new Location();
			location.setLocationid(UUIDUtils.getUUID());
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			officialStrategyDao.saveLocation(location);
			attraction.setLocation(location);
			attraction.setLocationid(location.getLocationid());
			attraction.setTourismactivity(tourismactivity);
			attraction.setTourismaddress(tourismaddress);
			TourismCategory tourismCategory=officialStrategyDao.findTourismCategoryById(categoryid);
			attraction.setTourismCategory(tourismCategory);
			attraction.setTourismdescription(tourismdescription);
			attraction.setTourismforeignname(tourismforeignname);
			attraction.setTourismguide(tourismguide);
			attraction.setTourismid(UUIDUtils.getUUID());
			attraction.setTourismname(tourismname);
			attraction.setTourismopendesc(tourismopendesc);
			attraction.setTourismphone(tourismphone);
			attraction.setTourismprice(tourismprice);
			attraction.setTourismtype(tourismCategory.getTmcategorytype());
			attraction.setTourismurl(tourismurl);
			officialStrategyDao.saveTourismAttraction(attraction);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return attraction;
	}

	/**
	 * 根据type获取地点分类数据
	 * @param type
	 * @return
	 */
	public List<TourismCategory> findTourismCategoryByType(String type) {
		List<TourismCategory> results=null;
		try {
			results=officialStrategyDao.findTourismCategoryByType(type);
		} catch (Exception e) {
			
		}
		return results;
	}

	/**
	 * 根据id查找地点对象
	 * @param attractionid
	 * @return
	 */
	public TourismAttraction findTourismAttractionById(String attractionid) {
		TourismAttraction attraction=null;
		try {
			attraction=officialStrategyDao.findTourismAttractionById(attractionid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return attraction;
	}

	/**
	 * 保存地点封面图片
	 * @param url
	 * @param attraction
	 */
	public void saveTourismCovers(String url, TourismAttraction attraction) {
		try {
			TourismCovers covers=new TourismCovers();
			covers.setTourismAttraction(attraction);
			covers.setTourismcoverid(UUIDUtils.getUUID());
			covers.setTourismurl(url);
			officialStrategyDao.saveTourismCovers(covers);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据美食id查询相关的菜品
	 * @param attractionid
	 * @return
	 */
	public List<Cuision> findAllCuisionData(String attractionid) {
		List<Cuision> results=null;
		try {
			TourismAttraction attraction=officialStrategyDao.findTourismAttractionById(attractionid);
			results=officialStrategyDao.findAllCuisionData(attraction);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	/**
	 * 保存菜品对象
	 * @param cuisionCover
	 * @param cusionCoverFileName
	 * @param attractionid
	 * @param cuisionname
	 * @param cuisionprice
	 * @param cuisionpricecurrency
	 * @param cuisionforeignprice
	 * @param cuisionforeigncurrency
	 * @param cuisiondescription
	 * @return
	 */
	public Cuision saveCuision(File cuisionCover, String cuisionCoverFileName, String attractionid,String cuisionname,Float cuisionprice,
			String cuisionpricecurrency, Float cuisionforeignprice, String cuisionforeigncurrency,
			String cuisiondescription) {
		Cuision result=null;
		try {
			Cuision cuision=new Cuision();
			TourismAttraction attraction=officialStrategyDao.findTourismAttractionById(attractionid);
			cuision.setAttraction(attraction);
			cuision.setCuisiondescription(cuisiondescription);
			cuision.setCuisionforeigncurrency(cuisionforeigncurrency);
			cuision.setCuisionforeignprice(cuisionforeignprice);
			cuision.setCuisionid(UUIDUtils.getUUID());
			cuision.setCuisionname(cuisionname);
			cuision.setCuisionprice(cuisionprice);
			cuision.setCuisionpricecurrency(cuisionpricecurrency);
			DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			String myFilename=format.format(new Date())+cuisionCoverFileName;
			String storagePath=ServletActionContext.getServletContext().getRealPath("/images/cuisioncover");
			if(null!=cuisionCover){//图片不为空
				File saveFile=new File(new File(storagePath),myFilename);
				if(!saveFile.getParentFile().exists()){
					saveFile.getParentFile().mkdirs();
					FileUtils.copyFile(cuisionCover, saveFile);
				}else{
					FileUtils.copyFile(cuisionCover, saveFile);
				}
				cuision.setCuisionimage("images/cuisioncover/"+myFilename);
			}
			officialStrategyDao.saveCuisionObject(cuision);
			result=cuision;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 根据城市id查询相关路线
	 * @param cityid
	 * @return
	 */
	public List<Routes> findRoutesByCityId(int cityid) {
		List<Routes> results=new ArrayList<>();
		try {
			City city=officialStrategyDao.findCityByCityId(cityid);
			results=officialStrategyDao.findRoutesByCity(city);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	/**
	 * 保存推荐路线对象
	 * @param cityid
	 * @param routename
	 * @param routeCover
	 * @param routeCoverName
	 * @param routeContent
	 * @param routePlainText
	 * @return
	 */
	public Routes saveRoutes(int cityid, String routename, File routeCover, String routeCoverName, String routeContent,
			String routePlainText) {
		Routes result=null;
		try {
			Routes route=new Routes();
			City city=officialStrategyDao.findCityByCityId(cityid);
			route.setCity(city);
			route.setRoutecontent(routeContent);
			route.setRouteid(UUIDUtils.getUUID());
			route.setRoutename(routename);
			route.setRouteplaintext(routePlainText);
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date=new Date();
			route.setRoutedate(format.parse(format.format(date)));
			format=new SimpleDateFormat("yyyyMMddHHmmss");
			String myFilename=format.format(new Date())+routeCoverName;
			String storagePath=ServletActionContext.getServletContext().getRealPath("/images/routecover");
			if(null!=routeCover){//图片不为空
				File saveFile=new File(new File(storagePath),myFilename);
				if(!saveFile.getParentFile().exists()){
					saveFile.getParentFile().mkdirs();
					FileUtils.copyFile(routeCover, saveFile);
				}else{
					FileUtils.copyFile(routeCover, saveFile);
				}
				route.setRoutecover("images/routecover/"+myFilename);
			}
			officialStrategyDao.saveRoute(route);
			result=route;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 根据城市id查找推荐内容集合
	 * @param cityid
	 * @return
	 */
	public List<CityIndexRecommend> findCityRecommendsByCityId(int cityid) {
		List<CityIndexRecommend> results=new ArrayList<>();
		try {
			City city=officialStrategyDao.findCityByCityId(cityid);
			results=officialStrategyDao.findCityRecommendsByCity(city);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return results;
	}

	/**
	 * 保存城市主页推荐数据
	 * @param title
	 * @param coverFile
	 * @param coverName
	 * @param desc
	 * @param recommendContent
	 * @param cityid
	 * @return
	 */
	public Boolean saveCityIndexRecommend(String title, File coverFile, String coverName, String desc,
			String recommendContent, int cityid) {
		Boolean result=false;
		try {
			CityIndexRecommend recommend=new CityIndexRecommend();
			City city=officialStrategyDao.findCityByCityId(cityid);
			recommend.setCity(city);
			recommend.setCityindexcontent(recommendContent);
			DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			String myFileName=format.format(new Date())+coverName;
			String storagePath=ServletActionContext.getServletContext().getRealPath("/images/cityindexrecommend");
			if(null!=coverFile){
				File saveFile=new File(new File(storagePath),myFileName);
				if(!saveFile.getParentFile().exists()){
					saveFile.getParentFile().mkdirs();
					FileUtils.copyFile(coverFile, saveFile);
				}else{
					FileUtils.copyFile(coverFile, saveFile);
				}
				recommend.setCityindexcover("images/cityindexrecommend/"+myFileName);
			}
			recommend.setCityindexdes(desc);
			recommend.setCityindexid(UUIDUtils.getUUID());
			recommend.setCityindexname(title);
			result=officialStrategyDao.saveCityIndexRecommend(recommend);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	
	
}

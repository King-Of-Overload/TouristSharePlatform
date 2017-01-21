package alan.share.user.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.Session;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;

import alan.share.index.dao.IndexDao;
import alan.share.officialstrategy.model.City;
import alan.share.officialstrategy.model.Destination;
import alan.share.officialstrategy.model.Provinces;
import alan.share.photo.dao.PhotoDao;
import alan.share.photo.model.UserAlbums;
import alan.share.photo.model.UserPhotos;
import alan.share.product.model.OrderItem;
import alan.share.product.model.UserCollections;
import alan.share.skillacademy.dao.SkillAcademyDao;
import alan.share.skillacademy.model.SkillAcademy;
import alan.share.sort.SortSpaceBean;
import alan.share.user.dao.TripUserDao;
import alan.share.user.model.BestChoose;
import alan.share.user.model.PayAttention;
import alan.share.user.model.SecrectMessage;
import alan.share.user.model.TripUser;
import alan.share.user.model.UserFeedBack;
import alan.share.user.model.Visitor;
import alan.share.userstrategy.dao.UserStrategyDao;
import alan.share.userstrategy.model.UserStrategy;
import alan.share.utils.DomUtil;
import alan.share.utils.ImageBean;
import alan.share.utils.Mail;
import alan.share.utils.MailUtils;
import alan.share.utils.RequestURLs;
import alan.share.utils.SpaceBean;
import alan.share.utils.StringUtil;
import alan.share.utils.UUIDUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
public class TripUserService {
	private TripUserDao userDao;
	private UserStrategyDao strategyDao;
	private PhotoDao photoDao;
	private SkillAcademyDao academyDao;
	private IndexDao indexDao;

	public IndexDao getIndexDao() {
		return indexDao;
	}



	public void setIndexDao(IndexDao indexDao) {
		this.indexDao = indexDao;
	}



	public SkillAcademyDao getAcademyDao() {
		return academyDao;
	}



	public void setAcademyDao(SkillAcademyDao academyDao) {
		this.academyDao = academyDao;
	}



	public PhotoDao getPhotoDao() {
		return photoDao;
	}



	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}



	public TripUserDao getUserDao() {
		return userDao;
	}



	public void setUserDao(TripUserDao userDao) {
		this.userDao = userDao;
	}
	



	public UserStrategyDao getStrategyDao() {
		return strategyDao;
	}



	public void setStrategyDao(UserStrategyDao strategyDao) {
		this.strategyDao = strategyDao;
	}



	public TripUser findByUserName(String username) {//根据用户名查找
		return userDao.findByUserName(username);
	}
	
	public TripUser findByActiveCode(String code){//根据用户激活码查找
		return userDao.findByActiveCode(code);
	}


/**
 * 注册用户
 * @param user
 */
	public Boolean save(TripUser user) {
		Boolean result=false;
		//将数据保存到数据库
		user.setUserstate(0);//0:用户未激活  1：用户已经激活
		user.setUserid(UUIDUtils.getUUID());
		String code=UUIDUtils.getUUID()+UUIDUtils.getUUID();//64位随机字符串
		user.setUseractivecode(code);
		userDao.save(user);
		String serverHost="smtp.163.com";
		String memailname="yzluxintao@163.com";//邮箱登录名
		String mpassword="19950420,.";//邮箱密码
		Session session=MailUtils.createSession(serverHost, memailname, mpassword);
		String sender=memailname;//发件人
		String reseiver=user.getUseremail();//收件人
		String theme="来自芳草寻源旅游共享平台的官方激活邮件";//主题
		String content="<h1>恭喜您成为芳草寻源大家庭中的一员，请点击下面链接完成激活操作！</h1><h3><a href='http://10.248.142.128:8080/TouristSharePlatform/tripuser_active.do?code="+code+"'>http://10.248.142.128:8080/TouristSharePlatform/tripuser_active.do?code="+code+"</a></h3>";
		Mail mail=new Mail(sender,reseiver,theme,content);
		try {
			MailUtils.send(session, mail);//发送邮件
			result=true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}


/**
 * 更新用户基本信息
 * @param originalUser
 */
public void update(TripUser existUser) {
	userDao.update(existUser);
}


/**
 * 登录方法
 * @param username
 * @return
 */
public TripUser findByUserNameAndPassword(String username,String password) {
	return userDao.findByUserNameAndPassword(username,password);
}


/**
 * 将用户信息转换为json对象
 * @param existUser
 * @return
 */
public JSONObject parseUserToJSONObject(TripUser existUser) {
	JSONObject jObject=new JSONObject();
	jObject.put("username", existUser.getUsername());
	jObject.put("email", existUser.getUseremail());
	jObject.put("phone", existUser.getPhone());
	jObject.put("sex", existUser.getSex());
	jObject.put("headerimage", existUser.getHeaderimage());
	return jObject;
}


/**
 * 从json字符串中获取图片裁剪信息
 * @param imageClipParameter json数组
 * @return
 * {"x":287.4148351648352,"y":99.06318681318683,"height":428,"width":428,"rotate":0}
 */
public ImageBean parseImageFromJSON(String imageClipParameter) {
	ImageBean bean=new ImageBean();
	JSONObject jsonObject=JSONObject.fromString(imageClipParameter);
	bean.setxLength((int)jsonObject.getDouble("x"));
	bean.setyLength((int)jsonObject.getDouble("y"));
	bean.setWidth(jsonObject.getInt("width"));
	bean.setHeight(jsonObject.getInt("height"));
	bean.setRotate(jsonObject.getInt("rotate"));
	return bean;
}


/**
 * 更新用户头像
 * @param userId 用户id
 * @param headerImage 头像名字
 * @return
 */
public Boolean updateUserHeaderImage(TripUser user, String headerImage) {
	return userDao.updateUserHeaderImage(user,headerImage);
}


/**
 * 检查登录状态
 * @return
 */
public TripUser findByUserCookieValue(String userCookieValue) {
	return userDao.findByUserCookieValue(userCookieValue);
}



/**
 * 根据userid查询用户对象
 * @param userid
 * @return
 */
public TripUser findUserByUserId(String userid) {
	return userDao.findUserByUserId(userid);
}



/**
 * 读取用户粉丝数
 * @param userid
 * @return
 */
public int findCountPayAttentionByFollowId(String userid) {
	return userDao.findCountPayAttentionByFollowId(userid);
}


/**
 * 根据用户id查询最近访问记录
 * @param userid
 * @return
 */
public List<Visitor> findVisitorsByVisitorId(String userid) {
	return userDao.findVisitorsByVisitorId(userid);
}


/**
 * 保存访问用户空间的记录
 * @param currentUser 当前登录用户
 * @param userid 被访问的用户id
 * @throws ParseException 
 */
public void saveUserVisitRecord(TripUser currentUser, String userid) throws ParseException {
	Visitor v=new Visitor();
	v.setUser(currentUser);
	v.setVisitorid(userid);
	Calendar calendar=Calendar.getInstance();
	calendar.setTime(new Date());
	DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date visitTime=format.parse(format.format(calendar.getTime()));
	v.setVisitdate(visitTime);
	userDao.saveUserVisitRecord(v);
}


/**
 * 根据用户名查出关注集合
 * @param visitUsername 用户名
 * @return
 */
public List<PayAttention> findFocusStatus(String userid) {
	TripUser user=userDao.findUserByUserId(userid);
	List<PayAttention> list=userDao.findFocusListByUserObject(user);
	return list;
}

/**
 * 根据用户名统计关注人数
 * @return 
 */
public int findCountFocusByUserId(String userid){
	TripUser user=userDao.findUserByUserId(userid);
	int result=userDao.findCountFocusByUserObject(user);
	return result;
}


/**
 * 更改用户关注与取消关注状态
 * @param currentUserName 当前操作用户
 * @param userid 被操作用户id
 * @param mode 模式    focus cancel
 * @return
 */
public String saveUserFocusOrCancelStatus(String currentUserId, String userid, String mode) {
	TripUser operateUser=userDao.findUserByUserId(currentUserId);
	return userDao.saveUserFocusOrCancelStatus(operateUser,userid,mode);
}


/**
 * 根据用户id找出粉丝集合
 * @param userid 用户id
 * @return
 */
public List<PayAttention> findPayAttentionListByUserId(String userid) {
	return userDao.findPayAttentionListByUserId(userid);
}
/**
 * 读取用户自己的攻略，相册，技法信息
 * @return
 */
public List<SpaceBean> findUserFreshData(TripUser user){
	List<SpaceBean> results=new ArrayList<>();
	try {
		if(null!=user){
			List<UserStrategy> currentUserStrategys=strategyDao.findUserStrategyByTripUser(user);//查找当前用户的攻略
			if(currentUserStrategys!=null&&currentUserStrategys.size()>0){
				for (UserStrategy u : currentUserStrategys) {
					SpaceBean bean=new SpaceBean();
					bean.setId(u.getUstrategyid());
					bean.setClickedNum(u.getUclickednum());
					bean.setContent(u.getUstrategycontent());
					bean.setCommentNum(0);//TODO:评论数，需要进行修改
					bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(u.getUstrategycontent()));
					bean.setImageList(DomUtil.getAllImageListFromHtmlDoc(u.getUstrategycontent()));
					bean.setDescription(u.getUstrategyplaintext());
					bean.setLikeCount(u.getUlikecount());
					bean.setTime(u.getUstrategydate());
					bean.setTitle(u.getUstrategyname());
					bean.setType("strategy");
					bean.setUser(u.getTripUser());
					bean.setRealHTML(u.getUstrategycontent());
					results.add(bean);
				}
			}
			//查找当前用户的相册
			List<UserAlbums> currentUserAlbums=photoDao.findUserAlbums(user);
			if(currentUserAlbums!=null&&currentUserAlbums.size()>0){
				for (UserAlbums userAlbums : currentUserAlbums) {
					SpaceBean bean=new SpaceBean();
					bean.setClickedNum(userAlbums.getClickednum());
					List<UserPhotos> photos=photoDao.findUserPhotosByAlbum(userAlbums);
					if(photos!=null&&photos.size()>0){
						bean.setCoverImage(photos.get(0).getPhotourl());
						List<String> strs=new ArrayList<>();
						for(UserPhotos up:photos){
							strs.add(RequestURLs.MAIN_URL+up.getPhotourl());
						}
						bean.setImageList(strs);
					}
					bean.setDescription(userAlbums.getAlbumdescription());
					bean.setId(userAlbums.getAlbumid());
					bean.setTime(userAlbums.getUploadtime());
					bean.setTitle(userAlbums.getAlbumname());
					bean.setType("album");
					bean.setUser(user);
					results.add(bean);
				}
			}
			//查找当前用户的技法文章
			List<SkillAcademy> currentUserAcademys=academyDao.findUserSkillAcademyByTripUser(user);
			if(currentUserAcademys!=null&&currentUserAcademys.size()>0){
				for (SkillAcademy s : currentUserAcademys) {
					SpaceBean bean=new SpaceBean();
					bean.setClickedNum(s.getClickednum());
					bean.setCommentNum(indexDao.findCountCommentsNumber(s.getSkilid()));
					bean.setContent(s.getSkillcontent());
					bean.setIsessence(s.getIsessence());
					bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
					bean.setImageList(DomUtil.getAllImageListFromHtmlDoc(s.getSkillcontent()));
					bean.setDescription(s.getSkillplaintext());
					bean.setId(s.getSkilid());
					bean.setTime(s.getSkilldate());
					bean.setTitle(s.getSkilltitle());
					bean.setType("skillacademy");
					bean.setUser(user);
					bean.setRealHTML(s.getSkillcontent());
					results.add(bean);
				}
			}
		}else{
			throw new RuntimeException("用户空");
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}


/**
 * 读取用户的动态信息，包括用户自己的信息与关注列表当中的信息
 * 手机端API 适配九宫格图片显示方式
 * @param currentUser 当前用户
 * @param focusUsers 关注用户
 * @return
 */
@SuppressWarnings("unchecked")
public List<SpaceBean> findUserFreshThingsForMobile(TripUser currentUser, List<TripUser> focusUsers) {
	List<SpaceBean> results=this.findUserFreshData(currentUser);//查找用户自己的信息
		//查询关注列表中用户的动态
		if(null!=focusUsers&&!focusUsers.isEmpty()){
			for(int i=0;i<focusUsers.size();i++){
				TripUser focusUser=focusUsers.get(i);
				List<UserStrategy> focusUserStrategys=strategyDao.findUserStrategyByTripUser(focusUser);//查找当前用户的攻略
				if(focusUserStrategys!=null&&focusUserStrategys.size()>0){
					for (UserStrategy u : focusUserStrategys) {
						SpaceBean bean=new SpaceBean();
						bean.setId(u.getUstrategyid());
						bean.setClickedNum(u.getUclickednum());
						bean.setContent(u.getUstrategycontent());
						bean.setCommentNum(0);//评论数，需要进行修改
						bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(u.getUstrategycontent()));
						bean.setImageList(DomUtil.getAllImageListFromHtmlDoc(u.getUstrategycontent()));
						bean.setDescription(u.getUstrategyplaintext());
						bean.setLikeCount(u.getUlikecount());
						bean.setTime(u.getUstrategydate());
						bean.setTitle(u.getUstrategyname());
						bean.setType("strategy");
						bean.setUser(u.getTripUser());
						results.add(bean);
					}
				}
				//查找关注用户的相册
				List<UserAlbums> focusUserAlbums=photoDao.findUserAlbums(focusUser);
				if(focusUserAlbums!=null&&focusUserAlbums.size()>0){
					for (UserAlbums userAlbums : focusUserAlbums) {
						SpaceBean bean=new SpaceBean();
						bean.setClickedNum(userAlbums.getClickednum());
						List<UserPhotos> photos=photoDao.findUserPhotosByAlbum(userAlbums);
						if(photos!=null&&photos.size()>0){
							List<String> imgs=new ArrayList<>();
							bean.setCoverImage(photos.get(0).getPhotourl());
							for(UserPhotos p:photos){
								imgs.add(RequestURLs.MAIN_URL+p.getPhotourl());
							}
							bean.setImageList(imgs);
							}
						bean.setDescription(userAlbums.getAlbumdescription());
						bean.setId(userAlbums.getAlbumid());
						bean.setTime(userAlbums.getUploadtime());
						bean.setTitle(userAlbums.getAlbumname());
						bean.setType("album");
						bean.setUser(userAlbums.getTripUser());
						results.add(bean);
					}
				}
				//查找关注用户的技法文章
				List<SkillAcademy> focusUserAcademys=academyDao.findUserSkillAcademyByTripUser(focusUser);
				if(focusUserAcademys!=null&&focusUserAcademys.size()>0){
					for (SkillAcademy s : focusUserAcademys) {
						SpaceBean bean=new SpaceBean();
						bean.setClickedNum(s.getClickednum());
						bean.setContent(s.getSkillcontent());
						bean.setIsessence(s.getIsessence());
						bean.setCommentNum(indexDao.findCountCommentsNumber(s.getSkilid()));
						bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
						bean.setImageList(DomUtil.getAllImageListFromHtmlDoc(s.getSkillcontent()));
						bean.setDescription(s.getSkillplaintext());
						bean.setId(s.getSkilid());
						bean.setTime(s.getSkilldate());
						bean.setTitle(s.getSkilltitle());
						bean.setType("skillacademy");
						bean.setUser(s.getUser());
						results.add(bean);
					}
				}
			}	
		}
		//对数据进行排序
		SortSpaceBean sortBean=new SortSpaceBean();
		if(results!=null&&results.size()>0){
			Collections.sort(results, sortBean);
			Collections.reverse(results);//降序排列
		}
	return results;
}

/**
 * 读取用户的动态信息，包括用户自己的信息与关注列表当中的信息
 * @param currentUser 当前用户
 * @param focusUsers 关注用户
 * @return
 */
@SuppressWarnings("unchecked")
public List<SpaceBean> findUserFreshThings(TripUser currentUser, List<TripUser> focusUsers) {
	List<SpaceBean> results=new ArrayList<>();
	List<UserStrategy> currentUserStrategys=strategyDao.findUserStrategyByTripUser(currentUser);//查找当前用户的攻略
	if(currentUserStrategys!=null&&currentUserStrategys.size()>0){
		for (UserStrategy u : currentUserStrategys) {
			SpaceBean bean=new SpaceBean();
			bean.setId(u.getUstrategyid());
			bean.setClickedNum(u.getUclickednum());
			bean.setCommentNum(0);//评论数，需要进行修改
			bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(u.getUstrategycontent()));
			if(u.getUstrategyplaintext().length()>200){
				bean.setDescription(u.getUstrategyplaintext().substring(0, 200)+"……");
			}else{
				bean.setDescription(u.getUstrategyplaintext());
			}
			bean.setLikeCount(u.getUlikecount());
			bean.setTime(u.getUstrategydate());
			bean.setTitle(u.getUstrategyname());
			bean.setType("strategy");
			bean.setUser(u.getTripUser());
			results.add(bean);
		}
	}
		//查找当前用户的相册
		List<UserAlbums> currentUserAlbums=photoDao.findUserAlbums(currentUser);
		if(currentUserAlbums!=null&&currentUserAlbums.size()>0){
			for (UserAlbums userAlbums : currentUserAlbums) {
				SpaceBean bean=new SpaceBean();
				bean.setClickedNum(userAlbums.getClickednum());
				List<UserPhotos> photos=photoDao.findUserPhotosByAlbum(userAlbums);
				if(photos!=null&&photos.size()>0){bean.setCoverImage(photos.get(0).getPhotourl());}
				bean.setDescription(userAlbums.getAlbumdescription());
				bean.setId(userAlbums.getAlbumid());
				bean.setTime(userAlbums.getUploadtime());
				bean.setTitle(userAlbums.getAlbumname());
				bean.setType("album");
				bean.setUser(currentUser);
				results.add(bean);
			}
		}
		//查找当前用户的技法文章
		List<SkillAcademy> currentUserAcademys=academyDao.findUserSkillAcademyByTripUser(currentUser);
		if(currentUserAcademys!=null&&currentUserAcademys.size()>0){
			for (SkillAcademy s : currentUserAcademys) {
				SpaceBean bean=new SpaceBean();
				bean.setClickedNum(s.getClickednum());
				bean.setCommentNum(indexDao.findCountCommentsNumber(s.getSkilid()));
				bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
				bean.setDescription(s.getSkillplaintext().substring(0, 300));
				bean.setId(s.getSkilid());
				bean.setTime(s.getSkilldate());
				bean.setTitle(s.getSkilltitle());
				bean.setType("skillacademy");
				bean.setUser(currentUser);
				results.add(bean);
			}
		}
		//查询关注列表中用户的动态
		if(null!=focusUsers&&!focusUsers.isEmpty()){
			for(int i=0;i<focusUsers.size();i++){
				TripUser focusUser=focusUsers.get(i);
				List<UserStrategy> focusUserStrategys=strategyDao.findUserStrategyByTripUser(focusUser);//查找当前用户的攻略
				if(focusUserStrategys!=null&&focusUserStrategys.size()>0){
					for (UserStrategy u : focusUserStrategys) {
						SpaceBean bean=new SpaceBean();
						bean.setId(u.getUstrategyid());
						bean.setClickedNum(u.getUclickednum());
						bean.setCommentNum(0);//评论数，需要进行修改
						bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(u.getUstrategycontent()));
						bean.setDescription(u.getUstrategyplaintext().substring(0, 100)+"……");
						bean.setLikeCount(u.getUlikecount());
						bean.setTime(u.getUstrategydate());
						bean.setTitle(u.getUstrategyname());
						bean.setType("strategy");
						bean.setUser(u.getTripUser());
						results.add(bean);
					}
				}
				//查找关注用户的相册
				List<UserAlbums> focusUserAlbums=photoDao.findUserAlbums(focusUser);
				if(focusUserAlbums!=null&&focusUserAlbums.size()>0){
					for (UserAlbums userAlbums : focusUserAlbums) {
						SpaceBean bean=new SpaceBean();
						bean.setClickedNum(userAlbums.getClickednum());
						List<UserPhotos> photos=photoDao.findUserPhotosByAlbum(userAlbums);
						if(photos!=null&&photos.size()>0){bean.setCoverImage(photos.get(0).getPhotourl());}
						bean.setDescription(userAlbums.getAlbumdescription());
						bean.setId(userAlbums.getAlbumid());
						bean.setTime(userAlbums.getUploadtime());
						bean.setTitle(userAlbums.getAlbumname());
						bean.setType("album");
						bean.setUser(userAlbums.getTripUser());
						results.add(bean);
					}
				}
				//查找关注用户的技法文章
				List<SkillAcademy> focusUserAcademys=academyDao.findUserSkillAcademyByTripUser(focusUser);
				if(focusUserAcademys!=null&&focusUserAcademys.size()>0){
					for (SkillAcademy s : focusUserAcademys) {
						SpaceBean bean=new SpaceBean();
						bean.setClickedNum(s.getClickednum());
						bean.setCommentNum(indexDao.findCountCommentsNumber(s.getSkilid()));
						bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
						bean.setDescription(s.getSkillplaintext().substring(0, 300));
						bean.setId(s.getSkilid());
						bean.setTime(s.getSkilldate());
						bean.setTitle(s.getSkilltitle());
						bean.setType("skillacademy");
						bean.setUser(s.getUser());
						results.add(bean);
					}
				}
			}	
		}
		//对数据进行排序
		SortSpaceBean sortBean=new SortSpaceBean();
		if(results!=null&&results.size()>0){
			Collections.sort(results, sortBean);
			Collections.reverse(results);//降序排列
		}
	return results;
}



/**
 * 保存悄悄话内容
 * @param sender 发送者
 * @param receiver 接收者
 * @param secretMessage 悄悄话
 * @return
 * @throws ParseException 
 */
public Boolean saveUserSecretWord(TripUser sender, TripUser receiver, String secretMessage) throws ParseException {
	SecrectMessage smessage=new SecrectMessage();
	smessage.setIsread(1);
	smessage.setMessage(secretMessage);
	smessage.setReceiver(receiver);
	smessage.setSender(sender);
	smessage.setSmessageid(UUIDUtils.getUUID());
	Calendar calendar=Calendar.getInstance();
	calendar.setTime(new Date());
	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	smessage.setSmessagetime(format.parse(format.format(calendar.getTime())));
	return userDao.saveUserSecretWord(smessage);
}


/**
 * 统计未读消息个数
 * @param currentUser 当前用户
 * @return
 */
public int findCountUnReadMessageSize(TripUser currentUser) {
	return userDao.findCountUnReadMessageSize(currentUser);
}


/**
 * 将用户对象封装成json数组
 * @param existUser
 * @return
 */
public JSONArray enstoreTripUserToJSONArray(TripUser existUser) {
	JSONArray result=new JSONArray();
	JSONObject object=null;
	try {
		object=new JSONObject();
		object.put("userid", existUser.getUserid());
		object.put("username", existUser.getUsername());
		object.put("useremail", existUser.getUseremail());
		object.put("phone", existUser.getPhone());
		object.put("sex", existUser.getSex());
		object.put("city", existUser.getCity().getProvince().getProvincename()+existUser.getCity().getCityname());
		object.put("usignature", existUser.getUsignature());
		result.put(object);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


/**
 * 根据接收者查出所有的留言
 * @param visitUser 接收者
 * @return
 */
public List<SecrectMessage> findMessageByReceiver(TripUser visitUser) {
	return userDao.findMessageByReceiver(visitUser);
}


/**
 * 根据悄悄话id查询悄悄话对象
 * @param messageid 悄悄话id
 * @return
 */
public SecrectMessage findSecretMessageByMid(String messageid) {
	return userDao.findSecretMessageByMid(messageid);
}


/**
 * 更新悄悄话对象
 * @param existMessage 悄悄话对象
 * @return
 */
public Boolean updateSecretMessage(SecrectMessage existMessage) {
	return userDao.updateSecretMessage(existMessage);
}



/**
 * 批量删除
 * @param messageIds
 * @return
 */
public Boolean removeMessages(ArrayList<String> messageIds) {
	Boolean result=false;
	if(messageIds!=null&&messageIds.size()>0){
		for(int i=0;i<messageIds.size();i++){
			String mid=messageIds.get(i);
			SecrectMessage message=userDao.findSecretMessageByMid(mid);
			result=userDao.removeSecrectMessageById(message);
		}
	}
	return result;
}



/**
 * 根据用户对象查询相关收藏集合
 * @return
 */
public List<UserCollections> findUserCollectionsByTripUser(TripUser user) {
	return userDao.findUserCollectionsByTripUser(user);
}



/**
 * 将收藏集合封装成jsonarray
 * @param collections
 * @return
 */
public JSONArray enstoreUserCollectionsToJSONArray(List<UserCollections> collections) {
	JSONArray array=new JSONArray();
	JSONObject object=null;
	try {
		if(null!=collections&&!collections.isEmpty()){
			for(UserCollections c:collections){
				object=new JSONObject();
				object.put("pid", c.getProduct().getPid());
				object.put("productName", c.getProduct().getPname());
				array.put(object);
			}
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return array;
}


/**
 * 根据城市找出用户
 * @param city
 * @return
 */
public List<TripUser> findUsersByCity(City city) {
	return userDao.findUsersByCity(city);
}


/**
 * 查找用户未读的消息
 * @param userid
 * @return
 */
public List<SecrectMessage> findUnReadMessageByUserId(String userid) {
	TripUser user=userDao.findUserByUserId(userid);
	return userDao.findUnReadMessageByUser(user);
}


/**
 * 将悄悄话集合封装成json数组
 * @param messages
 * @return
 */
public JSONArray enstoreSecrectMessageToJSONArray(List<SecrectMessage> messages) {
	JSONArray array=new JSONArray();
	JSONObject object=null;
	for(SecrectMessage m:messages){
		object=new JSONObject();
		object.put("senderName",m.getSender().getUsername());
		object.put("content", m.getMessage());
		array.put(object);
	}
	return array;
}


/**
 * 保存用户反馈信息
 * @param feedbackContent
 * @param feedbackContact
 * @return
 */
public Boolean saveUserFeedBack(String feedbackContent, String feedbackContact) {
	UserFeedBack feedBack=new UserFeedBack();
	feedBack.setFeedbackid(UUIDUtils.getUUID());
	feedBack.setFeedcontent(feedbackContent);
	feedBack.setFeedcontact(feedbackContact);
	return userDao.saveUserFeedBack(feedBack);
}


/**
 * 验证用户登录
 * @param username 
 * @param passwordMD5
 * @return
 */
public TripUser validateTripUser(String username, String passwordMD5) {
	TripUser resultUser=null;
	try {
		TripUser existUser=userDao.findByUserName(username);
		if(existUser!=null){
			String existPassword=existUser.getUserpassword();
			String existPwdMD5=StringUtil.getMD5(existPassword);
			if(existPwdMD5.equals(passwordMD5)){//密码一致
				resultUser=new TripUser();
				resultUser=existUser;
			}
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return resultUser;
}


/**
 * 保存用户登录状态码
 * @param loginStatusCode
 */
public void saveUserLoginStatusCode(TripUser user,String loginStatusCode) {
	user.setMobilelogincode(loginStatusCode);
	try {
		userDao.update(user);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}


/**
 * 删除对应用户的登录码
 * @param userid
 * @return
 */
public Boolean removeUserCodeOverTime(String userid) {
	Boolean result=false;
	try {
		TripUser existUser=userDao.findUserByUserId(userid);
		existUser.setMobilelogincode(null);
		userDao.update(existUser);
		result=true;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


/**
 * 查询我的粉丝当中某一个人我又没有关注，互粉
 * @param userid
 * @param userid2
 * @return
 */
public int findCountFollowInUserById(String userid, String userid2) {
	int result=0;
	try {
		result=userDao.findCountFollowInUserByUser(userDao.findUserByUserId(userid),userid2);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


/**
 * 保存精选数据
 * @param title
 * @param ishot
 * @param htmlContent
 * @param plainText
 * @param imgFile
 * @return
 */
public Boolean saveBestChooseContent(String title, String ishot, String htmlContent, String plainText, File imgFile,String fileName) {
	Boolean result=false;
	try {
		BestChoose bestChoose=new BestChoose();
		bestChoose.setBestclickcount(0);
		bestChoose.setBestcontent(htmlContent);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		bestChoose.setBestdate(format.parse(format.format(calendar.getTime())));
		bestChoose.setBestid(UUIDUtils.getUUID());
		bestChoose.setBestishot(Integer.parseInt(ishot));
		bestChoose.setBestname(title);
		bestChoose.setBestplaintext(plainText);
		String storagePath=ServletActionContext.getServletContext().getRealPath("/images/bestchoosecover");
		format=new SimpleDateFormat("yyyyMMddHHmmss");
		String myfileName=format.format(new Date())+fileName;
		if(imgFile!=null){
			File saveFile=new File(new File(storagePath),myfileName);
			if(!saveFile.getParentFile().exists()){
				saveFile.getParentFile().mkdirs();
				FileUtils.copyFile(imgFile, saveFile);
			}else{
				FileUtils.copyFile(imgFile, saveFile);
			}
		}
		bestChoose.setBestcover("images/bestchoosecover/"+myfileName);
		result=userDao.saveBestChooseData(bestChoose);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


/**
 * 查找出所有精美数据
 * @return
 */
public List<BestChoose> findAllBestChoose() {
	List<BestChoose> result;
	try {
		result=userDao.findAllBestChoose();
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return result;
}


/**
 * 根据大洲分类查找province
 * @param desid
 * @return
 */
public List<Provinces> findAllProvince(int desid) {
	List<Provinces> results=null;
	try {
		Destination destination=userDao.findDestinationByDesId(desid);
		results=userDao.findProvinceByDes(destination);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}


/**
 * 根据省份id查找下属城市
 * @param provinceid
 * @return
 */
public List<City> findCityByPid(int provinceid) {
	List<City> results=null;
	try {
		Provinces p=userDao.findProvinceByPid(provinceid);
		results=userDao.findCityByProvince(p);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}


/**
 * 根据大洲名搜索相关地区集合
 * @param destionName
 * @return
 */
public List<Provinces> findProvincesByDestinationName(String destionName) {
	List<Provinces> results=new ArrayList<>();
	try {
		Destination destination=userDao.findDestinationByDesName(destionName);
		results=userDao.findProvinceByDes(destination);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return results;
}









}

package alan.share.user.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

import alan.share.adapter.MyTypeAdapter;
import alan.share.index.service.IndexService;
import alan.share.officialstrategy.model.City;
import alan.share.officialstrategy.model.Cuision;
import alan.share.officialstrategy.model.Destination;
import alan.share.officialstrategy.model.LocalInfomation;
import alan.share.officialstrategy.model.Provinces;
import alan.share.officialstrategy.model.Routes;
import alan.share.officialstrategy.model.TourismAttraction;
import alan.share.officialstrategy.model.TourismCategory;
import alan.share.officialstrategy.model.TourismCovers;
import alan.share.officialstrategy.service.OfficialStrategyService;
import alan.share.photo.model.UserAlbums;
import alan.share.photo.model.UserPhotos;
import alan.share.photo.service.PhotoService;
import alan.share.product.model.OrderItem;
import alan.share.product.model.Product;
import alan.share.product.model.ProductSecondCategory;
import alan.share.product.model.UserCollections;
import alan.share.product.service.ProductService;
import alan.share.skillacademy.model.SkillAcademy;
import alan.share.skillacademy.service.SkillAcademyService;
import alan.share.sort.SortPersonalFoot;
import alan.share.sort.SortPersonalFootDetailList;
import alan.share.sort.SortSpaceBean;
import alan.share.user.model.BestChoose;
import alan.share.user.model.Comments;
import alan.share.user.model.PayAttention;
import alan.share.user.model.Reply;
import alan.share.user.model.SecrectMessage;
import alan.share.user.model.TripUser;
import alan.share.user.model.Visitor;
import alan.share.user.service.TripUserService;
import alan.share.userstrategy.model.UserStrategy;
import alan.share.userstrategy.service.UserStrategyService;
import alan.share.utilbean.PersonalFoot;
import alan.share.utilbean.PersonalFootDetail;
import alan.share.utils.CookieUtil;
import alan.share.utils.DomUtil;
import alan.share.utils.ImageBean;
import alan.share.utils.ImageCliper;
import alan.share.utils.PageBean;
import alan.share.utils.SingleViewOnRoad;
import alan.share.utils.SpaceBean;
import alan.share.utils.UUIDUtils;
import alan.share.utils.UserSpacePhoto;
import alan.share.utils.ViewOnRoadBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
/**
 * 用户模块Action类，实现模型驱动
 * @author Alan
 *
 */
public class TripUserAction extends ActionSupport implements ServletRequestAware,ServletResponseAware,ModelDriven<TripUser>{
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TripUserService userService;
	private PhotoService photoService;
	private UserStrategyService strategyService;
	private SkillAcademyService academyService;
	private OfficialStrategyService osStrategyService;
	private ProductService productService;
	private IndexService indexService;
	
	protected String dirTemp = "uploadAttractionFile/temp";
	
	private TripUser personalInfo;//用户个人信息
	
	private File avatar_file;//上传头像IO文件
	
	private File best_cover;//精选数据封面
	private String best_coverFileName;//封面名称
	
	private File local_cover;//城市首页图片封面
	private String local_coverFileName;//城市首页图片封面名称
	
	private File cuision_cover;//菜品图片
	private String cuision_coverFileName;//菜品图片名称
	
	private File route_cover;//路线推荐封面文件
	private String route_coverFileName;//路线推荐 图片名称
	
	private static final String FOOD="food";
	private static final String HOTEL="hotel";
	private static final String VIEW="view";
	private static final String SHOP="shop";
	private static final String PLAY="play";
	private static final String LIVE="live";
	
	
	public File getRoute_cover() {
		return route_cover;
	}

	public void setRoute_cover(File route_cover) {
		this.route_cover = route_cover;
	}

	public String getRoute_coverFileName() {
		return route_coverFileName;
	}

	public void setRoute_coverFileName(String route_coverFileName) {
		this.route_coverFileName = route_coverFileName;
	}

	public String getCuision_coverFileName() {
		return cuision_coverFileName;
	}

	public void setCuision_coverFileName(String cuision_coverFileName) {
		this.cuision_coverFileName = cuision_coverFileName;
	}

	public File getCuision_cover() {
		return cuision_cover;
	}

	public void setCuision_cover(File cuision_cover) {
		this.cuision_cover = cuision_cover;
	}

	public File getLocal_cover() {
		return local_cover;
	}

	public void setLocal_cover(File local_cover) {
		this.local_cover = local_cover;
	}

	public String getLocal_coverFileName() {
		return local_coverFileName;
	}

	public void setLocal_coverFileName(String local_coverFileName) {
		this.local_coverFileName = local_coverFileName;
	}

	public String getBest_coverFileName() {
		return best_coverFileName;
	}

	public void setBest_coverFileName(String best_coverFileName) {
		this.best_coverFileName = best_coverFileName;
	}

	public File getBest_cover() {
		return best_cover;
	}

	public void setBest_cover(File best_cover) {
		this.best_cover = best_cover;
	}


	//模型驱动
	@Override
	public TripUser getModel() {
		return personalInfo;
	}
	
	
	

	public ProductService getProductService() {
		return productService;
	}




	public void setProductService(ProductService productService) {
		this.productService = productService;
	}




	public IndexService getIndexService() {
		return indexService;
	}




	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}




	public OfficialStrategyService getOsStrategyService() {
		return osStrategyService;
	}




	public void setOsStrategyService(OfficialStrategyService osStrategyService) {
		this.osStrategyService = osStrategyService;
	}




	public File getAvatar_file() {
		return avatar_file;
	}

	public SkillAcademyService getAcademyService() {
		return academyService;
	}




	public void setAcademyService(SkillAcademyService academyService) {
		this.academyService = academyService;
	}




	public UserStrategyService getStrategyService() {
		return strategyService;
	}




	public void setStrategyService(UserStrategyService strategyService) {
		this.strategyService = strategyService;
	}




	public PhotoService getPhotoService() {
		return photoService;
	}




	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}




	public void setAvatar_file(File avatar_file) {
		this.avatar_file = avatar_file;
	}




	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}



	
	public void setUserService(TripUserService userService) {
		this.userService = userService;
	}
	
	public TripUserService getUserService() {
		return userService;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	/**
	 * 编码
	 * @throws UnsupportedEncodingException
	 */
	private void encodingReqAndRes() throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
	}
	
	/**
	 * 修改用户信息
	 * 手机端API
	 * @param updateType
	 * @param userid
	 * @param other
	 * @return success
	 * @return
	 */
	public String updateUserInfo(){
		PrintWriter out=null;
		String result="";
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			TripUser user=userService.findUserByUserId(userid);
			String updateType=request.getParameter("updateType");
			if(updateType.equals("nickname")){//修改昵称
				String newNickname=request.getParameter("newNickName");
				user.setUsername(newNickname);
				result=newNickname;
			}else if(updateType.equals("sexInfo")){//修改性别
				String newSex=request.getParameter("newSex");
				user.setSex(newSex);
				result=newSex;
			}else if(updateType.equals("cityInfo")){//修改省市
				String cityName=request.getParameter("city");
				//String provinceName=request.getParameter("province");
				City newCity=osStrategyService.getCity(cityName);
				user.setCity(newCity);
				result=cityName;
			}else if(updateType.equals("email")){//修改邮箱
				String newMail=request.getParameter("newEmail");//新的email
				user.setUseremail(newMail);
				result=newMail;
			}else if(updateType.equals("signature")){//个人签名
				String newSignature=request.getParameter("newSignature");
				user.setUsignature(newSignature);
				result=newSignature;
			}else if(updateType.equals("password")){//修改密码
				String newPassword=new String(Base64.getMimeDecoder().decode(request.getParameter("newPassword")));
				String oldPassword=new String(Base64.getMimeDecoder().decode(request.getParameter("oldPassword")));
				if(oldPassword.equals(user.getUserpassword())){
					user.setUserpassword(newPassword);
					result="updateSuccess";
				}else{
					result="updatePwdError";
				}
			}else if(updateType.equals("phone")){//修改手机号
				String newPhone=request.getParameter("phone");
				user.setPhone(newPhone);
				result=newPhone;
			}
			userService.update(user);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}


	
	
	
	//跳转到注册页面执行的action
	public String registerPage(){
		return "registerPage";
	}
	
	//验证用户名是否存在
	public String findByName() throws IOException{
		encodingReqAndRes();
		String username=request.getParameter("username");
		TripUser existUser=userService.findByUserName(username);

		//判断
		if(existUser!=null){
			//查询到该用户，用户已经存在
			response.getWriter().print("ReRegister");
		}else{
			response.getWriter().print("OKRegister");
		}
		return NONE;
	}
	
	
	public String regist() throws Exception{
		encodingReqAndRes();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		TripUser user=new TripUser();
		user.setUsername(username);
		user.setUserpassword(password);
		user.setUseremail(email);
		user.setUserstate(0);
		user.setSex("未知");
		user.setIsmaster(1);
		user.setHeaderimage("images/headerImages/default.jpg");
		City c=osStrategyService.getCity("未知");
		user.setCity(c);
		userService.save(user);
		response.getWriter().print("RegistSuccess");
		return NONE;
	}
	/**
	 * 用户注册
	 * 手机端API 
	 * @param username(用户名) @param passwordEncode(加密后密码，需要解密)
	 * @param email(邮箱)
	 * @return registerSuccess(注册成功) registerError(注册失败)
	 */
	public String registerRemote(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String username=request.getParameter("username");
			String passwordEncode=request.getParameter("passwordEncode");
			String password=new String(Base64.getMimeDecoder().decode(passwordEncode));
			String email=request.getParameter("email");
			System.out.println("用户名："+username+"加密："+passwordEncode+"密码："+password
					+"邮箱："+email);
			TripUser user=new TripUser();
			user.setUsername(username);
			user.setUserpassword(password);
			user.setUseremail(email);
			user.setUserstate(0);
			user.setSex("未知");
			user.setIsmaster(1);
			user.setHeaderimage("images/headerImages/default.jpg");
			City c=osStrategyService.getCity("未知");
			user.setCity(c);
			Boolean result=userService.save(user);
			if(result==true){
				out.print("registerSuccess");
			}else if(result==false){
				out.print("registerError");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 用户账户激活
	 * @return
	 */
	public String active(){
		String code=request.getParameter("code");
		TripUser existUser=userService.findByActiveCode(code);
		if(existUser==null){//激活码错误
			this.addActionMessage("激活失败：激活码与原先不一致");
		}else{
			//激活成功
			//修改用户状态
			existUser.setUserstate(1);
			existUser.setUseractivecode(null);
			userService.update(existUser);//更新数据
			this.addActionMessage("恭喜您激活成功，登录后好好享受吧！");
		}
		return "msg";
	}
	
	/**
	 * 跳转到登录界面
	 * @return
	 */
	public String loginPage(){
		return "loginPage";
	}
	
	/**
	 * 用户登录
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception{
		encodingReqAndRes();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		TripUser existUser=userService.findByUserNameAndPassword(username,password);
		if(existUser==null){
			response.getWriter().print("{'message':'loginERROR'}");
			return NONE;
		}else if(existUser.getUserstate()==0){
			response.getWriter().print("{'message':'notActived'}");
			return NONE;
		}else{
			//登录成功
			//存入cookie
			CookieUtil.saveCookie(existUser, response);
			String userCookie=CookieUtil.userCookie;
			existUser.setUsercookievalue(userCookie);
			userService.update(existUser);
			//将用户信息存入session中
			ServletActionContext.getRequest().getSession().setAttribute("sessionUser", existUser);
			response.getWriter().print("{'message':'"+existUser.getHeaderimage()+"','userid':'"+existUser.getUserid()+"'}");
			return NONE;
		}
	}
	
	/**
	 * 获取用户所有的信息
	 * 手机端API
	 * @param userid 用户id
	 * @return gsonString
	 */
	public String getUserCompleteInfo(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			TripUser user=userService.findUserByUserId(userid);
			if(null!=user){
				//读取粉丝数与关注数
				int followNum=userService.findCountPayAttentionByFollowId(user.getUserid());//读取用户粉丝数
				int focusNum=userService.findCountFocusByUserId(user.getUserid());//读取关注人数
				user.setFollowNum(followNum);
				user.setFocusNum(focusNum);
				Gson gson=new Gson();
				out.print(gson.toJson(user));
			}else{
				out.print("没有该用户");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 登录操作
	 * 手机端API
	 * @param username(用户名) @param passwordMD5(比对加密的密码)
	 * @return loginSuccess(登录成功) loginError(登录失败) JSON
	 */
	public String loginMobile(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String username=request.getParameter("username");
			String passwordMD5=request.getParameter("passwordMD5");
			System.out.println(username+passwordMD5);
			TripUser loginUser=userService.validateTripUser(username,passwordMD5);
			JSONObject object=new JSONObject();
			if(loginUser==null){
				object.put("loginStatus", "loginError");
			}else{
				//读取粉丝数与关注数
				int followNum=userService.findCountPayAttentionByFollowId(loginUser.getUserid());//读取用户粉丝数
				int focusNum=userService.findCountFocusByUserId(loginUser.getUserid());//读取关注人数
				object.put("loginStatus", "loginSuccess");
				object.put("userid", loginUser.getUserid());
				String usignature=loginUser.getUsignature();
				if(null==usignature||("").equals(usignature)){
					object.put("usignature","主人有点懒哦");
				}else{object.put("usignature", usignature);}
				DateFormat format1=new SimpleDateFormat("yyyyMMddHHmmss");
				DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String loginStatusCode=UUIDUtils.getUUID()+format1.format(new Date());
				object.put("loginStatusCode", loginStatusCode);
				object.put("loginTime", format2.format(new Date()));
				object.put("headerImage", loginUser.getHeaderimage());
				object.put("followNum", followNum);
				object.put("focusNum", focusNum);
				object.put("sex", loginUser.getSex());
				object.put("email",loginUser.getUseremail());
				object.put("phone", loginUser.getPhone());
				object.put("cityName", loginUser.getCity().getCityname());
				object.put("provinceName", loginUser.getCity().getProvince().getProvincename());
				userService.saveUserLoginStatusCode(loginUser,loginStatusCode);//保存用户登录状态码
			}
			out.print(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 客户端登录过期
	 * 手机端API
	 * @param userid
	 * @return success error
	 * @return
	 */
	public String removeUserCodeOverTime(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			Boolean result=userService.removeUserCodeOverTime(userid);
			if(result==true){
				out.print("success");
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 检查用户登录状态
	 * 手机端API
	 * @param userid @param loginStatusCode
	 * @return
	 */
	public String checkUserLoginStatus(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			String loginStatusCode=request.getParameter("loginStatusCode");
			 TripUser existUser=userService.findUserByUserId(userid);
			 JSONObject object=new JSONObject();
			 if(null!=existUser.getMobilelogincode()){
				 if(existUser.getMobilelogincode().equals(loginStatusCode)){
					parseOrderItemData(object, existUser);
				 }else{
					 existUser.setMobilelogincode(null);
					 userService.update(existUser);
					 object.put("checkStatus", "illegal");
				 }
			 }else{
				 parseOrderItemData(object, existUser);
			 }
			 
			 out.print(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	private void parseOrderItemData(JSONObject object,TripUser existUser){
		 DateFormat format1=new SimpleDateFormat("yyyyMMddHHmmss");
		 DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String newLoginStatusCode=UUIDUtils.getUUID()+format1.format(new Date());
		 existUser.setMobilelogincode(newLoginStatusCode);
		 userService.update(existUser);
		 object.put("checkStatus", "legal");
		 object.put("loginStatusCode", newLoginStatusCode);
		 object.put("loginTime", format2.format(new Date()));
		 object.put("userid", existUser.getUserid());
		 object.put("username", existUser.getUsername());
		 object.put("headerImage", existUser.getHeaderimage());
		 object.put("sex", existUser.getSex());
		 object.put("email",existUser.getUseremail());
			object.put("phone", existUser.getPhone());
			object.put("cityName", existUser.getCity().getCityname());
			object.put("provinceName", existUser.getCity().getProvince().getProvincename());
		 
	}
	/**
	 * 退出登录
	 * @return
	 * @throws IOException 
	 */
	public String quit() throws IOException{
		encodingReqAndRes();
		//销毁session
		ServletActionContext.getRequest().getSession().invalidate();
		CookieUtil.clearCookie(response);//清除cookie
		PrintWriter out=response.getWriter();
		try{
			out.print("{'message':'quitSUCCESS'}");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取用户空间相关数据
	 * 手机端API
	 * @param userid 用户id
	 * @param loadType 加载类型   mainPage,lightStrategy,album,footStep
	 * @param isCurrentUser 是否为当前用户 true false
	 * @return gsonString
	 */
	public String getUserSpaceData(){
		PrintWriter out=null;
		Gson gson=new Gson();
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			String loadType=request.getParameter("loadType");
			String isCurrentUser=request.getParameter("isCurrentUser");
			TripUser currentUser=userService.findUserByUserId(userid);
			if(isCurrentUser.equals("true")&&loadType.equals("mainPage")){//当前用户且请求主页面数据，需要携带动态
				//读取关注数据
				List<PayAttention> focusList=userService.findFocusStatus(currentUser.getUserid());
				List<TripUser> focusUsers=new ArrayList<>();//所有关注的用户
				if(focusList!=null&&focusList.size()>0){
			      for(int i=0;i<focusList.size();i++){
			    	  String focusId=focusList.get(i).getFollowid();
			    	  TripUser focusUser=userService.findUserByUserId(focusId);
			    	  focusUsers.add(focusUser);
			      }
				}
				List<SpaceBean> freshThings=userService.findUserFreshThingsForMobile(currentUser,focusUsers);//读取关注人与自己的动态
				String result=gson.toJson(freshThings);
				out.print(result);
			}else if(loadType.equals("mainPage")&&isCurrentUser.equals("false")){//其他用户，请求主页数据
				List<SpaceBean> list=new ArrayList<>();
				if(loadType.equals("mainPage")){
					list=userService.findUserFreshData(currentUser);
				}
				String results=gson.toJson(list);
				out.println(results);
			}else if(loadType.equals("lightStrategy")){//加载轻游记
				//TODO:加载轻游记
			}else if(loadType.equals("album")){//加载相册
				List<SpaceBean> albumDatas=photoService.getAlubumSpaceDatasByUser(currentUser);
				String result=gson.toJson(albumDatas);
				out.print(result);
			}else if(loadType.equals("footStep")){//加载足迹
				//TODO:加载足迹数据
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 *获取用户关注与粉丝集合
	 *手机端API
	 *@param userid 用户id 
	 *@param type focus follow
	 * @return gsonString
	 */
	public String getUserFriends(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			String type=request.getParameter("type");
			List<TripUser> users=new ArrayList<>();
			if(type.equals("focus")){//请求关注数
				//读取关注数
				List<PayAttention> focusList=userService.findFocusStatus(userid);
				if(null!=focusList){
				      for(int i=0;i<focusList.size();i++){
				    	  String focusId=focusList.get(i).getFollowid();
				    	  TripUser focusUser=userService.findUserByUserId(focusId);
				    	  focusUser.setFocused(0);
				    	  users.add(focusUser);
				      }
					}
			}else if(type.equals("follow")){//请求粉丝数
				//读取粉丝数
				List<PayAttention> fansList=userService.findPayAttentionListByUserId(userid);
				if(null!=fansList){
					for(PayAttention attention:fansList){
						TripUser u=attention.getUser();
						int result=userService.findCountFollowInUserById(userid,attention.getUser().getUserid());
						if(result==0){
							u.setFocused(1);
						}else{
							u.setFocused(0);
						}
						users.add(u);
					}
				}
			}
			Gson gson=new Gson();
			out.print(gson.toJson(users));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	/**
	 * 跳转到用户空间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String goToSpace(){
		try {
			this.encodingReqAndRes();
			ValueStack stack=ActionContext.getContext().getValueStack();
			String requestType=request.getParameter("requestType");//主页:index 相册:album 技法学院:skillacademy
			String currentUserid=CookieUtil.readCookieReturnId(request);
			TripUser currentUser=userService.findUserByUserId(currentUserid);
			//读取粉丝数
			int fansCount=userService.findCountPayAttentionByFollowId(currentUser.getUserid());
			List<PayAttention> fansList=userService.findPayAttentionListByUserId(currentUser.getUserid());
			//读取关注数
			List<PayAttention> focusList=userService.findFocusStatus(currentUser.getUserid());
			List<TripUser> focusUsers=new ArrayList<>();
			int focusCount=0;
			if(focusList!=null){
		      focusCount=focusList.size();
		      for(int i=0;i<focusList.size();i++){
		    	  String focusId=focusList.get(i).getFollowid();
		    	  TripUser focusUser=userService.findUserByUserId(focusId);
		    	  focusUsers.add(focusUser);
		      }
			}
			if(requestType.equals("index")){
				//读取用户动态
				List<SpaceBean> freshThings=userService.findUserFreshThings(currentUser,focusUsers);
				stack.set("freshThings", freshThings);//动态集合数据
			}else if(requestType.equals("album")){
				List<UserAlbums> albumList=photoService.findUserAlbums(currentUser);//相册集合
				List<SpaceBean> albums=new ArrayList<>();
				for (UserAlbums u : albumList) {
					SpaceBean bean=new SpaceBean();
					bean.setClickedNum(u.getClickednum());
					List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(u.getAlbumid());
					if(photos!=null&&photos.size()>0){
						bean.setCoverImage(photos.get(0).getPhotourl());
					}
					bean.setDescription(u.getAlbumdescription());
					bean.setId(u.getAlbumid());
					bean.setTime(u.getUploadtime());
					bean.setTitle(u.getAlbumname());
					bean.setType("album");
					bean.setUser(currentUser);
					albums.add(bean);
				}
				SortSpaceBean sort=new SortSpaceBean();
				Collections.sort(albums, sort);
				Collections.reverse(albums);
				stack.set("albums", albums);
			}else if(requestType.equals("skillacademy")){
				List<SkillAcademy> academyList=academyService.findSkillAcademyByTripUser(currentUser);
				List<SpaceBean> academies=null;
				if(academyList!=null&&academyList.size()>0){
					academies=new ArrayList<>();
					for (SkillAcademy s : academyList) {
						SpaceBean bean=new SpaceBean();
						bean.setClickedNum(s.getClickednum());
						int cnumber=indexService.findCountCommentsNumberByTopicId(s.getSkilid());
						bean.setCommentNum(cnumber);
						bean.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
						bean.setDescription(s.getSkillplaintext().substring(0, 300)+"……");
						bean.setId(s.getSkilid());
						bean.setTime(s.getSkilldate());
						bean.setTitle(s.getSkilltitle());
						bean.setType("skillacademy");
						bean.setUser(s.getUser());
						academies.add(bean);
					}
				}
				stack.set("academies", academies);//技法学院文章数据
			}
			stack.set("currentUser", currentUser);
			stack.set("fansCount", fansCount);//粉丝数
			stack.set("fansList", fansList);//粉丝集合
			stack.set("focusCount", focusCount);//关注数
			stack.set("focusUsers", focusUsers);//关注集合
			stack.set("requestType", requestType);//请求类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "space";
	}
	
	/**
	 * 跳转到用户详细信息界面
	 * @throws IOException 
	 */
	public String goToUser() throws IOException{
		encodingReqAndRes();
		TripUser existUser=(TripUser) request.getSession().getAttribute("sessionUser");
		if(existUser!=null){
			personalInfo=userService.findByUserName(existUser.getUsername());
			JSONObject jObject=userService.parseUserToJSONObject(personalInfo);
			System.out.println(jObject.toString());
			response.getWriter().print(jObject);
			return "user";
		}else{
			response.getWriter().print("<span class='errorOut'>验证信息已经过期，请重新登录</span>");
			return "loginPage";
		}

	}
	/**
	 * 头像上传
	 * 手机端API
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String uploadHeaderImageMobile(){
		InputStream headerImageStream=null;
		OutputStream os=null;
		PrintWriter out=null;
		try{
		this.encodingReqAndRes();
		String userid=request.getParameter("userid");
			out=response.getWriter();
		TripUser existUser=userService.findUserByUserId(userid);
		String username=existUser.getUsername();
	       // 创建汉语拼音处理类  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        // 输出设置，大小写，音标方式  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		String headerName=PinyinHelper.toHanyuPinyinString(username, defaultFormat, "");
		headerImageStream=new FileInputStream(avatar_file);
		String rootPath=request.getRealPath("/images/headerImages");
		File destFile=new File(rootPath,headerName+".jpg");
		String path=destFile.getPath();//\TouristSharePlatform\images\headerImages\tangyurou.jpg
		String headerFileName=path.substring(path.lastIndexOf('\\')+1);
		System.out.println(headerFileName);
		os=new FileOutputStream(destFile);
		byte[] b=new byte[1024];
		int length=0;
		while((length=headerImageStream.read(b))>0){
			os.write(b, 0, length);
		}
		//更新数据库
		Boolean result=userService.updateUserHeaderImage(existUser,"images/headerImages/"+headerFileName);
		if(result==true){
			out.print("images/headerImages/"+headerFileName);
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
				try {
					if(headerImageStream!=null) headerImageStream.close();
					if(os!=null) os.close();
					if(out!=null) out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return NONE;
	}
	
	
	/**
	 * 头像上传
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("deprecation")
	public String uploadHeaderImage() throws UnsupportedEncodingException{
		this.encodingReqAndRes();
		String username=request.getParameter("avatar-username");//用户名
		username=URLDecoder.decode(username, "UTF-8");
		InputStream headerImageStream=null;
		OutputStream os=null;
		PrintWriter out=null;
		try{
			out=response.getWriter();
		String imageClipParameter=request.getParameter("avatar_data");// {"x":287.4148351648352,"y":99.06318681318683,"height":428,"width":428,"rotate":0}
		TripUser existUser=userService.findByUserName(username);
	       // 创建汉语拼音处理类  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        // 输出设置，大小写，音标方式  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		String headerName=PinyinHelper.toHanyuPinyinString(username, defaultFormat, "");
		ImageBean imgBean=userService.parseImageFromJSON(imageClipParameter);
		headerImageStream=ImageCliper.cutImage(avatar_file, imgBean.getxLength(),imgBean.getyLength(),imgBean.getWidth(),imgBean.getHeight());
		String rootPath=request.getRealPath("/images/headerImages");
		File destFile=new File(rootPath,headerName+".jpg");
		String path=destFile.getPath();//\TouristSharePlatform\images\headerImages\tangyurou.jpg
		String headerFileName=path.substring(path.lastIndexOf('\\')+1);
		System.out.println(headerFileName);
		os=new FileOutputStream(destFile);
		byte[] b=new byte[1024];
		int length=0;
		while((length=headerImageStream.read(b))>0){
			os.write(b, 0, length);
		}
		//更新数据库
		Boolean result=userService.updateUserHeaderImage(existUser,"images/headerImages/"+headerFileName);
		if(result==true){
			String returnPath="images/headerImages/"+headerFileName;
			out.print(" {\"result\":\""+returnPath+"\"}");//"恭喜您，头像上传成功，请刷新后查看"
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
				try {
					if(headerImageStream!=null) headerImageStream.close();
					if(os!=null) os.close();
					if(out!=null) out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return NONE;
	}
	
	/**
	 * 跳转到用户的空间(非用户空间)
	 * @return
	 */
	public String goToUserSpace(){
		try {
			this.encodingReqAndRes();
			ValueStack stack=ActionContext.getContext().getValueStack();
			String requestType=request.getParameter("requestType");//请求类型  strategy album skillacademy
			String userid=request.getParameter("userid");
			int page=Integer.parseInt(request.getParameter("page"));
			String currentIdentity="";
			String focusStatus="关注";
			TripUser existUser=userService.findUserByUserId(userid);//用户信息
			//添加访客记录
			String visitUserId=CookieUtil.readCookieReturnId(request);//当前访问者的用户名
			if(!existUser.getUserid().equals(visitUserId)&&visitUserId!=null){
				TripUser currentUser=userService.findUserByUserId(visitUserId);//查出当前的登录用户
				userService.saveUserVisitRecord(currentUser,existUser.getUserid());//保存访问记录
				currentIdentity="visitor";
			}else if(existUser.getUserid().equals(visitUserId)){
				currentIdentity="owner";
			}else{currentIdentity="visitor";}
			int fansNumber=userService.findCountPayAttentionByFollowId(userid);//查询粉丝数
			//最新相册查询
			List<UserAlbums> albums=photoService.findUserAlbums(existUser);
			List<UserSpacePhoto> showAlbums=new ArrayList<>();//最新相册数据
			int length=0;
			if(albums!=null&&albums.size()>0){
				if(albums.size()>12){
					length=12;
				}else{
					length=albums.size();
				}
				UserSpacePhoto p=null;
				for(int i=0;i<length;i++){
					UserAlbums album=albums.get(i);
					List<UserPhotos> photoList=photoService.findUserPhotosByAlbumId(album.getAlbumid());
					if(photoList!=null&&photoList.size()>0){
						p=new UserSpacePhoto();
						p.setAlbumid(album.getAlbumid());
						p.setPhotourl(photoList.get(0).getPhotourl());
						showAlbums.add(p);
					}
				}
			}
			//读取最近访问记录
			List<Visitor> visitors=userService.findVisitorsByVisitorId(existUser.getUserid());
			List<Visitor> vs=new ArrayList<>();
			if(visitors!=null){
				int vLen=0;
				if(visitors.size()>8){
					vLen=8;
				}else{
					vLen=visitors.size();
				}
				for(int i=0;i<vLen;i++){
					Visitor v=visitors.get(i);
					vs.add(v);
				}
			}
			//查看关注记录
			List<PayAttention> focusList=userService.findFocusStatus(visitUserId);
			if(focusList!=null&&focusList.size()>0){
				for (PayAttention p : focusList) {
					if(p.getFollowid().equals(existUser.getUserid())){//关注了该用户
						focusStatus="";
						focusStatus="已关注";
					}
				}
			}else{focusStatus="";focusStatus="关注";}
			if(requestType.equals("strategy")){
				//读取攻略信息
				PageBean<UserStrategy> stratiges=strategyService.findUserStrategyByTripUserWithPagination(page,existUser);
				stack.set("stratiges", stratiges);//右侧攻略数据
			}else if(requestType.equals("album")){
				PageBean<UserAlbums> albumList=photoService.findUserAlbumsByTripUserWithPagination(page,existUser);
				List<UserAlbums> as=albumList.getList();
				if(as!=null&&as.size()>0){
					for (UserAlbums u : as) {
						List<UserPhotos> plist=photoService.findUserPhotosByAlbumId(u.getAlbumid());
						if(plist!=null&&plist.size()>0){
							u.setCoverImage(plist.get(0).getPhotourl());
						}
					}
				}
				stack.set("albumList", albumList);//相册数据
			}else if(requestType.equals("skillacademy")){
				PageBean<SkillAcademy> academiesList=academyService.findSkillAcademyByTripUserWithPagination(page,existUser);
				List<SkillAcademy> acaList=academiesList.getList();
				if(acaList!=null&&acaList.size()>0){
					for (SkillAcademy s : acaList) {
						s.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getSkillcontent()));
						if(s.getSkillcontent().length()>500){
							s.setSkillcontent(DomUtil.subStringHTML(s.getSkillcontent(), 500, "…………"));
						}	
					}
				}
				stack.set("academiesList", academiesList);//技法学院数据
			}
			stack.set("existUser", existUser);//用户信息
			stack.set("fansNumber", fansNumber);//粉丝数
			stack.set("showAlbums", showAlbums);//左侧相册数据
			stack.set("visitors", vs);//最近访问
			stack.set("currentIdentity", currentIdentity);//当前访问用户身份
			stack.set("focusStatus", focusStatus);//关注状态
			stack.set("requestType", requestType);//请求类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goToUserSpace";
	}
	
	/**
	 * 跳转到用户资料界面
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String goToPersonalDoc(){
		try {
			this.encodingReqAndRes();
			String userid=request.getParameter("userid");
			String currentUserId=CookieUtil.readCookieReturnId(request);
			if(null==currentUserId||("").equals(currentUserId)){
				return "loginPage";
			}
			TripUser currentUser=userService.findUserByUserId(currentUserId);
			TripUser visitUser=userService.findUserByUserId(userid);
			//路上风景数据
			List<UserStrategy> strategies=strategyService.findUserStrategyByTripUser(visitUser);//该用户的所有攻略
			List<UserAlbums> albums=photoService.findUserAlbums(visitUser);//该用户所有的相册
			List<ViewOnRoadBean> viewRBean=new LinkedList<>();
			ViewOnRoadBean bean=null;
			int flag1=1;//0 有重复 1有重复
			int flag2=1;
			if(strategies!=null&&strategies.size()>0){
			for(UserStrategy s:strategies){
				String cityName=s.getCity().getCityname();
				if(viewRBean.size()>0){
					flag1=1;
					for(int i=0;i<viewRBean.size();i++){
						String cName=viewRBean.get(i).getCityName();
						if(cName.equals(cityName)){
							List<SingleViewOnRoad> so=viewRBean.get(i).getList();
							SingleViewOnRoad or=new SingleViewOnRoad();
							or.setContent(s.getUstrategycontent());
							or.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getUstrategycontent()));
							or.setDescription(s.getUstrategyplaintext().substring(0, 10));
							or.setPlainText(s.getUstrategyplaintext());
							or.setSid(s.getUstrategyid());
							or.setSname(s.getUstrategyname());
							or.setType("strategy");
							Calendar c=Calendar.getInstance();
							c.setTime(s.getUstrategydate());
							DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							or.setDate(format.parse(format.format(c.getTime())));
							so.add(or);
							flag1=0;
							//viewRBean.get(i).setList(so);
						}
					}
					if(flag1==1){
						bean=new ViewOnRoadBean();//创建单个城市的对象，其中包含城市名与攻略相册数组
						bean.setCityName(cityName);
						List<SingleViewOnRoad> sr=new ArrayList<>();
						SingleViewOnRoad or=new SingleViewOnRoad();
						or.setContent(s.getUstrategycontent());
						or.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getUstrategycontent()));
						or.setDescription(s.getUstrategyplaintext().substring(0, 10));
						or.setPlainText(s.getUstrategyplaintext());
						or.setSid(s.getUstrategyid());
						or.setSname(s.getUstrategyname());
						or.setType("strategy");
						Calendar c=Calendar.getInstance();
						c.setTime(s.getUstrategydate());
						DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						or.setDate(format.parse(format.format(c.getTime())));
						sr.add(or);
						bean.setList(sr);
						viewRBean.add(bean);
					}
				}else{
					bean=new ViewOnRoadBean();//创建单个城市的对象，其中包含城市名与攻略相册数组
					bean.setCityName(cityName);
					List<SingleViewOnRoad> sr=new ArrayList<>();
					SingleViewOnRoad or=new SingleViewOnRoad();
					or.setContent(s.getUstrategycontent());
					or.setCoverImage(DomUtil.getSingleImageFromHtmlDocument(s.getUstrategycontent()));
					or.setDescription(s.getUstrategyplaintext().substring(0, 10));
					or.setPlainText(s.getUstrategyplaintext());
					or.setSid(s.getUstrategyid());
					or.setSname(s.getUstrategyname());
					or.setType("strategy");
					sr.add(or);
					bean.setList(sr);
					viewRBean.add(bean);
				}
			}
			}
			if(albums!=null&&albums.size()>0){
			for(UserAlbums album:albums){
				String cityName=album.getCity().getCityname();
				if(viewRBean.size()>0){
					flag2=1;
					for(int i=0;i<viewRBean.size();i++){
						List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
						if(null!=photos&&photos.size()>0){
							String cName=viewRBean.get(i).getCityName();
							if(cName.equals(cityName)){
								List<SingleViewOnRoad> so=viewRBean.get(i).getList();
								SingleViewOnRoad or=new SingleViewOnRoad();
								or.setDescription(album.getAlbumdescription());
								or.setSid(album.getAlbumid());
								or.setSname(album.getAlbumname());
								or.setCoverImage(photos.get(0).getPhotourl());
								or.setType("album");
								Calendar c=Calendar.getInstance();
								c.setTime(album.getUploadtime());
								DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								or.setDate(format.parse(format.format(c.getTime())));
								so.add(or);
								flag2=0;
								//viewRBean.get(i).setList(so);
							}
						}
					}
					if(flag2==1){
						List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
						if(null!=photos&&photos.size()>0){
							bean=new ViewOnRoadBean();
							bean.setCityName(cityName);
							List<SingleViewOnRoad> sr=new ArrayList<>();
							SingleViewOnRoad or=new SingleViewOnRoad();
							Calendar c=Calendar.getInstance();
							c.setTime(album.getUploadtime());
							or.setCoverImage(photos.get(0).getPhotourl());
							DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							or.setDate(format.parse(format.format(c.getTime())));
							or.setDescription(album.getAlbumdescription());
							or.setSid(album.getAlbumid());
							or.setSname(album.getAlbumname());
							or.setType("album");
							sr.add(or);
							bean.setList(sr);
							viewRBean.add(bean);
						}
					}
				}else{
					List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
					if(null!=photos&&photos.size()>0){
						bean=new ViewOnRoadBean();
						bean.setCityName(cityName);
						List<SingleViewOnRoad> sr=new ArrayList<>();
						SingleViewOnRoad or=new SingleViewOnRoad();
						Calendar c=Calendar.getInstance();
						c.setTime(album.getUploadtime());
						or.setCoverImage(photos.get(0).getPhotourl());
						DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						or.setDate(format.parse(format.format(c.getTime())));
						or.setDescription(album.getAlbumdescription());
						or.setSid(album.getAlbumid());
						or.setSname(album.getAlbumname());
						or.setType("album");
						sr.add(or);
						bean.setList(sr);
						viewRBean.add(bean);
					}
				}
			}
			}
			//个人足迹数据
			List<SkillAcademy> academies=academyService.findSkillAcademyByTripUser(visitUser);//技法学院数据
			List<PersonalFoot> footList=new ArrayList<>();//最终数据
			PersonalFoot foot=null;
			DateFormat format1=new SimpleDateFormat("yyyy");
			DateFormat format2=new SimpleDateFormat("MM-dd");
			int flagstrategy=1;
			//用户攻略处理
			if(strategies!=null&&strategies.size()>0){
			for(UserStrategy s:strategies){
				String year=format1.format(s.getUstrategydate());
				if(footList.size()>0){
					flagstrategy=1;
					for(int i=0;i<footList.size();i++){
						String existYeay=footList.get(i).getYear();
						if(existYeay.equals(year)){
							List<PersonalFootDetail> existList=footList.get(i).getList();
							PersonalFootDetail d=new PersonalFootDetail();
							d.setDescription(s.getUstrategyplaintext().substring(0, 50));
							d.setMonthAndDay(format2.format(s.getUstrategydate()));
							d.setName(s.getUstrategyname());
							d.setPid(s.getUstrategyid());
							d.setType("strategy");
							existList.add(d);
							flagstrategy=0;
						}
					}
					if(flagstrategy==1){
						foot=new PersonalFoot();
						foot.setYear(year);
						List<PersonalFootDetail> details=new ArrayList<>();
						PersonalFootDetail d=new PersonalFootDetail();
						d.setDescription(s.getUstrategyplaintext().substring(0, 50)+"……");
						d.setMonthAndDay(format2.format(s.getUstrategydate()));
						d.setName(s.getUstrategyname());
						d.setPid(s.getUstrategyid());
						d.setType("strategy");
						details.add(d);
						foot.setList(details);
						footList.add(foot);
					}
				}else{
					foot=new PersonalFoot();
					foot.setYear(year);
					List<PersonalFootDetail> details=new ArrayList<>();
					PersonalFootDetail d=new PersonalFootDetail();
					d.setDescription(s.getUstrategyplaintext().substring(0, 50)+"……");
					d.setMonthAndDay(format2.format(s.getUstrategydate()));
					d.setName(s.getUstrategyname());
					d.setPid(s.getUstrategyid());
					d.setType("strategy");
					details.add(d);
					foot.setList(details);
					footList.add(foot);
				}
			}
			}
			//用户相册处理
			int flagPhoto=1;
			if(albums!=null&&albums.size()>0){
			for(UserAlbums album:albums){
				String year=format1.format(album.getUploadtime());
				if(footList.size()>0){
					flagPhoto=1;
					for(int i=0;i<footList.size();i++){
						String existYear=footList.get(i).getYear();
						if(existYear.equals(year)){
							List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
							if(null!=photos&&photos.size()>0){
								List<PersonalFootDetail> existList=footList.get(i).getList();
								PersonalFootDetail d=new PersonalFootDetail();
								d.setDescription(album.getAlbumdescription());
								d.setMonthAndDay(format2.format(album.getUploadtime()));
								d.setName(album.getAlbumname());
								d.setPid(album.getAlbumid());
								d.setType("album");
								existList.add(d);
								flagPhoto=0;
							}
						}
					}
					if(flagPhoto==1){
						List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
						if(null!=photos&&photos.size()>0){
							foot=new PersonalFoot();
							foot.setYear(year);
							List<PersonalFootDetail> details=new ArrayList<>();
							PersonalFootDetail d=new PersonalFootDetail();
							d.setDescription(album.getAlbumdescription());
							d.setMonthAndDay(format2.format(album.getUploadtime()));
							d.setName(album.getAlbumname());
							d.setPid(album.getAlbumid());
							d.setType("album");
							details.add(d);
							foot.setList(details);
							footList.add(foot);
						}
					}
				}else{
					List<UserPhotos> photos=photoService.findUserPhotosByAlbumId(album.getAlbumid());
					if(null!=photos&&photos.size()>0){
						foot=new PersonalFoot();
						foot.setYear(year);
						List<PersonalFootDetail> details=new ArrayList<>();
						PersonalFootDetail d=new PersonalFootDetail();
						d.setDescription(album.getAlbumdescription());
						d.setMonthAndDay(format2.format(album.getUploadtime()));
						d.setName(album.getAlbumname());
						d.setPid(album.getAlbumid());
						d.setType("album");
						details.add(d);
						foot.setList(details);
						footList.add(foot);
					}
				}
			}
			}
			//用户技法文章处理
			int flagSkillAcademy=1;
			if(academies!=null&&academies.size()>0){
			for(SkillAcademy academy:academies){
				String year=format1.format(academy.getSkilldate());
				if(footList.size()>0){
					flagSkillAcademy=1;
					for(int i=0;i<footList.size();i++){
						String existYear=footList.get(i).getYear();
						if(existYear.equals(year)){
							List<PersonalFootDetail> existList=footList.get(i).getList();
							PersonalFootDetail d=new PersonalFootDetail();
							d.setDescription(academy.getSkillplaintext().substring(0, 50)+"……");
							d.setMonthAndDay(format2.format(academy.getSkilldate()));
							d.setName(academy.getSkilltitle());
							d.setPid(academy.getSkilid());
							d.setType("skillacademy");
							existList.add(d);
							flagstrategy=0;
						}
					}
					if(flagSkillAcademy==1){
						foot=new PersonalFoot();
						foot.setYear(year);
						List<PersonalFootDetail> details=new ArrayList<>();
						PersonalFootDetail d=new PersonalFootDetail();
						d.setDescription(academy.getSkillplaintext().substring(0, 50)+"……");
						d.setMonthAndDay(format2.format(academy.getSkilldate()));
						d.setName(academy.getSkilltitle());
						d.setPid(academy.getSkilid());
						d.setType("skillacademy");
						details.add(d);
						foot.setList(details);
						footList.add(foot);
					}
				}else{
					foot=new PersonalFoot();
					foot.setYear(year);
					List<PersonalFootDetail> details=new ArrayList<>();
					PersonalFootDetail d=new PersonalFootDetail();
					d.setDescription(academy.getSkillplaintext().substring(0, 50)+"……");
					d.setMonthAndDay(format2.format(academy.getSkilldate()));
					d.setName(academy.getSkilltitle());
					d.setPid(academy.getSkilid());
					d.setType("skillacademy");
					details.add(d);
					foot.setList(details);
					footList.add(foot);
				}
			}
			}
			//排序数据
			//首先排序内部文章序列
			for(int i=0;i<footList.size();i++){
				List<PersonalFootDetail> list=footList.get(i).getList();
				SortPersonalFootDetailList sort=new SortPersonalFootDetailList();
				Collections.sort(list, sort);
				Collections.reverse(list);
			}
			//接着排序总的年份
			SortPersonalFoot sortFoot=new SortPersonalFoot();
			Collections.sort(footList, sortFoot);
			Collections.reverse(footList);
			//当前用户关注的人
			//读取关注数
			List<PayAttention> focusList=userService.findFocusStatus(visitUser.getUserid());
			List<TripUser> focusUsers=new ArrayList<>();
			int focusCount=0;
			if(focusList!=null){
		      focusCount=focusList.size();
		      for(int i=0;i<focusList.size();i++){
		    	  String focusId=focusList.get(i).getFollowid();
		    	  TripUser focusUser=userService.findUserByUserId(focusId);
		    	  focusUsers.add(focusUser);
		      }
			}
			//读取所有留言
			List<SecrectMessage> messages=userService.findMessageByReceiver(visitUser);
			ValueStack stack=ActionContext.getContext().getValueStack();
			if(currentUser.getUserid().equals(visitUser.getUserid())){
				List<TripUser> users=userService.findUsersByCity(currentUser.getCity());
				for(int i=users.size()-1;i>=0;i--){
					if(users.get(i).getUserid().equals(currentUser.getUserid())){
						users.remove(users.get(i));
					}
				}
				users.subList(0, 4);
				stack.set("recommandUsers", users);//推荐用户
			}
			stack.set("currentUser", currentUser);
			stack.set("visitUser", visitUser);
			stack.set("viewRBean", viewRBean);
			stack.set("footList", footList);//年份轴数据
			stack.set("focusCount", focusCount);//关注人个数
			stack.set("focusUsers", focusUsers);//关注列表
			stack.set("messages", messages);//所有留言
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goToPersonalDoc";
	}
	
	/**
	 * 阅读悄悄话根据id将本条消息状态置为已读
	 * @return
	 */
	public String readMessage(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String messageid=request.getParameter("messageid");
			if(null!=messageid&&!("").equals(messageid)){
				SecrectMessage existMessage=userService.findSecretMessageByMid(messageid);
				existMessage.setIsread(0);
				Boolean result=userService.updateSecretMessage(existMessage);
				if(result==true){
					out.print("success");
				}else{
					out.print("error");
				}
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 批量删除悄悄话消息
	 * @return
	 */
	public String removeMessage(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String json=request.getParameter("jsonArray");//[{"id":"50365fa8959f404682389a6ea00199ca"},{"id":"ea4d6d383cb544f796e280d037a15840"},{"id":"eb182b757df44ee5906716f5d2065e75"}]
			System.out.println(json);
			JSONArray jsonArray=JSONArray.fromString(json);
			ArrayList<String> messageIds=new ArrayList<>();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject object=jsonArray.getJSONObject(i);
				String messageid=object.optString("id");
				messageIds.add(messageid);
				System.out.println(messageid);
			}
			Boolean result=userService.removeMessages(messageIds);
			if(result==true){
				out.print("success");
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 将消息全部标记为已读
	 * @return
	 */
	public String markMessageList(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String json=request.getParameter("jsonArray");
			System.out.println(json.toString());
			JSONArray jsonArray=JSONArray.fromString(json);
			Boolean result=false;
			for(int i=0;i<jsonArray.length();i++){
				JSONObject object=jsonArray.getJSONObject(i);
				String messageid=object.optString("id");
				SecrectMessage existMessage=userService.findSecretMessageByMid(messageid);
				existMessage.setIsread(0);
				result=userService.updateSecretMessage(existMessage);
			}
			if(result==true){
				out.print("success");
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("error");
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	/**
	 * 修改用户信息
	 * @return
	 */
	public String updateUserDocument(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String nickname=request.getParameter("nickname");
			String usignature=request.getParameter("usignature");
			String sex=request.getParameter("sex");
			String phone=request.getParameter("phone");
			String cityName=request.getParameter("cityName").trim();
			String userid=request.getParameter("userid");
			TripUser existUser=userService.findUserByUserId(userid);
			existUser.setUsername(nickname);
			existUser.setSex(sex);
			existUser.setPhone(phone);
			City c=osStrategyService.getCity(cityName);
			existUser.setCity(c);
			existUser.setUsignature(usignature);
			userService.update(existUser);
			JSONArray jArray=userService.enstoreTripUserToJSONArray(existUser);
			System.out.println(jArray.toString());
			out.print(jArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 发送悄悄话
	 * @return
	 */
	public String sendSecrectWord(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String senderName=URLDecoder.decode(request.getParameter("senderName"),"utf-8");//发送者昵称
			String receiverId=request.getParameter("receiverId");//接收者id
			String secretMessage=URLDecoder.decode(request.getParameter("message"), "utf-8");//悄悄话
			if(null!=senderName&&!("").equals(senderName)&&null!=receiverId&&!("").equals(receiverId)){
				TripUser sender=userService.findByUserName(senderName);//发送者
				TripUser receiver=userService.findUserByUserId(receiverId);//接收者
				if(null!=sender&&null!=receiver){
					Boolean result=userService.saveUserSecretWord(sender,receiver,secretMessage);
					if(result==true){
						out.print("success");
					}else{
						out.print("error");
					}
				}
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取未读消息数
	 * @return
	 */
	public String getUnreadMessageSize(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String username=URLDecoder.decode(request.getParameter("username"), "utf-8");
			TripUser currentUser=userService.findByUserName(username);
			int number=userService.findCountUnReadMessageSize(currentUser);
			out.print(number);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	/**
	 * 关注或者取消关注某人
	 * @return
	 */
	public String focusOrCancelOnSomeOne(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String result="";
			String clickStatus=request.getParameter("clickStatus");
			String userid=request.getParameter("userid");//被关注的用户id
			String currentUserId=CookieUtil.readCookieReturnId(request);//当前操作用户
			if(clickStatus.equals("focus")){
				result=userService.saveUserFocusOrCancelStatus(currentUserId,userid,"focus");
			}else if(clickStatus.equals("cancel")){
				result=userService.saveUserFocusOrCancelStatus(currentUserId,userid,"cancel");
			}
			if(result!=null&&!result.equals("")){
				if(result.equals("focusSuccess")){
					out.print("focusSuccess");
				}else if(result.equals("cancelSuccess")){
					out.print("cancelSuccess");
				}
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取回复消息
	 * @return
	 */
	public String acquireReplyData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			TripUser fromUser=userService.findUserByUserId(userid);
			List<Comments> commentList=indexService.findCommentsListByFromUser(fromUser);//别人给我的回复
			if(null!=commentList&&!commentList.isEmpty()){
				for(Comments c:commentList){
					List<Reply> replies=indexService.findReplyListByComment(c);
					if(null!=replies&&!replies.isEmpty()){
						c.setReplies(replies);
					}
				}
			}
			JSONArray myReplyArray=indexService.enstoreCommentListToJSONArray(commentList);//别人给我的回复array
			List<Comments> realList=indexService.addRelatedComments(userid);//别人对我的评论
			JSONArray commentArray=new JSONArray();//别人对我的评价array
			if(null!=realList&&!realList.isEmpty()){
				JSONObject o=null;
				SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH.mm.ss");
				for(Comments c:realList){
					o=new JSONObject();
					o.put("commentid", c.getCommentid());
					o.put("topicid", c.getTopicid());
					o.put("topictype", c.getTopictype());
					o.put("commentcontent", c.getCommentcontent());
					o.put("commentName", c.getFromuser().getUsername());
					o.put("headerImage", c.getFromuser().getHeaderimage());
					o.put("commentdate", format.format(c.getCommentdate()));
					commentArray.put(o);
				}
			}
			JSONObject results=new JSONObject();
			results.put("myCommentsList", realList);
			results.put("myReplyList", myReplyArray);
			out.print(results.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取收藏记录
	 * @return
	 */
	public String acquireCollectionData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			TripUser existUser=userService.findUserByUserId(userid);
			List<UserCollections> collections=userService.findUserCollectionsByTripUser(existUser);
			JSONArray array=userService.enstoreUserCollectionsToJSONArray(collections);
			out.print(array.toString());
			System.out.println(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取订单信息
	 * @return
	 */
	public String acquireMyOrderData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			List<OrderItem> items=productService.findOrdersByUserId(userid);
			JSONArray array=productService.enstoreOrderItemListToJSONArray(items);
			out.print(array.toString());
			System.out.println(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	/**
	 * 获取用户的已付款订单
	 * 手机端API 
	 * @param userid 
	 * @return gsonString
	 */
	public String getUserPayedOrderList(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			List<OrderItem> orderItems=productService.findUserPayedOrdersByUserid(userid);
			if(!orderItems.isEmpty()){
				JSONArray array=new JSONArray();
				JSONObject object=null;
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(OrderItem i:orderItems){
					object=new JSONObject();
					object.put("oid",i.getOid());
					object.put("oprice", i.getOprice());
					object.put("otime", format.format(i.getOtime()));
					object.put("ostate", i.getOstate());
					object.put("paddress", i.getPaddress());
					JSONObject p=new JSONObject();
					Product product=i.getProduct();
					p.put("pid", product.getPid());
					p.put("pname", product.getPname());
					p.put("marketprice", product.getMarketprice());
					p.put("shopprice", product.getShopprice());
					object.put("coverImage", DomUtil.getSingleImageFromHtmlDocument(product.getPcontent()));
					object.put("product", p);
					array.put(object);
				}
				out.print(array.toString());
			}else{
				out.print("null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	/**
	 * 获取用户的未付款订单
	 * 手机端API 
	 * @param userid 
	 * @return gsonString
	 * @return
	 */
	public String getUserUnPayedOrderList(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			List<OrderItem> orderItems=productService.findUserUnPayedOrdersByUserid(userid);
			if(!orderItems.isEmpty()){
				JSONArray array=new JSONArray();
				JSONObject object=null;
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(OrderItem i:orderItems){
					object=new JSONObject();
					object.put("oid",i.getOid());
					object.put("oprice", i.getOprice());
					object.put("otime", format.format(i.getOtime()));
					object.put("ostate", i.getOstate());
					object.put("paddress", i.getPaddress());
					JSONObject p=new JSONObject();
					Product product=i.getProduct();
					p.put("pid", product.getPid());
					p.put("pname", product.getPname());
					p.put("marketprice", product.getMarketprice());
					p.put("shopprice", product.getShopprice());
					object.put("coverImage", DomUtil.getSingleImageFromHtmlDocument(product.getPcontent()));
					object.put("product", p);
					array.put(object);
				}
				out.print(array.toString());
			}else{
				System.out.println("没有未支付订单");
				out.print("null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取未读消息
	 * @return
	 */
	public String acquireUnReadMessage(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userid=request.getParameter("userid");
			List<SecrectMessage> messages=userService.findUnReadMessageByUserId(userid);
			JSONArray array=userService.enstoreSecrectMessageToJSONArray(messages);
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	
	/**
	 * 检查登录状态
	 * @return
	 */
	public String checkLoginStatus(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String userCookieValue=request.getParameter("userCookieValue");
			String loginStatus=request.getParameter("loginStatus");
			TripUser existUser=userService.findByUserCookieValue(userCookieValue);
			if(existUser!=null){
			if(loginStatus!=null){
				request.getSession().setAttribute("sessionUser", existUser);
				return NONE;
			}
			request.getSession().setAttribute("sessionUser", existUser);
				out.print("{'message':'SUCCESS','username':'"+existUser.getUsername()+"','headerImage':'"+existUser.getHeaderimage()+"','userid':'"+existUser.getUserid()+"'}");
			}else{
				out.print("{'message':'ERROR'}");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return NONE;
	}
	
	/**
	 * 提交用户反馈信息
	 * 手机端API，接受反馈信息与反馈通信地址，返回保存状态
	 * @feedbackContent(反馈内容)  @feedbackContact(反馈通信地址)
	 * @return    "success"(成功) "error"(失败)
	 */
	public String submitUserFeedBack(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String feedbackContent=request.getParameter("feedbackContent");
			String feedbackContact=request.getParameter("feedbackContact");
			System.out.println("反馈内容:"+feedbackContent+"反馈通信地址:"+feedbackContact);
			Boolean result=userService.saveUserFeedBack(feedbackContent,feedbackContact);
			if(result==true){
				out.print("success");
			}else{
				out.print("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取所有精选数据
	 * 手机端API
	 * @param requestType bestChoose||bestStrategy
	 * @return gsonString
	 */
	public String getAllBestChoose(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String requestType=request.getParameter("requestType");
			Gson gson=new Gson();
			if(requestType.equals("bestChoose")){
				List<BestChoose> bestChooses=userService.findAllBestChoose();
				out.print(gson.toJson(bestChooses));
			}else if(requestType.equals("bestStrategy")){
				List<UserStrategy> strategies=strategyService.findBestStrategy();
				out.print(gson.toJson(strategies));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据定位获取当前所在城市信息
	 * 手机端API
	 * @param cityid
	 * @return
	 */
	public String locateCurrentPosition(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int cityid=Integer.parseInt(request.getParameter("cityid"));
			LocalInfomation infomation=osStrategyService.findLocalInformationByCityId(cityid);
			Gson gson=new Gson();
			out.print(gson.toJson(infomation));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据大洲名获取地区集合
	 * 手机端API
	 * @param destinationName
	 * @return gsonString
	 */
	public String getProvinces(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String destionName=request.getParameter("destinationName");
			List<Provinces> provinces=userService.findProvincesByDestinationName(destionName);
			Gson gson=new Gson();
			if(provinces.size()>0){
				out.print(gson.toJson(provinces));
			}else{
				out.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	/*-------------------------------------------管理员业务逻辑区---------------------------------------------------------*/
	/**
	 * 管理员登录
	 * 后台API
	 * @return
	 */
	public String adminLogin(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String username=URLDecoder.decode(request.getParameter("username"), "utf-8");
			String password=URLDecoder.decode(request.getParameter("password"), "utf-8");
			TripUser user=userService.findByUserNameAndPassword(username, password);
			if(null==user){
				out.print("notfound");
			}else{
				//存入cookie
//				CookieUtil.saveCookie(user, response);
//				String userCookie=CookieUtil.userCookie;
//				user.setUsercookievalue(userCookie);
//				userService.update(user);
				Gson gson=new Gson();
				out.print(gson.toJson(user));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 发布精选数据
	 * 后台API
	 * @return
	 */
	public String uploadBestChoose(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String title=request.getParameter("title");
			String ishot=request.getParameter("ishot");
			String htmlContent=request.getParameter("bestContent");
			String plainText=request.getParameter("bestPlainText");
			File imgFile=best_cover;
			Boolean result=userService.saveBestChooseContent(title,ishot,htmlContent,plainText,imgFile,best_coverFileName);
			if(result){
				RequestDispatcher rd;
			    rd = ServletActionContext.getServletContext().getRequestDispatcher("/list.html");
			    rd.forward(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取大洲数据
	 * 后台API
	 * @return
	 */
	public String getIsland(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			List<Destination> results=osStrategyService.findAllDestinations();
			Gson gson=new Gson();
			out.print(gson.toJson(results));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据大洲id获取省份或国家信息
	 * 后台API
	 * @return
	 */
	public String getProvinceByDesId(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int desid=Integer.parseInt(request.getParameter("desid"));
			List<Provinces> results=userService.findAllProvince(desid);
			Gson gson=new Gson();
			out.print(gson.toJson(results));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据省份id获取城市集合
	 * 后台API
	 * @return
	 */
	public String getCityByPid(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int provinceid=Integer.parseInt(request.getParameter("provinceid"));
			List<City> cities=userService.findCityByPid(provinceid);
			Gson gson=new Gson();
			if(cities.size()>0){
				out.print(gson.toJson(cities));
			}else{
				out.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 上传并修改城市的主页信息
	 * 后台API
	 * @param selected_cityid 城市id
	 * @param local_cover 封面图片
	 * @param s_local_bref 目的地概况
	 * @param localContent 不可错过html
	 * @param localPlainText 不可错过plaintext
	 * @param s_howto_arrive 如何到达
	 * @param s_traffic 交通攻略
	 * @param s_guide 活动指南
	 * @param s_tips 小贴士
	 * @return gsonString 
	 */
	public String editLocalInfo(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			File localCover=local_cover;
			int cityId=Integer.parseInt(request.getParameter("selected_cityid"));
			String localBref=request.getParameter("s_local_bref");
			String notmissContent=request.getParameter("localContent");
			String notmissText=request.getParameter("localPlainText");
			String howToArrive=request.getParameter("s_howto_arrive");
			String traffic=request.getParameter("s_traffic");
			String guide=request.getParameter("s_guide");
			String tips=request.getParameter("s_tips");
			Boolean result=osStrategyService.saveLocalInformation(localCover,local_coverFileName,cityId,localBref,
					notmissContent,notmissText,howToArrive,traffic,guide,tips);
			if(result){
				out.print("保存成功");
			}else{
				out.print("保存失败，请稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	
	/**
	 * 初始化后台框架内容
	 * 后台API
	 * @param current_cityid
	 * @return gson
	 */
	public String initEditContentData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int cityid=Integer.parseInt(request.getParameter("current_cityid"));
			LocalInfomation infomation=osStrategyService.findLocalInformationByCityId(cityid);
			String result="";
			String data="";
			Gson gson=new Gson();
			if(null!=infomation){
				data=gson.toJson(infomation);
				result="exist";
			}else{
				result="empty";
			}
			JSONObject object=new JSONObject();
			object.put("result", result);
			JSONObject dataObj=JSONObject.fromString(data);
			object.put("data", dataObj);
			out.print(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据类型获取地点周边数据
	 * 后台API
	 * @param type food hotel view shop play live 类型
	 * @param selected_cityid 选中城市
	 * @return gson
	 */
	public String getAllTourismData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String type=request.getParameter("type");
			int cityid=Integer.parseInt(request.getParameter("selected_cityid"));
			List<TourismAttraction> attractions=new ArrayList<>();
			Gson gson=new Gson();
			JSONObject object=new JSONObject();
			String gsonStr=new String();
			String result="";
			switch (type) {
			case FOOD:{//美食
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(FOOD,cityid);
				break;
			}
			case HOTEL:{//酒店
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(HOTEL,cityid);
				break;
			}
			case VIEW:{//景点
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(VIEW,cityid);
				break;
			}
			case SHOP:{//购物
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(SHOP,cityid);
				break;
			}
			case PLAY:{//娱乐
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(PLAY,cityid);
				break;
			}
			case LIVE:{//生活
				attractions=osStrategyService.findTourismAttractionListByCityIdAndType(LIVE,cityid);
				break;
			}
			}
			String attractionGson=gson.toJson(attractions);
			object.put("datasize", attractions.size());
			object.put("result", attractionGson);
			out.print(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 添加新地点信息
	 * 后台API
	 * @param selected_cityid
	 * @param type food hotel view shop play live 类型
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
	public String addTourismContent(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			Gson gson=new Gson();
			TourismAttraction attraction=new TourismAttraction();
			int cityid=Integer.parseInt(request.getParameter("selected_cityid"));
			String type=request.getParameter("type");
			String tourismname=request.getParameter("tourismname");
			String tourismforeignname=request.getParameter("tourismforeignname");
			Float tourismprice=Float.parseFloat(request.getParameter("tourismprice"));
			String currencytype=request.getParameter("currencytype");
			String tourismphone=request.getParameter("tourismphone");
			String tourismdescription=request.getParameter("tourismdescription");
			String tourismopendesc=request.getParameter("tourismopendesc");
			String tourismaddress=request.getParameter("tourismaddress");
			String tourismguide=request.getParameter("tourismguide");
			String tourismurl=request.getParameter("tourismurl");
			String tourismactivity=request.getParameter("tourismactivity");
			BigDecimal longitude=new BigDecimal(request.getParameter("longitude"));
			BigDecimal latitude=new BigDecimal(request.getParameter("latitude"));
			int categoryid=Integer.parseInt(request.getParameter("categoryid"));
			switch(type){
			case FOOD:{//美食
				attraction=osStrategyService.saveTourismAttractions(FOOD,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			case HOTEL:{//酒店
				attraction=osStrategyService.saveTourismAttractions(HOTEL,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			case VIEW:{//景点
				attraction=osStrategyService.saveTourismAttractions(VIEW,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			case SHOP:{//购物
				attraction=osStrategyService.saveTourismAttractions(SHOP,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			case PLAY:{//娱乐
				attraction=osStrategyService.saveTourismAttractions(PLAY,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			case LIVE:{//生活
				attraction=osStrategyService.saveTourismAttractions(LIVE,cityid,tourismname,tourismforeignname,tourismprice,
						currencytype,tourismphone,tourismdescription,tourismopendesc,tourismaddress,tourismguide,
						tourismurl,tourismactivity,longitude,latitude,categoryid);
				break;
			}
			}
			out.print(gson.toJson(attraction));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 根据type获取地点分类数据
	 * 后台API
	 * @param type
	 * @return
	 */
	public String getTourismCategory(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String type=request.getParameter("type");
			System.out.println(type);
			List<TourismCategory> categories=osStrategyService.findTourismCategoryByType(type);
			Gson gson=new Gson();
			out.print(gson.toJson(categories));
			System.out.println(gson.toJson(categories));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 接受上传的景点图片
	 * @return
	 */
	public String uploadAttractionCoverImage(){
		PrintWriter out=null;
		try {
		    this.encodingReqAndRes();
		    out=response.getWriter();
		    String attractionid=request.getParameter("attractionid");//地点id
            String tempPath = request.getServletContext().getRealPath("/") + dirTemp;// 临时文件目录 
			DiskFileItemFactory factory=new DiskFileItemFactory();
			 factory.setSizeThreshold(20 * 1024 * 1024); //设定使用内存超过5M时，将产生临时文件并存储于临时目录中。     
		     factory.setRepository(new File(tempPath)); //设定存储临时文件的目录。    
			FileUpload upload=new FileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
			String filenameFormat=wrapper.getFileNames("files[]")[0];
			File uploadFile=wrapper.getFiles("files[]")[0];//上传的文件
			String fileExt = filenameFormat.substring(filenameFormat.lastIndexOf(".") + 1).toLowerCase();  
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;//新文件名
			String savePath = request.getServletContext().getRealPath("/") +"uploadAttractionFile";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
	        String ymd = sdf.format(new Date());  
	        savePath += "/" + ymd + "/"; 
	        File dirFile = new File(savePath);  
	        if (!dirFile.exists()) {  //创建文件夹
	            dirFile.mkdirs();  
	        }
	      //创建临时文件夹  
	        File dirTempFile = new File(tempPath);  
	        if (!dirTempFile.exists()) {  
	            dirTempFile.mkdirs();  
	        }  
	        File uploadedFile = new File(savePath, newFileName);//创建文件
	        OutputStream os = new FileOutputStream(uploadedFile);
	        InputStream is = new FileInputStream(uploadFile);//上传的文件输入流  
            byte buf[] = new byte[1024];//可以修改 1024 以提高读取速度  
            int length = 0;    
            while( (length = is.read(buf)) > 0 ){    
                os.write(buf, 0, length);    
            }
            TourismAttraction attraction=osStrategyService.findTourismAttractionById(attractionid);
            osStrategyService.saveTourismCovers("uploadAttractionFile/"+ymd+"/"+newFileName,attraction);
            //关闭流    
            os.flush();  
            os.close();    
            is.close();
            System.out.println("上传成功！路径："+savePath+"/"+newFileName);  
            out.print(savePath+"/"+newFileName); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 根据美食id查询相关的菜品
	 * 后台API
	 * @param attractionid
	 * @return gsonString
	 */
	public String getAllCuisionData(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			String attractionid=request.getParameter("attractionid");
			List<Cuision> cuisions=osStrategyService.findAllCuisionData(attractionid);
			Gson gson=new Gson();
			out.print(gson.toJson(cuisions));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 保存美食的菜品
	 * @param attractionid
	 * @param cuisionname 菜品名称
	 * @param cuisionprice 当地价格
	 * @param cuisionpricecurrency 当地货币类型
	 * @param cuisionforeignprice 中国价格
	 * @param cuisionforeigncurrency 人民币类型
	 * @param cuision_cover 菜品图片
	 * @param cuisiondescription 菜品介绍  
	 * @return gsonString
	 */
	public String saveCuision(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			File cuisionCover=cuision_cover;
			String cuisionCoverFileName=cuision_coverFileName;
			String attractionid=request.getParameter("attractionid");
			String cuisionname=request.getParameter("cuisionname");
			Float cuisionprice=Float.valueOf(request.getParameter("cuisionprice"));
			String cuisionpricecurrency=request.getParameter("cuisionpricecurrency");
			Float cuisionforeignprice=Float.valueOf(request.getParameter("cuisionforeignprice"));
			String cuisionforeigncurrency=request.getParameter("cuisionforeigncurrency");
			String cuisiondescription=request.getParameter("cuisiondescription");
			Cuision cuision=osStrategyService.saveCuision(cuisionCover,cuisionCoverFileName,attractionid,cuisionname,
					cuisionprice,cuisionpricecurrency,cuisionforeignprice,cuisionforeigncurrency,cuisiondescription);
			JSONObject object=new JSONObject();
			Gson gson=new Gson();
			if(null!=cuision){
				object.put("result", "success");
				object.put("data", gson.toJson(cuision));
			}else{
				object.put("result", "error");
			}
			out.print(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 获取相关旅游路线
	 * 后台API && 手机端API
	 * @param cityid
	 * @return gsonString
	 */
	public String getAllRoutes(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int cityid=Integer.parseInt(request.getParameter("selected_cityid"));
			List<Routes> routes=osStrategyService.findRoutesByCityId(cityid);
			Gson gson=new Gson();
			if(routes.size()>0){
				out.print(gson.toJson(routes));
			}else{
				out.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	/**
	 * 保存路线
	 * 后台API
	 * @param selected_cityid
	 * @param routename
	 * @param route_cover
	 * @param routeContent
	 * @param routePlainText
	 * @return
	 */
	public String saveRoutes(){
		PrintWriter out=null;
		try {
			this.encodingReqAndRes();
			out=response.getWriter();
			int cityid=Integer.parseInt(request.getParameter("selected_cityid"));
			String routename=request.getParameter("routename");
			File routeCover=route_cover;
			String routeCoverName=route_coverFileName;
			String routeContent=request.getParameter("routeContent");
			String routePlainText=request.getParameter("routePlainText");
			Routes route=osStrategyService.saveRoutes(cityid,routename,routeCover,routeCoverName,routeContent,routePlainText);
			JSONObject jsonObject=new JSONObject();
			Gson gson=new Gson();
			if(null!=route){
				jsonObject.put("result", "success");
				jsonObject.put("data", gson.toJson(route));
			}else{
				jsonObject.put("result", "error");
			}
			out.print(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		return NONE;
	}
	
	

	


}
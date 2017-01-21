package alan.share.user.model;

import java.io.Serializable;
/**
 * 用户反馈pojo映射
 * @author Alan
 *
 */
public class UserFeedBack implements Serializable{
	private String feedbackid;
	private String feedcontent;
	private String feedcontact;
	public String getFeedbackid() {
		return feedbackid;
	}
	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}
	public String getFeedcontent() {
		return feedcontent;
	}
	public void setFeedcontent(String feedcontent) {
		this.feedcontent = feedcontent;
	}
	public String getFeedcontact() {
		return feedcontact;
	}
	public void setFeedcontact(String feedcontact) {
		this.feedcontact = feedcontact;
	}
	public UserFeedBack() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}

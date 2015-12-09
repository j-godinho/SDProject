package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.RewardsBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class RewardsAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private RewardsBean rewardsBean;
	
	

	@Override
	public String execute() {
		
		setRewardsBean(new RewardsBean((String)session.get("username")));
		return SUCCESS;
	}
	
	public RewardsBean getRewardsBean() {
		return rewardsBean;
	}
	public void setRewardsBean(RewardsBean rewardsBean) {
		//this.session.put("availableProjects", availableProjects);
		this.rewardsBean= rewardsBean;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	
}

package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.CreateProjectBean;
import sd.model.RewardsBean;
import sd.model.UpdateRewardsBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class UpdateRewardsAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private UpdateRewardsBean updateRewardsBean;
	

	public String addReward() {
		System.out.println("addRewardAction");
		if(getUpdateRewardsBean().addReward((String)session.get("username")) ==0){
			return SUCCESS;
		}
		else{
			return ERROR;
			
		}
		//setCreateProjectBean(new CreateProjectBean((String)session.get("username")));
		
	}
	
	public String removeReward() {
		System.out.println("removeReward");
		
		if(getUpdateRewardsBean().removeReward((String)session.get("username")) ==0){
			return SUCCESS;
		}
		else{
			return ERROR;
			//System.out.println("Error");
		}
		//setCreateProjectBean(new CreateProjectBean((String)session.get("username")));
		
	}
	
	public UpdateRewardsBean getUpdateRewardsBean() {
		System.out.println("getupdateRewardsBean");
		return updateRewardsBean;
	}
	public void setUpdateRewardsBean(UpdateRewardsBean updateRewardsBean) {
		//this.session.put("availableProjects", availableProjects);
		System.out.println("setupdateRewardsBean");
		this.updateRewardsBean = updateRewardsBean;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	
}

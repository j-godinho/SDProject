package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.CreateProjectBean;
import sd.model.RewardsBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class CreateProjectAction extends ActionSupport implements SessionAware{
	
	
	private static final long serialVersionUID = 8584724140467321111L;
	private Map<String, Object> session;
	private CreateProjectBean createProjectBean;
	
	

	@Override
	public String execute() {
		System.out.println("executeCreateProject");
		
		if(getCreateProjectBean().insertProject((String)session.get("username"))==0){
			return SUCCESS;
		}
		else{
			return ERROR;
			//System.out.println("Error");
		}
		//setCreateProjectBean(new CreateProjectBean((String)session.get("username")));
		
	}
	
	public CreateProjectBean getCreateProjectBean() {
		return createProjectBean;
	}
	public void setCreateProjectBean(CreateProjectBean createProjectBean) {
		//this.session.put("availableProjects", availableProjects);
		this.createProjectBean = createProjectBean;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	
}

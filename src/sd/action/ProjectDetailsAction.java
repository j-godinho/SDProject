package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.ProjectDetailsBean;

import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class ProjectDetailsAction extends ActionSupport implements SessionAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8404085238610630922L;
	private Map<String, Object> session;
	private ProjectDetailsBean projectDetailsBean;

	@Override
	public String execute() throws Exception {
		System.out.println("executeDetails");
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		// setAvailableProjects(new AvailableProjects());
		if(getProjectDetailsBean().getInfo()==1)
		{
			return SUCCESS;
		}
		return SUCCESS;
	}

	public ProjectDetailsBean getProjectDetailsBean() {
		System.out.println(projectDetailsBean);
		return projectDetailsBean;

	}

	public void setProjectDetailsBean(ProjectDetailsBean projectDetailsBean) {
		System.out.println("setProjectDetails");
		// System.out.println(projDetails);
		this.projectDetailsBean = projectDetailsBean;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
		
	}

	public Map<String, Object> getSession() {
		return session;
	}
	


}

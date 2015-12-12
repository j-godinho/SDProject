package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.AvailableProjects;
import sd.model.PrimesBean;
import sd.model.ProjectDetailsBean;

import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class ProjectDetailsAction extends ActionSupport implements SessionAware{
	private Map<String, Object> session;
	private ProjectDetailsBean projectDetailsBean;

	@Override
	public String execute() throws Exception {
		System.out.println("executeDetails");
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		// setAvailableProjects(new AvailableProjects());
		setProjectDetailsBean(new ProjectDetailsBean());
		return SUCCESS;
	}

	public ProjectDetailsBean getProjectDetailsBean() {
		System.out.println(projectDetailsBean);
		return projectDetailsBean;

	}

	public void setProjectDetailsBean(ProjectDetailsBean projDetailsBean) {
		System.out.println("setProjectDetails");
		// System.out.println(projDetails);
		this.projectDetailsBean = projDetailsBean;
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

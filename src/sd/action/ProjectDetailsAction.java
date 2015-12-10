package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.AvailableProjects;
import sd.model.PrimesBean;
import sd.model.ProjectDetailsBean;

import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class ProjectDetailsAction extends ActionSupport {
	private Map<String, Object> session;
	private AvailableProjects availableProjects;
	private ProjectDetailsBean projDetails;

	@Override
	public String execute() throws Exception {
		System.out.println("executeDetails");
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		// setAvailableProjects(new AvailableProjects());
		setProjectDetails(new ProjectDetailsBean());
		return SUCCESS;
	}

	public ProjectDetailsBean getProjectDetails() {
		System.out.println(projDetails);
		return projDetails;

	}

	public void setProjectDetails(ProjectDetailsBean projDetails) {
		System.out.println("setProjectDetails");
		// System.out.println(projDetails);
		this.projDetails = projDetails;
	}

}

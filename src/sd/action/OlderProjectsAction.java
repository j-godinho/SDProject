package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.AvailableProjects;
import sd.model.OlderProjects;


import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class OlderProjectsAction extends ActionSupport {
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private OlderProjects olderProjects;

	@Override
	public String execute() throws Exception {
		System.out.println("execute");
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		setOlderProjects(new OlderProjects());
		return SUCCESS;
	}

	public OlderProjects getOlderProjects() {
		System.out.println("Action: getOlderProjects");
		return olderProjects;
	}

	public void setOlderProjects(OlderProjects availableProjects) {
		System.out.println("setAvailableProjects");
		// this.session.put("availableProjects", availableProjects);
		this.olderProjects = availableProjects;
	}

}

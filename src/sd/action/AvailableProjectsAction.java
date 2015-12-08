package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.AvailableProjects;
import sd.model.PrimesBean;

import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class AvailableProjectsAction extends ActionSupport{
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private AvailableProjects availableProjects;
	
	@Override
	public String execute() throws Exception {
		System.out.println("execute");
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		setAvailableProjects(new AvailableProjects());
		return SUCCESS;
	}
	
	public AvailableProjects getAvailableProjects() {
		System.out.println("Action: getAvailableProjects");
		//System.out.println(session.getAttributeNames());
		return availableProjects;
	}
	public void setAvailableProjects(AvailableProjects availableProjects) {
		System.out.println("setAvailableProjects");
		//this.session.put("availableProjects", availableProjects);
		this.availableProjects = availableProjects;
	}
	
	/*@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = session;
		
	}*/
}

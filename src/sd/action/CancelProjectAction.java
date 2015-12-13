package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.CancelProjectBean;
import sd.model.RewardsBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class CancelProjectAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1175671191841904797L;
	private Map<String, Object> session;
	private CancelProjectBean cancelProjectBean;

	@Override
	public String execute() {

		if(getCancelProjectBean().cancelProject((String) session.get("username")) == 1){
			
			return SUCCESS;
		}
		return SUCCESS;
		
	}

	public CancelProjectBean getCancelProjectBean() {
		return cancelProjectBean;
	}

	public void setCancelProjectBean(CancelProjectBean cancelProjectBean) {
		// this.session.put("availableProjects", availableProjects);
		this.cancelProjectBean = cancelProjectBean;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}

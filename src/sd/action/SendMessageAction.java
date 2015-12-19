package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.ProjectDetailsBean;
import sd.model.SendMessageBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class SendMessageAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = -6308448010643497018L;
	private Map<String, Object> session;
	private SendMessageBean sendMessageBean;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public String execute() throws Exception {

		// setSendMessageBean(new
		// SendMessageBean((String)session.get("username")));
		System.out.println("execute message action");
		
		if (getSendMessageBean().send((String) session.get("username")) == 0) {
			return SUCCESS;
		} else {
			System.out.println("Error");

		}

		return SUCCESS;
	}
	public String list() throws Exception {
		System.out.println("listProjectDetails");
		setSendMessageBean(new SendMessageBean());
		getSendMessageBean().getProjects();
		return SUCCESS;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public SendMessageBean getSendMessageBean() {
		return sendMessageBean;
	}

	public void setSendMessageBean(SendMessageBean sendMessageBean) {
		this.sendMessageBean = sendMessageBean;
	}

}

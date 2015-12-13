package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import common.Client;
import common.Response;
import sd.model.AnswerMessageBean;
import sd.model.ProjectDetailsBean;

import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class AnswerMessageAction extends ActionSupport implements SessionAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8404085238610630922L;
	private Map<String, Object> session;
	private AnswerMessageBean answerMessageBean;

	
	public String list() throws RemoteException {
		System.out.println("execute listAction");
		setAnswerMessageBean(new AnswerMessageBean());
		if(getAnswerMessageBean().listMessages((String)session.get("username"))==1)
		{
			System.out.println("answerMessageBean list Message");
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	public String answer() throws Exception{
		if(getAnswerMessageBean().answer((String)session.get("username"))==1)
		{
			return SUCCESS;
		}
		return SUCCESS;
	}
	

	public AnswerMessageBean getAnswerMessageBean() {
		return answerMessageBean;
	}

	public void setAnswerMessageBean(AnswerMessageBean answerMessageBean) {
		this.answerMessageBean = answerMessageBean;
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

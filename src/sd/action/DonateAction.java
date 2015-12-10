package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.DonateBean;
import sd.model.SendMessageBean;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class DonateAction extends ActionSupport implements SessionAware{
	

	private static final long serialVersionUID = -6308448010643497018L;
	private Map<String, Object> session;
	private DonateBean donateBean;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public String execute() throws Exception{	

		if(getDonateBean().donate((String)session.get("username"))==0)
		{
			return SUCCESS;
		}
		else
		{
			System.out.println("Error");
			
		}

		return SUCCESS;
	}


	public Map<String, Object> getSession() {
		return session;
	}

	public DonateBean getDonateBean() {
		return donateBean;
	}

	public void setDonateBean(DonateBean donateBean) {
		this.donateBean = donateBean;
	}	
	
}
	
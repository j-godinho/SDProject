package sd.action;

import com.opensymphony.xwork2.ActionSupport;

import sd.model.PrimesBean;

import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class PrimesAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() throws Exception {
		System.out.println("execute");
		System.out.println(session.values());
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		return SUCCESS;
	}

	public PrimesBean getPrimesBean() {
		System.out.println("Action: getPrimesBean");
		if(!session.containsKey("primesBean"))
			this.setPrimesBean(new PrimesBean());
		return (PrimesBean) session.get("primesBean");
	}

	public void setPrimesBean(PrimesBean primesBean) {
		this.session.put("primesBean", primesBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

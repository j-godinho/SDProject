package interceptors;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthInterceptor implements Interceptor {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();

		if (session.get("username") != null) {
			System.out.println("[AuthInterceptor] @ intercept - OK");
			return invocation.invoke();
		} else {
			System.out.println("[AuthInterceptor] @ intercept - ERROR");
			session.put("authErrorNC", "Please Log in");
			return Action.ERROR;
		}
	}

}

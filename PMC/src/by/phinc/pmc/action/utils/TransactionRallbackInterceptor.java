package by.phinc.pmc.action.utils;

import by.phinc.pmc.exception.PMCNeedTransactionRallbackException;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class TransactionRallbackInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 27L;
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionRallbackInterceptor.class);

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String result = actionInvocation.invoke();
		Action action = (Action)actionInvocation.getAction();
		if (action instanceof ValidationAware) {
			ValidationAware validationAware = (ValidationAware)action;
			if (validationAware.hasErrors()) {
				LOG.info("Some validation errors have been detected");
				throw new PMCNeedTransactionRallbackException(result);
			}
		}
		return result;
	}

}

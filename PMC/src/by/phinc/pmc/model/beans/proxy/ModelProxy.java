package by.phinc.pmc.model.beans.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.dao.GenericDAO;

public class ModelProxy implements InvocationHandler {

	private GenericDAO<IModel<Integer>, Integer> dao;
	
	private IModel<Integer> model;
	
	private boolean initialized = false;
	
	
	public ModelProxy(GenericDAO<IModel<Integer>, Integer> dao, IModel<Integer> model) {
		super();
		this.dao = dao;
		this.model = model;
	}

	
	public static Object newInstance(GenericDAO<IModel<Integer>, Integer> dao, 
			IModel<Integer> model) {
		return java.lang.reflect.Proxy.newProxyInstance(
				model.getClass().getClassLoader(),
				model.getClass().getInterfaces(),
				new ModelProxy(dao, model));
	}
	
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		Object result = null;
		try {
			if (!initialized && !isIdMethod(m)) {
				System.out.println("initializing for method " + m.getName());
				model = dao.findById(model.getId(), false);
				initialized = true;
			}		
			System.out.println("before method " + m.getName());
			result = m.invoke(model, args);
		} catch (InvocationTargetException e) {
			throw new PMCException("Proxy invokation exception: " + e.getMessage());
		} finally {
			System.out.println("after method " + m.getName());
		}
		return result;
	}
	
	private boolean isIdMethod(Method m) {
		return m.getName().substring(3).equalsIgnoreCase("Id");
	}
}
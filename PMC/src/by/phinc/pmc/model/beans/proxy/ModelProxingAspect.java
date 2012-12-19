package by.phinc.pmc.model.beans.proxy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

import by.phinc.pmc.model.beans.IModel;

@Aspect
public class ModelProxingAspect {
	
	@DeclareParents(
			value="by.phinc.pmc.model.beans.Project+", 
			defaultImpl=LazyLoadingImpl.class)
	private static ILazyLoading mixin;
	
	
	//@Pointcut("within(by.phinc.pmc.model.beans..*)")
	@Pointcut("execution(* by.phinc.pmc.model.beans.*.*(..))")
	public void inBeans(){}
	
	//@Before("execution(* by.phinc.pmc.model.beans.*.*(..)) && this(lazyLoading)")
	@Before("inBeans() && this(lazyLoading)")
	public void proceedLazyLoading(ILazyLoading lazyLoading) {
		System.out.println("Before method invokation");
		if (lazyLoading instanceof IModel) {
			IModel model = (IModel)lazyLoading;
			if (!lazyLoading.isInitialized()) {
				lazyLoading.init((Integer)model.getId());
			}
		}
//		if (!lazyLoading.isInitialized()) {
//			lazyLoading.init();
//		}
	}
}

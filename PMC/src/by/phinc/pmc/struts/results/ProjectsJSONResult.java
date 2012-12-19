package by.phinc.pmc.struts.results;

import java.io.PrintWriter;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
public class ProjectsJSONResult implements Result {

	public static final String DEFAULT_PARAM = "classAlias";
	
	private static final long serialVersionUID = 27L;
	
	private String classAlias;
	
	
	public String getClassAlias() {
		return classAlias;
	}

	public void setClassAlias(String classAlias) {
		this.classAlias = classAlias;
	}


	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		ServletActionContext.getResponse().setContentType("text/json");
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		
		ValueStack stack = invocation.getStack();
		Object jsonProjects = stack.findValue("projects");
		
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		
		if (classAlias == null) {
			classAlias = "object";
		}
		
		xstream.alias(classAlias, jsonProjects.getClass());		
		out.println(xstream.toXML(jsonProjects));
	}

}

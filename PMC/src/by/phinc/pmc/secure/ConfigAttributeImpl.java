package by.phinc.pmc.secure;

public class ConfigAttributeImpl implements ConfigAttribute {
	
	private String attribute;
	
	
	
	public ConfigAttributeImpl(String attribute) {
		super();
		this.attribute = attribute;
	}

	@Override
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	
}

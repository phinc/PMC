package by.phinc.pmc.secure;

public class GrantedAuthorityImpl implements GrantedAuthority {
	
	private String authority;
	
	
	public GrantedAuthorityImpl() {
		super();
	}

	public GrantedAuthorityImpl(String authority) {
		super();
		this.authority = authority;
	}



	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}



	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GrantedAuthority)) {
			return false;
		}
		GrantedAuthority grantedAuthority = (GrantedAuthority)obj;
		return authority.equals(grantedAuthority.getAuthority());
	}
	
	
}

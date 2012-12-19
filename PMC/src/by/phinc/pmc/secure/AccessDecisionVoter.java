package by.phinc.pmc.secure;

import java.util.List;

public interface AccessDecisionVoter {
	
	enum Access {
		ACCESS_ABSTAIN, ACCESS_DENIED, ACCESS_GRANTED
	}
	
	Access vote(Authentication authentication, Object secureObject,
			List<ConfigAttribute> config);
}

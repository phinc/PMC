package by.phinc.pmc.secure;

import java.util.List;

import by.phinc.pmc.exception.PMCAccessDeniedException;
import by.phinc.pmc.secure.AccessDecisionVoter.Access;

public class AccessDecisionManagerImpl implements AccessDecisionManager {
	
	private List<AccessDecisionVoter> voters;
	
	
	public List<AccessDecisionVoter> getVoters() {
		return voters;
	}

	public void setVoters(List<AccessDecisionVoter> voters) {
		this.voters = voters;
	}

	@Override
	public void decide(Authentication authentication, Object secureObject,
			List<ConfigAttribute> config) throws PMCAccessDeniedException {
		boolean isAccess = true;
		for (AccessDecisionVoter voter : voters) {
			isAccess = isAccess && checkAccess(voter.vote(authentication, secureObject, config));
		}
		if (!isAccess) {
			throw new PMCAccessDeniedException();
		}
	}
	
	/*
	 * return true if access is abstained or granted
	 */
	private boolean checkAccess(Access access) {
		return Access.ACCESS_ABSTAIN.equals(access) ||
				Access.ACCESS_GRANTED.equals(access);
	}

}

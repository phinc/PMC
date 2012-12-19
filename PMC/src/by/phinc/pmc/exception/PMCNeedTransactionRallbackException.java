package by.phinc.pmc.exception;

public class PMCNeedTransactionRallbackException extends PMCException {

	private static final long serialVersionUID = 27L;
	
	private String result;

	public PMCNeedTransactionRallbackException(String result) {
		super();
		this.result = result;
	}

	public String getResult() {
		return result;
	}
	
	

}

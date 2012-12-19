package by.phinc.pmc.model.beans;

public enum Status implements IModel<Integer>{
	
	NOT_STARTED(1),
	IN_PROGRESS(2),
	COMPLETED(3),
	CANCELLED(4),
	ON_HOLD(5);
	
	private Integer id;
	
	Status(Integer id) {
		this.id = id;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer pId) {
		this.id = pId;
	}
	
	public static Status getStatusById(Integer id) {
		Status[] statuses = Status.values();
		for (Status st : statuses) {
			if (st.id == id) {
				return st;
			}
		}
		return null;
	}

}

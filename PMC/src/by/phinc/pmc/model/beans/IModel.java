package by.phinc.pmc.model.beans;

import java.io.Serializable;

public interface IModel<ID extends Serializable> {
   
	public abstract ID getId();
    
	public abstract void setId(final ID pId);
}
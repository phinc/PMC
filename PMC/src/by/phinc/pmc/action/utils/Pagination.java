package by.phinc.pmc.action.utils;

import java.util.Collection;

import by.phinc.pmc.model.beans.IModel;

public class Pagination<T extends IModel<Integer>, O extends IModel<Integer>> {
	
	private Integer id;  //owner object id
	
	private O owner; //owner object
	
	private int page;    //current page number
	
	private int rows;    //number of rows on the page
	
	private String sidx; //column name to sort by
	
	private String sord; //sort order
	
	private int records; //total number of rows
	
	private Collection<T> entities;

	
	public Pagination() {
		super();
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public O getOwner() {
		return owner;
	}

	public void setOwner(O owner) {
		this.owner = owner;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public int getTotal() {
		return (int)Math.ceil((double)getRecords() / getRows());
	}

	public Collection<T> getEntities() {
		return entities;
	}

	public void setEntities(Collection<T> entities) {
		this.entities = entities;
	}
	
	public int getFirstRow() {
		return (page - 1) * rows;
	}
}

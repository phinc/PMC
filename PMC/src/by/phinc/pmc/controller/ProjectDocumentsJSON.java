/*
 * ProjectDocumentsJSON
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of documents according to the give project id.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of documents is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.service.IDocumentService;
import by.phinc.pmc.service.IProjectService;

public class ProjectDocumentsJSON extends BaseJSONAction {
	
	private static final long serialVersionUID = 27L;
	
	private IDocumentService documentService;
	
	private IProjectService projectService;
	
	private Pagination<Document, Project> pagination = new Pagination<Document, Project>();
	
	
	
	public void setDocumentService(IDocumentService documentService) {
		this.documentService = documentService;
	}
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	public void setId(Integer id) {
		pagination.setId(id);
	}
	
	public Integer getId() {
		return pagination.getId();
	}
	
	public int getPage() {
		return pagination.getPage();
	}
	
	public void setPage(int page) {
		pagination.setPage(page);
	}
	
	public int getRows() {
		return pagination.getRows();
	}
	
	public void setRows(int rows) {
		pagination.setRows(rows);
	}
	
	public void setSidx(String sidx) {
		pagination.setSidx(sidx);
	}
	
	public void setSord(String sord) {
		pagination.setSord(sord);
	}
	
	public int getRecords() {
		return pagination.getRecords();
	}
	
	public void setRecords(int records) {
		pagination.setRecords(records);
	}
	
	public int getTotal() {
		return pagination.getTotal();
	}
	
	public Collection<Document> getDocuments() {
		return pagination.getEntities();
	}

	public String execute() {
		pagination.setOwner(projectService.findById(getId()));
		documentService.findWithPaginationByProject(pagination);
		return SUCCESS;
	}
}

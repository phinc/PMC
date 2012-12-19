/*
 * DocumentCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for document object.
 * It also removes the file bound to the document.
 */
package by.phinc.pmc.controller;

import java.io.File;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.service.IDocumentService;
import by.phinc.pmc.service.IModelService;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.util.DocumentUtil;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ProjectDocumentCRUD extends BaseCRUDAction 
		implements ModelDriven<Document>, Preparable, SessionAware	{
	
	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/documentDialog.jsp";
	
	@SuppressWarnings("rawtypes")
	private Map session;
	
	private IDocumentService documentService;
	
	private IProjectService projectService;
	
	private File file;
	
	private String fileContentType;
	
	private String fileFileName;
	
	private Document model;
	
	
	
	public void setDocumentService(IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	@Override
	public Document getModel() {
		return model;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	
	@Override
	public void prepare() throws Exception {
		if (getId() == null) {
			model = new Document();
		} else {
			model = documentService.findById(getId());
		}
	}
	
	@Override
	public String getDestination() {
		return DESTINATION;
	}

	@Override
	public String save() {
		model.setFilename(file.getName());
		model.setPath(file.getPath());
		model.setName(fileFileName);
		DocumentUtil.saveDocument(file, model);
		//TO DO
		Project project = (Project)session.get(Constants.PROJECT);
		project = projectService.findById(project.getId());
		project.addDocument(model);
		return super.save();
	}

	@Override
	public String remove() {
		DocumentUtil.deleteDocument(model);
		return super.remove();
	}
	
	public void validate() {
		if (getModel().getDescription() == null || 
				getModel().getDescription().length() == 0) {
			addFieldError("description", getText("document.description.required"));
		}
		if (file == null) {
			addFieldError("file", getText("document.file.required"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)documentService;
	}

}

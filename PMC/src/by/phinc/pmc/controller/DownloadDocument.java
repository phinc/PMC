package by.phinc.pmc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.service.IDocumentService;

public class DownloadDocument extends BaseAction {

	private static final long serialVersionUID = 27L;
	
	private IDocumentService documentService;
	
	private Integer id;
	
	private InputStream fileInputStream;
	
	private String filename;
	
	
	
	public void setDocumentService(IDocumentService documentService) {
		this.documentService = documentService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	
	public String getFilename() {
		return filename;
	}

	public String execute() {
		Document doc = documentService.findById(id);
		try {
			fileInputStream = new FileInputStream(new File(doc.getPath()));
			filename = doc.getName();
			return SUCCESS;
		} catch (FileNotFoundException e) {
			addActionError(getText("file.not.found") + ": " + doc.getPath());
			throw new PMCException(e);
		}
	}
}

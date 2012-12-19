package by.phinc.pmc.model.dao;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.DocumentOwner;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public interface IDocumentDAO extends GenericDAO<Document, Integer> {
	
	void fillOwnerWithDocuments(DocumentOwner owner);
	
	/*
	 * Fetch the collection of documents by the project id.
	 * Perform the pagination of the result.
	 */
	void findWithPaginationByProject(Pagination<Document, Project> pagination);
	
	/*
	 * Fetch the collection of documents by the task id.
	 * Perform the pagination of the result.
	 */
	void findWithPaginationByTask(Pagination<Document, Task> pagination);
}

package by.phinc.pmc.service;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public interface IDocumentService extends IModelService<Document, Integer> {
	
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

package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public class DocumentServiceImpl extends AbstractService implements
		IDocumentService {

	@Override
	@Transactional(readOnly=true)
	public Document findById(Integer id) {
		return getDaoFactory().getDocumentDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Document> findAll() {
		return getDaoFactory().getDocumentDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Document> findByExample(Document example) {
		return getDaoFactory().getDocumentDAO().findByExample(example);
	}

	@Override
	@Transactional
	public Document makePersistent(Document model) {
		return getDaoFactory().getDocumentDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(Document model) {
		getDaoFactory().getDocumentDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPaginationByProject(Pagination<Document, Project> pagination) {
		getDaoFactory().getDocumentDAO().findWithPaginationByProject(pagination);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPaginationByTask(Pagination<Document, Task> pagination) {
		getDaoFactory().getDocumentDAO().findWithPaginationByTask(pagination);
	}

}

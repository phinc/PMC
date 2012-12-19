package by.phinc.pmc.model.beans;

import java.util.SortedSet;

public interface DocumentOwner extends IModel<Integer> {
	
	SortedSet<Document> getDocuments();
	
	void setDocuments(SortedSet<Document> documents);
	
	void addDocument(Document document);
}

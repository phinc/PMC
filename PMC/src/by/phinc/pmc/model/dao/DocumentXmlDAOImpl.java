package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.DocumentOwner;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;
import static by.phinc.pmc.util.Constants.*;

public class DocumentXmlDAOImpl extends GenericXmlDAO<Document, Integer>
		implements IDocumentDAO {
	
	private enum Tag implements ITagBase{
		DOCUMENTS, DOCUMENT, NAME, FILE_NAME,
		DESCRIPTION, PATH, PROJECT, TASK;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private static final String DATA_FILE;
	
	private static final String TEMP_FILE;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);
		String path = bundle.getString("xml.data.path");
		String filename = bundle.getString("xml.data.document.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	

	private Document readDocument(XMLEvent event, XMLEventReader eventReader) 
				throws IOException, XMLStreamException, ParseException{
		
		XMLEvent nextEvent;
		String seName = "";
		EndElement ee;
		@SuppressWarnings("unused")
		Integer tempId;
		
		Document document = new Document();
		StartElement se = event.asStartElement();
		Attribute attribute = se.getAttributeByName(
    							new QName(Attr.ID.toTagName()));
    	document.setId(Integer.valueOf(attribute.getValue()));
		
		
		while(eventReader.hasNext()){
			event = eventReader.nextEvent();
			nextEvent = eventReader.peek();
			    
			seName = "";
			    
			switch (event.getEventType()) {
	            case START_ELEMENT :
	            	se = event.asStartElement();
	                seName = se.getName().getLocalPart();
	                
	                Tag tag = Tag.valueOf(toEnumName(seName));
	                switch (tag){
		                case DESCRIPTION:
		                	document.setDescription(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case NAME:
		                	document.setName(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case FILE_NAME:
		                	document.setFilename(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case PATH:
		                	document.setPath(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case PROJECT:
		                	attribute = se.getAttributeByName(
		                			new QName(Attr.ID.toTagName()));
		                	tempId = Integer.valueOf(attribute.getValue());
		                	//document.setOwner(new ProjectProxy(tempId));
		                	break;
		                case TASK:
		                	attribute = se.getAttributeByName(
		                			new QName(Attr.ID.toTagName()));
		                	tempId = Integer.valueOf(attribute.getValue());
		                	//document.setOwner(new TaskProxy(tempId));
		                	break;
		                default:
		               		throw new PMCException("Error data file format '" + 
		               				DATA_FILE + "' unknown tag " + seName);
	                }
	                break;            
	            case END_ELEMENT:
	            	ee = event.asEndElement();
	            	seName = ee.getName().getLocalPart();
	            	if (Tag.DOCUMENT.toTagName().equals(seName)) {
	            		return document;
	            	}
	            	break;
	            default :
	                break;
			}
		}
		return null;
	}

	
	@Override
	public Document findById(final Integer id, boolean lock) {
		return (new XmlStreamInProcessingTemplate<Document>() {

			@Override
			public Document doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				Document document = null;
				String seName = "";
				StartElement se;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
					seName = "";
					    
					switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case DOCUMENTS:
				                	break;
				                case DOCUMENT:
				                	document = readDocument(event, eventReader);
				                	if (document.getId().equals(id)) {
				                		return document;
				                	}
				                	break;
//				                	document = new Document();
//				                	attribute = se.getAttributeByName(
//				                			new QName(Attr.ID.toTagName()));
//				                	document.setId(Integer.valueOf(
//		                					attribute.getValue()));				                	
//				                	break;
//				                case DESCRIPTION:
//				                	document.setDescription(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case NAME:
//				                	document.setName(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case FILE_NAME:
//				                	document.setFilename(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case PATH:
//				                	document.setPath(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case PROJECT:
//				                	attribute = se.getAttributeByName(
//				                			new QName(Attr.ID.toTagName()));
//				                	tempId = Integer.valueOf(attribute.getValue());
//				                	document.setOwner(new ProjectProxy(tempId));
//				                	break;
//				                case TASK:
//				                	attribute = se.getAttributeByName(
//				                			new QName(Attr.ID.toTagName()));
//				                	tempId = Integer.valueOf(attribute.getValue());
//				                	document.setOwner(new TaskProxy(tempId));
//				                	break;
//				                default:
//				               		throw new PMCException("Error data file format '" + 
//				               				DATA_FILE + "' unknown tag " + seName);
			                }
			                break;
			            default :
			                break;
					}
				}
				return null;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	@Override
	public Collection<Document> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Document> findByExample(Document exampleInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document makePersistent(Document document) {
		synchronized (this.getClass()){
			
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> document)
						throws IOException, XMLStreamException {
					
					XMLEvent event;
					String seName = "";
					StartElement se;
					EndElement ee;
					Characters characters;
					int maxId = -1;
					int curId = -1;
					XMLEventFactory eventFactory = XMLEventFactory.newInstance();
					
					while(eventReader.hasNext()){
						event = eventReader.nextEvent();
					    
					    switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case DOCUMENTS:
				                	eventWriter.add(event);
				                	break;
				                case DOCUMENT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing document
				                	if ((document.getId() != null) && 
				                			(document.getId() == curId)) {
				                		createDocumentNode(eventFactory, 
				                				eventWriter, (Document)document);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((document.getId() == null) || 
				                			(document.getId() != curId)) {
				                		eventWriter.add(event);
				                	}
				                	break;
			                }
			                break;            
			            case START_DOCUMENT:
			            case END_DOCUMENT:
			            	eventWriter.add(event);
			            	break;
			            case CHARACTERS: 
			            	if ((document.getId() == null) || 
			            			(document.getId() != curId)) {
				            	//insert cdata section for elements
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case DESCRIPTION:
						                case NAME:
						                case PATH:
						                	characters = eventFactory.createCData(
						                			event.asCharacters().getData());
						                	eventWriter.add(characters);
						                	break;
						                default:
						                	eventWriter.add(event);
						                	break;
					                }
				            	} else {
				            		eventWriter.add(event);
				            	}
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	tag = Tag.valueOf(toEnumName(seName));
			            	switch (tag) {
				            	case DOCUMENTS:
				            		if (document.getId() == null) {
				            			//insert new document at the end of the file
				            			document.setId(++maxId);
					            		createDocumentNode(eventFactory, 
					            				eventWriter, (Document)document);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating
			            			if ((document.getId() == null) || 
			            					(document.getId() != curId)) {
			            				eventWriter.add(event);					            		
			            			}
			            			break;
			            	}
			            	seName = "";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, document, readWriteLock);
			
			return document;
		}
	}
	
	private void createDocumentNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Document document) 
					throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.DOCUMENT.toTagName());		
		eventWriter.add(startElement);
		//save attributes
		eventWriter.add(eventFactory.createAttribute(Attr.ID.toTagName(), 
				String.valueOf(document.getId())));
		eventWriter.add(newLine);
		
		createItemNode(eventFactory, eventWriter, 
				Tag.NAME.toTagName(), document.getName(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.DESCRIPTION.toTagName(), document.getDescription(), true);		
		createItemNode(eventFactory, eventWriter, 
				Tag.FILE_NAME.toTagName(), document.getFilename(), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.PATH.toTagName(), document.getPath(), true);
		
		eventWriter.add(tab);
//		String base = (document.getOwner() instanceof Project) ? 
//				Tag.PROJECT.toTagName(): Tag.TASK.toTagName();
//		eventWriter.add(eventFactory.createStartElement("", "", base));
//		eventWriter.add(eventFactory.createAttribute(Attr.ID.toTagName(), 
//				document.getOwner().getId().toString()));		
//		eventWriter.add(eventFactory.createEndElement("", "", base));
//		eventWriter.add(newLine);
		
		eventWriter.add(tab);
		eventWriter.add(eventFactory.createEndElement("", "", 
				Tag.DOCUMENT.toTagName()));
		eventWriter.add(newLine);
	}

	@Override
	public void makeTransient(Document document) {
		synchronized (this.getClass()){
			
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> document)
						throws IOException, XMLStreamException {
					
					XMLEvent event;
					String seName = "";
					StartElement se;
					EndElement ee;
					Characters characters;
					int curId = -1;
					XMLEventFactory eventFactory = XMLEventFactory.newInstance();
					
					while(eventReader.hasNext()){
						event = eventReader.nextEvent();
					    
					    switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case DOCUMENTS:
				                	eventWriter.add(event);
				                	break;
				                case DOCUMENT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//omit removing document
				                	if (document.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (document.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
			                }
			                break;            
			            case START_DOCUMENT:
			            case END_DOCUMENT:
			            	eventWriter.add(event);
			            	break;
			            case CHARACTERS: 
			            	if (document.getId() != curId) {
				            	//insert cdata section for elements
			            		if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case DESCRIPTION:
						                case NAME:
						                case PATH:
						                	characters = eventFactory.createCData(
						                			event.asCharacters().getData());
						                	eventWriter.add(characters);
						                	break;
						                default:
						                	eventWriter.add(event);
						                	break;
					                }
				            	} else {
				            		eventWriter.add(event);
				            	}
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	if (document.getId() != curId || 
			            			Tag.DOCUMENTS.toTagName().equals(seName)) {
					            eventWriter.add(event);
			            	}
			            	seName="";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, document, readWriteLock);
		}
	}

	@Override
	public void fillOwnerWithDocuments(final DocumentOwner owner) {
		new XmlStreamInProcessingTemplate<SortedSet<Document>>() {
			
			@Override
			public SortedSet<Document> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				@SuppressWarnings("unused")
				Document document = null;
				String seName = "";
				StartElement se;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
//					nextEvent = eventReader.peek();					    
					seName = "";
					    
					switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case DOCUMENTS:
				                	owner.setDocuments(new TreeSet<Document>());
				                	break;
				                case DOCUMENT:
				                	document = readDocument(event, eventReader);
//				                	if (document.getOwner().equals(owner)) {
//				                		owner.addDocument(document);
//				                	}
//				                	document = new Document();
//				                	curDocumentId = 0;				                	
//				                	attribute = se.getAttributeByName(
//				                			new QName(Attr.ID.toTagName()));
//				                	document.setId(Integer.valueOf(
//		                					attribute.getValue()));
//				                	break;
//				                case DESCRIPTION:
//				                	document.setDescription(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case NAME:
//				                	document.setName(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case FILE_NAME:
//				                	document.setFilename(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case PATH:
//				                	document.setPath(
//				                			getElementText(eventReader, nextEvent));
//				                	break;
//				                case PROJECT:
//				                	if (base instanceof Project) {
//					                	attribute = se.getAttributeByName(
//					                			new QName(Attr.ID.toTagName()));
//					                	curDocumentId = Integer.valueOf(attribute.getValue());
//				                	}
//				                	break;
//				                case TASK:				                	
//				                	if (base instanceof Task) {
//				                		attribute = se.getAttributeByName(
//					                			new QName(Attr.ID.toTagName()));
//					                	curDocumentId = Integer.valueOf(attribute.getValue());
//				                	}
//				                	break;
//				                default:
//				               		throw new PMCException("Error data file format '" + 
//				               					DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
//			            case END_ELEMENT:
//			            	ee = event.asEndElement();
//			            	temp = ee.getName().getLocalPart();
//			            	//add document to list only if its project id is equal to the given id
//			            	if (temp.equals(Tag.DOCUMENT.toTagName()) && 
//			            			curDocumentId.equals(base.getId())) {
//			            		base.addDocument(document);
//			            	}
			            default :
			                break;
					}
				}
				return owner.getDocuments();
			}
			
		}.process(DATA_FILE, readWriteLock);
	}
	


	@Override
	public void findWithPaginationByProject(final Pagination<Document, Project> pagination) {
		Project project = pagination.getOwner();
		
		fillOwnerWithDocuments(project);		
		if (project.getDocuments() == null || project.getDocuments().isEmpty()) {
			return;
		}
		Comparator<Document> comp = new Comparator<Document>(){

			@Override
			public int compare(Document arg0, Document arg1) {
				if (pagination.getSidx().equals("name")) {
					return arg0.getName().compareTo(arg1.getName());
				} else if (pagination.getSidx().equals("description")) {
					return arg0.getDescription().compareTo(arg1.getDescription());
				} 
				return 0;
			}
			
		};
		
		List<Document> documentList = new ArrayList<Document>(project.getDocuments());
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(documentList, comp);
		} else {
			Collections.sort(documentList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(documentList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(documentList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

	
	@Override
	public void findWithPaginationByTask(final Pagination<Document, Task> pagination) {
		
		Task task = pagination.getOwner();
		
		fillOwnerWithDocuments(task);		
		if (task.getDocuments() == null || task.getDocuments().isEmpty()) {
			return;
		}
		Comparator<Document> comp = new Comparator<Document>(){

			@Override
			public int compare(Document arg0, Document arg1) {
				if (pagination.getSidx().equals("name")) {
					return arg0.getName().compareTo(arg1.getName());
				} else if (pagination.getSidx().equals("description")) {
					return arg0.getDescription().compareTo(arg1.getDescription());
				} 
				return 0;
			}
			
		};
		
		List<Document> documentList = new ArrayList<Document>(task.getDocuments());
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(documentList, comp);
		} else {
			Collections.sort(documentList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(documentList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(documentList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

}

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.util.DocumentUtil;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;

public class TaskXmlDAOImpl extends GenericXmlDAO<Task, Integer> implements ITaskDAO{
	
	private enum Tag implements ITagBase{
		TASKS, TASK, SHORT_DESCRIPTION, LONG_DESCRIPTION,
		PLANNED_START_DATE, PLANNED_DURATION,
		ACTUAL_START_DATE, ACTUAL_DURATION;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID, STATUS_ID, PROJECT_ID;

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
		String filename = bundle.getString("xml.data.task.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	
	private IAssignmentDAO assignmentDAO;
	
	private IDocumentDAO documentDAO;
	
	
	public TaskXmlDAOImpl() {
		super();
	}
	

	public IAssignmentDAO getAssignmentDAO() {
		return assignmentDAO;
	}

	public void setAssignmentDAO(IAssignmentDAO assignmentDAO) {
		this.assignmentDAO = assignmentDAO;
	}

	public IDocumentDAO getDocumentDAO() {
		return documentDAO;
	}

	public void setDocumentDAO(IDocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}


	@Override
	public Task findById(final Integer id, boolean lock) {
		return (new XmlStreamInProcessingTemplate<Task>() {

			@Override
			public Task doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				Task task = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Status status = null;
				Integer tempId;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
					nextEvent = eventReader.peek();
					    
					seName = "";
					attrName = "";
					    
					switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case TASKS:
				                	break;
				                case TASK:
				                	task = new Task();
				                	
				                	@SuppressWarnings("unchecked")
									Iterator<Attribute> attributes = se.getAttributes();
				                	Attribute attribute;
				                	Attr attr;
				                	while (attributes.hasNext()) {
				                		attribute = attributes.next();
				                		attrName = attribute.getName().getLocalPart();
				                		attr = Attr.valueOf(toEnumName(attrName));
				                		switch (attr) {
					                		case ID:
					                			task.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case PROJECT_ID:
					                			tempId = Integer.valueOf(
					                					attribute.getValue());
					                			task.setProject(proxyFactory.getProjectProxy(tempId));					                			
					                			break;
					                		case STATUS_ID:
					                			status = Status.getStatusById(
					                					Integer.valueOf(attribute.getValue()));
					                			task.setStatus(status);
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                					DATA_FILE + "' unknown attribute " +
					                					attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	task.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	task.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	task.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	task.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		task.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		task.setActDuration(Float.valueOf(temp));
				                	}
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + 
				               				DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	//add project to list only if its project id is equal to the given id
			            	if (temp.equals(Tag.TASK.toTagName()) && 
			            			id.equals(task.getId())) {
			            		return task;
			            	}
			            default :
			                break;
					}
				}
				return null;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	
	@Override
	public List<Task> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> findByExample(Task exampleInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task makePersistent(Task task) {
		synchronized (this.getClass()){
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> task)
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
				                case TASKS:
				                	eventWriter.add(event);
				                	break;
				                case TASK:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing task
				                	if ((task.getId() != null) && 
				                			(task.getId() == curId)) {
				                		createTaskNode(eventFactory, 
				                				eventWriter, (Task)task);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((task.getId() == null) || 
				                			(task.getId() != curId)) {
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
			            	if ((task.getId() == null) || (task.getId() != curId)) {
				            	//insert cdata section for elements: name, description
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case SHORT_DESCRIPTION:
						                case LONG_DESCRIPTION:
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
				                	break;
				            	}
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	tag = Tag.valueOf(toEnumName(seName));
			            	switch (tag) {
				            	case TASKS:
				            		if (task.getId() == null) {
				            			//insert new task at the end of the file
				            			task.setId(++maxId);
					            		createTaskNode(eventFactory, eventWriter, (Task)task);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating project
			            			if ((task.getId() == null) || (task.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, task, readWriteLock);
		
			return task;
		}
	}

	@Override
	public void makeTransient(Task task) {
		synchronized (this.getClass()){
			removeTask(task);
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> task)
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
				                case TASKS:
				                	eventWriter.add(event);
				                	break;
				                case TASK:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//omit removing task
				                	if (task.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (task.getId() != curId) {
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
			            	if (task.getId() != curId) {
				            	//insert cdata section for elements
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case SHORT_DESCRIPTION:
						                case LONG_DESCRIPTION:
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
				                	break;
				            	}
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	if (task.getId() != curId || 
			            			Tag.TASKS.toTagName().equals(seName)) {
					            eventWriter.add(event);
			            	}
			            	seName="";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, task, readWriteLock);
		}
	}

	
	private void removeTask(Task task) {
		assignmentDAO.fillTaskWithAssignments(task);
		if (task.getAssignments() != null) {			
			for (Assignment assignment : task.getAssignments()) {
				assignmentDAO.makeTransient(assignment);
			}
		}
		documentDAO.fillOwnerWithDocuments(task);
		if (task.getDocuments() != null) {
			for (Document document : task.getDocuments()) {
				DocumentUtil.deleteDocument(document);
				documentDAO.makeTransient(document);
			}
		}
	}
	
	@Override
	public void fillProjectWithTasks(final Project project) {
		new XmlStreamInProcessingTemplate<SortedSet<Task>>() {

			@Override
			public SortedSet<Task> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				Task task = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Status status = null;
				Integer curProjectId = 0;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
					nextEvent = eventReader.peek();
					    
					seName = "";
					attrName = "";
					    
					switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case TASKS:
				                	project.setTasks(new TreeSet<Task>());
				                	break;
				                case TASK:
				                	task = new Task();
				                	curProjectId = 0;
				                	
				                	@SuppressWarnings("unchecked")
									Iterator<Attribute> attributes = se.getAttributes();
				                	Attribute attribute;
				                	Attr attr;
				                	while (attributes.hasNext()) {
				                		attribute = attributes.next();
				                		attrName = attribute.getName().getLocalPart();
				                		attr = Attr.valueOf(toEnumName(attrName));
				                		switch (attr) {
					                		case ID:
					                			task.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case PROJECT_ID:
					                			curProjectId = Integer.valueOf(
					                					attribute.getValue()); 			
					                			break;
					                		case STATUS_ID:
					                			status = Status.getStatusById(
					                					Integer.valueOf(attribute.getValue()));
					                			task.setStatus(status);
					                			break;
					                		default:
					                			throw new PMCException(
					                					"Error data file format '" + 
					                					DATA_FILE + "' unknown attribute " + 
					                					attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	task.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	task.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	task.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	task.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		task.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		task.setActDuration(Float.valueOf(temp));
				                	}
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + 
				               				DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	//add project to list only if its project id is equal to the given id
			            	if (temp.equals(Tag.TASK.toTagName()) && 
			            			curProjectId.equals(project.getId())) {
			            		project.addTask(task);
			            	}
			            default :
			                break;
					}
				}
				return project.getTasks();
			}
			
		}.process(DATA_FILE, readWriteLock);
	}
	
	
	private void createTaskNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Task task) 
					throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.TASK.toTagName());
		
		eventWriter.add(startElement);
		Attribute idAttr = eventFactory.createAttribute(
				Attr.ID.toTagName(), String.valueOf(task.getId()));
		eventWriter.add(idAttr);
		Attribute statusAttr = eventFactory.createAttribute(
				Attr.STATUS_ID.toTagName(), String.valueOf(task.getStatus().getId()));
		eventWriter.add(statusAttr);
		eventWriter.add(eventFactory.createAttribute(Attr.PROJECT_ID.toTagName(), 
				task.getProject().getId().toString()));
		eventWriter.add(newLine);
		
		createItemNode(eventFactory, eventWriter, 
				Tag.SHORT_DESCRIPTION.toTagName(), task.getName(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.LONG_DESCRIPTION.toTagName(), task.getDescription(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_START_DATE.toTagName(), dateToString(task.getPlanStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_DURATION.toTagName(), 
				(task.getPlanDuration() != null) ? 
						String.valueOf(task.getPlanDuration()) : "", false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_START_DATE.toTagName(), dateToString(task.getActStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_DURATION.toTagName(), 
				(task.getActDuration() != null) ? 
						String.valueOf(task.getActDuration()) : "", false);
		
		eventWriter.add(tab);
		eventWriter.add(eventFactory.createEndElement("", "", Tag.TASK.toTagName()));
		eventWriter.add(newLine);
	}

	@Override
	public void findWithPagination(final Pagination<Task, Project> pagination) {
		Project project = pagination.getOwner();
		fillProjectWithTasks(project);
		
		if (project.getTasks() == null || project.getTasks().isEmpty()) {
			return;
		}
		Comparator<Task> comp = new Comparator<Task>(){

			@Override
			public int compare(Task arg0, Task arg1) {
				if (pagination.getSidx().equals("name")) {
					return arg0.getName().compareTo(arg1.getName());
				} else if (pagination.getSidx().equals("planStart")) {
					return arg0.getPlanStart().compareTo(arg1.getPlanStart());
				} else if (pagination.getSidx().equals("status")) {
					return arg0.getStatus().compareTo(arg1.getStatus());
				} 
				return 0;
			}
			
		};
		
		List<Task> taskList = new ArrayList<Task>(project.getTasks());
		if ("asc".equals(pagination.getSord())) {
			Collections.sort(taskList, comp);
		} else {
			Collections.sort(taskList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(taskList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(taskList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

}

package by.phinc.pmc.model.dao;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReadWriteLock;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import static javax.xml.stream.XMLStreamConstants.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.util.DocumentUtil;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;
import static by.phinc.pmc.util.Constants.*;

public class ProjectXmlDAOImpl extends GenericXmlDAO<Project, Integer> 
							   implements IProjectDAO {	
	
	private enum Tag implements ITagBase{
		PROJECTS, PROJECT, NAME, DESCRIPTION,
		CUSTOMER, PLANNED_START_DATE, PLANNED_DURATION,
		ACTUAL_START_DATE, ACTUAL_DURATION;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID, STATUS_ID;

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
		String filename = bundle.getString("xml.data.project.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	
	private ITeamDAO teamDAO;
	
	private ITaskDAO taskDAO;
	
	private IDocumentDAO documentDAO;
	
	
	public ProjectXmlDAOImpl() {
		super();
	}


	public ITeamDAO getTeamDAO() {
		return teamDAO;
	}

	public void setTeamDAO(ITeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	public ITaskDAO getTaskDAO() {
		return taskDAO;
	}

	public void setTaskDAO(ITaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public IDocumentDAO getDocumentDAO() {
		return documentDAO;
	}
	
	public void setDocumentDAO(IDocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}


	@Override
	public Project findById(final Integer id, boolean lock) {		
		
		return (new XmlStreamInProcessingTemplate<Project>() {

			@Override
			public Project doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				Project project = null;
				XMLEvent event;
				XMLEvent nextEvent;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				int statusId;
				
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
		                case PROJECTS:
		                	break;
		                case PROJECT:
		                	project = new Project();
		                	
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
			                			project.setId(Integer.valueOf(attribute.getValue()));
			                			break;
			                		case STATUS_ID:
			                			statusId = Integer.valueOf(attribute.getValue());
			                			project.setStatus(Status.getStatusById(statusId));
			                			break;
			                		default:
			                			throw new PMCException("Error data file format '" + 
			                					DATA_FILE + "' unknown attribute " + attrName);
		                		}
		                	}
		                	break;
		                case NAME:
		                	project.setName(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case DESCRIPTION:
		                	project.setDescription(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case CUSTOMER:
		                	project.setCustomer(
		                			getElementText(eventReader, nextEvent));
		                	break;
		                case PLANNED_START_DATE:
		                	project.setPlanStart(dateFormat.parse(
		                			getElementText(eventReader, nextEvent)));
		                	break;
		                case PLANNED_DURATION:
		                	project.setPlanDuration(Float.valueOf(
		                			getElementText(eventReader, nextEvent)));
		                	break;
		                case ACTUAL_START_DATE:
		                	temp = getElementText(eventReader, nextEvent);
		                	if (!temp.isEmpty()) {
		                		project.setActStart(dateFormat.parse(temp));
		                	}
		                	break;
		                case ACTUAL_DURATION:
		                	temp = getElementText(eventReader, nextEvent);
		                	if (!temp.isEmpty()) {
		                		project.setActDuration(Float.valueOf(temp));
		                	}
		                	break;
		                default:
		               		throw new PMCException("Error data file format '" + DATA_FILE +
		               				 "' unknown tag " + seName);
		                }
		                break;            
		            case END_ELEMENT:
		            	ee = event.asEndElement();
		            	seName = ee.getName().getLocalPart();
		            	if (Tag.PROJECT.toTagName().equals(seName) && project != null 
		            			&& id == project.getId()) {
		            		return project;
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
	public List<Project> findAll() {
		return (new XmlStreamInProcessingTemplate<List<Project>>() {

			@Override
			public List<Project> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				List<Project> projectList = null;
				Project project = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				int statusId;
				
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
				                case PROJECTS:
				                	projectList = new ArrayList<Project>();
				                	break;
				                case PROJECT:
				                	project = new Project();
				                	
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
					                			project.setId(
					                					Integer.valueOf(attribute.getValue()));
					                			break;
					                		case STATUS_ID:
					                			statusId = Integer.valueOf(attribute.getValue());
					                			project.setStatus(Status.getStatusById(statusId));
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                					DATA_FILE + "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case NAME:
				                	project.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case DESCRIPTION:
				                	project.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case CUSTOMER:
				                	project.setCustomer(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	project.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	project.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		project.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		project.setActDuration(Float.valueOf(temp));
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
			            	if (temp.equals(Tag.PROJECT.toTagName())) {
			            		if (projectList == null) {
			                		throw new PMCException("Error data file format '" + 
			                								DATA_FILE + "'");
			                	}
			                	projectList.add(project);
			            	}
			            default :
			                break;
					}
				}
				return projectList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}


	@Override
	public List<Project> findByExample(Project exampleInstance) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Project makePersistent(Project project) {		
		synchronized (this.getClass()){
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> project)
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
				                case PROJECTS:
				                	eventWriter.add(event);
				                	break;
				                case PROJECT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing project
				                	if ((project.getId() != null) && 
				                			(project.getId() == curId)) {
				                		createProjectNode(eventFactory, eventWriter, (Project)project);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((project.getId() == null) 
				                			|| (project.getId() != curId)) {
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
			            	if ((project.getId() == null) || (project.getId() != curId)) {
				            	//insert cdata section for elements: name, description and customer
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case NAME:
						                case DESCRIPTION:
						                case CUSTOMER:
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
				            	case PROJECTS:
				            		if (project.getId() == null) {
				            			//insert new project at the end of the file
				            			project.setId(++maxId);
					            		createProjectNode(eventFactory, eventWriter, (Project)project);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating project
			            			if ((project.getId() == null) || 
			            					(project.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, project, readWriteLock);
			return project;
		}
	}


	@Override
	public void makeTransient(Project project) {
		synchronized (this.getClass()){
			//remove associated objects
			removeProject(project);
			
			new XmlStreamProcessingTemplate() {
					
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> project)
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
				                case PROJECTS:
				                	eventWriter.add(event);
				                	break;
				                case PROJECT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//omit removing project
				                	if (project.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (project.getId() != curId) {
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
			            	if (project.getId() != curId) {
				            	//insert cdata section for elements
			            		if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case NAME:
						                case DESCRIPTION:
						                case CUSTOMER:
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
			            	if (project.getId() != curId || Tag.PROJECTS.toTagName().equals(seName)) {
					            eventWriter.add(event);
			            	}
			            	seName="";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, project, readWriteLock);
		}
	}
	
	private void removeProject(Project project) {
		teamDAO.fillProjectWithTeam(project);
		if (project.getTeam() != null) {			
			for (TeamMember member : project.getTeam()) {
				teamDAO.makeTransient(member);
			}
		}
		taskDAO.fillProjectWithTasks(project);
		if (project.getTasks() != null) {
			for (Task task : project.getTasks()) {
				taskDAO.makeTransient(task);
			}
		}
		documentDAO.fillOwnerWithDocuments(project);
		if (project.getDocuments() != null) {
			for (Document document : project.getDocuments()) {
				DocumentUtil.deleteDocument(document);
				documentDAO.makeTransient(document);
			}
		}
	}
	
	private void createProjectNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Project project) throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.PROJECT.toTagName());
		
		eventWriter.add(startElement);
		Attribute idAttr = eventFactory.createAttribute(
				Attr.ID.toTagName(), String.valueOf(project.getId()));
		eventWriter.add(idAttr);
		Attribute statusAttr = eventFactory.createAttribute(
				Attr.STATUS_ID.toTagName(), String.valueOf(project.getStatus().getId()));
		eventWriter.add(statusAttr);
		eventWriter.add(newLine);
		createItemNode(eventFactory, eventWriter, 
				Tag.NAME.toTagName(), project.getName(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.DESCRIPTION.toTagName(), project.getDescription(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.CUSTOMER.toTagName(), project.getCustomer(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_START_DATE.toTagName(), 
				dateToString(project.getPlanStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_DURATION.toTagName(), 
				(project.getPlanDuration() != null )? 
						String.valueOf(project.getPlanDuration()) : "", false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_START_DATE.toTagName(), 
				dateToString(project.getActStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_DURATION.toTagName(), 
				(project.getActDuration() != null)? 
						String.valueOf(project.getActDuration()) : "", false);
		eventWriter.add(newLine);
		EndElement endElement = eventFactory.createEndElement("", "", 
				Tag.PROJECT.toTagName());
		eventWriter.add(tab);
		eventWriter.add(endElement);
		eventWriter.add(newLine);
	}


	@Override
	public void findWithPagination(final Pagination<Project, Project> pagination) {
		Collection<Project> projectCol = findAll();
		if (projectCol == null || projectCol.isEmpty()) {
			return;
		}
		Comparator<Project> comp = new Comparator<Project>(){

			@Override
			public int compare(Project arg0, Project arg1) {
				if (pagination.getSidx().equals("name")) {
					return arg0.getName().compareTo(arg1.getName());
				} else if (pagination.getSidx().equals("status")) {
					return arg0.getStatus().compareTo(arg1.getStatus());
				} else if (pagination.getSidx().equals("planStart")) {
					return arg0.getPlanStart().compareTo(arg1.getPlanStart());
				} 
				return 0;
			}
			
		};
		
		List<Project> projectList = new ArrayList<Project>(projectCol);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(projectList, comp);
		} else {
			Collections.sort(projectList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(projectList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(projectList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}


	@Override
	public void findUserProjectWithPagination(final Pagination<Project, Employee> pagination) {
		Collection<Project> projectCol = teamDAO.getProjectsByEmployee(
				pagination.getOwner());
		
		if (projectCol == null || projectCol.isEmpty()) {
			return;
		}
		Comparator<Project> comp = new Comparator<Project>(){

			@Override
			public int compare(Project arg0, Project arg1) {
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
		
		List<Project> projectList = new ArrayList<Project>(projectCol);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(projectList, comp);
		} else {
			Collections.sort(projectList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(projectList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(projectList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

	
}

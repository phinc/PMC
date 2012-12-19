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
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;
import static by.phinc.pmc.util.Constants.*;

public class AssignmentXmlDAOImpl extends GenericXmlDAO<Assignment, Integer>
		implements IAssignmentDAO {
	
	private enum Tag implements ITagBase{
		ASSIGNMENTS, ASSIGNMENT, 
		SHORT_DESCRIPTION, LONG_DESCRIPTION,
		PLANNED_START_DATE, PLANNED_DURATION,
		ACTUAL_START_DATE, ACTUAL_DURATION;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID, STATUS_ID, TASK_ID, TEAM_MEMBER_ID;

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
		String filename = bundle.getString("xml.data.assignment.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	
	private IActivityDAO activityDAO;
	
	
	public AssignmentXmlDAOImpl() {
		super();
	}
	

	public IActivityDAO getActivityDAO() {
		return activityDAO;
	}

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}


	@Override
	public Assignment findById(final Integer id, boolean lock) {
		Assignment example = new Assignment();
		example.setId(id);
		Collection<Assignment> col = findByExample(example);
		return ((col != null) && !col.isEmpty()) ? col.iterator().next() : null;
	}

	
	@Override
	public List<Assignment> findAll() {
		return (new XmlStreamInProcessingTemplate<List<Assignment>>() {

			@Override
			public List<Assignment> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				List<Assignment> assignmentList = null;
				Assignment assignment = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
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
				                case ASSIGNMENTS:
				                	assignmentList = new ArrayList<Assignment>();
				                	break;
				                case ASSIGNMENT:
				                	assignment = new Assignment();
				                	
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
					                			assignment.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case STATUS_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setStatus(Status.getStatusById(tempId));
					                			break;
					                		case TASK_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setTask(proxyFactory.getTaskProxy(tempId));
					                			break;
					                		case TEAM_MEMBER_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setMember(
					                					proxyFactory.getTeamMemberProxy(tempId));
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                					DATA_FILE + "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	assignment.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	assignment.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	assignment.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	assignment.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActDuration(Float.valueOf(temp));
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
			            	if (temp.equals(Tag.ASSIGNMENT.toTagName())) {
			            		if (assignmentList == null) {
			                		throw new PMCException(
			                				"Error data file format '" + DATA_FILE + "'");
			                	}
			            		assignmentList.add(assignment);
			            	}
			            default :
			                break;
					}
				}
				return assignmentList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}
	
	
	private boolean comparesAssignments(Assignment ass, Assignment example) {
		boolean result = true;
		if (example.getId() != null) {
			result = result && example.getId().equals(ass.getId());
		}
		if (example.getActDuration() != null) {
			result = result && example.getActDuration().equals(ass.getActDuration());
		}
		if (example.getActStart() != null) {
			result = result && example.getActStart().equals(ass.getActStart());
		}
		if (example.getDescription() != null) {
			result = result && example.getDescription().equals(ass.getDescription());
		}
		if (example.getMember() != null) {
			result = result && example.getMember().equals(ass.getMember());
		}
		if (example.getName() != null) {
			result = result && example.getName().equals(ass.getName());
		}
		if (example.getPlanDuration() != null) {
			result = result && example.getPlanDuration().equals(ass.getPlanDuration());
		}
		if (example.getPlanStart() != null) {
			result = result && example.getPlanStart().equals(ass.getPlanStart());
		}
		if (example.getStatus() != null) {
			result = result && example.getStatus().equals(ass.getStatus());
		}
		if (example.getTask() != null) {
			result = result && example.getTask().equals(ass.getTask());
		}
		return result;
	}
	

	@Override
	public List<Assignment> findByExample(final Assignment example) {
		return (new XmlStreamInProcessingTemplate<List<Assignment>>() {

			@Override
			public List<Assignment> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				List<Assignment> assignmentList = new ArrayList<Assignment>();
				Assignment assignment = null;
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
				                case ASSIGNMENTS:
				                	break;
				                case ASSIGNMENT:
				                	assignment = new Assignment();
				                	
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
					                			assignment.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case TEAM_MEMBER_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setMember(
					                					proxyFactory.getTeamMemberProxy(tempId));
					                			break;
					                		case STATUS_ID:
					                			status = Status.getStatusById(
					                					Integer.valueOf(attribute.getValue()));
					                			assignment.setStatus(status);
					                			break;
					                		case TASK_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setTask(proxyFactory.getTaskProxy(tempId));
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                		DATA_FILE + "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	assignment.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	assignment.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	assignment.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	assignment.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActDuration(Float.valueOf(temp));
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
			            	//add project to list only if its task id is equal to the given id
			            	if (temp.equals(Tag.ASSIGNMENT.toTagName()) && (assignment != null) &&
			            			comparesAssignments(assignment, example)) {
			            		assignmentList.add(assignment);
			            	}
			            default :
			                break;
					}
				}
				return assignmentList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	
	@Override
	public Assignment makePersistent(Assignment assignment) {
		synchronized (this.getClass()){
			
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> assignment)
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
				                case ASSIGNMENTS:
				                	eventWriter.add(event);
				                	break;
				                case ASSIGNMENT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing assignment
				                	if ((assignment.getId() != null) && 
				                			(assignment.getId() == curId)) {
				                		createAssignmentNode(eventFactory, 
				                				eventWriter, (Assignment)assignment);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((assignment.getId() == null) || 
				                			(assignment.getId() != curId)) {
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
			            	if ((assignment.getId() == null) || 
			            			(assignment.getId() != curId)) {
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
				            	case ASSIGNMENTS:
				            		if (assignment.getId() == null) {
				            			//insert new assignment at the end of the file
				            			assignment.setId(++maxId);
					            		createAssignmentNode(eventFactory, 
					            				eventWriter, (Assignment)assignment);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating project
			            			if ((assignment.getId() == null) || 
			            					(assignment.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, assignment, readWriteLock);
			
			return assignment;
		}
	}

	@Override
	public void makeTransient(Assignment assignment) {
		synchronized (this.getClass()){
			removeActivity(assignment);
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> assignment)
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
				                case ASSIGNMENTS:
				                	eventWriter.add(event);
				                	break;
				                case ASSIGNMENT:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//omit removing assignment
				                	if (assignment.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (assignment.getId() != curId) {
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
			            	if (assignment.getId() != curId) {
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
			            	if (assignment.getId() != curId || 
			            			Tag.ASSIGNMENTS.toTagName().equals(seName)) {
					            eventWriter.add(event);
			            	}
			            	seName="";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, assignment, readWriteLock);
		}
	}
	
	
	private void removeActivity(Assignment assignment) {
		activityDAO.fillAssignmentWithActivities(assignment);
		if (assignment.getActivities() != null) {			
			for (Activity activity : assignment.getActivities()) {
				activityDAO.makeTransient(activity);
			}
		}
	}

	
	@Override
	public void fillTaskWithAssignments(final Task task) {
		new XmlStreamInProcessingTemplate<SortedSet<Assignment>>() {
				
			@Override
			public SortedSet<Assignment> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				Assignment assignment = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Status status = null;
				Integer tempId;
				Integer curTaskId = 0;
				
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
				                case ASSIGNMENTS:
				                	task.setAssignments(new TreeSet<Assignment>());
				                	break;
				                case ASSIGNMENT:
				                	assignment = new Assignment();
				                	curTaskId = 0;
				                	
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
					                			assignment.setId(
					                					Integer.valueOf(attribute.getValue()));
					                			break;
					                		case TEAM_MEMBER_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setMember(
					                					proxyFactory.getTeamMemberProxy(tempId));
					                			break;
					                		case STATUS_ID:
					                			status = Status.getStatusById(
					                					Integer.valueOf(attribute.getValue()));
					                			assignment.setStatus(status);
					                			break;
					                		case TASK_ID:
					                			curTaskId = Integer.valueOf(attribute.getValue());
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                						DATA_FILE + "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	assignment.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	assignment.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	assignment.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	assignment.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActDuration(Float.valueOf(temp));
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
			            	//add project to list only if its task id is equal to the given id
			            	if (temp.equals(Tag.ASSIGNMENT.toTagName()) && 
			            			curTaskId.equals(task.getId())) {
			            		task.addAssignment(assignment);
			            	}
			            default :
			                break;
					}
				}
				return task.getAssignments();
			}
			
		}.process(DATA_FILE, readWriteLock);
	}
	
	
	private void createAssignmentNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Assignment assignment) 
					throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.ASSIGNMENT.toTagName());
		
		eventWriter.add(startElement);
		//save attributes
		eventWriter.add(eventFactory.createAttribute(Attr.ID.toTagName(), 
				String.valueOf(assignment.getId())));
		eventWriter.add(eventFactory.createAttribute(Attr.STATUS_ID.toTagName(), 
				String.valueOf(assignment.getStatus().getId())));
		eventWriter.add(eventFactory.createAttribute(Attr.TASK_ID.toTagName(), 
				assignment.getTask().getId().toString()));
		eventWriter.add(eventFactory.createAttribute(Attr.TEAM_MEMBER_ID.toTagName(),
				assignment.getMember().getId().toString()));
		eventWriter.add(newLine);
		
		createItemNode(eventFactory, eventWriter, 
				Tag.SHORT_DESCRIPTION.toTagName(), 
				assignment.getName(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.LONG_DESCRIPTION.toTagName(), 
				assignment.getDescription(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_START_DATE.toTagName(), 
				dateToString(assignment.getPlanStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.PLANNED_DURATION.toTagName(), 
				(assignment.getPlanDuration() != null) ? 
						String.valueOf(assignment.getPlanDuration()) : "", false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_START_DATE.toTagName(), 
				dateToString(assignment.getActStart()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.ACTUAL_DURATION.toTagName(), 
				(assignment.getActDuration() != null) ? 
						String.valueOf(assignment.getActDuration()) : "", false);
		
		eventWriter.add(tab);
		eventWriter.add(eventFactory.createEndElement("", "", Tag.ASSIGNMENT.toTagName()));
		eventWriter.add(newLine);
	}

	
	private Collection<Assignment> getAssignmentsByEmployee(final Employee employee) {
		return (new XmlStreamInProcessingTemplate<SortedSet<Assignment>>() {

			@Override
			public SortedSet<Assignment> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				SortedSet<Assignment> assignmentList = null;
				Assignment assignment = null;
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
				                case ASSIGNMENTS:
				                	assignmentList = new TreeSet<Assignment>();
				                	break;
				                case ASSIGNMENT:
				                	assignment = new Assignment();
				                	
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
					                			assignment.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case TEAM_MEMBER_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setMember(
					                					proxyFactory.getTeamMemberProxy(tempId));
					                			break;
					                		case STATUS_ID:
					                			status = Status.getStatusById(
					                					Integer.valueOf(attribute.getValue()));
					                			assignment.setStatus(status);
					                			break;
					                		case TASK_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			assignment.setTask(proxyFactory.getTaskProxy(tempId));
					                			break;
					                		default:
					                			throw new PMCException("Error data file format '" + 
					                		DATA_FILE + "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case SHORT_DESCRIPTION:
				                	assignment.setName(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case LONG_DESCRIPTION:
				                	assignment.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case PLANNED_START_DATE:
				                	assignment.setPlanStart(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case PLANNED_DURATION:
				                	assignment.setPlanDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case ACTUAL_START_DATE:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActStart(dateFormat.parse(temp));
				                	}
				                	break;
				                case ACTUAL_DURATION:
				                	temp = getElementText(eventReader, nextEvent);
				                	if (!temp.isEmpty()) {
				                		assignment.setActDuration(Float.valueOf(temp));
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
			            	//add project to list only if its task id is equal to the given id
			            	if (temp.equals(Tag.ASSIGNMENT.toTagName()) && 
			            			employee.getId().equals(
			            				assignment.getMember().getEmployee().getId())) {
			            		if (assignmentList == null) {
			                		throw new PMCException(
			                			"Error data file format '" + DATA_FILE + "'");
			                	}
			            		assignmentList.add(assignment);
			            	}
			            default :
			                break;
					}
				}
				return assignmentList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	@Override
	public void findWithPagination(final Pagination<Assignment, Task> pagination) {
		Task task = pagination.getOwner();
		fillTaskWithAssignments(task);
		
		if (task.getAssignments() == null || task.getAssignments().isEmpty()) {
			return;
		}
		Comparator<Assignment> comp = new Comparator<Assignment>(){

			@Override
			public int compare(Assignment arg0, Assignment arg1) {
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
		
		List<Assignment> assignmentList = new ArrayList<Assignment>(
				task.getAssignments());
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(assignmentList, comp);
		} else {
			Collections.sort(assignmentList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(assignmentList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(assignmentList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

	
	@Override
	public void findUserAssignmentWithPagination(
			final Pagination<Assignment, Employee> pagination) {
		Collection<Assignment> assCol = getAssignmentsByEmployee(pagination.getOwner());
		
		if (assCol == null || assCol.isEmpty()) {
			return;
		}
		Comparator<Assignment> comp = new Comparator<Assignment>(){

			@Override
			public int compare(Assignment arg0, Assignment arg1) {
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
		
		List<Assignment> assignmentList = new ArrayList<Assignment>(assCol);
		if ("asc".equals(pagination.getSord())) {
			Collections.sort(assignmentList, comp);
		} else {
			Collections.sort(assignmentList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(assignmentList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(assignmentList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

}

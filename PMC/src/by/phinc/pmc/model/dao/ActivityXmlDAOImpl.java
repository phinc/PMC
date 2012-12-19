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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.TimeJournalBean;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;
import static by.phinc.pmc.util.Constants.*;

public class ActivityXmlDAOImpl extends GenericXmlDAO<Activity, Integer>
		implements IActivityDAO {
	
	private enum Tag implements ITagBase{
		ACTIVITIES, ACTIVITY, DESCRIPTION,
		START_DATE, DURATION, REPORTER;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID, ASSIGNMENT_ID, EMPLOYEE_ID;

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
		String filename = bundle.getString("xml.data.activity.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	
	
	public ActivityXmlDAOImpl() {
		super();
	}


	@Override
	public Activity findById(final Integer id, boolean lock) {
		return (new XmlStreamInProcessingTemplate<Activity>() {

			@Override
			public Activity doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				Activity activity = null;
				String seName = "";
				String attrName = "";
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
				                case ACTIVITIES:
				                	break;
				                case ACTIVITY:
				                	activity = new Activity();
				                	
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
					                			activity.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case ASSIGNMENT_ID:
					                			tempId = Integer.valueOf(
					                					attribute.getValue());
					                			activity.setAssignment(
					                					proxyFactory.getAssignmenmtProxy(tempId));
					                			break;
					                		default:
					                			throw new PMCException(
					                				"Error data file format '" + DATA_FILE +
					                      			"' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case DESCRIPTION:
				                	activity.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case START_DATE:
				                	activity.setStartDate(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case DURATION:
				                	activity.setDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case REPORTER:
				                	attribute = se.getAttributeByName(
				                			new QName(Attr.EMPLOYEE_ID.toTagName()));
				                	tempId = Integer.valueOf(attribute.getValue());
				                	activity.setReporter(proxyFactory.getEmployeeProxy(tempId));
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + 
				               				DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	if (Tag.ACTIVITY.toTagName().equals(seName) 
			            			&& activity != null 
			            			&& id.equals(activity.getId())) {
			            		return activity;
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
	public Collection<Activity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Activity> findByExample(Activity exampleInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Activity makePersistent(Activity activity) {
		synchronized (this.getClass()){
			
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> activity)
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
				                case ACTIVITIES:
				                	eventWriter.add(event);
				                	break;
				                case ACTIVITY:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing activity
				                	if ((activity.getId() != null) && 
				                			(activity.getId() == curId)) {
				                		createActivityNode(eventFactory, 
				                				eventWriter, (Activity)activity);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((activity.getId() == null) || 
				                			(activity.getId() != curId)) {
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
			            	if ((activity.getId() == null) || 
			            			(activity.getId() != curId)) {
				            	//insert cdata section for elements: description
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case DESCRIPTION:
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
				            	case ACTIVITIES:
				            		if (activity.getId() == null) {
				            			//insert new activity at the end of the file
				            			activity.setId(++maxId);
					            		createActivityNode(eventFactory, 
					            				eventWriter, (Activity)activity);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating
			            			if ((activity.getId() == null) || 
			            					(activity.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, activity, readWriteLock);
			
			return activity;
		}
	}
	
	private void createActivityNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Activity activity) 
					throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.ACTIVITY.toTagName());
		
		eventWriter.add(startElement);
		//save attributes
		eventWriter.add(eventFactory.createAttribute(Attr.ID.toTagName(), 
				String.valueOf(activity.getId())));
		eventWriter.add(eventFactory.createAttribute(Attr.ASSIGNMENT_ID.toTagName(), 
				activity.getAssignment().getId().toString()));
		eventWriter.add(newLine);
		
		createItemNode(eventFactory, eventWriter, 
				Tag.DESCRIPTION.toTagName(), 
				activity.getDescription(), true);
		createItemNode(eventFactory, eventWriter, 
				Tag.START_DATE.toTagName(), 
				dateToString(activity.getStartDate()), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.DURATION.toTagName(), 
				(activity.getDuration() != null) ? 
						String.valueOf(activity.getDuration()) : "", false);
		
		eventWriter.add(tab);
		eventWriter.add(eventFactory.createStartElement("", "", 
				Tag.REPORTER.toTagName()));
		eventWriter.add(eventFactory.createAttribute(Attr.EMPLOYEE_ID.toTagName(), 
				activity.getReporter().getId().toString()));		
		eventWriter.add(eventFactory.createEndElement("", "", 
				Tag.REPORTER.toTagName()));
		eventWriter.add(newLine);
		
		eventWriter.add(tab);
		eventWriter.add(eventFactory.createEndElement("", "", 
				Tag.ACTIVITY.toTagName()));
		eventWriter.add(newLine);
	}

	@Override
	public void makeTransient(Activity activity) {
		synchronized (this.getClass()){
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> activity)
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
				                case ACTIVITIES:
				                	eventWriter.add(event);
				                	break;
				                case ACTIVITY:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//omit removing assignment
				                	if (activity.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (activity.getId() != curId) {
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
			            	if (activity.getId() != curId) {
				            	//insert cdata section for elements
				            	if (!seName.isEmpty()) {
					            	tag = Tag.valueOf(toEnumName(seName));
					                switch (tag){
						                case DESCRIPTION:
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
			            	if (activity.getId() != curId || 
			            			Tag.ACTIVITIES.toTagName().equals(seName)) {
					            eventWriter.add(event);
			            	}
			            	seName="";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, activity, readWriteLock);
		}
	}

	
	@Override
	public void fillAssignmentWithActivities(final Assignment assignment) {
		new XmlStreamInProcessingTemplate<SortedSet<Activity>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public SortedSet<Activity> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				Activity activity = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Integer tempId;
				Integer curAssignmentId = 0;
				Attribute attribute;
            	Attr attr;
            	Iterator<Attribute> attributes;
				
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
				                case ACTIVITIES:
				                	assignment.setActivities(new TreeSet<Activity>());
				                	break;
				                case ACTIVITY:
				                	activity = new Activity();
				                	curAssignmentId = 0;
				                	
									attributes = se.getAttributes();				                	
				                	while (attributes.hasNext()) {
				                		attribute = attributes.next();
				                		attrName = attribute.getName().getLocalPart();
				                		attr = Attr.valueOf(toEnumName(attrName));
				                		switch (attr) {
					                		case ID:
					                			activity.setId(Integer.valueOf(
					                					attribute.getValue()));
					                			break;
					                		case ASSIGNMENT_ID:
					                			curAssignmentId = Integer.valueOf(
					                					attribute.getValue());
					                			break;
					                		default:
					                			throw new PMCException(
					                				"Error data file format '" + DATA_FILE +
					                      			"' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case DESCRIPTION:
				                	activity.setDescription(
				                			getElementText(eventReader, nextEvent));
				                	break;
				                case START_DATE:
				                	activity.setStartDate(dateFormat.parse(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case DURATION:
				                	activity.setDuration(Float.valueOf(
				                			getElementText(eventReader, nextEvent)));
				                	break;
				                case REPORTER:
				                	attribute = se.getAttributeByName(
				                			new QName(Attr.EMPLOYEE_ID.toTagName()));
				                	tempId = Integer.valueOf(attribute.getValue());
				                	activity.setReporter(proxyFactory.getEmployeeProxy(tempId));
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + 
				               					DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	//add activity to the list only if its assignment id is equal to the given id
			            	if (temp.equals(Tag.ACTIVITY.toTagName()) && 
			            			curAssignmentId.equals(assignment.getId())) {
			            		assignment.addActivity(activity);
			            	}
			            default :
			                break;
					}
				}
				return assignment.getActivities();
			}
			
		}.process(DATA_FILE, readWriteLock);
	}

	@Override
	public void findWithPagination(final Pagination<Activity, Assignment> pagination) {
		Assignment assignment = pagination.getOwner();
		fillAssignmentWithActivities(assignment);
		
		if (assignment.getActivities() == null || 
				assignment.getActivities().isEmpty()) {
			return;
		}
		Comparator<Activity> comp = new Comparator<Activity>(){

			@Override
			public int compare(Activity arg0, Activity arg1) {
				if (pagination.getSidx().equals("description")) {
					return arg0.getDescription().compareTo(arg1.getDescription());
				} else if (pagination.getSidx().equals("startDate")) {
					return arg0.getStartDate().compareTo(arg1.getStartDate());
				} else if (pagination.getSidx().equals("reporter")) {
					return arg0.getReporter().getName().compareTo(
							arg1.getReporter().getName());
				} 
				return 0;
			}
			
		};
		
		List<Activity> activityList = new ArrayList<Activity>(
				assignment.getActivities());
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(activityList, comp);
		} else {
			Collections.sort(activityList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(activityList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(activityList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}

	@Override
	public Set<TimeJournalBean> findUserActivityByDate(Date date, Employee employee) {
		return null;
		// TODO Auto-generated method stub
		
	}

}

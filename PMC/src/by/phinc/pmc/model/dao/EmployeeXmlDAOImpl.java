package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.*;
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
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;

public class EmployeeXmlDAOImpl extends GenericXmlDAO<Employee, Integer> 
		implements IEmployeeDAO {
	
	private static final String DATA_FILE;
	
	private static final String TEMP_FILE;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);
		String path = bundle.getString("xml.data.path");
		String filename = bundle.getString("xml.data.employee.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private enum Tag implements ITagBase{
		EMPLOYEES, EMPLOYEE, FIRST_NAME,
		LAST_NAME, EMAIL, POST,
		LOGIN, PASSWORD;

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
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	

	public EmployeeXmlDAOImpl() {
		super();
	}

	
	private void createEmployeeNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, Employee employee) 
					throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.EMPLOYEE.toTagName());
		eventWriter.add(startElement);
		Attribute idAttr = eventFactory.createAttribute(
				Attr.ID.toTagName(), String.valueOf(employee.getId()));
		eventWriter.add(idAttr);
		eventWriter.add(newLine);
		createItemNode(eventFactory, eventWriter, 
				Tag.FIRST_NAME.toTagName(), employee.getFirstName(), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.LAST_NAME.toTagName(), employee.getLastName(), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.EMAIL.toTagName(), employee.getEmail(), false);
		createItemNode(eventFactory, eventWriter,  
				Tag.POST.toTagName(), employee.getPost(), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.LOGIN.toTagName(), employee.getLogin(), false);
		createItemNode(eventFactory, eventWriter, 
				Tag.PASSWORD.toTagName(), employee.getPassword(), true);
		EndElement endElement = eventFactory.createEndElement("", "", 
				Tag.EMPLOYEE.toTagName());
		eventWriter.add(tab);
		eventWriter.add(endElement);
		eventWriter.add(newLine);
	}


	@Override
	public Employee findById(final Integer id, boolean lock) {
		Employee example = new Employee();
		example.setId(id);
		Collection<Employee> col = findByExample(example);
		return ((col != null) && (!col.isEmpty())) ? col.iterator().next() : null;
	}


	@Override
	public List<Employee> findAll() {
		return (new XmlStreamInProcessingTemplate<List<Employee>>() {

			@Override
			public List<Employee> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {		
				XMLEvent event;
				XMLEvent nextEvent;
				List<Employee> employeeList = null;
				Employee employee = null;
				String seName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Tag tag;
				Attribute attribute;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
				    nextEvent = eventReader.peek();
				    
				    seName = "";
				    
				    switch (event.getEventType()) {
		            case START_ELEMENT :
		            	se = event.asStartElement();
		                seName = se.getName().getLocalPart();
		                
		                tag = Tag.valueOf(toEnumName(seName));
		                switch (tag){
		                case EMPLOYEES:
		                	employeeList = new ArrayList<Employee>();
		                	break;
		                case EMPLOYEE:
		                	employee = new Employee();
		                	
		                	attribute = se.getAttributeByName(new QName(Attr.ID.toTagName()));
		                	employee.setId(Integer.valueOf(attribute.getValue()));
		                	break;
		                case FIRST_NAME:
		                	employee.setFirstName(getElementText(eventReader, nextEvent));
		                	break;
		                case LAST_NAME:
		                	employee.setLastName(getElementText(eventReader, nextEvent));
		                	break;
		                case EMAIL:
		                	employee.setEmail(getElementText(eventReader, nextEvent));
		                	break;
		                case POST:
		                	employee.setPost(getElementText(eventReader, nextEvent));
		                	break;
		                case LOGIN:
		                	employee.setLogin(getElementText(eventReader, nextEvent));
		                	break;
		                case PASSWORD:
		                	employee.setPassword(getElementText(eventReader, nextEvent));
		                	break;
		               	default:
		               		throw new PMCException("Error data file format '" + DATA_FILE +
		               				 "' unknown tag " + seName);
		                }
		                break;            
		            case END_ELEMENT:
		            	ee = event.asEndElement();
		            	temp = ee.getName().getLocalPart();
		            	if (temp.equals(Tag.EMPLOYEE.toTagName())) {
		            		if (employeeList == null) {
		                		throw new PMCException("Error data file format '" +
		                								DATA_FILE + "'");
		                	}
		                	employeeList.add(employee);
		            	}
		            	break;
		            default :
		                break;
				    }
				}
				return employeeList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}
	
	
	private boolean compareEmployee(Employee empl, Employee example) {
		boolean result = true;
		if (example.getId() != null) {
			result = result && example.getId().equals(empl.getId());
		}
		if (example.getFirstName() != null) {
			result = result && example.getFirstName().equals(empl.getFirstName());
		}
		if (example.getLastName() != null) {
			result = result && example.getLastName().equals(empl.getLastName());
		}
		if (example.getEmail() != null) {
			result = result && example.getEmail().equals(empl.getEmail());
		}
		if (example.getLogin() != null) {
			result = result && example.getLogin().equals(empl.getLogin());
		}
		if (example.getPassword() != null) {
			result = result && example.getPassword().equals(empl.getPassword());
		}
		if (example.getPost() != null) {
			result = result && example.getPost().equals(empl.getPost());
		}
		return result;
	}


	@Override
	public List<Employee> findByExample(final Employee example) {
		if (example == null) {
			return null;
		}
		
		return (new XmlStreamInProcessingTemplate<List<Employee>>() {

			@Override
			public List<Employee> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {		
				XMLEvent event;
				XMLEvent nextEvent;
				List<Employee> employeeList = new ArrayList<Employee>();
				Employee employee = null;
				String seName = "";
				StartElement se;
				EndElement ee;
				Tag tag;
				Attribute attribute;
				
				while (eventReader.hasNext()){
					event = eventReader.nextEvent();
				    nextEvent = eventReader.peek();
					
				    if (event.isStartElement()) {
						se = event.asStartElement();
			            seName = se.getName().getLocalPart();
						tag = Tag.valueOf(toEnumName(seName));
			            switch (tag){
				            case EMPLOYEES:
			                	break;
			                case EMPLOYEE:
			                	employee = new Employee();
			                	se = event.asStartElement();
			            		attribute = se.getAttributeByName(new QName(
			            				Attr.ID.toTagName()));
			                	employee.setId(Integer.valueOf(attribute.getValue()));
			                	break;    
				            case FIRST_NAME:
				            	employee.setFirstName(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            case LAST_NAME:
				            	employee.setLastName(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            case EMAIL:
				            	employee.setEmail(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            case POST:
				            	employee.setPost(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            case LOGIN:
				            	employee.setLogin(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            case PASSWORD:
				            	employee.setPassword(
				            			getElementText(eventReader, nextEvent));
				            	break;
				            
			               	default:
			               		throw new PMCException("Error data file format '" + 
			               				DATA_FILE + "' unknown tag " + seName);
			            }
				    } else if (event.isEndElement()) {
				    	ee = event.asEndElement();
		            	seName = ee.getName().getLocalPart();
		            	if (Tag.EMPLOYEE.toTagName().equals(seName) &&
		            			employee != null && compareEmployee(employee, example)) {
	                		employeeList.add(employee);
	                	}
				    }
				}			
				return employeeList;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}


	@Override
	public Employee makePersistent(Employee employee) {		
		synchronized (this.getClass()){
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> employee)
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
				                	case EMPLOYEES:
				                		eventWriter.add(event);
				                		break;
				                	case EMPLOYEE:
					                	Attribute attribute = se.getAttributeByName(
					                			new QName(Attr.ID.toTagName()));
					                	curId = Integer.valueOf(attribute.getValue());
					                	if (curId > maxId) {
					                		maxId = curId;
					                	} 
					                	if (employee.getId() != null && 
					                			employee.getId() == curId) {
					                		createEmployeeNode(eventFactory, eventWriter, (Employee)employee);
					                	} else {
					                		eventWriter.add(event);
					                	}
					                	break;
					                default:
					                	if ((employee.getId() == null) || (
					                			employee.getId() != curId)) {
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
				            	if ((employee.getId() == null) || 
				            			(employee.getId() != curId)) {
					            	//insert cdata section for elements name, description and customer
					            	if (Tag.PASSWORD.toTagName().equals(seName)){
							            characters = eventFactory.createCData(
							            		event.asCharacters().getData());
							            eventWriter.add(characters);
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
					            	case EMPLOYEES:
					            		if (employee.getId() == null) {
					            			//insert new employee at the end of the file
					            			employee.setId(++maxId);
						            		createEmployeeNode(eventFactory, 
						            				eventWriter, (Employee)employee);
						            	}
					            		eventWriter.add(event);
					            		break;
				            		default:
				            			//skip writing if current event is updating employee
				            			if ((employee.getId() == null) || 
				            					(employee.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, employee, readWriteLock);
		
			return employee;
		}
	}


	@Override
	public void makeTransient(Employee employee) {
		synchronized (this.getClass()){
			
				new XmlStreamProcessingTemplate() {
					
					@Override
					public void doProcess(XMLEventWriter eventWriter, 
							XMLEventReader eventReader, IModel<Integer> employee)
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
					                case EMPLOYEES:
					                	eventWriter.add(event);
					                	break;
					                case EMPLOYEE:
					                	Attribute attribute = se.getAttributeByName(
					                			new QName(Attr.ID.toTagName()));
					                	curId = Integer.valueOf(attribute.getValue());
					                	//omit removing employee
					                	if (employee.getId() != curId) {
					                		eventWriter.add(event);
					                	}
					                	break;
					                default:
					                	if (employee.getId() != curId) {
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
				            	if (employee.getId() != curId) {
					            	//insert cdata section for elements
				            		if (Tag.PASSWORD.toTagName().equals(seName)){
							            characters = eventFactory.createCData(
							            		event.asCharacters().getData());
							            eventWriter.add(characters);
						            } else {
							            eventWriter.add(event);
						            }
				            	}
				            	break;
				            case END_ELEMENT:
				            	ee = event.asEndElement();
				            	seName = ee.getName().getLocalPart();
				            	if (employee.getId() != curId || 
				            			Tag.EMPLOYEES.toTagName().equals(seName)) {
						            eventWriter.add(event);
				            	}
				            	seName="";
				            	break;
				            default :
				            	break;
						    }
						}
					}
			}.process(DATA_FILE, TEMP_FILE, employee, readWriteLock);
		}
	}


	@Override
	public void findWithPagination(final Pagination<Employee, Employee> pagination) {
		Collection<Employee> employeeCol = findAll();
		if (employeeCol == null || employeeCol.isEmpty()) {
			return;
		}
		Comparator<Employee> comp = new Comparator<Employee>(){

			@Override
			public int compare(Employee arg0, Employee arg1) {
				if (pagination.getSidx().equals("firstName")) {
					return arg0.getFirstName().compareTo(arg1.getFirstName());
				} else if (pagination.getSidx().equals("lastName")) {
					return arg0.getLastName().compareTo(arg1.getLastName());
				} else if (pagination.getSidx().equals("post")) {
					return arg0.getPost().compareTo(arg1.getPost());
				} 
				return 0;
			}
			
		};
		
		List<Employee> employeeList = new ArrayList<Employee>(employeeCol);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(employeeList, comp);
		} else {
			Collections.sort(employeeList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(employeeList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(employeeList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
							? fromIndex + pagination.getRows() 
							: pagination.getRecords()));
	}

}

package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;
import static by.phinc.pmc.util.Constants.SORT_DIR_ASC;
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
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.util.ITagBase;
import by.phinc.pmc.util.WriteLockHelper;
import by.phinc.pmc.util.XmlStreamInProcessingTemplate;
import by.phinc.pmc.util.XmlStreamProcessingTemplate;

public class TeamXmlDAOImpl extends GenericXmlDAO<TeamMember, Integer> 
							implements ITeamDAO {
	
	private enum Tag implements ITagBase{
		TEAMS, TEAM_MEMBER, ROLE;

		@Override
		public String toTagName() {
			return name().toLowerCase().replace(ACC_SIGN, DASH_SIGN);
		}
	}
	
	private enum Attr implements ITagBase{
		ID, PROJECT_ID, EMPLOYEE_ID;

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
		String filename = bundle.getString("xml.data.team.file");
		DATA_FILE = path + File.separator + filename;
		TEMP_FILE = path + File.separator + ACC_SIGN + filename;
	}
	
	private final ReadWriteLock readWriteLock = 
			WriteLockHelper.getInstance().getReadWriteLock(DATA_FILE);
	

	@Override
	public TeamMember findById(final Integer id, boolean lock) {
		TeamMember example = new TeamMember();
		example.setId(id);
		Collection<TeamMember> team = findByExample(example);
		return ((team != null) && !team.isEmpty()) ? team.iterator().next() : null;
	}

	
	@Override
	public List<TeamMember> findAll() {
		return (new XmlStreamInProcessingTemplate<List<TeamMember>>() {

			@Override
			public List<TeamMember> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				List<TeamMember> team = null;
				TeamMember member = null;
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
				                case TEAMS:
				                	team = new ArrayList<TeamMember>();
				                	break;
				                case TEAM_MEMBER:
				                	member = new TeamMember();
				                	
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
					                			member.setId(Integer.valueOf(attribute.getValue()));
					                			break;
					                		case PROJECT_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			member.setProject(proxyFactory.getProjectProxy(tempId));
					                			break;
					                		case EMPLOYEE_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
							                	member.setEmployee(proxyFactory.getEmployeeProxy(tempId));
							                	break;
					                		default:
					                			throw new PMCException("Error data file format '" + DATA_FILE +
					                      				 "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case ROLE:
				                	member.setRole(getElementText(eventReader, nextEvent));
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + 
				               				DATA_FILE + "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	if (temp.equals(Tag.TEAM_MEMBER.toTagName())) {
			            		if (team == null) {
			                		throw new PMCException("Error data file format '" + 
			                				DATA_FILE + "'");
			                	}
			                	team.add(member);
			            	}
			            default :
			                break;
					}
				}
				return team;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}
	
	
	private boolean compareTeamMembers(TeamMember member, TeamMember example) {
		boolean result = true;
		if (example.getId() != null) {
			result = result && example.getId().equals(member.getId());
		}
		if (example.getEmployee() != null) {
			result = result && example.getEmployee().equals(member.getEmployee());
		}
		if (example.getRole() != null) {
			result = result && example.getRole().equals(member.getRole());
		}
		if (example.getProject() != null) {
			result = result && example.getProject().equals(member.getProject());
		}
		return result;
	}

	
	@Override
	public List<TeamMember> findByExample(final TeamMember example) {		
		if (example == null) {
			return null;
		}
		
		return (new XmlStreamInProcessingTemplate<List<TeamMember>>() {

			@Override
			public List<TeamMember> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				TeamMember member = null;
				List<TeamMember> team = new ArrayList<TeamMember>();
				XMLEvent event;
				XMLEvent nextEvent;
				String seName = "";
				String attrName = "";
				StartElement se;
				EndElement ee;
				Integer tempId;
				Tag tag;
				Attribute attribute;
            	Attr attr;
				
				while(eventReader.hasNext()){
					event = eventReader.nextEvent();
				    nextEvent = eventReader.peek();
				    
				    seName = "";
				    attrName = "";
				    
				    switch (event.getEventType()) {
		            case START_ELEMENT :
		            	se = event.asStartElement();
		                seName = se.getName().getLocalPart();
		                
		                tag = Tag.valueOf(toEnumName(seName));
		                switch (tag){
		                case TEAMS:
		                	break;
		                case TEAM_MEMBER:
		                	member = new TeamMember();
		                	
		                	@SuppressWarnings("unchecked")
							Iterator<Attribute> attributes = se.getAttributes();		                	
		                	while (attributes.hasNext()) {
		                		attribute = attributes.next();
		                		attrName = attribute.getName().getLocalPart();
		                		attr = Attr.valueOf(toEnumName(attrName));
		                		switch (attr) {
			                		case ID:
			                			member.setId(Integer.valueOf(attribute.getValue()));
			                			break;
			                		case PROJECT_ID:
			                			tempId = Integer.valueOf(attribute.getValue());
			                			member.setProject(proxyFactory.getProjectProxy(tempId));
			                			break;
			                		case EMPLOYEE_ID:		                	
					                	tempId = Integer.valueOf(attribute.getValue());
					                	member.setEmployee(proxyFactory.getEmployeeProxy(tempId));
					                	break;
			                		default:
			                			throw new PMCException("Error data file format '" + DATA_FILE +
			                      				 "' unknown attribute " + attrName);
		                		}
		                	}
		                	break;
		                case ROLE:
		                	member.setRole(getElementText(eventReader, nextEvent));
		                	break;
		                default:
		               		throw new PMCException("Error data file format '" + DATA_FILE +
		               				 "' unknown tag " + seName);
		                }
		                break;            
		            case END_ELEMENT:
		            	ee = event.asEndElement();
		            	seName = ee.getName().getLocalPart();
		            	if (Tag.TEAM_MEMBER.toTagName().equals(seName) && (member != null) 
		            			&& compareTeamMembers(member, example)) {
		            		team.add(member);
		            	}
		            default :
		                break;
				    }
				}
				return team;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	
	
	@Override
	public TeamMember makePersistent(TeamMember member) {
		synchronized (this.getClass()){
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, 
						XMLEventReader eventReader, IModel<Integer> member)
						throws IOException, XMLStreamException {
					
					XMLEvent event;
					String seName = "";
					StartElement se;
					EndElement ee;
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
				                case TEAMS:
				                	eventWriter.add(event);
				                	break;
				                case TEAM_MEMBER:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	if (curId > maxId) {
				                		maxId = curId;
				                	}
				                	//update existing team member
				                	if ((member.getId() != null) && (member.getId() == curId)) {
				                		createMemberNode(eventFactory, eventWriter, (TeamMember)member);
				                	} else {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if ((member.getId() == null) || (member.getId() != curId)) {
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
			            	if ((member.getId() == null) || (member.getId() != curId)) {
				            	eventWriter.add(event);
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	tag = Tag.valueOf(toEnumName(seName));
			            	switch (tag) {
				            	case TEAMS:
				            		if (member.getId() == null) {
				            			//insert new project at the end of the file
				            			member.setId(++maxId);
					            		createMemberNode(eventFactory, eventWriter, (TeamMember)member);
					            	}
				            		eventWriter.add(event);
				            		break;
			            		default:
			            			//skip writing if current event is updating project
			            			if ((member.getId() == null) || (member.getId() != curId)) {
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
			}.process(DATA_FILE, TEMP_FILE, member, readWriteLock);
		
			return member;
		}
	}

	@Override
	public void makeTransient(TeamMember member) {
		synchronized (this.getClass()){
		
			new XmlStreamProcessingTemplate() {
				
				@Override
				public void doProcess(XMLEventWriter eventWriter, XMLEventReader eventReader, IModel<Integer> member)
						throws IOException, XMLStreamException {
					
					XMLEvent event;
					String seName = "";
					StartElement se;
					EndElement ee;
					int curId = -1;
					
					while(eventReader.hasNext()){
						event = eventReader.nextEvent();
					    
					    switch (event.getEventType()) {
			            case START_ELEMENT :
			            	se = event.asStartElement();
			                seName = se.getName().getLocalPart();
			                
			                Tag tag = Tag.valueOf(toEnumName(seName));
			                switch (tag){
				                case TEAMS:
				                	eventWriter.add(event);
				                	break;
				                case TEAM_MEMBER:
				                	Attribute attribute = se.getAttributeByName(
				                			new QName(Attr.ID.toTagName()));
				                	curId = Integer.valueOf(attribute.getValue());
				                	//update existing team member
				                	if (member.getId() != curId) {
				                		eventWriter.add(event);
				                	}
				                	break;
				                default:
				                	if (member.getId() != curId) {
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
			            	if (member.getId() != curId) {
				            	eventWriter.add(event);
						        break;
			            	}
			            	break;
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	seName = ee.getName().getLocalPart();
			            	if (Tag.TEAMS.toTagName().equals(seName) || member.getId() != curId) {
			            		eventWriter.add(event);	
			            	}
			            	seName = "";
			            	break;
			            default :
			            	break;
					    }
					}
				}
			}.process(DATA_FILE, TEMP_FILE, member, readWriteLock);
		
		}
	}

	
	private void createMemberNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, TeamMember member) throws XMLStreamException {
		
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);
		XMLEvent tab = eventFactory.createDTD(TAB);
		
		StartElement startElement = eventFactory.createStartElement(
				"", "", Tag.TEAM_MEMBER.toTagName());
		eventWriter.add(startElement);
		Attribute idAttr = eventFactory.createAttribute(
				Attr.ID.toTagName(), String.valueOf(member.getId()));
		eventWriter.add(idAttr);
		Attribute projectIdAttr = eventFactory.createAttribute(
				Attr.PROJECT_ID.toTagName(), 
				String.valueOf(member.getProject().getId()));
		eventWriter.add(projectIdAttr);
		Attribute employeeIdAttr = eventFactory.createAttribute(
				Attr.EMPLOYEE_ID.toTagName(), 
				String.valueOf(member.getEmployee().getId()));
		eventWriter.add(employeeIdAttr);
		eventWriter.add(newLine);
		
		createItemNode(eventFactory, eventWriter, 
				Tag.ROLE.toTagName(), member.getRole(), false);
		
		EndElement endElement = eventFactory.createEndElement("", "", 
				Tag.TEAM_MEMBER.toTagName());
		eventWriter.add(tab);
		eventWriter.add(endElement);
		eventWriter.add(newLine);
	}

	
	@Override
	public void fillProjectWithTeam(final Project project) {
		new XmlStreamInProcessingTemplate<SortedSet<TeamMember>>() {
			
			@Override
			public SortedSet<TeamMember> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				TeamMember member = null;
				String seName = "";
				String attrName = "";
				String temp = "";
				StartElement se;
				EndElement ee;
				Integer curProjectId = 0;
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
				                case TEAMS:
				                	project.setTeam(new TreeSet<TeamMember>());
				                	break;
				                case TEAM_MEMBER:
				                	member = new TeamMember();
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
					                			member.setId(Integer.valueOf(attribute.getValue()));
					                			break;
					                		case PROJECT_ID:
					                			curProjectId = Integer.valueOf(attribute.getValue());
					                			break;
					                		case EMPLOYEE_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
							                	member.setEmployee(proxyFactory.getEmployeeProxy(tempId));
							                	break;
					                		default:
					                			throw new PMCException("Error data file format '" + DATA_FILE +
					                      				 "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case ROLE:
				                	member.setRole(getElementText(eventReader, nextEvent));
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + DATA_FILE +
				               				 "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	if (temp.equals(Tag.TEAM_MEMBER.toTagName()) && 
			            			curProjectId.equals(project.getId())) {
			            		project.addTeamMember(member);
			            	}
			            default :
			                break;
					}
				}
				return project.getTeam();
			}
			
		}.process(DATA_FILE, readWriteLock);
	}

	@Override
	public Collection<Project> getProjectsByEmployee(final Employee employee) {
		return (new XmlStreamInProcessingTemplate<Collection<Project>>() {
			
			@Override
			public Collection<Project> doProcess(XMLEventReader eventReader)
					throws IOException, XMLStreamException, ParseException {
				XMLEvent event;
				XMLEvent nextEvent;
				SortedSet<Project> projects = null;
				TeamMember member = null;
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
				                case TEAMS:
				                	projects = new TreeSet<Project>();
				                	break;
				                case TEAM_MEMBER:
				                	member = new TeamMember();
				                	
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
					                			member.setId(Integer.valueOf(attribute.getValue()));
					                			break;
					                		case PROJECT_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
					                			member.setProject(proxyFactory.getProjectProxy(tempId));
					                			break;
					                		case EMPLOYEE_ID:
					                			tempId = Integer.valueOf(attribute.getValue());
							                	member.setEmployee(proxyFactory.getEmployeeProxy(tempId));
							                	break;
					                		default:
					                			throw new PMCException("Error data file format '" + DATA_FILE +
					                      				 "' unknown attribute " + attrName);
				                		}
				                	}
				                	break;
				                case ROLE:
				                	member.setRole(getElementText(eventReader, nextEvent));
				                	break;
				                default:
				               		throw new PMCException("Error data file format '" + DATA_FILE +
				               				 "' unknown tag " + seName);
			                }
			                break;            
			            case END_ELEMENT:
			            	ee = event.asEndElement();
			            	temp = ee.getName().getLocalPart();
			            	if (temp.equals(Tag.TEAM_MEMBER.toTagName()) 
			            			&& employee.getId().equals(member.getEmployee().getId())) {
			            		projects.add(member.getProject());
			            	}
			            default :
			                break;
					}
				}
				return projects;
			}
			
		}).process(DATA_FILE, readWriteLock);
	}

	@Override
	public void findWithPagination(final Pagination<TeamMember, Project> pagination) {
		Project project = pagination.getOwner();
		fillProjectWithTeam(project);

		if (project.getTeam() == null || project.getTeam().size() == 0) {
			return;
		}
		Comparator<TeamMember> comp = new Comparator<TeamMember>(){

			@Override
			public int compare(TeamMember arg0, TeamMember arg1) {
				if (pagination.getSidx().equals("firstName")) {
					return arg0.getEmployee().getFirstName().compareTo(
							arg1.getEmployee().getFirstName());
				} else if (pagination.getSidx().equals("lastName")) {
					return arg0.getEmployee().getLastName().compareTo(
							arg1.getEmployee().getLastName());
				} else if (pagination.getSidx().equals("post")) {
					return arg0.getEmployee().getPost().compareTo(
							arg1.getEmployee().getPost());
				} else if (pagination.getSidx().equals("role")) {
					return arg0.getRole().compareTo(arg1.getRole());
				}
				return 0;
			}
			
		};
		
		List<TeamMember> teamList = new ArrayList<TeamMember>(project.getTeam());
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			Collections.sort(teamList, comp);
		} else {
			Collections.sort(teamList, Collections.reverseOrder(comp));
		}
		pagination.setRecords(teamList.size());
		int fromIndex = (pagination.getPage() - 1) * pagination.getRows();
		pagination.setEntities(teamList.subList(fromIndex, 
				(fromIndex + pagination.getRows() < pagination.getRecords()) 
											? fromIndex + pagination.getRows() 
											: pagination.getRecords()));
	}


}

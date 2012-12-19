package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.proxy.ProxyFactory;


public abstract class GenericXmlDAO<T extends IModel<ID>, ID extends Serializable> implements GenericDAO<T, ID>{
	
	public static final char ACC_SIGN = '_';
	
	public static final char DASH_SIGN = '-';
	
	public static final String NEW_LINE= "\n";
	
	public static final String TAB = "\t";
	
	protected DateFormat dateFormat;
	
	protected ProxyFactory proxyFactory;
	
	
	public GenericXmlDAO() {
		super();
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);	
		dateFormat = new SimpleDateFormat(bundle.getString("date.format"));
	}
	


	public void setProxyFactory(ProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}


	/* 
	 * Convert xml tag name to enum element name 
	 */
	public String toEnumName(String value) {
		return value.toUpperCase().replace(DASH_SIGN, ACC_SIGN);
	}
	
	
	/* return inner tag text end move to next event */
	protected String getElementText(XMLEventReader eventReader, XMLEvent nextEvent) 
			throws XMLStreamException {
		String result = "";
		//nextEvent may be equal to null if the stream is at EOF
		if (nextEvent != null && nextEvent.isCharacters()) {
        	Characters c = eventReader.nextEvent().asCharacters();
        	if (!c.isWhiteSpace()) {
        		result = c.getData();
        	}
        }
		return result;
	}
	
	
	/* 
	 * Create and save to eventWriter new xml text only tag
	 * with name equal to elementName and innerText equal to value
	 */
	protected void createItemNode(XMLEventFactory eventFactory, 
			XMLEventWriter eventWriter, String elementName, String value, Boolean cdata) 
					throws XMLStreamException{  
		XMLEvent tab = eventFactory.createDTD(TAB);
		XMLEvent newLine = eventFactory.createDTD(NEW_LINE);  
        StartElement startElement = eventFactory.createStartElement("", "", elementName);  
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(startElement);  
        Characters characters = cdata ? eventFactory.createCData(value) 
        							  : eventFactory.createCharacters(value);  
        eventWriter.add(characters);  
        EndElement endElement = eventFactory.createEndElement("", "", elementName);  
        eventWriter.add(endElement);  
        eventWriter.add(newLine);
    }  
	
	
	protected String dateToString(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}
	
}

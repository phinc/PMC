package by.phinc.pmc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.locks.ReadWriteLock;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import by.phinc.pmc.exception.PMCException;

public abstract class XmlStreamInProcessingTemplate<T> {
	
	public T process (String datafile, final ReadWriteLock lock) {
		IOException ioException = null;
		XMLStreamException xmlException = null;
		ParseException parseException = null;
		
		FileReader reader = null;
		BufferedReader bufReader = null;
		XMLEventReader eventReader = null;
		
		//acquire read lock
		lock.readLock().lock();
		try {
			try {
				XMLInputFactory inFactory = XMLInputFactory.newInstance();
				reader = new FileReader(datafile);
				bufReader = new BufferedReader(reader);
				eventReader = inFactory.createXMLEventReader(bufReader);
				
				return doProcess(eventReader);
				
			} catch (XMLStreamException e) {			
				xmlException = e;
			} catch (IOException e) {
				ioException = e;
			} catch (ParseException e) {
				parseException = e;
			} finally {
				try {
					if (eventReader != null) {
						eventReader.close();
					}
				} catch (XMLStreamException e) {
					if (ioException != null) {
						throw new PMCException("Error while closing data file '" + 
					datafile + "'", ioException, e);
					} else if (xmlException != null) {
						throw new PMCException("Error while closing data file '" + 
					datafile + "'", xmlException, e);
					} else if (parseException != null) {
						throw new PMCException("Error while parsing data file  '" + 
					datafile + "'", parseException, e);
					} else {
						throw new PMCException("Error while closing data file '" + 
					datafile + "'", e);
					}					
				} finally {
					try {
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {
						if (ioException != null) {
							throw new PMCException("Error while closing data file '" + 
						datafile + "'", ioException, e);
						} else if (xmlException != null) {
							throw new PMCException("Error while closing data file '" + 
						datafile + "'", xmlException, e);
						} else if (parseException != null) {
							throw new PMCException("Error while parsing data file  '" + 
						datafile + "'", parseException, e);
						} else {
							throw new PMCException("Error while closing data file '" + 
						datafile + "'", e);
						}
					} finally {
						if (ioException != null) {
							throw new PMCException("Error while reading data file '" + 
						datafile + "'", ioException);
						} else if (xmlException != null) {
							throw new PMCException("Error while reading data file '" + 
						datafile + "'", xmlException);
						} else if (parseException != null) {
							throw new PMCException("Error while parsing data file  '" + 
						datafile + "'", parseException);
						} 
					}
				}		
			}
		} finally {
			lock.readLock().unlock();
		}
		return null;
	}
	
	public abstract T doProcess(XMLEventReader reader) 
			throws IOException, XMLStreamException, ParseException;
}

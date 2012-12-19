package by.phinc.pmc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.IModel;

public abstract class XmlStreamProcessingTemplate {
	
	public void process (String datafile, String tempfile, IModel<Integer> model, 
			final ReadWriteLock lock) {
		
		//acquire write lock
		lock.writeLock().lock();
		
		try {
			IOException ioException = null;
			XMLStreamException xmlException = null;
			
			FileWriter writer = null;
			BufferedWriter bufWriter = null;
			FileReader reader = null;
			BufferedReader bufReader = null;
			XMLEventWriter eventWriter = null;
			XMLEventReader eventReader = null;
			try {			
				XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
				writer = new FileWriter(tempfile);
				bufWriter = new BufferedWriter(writer);
				eventWriter = outFactory.createXMLEventWriter(
						bufWriter); 
				
				XMLInputFactory inFactory = XMLInputFactory.newInstance();
				reader = new FileReader(datafile);
				bufReader = new BufferedReader(reader);
				eventReader = inFactory.createXMLEventReader(bufReader);
				
				doProcess(eventWriter, eventReader, model);
				
			} catch (XMLStreamException e) {
				xmlException = e;
			} catch (IOException e) {
				ioException = e;
			} finally {
				try {
					if (eventWriter != null) {
						eventWriter.flush();
						eventWriter.close();
					}
				} catch (XMLStreamException e) {
					checkException(e, ioException, xmlException, tempfile);
				} finally {					
					try {
						if (writer != null) {
							writer.close();
						}
					} catch (IOException e) {
						checkException(e, ioException, xmlException, tempfile);
					} finally {
						try {
							if (eventReader != null) {
								eventReader.close();
							}
						} catch (XMLStreamException e) {
							checkException(e, ioException, xmlException, datafile);
						} finally {
							try {
								if (reader != null) {
									reader.close();
								}
							} catch (IOException e) {
								checkException(e, ioException, xmlException, datafile);
							} finally {
								if (ioException != null) {
									throw new PMCException("Error while rewriting data file '" + 
								datafile + "'", ioException);
								} 
								if (xmlException != null) {
									throw new PMCException("Error while rewriting data file '" + 
								datafile + "'", xmlException);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			//delete temporary file if there is some errors while processing
			try {
				deleteFile(tempfile);
			} catch (IOException e1) {
				throw new PMCException("Error while deleting temorary data file '" + 
										tempfile + "'", e, e1);
			}
			throw new PMCException ("Error while updating data file '" + 
					datafile + "'. The changes wasn't saved", e);
		} 
		//rewrite old data file if no errors
		try {
			renameFile(tempfile, datafile);
		} catch (IOException e) {
			throw new PMCException("Error while renaming data file '" + datafile + "'", e);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public abstract void doProcess(XMLEventWriter writer, XMLEventReader reader, IModel<Integer> model) 
			throws IOException, XMLStreamException;
	
	
	
	private void checkException(Exception e, IOException ioException, 
			XMLStreamException xmlException, String datafile) {
		if (ioException != null) {
			throw new PMCException("Error while closing data file '" + 
		datafile + "'", ioException, e);
		} else if (xmlException != null) {
			throw new PMCException("Error while closing data file '" + 
		datafile + "'", xmlException, e);
		} else {
			throw new PMCException("Error while closing data file '" + 
		datafile + "'", e);
		} 
	}
	
	private static void deleteFile(String fileName) throws IOException {
	    File tmpFile = new File(fileName);
	    if (tmpFile.exists()) {
	       if (!tmpFile.delete()) {
	           throw new IOException(fileName + " was not successfully deleted"); 
	       }
	    }
	}
	
	private static void renameFile(String oldName, String newName) throws IOException {
	    File srcFile = new File(oldName);
	    boolean bSucceeded = false;
	    try {
	        File destFile = new File(newName);
	        if (destFile.exists()) {
	            if (!destFile.delete()) {
	                throw new IOException(oldName + " was not successfully renamed to " + newName); 
	            }
	        }
	        if (!srcFile.renameTo(destFile))        {
	            throw new IOException(oldName + " was not successfully renamed to " + newName);
	        } else {
	                bSucceeded = true;
	        }
	    } finally {
	          if (bSucceeded) {
	                srcFile.delete();
	          }
	    }
	}
}

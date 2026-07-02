package com.gswrapper.util;

import java.io.*;
import javax.xml.bind.*;
import org.apache.log4j.*;

import com.gswrapper.control.*;

/**
 * 
 * LectorXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date2019-07-19
 *
 */
public class LectorXML {
	
	private static final Logger LOGGER = Logger.getLogger(AccionManager.class);
	
	private static LectorXML instance;
	
	public static synchronized LectorXML getInstance() {
		
		if(instance == null) instance = new LectorXML();
		
		return instance;
	}
	
	
	@SuppressWarnings("unchecked")
	public <E>E leerXML(String path, Class<E> E) {
		
		E objetoXML = null;
		
		try {
			
		    Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(E).createUnmarshaller(); 
		    objetoXML = (E) jaxbUnmarshaller.unmarshal(new File(path));   
		    
		}catch (JAXBException e) {
			
			LOGGER.error("Error al intentar leer " + path, e);
		}
		
		return objetoXML;
	}
	
	
	public <E> boolean escribirXML(String path, Class<E> E, E objetoXML) {
		
		boolean exito = false;
		
		try {
			
			Marshaller marshaller = JAXBContext.newInstance(E).createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
		    marshaller.marshal(objetoXML, new FileOutputStream(path));
		    
		    exito = true;
			
		}catch (JAXBException | FileNotFoundException e) {
			
			LOGGER.error("Error al intentar escribir " + path, e);
		}
		
		return exito;
	}
}

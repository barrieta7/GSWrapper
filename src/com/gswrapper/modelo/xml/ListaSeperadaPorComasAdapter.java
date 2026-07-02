package com.gswrapper.modelo.xml;

import java.util.*;

import javax.xml.bind.annotation.adapters.*;

import com.gswrapper.util.*;

/**
 * 
 * ListaSeperadaPorComasAdapter.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class ListaSeperadaPorComasAdapter extends XmlAdapter<String, List<String>>{
	
	private static final String DELIMITADOR_LISTA = ",";

	@Override
	public List<String> unmarshal(String listaString) throws Exception {
	
		List<String> lista = new ArrayList<>();

        for (String valor : listaString.split(DELIMITADOR_LISTA)) {
        	
            if (!Condition.empty(valor))  lista.add(valor.trim());
        }

        return lista;
	}

	@Override
	public String marshal(List<String> lista) throws Exception {
		
		StringBuilder sb = new StringBuilder();

	    for (String valor : lista) {
	    	
	    	if(!Condition.empty(valor)) {
	    		
	    		if (sb.length() > 0)  sb.append(", ");

	            sb.append(valor);
	    	}
	    }    
	    
	    return sb.toString();
	}
		
}

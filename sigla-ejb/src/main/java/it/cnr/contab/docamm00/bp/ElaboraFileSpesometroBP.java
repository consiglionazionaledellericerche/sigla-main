/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.bp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewBulk;
import it.cnr.contab.docamm00.ejb.ElaboraFileIntraComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2.DatiFatturaType;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2.NazioneITType;


public class ElaboraFileSpesometroBP extends SimpleCRUDBP {
	
public ElaboraFileSpesometroBP() {
	super();
}
public ElaboraFileSpesometroBP(String function) {
	super(function);
} 
public ElaboraFileIntraComponentSession createComponentSession() throws BusinessProcessException
{
return (ElaboraFileIntraComponentSession)createComponentSession("CNRDOCAMM00_EJB_ElaboraFileIntraComponentSession",ElaboraFileIntraComponentSession.class);
}
public boolean isScaricaButtonEnabled() {
	if(getFile()!=null)
		return true;
	else
		return false;
}


protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.start");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.download");
	
	return toolbar;
}

private String file;
private Integer conta=0;
private String nome_file;
public void doElaboraFile(ActionContext context,VSpesometroNewBulk dett) throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException, JAXBException {
		 try{  
		  File f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
				  dett.getNome_file()+".xml");
			  
			FileOutputStream fileOutputStream = new FileOutputStream(f);
			ElaboraFileIntraComponentSession component = createComponentSession();
			if (component.EstraiLista(context.getUserContext(), dett).size() >1000)
					throw new it.cnr.jada.comp.ApplicationException("Sono state selezionati troppi dati, diminuire intervallo!");
			JAXBElement<DatiFatturaType> datifatturaType = component.creaDatiFatturaType(context.getUserContext(), dett);
		
			JAXBContext jaxbContext = JAXBContext.newInstance("it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2");
			
			jaxbContext.createMarshaller().marshal(datifatturaType, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			setFile("/tmp/"+f.getName());	  

		 } catch (FileNotFoundException e) {
		   throw new ApplicationException("File non trovato!");
		 }
		 catch (IllegalArgumentException e) {
		 throw new ApplicationException("Formato file non valido!");
		 }
		 catch (IOException e) {
		 throw new ApplicationException("Errore nella scrittura del file!");		
		 }	
}
/**
 * @param s Stringa in Input
 * @param allineamento del testo "D" Destra - "S" Sinistra 
 * @param dimensione richiesta del campo
 * @param riempimento carattere di riempimento per raggiungere la dimensione richiesta
 * @return La stringa formattata e riempita con l'allinemento richiesto
 */
public String Formatta(String s, String allineamento,Integer dimensione,String riempimento){
	if (s==null)
		s=riempimento;
	if (s.length()< dimensione){
		if (allineamento.compareTo("D")==0){
			while (s.length()<dimensione)
			 s=riempimento+s;
		   return s.toUpperCase();
		}
		else
		{
			while (s.length()<dimensione)
				 s=s+riempimento;
			return s.toUpperCase();
		}
	}else if (s.length()> dimensione){
		s=s.substring(0,dimensione);
		return s.toUpperCase();
	}
	return s.toUpperCase();
}

public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
Button[] toolbar = getToolbar();
if(getFile()!=null){
	HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
	HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
    StringBuffer stringbuffer = new StringBuffer();
    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
    toolbar[1].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
}
super.writeToolbar(pagecontext);
}
public String getFile() {
return file;
}
public void setFile(String file) {
this.file = file;
}
@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		try {
			  super.init(config,context);
			  AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
		      AnagraficoBulk ente = sess.getAnagraficoEnte(context.getUserContext());
		     
			VSpesometroNewBulk bulk  = new VSpesometroNewBulk();
			bulk.setNome_file(NazioneITType.IT+ente.getCodice_fiscale()+"_"+"DF"+"_"+"00001");
			setModel(context,bulk);
		} catch(Throwable e) {
			throw handleException(e);
		}
		
	}
}
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

package it.cnr.contab.cori00.bp;

/**
 * Un BusinessProcess controller che permette di effettuare operazioni di CRUD su istanze di 
 *	Liquid_coriBulk per la gestione della Liquidazione dei Contributi/Ritenuta.
**/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.*;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
 
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.cori00.docs.bulk.*;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.cori00.views.bulk.F24ep_tempBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.*;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
 
public class CRUDLiquidazioneCORIBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private boolean isCalcolato = false;
	private boolean isLiquidato = false;
	
	private final Liquid_gruppoCoriCRUDController gruppi = new Liquid_gruppoCoriCRUDController(
									"Gruppi",
									Liquid_gruppo_coriIBulk.class,
									"coriColl",
									this);

	private final RemoteDetailCRUDController gruppiDet = new RemoteDetailCRUDController("gruppiDet",Liquid_gruppo_cori_detIBulk.class,"gruppiDet","CNRCORI00_EJB_Liquid_coriComponentSession",gruppi,false);

	private final SimpleDetailCRUDController capitoli = new SimpleDetailCRUDController("capitoli", it.cnr.contab.cori00.views.bulk.V_liquid_capitoli_coriBulk.class, "capitoliColl", gruppi,false);

public CRUDLiquidazioneCORIBP() {
	super("Tr");
	setTab("tab", "tabLiquidCoriTestata");
	setTab("tabLiquidCoriCapitoli","tabLiquidCoriCapitoli");
}
public CRUDLiquidazioneCORIBP(String function) {
	super(function + "Tr");
	setTab("tab", "tabLiquidCoriTestata");
}
/**
  *  Crea una toolbar in aggiunta alla normale toolbar del CRUD.
  *	La nuova toolbar è stata costruita per mostrare il tasto "Visualizza accessori".
  *
  * @return toolbar i <code>Button[]</code> pulsanti creati
**/ 
protected it.cnr.jada.util.jsp.Button[] createViewDettaglioGruppiToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.viewDettaglioGruppi");	
	
	return toolbar;
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[11];
	//Button[] toolbar = new Button[12];
	int i = 0;
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
	toolbar[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
	//toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.f24");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.f24Tot");
	// attenzione il link al file viene settato dinamicamente con il nome del file nella writeToolbar(PageContext pagecontext) 
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.download");
	return toolbar;
}
/**
 * Restituisce il valore della proprietà 'capitoli'
 *
 * @return capitoli il <code>SimpleDetailCRUDController</code> controller sui capitoli di un Gruppo CORI
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCapitoli() {
	return capitoli;
}
/**
 * Restituisce il valore della proprietà 'gruppi'
 *
 * @return gruppi il <code>SimpleDetailCRUDController</code> controller sui gruppi CORI
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getGruppi() {
	return gruppi;
}
/**
 * Restituisce il valore della proprietà 'gruppiDet'
 *
 * @return gruppiDet il <code>RemoteDetailCRUDController</code> controller sui detagli di un gruppo CORI
 */
public final it.cnr.jada.util.action.RemoteDetailCRUDController getGruppiDet() {
	return gruppiDet;
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param config <code>Config</code>
 * @param context <code>ActionContext</code>
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	context.getBusinessProcess("/GestioneUtenteBP").removeChild("CRUDLiquidazioneCORIBP");
	
	super.init(config,context);
	resetTabs();

	gruppiDet.setEnabled(false);
	capitoli.setEnabled(false);
	
}
/**
 * E' stata generata la richiesta di conoscere lo stato della Liquidazione.
 *	Durante una operazione di Liquida CORI si verificano due momenti chiave: il primo è rappresentato d
 *	dal calcolo delle liquidazioni che l'utente può effettuare: l'utente clicca sul tasto 
 *	"Calcola liquidazione", ed il sistema propone tutti i Gruppi CORI che sono ancora da liquidare.
 *	L'utente, poi, seleziona i gruppi ed effettua la Liquidazione vera e propria.
 *	Il metodo restituisce TRUE se è stata effettuata l'operazione di "Calcola Liquidazione".
 *
 * @return isCalcolato <code>boolean</code> lo stato del processo
 */
public boolean isCalcolato() {
	return isCalcolato;
}
/**
 *	Nascondo il bottone di Cancella
 *
 * @return <code>boolean</code> TRUE
**/
public boolean isDeleteButtonHidden() {

	return true;
}
/**
 * E' stata generata la richiesta di conoscere lo stato della Liquidazione.
 *	Durante una operazione di Liquida CORI si verificano due momenti chiave: il primo è rappresentato d
 *	dal calcolo delle liquidazioni che l'utente può effettuare: l'utente clicca sul tasto 
 *	"Calcola liquidazione", ed il sistema propone tutti i Gruppi CORI che sono ancora da liquidare.
 *	L'utente, poi, seleziona i gruppi ed effettua la Liquidazione vera e propria.
 *	Il metodo restituisce TRUE se è stata effettuata l'operazione di "Liquida".
 *
 * @return isLiquidato <code>boolean</code> lo stato del processo
 */
public boolean isLiquidato() {
	return isLiquidato;
}
/**
 *	Nascondo il bottone di Salva
 *
 * @return <code>boolean</code> TRUE
**/

public boolean isSaveButtonHidden() {
	return true;
}
/**
 * E' stata generata la richiesta di conoscere lo stato della Liquidazione.
 *	Durante una operazione di Liquida CORI si verificano due momenti chiave: il primo è rappresentato d
 *	dal calcolo delle liquidazioni che l'utente può effettuare: l'utente clicca sul tasto 
 *	"Calcola liquidazione", ed il sistema propone tutti i Gruppi CORI che sono ancora da liquidare.
 *	L'utente, poi, seleziona i gruppi ed effettua la Liquidazione vera e propria.
 *	Il metodo restituisce TRUE se è stata effettuata l'operazione di "Calcola Liquidazione".
 *
 * @return isCalcolato <code>boolean</code> lo stato del processo
 */
public boolean isTabCapitoliVisible() {

	if (isInserting())
		return isCalcolato;

	return true;
}
/**
 *	Nascondo il bottone di Vista Dettaglio Gruppi Locali
 *
 * @return <code>boolean</code> TRUE
**/
public boolean isViewDettaglioGruppiEnabled() {
	return true;
	//return isEditing();
}
/**
 * Imposta come attivi i tab di default.
**/

public void resetTabs() {
	setTab("tab","tabLiquidCoriTestata");
}
/**
 * Imposta il valore della proprietà 'isCalcolato'
 *
 * @param newIsCalcolato il <code>boolean</code> valore da assegnare a 'isCalcolato'
**/
public void setIsCalcolato(boolean newIsCalcolato) {
	isCalcolato = newIsCalcolato;
}
/**
 * Imposta il valore della proprietà 'isLiquidato'
 *
 * @param newIsLiquidato il <code>boolean</code> valore da assegnare a 'isLiquidato'
**/
public void setIsLiquidato(boolean newIsLiquidato) {
	isLiquidato = newIsLiquidato;
}
/**
 * Aggiunge alla normale toolbar di CRUD, la toolbar per la visualizzazione dei beni 
 *	accessori, (v. metodo createViewAccessoriToolbar()).
 *
 * @param writer <code>JspWriter</code>
**/
public void writeToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

	super.writeToolbar(writer);
	writeViewDettaglioGruppiToolbar(writer);
}
/**
 * Scrive la toolbar contenente il tasto di "Dettaglio Gruppi Locali"
 *
 * @param writer <code>JspWriter</code>
**/
public void writeViewDettaglioGruppiToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

	//if (!isSearching()) {
		//openToolbar(writer);
		//it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createViewAccessoriToolbar(),this);
		//closeToolbar(writer);
	//}

	openToolbar(writer);
	it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createViewDettaglioGruppiToolbar(),this, this.getParentRoot().isBootstrap());
	closeToolbar(writer);
}
private boolean F24 = true;
private String file;
private boolean abilitatoF24;

public boolean isScaricaButtonEnabled() {
	if(getFile()!=null)
		return true;
	else
		return false;
}
public boolean isF24ButtonHidden() {
	if(isAbilitatoF24()&& isF24ButtonEnabled())
		return false;
	else
		return true;
}
public boolean isF24ButtonEnabled() {
	if(getModel()!=null && ((Liquid_coriBulk)getModel()).getStato()!=null &&
			((Liquid_coriBulk)getModel()).getStato().compareTo("L")==0)
		return true;
	else
		return false;
}

public boolean isAbilitatoF24() {
	return abilitatoF24;
}
public void setAbilitatoF24(boolean abilitatoF24) {
	this.abilitatoF24 = abilitatoF24;
}
protected void initialize(ActionContext context) throws BusinessProcessException {
try {
	setAbilitatoF24(UtenteBulk.isAbilitatoF24EP(context.getUserContext()));	
} catch (ComponentException e1) {
	throw handleException(e1);
} catch (RemoteException e1) {
	throw handleException(e1);
}
super.initialize(context);
}
public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
Button[] toolbar = getToolbar();
if(getFile()!=null){
	//HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
	HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
    StringBuffer stringbuffer = new StringBuffer();
    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
    toolbar[10].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
}
super.writeToolbar(pagecontext);
}
public String getFile() {
return file;
}
public void setFile(String file) {
this.file = file;
}
/**Procedura attualmente NON UTILIZZATA Vecchio tracciato*/
public void Estrazione(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
	 try{	
		 //	da fare 
		 // lanciare popolamento della tabella da cui pescare i dati  
		 // estrarli per popolare la tabella definitiva
      AnagraficoBulk ente = ((Liquid_coriComponentSession)createComponentSession()).getAnagraficoEnte(context.getUserContext());
  	  java.util.List lista=null;
  	 it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk liquidazione=null;
      if (getModel()!=null && getModel() instanceof it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk) {
    	  liquidazione =(it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk) getModel();
    	  ((Liquid_coriComponentSession)createComponentSession()).Popola_f24(context.getUserContext(),liquidazione);
          lista=((Liquid_coriComponentSession)createComponentSession()).EstraiLista(context.getUserContext(),liquidazione);
      }
      File f = null;
     String data_formattata=Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0").concat(
    		 Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
    				 EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR));
      if(liquidazione!=null)
    	  	  f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
			  "F24EP-"+liquidazione.getCd_unita_organizzativa().replace(".","-")+"-"+liquidazione.getPg_liquidazione()+"-"+data_formattata+".T24");
      else
    	  throw new ApplicationException("Non ci sono dati!");    	
  
	  OutputStream os = (OutputStream)new FileOutputStream(f);
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      
      if (lista!= null  && !lista.isEmpty())
     {  
    	  //Testata - posizionale lunghezza 1900 caratteri
    	  String Codice_Fiscale =ente.getCodice_fiscale();
    	  
    	  //Testata parte iniziale Fissa Tipo Record "A"
    	  bw.append("A");
    	  bw.append(Formatta(null,"S",14," "));
    	  bw.append(Formatta("F24EP","S",5," "));
    	  bw.append("14");
    	  bw.append(Formatta(Codice_Fiscale,"S",16," "));
    	  bw.append(Formatta(null,"S",177," "));
    	  bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
    	  bw.append(Formatta(null,"S",164," "));
    	  bw.append(Formatta(null,"S",1," ")); //???"A"
    	  bw.append(Formatta(null,"S",14," ")); 
    	  bw.append(Formatta(null,"S",67," "));
    	  bw.append("001");
    	  bw.append("001");//?
    	  bw.append(Formatta(null,"S",100," "));
    	  bw.append("1");
    	  bw.append(Formatta(null,"S",1269," "));
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");
          // Fine Testata
    	  //parte iniziale Fissa Tipo Record "M"
    	  bw.append("M");
    	  bw.append(Formatta(Codice_Fiscale,"S",11," "));
    	  bw.append(Formatta(null,"S",5," "));
    	  bw.append(Formatta("1","D",8,"0"));
    	  bw.append(Formatta(null,"S",3," "));
    	  bw.append(Formatta(null,"S",25," "));
    	  bw.append(Formatta(null,"S",20," "));
    	  bw.append(Formatta(null,"S",16," "));
    	  bw.append(Formatta(null,"S",1," "));
    	  bw.append("E");
    	  bw.append(Formatta(null,"S",426," "));
    	  bw.append(Formatta(ente.getRagione_sociale(),"S",55," "));
    	  bw.append(Formatta(null,"S",1195," "));
    	  bw.append("14");
    	  bw.append(Formatta(Codice_Fiscale,"D",11,"0"));
    	  
    	  String conto = ((Liquid_coriComponentSession)createComponentSession()).getContoSpecialeEnteF24(context.getUserContext());
    	  bw.append(conto);
    	  //da fare 
    	  //bw.append("IT");
    	  //bw.append("12"); //iban
    	  //bw.append("H"); //cin
    	  //bw.append("01000"); //banca italia abi
    	  //bw.append("03245"); //banca italia cab
      	  //bw.append("348300167369"); //conto + tesoreria NU 12
      	  //
    	  bw.append(Formatta(null,"S",61," "));
    	  bw.append("EURO"); //valuta
    	  BigDecimal tot_f24=new BigDecimal(0);
    	  for (Iterator i = lista.iterator(); i.hasNext();) {
		    	 F24ep_tempBulk f24ep =(F24ep_tempBulk)i.next();
		    	 tot_f24=tot_f24.add(f24ep.getImporto_debito());
    	  }    	  
    	  bw.append(Formatta(new it.cnr.contab.util.EuroFormat().format(tot_f24),"S",15," ")); 
    	  bw.append("01-01-0000"); //data versamento
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");

    	  // fine record tipo M
    	  int riga=1;
    	  Integer tot_righe=1;
    	  BigDecimal tot1=new BigDecimal(0);
    	  
    	  for (Iterator i = lista.iterator(); i.hasNext();) {
    		  F24ep_tempBulk f24ep =(F24ep_tempBulk)i.next();
    		  if(riga==1){
	    		  bw.append("V");
	    		  bw.append(Formatta(Codice_Fiscale,"S",16," "));
	    		  bw.append(Formatta("1","D",8,"0"));
	    		  bw.append(Formatta(null,"S",3," "));
	    		  bw.append(Formatta(null,"S",25," "));
	    		  bw.append(Formatta(null,"S",20," "));
	    		  bw.append(Formatta(null,"S",16," "));
	    		  bw.append("6");
    		  }
    		  
    		  tot1=tot1.add(f24ep.getImporto_debito());
    		  bw.append(Formatta(f24ep.getCodice_tributo(),"S",4," "));
    		  bw.append(Formatta(f24ep.getCodice_ente(),"S",4," "));
    		  bw.append(Formatta(f24ep.getMese_rif(),"D",4,"0"));
    		  bw.append(Formatta(f24ep.getAnno_rif(),"D",4,"0"));
    		  bw.append(Formatta(null,"S",13," "));
    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
    		  bw.append(Formatta(null,"D",15,"0"));
    		  if(riga ==28){
    			  tot_righe=tot_righe+1;
    			  //saldo di sezione
    			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
        		  bw.append(Formatta(null,"D",15,"0"));
        		  bw.append("P");
        		  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
        		  bw.append(Formatta(null,"S",4," "));
    			  //fine saldo di sezione
        		  //saldo finale 
        		  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0")); //?? congruente
        		  bw.append("01010000"); //data versamento
        		  bw.append(Formatta(null,"S",82," "));
        		  bw.append("A");
            	  //bw.newLine(); da problemi per il formato del file
            	  bw.append("\r\n");

    			  riga=1;
    			  tot1=new BigDecimal(0);
    		  }else
    			  riga=riga+1;
    	  }
    	  // completo ultima riga
    	  if(riga!=1){
    		  while(riga<=28){
    			  bw.append(Formatta(null,"S",4," "));//codice tributo
    			  bw.append(Formatta(null,"S",4," "));//codice ente
    			  bw.append(Formatta(null,"S",4," "));//mese
    			  bw.append(Formatta(null,"D",4,"0"));//anno
    			  bw.append(Formatta(null,"S",13," "));
    			  bw.append(Formatta(null,"D",15,"0"));//importo a debito
    			  bw.append(Formatta(null,"D",15,"0"));
    			  riga=riga+1; 
    		  }  	
	    	  //saldo di sezione
	    	  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
			  bw.append(Formatta(null,"D",15,"0"));
			  bw.append("P");
			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
			  bw.append(Formatta(null,"S",4," "));
			  //fine saldo di sezione
			  //saldo finale 
			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0")); //?? congruente
			  bw.append("01010000"); //data versamento
			  bw.append(Formatta(null,"S",82," "));
			  bw.append("A");
	    	  //bw.newLine(); da problemi per il formato del file
	    	  bw.append("\r\n");
    	  }else
    		  tot_righe=tot_righe-1;
    		  
    	  //Coda
    	  bw.append("Z");
    	  bw.append(Formatta(null,"S",14," "));
    	  bw.append(Formatta(tot_righe.toString(),"D",9,"0"));// numero record tipo V
    	  bw.append(Formatta("1","D",9,"0"));// numero record tipo M
    	  bw.append(Formatta(null,"S",1864," "));
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");

    	  //fine Coda
		  bw.flush();
	      bw.close();
	      osw.close();
	      os.close();	      
	      ///
		     setFile("/tmp/"+f.getName());	  
		     }else{
		    	  bw.flush();
			      bw.close();
			      osw.close();
			      os.close();	      
		    	 throw new ApplicationException("Non ci sono dati!");    	 
		     }    
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
@Override
public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
	setFile(null);
	return super.initializeModelForSearch(actioncontext, oggettobulk);
}

public void SelezionaF24(ActionContext context,boolean negativi) {
	try {
		List gruppi=getGruppi().getDetails();
		Gruppo_crBulk gruppo_cr=null;
		java.sql.Connection conn=null;
		Iterator it = gruppi.iterator();
		for (int i=0; i< gruppi.size();i++){
			Liquid_gruppo_coriBulk liquid=(Liquid_gruppo_coriBulk)it.next();
			conn= EJBCommonServices.getConnection(context); 
			try{
				it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
				gruppo_cr = new Gruppo_crBulk();
				gruppo_cr.setEsercizio(liquid.getEsercizio());
				gruppo_cr.setCd_gruppo_cr(liquid.getCd_gruppo_cr());  
				gruppo_cr = (Gruppo_crBulk)homeCache.getHome(Gruppo_crBulk.class).findByPrimaryKey(gruppo_cr);
				if (gruppo_cr.getFl_f24online().booleanValue()){
					if (negativi)
						getGruppi().getSelection().setSelected(i);
					else
						if(liquid.getIm_liquidato().compareTo(BigDecimal.ZERO)>=0)
							getGruppi().getSelection().setSelected(i);
				}
			}
			 finally {
   				   if (conn!=null)
   					  try{conn.close();}catch( java.sql.SQLException e ){};
   			}
			 
		} 
		List liq = getGruppi().getDetails();
		if (!liq.isEmpty()) {
			OggettoBulk[] selezionati = new OggettoBulk[liq.size()];
			BitSet oldbit = new BitSet(liq.size());
			BitSet newbit = new BitSet(liq.size());
			Iterator iter = liq.iterator();
			for(int i=0;iter.hasNext();i++) {
				selezionati[i]=(OggettoBulk)iter.next();
				oldbit.clear(i);
				newbit.set(i);
			}
		}
	}
	 catch(Throwable e) {
	}
		 
}


public void EstrazioneTot(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
	 try{	
		 //	da fare 
		 // lanciare popolamento della tabella da cui pescare i dati  
		 // estrarli per popolare la tabella definitiva
      AnagraficoBulk ente = ((Liquid_coriComponentSession)createComponentSession()).getAnagraficoEnte(context.getUserContext());
  	  java.util.List lista=null;
  	 it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk liquidazione=null;
      if (getModel()!=null && getModel() instanceof it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk) {
    	  liquidazione =(it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk) getModel();
    	  ((Liquid_coriComponentSession)createComponentSession()).Popola_f24Tot(context.getUserContext(),liquidazione);
          lista=((Liquid_coriComponentSession)createComponentSession()).EstraiListaTot(context.getUserContext(),liquidazione);
      }
      File f = null;
     String data_formattata=Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0").concat(
    		 Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
    				 EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR));
      if(liquidazione!=null)
    	  	  f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
			  "F24EP-"+liquidazione.getCd_unita_organizzativa().replace(".","-")+"-"+liquidazione.getPg_liquidazione()+"-"+data_formattata+".T24");
      else
    	  throw new ApplicationException("Non ci sono dati!");    	
  
	  OutputStream os = (OutputStream)new FileOutputStream(f);
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);
      
      if (lista!= null  && !lista.isEmpty())
     {  
    	  //Testata - posizionale lunghezza 1900 caratteri
    	  String Codice_Fiscale =ente.getCodice_fiscale();
    	  
    	  //Testata parte iniziale Fissa Tipo Record "A"
    	  bw.append("A");
    	  bw.append(Formatta(null,"S",14," "));
    	  bw.append(Formatta("F24EP","S",5," "));
    	  bw.append("14");
    	  bw.append(Formatta(Codice_Fiscale,"S",16," "));
    	  bw.append(Formatta(null,"S",177," "));
    	  bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
    	  bw.append(Formatta(null,"S",164," "));
    	  bw.append(Formatta(null,"S",1," ")); //???"A"
    	  bw.append(Formatta(null,"S",14," ")); 
    	  bw.append(Formatta(null,"S",67," "));
    	  bw.append("001");
    	  bw.append("001");//?
    	  bw.append(Formatta(null,"S",100," "));
    	  bw.append("1");
    	  bw.append(Formatta(null,"S",1269," "));
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");
          // Fine Testata
    	  //parte iniziale Fissa Tipo Record "M"
    	  bw.append("M");
    	  bw.append(Formatta(Codice_Fiscale,"S",11," "));
    	  bw.append(Formatta(null,"S",5," "));
    	  bw.append(Formatta("1","D",8,"0"));
    	  bw.append(Formatta(null,"S",3," "));
    	  bw.append(Formatta(null,"S",25," "));
    	  bw.append(Formatta(null,"S",20," "));
    	  bw.append(Formatta(null,"S",16," "));
    	  bw.append(Formatta(null,"S",1," "));
    	  bw.append("E");
    	  bw.append(Formatta(null,"S",426," "));
    	  bw.append(Formatta(ente.getRagione_sociale(),"S",55," "));
    	  bw.append(Formatta(null,"S",1195," "));
    	  bw.append("14");
    	  bw.append(Formatta(Codice_Fiscale,"D",11,"0"));
    	  
    	  String conto = ((Liquid_coriComponentSession)createComponentSession()).getContoSpecialeEnteF24(context.getUserContext());
    	  // ??????
    	  bw.append(conto); //??????
    	  //da fare 
    	  //bw.append("IT");
    	  //bw.append("12"); //iban
    	  //bw.append("H"); //cin
    	  //bw.append("01000"); //banca italia abi
    	  //bw.append("03245"); //banca italia cab
      	  //bw.append("348300167369"); //conto + tesoreria NU 12
      	  //
    	  //modificato per email
    	  //bw.append(Formatta(null,"S",61," "));
    	  bw.append(Formatta(null,"S",1," "));
    	  bw.append(Formatta(null,"S",60," ")); // email contribuente
    	  
    	  bw.append("EURO"); //valuta
    	  BigDecimal tot_f24=new BigDecimal(0);
    	  for (Iterator i = lista.iterator(); i.hasNext();) {
    		  it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk f24ep =(it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk)i.next();
		    	 tot_f24=tot_f24.add(f24ep.getImporto_debito());
    	  }    	  
    	  bw.append(Formatta(new it.cnr.contab.util.EuroFormat().format(tot_f24),"S",15," ")); 
    	  bw.append("01-01-0000"); //data versamento
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");

    	  // fine record tipo M
    	  int riga=1;
    	  Integer tot_righe=1;
    	  BigDecimal tot1=new BigDecimal(0);
    	  
    	  for (Iterator i = lista.iterator(); i.hasNext();) {
    		  it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk f24ep =(it.cnr.contab.cori00.views.bulk.F24ep_tempTotBulk)i.next();
    		  if(riga==1){
	    		  bw.append("V");
	    		  bw.append(Formatta(Codice_Fiscale,"S",16," "));
	    		  bw.append(Formatta("1","D",8,"0"));
	    		  bw.append(Formatta(null,"S",3," "));
	    		  bw.append(Formatta(null,"S",25," "));
	    		  bw.append(Formatta(null,"S",20," "));
	    		  bw.append(Formatta(null,"S",16," "));
	    		  //modificato
	    		  //bw.append("6");
	    		  bw.append("7");
	    		// SEZIONE VERSAMENTI ???
	    		  bw.append(Formatta(null,"S",3," ")); //CODICE UFFICIO FINANZIARIO
	    		  bw.append(Formatta(null,"D",11,"0")); //CODICE ATTO
	    		  
    		  }
    		  
    		  tot1=tot1.add(f24ep.getImporto_debito());
    		  /* totalmente modificato
    		  bw.append(Formatta(f24ep.getCodice_tributo(),"S",4," "));
    		  bw.append(Formatta(f24ep.getCodice_ente(),"S",4," "));
    		  bw.append(Formatta(f24ep.getMese_rif(),"D",4,"0"));
    		  bw.append(Formatta(f24ep.getAnno_rif(),"D",4,"0"));
    		  bw.append(Formatta(null,"S",13," "));
    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
    		  bw.append(Formatta(null,"D",15,"0"));*/
    		  
    		  bw.append(Formatta(f24ep.getTipo_riga_f24(),"S",1," "));
    		  bw.append(Formatta(f24ep.getCodice_tributo(),"S",6," "));
    		  if(f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.ENTI_LOCALI)==0 ||
    		     f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.ERARIO)==0||
    		     f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.REGIONI)==0||
    		     f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.FONDI_PENSIONE)==0){
    			  // Vecchie tipologie inalterate 
	    		  bw.append(Formatta(f24ep.getCodice_ente(),"S",5," "));
	    		  bw.append(Formatta(null,"S",17," "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
	    		  bw.append(Formatta(Formatta(f24ep.getMese_rif(),"D",4,"0"),"S",6," "));
	    		  bw.append(Formatta(Formatta(f24ep.getAnno_rif(),"D",4,"0"),"S",6," "));
	    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
    	  	  }
	    	  else{
	    		  //nuovi
	    		 if(f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPS)==0){
	    			  String sede_inps = ((Liquid_coriComponentSession)createComponentSession()).getSedeInpsF24(context.getUserContext());
	    			  bw.append(Formatta(sede_inps,"S",5," "));
		    		  bw.append(Formatta(f24ep.getCd_matricola_inps(),"S",17," "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
		    		  bw.append(Formatta(f24ep.getPeriodo_da(),"S",6," "));
		    		  // non valorizzare per i tributi usati nel CNR
		    		  //bw.append(Formatta(f24ep.getPeriodo_a(),"S",6," "));
		    		  bw.append(Formatta(null,"S",6," "));
		    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
	    		 }
	    		 if(f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INAIL)==0){
	    			  Configurazione_cnrBulk conf_inail = ((Liquid_coriComponentSession)createComponentSession()).getSedeInailF24(context.getUserContext());
	    			  bw.append(Formatta(conf_inail.getVal01(),"S",5," "));
	    			  // ???? numero posizione assicurativa ???
		    		  bw.append(Formatta(conf_inail.getVal02(),"S",17," "));  // ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
		    		  // ??? causale fonte risoluzione 97
		    		  bw.append(Formatta("P","S",6," "));
		    		  bw.append(Formatta(null,"S",6," "));
		    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
	    		 }
	    		 if(f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPDAP)==0){
	    			  String sede_inpdap = ((Liquid_coriComponentSession)createComponentSession()).getSedeInpdapF24(context.getUserContext());
	    			  bw.append(Formatta(sede_inpdap,"S",5," "));
	    			  //??? FACOLTATIVO ???
		    		  bw.append(Formatta(null,"S",17," "));  // ESTREMI IDENTIFICATIVI 
		    		  bw.append(Formatta(f24ep.getPeriodo_da(),"S",6," "));
		    		  bw.append(Formatta(f24ep.getPeriodo_a(),"S",6," "));
		    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
	    		 }
	    		 if(f24ep.getTipo_riga_f24().compareTo(Gruppo_crBulk.INPGI)==0){
	    			  String sede_inpgi= ((Liquid_coriComponentSession)createComponentSession()).getSedeInpgiF24(context.getUserContext());
	    			  bw.append(Formatta(null,"S",5," "));
	    			  //??? FACOLTATIVO ???
		    		  bw.append(Formatta(Formatta(sede_inpgi,"S",5,"0"),"S",17," "));  // ESTREMI IDENTIFICATIVI 
		    		  bw.append(Formatta(Formatta(f24ep.getMese_rif(),"D",4,"0"),"S",6," "));
		    		  bw.append(Formatta(Formatta(f24ep.getAnno_rif(),"D",4,"0"),"S",6," "));
		    		  bw.append(Formatta(f24ep.getImporto_debito().movePointRight(2).toString(),"D",15,"0"));
	    		 }
	    	  }
    		  if(riga ==28){
    			  tot_righe=tot_righe+1;
    			  //saldo di sezione
    			  bw.append(Formatta(null,"S",70," ")); // Nuovo filler
    			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
        		  bw.append(Formatta(null,"D",15,"0"));
        		  bw.append("P");
        		  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
        		  bw.append(Formatta(null,"S",4," "));
    			  //fine saldo di sezione
        		  //saldo finale 
        		  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0")); //?? congruente
        		  bw.append("01010000"); //data versamento
        		  bw.append(Formatta(null,"S",82," "));
        		  bw.append("A");
            	  //bw.newLine(); da problemi per il formato del file
            	  bw.append("\r\n");

    			  riga=1;
    			  tot1=new BigDecimal(0);
    		  }else
    			  riga=riga+1;
    	  }
    	  // completo ultima riga
    	  if(riga!=1){
    		  while(riga<=28){
    			  bw.append(Formatta(null,"S",1," "));//getTipo_riga_f24
    			  bw.append(Formatta(null,"S",6," "));//codice tributo
    			  bw.append(Formatta(null,"S",5," "));//codice ente
    			  bw.append(Formatta(null,"S",17," "));// ESTREMI IDENTIFICATIVI NON COMPILARE PER VECCHIO F24EP
    			  bw.append(Formatta(null,"S",6," "));//mese
    			  bw.append(Formatta(null,"S",6," "));//anno
    			  bw.append(Formatta(null,"D",15,"0"));//importo a debito
    			  riga=riga+1; 
    		  }  	
	    	  //saldo di sezione
    		  bw.append(Formatta(null,"S",70," ")); // Nuovo filler
	    	  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
			  bw.append(Formatta(null,"D",15,"0"));
			  bw.append("P");
			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0"));
			  bw.append(Formatta(null,"S",4," "));
			  //fine saldo di sezione
			  //saldo finale 
			  bw.append(Formatta(tot1.movePointRight(2).toString(),"D",15,"0")); //?? congruente
			  bw.append("01010000"); //data versamento
			  bw.append(Formatta(null,"S",82," "));
			  bw.append("A");
	    	  //bw.newLine(); da problemi per il formato del file
	    	  bw.append("\r\n");
    	  }else
    		  tot_righe=tot_righe-1;
    		  
    	  //Coda
    	  bw.append("Z");
    	  bw.append(Formatta(null,"S",14," "));
    	  bw.append(Formatta(tot_righe.toString(),"D",9,"0"));// numero record tipo V
    	  bw.append(Formatta("1","D",9,"0"));// numero record tipo M
    	  bw.append(Formatta(null,"S",1864," "));
    	  bw.append("A");
    	  //bw.newLine(); da problemi per il formato del file
    	  bw.append("\r\n");

    	  //fine Coda
		  bw.flush();
	      bw.close();
	      osw.close();
	      os.close();	      
	      ///
		     setFile("/tmp/"+f.getName());	  
		     }else{
		    	  bw.flush();
			      bw.close();
			      osw.close();
			      os.close();	      
		    	 throw new ApplicationException("Non ci sono dati!");    	 
		     }    
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
public void SelezionaF24Prev(ActionContext context,boolean negativi) {
	try {
		List gruppi=getGruppi().getDetails();
		Gruppo_crBulk gruppo_cr=null;
		java.sql.Connection conn=null;
		Iterator it = gruppi.iterator();
		for (int i=0; i< gruppi.size();i++){
			Liquid_gruppo_coriBulk liquid=(Liquid_gruppo_coriBulk)it.next();
			conn= EJBCommonServices.getConnection(context); 
			try{
				it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
				gruppo_cr = new Gruppo_crBulk();
				gruppo_cr.setEsercizio(liquid.getEsercizio());
				gruppo_cr.setCd_gruppo_cr(liquid.getCd_gruppo_cr());
				gruppo_cr = (Gruppo_crBulk)homeCache.getHome(Gruppo_crBulk.class).findByPrimaryKey(gruppo_cr);
				if (gruppo_cr.getFl_f24online_previd().booleanValue())
					if (negativi)
						getGruppi().getSelection().setSelected(i);
					else
						if(liquid.getIm_liquidato().compareTo(BigDecimal.ZERO)>=0)
							getGruppi().getSelection().setSelected(i);
			  }
			 finally {
   				   if (conn!=null)
   					  try{conn.close();}catch( java.sql.SQLException e ){};
   			}
			 
		} 
		List liq = getGruppi().getDetails();
		if (!liq.isEmpty()) {
			OggettoBulk[] selezionati = new OggettoBulk[liq.size()];
			BitSet oldbit = new BitSet(liq.size());
			BitSet newbit = new BitSet(liq.size());
			Iterator iter = liq.iterator();
			for(int i=0;iter.hasNext();i++) {
				selezionati[i]=(OggettoBulk)iter.next();
				oldbit.clear(i);
				newbit.set(i);
			}
		}
	}
	 catch(Throwable e) {
	}
		 
}
public static CRUDLiquidazioneCORIBP getBusinessProcessFor(it.cnr.jada.action.ActionContext context, Liquid_coriBulk liq, String mode) throws it.cnr.jada.action.BusinessProcessException 
{
	if(liq == null) 
		return null;
	else
		return (CRUDLiquidazioneCORIBP)context.getUserInfo().createBusinessProcess(context,"CRUDLiquidazioneCORIBP",new Object[] { mode });
}
}
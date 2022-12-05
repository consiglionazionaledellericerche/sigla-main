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

package it.cnr.contab.compensi00.bp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.EcfBulk;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.ejb.BonusComponentSession;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;


public class CRUDBonusBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	public final static String SAVE_POINT_NAME = "BONUS_SP";
	private boolean isAbilitato;
	private String file;
	public CRUDBonusBP() {
		super("Tr");
	}

	public CRUDBonusBP(String s) {
		super(s+"Tr");
	}
	private CRUDBonusNucleoFamBP crudBonusNucleoFamBP = new CRUDBonusNucleoFamBP( "dettagliCRUDController", Bonus_nucleo_famBulk.class, "bonusNucleoFamColl", this){
	protected void validate(ActionContext context, OggettoBulk detail) throws ValidationException {
		validaDettaglio(context,(Bonus_nucleo_famBulk)detail,getModelIndex());
	}
	};
	public boolean isDeleteButtonEnabled() 
	{
		if (this.getModel() !=null && ((BonusBulk)this.getModel()).getFl_trasmesso()!=null &&((BonusBulk)this.getModel()).getFl_trasmesso())
			return false;
		else if (this.getModel() !=null && !((BonusBulk)this.getModel()).isModificabile())
			return false;
		else
			return super.isDeleteButtonEnabled();		
	}
	public boolean isSaveButtonEnabled() 
	{
		if ((this.getModel() !=null && ((BonusBulk)this.getModel()).getFl_trasmesso()!=null &&((BonusBulk)this.getModel()).getFl_trasmesso()))
			return false;
		else if ((this.getModel()!=null)&& ((BonusBulk)this.getModel()).isROBonus()&& this.isDirty())
			return true;
		else if ((this.getModel()!=null)&& ((BonusBulk)this.getModel()).isROBonus())
			return false;
		else
			return super.isSaveButtonEnabled();		
	}
	private void validaDettaglio(ActionContext context, Bonus_nucleo_famBulk detail,int index) throws ValidationException {
		
		if (detail.getCf_componente_nucleo()==null)
			throw new ValidationException("Inserire il codice fiscale del componente");
		if(detail.getIm_reddito_componente()==null)
			throw new ValidationException("Inserire reddito del componente");
		if(detail.getTipo_componente_nucleo()==null)
			throw new ValidationException("Inserire relazione di parentela del componente");
		if(detail.getCf_componente_nucleo()!=null && detail.getBonus()!=null && detail.getBonus().getDt_registrazione()!=null){
			if(detail.getBonus().getBonusNucleoFamColl().size()!=0)
			for(Integer indice=0;indice.compareTo(detail.getBonus().getBonusNucleoFamColl().size())<0;indice++){
				Bonus_nucleo_famBulk det=(Bonus_nucleo_famBulk)detail.getBonus().getBonusNucleoFamColl().get(indice);
				if(indice!=index && detail.getTipo_componente_nucleo().compareTo(det.getTipo_componente_nucleo())==0 && detail.getTipo_componente_nucleo().compareTo(Bonus_nucleo_famBulk.CONIUGE)==0)
					throw new ValidationException("Il coniuge è già stato inserito");
				if(indice!=index && detail.getCf_componente_nucleo().compareTo(det.getCf_componente_nucleo())==0)
					throw new ValidationException("Il codice fiscale del componente è già stato inserito");
			}			
		}	 
		if (detail.getTipo_componente_nucleo().compareTo(Bonus_nucleo_famBulk.CONIUGE)!=0){
			try{
				BonusComponentSession sess = (BonusComponentSession)createComponentSession();
				if(!sess.verificaLimiteFamiliareCarico(context.getUserContext(),detail)) 
					throw new ValidationException("Il componente non è a carico");
			} catch (BusinessProcessException e) {
				handleException(e);
			}catch (ComponentException e) {
				handleException(e);
			} catch (RemoteException e) {
				handleException(e);			
			}
		}
	}

	public void validaCodiceFiscale(ActionContext context, BonusBulk bonus) throws BusinessProcessException, ComponentException, RemoteException, ValidationException, SQLException {
		BonusComponentSession sess = (BonusComponentSession)createComponentSession();
		sess.checkCodiceFiscale(context.getUserContext(),bonus);
		if(isInserting())
			bonus=sess.recuperoDati(context.getUserContext(), bonus);
		setModel(context, bonus);		
	}

	public void validaCodiceFiscaleComponente(ActionContext context,
			Bonus_nucleo_famBulk bonus_nucleo_fam) throws ComponentException, RemoteException, BusinessProcessException, ValidationException, SQLException {
		BonusComponentSession sess = (BonusComponentSession)createComponentSession();
		sess.checkCodiceFiscaleComponente(context.getUserContext(),bonus_nucleo_fam);
	}

	public void eseguiCalcoloTot(ActionContext context, BonusBulk model) {
		BigDecimal tot=model.getIm_reddito();
		for(Iterator i=model.getBonusNucleoFamColl().iterator();i.hasNext();){
			Bonus_nucleo_famBulk det=(Bonus_nucleo_famBulk)i.next();
			if(det.getIm_reddito_componente()!=null)
			   tot=tot.add(det.getIm_reddito_componente());
		}
		model.setIm_reddito_nucleo_f(tot);
		
	}

	public CRUDBonusNucleoFamBP getCrudBonusNucleoFamBP() {
		return crudBonusNucleoFamBP;
	}

	public BonusBulk completaBonus(ActionContext context, BonusBulk bonus) throws ComponentException, RemoteException, BusinessProcessException {
		BonusComponentSession sess = (BonusComponentSession)createComponentSession();
		return sess.completaBonus(context.getUserContext(),bonus); 
	}

	public void validataTestata(ActionContext context, BonusBulk bonus) throws ApplicationException {
		if (bonus!=null && bonus.getCodice_fiscale()==null )
			throw new ApplicationException("Inserire codice fiscale richiedente");
		if (bonus!=null && bonus.getDt_registrazione()==null )
			throw new ApplicationException("Inserire data registrazione richiesta");
		if (bonus!=null && bonus.getDt_richiesta()==null )
			throw new ApplicationException("Inserire data richiesta");
		if (bonus!=null && bonus.getIm_reddito()==null )
			throw new ApplicationException("Inserire reddito richiedente");
		if (bonus!=null && bonus.getIm_reddito().compareTo(BigDecimal.ZERO)==0 )
			throw new ApplicationException("Il reddito del reddito richiedente non può essere 0");
		if (bonus!=null && bonus.getEsercizio_imposta()==null )
			throw new ApplicationException("Inserire esercizio imposta");
		if(bonus.getDt_richiesta().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
				throw new ApplicationException("Attenzione la data richiesta è superiore alla data odierna");
		if(bonus.getDt_registrazione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
				throw new ApplicationException("Attenzione la data registrazione è superiore alla data odierna");
		try {
			validaCodiceFiscale(context, bonus);
		} catch (BusinessProcessException e) {
			handleException(e);
		} catch (ComponentException e) {
			throw new ApplicationException(e);
		} catch (RemoteException e) {
			handleException(e);
		} catch (ValidationException e) {
			throw new ApplicationException(e);
		} catch (SQLException e) {
			handleException(e);
		}
	}

	public void setAbilitato(boolean isAbilitato) {
		this.isAbilitato = isAbilitato;
	}

	public boolean isAbilitato() {
		if((Bonus_nucleo_famBulk)this.getCrudBonusNucleoFamBP().getModel()!=null && 
			((Bonus_nucleo_famBulk)this.getCrudBonusNucleoFamBP().getModel()).getIm_reddito_componente()!=null &&
		  ((Bonus_nucleo_famBulk)this.getCrudBonusNucleoFamBP().getModel()).getIm_reddito_componente().compareTo(((BonusBulk)this.getModel()).getLimite())<=0)
	    	return true;
		else
			return false;
	}
	
	public BonusComponentSession createComponentSession() throws BusinessProcessException {
		return (BonusComponentSession)createComponentSession("CNRCOMPENSI00_EJB_BonusComponentSession", BonusComponentSession.class);
	}
	public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
		Button[] toolbar = getToolbar();
		if(getFile()!=null){
			//HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
			HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
		    StringBuffer stringbuffer = new StringBuffer();
		    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
		    toolbar[7].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
		}
		super.writeToolbar(pagecontext);
	}
	public boolean isScaricaButtonEnabled() {
		if(getFile()!=null)
			return true;
		else
			return false;
	}
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	public boolean  isConfermaButtonEnabled(){
		if(getFile()!=null)
			return true;
		else
			return false;
	}
	public void Estrazione(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException, PersistencyException{
		 try{	
	      AnagraficoBulk ente = ((Liquid_coriComponentSession)createComponentSession("CNRCORI00_EJB_Liquid_coriComponentSession", Liquid_coriComponentSession.class)).getAnagraficoEnte(context.getUserContext());
	      BonusComponentSession sess = (BonusComponentSession)createComponentSession();
	  	  java.util.List lista=sess.estraiLista(context.getUserContext());
	  	  File  f=null;
	  	  Integer conta_det=0;
	  	  Integer altro=0;
	  	  Integer mod=1;
	      String data_formattata=Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0").concat(
	    		 Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
	    				 EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR));
	      if(lista!=null && !lista.isEmpty() && lista.size()!=0)
	    	  f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/","Bonus-"+data_formattata+".rbs");
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
	    	  bw.append(Formatta("RBS09","S",5," "));
	    	  // Tipo fornitore 
	    	  bw.append("01");
	    	  bw.append(Formatta(Codice_Fiscale,"S",16," "));
	    	  
	    	  bw.append(Formatta(null,"S",483," "));
	    	  bw.append(Formatta(null,"S",4,"0"));
	    	  bw.append(Formatta(null,"S",4,"0")); 
	    	  
	    	  bw.append(Formatta(null,"S",100," ")); 
	    	  bw.append(Formatta(null,"S",1068," "));
	    	  bw.append(Formatta(null,"S",200," "));
	    	  
	    	  bw.append("A");
	    	  bw.append("\r\n");
	          // Fine Testata
	    	  // inizio Record B
	    	  for(Iterator i=lista.iterator();i.hasNext();){
	    		  BonusBulk bonus=(BonusBulk)i.next();
	    			bonus=sess.recuperoDati(context.getUserContext(), bonus);
		    	  //parte iniziale Fissa Tipo Record "B"
		    	  bw.append("B");
		    	  bw.append(Formatta(bonus.getCodice_fiscale(),"S",16," "));
		    	  
		    	  bw.append(Formatta("1","D",8,"0"));
		    	  // tipo modello
		    	  bw.append("1");
		    	  
		    	  bw.append(Formatta(null,"S",27," "));
		    	  bw.append(Formatta(null,"S",20," "));
		    	  bw.append(Formatta(null,"S",16," "));
		    	  // Flag anomalia a  NO 
		    	  bw.append("0");
		    	  // Cognome
		    	  bw.append(Formatta(bonus.getCognome(),"S",24," "));
		    	  // nome
		    	  bw.append(Formatta(bonus.getNome(),"S",20," "));
		    	  // sesso
		    	  bw.append(Formatta(bonus.getTerzo().getAnagrafico().getTi_sesso(),"S",1," "));
		    	  // data nascita
		    	  GregorianCalendar dataNascita = new GregorianCalendar();
		  		  dataNascita.setTime(new java.util.Date( bonus.getDt_nascita().getTime()));
		    	  String data_nasc_form =Formatta(new Integer(dataNascita.get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0").concat(
		    	    		 Formatta(new Integer(dataNascita.get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
		    	    		 dataNascita.get(java.util.Calendar.YEAR));
		    	  bw.append(Formatta(data_nasc_form,"S",8,"0"));
		    	  // comune
		    	  bw.append(Formatta(bonus.getTerzo().getAnagrafico().getComune_nascita().getDs_comune(),"S",40," "));
		    	  // provincia se null 'EE'
		    	  if(bonus.getTerzo().getAnagrafico().getComune_nascita().getTi_italiano_estero().compareTo(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk.COMUNE_ITALIANO)==0)
			    	  bw.append(Formatta(bonus.getTerzo().getAnagrafico().getComune_nascita().getCd_provincia(),"S",2," "));
		    	  else  
		    		  bw.append(Formatta("EE","S",2," "));
		    	  // sost.imp. 
		    	  bw.append(Formatta(null,"S",24," "));
		    	  bw.append(Formatta(null,"S",20," "));
		    	  bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
		    	  bw.append(Formatta(Codice_Fiscale,"S",16," "));
		    	 
		    	  //flag
		    	  if(bonus.getCd_condizione().compareTo("a")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("b")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("c")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("d")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("e")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("f")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  if(bonus.getCd_condizione().compareTo("g")==0)
		    		  bw.append("1");
		    	  else 
		    		  bw.append("0");
		    	  
		    	  if(bonus.getEsercizio_imposta().compareTo(new Integer("2007"))==0){
			    	  // anno 2007
		    		  bw.append("1");
			    	  // anno 2008
			    	  bw.append("0");
		    	  }else{
		    		  bw.append("0");
			    	  // anno 2008
			    	  bw.append("1");
		    	  }
		    	  //iban
		    	  bw.append(Formatta(null,"S",27," "));
		    	//data richiesta
		    	  GregorianCalendar dataRichiesta = new GregorianCalendar();
		    	  dataRichiesta.setTime(new java.util.Date( bonus.getDt_richiesta().getTime()));
		    	  String data_ric =Formatta(new Integer(dataRichiesta.get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0").concat(
		    	    		 Formatta(new Integer(dataRichiesta.get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
		    	    		 dataRichiesta.get(java.util.Calendar.YEAR));
		    	  bw.append(Formatta(data_ric,"S",8,"0"));
		    	  //firma
		    	  bw.append("1");
		    	  //intermediario
		    	  bw.append(Formatta(null,"S",16," "));
		    	  bw.append(Formatta(null,"S",5," "));
		    	  bw.append(Formatta(null,"S",8,"0"));
		    	  bw.append("0");
		    	  //flag anomalia
		    	  bw.append("0");
		    	  bw.append(Formatta(null,"S",1446," "));
		    	  //codice fiscale persona fisica che effettua l'invio
		    	  bw.append(Formatta(sess.recuperaCodiceFiscaleInvio(context.getUserContext()),"S",16," "));
	    	  
		    	  bw.append(Formatta(null,"S",20," "));
		    	  bw.append(Formatta(null,"S",34," "));
		    	  bw.append("A");
		    	  bw.append("\r\n");
		    	  // fine Record B
		    	  java.util.List det=sess.estraiDettagli(context.getUserContext(), bonus);
		    	  Integer prog=0;
		    	  mod=1;  
		    	  for(Iterator d=det.iterator();d.hasNext();){
		    		  Bonus_nucleo_famBulk dettaglio=(Bonus_nucleo_famBulk)d.next();
		    		  if(prog==5){
		    			  // totali
			    		  if(mod==1){
				    		  bw.append("BS007004");
				    		  bw.append(Formatta((bonus.getIm_reddito_nucleo_f().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
				    		  bw.append("BS008001");
				    		  bw.append(Formatta((bonus.getIm_bonus().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
			    		  }
			    		  completaRiga(bw,prog,altro,mod);
			    		  mod=mod+1;
			    		  bw.append(Formatta(null,"S",8," "));
			    		  bw.append("A");	   
			    		  bw.append("\r\n");
			    		  prog=0;
			    		  altro=0;
		    		  }
		    		  prog=prog+1;
			    	  // record tipo c
			    	  if(prog==1){
			    		  conta_det=conta_det+1;
			    		  bw.append("C");
				    	  bw.append(Formatta(bonus.getCodice_fiscale(),"S",16," "));
				    	  //progressivo modulo 
				    	  bw.append(Formatta(mod.toString(),"D",8,"0"));
				    	  bw.append(Formatta(null,"S",3," "));
				    	  bw.append(Formatta(null,"S",25," "));
				    	  bw.append(Formatta(null,"S",20," "));
				    	  bw.append(Formatta(null,"S",16," "));
				    	  if(mod==1){
				    		  bw.append("BS001004");
				    		  //?? arrotondamenti ???
				    		  bw.append(Formatta((bonus.getIm_reddito().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
				    	  }
			    	  }  
			    	  if(prog==1 && dettaglio.getTipo_componente_nucleo().compareTo(Bonus_nucleo_famBulk.CONIUGE)==0){
			    		  bw.append("BS002001");
			    		  bw.append(Formatta("1","D",16," "));
			    		  bw.append("BS002003");
			    		  bw.append(Formatta(dettaglio.getCf_componente_nucleo(),"S",16," "));
			    		  bw.append("BS002004");
			    		  bw.append(Formatta((dettaglio.getIm_reddito_componente().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
			    	  }
			    	  else if(prog==1 && dettaglio.getTipo_componente_nucleo().compareTo(Bonus_nucleo_famBulk.CONIUGE)!=0){
			    		  prog=prog+1;
			    		  altro=1;
			    	  }
			    	if(prog!=1||mod!=1){
			    	  if(prog<=5){
			    		  bw.append("BS00"+(prog+1)+"001");
			    		  bw.append(Formatta(dettaglio.getTipo_componente_nucleo(),"S",16," "));
			    		  bw.append("BS00"+(prog+1)+"003");
			    		  bw.append(Formatta(dettaglio.getCf_componente_nucleo(),"S",16," "));
			    		  bw.append("BS00"+(prog+1)+"004");
			    		  bw.append(Formatta((dettaglio.getIm_reddito_componente().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));			    		  
			    	  }
			    	}
			      
		    	  // fine record tipo c
		    	  }//end loop dettagli
		    	  // completo riga
		    	   // totali
		    	  if(mod==1){
		    		  bw.append("BS007004");
		    		  bw.append(Formatta((bonus.getIm_reddito_nucleo_f().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
		    		  bw.append("BS008001");
		    		  bw.append(Formatta((bonus.getIm_bonus().setScale(0,java.math.BigDecimal.ROUND_HALF_UP)).toString(),"D",16," "));
		    	  }  
	    		  completaRiga(bw,prog,altro,mod);
	    		  bw.append(Formatta(null,"S",8," "));
	    		  bw.append("A");	   
	    		  bw.append("\r\n");
	    		  prog=0;	  
	    		  altro=0;
	     }// end loop bonus
	    	  //record coda
	    	  bw.append("Z");
	    	  bw.append(Formatta(null,"S",14," "));
	    	  bw.append(Formatta(new Integer(lista.size()).toString(),"D",9,"0"));
	    	  bw.append(Formatta(conta_det.toString(),"D",9,"0"));
	    	  bw.append(Formatta(null,"S",1864," "));
	    	  
	    	  bw.append("A");	   
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
	    catch (SQLException e) {
			  throw new ApplicationException("Errore nel recupero dei dati");
		}
		catch (IllegalArgumentException e) {
			throw new ApplicationException("Formato file non valido!");
		}
		catch (IOException e) {
			throw new ApplicationException("Errore nella scrittura del file!");		
		}		  
	}
		private void completaRiga(BufferedWriter bw, Integer prog,Integer altro,Integer mod) throws IOException {
			BigDecimal progressivo;
		   if(mod==1)
			   progressivo=new BigDecimal(((prog-altro)*3)+3);
		   else
			   progressivo=new BigDecimal(((prog-altro)*3));
			while (progressivo.remainder(new BigDecimal("75")).compareTo(BigDecimal.ZERO)!=0){
			   bw.append(Formatta(null,"S",24," "));
			   progressivo=progressivo.add(BigDecimal.ONE);
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
		public it.cnr.jada.util.jsp.Button[] createToolbar() {

			it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[9];
			int i = 0;
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.estrai");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.download");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.conferma");
			return toolbar;
		}
		public void confermaInvio(ActionContext context) throws BusinessProcessException {
			try {
				BonusComponentSession sess = (BonusComponentSession)createComponentSession();
		  		sess.aggiornaInvio(context.getUserContext());
		  		commitUserTransaction();
			} catch (ComponentException e) {
				throw handleException(e);
			} catch (RemoteException e) {
				throw handleException(e);
			} catch (PersistencyException e) {
				throw handleException(e);
			}
		  	  
		}
		

} 

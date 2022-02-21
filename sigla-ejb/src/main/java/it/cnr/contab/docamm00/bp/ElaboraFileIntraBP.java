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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.EcfBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Bulk;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.contab.docamm00.ejb.ElaboraFileIntraComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

public class ElaboraFileIntraBP extends SimpleCRUDBP {
	
public ElaboraFileIntraBP() {
	super();
}
public ElaboraFileIntraBP(String function) {
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
public boolean isConfermaButtonHidden() {
	if (this.getModel()  instanceof VIntrastatBulk && getFile()!=null && getInvio() ) {
		return false;
	}
	else
		return true;
}
public boolean isElaboraFileInvioButtonHidden() {
	if (this.getModel()  instanceof VIntrastatBulk ) {
		return false;
	}
	else
		return true;
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.start");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.start_inv");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.download");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.conferma");
	
	return toolbar;
}

private String file;
private Boolean invio=new Boolean(false);
public Boolean getInvio() {
	return invio;
}
public void setInvio(Boolean invio) {
	this.invio = invio;
}
public void doElaboraFile(ActionContext context,VIntrastatBulk dett,Boolean invio) throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException {
	  try{
		  setInvio(invio);
		  dett.setNrProtocolloAcq(null);
		  dett.setNrProtocolloVen(null);
		  AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
	      AnagraficoBulk ente = sess.getAnagraficoEnte(context.getUserContext());
	      java.util.List lista=((ElaboraFileIntraComponentSession)createComponentSession()).EstraiLista(context.getUserContext(),getModel());
   		  it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRASTAT);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			
	    	if(config.getVal02()==null)
				 throw new ApplicationException("Codice utente abilitato non configurato");
	    	 File f;
	    if (invio){
	       f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
				   Formatta(config.getVal02(),"S",4," ")+//codice utente abilitato
				   Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0")+
				   Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0")+
				   ".I01");
	    }
	    else{
	    	 f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
					  "scambi.cee");
	    }
		  OutputStream os = (OutputStream)new FileOutputStream(f);
	      OutputStreamWriter osw = new OutputStreamWriter(os);
	      BufferedWriter bw = new BufferedWriter(osw);
	      
	     
	   
    if (!lista.isEmpty()){
    	java.util.List listaSezioneUnoAcquisti=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneUnoAcquisti(context.getUserContext(),getModel());
    	java.util.List listaSezioneDueAcquisti=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneDueAcquisti(context.getUserContext(),getModel());
    	java.util.List listaSezioneTreAcquisti=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneTreAcquisti(context.getUserContext(),getModel());
    	java.util.List listaSezioneQuattroAcquisti=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneQuattroAcquisti(context.getUserContext(),getModel());
    	java.util.List listaSezioneUnoVendite=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneUnoVendite(context.getUserContext(),getModel());
    	java.util.List listaSezioneDueVendite=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneDueVendite(context.getUserContext(),getModel());
    	java.util.List listaSezioneTreVendite=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneTreVendite(context.getUserContext(),getModel());
    	java.util.List listaSezioneQuattroVendite=((ElaboraFileIntraComponentSession)createComponentSession()).SezioneQuattroVendite(context.getUserContext(),getModel());
    	String P_iva=Formatta(ente.getPartita_iva(),"D",11,"0");
    	if(invio){
	    	//File invio
	    	bw.append(Formatta(config.getVal02(),"S",4," ")); //codice utente abilitato
			
	    	bw.append(Formatta(null,"S",12," "));//riservata SDA
	    	//Nome del flusso
	    	bw.append(Formatta(config.getVal02(),"S",4," ")); //codice utente abilitato
	    	bw.append(Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1).toString(),"D",2,"0"));
	    	bw.append(Formatta(new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
	    	bw.append(".");// nome file
	    	bw.append("I");// tipo file Dichiarazioni Intrastat
	    	bw.append("01");//progressivo dell'interchange
	    	//fine del nome del flusso
	    	bw.append(Formatta(null,"S",12," "));//riservata SDA
			if(config.getVal01()==null)
				 throw new ApplicationException("Codice sezione doganale non configurato");
			bw.append(Formatta(config.getVal01(),"D",6,"0"));
			bw.append(Formatta(null,"S",4," "));//riservata SDA
			bw.append(Formatta(P_iva,"S",16," "));
			if(config.getVal03()==null)
				 throw new ApplicationException("Progressivo sede utente abilitato non configurato");
			bw.append(Formatta(config.getVal03(),"D",3,"0")); //progressivo sede
			bw.append(Formatta(null,"S",1," "));//riservata SDA
			Integer cont=((listaSezioneUnoAcquisti.size()+listaSezioneDueAcquisti.size()+listaSezioneTreAcquisti.size()+listaSezioneQuattroAcquisti.size())==new Integer(0)?0:1 )+
				    ((listaSezioneUnoVendite.size()+listaSezioneDueVendite.size()+listaSezioneTreVendite.size()+listaSezioneQuattroVendite.size())==new Integer(0)?0:1 )+
				    +listaSezioneUnoAcquisti.size()+listaSezioneDueAcquisti.size()+listaSezioneTreAcquisti.size()+listaSezioneQuattroAcquisti.size()
					+listaSezioneUnoVendite.size()+listaSezioneDueVendite.size()+listaSezioneTreVendite.size()+listaSezioneQuattroVendite.size()+1;
			bw.append(Formatta(cont.toString(),"D",5,"0")); //conteggio totale + frontespizi + testata
			bw.append("\r\n"); 
    	}
    	//parte iniziale fissa
    	Integer prot=0;
    	if(!listaSezioneUnoAcquisti.isEmpty()||
    	    	   !listaSezioneDueAcquisti.isEmpty()||
    	    	   !listaSezioneTreAcquisti.isEmpty()||
    	    	   !listaSezioneQuattroAcquisti.isEmpty()){
	    	bw.append(new String("EUROX"));
	    	bw.append(P_iva);
	    	prot=config.getIm01().intValue()+1;
	    	bw.append(Formatta(prot.toString(),"D",6,"0"));
	    	bw.append("0");// Tipo record Frontespizio
	    	bw.append(Formatta("0","D",5,"0"));// progressivo riga di dettaglio per frontespizio 0
	    	
    		bw.append("A");// Tipo frontespizio Acquisti
	    	bw.append(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString().substring(2));// Tipo frontespizio Acquisti
	    	bw.append("M");// Periodicità mensile
	    	bw.append(Formatta(dett.getMese().toString(),"D",2,"0"));
	    	bw.append(P_iva);
	    	bw.append("0");//0=operazione sono riferite al mese o trimestre completo 9(1)
	    	bw.append("0");//0=nessun caso particolare                               9(1)
	    	bw.append(Formatta(null,"D",11,"0"));  // partita iva delegato
	    	Integer conta_det=0;
	    	java.math.BigInteger somma_det=new java.math.BigInteger("0");
	    	if(!listaSezioneUnoAcquisti.isEmpty()){
	    		conta_det=listaSezioneUnoAcquisti.size();
	    		for (Iterator i=listaSezioneUnoAcquisti.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez1 acq	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez1 acq
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	if(!listaSezioneDueAcquisti.isEmpty()){
	    		conta_det=listaSezioneDueAcquisti.size();
	    		for (Iterator i=listaSezioneDueAcquisti.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			if(det.getPgStorico() !=null && det.getNrProtocollo() !=null)
	    				somma_det=somma_det.subtract(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    			else
	    				somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez2 acq	
	    	
	    	// caso particolare
	    	if (somma_det.compareTo(BigInteger.ZERO)>=0)
	    		bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez2 acq
	    	else
	    	{
	    		switch (new Integer(somma_det.toString().substring(somma_det.toString().length()-1,somma_det.toString().length())).intValue()) {
				case 0:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("p"),"D",13,"0").toLowerCase());
					break;
				case 1:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("q"),"D",13,"0").toLowerCase());
					break;
				case 2:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("r"),"D",13,"0").toLowerCase());
					break;	
				case 3:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("s"),"D",13,"0").toLowerCase());
					break;
				case 4:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("t"),"D",13,"0").toLowerCase());
					break;
				case 5:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("u"),"D",13,"0").toLowerCase());
					break;
				case 6:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("v"),"D",13,"0").toLowerCase());
					break;
				case 7:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("w"),"D",13,"0").toLowerCase());
					break;
				case 8:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("x"),"D",13,"0").toLowerCase());
					break;
				case 9:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("y"),"D",13,"0").toLowerCase());
					break;
				default:
					break;
				}
	    	}
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	
	    	if(!listaSezioneTreAcquisti.isEmpty()){
	    		conta_det=listaSezioneTreAcquisti.size();
	    		for (Iterator i=listaSezioneTreAcquisti.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez3 acq	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez3 acq
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	
	    	if(!listaSezioneQuattroAcquisti.isEmpty()){
	    		conta_det=listaSezioneQuattroAcquisti.size();
	    		for (Iterator i=listaSezioneQuattroAcquisti.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.subtract(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez4 acq	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez4 acq
	    	bw.append("\r\n");
    	}
    	Integer conta=0;
    	if(!listaSezioneUnoAcquisti.isEmpty()){
    		for (Iterator i=listaSezioneUnoAcquisti.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("1");// Sezione Uno Acquisti
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione fornitore
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(new BigDecimal(det.getAmmontareDivisa()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(det.getCdNaturaTransazione(),"S",1," "));
    			bw.append(Formatta(det.getCdNomenclaturaCombinata(),"D",8,"0"));
    			bw.append(Formatta(new BigDecimal(det.getMassaNetta()).abs().toString(),"D",10,"0"));
    			bw.append(Formatta(new BigDecimal(det.getUnitaSupplementari()).abs().toString(),"D",10,"0"));
    			bw.append(Formatta(new BigDecimal(det.getValoreStatistico()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(det.getCdConsegna(),"S",1," "));
    			bw.append(Formatta(det.getCdModalitaTrasporto(),"D",1,"0"));
    			bw.append(Formatta(det.getProvenienza(),"S",2," "));
    			bw.append(Formatta(det.getOrigine(),"S",2," "));
    			bw.append(Formatta(det.getCdProvinciaDestinazione(),"S",2," "));
				bw.append(Formatta("","S",1," ")); // Campo aggiunto per sdoppiamento colonna natura della transazione
    			bw.append("\r\n");
    		}
    	}
    	conta=0;
    	if(!listaSezioneDueAcquisti.isEmpty()){
    		for (Iterator i=listaSezioneDueAcquisti.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("2");// Sezione Due Acquisti
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			
    			// DA VERIFICARE
    			bw.append(Formatta(det.getMese().toString(),"D",2,"0"));//mese
    			//Calcolo trimestre non serve 
    			/*BigDecimal resto =new BigDecimal(det.getMese().toString()).remainder(new BigDecimal("3"));
    			BigInteger trim_i =new BigInteger(det.getMese().toString()).divide(new BigInteger("3"));
    			if(resto.compareTo(BigDecimal.ZERO)==0)
    				bw.append(trim_i.toString());//trimestre
    			else
    				bw.append(trim_i.add(new BigInteger("1")).toString());//trimestre 
    			*/
    			bw.append("0");//trimestre
    			bw.append(det.getEsercizio().toString().substring(2));// 
    			// Fine da verificare
    			
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione fornitore
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			/*if (new BigDecimal(det.getAmmontareEuro()).abs().compareTo(new BigDecimal(det.getAmmontareEuro()))==0)
				bw.append("+");
				else
					bw.append("-");*/
				if(det.getPgStorico() !=null && det.getNrProtocollo() !=null)
					bw.append("-");
				else
					bw.append("+");
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(new BigDecimal(det.getAmmontareDivisa()).abs().toString(),"D",13,"0"));
    			
    			bw.append(Formatta(det.getCdNaturaTransazione(),"S",1," "));
    			bw.append(Formatta(det.getCdNomenclaturaCombinata(),"D",8,"0"));
    			bw.append(Formatta(new BigDecimal(det.getValoreStatistico()).abs().toString(),"D",13,"0"));
    			bw.append("\r\n");
    		}
    	}
    	conta=0;
    	if(!listaSezioneTreAcquisti.isEmpty()){
    		for (Iterator i=listaSezioneTreAcquisti.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("3");// Sezione Tre Acquisti
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			
    			
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione fornitore
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(new BigDecimal(det.getAmmontareDivisa()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(null,"S",15," "));//bw.append(Formatta(det.getNrFattura(),"S",15," "));
    			bw.append(Formatta(null,"S",6," "));//bw.append(Formatta(det.getDtFattura(),"S",6," "));
    			bw.append(Formatta(det.getCdCpa(),"D",5,"0"));
    			bw.append(Formatta(null,"S",1,"0"));//diminuzione 1 carattere codice cpa
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaErogazione(),"S",1," "));
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaIncasso(),"S",1," "));
    			bw.append(Formatta(det.getProvenienza(),"S",2," "));
    			
    			bw.append("\r\n");
    		}
    	}
    	
    	conta=0;
    	if(!listaSezioneQuattroAcquisti.isEmpty()){
    		for (Iterator i=listaSezioneQuattroAcquisti.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("4");// Sezione Quattro Acquisti
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			//???????????????????????????????????????????????????????
    			
    			if(config.getVal01()==null)
    				 throw new ApplicationException("Codice sezione doganale non configurato");
    			bw.append(Formatta(config.getVal01(),"D",6,"0"));
    			bw.append(det.getEsercizio().toString().substring(2));// anno elenco rettifica
    			
    			if(det.getNrProtocollo()!=null)
    				bw.append(Formatta(det.getNrProtocollo().toString(),"D",6,"0"));  // protocollo elenco rettifica
    			else
    				bw.append(Formatta("0","D",6,"0"));  // protocollo elenco rettifica
    			if(det.getNrProgressivo()!=null)
    				bw.append(Formatta(det.getNrProgressivo().toString(),"D",5,"0"));  // progressivo della sezione 3 da rettificare
    			else
    				bw.append(Formatta("0","D",5,"0"));  // progressivo della sezione 3 da rettificare
    			//???????????????????????????????????????????????????????
    			
    			//per la cancellazione non devono essere valorizzati questi campi
    			bw.append(Formatta(null,"S",2," "));//nazione fornitore //bw.append(Formatta(det.getNazFiscale(),"S",2," "));
    			bw.append(Formatta(null,"S",12," "));//bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(null,"D",13,"0"));//bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(null,"D",13,"0"));//bw.append(Formatta(new BigDecimal(det.getAmmontareDivisa()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(null,"S",15," "));//bw.append(Formatta(det.getNrFattura(),"S",15," "));
    			bw.append(Formatta(null,"S",6," "));//bw.append(Formatta(det.getDtFattura(),"S",6," "));
    			bw.append(Formatta(null,"D",5,"0"));
    			bw.append(Formatta(null,"S",1,"0"));//diminuzione 1 carattere codice cpa
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaErogazione(),"S",1," "));
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaIncasso(),"S",1," "));
    			bw.append(Formatta(null,"S",2," "));//bw.append(Formatta(det.getProvenienza(),"S",2," "));

    			bw.append("\r\n");
    		}
    	}
    	
    	// VENDITE
    	if(!listaSezioneUnoVendite.isEmpty()||
    	   !listaSezioneDueVendite.isEmpty()||
    	   !listaSezioneTreVendite.isEmpty()||
    	   !listaSezioneQuattroVendite.isEmpty()){
    		if(prot!=0)
        		prot=prot+1;
        	else
        		prot=config.getIm01().intValue()+1;
         	//parte iniziale fissa
    		bw.append(new String("EUROX"));
        	bw.append(P_iva);
        	bw.append(Formatta(prot.toString(),"D",6,"0"));  // Progressivo invio
        	bw.append("0");// Tipo record Frontespizio
        	bw.append(Formatta("0","D",5,"0"));// progressivo riga di dettaglio per frontespizio 0
        	
    		
	    	bw.append("C");// Tipo frontespizio Cessioni
	    	bw.append(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString().substring(2));// Tipo frontespizio VEndite
	    	bw.append("M");// Periodicità mensile
	    	bw.append(Formatta(dett.getMese().toString(),"D",2,"0"));
	    	bw.append(P_iva);
	    	bw.append("0");//0=operazione sono riferite al mese o trimestre completo 9(1)
	    	bw.append("0");//0=nessun caso particolare                               9(1)
	    	bw.append(Formatta(null,"S",11,"0"));  // partita iva delegato
	    	Integer conta_det=0;
	    	java.math.BigInteger somma_det=new java.math.BigInteger("0");
	    	if(!listaSezioneUnoVendite.isEmpty()){
	    		conta_det=listaSezioneUnoVendite.size();
	    		for (Iterator i=listaSezioneUnoVendite.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez1 ven	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez1 ven
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	if(!listaSezioneDueVendite.isEmpty()){
	    		conta_det=listaSezioneDueVendite.size();
	    		for (Iterator i=listaSezioneDueVendite.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			if(det.getPgStorico() !=null && det.getNrProtocollo() !=null)
	    				somma_det=somma_det.subtract(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    			else
	    				somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez2 ven	
	    	
	    	// caso particolare
	    	if (somma_det.compareTo(BigInteger.ZERO)>=0)
	    		bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez2 ven
	    	else
	    	{
	    		switch (new Integer(somma_det.toString().substring(somma_det.toString().length()-1,somma_det.toString().length())).intValue()) {
				case 0:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("p"),"D",13,"0").toLowerCase());
					break;
				case 1:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("q"),"D",13,"0").toLowerCase());
					break;
				case 2:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("r"),"D",13,"0").toLowerCase());
					break;	
				case 3:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("s"),"D",13,"0").toLowerCase());
					break;
				case 4:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("t"),"D",13,"0").toLowerCase());
					break;
				case 5:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("u"),"D",13,"0").toLowerCase());
					break;
				case 6:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("v"),"D",13,"0").toLowerCase());
					break;
				case 7:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("w"),"D",13,"0").toLowerCase());
					break;
				case 8:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("x"),"D",13,"0").toLowerCase());
					break;
				case 9:
					bw.append(Formatta(somma_det.abs().toString().substring(0,somma_det.abs().toString().length()-1).concat("y"),"D",13,"0").toLowerCase());
					break;
				default:
					break;
				}
	    	}
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	
	    	if(!listaSezioneTreVendite.isEmpty()){
	    		conta_det=listaSezioneTreVendite.size();
	    		for (Iterator i=listaSezioneTreVendite.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez3 ven	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez3 ven
	    	somma_det=new java.math.BigInteger("0");
	    	conta_det=0;
	    	
	    	if(!listaSezioneQuattroVendite.isEmpty()){
	    		conta_det=listaSezioneQuattroVendite.size();
	    		for (Iterator i=listaSezioneQuattroVendite.iterator();i.hasNext();){
	    			VIntrastatBulk det=(VIntrastatBulk)i.next();
	    			somma_det=somma_det.add(new java.math.BigInteger(det.getAmmontareEuro().toString()));
	    		}
	    	}
	    	bw.append(Formatta(conta_det.toString(),"D",5,"0"));  // numero det sez4 ven	
	    	bw.append(Formatta(somma_det.toString(),"D",13,"0"));  // somma_det sez4 ven
			bw.append(Formatta("0","D",5,"0"));  // Numero di righe dettaglio della sezione 5
	    	bw.append("\r\n");
    	}
    	conta=0;
    	if(!listaSezioneUnoVendite.isEmpty()){
    		for (Iterator i=listaSezioneUnoVendite.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("1");// Sezione Uno 
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione cliente
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(det.getCdNaturaTransazione(),"S",1," "));
    			bw.append(Formatta(det.getCdNomenclaturaCombinata(),"D",8,"0"));
    			bw.append(Formatta(new BigDecimal(det.getMassaNetta()).abs().toString(),"D",10,"0"));
    			bw.append(Formatta(new BigDecimal(det.getUnitaSupplementari()).abs().toString(),"D",10,"0"));
    			bw.append(Formatta(new BigDecimal(det.getValoreStatistico()).abs().toString(),"D",13,"0"));
    			bw.append(Formatta(det.getCdConsegna(),"S",1," "));
    			bw.append(Formatta(det.getCdModalitaTrasporto(),"D",1,"0"));
    			bw.append(Formatta(det.getDest(),"S",2," "));
    			bw.append(Formatta(det.getCdProvinciaOrigine(),"S",2," "));
				bw.append(Formatta("","S",1," ")); // Campo aggiunto per sdoppiamento colonna natura della transazione
				bw.append(Formatta("IT","S",2," ")); // Campo aggiunto. Codice del paese di origine della merce
    			bw.append("\r\n");
    		}
    	}
    	conta=0;
    	if(!listaSezioneDueVendite.isEmpty()){
    		for (Iterator i=listaSezioneDueVendite.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("2");// Sezione Due 
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			
    			// DA VERIFICARE
    			bw.append(Formatta(det.getMese().toString(),"D",2,"0"));//mese
    			//Calcolo trimestre non serve 
    			/*BigDecimal resto =new BigDecimal(det.getMese().toString()).remainder(new BigDecimal("3"));
    			BigInteger trim_i =new BigInteger(det.getMese().toString()).divide(new BigInteger("3"));
    			if(resto.compareTo(BigDecimal.ZERO)==0)
    				bw.append(trim_i.toString());//trimestre
    			else
    				bw.append(trim_i.add(new BigInteger("1")).toString());//trimestre 
    			*/
    			bw.append("0");//trimestre
    			bw.append(det.getEsercizio().toString().substring(2));
    			// Fine da verificare
    			
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione cliente
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			/*if (new BigDecimal(det.getAmmontareEuro()).abs().compareTo(new BigDecimal(det.getAmmontareEuro()))==0)
    				bw.append("+");
    			else
    				bw.append("-");*/
    			if(det.getPgStorico() !=null && det.getNrProtocollo() !=null)
    				bw.append("-");
    			else
    				bw.append("+");
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(det.getCdNaturaTransazione(),"S",1," "));
    			bw.append(Formatta(det.getCdNomenclaturaCombinata(),"D",8,"0"));
    			bw.append(Formatta(new BigDecimal(det.getValoreStatistico()).abs().toString(),"D",13,"0"));
    			bw.append("\r\n");
    		}
    	}
    	conta=0;
    	if(!listaSezioneTreVendite.isEmpty()){
    		for (Iterator i=listaSezioneTreVendite.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("3");// Sezione Tre 
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			bw.append(Formatta(det.getNazFiscale(),"S",2," "));//nazione cliente
    			bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(null,"S",15," "));//bw.append(Formatta(det.getNrFattura(),"S",15," "));
    			bw.append(Formatta(null,"S",6," "));//bw.append(Formatta(det.getDtFattura(),"S",6," "));
    			bw.append(Formatta(det.getCdCpa(),"D",5,"0"));
    			bw.append(Formatta(null,"S",1,"0"));//diminuzione 1 carattere codice cpa
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaErogazione(),"S",1," "));
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaIncasso(),"S",1," "));
    			bw.append(Formatta(det.getDest(),"S",2," "));
    			
    			bw.append("\r\n");
    		}
    	}
    	conta=0;
    	if(!listaSezioneQuattroVendite.isEmpty()){
    		for (Iterator i=listaSezioneQuattroVendite.iterator();i.hasNext();){
    			conta=conta+1;
    			VIntrastatBulk det=(VIntrastatBulk)i.next();
    			//	parte fissa
    			bw.append(new String("EUROX"));
    			bw.append(P_iva);
    			bw.append(Formatta(prot.toString(),"D",6,"0"));
    			bw.append("4");// Sezione Quattro 
    			bw.append(Formatta(conta.toString(),"D",5,"0"));// numero progressivo
    			// fine parte fissa
    			//???????????????????????????????????????????????????????
    			if(config.getVal01()==null)
   				 throw new ApplicationException("Codice sezione doganale non configurato");
    			bw.append(Formatta(config.getVal01(),"D",6,"0"));
    			bw.append(det.getEsercizio().toString().substring(2));// anno elenco rettifica
    			bw.append(Formatta(det.getNrProtocollo().toString(),"D",6,"0"));  // protocollo elenco rettifica
    			bw.append(Formatta(det.getNrProgressivo().toString(),"D",5,"0"));  // progressivo della sezione 3 da rettificare
    				//???????????????????????????????????????????????????????
    			//per la cancellazione non devono essere valorizzati questi campi
    			bw.append(Formatta(null,"S",2," "));//nazione fornitore //bw.append(Formatta(det.getNazFiscale(),"S",2," "));
    			bw.append(Formatta(null,"S",12," "));//bw.append(Formatta(det.getPartitaIva(),"S",12," "));
    			bw.append(Formatta(null,"D",13,"0"));//bw.append(Formatta(new BigDecimal(det.getAmmontareEuro()).abs().toString(),"D",13,"0"));  
    			bw.append(Formatta(null,"S",15," "));//bw.append(Formatta(det.getNrFattura(),"S",15," "));
    			bw.append(Formatta(null,"S",6," "));//bw.append(Formatta(det.getDtFattura(),"S",6," "));
    			bw.append(Formatta(det.getCdCpa(),"D",5,"0")); 
    			bw.append(Formatta(null,"S",1,"0"));//diminuzione 1 carattere codice cpa
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaErogazione(),"S",1," "));
    			bw.append(Formatta(null,"S",1," "));//bw.append(Formatta(det.getCdModalitaIncasso(),"S",1," "));
    			bw.append(Formatta(null,"S",2," "));//bw.append(Formatta(det.getDest(),"S",2," "));
    			
    			bw.append("\r\n");
    		}
    	}
    	
	      bw.flush();
	      bw.close();
	      osw.close();
	      os.close();	      
	      dett.setNrProtocollo(prot.toString());
	      setFile("/tmp/"+f.getName());	  
     }else{
	    	  bw.flush();
		      bw.close();
		      osw.close();
		      os.close();	      
	    	 throw new ApplicationException("Non ci sono dati da elaborare per il mese selezionato!");    	 
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

public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
Button[] toolbar = getToolbar();
if(getFile()!=null){
	HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
	HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
    StringBuffer stringbuffer = new StringBuffer();
    stringbuffer.append(pagecontext.getRequest().getScheme());
    stringbuffer.append("://");
    stringbuffer.append(pagecontext.getRequest().getServerName());
    stringbuffer.append(':');
    stringbuffer.append(pagecontext.getRequest().getServerPort());
    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
    toolbar[2].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
}
super.writeToolbar(pagecontext);
}
public String getFile() {
return file;
}
public void setFile(String file) {
this.file = file;
}
public void doElaboraFile(ActionContext context, VIntra12Bulk dett)throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException {
	 try{			  
		  
		  File f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
				  "INTRASTAT"
				  +EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)+
				  +new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1)+
				  +EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR)+"intra12.cee");
		  OutputStream os = (OutputStream)new FileOutputStream(f);
	      OutputStreamWriter osw = new OutputStreamWriter(os);
	      BufferedWriter bw = new BufferedWriter(osw);
	      
	      AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
	      AnagraficoBulk ente = sess.getAnagraficoEnte(context.getUserContext());
	      java.util.List lista=((ElaboraFileIntraComponentSession)createComponentSession()).EstraiListaIntra12(context.getUserContext(),getModel());
	      AnagraficoBulk resp=null; 
   if (!lista.isEmpty()){
	   it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRA_12);
			resp = (AnagraficoBulk)sess.findByPrimaryKey(context.getUserContext(), new AnagraficoBulk(new Integer(config.getVal03())));
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
	   BigDecimal totImpIntra=new BigDecimal("0");
	   BigDecimal totIvaIntra=new BigDecimal("0");
	   
	   BigDecimal totImpBeniIntra=new BigDecimal("0");
	   BigDecimal totIvaBeniIntra=new BigDecimal("0");
	   
	   BigDecimal totImpServIntra=new BigDecimal("0");
	   BigDecimal totIvaServIntra=new BigDecimal("0");
	   
	   BigDecimal totImpS30Intra=new BigDecimal("0");
	   
	   BigDecimal totImpServExtra=new BigDecimal("0");
	   BigDecimal totIvaServExtra=new BigDecimal("0");
	   
	   BigDecimal totImpS30Extra=new BigDecimal("0");
	   
	   BigDecimal totImpBeniExtra=new BigDecimal("0");
	   BigDecimal totIvaBeniExtra=new BigDecimal("0");
	   
	   for (Iterator i = lista.iterator(); i.hasNext();) {
		   VIntra12Bulk d=(VIntra12Bulk)i.next();
		   if(d.getFlIntraUe()){
			   totImpIntra=totImpIntra.add(d.getImponibile());
			   totIvaIntra=totIvaIntra.add(d.getIva());
		   
			   if(d.getCdBeneServizio().compareTo("BENI")==0){
				   	totImpBeniIntra=totImpBeniIntra.add(d.getImponibile());
		   	   		totIvaBeniIntra=totIvaBeniIntra.add(d.getIva());
		   		}
		   		else if(d.getCdBeneServizio().compareTo("S030")==0) {
		   			totImpS30Intra=totImpS30Intra.add(d.getImponibile());
		   		
		   			totImpServIntra=totImpServIntra.add(d.getImponibile());
		   	   		totIvaServIntra=totIvaServIntra.add(d.getIva());
		   		}else{
		   			totImpServIntra=totImpServIntra.add(d.getImponibile());
		   	   		totIvaServIntra=totIvaServIntra.add(d.getIva());
		   		}   			
	   }else    if(d.getFlExtraUe()){
		   			if(d.getCdBeneServizio().compareTo("S030")==0) {
			   			totImpS30Extra=totImpS30Extra.add(d.getImponibile());
			   		
			   			totImpServExtra=totImpServExtra.add(d.getImponibile());
			   	   		totIvaServExtra=totIvaServExtra.add(d.getIva());
			   		}else if (d.getCdBeneServizio().compareTo("BENI")!=0){
			   			totImpServExtra=totImpServExtra.add(d.getImponibile());
			   	   		totIvaServExtra=totIvaServExtra.add(d.getIva());
			   		} else if(d.getCdBeneServizio().compareTo("BENI")==0){
			   			totImpBeniExtra=totImpBeniExtra.add(d.getImponibile());
			   	   		totIvaBeniExtra=totIvaBeniExtra.add(d.getIva());
			   		}
	   		}
	   }
	   // Tipo Record A Testata
	   bw.append("A"); // tipo record
	   bw.append(Formatta(null,"S",14," "));// Filler
	   bw.append("T1210"); // Codice fornitura
	   bw.append("01"); // Soggetti che inviano proprie dichiarazioni
	   String Codice_Fiscale =ente.getCodice_fiscale();
	   String P_iva =ente.getPartita_iva();
	   if (Codice_Fiscale==null)
			    throw new ApplicationException("Codice Fiscale non valorizzati per l'ente!");
	   bw.append(Formatta(Codice_Fiscale,"S",16," "));// Codice fiscale
	   bw.append(Formatta(null,"S",483," "));// Filler
	   bw.append(Formatta(null,"S",4,"0")); //Filler
 	   bw.append(Formatta(null,"S",4,"0")); //Filler 
 	   bw.append(Formatta(null,"S",100," "));// Filler campo utente
 	   bw.append(Formatta(null,"S",1068," "));// Filler
 	   bw.append(Formatta(null,"S",200," "));// Filler 
 	   bw.append("A"); //
 	   bw.append("\r\n");
 	   // fine record A Testata

 	   
	   // Tipo Record B Frontespizio
	   bw.append("B"); // tipo record
	   bw.append(Formatta(Codice_Fiscale,"S",16," "));// Codice fiscale
	   bw.append(Formatta("1","D",8,"0"));// Progressivo modulo (VALE 1)
	   bw.append(Formatta(null,"S",3," "));// Filler campo utente
	   bw.append(Formatta(null,"S",25," "));// Filler
	   bw.append(Formatta(null,"S",20," "));// Filler campo utente x identificazione dichiarazione
	   
	   bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));// Codice fiscale produttore software (PRESIDENTE)
	   bw.append("1"); // Vale 0 o 1(dichiarazione confermata)
	   bw.append(Formatta(config.getVal01(),"S",3," "));// Ufficio Compentente 
	   bw.append(Formatta(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString(),"S",4," "));// Esercizio
	   bw.append(Formatta(((VIntra12Bulk)getModel()).getMese().toString(),"D",2,"0"));// Mese
	   bw.append(Formatta("1","D",1,"0"));// Correttiva nei termini?? checkbox
	   bw.append(Formatta(P_iva,"D",11,"0"));//  P_iva
	   bw.append(Formatta(config.getVal02(),"S",6," "));//  CODICE ATTIVITA ???
	   bw.append(Formatta("0","D",1,"0"));// Eventi eccezionali checkbox
	   
	   bw.append(Formatta(null,"S",24," "));//  Cognome
	   bw.append(Formatta(null,"S",20," "));//  Nome
	   bw.append(Formatta(null,"S",40," "));//  Comune nascita
	   bw.append(Formatta(null,"S",2," "));// prov. nascita
	   bw.append(Formatta(null,"D",8,"0"));// dt. nascita
	   bw.append(Formatta(null,"D",1," "));// sesso
	   // Obbligatori se non è Persona Fisica  
 	   bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
	   bw.append(Formatta(config.getIm01().toString(),"D",2,"0"));// NATURA GIURIDICA ????????? messo 1 PERCHè OBBLIGATORIO
	   bw.append(Formatta(null,"S",40," "));// Filler
	   bw.append(Formatta(null,"S",2," "));// Filler
	   bw.append(Formatta(null,"S",35," "));// Filler
	   bw.append(Formatta(null,"S",5," "));// Filler
	   bw.append(Formatta(null,"S",4," "));// Filler
	   
	   bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));//codice fiscale rappresentante
	   bw.append(Formatta(config.getIm02().toString(),"D",2,"0"));//carica rappresentante  ??????? messo 2 PERCHè OBBLIGATORIO
	   bw.append(Formatta(Codice_Fiscale,"S",11," "));//codice fiscale societa dichiarante
	   
	   bw.append(Formatta(resp.getCognome(),"S",24," "));//cognome rappresentante     ???????????????
	   bw.append(Formatta(resp.getNome(),"S",20," "));//nome rappresentante     ???????????????
	   bw.append(resp.getTi_sesso());//sesso rappresentante     ???????????????
	   GregorianCalendar dataNascita = new GregorianCalendar();
	   dataNascita.setTime(new Date(resp.getDt_nascita().getTime()));
	   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
	   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.MONTH)+1).toString(),"D",2,"0"));
	   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.YEAR)).toString(),"D",4,"0"));
	   ComuneBulk comune = (ComuneBulk)sess.findByPrimaryKey(context.getUserContext(), new ComuneBulk(resp.getComune_nascita().getPg_comune()));
	   if(comune==null)
		   throw new ApplicationException("Dati anagrafici del rappresentante non completi!");     
	   else
	   {
		   bw.append(Formatta(comune.getDs_comune(),"S",40," "));//COMUNE nascita rappresentante     ???????????????
		   bw.append(Formatta(comune.getCd_provincia(),"S",2," "));//PROV nascita rappresentante     ???????????????
	   }
	   //cambiato il tracciato dal 01/10/2015 diventati tutti filler
	   bw.append(Formatta(null,"S",3," "));//codice stato estero rappresentante     ???????????????
	   //bw.append(Formatta(null,"D",3,"0"));//codice stato estero rappresentante     ???????????????
	   bw.append(Formatta(null,"S",24," "));//stato fed rappresentante     ???????????????
	   bw.append(Formatta(null,"S",24," "));//residenza rappresentante     ???????????????
	   bw.append(Formatta(null,"S",35," "));//indirizzo estero rappresentante     ???????????????
	   bw.append(Formatta(null,"S",12," "));//cell rappresentante     ???????????????
	   
	   bw.append(Formatta("1","D",1,"0"));// Firma checkbox
	   
	   // Sezione intermediario
	   bw.append(Formatta(null,"S",16," ")); 
 	   bw.append(Formatta(null,"S",5,"0"));  
 	   bw.append(Formatta("0","D",1,"0"));//
 	   // cambiato il tracciato dal 01/10/2015
 	   bw.append(Formatta(null,"S",1," "));
 	   // fine cambio
 	   bw.append(Formatta(null,"D",8,"0"));
 	   bw.append(Formatta("0","D",1,"0"));// Firma intermediario
 	  
 	   
 	   //bw.append(Formatta(null,"S",1228," "));// Filler -- cambiato il tracciato dal 01/10/2015
 	   bw.append(Formatta(null,"S",1227," "));// Filler -- cambiato il tracciato dal 01/10/2015 
 	   bw.append(Formatta(null,"S",20," "));// Filler
 	   bw.append(Formatta(null,"S",34," "));// Filler 
 	   bw.append("A"); //
 	   bw.append("\r\n");
 	   // fine record B
 	   
  	  // Tipo Record C Coda
	   bw.append("C"); // tipo record
	   bw.append(Formatta(Codice_Fiscale,"S",16," "));// Codice fiscale
	   bw.append(Formatta("1","D",8,"0"));// Progressivo modulo (VALE 1)
	   bw.append(Formatta(null,"S",3," "));// Filler campo utente
	   bw.append(Formatta(null,"S",25," "));// Filler
	   bw.append(Formatta(null,"S",20," "));// Filler campo utente x identificazione dichiarazione
	   bw.append(Formatta(null,"S",16," "));// Codice fiscale produttore software
 	   
	   Integer limite_col=75;
	   Integer num_col=0;
	// Campi non posizionali
	   /*if(totImpIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012001");
		   bw.append(Formatta(totImpIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }
	   if(totIvaIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012002");
		   bw.append(Formatta(totIvaIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	   
	   if(totImpBeniIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012003");
		   bw.append(Formatta(totImpBeniIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	
	   if(totIvaBeniIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012004");
		   bw.append(Formatta(totIvaBeniIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }		
	   */
	   if(totImpBeniIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012001");
		   bw.append(Formatta(totImpBeniIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	
	   if(totIvaBeniIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012002");
		   bw.append(Formatta(totIvaBeniIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }		 
	   if(BigDecimal.ZERO.compareTo(BigDecimal.ZERO)!=0){
			  bw.append("TR012003");// Beni soggetti stabiliti in altri stati comunita (beni assemblati in Italia)
			  bw.append(Formatta(BigDecimal.ZERO.toString(),"D",16," "));
			  num_col++;
	   }	
	   if(BigDecimal.ZERO.compareTo(BigDecimal.ZERO)!=0){
			   bw.append("TR012004");// Iva Beni soggetti stabiliti in altri stati comunita (beni assemblati in Italia)
			   bw.append(Formatta(BigDecimal.ZERO.toString(),"D",16," "));
	 		   num_col++;
	   }		   
	   if(totImpServIntra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012005");
		   bw.append(Formatta(totImpServIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
	   		num_col++;
	   }	
	   if(totImpS30Intra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012006");
		   bw.append(Formatta(totImpS30Intra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	
	  if(totIvaServIntra.compareTo(BigDecimal.ZERO)!=0){
		  bw.append("TR012007");
		  bw.append(Formatta(totIvaServIntra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		  num_col++;
	  }	
	  if(totImpBeniExtra.compareTo(BigDecimal.ZERO)!=0){
		  bw.append("TR012008");// Bolle doganali ignorare imponibile - considerato beni san marino
		  bw.append(Formatta(totImpBeniExtra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		  num_col++;
	  }	
	  if(totIvaBeniExtra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012009");// Bolle doganali ignorare iva
		   bw.append(Formatta(totIvaBeniExtra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	
	   if(totImpServExtra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012010");
		   bw.append(Formatta(totImpServExtra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	   }	
	   
	 if(totImpS30Extra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012011");
		   bw.append(Formatta(totImpS30Extra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	 }	
	   
	if(totIvaServExtra.compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012012");
		   bw.append(Formatta(totIvaServExtra.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).toString().replace(".", ","),"D",16," "));
		   num_col++;
	}	
	 if((totIvaServExtra.add(totIvaServIntra).add(totIvaBeniIntra).add(totIvaBeniExtra)).compareTo(BigDecimal.ZERO)!=0){
		   bw.append("TR012013");
		   bw.append(Formatta((((totIvaServExtra.add(totIvaServIntra).add(totIvaBeniIntra).add(totIvaBeniExtra)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)).toString().replace(".", ",")),"D",16," "));
		   num_col++;
	 }	
	// Campi non posizionali
	   bw.append("TR012014");// data versamento
	   java.util.Date data=((ElaboraFileIntraComponentSession)createComponentSession()).recuperoMaxDtPagamentoLiq(context.getUserContext(),getModel());
	   if(data!=null) {
		   bw.append(Formatta(null,"D",8," "));//completamento 16 caratteri non necessari per data
		   GregorianCalendar datagc = new GregorianCalendar();
		   datagc.setTime(new Date(data.getTime()));
		   bw.append(Formatta(new Integer(datagc.get(GregorianCalendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
		   bw.append(Formatta(new Integer(datagc.get(GregorianCalendar.MONTH)+1).toString(),"D",2,"0"));
		   bw.append(Formatta(new Integer(datagc.get(GregorianCalendar.YEAR)).toString(),"D",4,"0"));
		   
		  
	   }
	   else
		   throw new ApplicationException("Il pagamento della liquidazione iva ente per il mese selezionato non è stato effettuato!");  
	   num_col++; 
	   bw.append(Formatta(null,"D",(limite_col-num_col)*24," ")); 
	   
	   bw.append(Formatta(null,"D",8," "));// Filler
	   bw.append("A"); // 
 	   bw.append("\r\n");
 	   // fine record C
 	   
 	  // Tipo Record Z Coda
	   bw.append("Z"); // tipo record
	   bw.append(Formatta(null,"S",14," "));// Filler
	   bw.append(Formatta("1","D",9,"0"));// N° record tipo B
	   bw.append(Formatta("1","D",9,"0"));// N° record tipo C
	   
 	   bw.append(Formatta(null,"S",1864," "));// Filler 
 	   bw.append("A"); //
 	   bw.append("\r\n");
 	   // fine record Z Coda
 		
      bw.flush();
      bw.close();
      osw.close();
      os.close();	     
      setFile("/tmp/"+f.getName());	  
	}else{
   	  bw.flush();
      bw.close();
      osw.close();
      os.close();	      
	 throw new ApplicationException("Non ci sono dati da elaborare per il mese selezionato!");    	 
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
public void confermaElaborazione(ActionContext context, VIntrastatBulk dett) throws ComponentException, PersistencyException, IntrospectionException, RemoteException, BusinessProcessException {
		((ElaboraFileIntraComponentSession)createComponentSession()).confermaElaborazione(context.getUserContext(),dett);
		setFile(null);
	}
}

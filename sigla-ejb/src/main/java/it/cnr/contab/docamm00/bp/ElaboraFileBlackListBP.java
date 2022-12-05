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
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistBulk;
import it.cnr.contab.docamm00.ejb.ElaboraFileIntraComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

public class ElaboraFileBlackListBP extends SimpleCRUDBP {
	
public ElaboraFileBlackListBP() {
	super();
}
public ElaboraFileBlackListBP(String function) {
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
public void doElaboraFile(ActionContext context,VFatcomBlacklistBulk dett) throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException {
	  
		 try{  
		  AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
	      AnagraficoBulk ente = sess.getAnagraficoEnte(context.getUserContext());
	      java.util.List lista=((ElaboraFileIntraComponentSession)createComponentSession()).EstraiBlacklist(context.getUserContext(),getModel(),null);
   		  it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRA_12);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			 File f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
				   ente.getCodice_fiscale()+"_"+//codice fiscale
				   Formatta((CNRUserContext.getEsercizio(context.getUserContext())).toString().substring(2),"D",2,"0")+		   
				   "M"+Formatta(new Integer(dett.getMese()).toString(),"D",2,"0")+
				   ".ivl");
	      OutputStream os = (OutputStream)new FileOutputStream(f);
	      OutputStreamWriter osw = new OutputStreamWriter(os);
	      BufferedWriter bw = new BufferedWriter(osw);
	      AnagraficoBulk resp=null; 
    if (!lista.isEmpty()){
  		try {
  			resp = (AnagraficoBulk)sess.findByPrimaryKey(context.getUserContext(), new AnagraficoBulk(new Integer(config.getVal03())));
  		} catch (RemoteException e) {
  			throw new ComponentException(e);
  		} catch (EJBException e) {
  			throw new ComponentException(e);
  		}
    
    		//parte iniziale fissa record A
    		bw.append(new String("A"));
	    	bw.append(Formatta(null,"S",14," "));
	    	bw.append("IVL10");
	    	bw.append("01"); //Invio propri dati
	    	bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));
	    	bw.append(Formatta(null,"S",483," "));
	    	bw.append(Formatta(null,"D",4,"0"));
	    	bw.append(Formatta(null,"D",4,"0"));
	    	bw.append(Formatta(null,"S",100," "));
	    	bw.append(Formatta(null,"S",1068," "));
	    	bw.append(Formatta(null,"S",200," "));
	    	bw.append("A");
	    	bw.append("\r\n");
	    	//fine record A
	    	//record B
	    	bw.append(new String("B"));
	    	bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));
	    	bw.append(Formatta("1","D",8,"0")); //Progressivo Modulo
	    	bw.append(Formatta(null,"S",3," "));
	    	bw.append(Formatta(null,"S",25," "));
	    	bw.append(Formatta(null,"S",20," "));
	    	bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));// Codice fiscale produttore software (PRESIDENTE)
	    	bw.append(Formatta(null,"D",1,"0"));
	    	bw.append("0"); //correttiva nei termini
	    	bw.append("0"); // integrativa
	    	bw.append((CNRUserContext.getEsercizio(context.getUserContext())).toString());
	    	bw.append(Formatta(dett.getMese().toString(),"D",2,"0"));//Mese
	    	bw.append("0");//TRimestre
  			//Calcolo trimestre non serve 
//			BigDecimal resto =new BigDecimal(dett.getMese().toString()).remainder(new BigDecimal("3"));
//			BigInteger trim_i =new BigInteger(dett.getMese().toString()).divide(new BigInteger("3"));
//			if(resto.compareTo(BigDecimal.ZERO)==0)
//				bw.append(trim_i.toString());//trimestre
//			else
//				bw.append(trim_i.add(new BigInteger("1")).toString());//trimestre 
	    	bw.append("0");//Varizione periodicità    	
	    	bw.append(Formatta(ente.getPartita_iva(),"D",11,"0"));
	    	bw.append(Formatta(null,"S",100," "));//email
	    	bw.append(Formatta(null,"S",12," "));//tel
	    	bw.append(Formatta(null,"S",12," "));//fax
	    	// Obbligatori se non è Persona Fisica	
    	    bw.append(Formatta(null,"S",24," "));//  Cognome
    	    bw.append(Formatta(null,"S",20," "));//  Nome
    	    bw.append(Formatta(null,"D",1," "));// sesso
    	    bw.append(Formatta(null,"S",79," "));//  Comune nascita
    	    bw.append(Formatta(null,"S",2," "));// prov. nascita
    	    bw.append(Formatta(null,"D",8,"0"));// dt. nascita
    	   // Obbligatori se non è Persona Fisica  
    	    bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
	    	bw.append(Formatta(config.getIm01().toString(),"D",2,"0"));// NATURA GIURIDICA ????????? messo 1 PERCHè OBBLIGATORIO
	    	bw.append(Formatta(null,"S",24," "));// Stato estero di residenza
	 	    bw.append(Formatta(null,"D",3,"0"));// Codice paese estero
	 	    bw.append(Formatta(null,"S",24," "));// Numero di identificazione IVA estero
	 	   
	 	   bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));//codice fiscale rappresentante
		   bw.append(Formatta(config.getIm02().toString(),"D",2,"0"));//carica rappresentante  ??????? messo 2 PERCHè OBBLIGATORIO
		   bw.append(Formatta(ente.getCodice_fiscale(),"S",11," "));//codice fiscale societa dichiarante
		   
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
		   ComuneBulk comune_res = (ComuneBulk)sess.findByPrimaryKey(context.getUserContext(), new ComuneBulk(resp.getComune_fiscale().getPg_comune()));
		   if(comune_res==null)
			   throw new ApplicationException("Dati anagrafici del rappresentante non completi!");     
		   else
		   {
			   bw.append(Formatta(comune_res.getDs_comune(),"S",40," "));//COMUNE residenza rappresentante     ???????????????
			   bw.append(Formatta(comune_res.getCd_provincia(),"S",2," "));//PROV residenza rappresentante     ???????????????
			   bw.append(Formatta(resp.getCap_comune_fiscale(),"D",5,"0"));//cap residenza rappresentante     ???????????????
			   bw.append(Formatta(resp.getVia_fiscale()+" "+resp.getNum_civico_fiscale().toString(),"S",35," "));//via residenza rappresentante     ???????????????
			   bw.append(Formatta(null,"S",12," "));//tel rappresentante    
		   }
		   
		   bw.append(Formatta(new Integer(lista.size()).toString(),"D",8,"0"));// Firma checkbox
		   bw.append(Formatta("1","D",1,"0"));// Firma checkbox
		   
		   
		   // Sezione intermediario
		   bw.append(Formatta(null,"S",16," ")); 
	 	   bw.append(Formatta(null,"S",5,"0"));  
	 	   bw.append(Formatta("0","D",1,"0"));//
	 	   bw.append(Formatta(null,"D",8,"0"));
	 	   bw.append(Formatta("0","D",1,"0"));// Firma intermediario
	 	  
	 	   bw.append(Formatta(null,"S",1103," "));// Filler
	 	   bw.append(Formatta(null,"S",20," "));// Filler
	 	   bw.append(Formatta(null,"S",34," "));// Filler 
	 	   bw.append("A"); 
	 	   bw.append("\r\n");
	 	   // fine record B
	 	  Integer old_terzo=null;
	 	  Integer conta=0;
	 	   // tipo record C parte fissa
	 	   for (Iterator i=lista.iterator();i.hasNext();){
	 		   VFatcomBlacklistBulk det=(VFatcomBlacklistBulk)i.next();
	 		   if(old_terzo ==null || (old_terzo!=det.getCd_terzo())){
				 	   bw.append("C");
				 	   conta++;
				 	   bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));//codice fiscale
			 	 	   bw.append(Formatta(conta.toString(),"D",8,"0"));// Progressivo modulo (VALE 1)
					   bw.append(Formatta(null,"S",3," "));// Filler campo utente
					   bw.append(Formatta(null,"S",25," "));// Filler
					   bw.append(Formatta(null,"S",20," "));// Filler campo utente x identificazione dichiarazione
				 	   bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));//codice fiscale prod sw
				 	  // parte variabile
				 	   Integer limite_col=75;
					   Integer num_col=0;
					// Campi non posizionali
					   if(det.getPersona().compareTo(AnagraficoBulk.FISICA)==0){
						   num_col=Formatta(det.getCognome(),"S",16," ",bw,"A100101A",num_col);
						   num_col=Formatta(det.getNome(),"S",16," ",bw,"A1001002",num_col);
						   if(det.getItaliano_estero().compareTo(NazioneBulk.ITALIA)==0){ //Dalla nazione di nascita
							   bw.append("A1001003");
							   dataNascita.setTime(new Date(det.getDt_nascita().getTime()));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.MONTH)+1).toString(),"D",2,"0"));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.YEAR)).toString(),"D",4,"0"));
							   num_col++;
							   
							   num_col=Formatta(det.getComune_nascita(),"S",16," ",bw,"A1001004",num_col);
							   
							   bw.append("A1001005");
							   bw.append(Formatta(det.getCd_provincia(),"S",16," "));
							   num_col++;
						   }else{
							   num_col=Formatta(det.getStato_nascita(),"S",16," ",bw,"A1001004",num_col);
						   }
					   }else{
						   num_col=Formatta(det.getRagione_sociale(),"S",16," ",bw,"A100101B",num_col);
					   }
					   bw.append("A1001006");
					   bw.append(Formatta(det.getCd_nazione(),"D",16," "));
					   num_col++;
					   
					   num_col=Formatta(det.getDs_nazione(),"S",16," ",bw,"A1001007",num_col);
					   
					   num_col=Formatta(det.getComune_sede(),"S",16," ",bw,"A1001008",num_col);
					   
					   num_col=Formatta(det.getIndirizzo_sede(),"S",16," ",bw,"A1001009",num_col);
					   
					   if(det.getId_fiscale_estero()!=null){
						   num_col=Formatta(det.getId_fiscale_estero(),"S",16," ",bw,"A1001010",num_col);
					   }
					   if(det.getCodice_fiscale()!=null){
						   num_col=Formatta(det.getCodice_fiscale(),"S",16," ",bw,"A1001011",num_col);
					   }
					   java.util.List lista_terzo=((ElaboraFileIntraComponentSession)createComponentSession()).EstraiBlacklist(context.getUserContext(),getModel(),new TerzoBulk(det.getCd_terzo()));
					   BigDecimal TotBeniImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotIvaBeniImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotServiziImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotIvaServiziImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotBeniNoImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotServiziNoImpAttiva=BigDecimal.ZERO;
					   BigDecimal TotEsentiAttiva=BigDecimal.ZERO;
					   BigDecimal TotBeniNoSogAttiva=BigDecimal.ZERO;
					   BigDecimal TotServiziNoSogAttiva=BigDecimal.ZERO;
					   
					   BigDecimal TotBeniImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotIvaBeniImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotServiziImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotIvaServiziImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotBeniNoImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotServiziNoImpPassiva=BigDecimal.ZERO;
					   BigDecimal TotEsentiPassiva=BigDecimal.ZERO;
					   BigDecimal TotBeniNoSogPassiva=BigDecimal.ZERO;
					   BigDecimal TotServiziNoSogPassiva=BigDecimal.ZERO;
					   
					   for (Iterator k=lista_terzo.iterator();k.hasNext();){
					 		   VFatcomBlacklistBulk det_terzo=(VFatcomBlacklistBulk)k.next();
					 		   if(det_terzo.getTipo().compareTo("A")==0){
					 			   if(det_terzo.getBene_servizio().compareTo(Fattura_passivaBulk.FATTURA_DI_BENI)==0){
					 				  TotBeniImpAttiva=TotBeniImpAttiva.add(det_terzo.getImponibile());
					 				  TotIvaBeniImpAttiva=TotIvaBeniImpAttiva.add(det_terzo.getIva());
					 				  TotBeniNoImpAttiva=TotBeniNoImpAttiva.add(det_terzo.getImp_non_imp());
					 				  TotBeniNoSogAttiva=TotBeniNoSogAttiva.add(det_terzo.getImp_non_soggetto());
					 			   }else //servizi
					 			   {
					 				  TotServiziImpAttiva=TotServiziImpAttiva.add(det_terzo.getImponibile());
					 				  TotIvaServiziImpAttiva=TotIvaServiziImpAttiva.add(det_terzo.getIva());
					 				  TotServiziNoImpAttiva=TotServiziNoImpAttiva.add(det_terzo.getImp_non_imp());
					 				  TotServiziNoSogAttiva=TotServiziNoSogAttiva.add(det_terzo.getImp_non_soggetto());
					 			   }
					 			   TotEsentiAttiva=TotEsentiAttiva.add(det_terzo.getImp_esente());
					 		   }
					 		   else{ // Passive
					 			  if(det_terzo.getBene_servizio().compareTo(Fattura_passivaBulk.FATTURA_DI_BENI)==0){
					 				  TotBeniImpPassiva=TotBeniImpPassiva.add(det_terzo.getImponibile());
					 				  TotIvaBeniImpPassiva=TotIvaBeniImpPassiva.add(det_terzo.getIva());
					 				  TotBeniNoImpPassiva=TotBeniNoImpPassiva.add(det_terzo.getImp_non_imp());
					 				  TotBeniNoSogPassiva=TotBeniNoSogPassiva.add(det_terzo.getImp_non_soggetto());
					 			   }else //servizi
					 			   {
					 				  TotServiziImpPassiva=TotServiziImpPassiva.add(det_terzo.getImponibile());
					 				  TotIvaServiziImpPassiva=TotIvaServiziImpPassiva.add(det_terzo.getIva());
					 				  TotServiziNoImpPassiva=TotServiziNoImpPassiva.add(det_terzo.getImp_non_imp());
					 				  TotServiziNoSogPassiva=TotServiziNoSogPassiva.add(det_terzo.getImp_non_soggetto());
					 			   }
					 			   TotEsentiPassiva=TotEsentiPassiva.add(det_terzo.getImp_esente());
					 		   }
					   }
					   old_terzo=det.getCd_terzo();
					      
					   if(TotBeniImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1002001");
						   bw.append(Formatta(TotBeniImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotIvaBeniImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1003001");
						   bw.append(Formatta(TotIvaBeniImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1004001");
						   bw.append(Formatta(TotServiziImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotIvaServiziImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1005001");
						   bw.append(Formatta(TotIvaServiziImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotBeniNoImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1006001");
						   bw.append(Formatta(TotBeniNoImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziNoImpAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1007001");
						   bw.append(Formatta(TotServiziNoImpAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotEsentiAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1008001");
						   bw.append(Formatta(TotEsentiAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotBeniNoSogAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1009001");
						   bw.append(Formatta(TotBeniNoSogAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziNoSogAttiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1010001");
						   bw.append(Formatta(TotServiziNoSogAttiva.toString(),"D",16," "));
						   num_col++;
					   }
					   //Passive
					   if(TotBeniImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1019001");
						   bw.append(Formatta(TotBeniImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotIvaBeniImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1020001");
						   bw.append(Formatta(TotIvaBeniImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1021001");
						   bw.append(Formatta(TotServiziImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotIvaServiziImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1022001");
						   bw.append(Formatta(TotIvaServiziImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotBeniNoImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1023001");
						   bw.append(Formatta(TotBeniNoImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziNoImpPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1024001");
						   bw.append(Formatta(TotServiziNoImpPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotEsentiPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1025001");
						   bw.append(Formatta(TotEsentiPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotBeniNoSogPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1026001");
						   bw.append(Formatta(TotBeniNoSogPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   if(TotServiziNoSogPassiva.compareTo(BigDecimal.ZERO)!=0){
						   bw.append("A1027001");
						   bw.append(Formatta(TotServiziNoSogPassiva.toString(),"D",16," "));
						   num_col++;
					   }
					   
					   bw.append(Formatta(null,"D",(limite_col-num_col)*24," ")); 
					   
					   bw.append(Formatta(null,"D",8," "));// Filler
					   bw.append("A"); // 
				 	   bw.append("\r\n");
	 		   }
		 	   // fine record C
	 		}
	 	  // Tipo Record Z Coda
		   bw.append("Z"); // tipo record
		   bw.append(Formatta(null,"S",14," "));// Filler
		   bw.append(Formatta("1","D",9,"0"));// N° record tipo B
		   bw.append(Formatta(new Integer(lista.size()).toString(),"D",9,"0"));// N° record tipo C
		   
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
/**
 * @param s Stringa in Input
 * @param allineamento del testo "D" Destra - "S" Sinistra 
 * @param dimensione richiesta del campo
 * @param riempimento carattere di riempimento per raggiungere la dimensione richiesta
 * @param w buffer di scrittura
 * @param codice parte fissa della colonna del record di tipo C
 * @param colonna n°colonna iniziale  
 * @return n° colonna finale 
  */
public Integer Formatta(String s, String allineamento,Integer dimensione,String riempimento,BufferedWriter w,String codice,Integer colonna) throws IOException{
	
	if (s==null ||s.length()<= dimensione){
		w.append(codice);
		w.append(Formatta(s,allineamento,dimensione,riempimento));
		colonna++;
	}else{
		s=s.toUpperCase();
		w.append(codice);
		w.append(s.substring(0,dimensione));
		colonna++;
		int i=0;
		while(dimensione+(i*(dimensione-1))<s.length()){
			i++; 
			w.append(codice); 
			if(s.length()>dimensione+((i-1)*(dimensione-1)))
				w.append(Formatta("+"+s.substring(dimensione+((i-1)*(dimensione-1)),s.length()),allineamento,dimensione,riempimento));
			colonna++;
		}
	}
	return colonna;
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
	
}
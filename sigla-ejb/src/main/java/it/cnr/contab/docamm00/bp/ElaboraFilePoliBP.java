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
import it.cnr.contab.docamm00.docs.bulk.VSpesometroBulk;
import it.cnr.contab.docamm00.ejb.ElaboraFileIntraComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
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

public class ElaboraFilePoliBP extends SimpleCRUDBP {
	
public ElaboraFilePoliBP() {
	super();
}
public ElaboraFilePoliBP(String function) {
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
public void doElaboraFile(ActionContext context,VSpesometroBulk dett) throws BusinessProcessException, ComponentException, PersistencyException, IntrospectionException {
		 try{  
		  conta=0;
		  Integer conta_bl3=0;
		  Integer conta_bl4=0;
		  Integer conta_fa=0;
		  AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
	      AnagraficoBulk ente = sess.getAnagraficoEnte(context.getUserContext());
	      // configurato dal  2014 - versione precedente gestita da altro Bp e su altra view altro tracciato
	      it.cnr.contab.config00.bulk.Configurazione_cnrBulk configblack = null;
			try {
				configblack = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_BLACKLIST);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
	      
	      if (configblack.getVal01().compareTo("M")==0){
	    	  if (dett.isFlBlacklist() && dett.getMese() == null )
	    		  throw new ApplicationException("Attenzione: specificare il Mese");
	      }
	      else if(configblack.getVal01().compareTo("M")!=0){
	    	  if (dett.isFlBlacklist() && dett.getMese() != null )
	    		  throw new ApplicationException("In caso di comunicazione annuale, il mese non deve essere indicato!");
	      }
			
	      java.util.List lista=((ElaboraFileIntraComponentSession)createComponentSession()).EstraiBlacklist(context.getUserContext(),getModel(),null);
   		  it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
			try {
				config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_COSTANTI, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_MODELLO_INTRA_12);
			} catch (RemoteException e) {
				throw new ComponentException(e);
			} catch (EJBException e) {
				throw new ComponentException(e);
			}
			File f;
			if(dett.getMese()!=null)
			   f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
				   ente.getCodice_fiscale()+"_"+//codice fiscale
				   Formatta((CNRUserContext.getEsercizio(context.getUserContext())).toString().substring(2),"D",2,"0")+		   
				   "M"+Formatta(new Integer(dett.getMese()).toString(),"D",2,"0")+
				   ".ccf");
			else
				f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
						   ente.getCodice_fiscale()+"_"+//codice fiscale
								   (dett.isFlBlacklist()?"BL_":"")+
								   (CNRUserContext.getEsercizio(context.getUserContext())).toString()+		   
								   ".ccf");
			
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
	    	bw.append("NSP00");
	    	bw.append("01"); //Invio propri dati
	    	bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));
	    	bw.append(Formatta(null,"S",483," "));
	    	bw.append(Formatta("0","D",4,"0")); //PROGRESSIVO 1??? 
	    	bw.append(Formatta("0","D",4,"0")); //NUM TOTALE INVII TELEMATICI 1???
	    	bw.append(Formatta(null,"S",100," "));
	    	bw.append(Formatta(null,"S",1068," "));
	    	bw.append(Formatta(null,"S",200," "));
	    	bw.append("A");
	    	bw.append("\r\n");
//	    	//fine record A
//	    	//record B
	    	bw.append("B");
	    	bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));
	    	bw.append(Formatta("1","D",8,"0")); //Progressivo Modulo
	    	bw.append(Formatta(null,"S",3," "));
	    	bw.append(Formatta(null,"S",25," "));
	    	bw.append(Formatta(null,"S",20," "));
//	    	bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));// Codice fiscale produttore software (PRESIDENTE)
	    	bw.append(Formatta(null,"S",16," "));
	    	bw.append("1");  // fisso ordinaria
	    	bw.append("0"); // sostitutiva
	    	bw.append("0"); // di annullamento
	    	bw.append(Formatta(null,"D",17,"0"));  // da indicare per sostitutiva
	    	bw.append(Formatta(null,"D",6,"0"));  // da indicare per sostitutiva
	    	bw.append("1");  // fisso dati aggregati
	    	bw.append("0"); // dati analitici
	    	// quadri compilati sia annuale che mensile
	    	if (dett.isFlBlacklist()){
	    		bw.append("0"); // quadro FA
	    		bw.append("0"); // quadro SA
	    		bw.append("1"); // quadro BL
	    		bw.append("0"); // quadro FE
	    		bw.append("0"); // quadro FR
	    		bw.append("0"); // quadro NE
	    		bw.append("0"); // quadro NR
	    		bw.append("0"); // quadro DF
	    		bw.append("0"); // quadro FN
	    		bw.append("0"); // quadro SE
	    		bw.append("0"); // quadro TU
	    		bw.append("1"); // quadro TA
	    	}
	    	else{
	    		bw.append("1"); // quadro FA
	    		bw.append("0"); // quadro SA
	    		bw.append("1"); // quadro BL
	    		bw.append("0"); // quadro FE
	    		bw.append("0"); // quadro FR
	    		bw.append("0"); // quadro NE
	    		bw.append("0"); // quadro NR
	    		bw.append("0"); // quadro DF
	    		bw.append("0"); // quadro FN
	    		bw.append("0"); // quadro SE
	    		bw.append("0"); // quadro TU
	    		bw.append("1"); // quadro TA
	    	}
	    	bw.append(Formatta(ente.getPartita_iva(),"S",11," "));
	    	bw.append(Formatta(config.getVal02(),"S",6," "));
	    	bw.append(Formatta(null,"S",12," "));//tel
	    	bw.append(Formatta(null,"S",12," "));//fax
	    	bw.append(Formatta(null,"S",50," "));//email
	    	
	    	// Obbligatori se non è Persona Fisica	
    	    bw.append(Formatta(null,"S",24," "));//  Cognome
    	    bw.append(Formatta(null,"S",20," "));//  Nome
    	    bw.append(Formatta(null,"D",1," "));// sesso
    	    bw.append(Formatta(null,"D",8,"0"));// dt. nascita
    	    bw.append(Formatta(null,"S",40," "));//  Comune nascita
    	    bw.append(Formatta(null,"S",2," "));// prov. nascita

//    	   // Obbligatori se non è Persona Fisica  
    	    bw.append(Formatta(ente.getRagione_sociale(),"S",60," "));
    	    
    	   bw.append((CNRUserContext.getEsercizio(context.getUserContext())).toString());
    		if (dett.getMese()!=null)
    			bw.append(Formatta(new Integer(dett.getMese()).toString(),"D",2,"0"));//Mese (Da valorizzare solo per se presenti ACQUISTI DA SAN MARINO
    		else	
    			bw.append(Formatta(null,"D",2,"0"));
    	    // Da verificare
// 	 	   bw.append(Formatta(resp.getCodice_fiscale(),"S",16," "));//codice fiscale rappresentante
// 		   bw.append(Formatta(config.getIm02().toString(),"D",2,"0"));//carica rappresentante  ??????? messo 2 PERCHè OBBLIGATORIO
// 		   bw.append(Formatta(null,"D",8,"0"));// dt. inizio procedura 
// 		   bw.append(Formatta(null,"D",8,"0"));// dt. fine
// 		   bw.append(Formatta(resp.getCognome(),"S",24," "));//cognome rappresentante     ???????????????
//		   bw.append(Formatta(resp.getNome(),"S",20," "));//nome rappresentante     ???????????????
//		   bw.append(resp.getTi_sesso());//sesso rappresentante     ???????????????
//		   GregorianCalendar dataNascita = new GregorianCalendar();
//		   dataNascita.setTime(new Date(resp.getDt_nascita().getTime()));
//		   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
//		   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.MONTH)+1).toString(),"D",2,"0"));
//		   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.YEAR)).toString(),"D",4,"0"));
//		   ComuneBulk comune = (ComuneBulk)sess.findByPrimaryKey(context.getUserContext(), new ComuneBulk(resp.getComune_nascita().getPg_comune()));
//		   if(comune==null)
//			   throw new ApplicationException("Dati anagrafici del rappresentante non completi!");     
//		   else
//		   {
//			   bw.append(Formatta(comune.getDs_comune(),"S",40," "));//COMUNE nascita rappresentante     ???????????????
//			   bw.append(Formatta(comune.getCd_provincia(),"S",2," "));//PROV nascita rappresentante     ???????????????
//		   }

    	   bw.append(Formatta(null,"S",16," "));//codice fiscale rappresentante
 		   bw.append(Formatta(null,"D",2,"0"));//carica rappresentante  ??????? messo 2 PERCHè OBBLIGATORIO
 		   bw.append(Formatta(null,"D",8,"0"));// dt. inizio procedura 
 		   bw.append(Formatta(null,"D",8,"0"));// dt. fine
 		   bw.append(Formatta(null,"S",24," "));//cognome rappresentante     ???????????????
		   bw.append(Formatta(null,"S",20," "));//nome rappresentante     ???????????????
		   bw.append(" ");//sesso rappresentante     ???????????????
		   bw.append(Formatta(null,"D",8,"0")); // data nascita
		   bw.append(Formatta(null,"S",40," "));//COMUNE nascita rappresentante     ???????????????
		   bw.append(Formatta(null,"S",2," "));//PROV nascita rappresentante     ???????????????
		   
		   bw.append(Formatta(null,"S",60," "));//ragione sociale rappresentante     ???????????????
		   
//		   // Sezione intermediario
		   bw.append(Formatta(null,"S",16," ")); 
	 	   bw.append(Formatta(null,"D",5,"0"));  
	 	   bw.append("0"); ///???
		   bw.append(Formatta(null,"S",1," "));
	 	   bw.append(Formatta(null,"D",8,"0")); 
	 	  
	 	   bw.append(Formatta(null,"S",1258," "));// Filler  
	 	   bw.append(Formatta(null,"S",20," "));// Filler
	 	   bw.append(Formatta(null,"S",18," "));// Filler 
	 	   bw.append("A"); 
	 	   bw.append("\r\n");
//	 	   // fine record B
	 	  
	 	  Integer limite_col=75;
	 	  Integer num_col=0;
	 	  GregorianCalendar dataNascita = new GregorianCalendar();
	 	   // tipo record C parte fissa
	 	   for (Iterator i=lista.iterator();i.hasNext();){
	 		   VSpesometroBulk det=(VSpesometroBulk)i.next();
	 		   	if(det.getQuadro().compareTo("BL")==0 ||conta_fa%3==0){
	 		   		   num_col=0;
				 	   bw.append("C");
				 	   conta++;
				 	   bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));//codice fiscale
			 	 	   bw.append(Formatta(conta.toString(),"D",8,"0"));// Progressivo modulo (VALE 1)
					   bw.append(Formatta(null,"S",3," "));// Filler campo utente
					   bw.append(Formatta(null,"S",25," "));// Filler
					   bw.append(Formatta(null,"S",20," "));// Filler campo utente x identificazione dichiarazione
				 	   bw.append(Formatta(null,"S",16," "));//codice fiscale prod sw
	 		  	}
			 	    // parte variabile
    				// Campi non posizionali
				 	   if(det.getQuadro().compareTo("BL")==0){
						   if(det.getCognome()!=null){
							   num_col=Formatta(det.getCognome(),"S",16," ",bw,"BL001001",num_col);
							   num_col=Formatta(det.getNome(),"S",16," ",bw,"BL001002",num_col);
							   num_col=Formatta(null,"D",8," ",bw,"BL001003",num_col);//completamento 16 caratteri non necessari per data
							   dataNascita.setTime(new Date(det.getDtNascita().getTime()));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.DAY_OF_MONTH)).toString(),"D",2,"0"));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.MONTH)+1).toString(),"D",2,"0"));
							   bw.append(Formatta(new Integer(dataNascita.get(GregorianCalendar.YEAR)).toString(),"D",4,"0"));
							   num_col=Formatta(det.getStatoNascita(),"S",16," ",bw,"BL001004",num_col);
							   num_col=Formatta(det.getProvincia(),"S",16," ",bw,"BL001005",num_col);
							   num_col=Formatta(det.getCodiceStatoEstero(),"D",16," ",bw,"BL001006",num_col);
						   }else{
							   num_col=Formatta(det.getRagioneSociale(),"S",16," ",bw,"BL001007",num_col);
							   num_col=Formatta(det.getComuneSede(),"S",16," ",bw,"BL001008",num_col);
							   num_col=Formatta(det.getCodiceStatoEstero(),"D",16," ",bw,"BL001009",num_col);
							   num_col=Formatta(det.getIndirizzoSede(),"S",16," ",bw,"BL001010",num_col);
						   } 
						   // ????? forse da togliere
						   //if (det.getPartitaIva()!=null)
							 //  num_col=Formatta(det.getPartitaIva(),"S",16," ",bw,"BL002001",num_col);
						   // valorizzazione del mese nella view solo per BL Fiscalità speciale
						   if(det.getTipoFiscalita().compareTo("FS")==0){ 
							   num_col=Formatta("1","D",16," ",bw,"BL002002",num_col);
							   //num_col=Formatta("1","D",16," ",bw,"BL002003",num_col);
							   //num_col=Formatta("1","D",16," ",bw,"BL002004",num_col);
							
							   if(det.getImponibileFa().compareTo(BigDecimal.ZERO)!=0) 
								  num_col=Formatta(det.getImponibileFa().toString(),"D",16," ",bw,"BL003001",num_col);
							   
							   if(det.getIvaFa().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getIvaFa().toString(),"D",16," ",bw,"BL003002",num_col);
								 
							   //attive bl004 indicato da ufficio competente mai valorizzato!!!
							   if(det.getImponibileNdFa().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getImponibileNdFa().toString(),"D",16," ",bw,"BL005001",num_col);
								   
							   if(det.getIvaNdFa().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getIvaNdFa().toString(),"D",16," ",bw,"BL005002",num_col);
								
							   if(det.getImponibileFp().compareTo(BigDecimal.ZERO)!=0)
								  num_col=Formatta(det.getImponibileFp().toString(),"D",16," ",bw,"BL006001",num_col);
								   
							   if(det.getIvaFp().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getIvaFp().toString(),"D",16," ",bw,"BL006002",num_col);
							 //attive bl007 indicato da ufficio competente mai valorizzato!!!		
							   if(det.getImponibileNdFp().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getImponibileNdFp().toString(),"D",16," ",bw,"BL008001",num_col);
									   
							   if(det.getIvaNdFp().compareTo(BigDecimal.ZERO)!=0)
								   num_col=Formatta(det.getIvaNdFp().toString(),"D",16," ",bw,"BL008002",num_col);
						   }else{
							   if(det.getTipo().compareTo("PASSIVE")==0 && det.getTiBeneServizio().compareTo(Bene_servizioBulk.SERVIZIO)==0){
								   conta_bl4++; 
								   num_col=Formatta("1","D",16," ",bw,"BL002004",num_col);
								   if(det.getImponibileFp().compareTo(BigDecimal.ZERO)!=0)
									   num_col=Formatta(det.getImponibileFp().toString(),"D",16," ",bw,"BL006001",num_col);
										   
									if(det.getIvaFp().compareTo(BigDecimal.ZERO)!=0)
									   num_col=Formatta(det.getIvaFp().toString(),"D",16," ",bw,"BL006002",num_col);
							   }
							   else{
								   conta_bl3++;
								   num_col=Formatta("1","D",16," ",bw,"BL002003",num_col);
								   if(det.getImponibileFa().compareTo(BigDecimal.ZERO)!=0) 
									   num_col=Formatta(det.getImponibileFa().toString(),"D",16," ",bw,"BL003001",num_col);
								   if(det.getIvaFa().compareTo(BigDecimal.ZERO)!=0)
									   num_col=Formatta(det.getIvaFa().toString(),"D",16," ",bw,"BL003002",num_col);
								   if(det.getImponibileFp().compareTo(BigDecimal.ZERO)!=0)
										  num_col=Formatta(det.getImponibileFp().toString(),"D",16," ",bw,"BL006001",num_col);
										   
								   if(det.getIvaFp().compareTo(BigDecimal.ZERO)!=0)
									     num_col=Formatta(det.getIvaFp().toString(),"D",16," ",bw,"BL006002",num_col); 
							   }
						   }
					   }// FINO QUADRO BL
				 	  else{
						   // Quadro FA 
				 		  //controllo forse inutile ma impedisce problemi sulla correttezza del file 
				 		 if(det.getCodiceFiscale()!=null||det.getPartitaIva()!=null){
				 		   String c=null;
				 		   if(conta_fa%3==0)
				 			    c=new String("1");
				 		   else
				 			    c=((Integer)((conta_fa%3)+1)).toString();
				 		  conta_fa++;
				 		   if(det.getCodiceFiscale()!=null)
							   num_col=Formatta(det.getCodiceFiscale(),"S",16," ",bw,"FA00"+c+"002",num_col);
				 		   else  if(det.getPartitaIva()!=null)
							   num_col=Formatta(det.getPartitaIva(),"S",16," ",bw,"FA00"+c+"001",num_col);
				 		 
						   if(det.getNrAttive()!=0)
							   num_col=Formatta(det.getNrAttive().toString(),"D",16," ",bw,"FA00"+c+"004",num_col);
						   if(det.getNrPassive()!=0)
							   num_col=Formatta(det.getNrPassive().toString(),"D",16," ",bw,"FA00"+c+"005",num_col);
						   if(det.getImponibileFa().compareTo(BigDecimal.ZERO)!=0) 
							   num_col=Formatta(det.getImponibileFa().toString(),"D",16," ",bw,"FA00"+c+"007",num_col);
						   if(det.getIvaFa().compareTo(BigDecimal.ZERO)!=0) 
							   num_col=Formatta(det.getIvaFa().toString(),"D",16," ",bw,"FA00"+c+"008",num_col);
						   if(det.getImponibileNdFa().compareTo(BigDecimal.ZERO)!=0) 
							   num_col=Formatta(det.getImponibileNdFa().toString(),"D",16," ",bw,"FA00"+c+"0010",num_col);
						   if(det.getIvaNdFa().compareTo(BigDecimal.ZERO)!=0) 
							   num_col=Formatta(det.getIvaNdFa().toString(),"D",16," ",bw,"FA00"+c+"011",num_col);
						   if(det.getImponibileFp().compareTo(BigDecimal.ZERO)!=0) 
							   num_col=Formatta(det.getImponibileFp().toString(),"D",16," ",bw,"FA00"+c+"012",num_col);
						   if(det.getIvaFp().compareTo(BigDecimal.ZERO)!=0) 
							  num_col=Formatta(det.getIvaFp().toString(),"D",16," ",bw,"FA00"+c+"013",num_col);
						   if(det.getImponibileNdFp().compareTo(BigDecimal.ZERO)!=0) 
							  num_col=Formatta(det.getImponibileNdFp().toString(),"D",16," ",bw,"FA00"+c+"0015",num_col);
						   if(det.getIvaNdFa().compareTo(BigDecimal.ZERO)!=0) 
							  num_col=Formatta(det.getIvaNdFp().toString(),"D",16," ",bw,"FA00"+c+"016",num_col);   
					   }
				 	  }
   			 	     if(det.getQuadro().compareTo("BL")==0 ||(conta_fa%3==0&&(det.getCodiceFiscale()!=null||det.getPartitaIva()!=null))){
						   bw.append(Formatta(null,"D",(limite_col-num_col)*24," ")); 
						   bw.append(Formatta(null,"D",8," "));// Filler
						   bw.append("A");  
					 	   bw.append("\r\n"); 
				  	}
	 	   }
	 	   if(conta_fa!= 0 && conta_fa%3!=0){ 
			   bw.append(Formatta(null,"D",(limite_col-num_col)*24," ")); 
			   bw.append(Formatta(null,"D",8," "));// Filler
			   bw.append("A");  
		 	   bw.append("\r\n");
		   	}
	 	   // fine record C
	 	   // tipo record E
	 	    bw.append("E");
	    	bw.append(Formatta(ente.getCodice_fiscale(),"S",16," "));
	    	bw.append(Formatta("1","D",8,"0")); //Progressivo Modulo
	    	bw.append(Formatta(null,"S",3," "));
	    	bw.append(Formatta(null,"S",25," "));
	    	bw.append(Formatta(null,"S",20," "));
	    	bw.append(Formatta(null,"S",16," "));
	    	num_col=0;
	    	// campi non posizionali
	    	if(dett.isFlBlacklist()){
	    		if(lista!=null && lista.size()!=0){
	    			bw.append("TA003001");
	    			// numero contraparti blacklist BL002002
	    			bw.append(Formatta(new Integer(lista.size()).toString(),"D",16," "));
	    			num_col++;
	    		} 
	    	}
	    	if(conta_fa!=0){
	    		bw.append("TA001001");
    			// numero contraparti fa
    			bw.append(Formatta(conta_fa.toString(),"D",16," "));
    			num_col++;
	    	}
	    	if(conta_bl3!=0){
	    		bw.append("TA003002");
    			// numero contraparti operazione con soggetti non residenti beni BL002003
    			bw.append(Formatta(conta_bl3.toString(),"D",16," "));
    			num_col++;
	    	}
	    	if(conta_bl4!=0){
	    		bw.append("TA003003");
	    		// numero contraparti servizi non residenti BL002004
    			bw.append(Formatta(conta_bl4.toString(),"D",16," "));
    			num_col++;
	    	}
	    	bw.append(Formatta(null,"D",(limite_col-num_col)*24," ")); 
			bw.append(Formatta(null,"D",8," "));// Filler
			bw.append("A");  
		 	bw.append("\r\n");
		   // fine record E
	 	  // Tipo Record Z Coda
		   bw.append("Z"); // tipo record
		   bw.append(Formatta(null,"S",14," "));// Filler
		   bw.append(Formatta("1","D",9,"0"));// N° record tipo B
		  // bw.append(Formatta(new Integer(lista.size()).toString(),"D",9,"0"));// N° record tipo C
		   bw.append(Formatta(conta.toString(),"D",9,"0"));// N° record tipo C
		   bw.append(Formatta("0","D",9,"0"));// N° record tipo D
		   bw.append(Formatta("1","D",9,"0"));// N° record tipo E
	 	   bw.append(Formatta(null,"S",1846," "));// Filler 
	 	   bw.append("A"); //
	 	   bw.append("\r\n");
//	 	   // fine record Z Coda
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
public Integer Formatta(String s, String allineamento,Integer dimensione,String riempimento,BufferedWriter w,String codice,Integer colonna,AnagraficoBulk ente) throws IOException{
	// Non utilizzata
	Integer limite_col=75;
	 if(colonna.compareTo(limite_col)==0 ){
		  w.append(Formatta(null,"D",8," "));// Filler
		  w.append("A");  
	 	  w.append("\r\n");
	 	  colonna=0;
	 }
     if(colonna.intValue()==0){
	 	   w.append("C");
	 	   conta++;
	 	   w.append(Formatta(ente.getCodice_fiscale(),"S",16," "));//codice fiscale
	 	   w.append(Formatta(conta.toString(),"D",8,"0"));// Progressivo modulo (VALE 1)
		   w.append(Formatta(null,"S",3," "));// Filler campo utente
		   w.append(Formatta(null,"S",25," "));// Filler
		   w.append(Formatta(null,"S",20," "));// Filler campo utente x identificazione dichiarazione
	 	   w.append(Formatta(null,"S",16," "));//codice fiscale prod sw
    }
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
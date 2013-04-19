package it.cnr.contab.docamm00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.client.docamm.FatturaAttivaException;
import it.cnr.contab.client.docamm.FatturaAttivaException_Exception;
import it.cnr.contab.client.docamm.FatturaAttivaIntra;
import it.cnr.contab.client.docamm.FatturaAttivaRiga;
import it.cnr.contab.client.docamm.FatturaAttivaScad;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.utenze00.bp.Costanti;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.util.DateUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;
@XmlSeeAlso({java.util.ArrayList.class}) 
@Stateless
@WebService(endpointInterface="it.cnr.contab.docamm00.ejb.FatturaAttivaComponentSessionWS")
@DeclareRoles({"WSUserRole","IITRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")
public class FatturaAttivaComponentWS {
	//public void InserimentoFattura(byte[] file) throws SOAPException, ComponentException, PersistencyException, IntrospectionException, RemoteException, EJBException, ValidationException {
/* non allineato alle ultime modifiche (rif_ordine e dt_scadenza)
 * public byte[] InserimentoFattura(byte[] file) throws  Exception {
		byte[] file_out=null;
		UserContext context=new WSUserContext("System",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
		try{
			   Fattura_attivaBulk testata;
			   Fattura_attiva_rigaBulk riga;
			   File filexml = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", File.separator +"Xml"+((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).callGetPgPerStampa(context)+".xml");
			   BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(filexml));
			   bos.write(file);
			   bos.flush();
			   bos.close();
	           DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	           DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	           Document doc = docBuilder.parse (filexml);
               doc.getDocumentElement ().normalize ();
               NodeList listOfFatture = doc.getElementsByTagName("fattura");
	           String par;	           
	           String par1;
	         
	           for(int s=0; s<listOfFatture.getLength() ; s++){
	        	   Node FattureNode = listOfFatture.item(s);
                   Element FatturaElement = (Element)FattureNode;
                   if (RecuperaValoreParametro(FatturaElement,"esercizio")==null||
                	   RecuperaValoreParametro(FatturaElement,"utcr")==null ||
                	   RecuperaValoreParametro(FatturaElement,"cd_cds_origine")==null ||
                	   RecuperaValoreParametro(FatturaElement,"cd_uo_origine")==null||
                	   RecuperaValoreParametro(FatturaElement,"pg_fattura_esterno")==null||
                	   RecuperaValoreParametro(FatturaElement,"ti_fattura")==null||
                	   RecuperaValoreParametro(FatturaElement,"cd_tipo_sezionale")==null||
                	   RecuperaValoreParametro(FatturaElement,"dt_registrazione")==null||
                	   RecuperaValoreParametro(FatturaElement,"ti_causale_emissione")==null||
                	   RecuperaValoreParametro(FatturaElement,"fl_liquidazione_differita")==null||
                	   RecuperaValoreParametro(FatturaElement,"fl_intra_ue")==null||
                	   RecuperaValoreParametro(FatturaElement,"fl_extra_ue")==null||
                	   RecuperaValoreParametro(FatturaElement,"fl_san_marino")==null||
                	   RecuperaValoreParametro(FatturaElement,"cd_terzo")==null||
                	   RecuperaValoreParametro(FatturaElement,"cd_terzo_uo_cds")==null||
                	   RecuperaValoreParametro(FatturaElement,"pg_banca_uo_cds")==null||
                	   RecuperaValoreParametro(FatturaElement,"cd_modalita_pag_uo_cds")==null||
                	   RecuperaValoreParametro(FatturaElement,"cd_divisa")==null||
                	   RecuperaValoreParametro(FatturaElement,"cambio")==null){
                	  AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
           		      AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
                   }
                   else{ 
                	   if (RecuperaValoreParametro(FatturaElement,"ti_causale_emissione").compareTo("C")!=0 &&
                		   RecuperaValoreParametro(FatturaElement,"ti_causale_emissione").compareTo("L")!=0	 &&
                		   RecuperaValoreParametro(FatturaElement,"ti_causale_emissione").compareTo("T")!=0){
                		   	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
                		   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
                	   }
                	   if (RecuperaValoreParametro(FatturaElement,"ti_fattura").compareTo("C")!=0 &&
                    	   RecuperaValoreParametro(FatturaElement,"ti_fattura").compareTo("F")!=0){
                		   		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
                    		   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
                    	}
                	try{
                	    par=RecuperaValoreParametro(FatturaElement,"ti_fattura");
   	                    if(par!=null && ((par.compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))){
   	                    	testata=new Nota_di_credito_attivaBulk();
   	                    	riga = new Nota_di_credito_attiva_rigaBulk();
   	                    }
   	                    else{
   	                    	testata=new Fattura_attiva_IBulk();
   	                    	riga = new Fattura_attiva_rigaIBulk();
   	                    }
   	                	testata.setToBeCreated();
	        	    	testata.setEsercizio(new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
	        	    	testata.setTi_fattura(par);
	                    par=RecuperaValoreParametro(FatturaElement,"esercizio");
	                    try{
	                    	testata.setEsercizio(new Integer(par));
	                    }catch (java.lang.NumberFormatException e) {
		                  	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
		                   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
		        		}
	                    par=RecuperaValoreParametro(FatturaElement,"utcr");
                	    testata.setUtcr(par);
	                    par=RecuperaValoreParametro(FatturaElement,"cd_cds_origine");
                	    testata.setCd_cds_origine(par);
	                    UserContext userContext = new WSUserContext(testata.getUtcr(),null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),testata.getCd_uo_origine(),testata.getCd_cds_origine(),null);
                	    CdsBulk cds=new CdsBulk(par);
                	    cds=(CdsBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,cds));
                	    if(cds==null){
                	    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_130.toString(),0);
		                   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_130),0);
                	    }
	                    par=RecuperaValoreParametro(FatturaElement,"cd_uo_origine");
	                    Unita_organizzativaBulk uo=new Unita_organizzativaBulk(par);
	                    uo=(Unita_organizzativaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,uo));
	                    if(uo==null){
	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_131.toString(),0);
	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_131),0);
	                    }
	                    if(uo.getCd_cds().compareTo(testata.getCd_cds_origine())!=0){
	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_135.toString(),0);
	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_135),0);
	                    }
                	    testata.setCd_uo_origine(par);
                	    try{
	                    par=RecuperaValoreParametro(FatturaElement,"pg_fattura_esterno");
                	    testata.setPg_fattura_esterno(new Long(par));
                        }catch (java.lang.NumberFormatException e) {
		                  	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
		                   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
		        		}
	      
                	    Fattura_attivaBulk fatturaAttiva;
                	    if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)!=0)){
                	    	fatturaAttiva = new Fattura_attiva_IBulk();
	            			fatturaAttiva.setEsercizio(testata.getEsercizio());
	            			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
	            			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
	            			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
                	    }else
                	    {
                	    	fatturaAttiva = new Nota_di_credito_attivaBulk();
	            			fatturaAttiva.setEsercizio(testata.getEsercizio());
	            			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
	            			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
	            			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
	            			fatturaAttiva.setTi_fattura(testata.getTi_fattura());
                	    }
                	    par=RecuperaValoreParametro(FatturaElement,"fl_intra_ue");
                	    testata.setFl_intra_ue(par.compareTo("Y")==0?true:false);
	                    String par2=RecuperaValoreParametro(FatturaElement,"fl_extra_ue");
                	    testata.setFl_extra_ue(par2.compareTo("Y")==0?true:false);
	                    String par3=RecuperaValoreParametro(FatturaElement,"fl_san_marino");
                	    testata.setFl_san_marino(par3.compareTo("Y")==0?true:false);
                	    if (!((par.compareTo("Y")==0 && par2.compareTo("N")==0 && par3.compareTo("N")==0)||
                	   		(par.compareTo("N")==0 && par2.compareTo("Y")==0 && par3.compareTo("N")==0)||
                	   		(par.compareTo("N")==0 && par2.compareTo("N")==0 && par3.compareTo("Y")==0)||
                	   		(par.compareTo("N")==0 && par2.compareTo("N")==0 && par3.compareTo("N")==0)))
                	    {
                	    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_129.toString(),0);
                 		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_129),0);
                	    }
                	    userContext.setTransactional(true);
	        		    // richiesta dall'inizializza (Fattura_attiva_IBulk)
	        	    	 if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))
	        	    	    testata = (Nota_di_credito_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).inizializzaBulkPerInserimento(userContext,testata);
	        	    	 else
	        	    		 testata = (Fattura_attiva_IBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).inizializzaBulkPerInserimento(userContext,testata);
	        	    	 ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).setSavePoint(context,"Fattura_automatica");
	                    //-------                    

	            		// potrebbe non essere univoca per tipologia	            		
	            		if ((((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).VerificaDuplicati(userContext,fatturaAttiva))){
	            			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_102.toString(),0);
	             		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_102),0);
	            		}
	                    par=RecuperaValoreParametro(FatturaElement,"cd_tipo_sezionale");
                    	testata.setTipo_sezionale(new Tipo_sezionaleBulk(par));
                    	boolean esiste = false;
                    	for (Iterator i=testata.getSezionali().iterator();!esiste && i.hasNext();){
                    		Tipo_sezionaleBulk tipo = (Tipo_sezionaleBulk)i.next();
                    		if(tipo.getCd_tipo_sezionale().compareTo(testata.getTipo_sezionale().getCd_tipo_sezionale())==0)
                    			esiste=true;
                    	}
                	    if (!esiste){
                	    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_127.toString(),0);
	             		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_127),0);
                	    }
	                    par=RecuperaValoreParametro(FatturaElement,"dt_registrazione");
                	    try {
							testata.setDt_registrazione(new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse(par).getTime()));
						} catch (ParseException e) {
							AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_112.toString(),0);
		           		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_112),0);
						}
	                    par=RecuperaValoreParametro(FatturaElement,"ti_causale_emissione");
                	    testata.setTi_causale_emissione(par);
	                    par=RecuperaValoreParametro(FatturaElement,"fl_liquidazione_differita");
                	    testata.setFl_liquidazione_differita(par.compareTo("Y")==0?true:false);
	                    par=RecuperaValoreParametro(FatturaElement,"ds_fattura_attiva");
                	    testata.setDs_fattura_attiva(par);
	                    
	                    par=RecuperaValoreParametro(FatturaElement,"cd_terzo");
	                    if(par!=null){
	                    	try{
		                	    testata.setCliente(new TerzoBulk(new Integer(par)));
		                	    testata.setCliente((TerzoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,testata.getCliente())));
		                    	if (testata.getCliente()==null){
		                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_105.toString(),0);
		                 		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_105),0);
		                    	}
		                    	else{
		                    		testata.setCd_terzo(new Integer(par));
		                	    	testata=(Fattura_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaTerzo(userContext,testata,testata.getCliente());
		                	    	if(testata.getCliente().isAnagraficoScaduto()||testata.getCliente().getTi_terzo().compareTo(TerzoBulk.CREDITORE)==0){
		                	    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_125.toString(),0);
			                 		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_125),0);
		                	    	}
		                	    	if((testata.getFl_liquidazione_differita().booleanValue()&& testata.getFl_liquidazione_differita().compareTo(testata.getCliente().getAnagrafico().getFl_fatturazione_differita())!=0)){
		                	    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_124.toString(),0);
			                 		    AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_124),0);
		                	    	}
		                    	}
	   	                    }catch (java.lang.NumberFormatException e) {
	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_115.toString(),0);
	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_115),0);
							}	
	                    }
	                    
	                    par=RecuperaValoreParametro(FatturaElement,"cd_terzo_uo_cds");
	                    try{
	                    	if(testata.getCd_terzo_uo_cds().compareTo(new Integer(par))!=0){
	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_133.toString(),0);
	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_133),0);
	                    	}
	                    }catch (java.lang.NumberFormatException e) {
   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_115.toString(),0);
   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_115),0);
						}
	                    par=RecuperaValoreParametro(FatturaElement,"cd_modalita_pag_uo_cds");
                	    testata.setCd_modalita_pag_uo_cds(par);
                	    testata.setModalita_pagamento_uo(((Rif_modalita_pagamentoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Rif_modalita_pagamentoBulk(par)))));
                	    if(testata.getModalita_pagamento_uo()==null)
                	    {
                	    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_132.toString(),0);
   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_132),0);
                	    }
	                    
	                    par=RecuperaValoreParametro(FatturaElement,"pg_banca_uo_cds");
	                    try{
	                    	testata.setPg_banca_uo_cds(new Long(par));
	                    }catch (java.lang.NumberFormatException e) {
   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
						}	
                	    testata.setBanca_uo(new BancaBulk(testata.getCd_terzo_uo_cds(),testata.getPg_banca_uo_cds()));
                	     if (testata.getBanca_uo()==null){
                 			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_134.toString(),0);
   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_134),0);
                 		 }
                	     java.util.List l=(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findListaBancheWS(userContext,testata.getCd_terzo_uo_cds().toString(),testata.getModalita_pagamento_uo().getCd_modalita_pag(),"","",""));
                 		 esiste=false;
                 		 for(Iterator i=l.iterator();!esiste && i.hasNext();){
                 			 BancaBulk banca=(BancaBulk)i.next();
                 			 if (banca.getPg_banca().compareTo(testata.getPg_banca_uo_cds())==0)
                 				 esiste=true;
                 		 }
                		 if(!esiste){
                			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_134.toString(),0);
  	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_134),0);
                		 }
                		 
	                    if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
	                    	par=RecuperaValoreParametro(FatturaElement,"cd_modalita_pag");
	                    	if(par!=null){
	                    	    ((Nota_di_credito_attivaBulk)testata).setModalita_pagamento((Rif_modalita_pagamentoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Rif_modalita_pagamentoBulk(par))));
	                    	    if(((Nota_di_credito_attivaBulk)testata).getModalita_pagamento()==null)
	                    	    {
	                    	    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_111.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_111),0);
	                    	    }
	                    		testata.setCd_modalita_pag(par);
	                    	}
                    		else
	                    	{
	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_111.toString(),0);
	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_111),0);
    	                    }
	                    	par=RecuperaValoreParametro(FatturaElement,"pg_banca");
	                    	if(par!=null){
	                    		try{
	                     		  ((Nota_di_credito_attivaBulk)testata).setBanca((new BancaBulk(testata.getCd_terzo(),new Long(par))));
	                     		  testata.setPg_banca(new Long(par));
	                     		 ((Nota_di_credito_attivaBulk)testata).setBanca((BancaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,((Nota_di_credito_attivaBulk)testata).getBanca())));
	                     		 if (((Nota_di_credito_attivaBulk)testata).getBanca()==null){
	                     			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_118.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_118),0);
	                     		 }
	                     		 l=(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findListaBancheWS(userContext,testata.getCd_terzo().toString(),((Nota_di_credito_attivaBulk)testata).getModalita_pagamento().getCd_modalita_pag(),"","",""));
	                     		 esiste=false;
	                     		 for(Iterator i=l.iterator();!esiste&&i.hasNext();){
	                     			 BancaBulk banca=(BancaBulk)i.next();
	                     			 if (banca.getPg_banca().compareTo(testata.getPg_banca())==0)
	                     				 esiste=true;
	                     		 }
	                     		 if(!esiste){
	                     			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_118.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_118),0);
	                     		 }
	                    		}catch (java.lang.NumberFormatException e) {
		   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
								}
	                    	}
	                    	else
	                    	{
	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_110.toString(),0);
	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_110),0);
    	                    }
	                    }
	                    par=RecuperaValoreParametro(FatturaElement,"cd_divisa");
	                    testata.setCd_divisa(par);
	                    par=RecuperaValoreParametro(FatturaElement,"cambio");
                	    testata.setCambio(new BigDecimal(par));
	                    par=RecuperaValoreParametro(FatturaElement,"note");
	                    if(par!=null)
	                	    testata.setNote(par);
	                    testata.validate();
	                    NodeList listOfRighe =FatturaElement.getElementsByTagName("righefat"); 
		                for(int r=0; r<listOfRighe.getLength() ; r++){
		                	 if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))
		                		riga=new Nota_di_credito_attiva_rigaBulk();
		                	else
		                		riga=new Fattura_attiva_rigaIBulk();
		                	 
	                    	testata.addToFattura_attiva_dettColl(riga);
	                    	riga.setToBeCreated();
	                    	
                    		Node RigheNode = listOfRighe.item(r);
    	                    Element RigheElement = (Element)RigheNode;
    	                    //?? da aggiungere controlli
		                	  if (RecuperaValoreParametro(RigheElement,"fl_iva_forzata")==null||
		                          RecuperaValoreParametro(RigheElement,"cd_voce")==null||
		                          (RecuperaValoreParametro(RigheElement,"cd_tariffario")==null && 
		                           RecuperaValoreParametro(RigheElement,"cd_voce_iva")==null &&
		                           RecuperaValoreParametro(RigheElement,"prezzo_unitario")==null &&
		                           RecuperaValoreParametro(RigheElement,"quantita")==null)){
		   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
	                          }
		                	  else{
	    	                    if(testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO)){
		    	                    par=RecuperaValoreParametro(RigheElement,"cd_tariffario");
		    	                    if(par!=null){
		    	                    	riga.setCd_tariffario(par);
		    	                    	//r.p.???
		    	                    	riga.setTariffario(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findTariffario(userContext,riga));
		    	                    	if(riga.getTariffario()==null){
		    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_128.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_128),0);
		    	                    	}
		    	                    	riga.setVoce_iva(riga.getTariffario().getVoce_iva());
		    	                    	riga.setVoce_iva((Voce_ivaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getVoce_iva())));
		    	                    	if (riga.getVoce_iva()==null){
		    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_106.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_106),0);
		    	                    	}else
		    	                    	{
		    	                    	riga.setPrezzo_unitario(riga.getTariffario().getIm_tariffario());
		    	                    	riga.setQuantita(new BigDecimal(riga.getTariffario().getUnita_misura()));
		    	                    	riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
		    	                    	riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).divide(new BigDecimal(100)));
		    	                    	riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
		    	                    	riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
		    	                    	riga.setDs_riga_fattura(riga.getTariffario().getDs_tariffario());
		    	                    	}
		    	                    }else{
			   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_103.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_103),0);
		    	                    }
	    	                    }
	    	                    if( !testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO)){
		    	                    par=RecuperaValoreParametro(RigheElement,"prezzo_unitario");
		    	                    try{
		    	                    	if(par!=null)
		    	                    		riga.setPrezzo_unitario(new BigDecimal(par));
		    	                    }catch (java.lang.NumberFormatException e) {
			   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_113.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_113),0);
									}
		    	                    par=RecuperaValoreParametro(RigheElement,"quantita");
		    	                    try{
			    	                    	if(par!=null)
			    	                    		riga.setQuantita(new BigDecimal(par));
			    	                    }catch (java.lang.NumberFormatException e) {
				   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_114.toString(),0);
				   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_114),0);
									}	
			    	                
		  	    	                par=RecuperaValoreParametro(RigheElement,"cd_voce_iva");
		    	                    if(par!=null){
		    	                    	riga.setVoce_iva(new Voce_ivaBulk(par));
		    	                    	riga.setCd_voce_iva(par);
		    	                    }
	    	                    	riga.setVoce_iva((Voce_ivaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getVoce_iva())));
	    	                    	if (riga.getVoce_iva()==null){
	    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_106.toString(),0);
	    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_106),0);
	    	                    	}else{
			  	    	                riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
		    	                    	riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).divide(new BigDecimal(100)));
		    	                    	riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
		    	                    	riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
		    	                    	par=RecuperaValoreParametro(RigheElement,"ds_riga_fattura");
		  	    	                    if(par!=null)
		  	    	                    	riga.setDs_riga_fattura(par);
		    	                    	}
		    	                    }
	    	                    if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
	    	                    	 par=RecuperaValoreParametro(RigheElement,"pg_fattura_assncna_fin");
	    	                    	  par2=RecuperaValoreParametro(RigheElement,"pg_riga_assncna_fin");
	    	                    	  par3=RecuperaValoreParametro(RigheElement,"esercizio_assncna_fin");
		    	                    if(par!=null && par2!=null && par3!=null){
		    	                    	try{
			    	                    	((Nota_di_credito_attiva_rigaBulk)riga).setRiga_fattura_associata((Fattura_attiva_rigaIBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Fattura_attiva_rigaIBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),new Integer(par3),new Long(par),new Long(par2)))));
			    	                    	Fattura_attiva_rigaIBulk rigaFP = ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata();
			    	                    	if (rigaFP!=null){
				    	                    	java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(riga.getIm_totale_divisa());
				    	                    	if(nuovoImportoDisponibile.compareTo(new BigDecimal("0"))<0)
				    	                    	{
						   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_117.toString(),0);
						   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_117),0);
					    	                    }else
					    	                    {
				    	                    	rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
				    	                    	((Nota_di_credito_attiva_rigaBulk)riga).setRiga_fattura_associata(rigaFP);
					    	                    }
			    	                    	}
		    	                    	}catch (java.lang.NumberFormatException e) {
		    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_109.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_109),0);
		    							}	
		    	                    }
		    	                    // non dovrebbe servire questo controllo!
		    	                    else{
			   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_109.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_109),0);
		    	                    }
		    	                    if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata()==null){
		    	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_109.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_109),0);
		    	                    }else
		    	                    {
		    	                    	//controllo cliente coerente fattura - NC
		    	                    	((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().setFattura_attivaI((Fattura_attiva_IBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,
		    	                    			((Fattura_attiva_IBulk)new Fattura_attiva_IBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getEsercizio(),((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getPg_fattura_attiva())))));
		    	                    	((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().setCliente((TerzoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getCliente())));
		    	                    	if (testata.getCliente().getCd_terzo().compareTo(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getCd_terzo())!=0){
		    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_116.toString(),0);
			   	                    		AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_116),0);
		    	                    	}
		    	                    	//Controllo codice iva coerente fattura - NC
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_voce_iva()!=null &&
		    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_voce_iva().compareTo(riga.getCd_voce_iva())!=0)
		    	                    	{
		    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_120.toString(),0);
				   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_120),0);
		    	                    	}
		    	                    	//controllo causale emissione coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTi_causale_emissione()!=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTi_causale_emissione().compareTo(riga.getFattura_attiva().getTi_causale_emissione())!=0)
				    	               	{
			    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_121.toString(),0);
					   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_121),0);
				    	               	}
		    	                    	//controllo sezionale coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTipo_sezionale()!=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTipo_sezionale().getCd_tipo_sezionale().compareTo(riga.getFattura_attiva().getTipo_sezionale().getCd_tipo_sezionale())!=0)
				    	               	{
			    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_122.toString(),0);
					   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_122),0);
				    	               	}
		    	                    	//controllo tariffario coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_tariffario()!=null &&
				    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_tariffario().compareTo(riga.getCd_tariffario())!=0)
				    	               	{
			    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_123.toString(),0);
					   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_123),0);
				    	               	}
		    	                    	//controllo flag coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    			(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_extra_ue().compareTo(riga.getFattura_attiva().getFl_extra_ue())!=0 ||
    	                    					((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_intra_ue().compareTo(riga.getFattura_attiva().getFl_intra_ue())!=0||
    	                    					((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_san_marino().compareTo(riga.getFattura_attiva().getFl_san_marino())!=0||
    	                    					((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_liquidazione_differita().compareTo(riga.getFattura_attiva().getFl_liquidazione_differita())!=0))
				    	               	{
			    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_126.toString(),0);
					   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_126),0);
				    	               	}
		    	                    }
		    	                    if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
		    	        	    	    testata.setCd_cds(testata.getCd_cds_origine());
		    		                   	testata.setCd_unita_organizzativa(testata.getCd_uo_origine());
		    	        	    	 }
	    	                    }
	    	                    par=RecuperaValoreParametro(RigheElement,"fl_iva_forzata");
	    	                    riga.setFl_iva_forzata(par.compareTo("Y")==0?true:false);
	    	                    riga.setUtcr(testata.getUtcr());

	    	                    AssociaValoreParamentro(FatturaElement, "progressivo_riga", riga.getProgressivo_riga().toString(),r);
	    	              if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){      
	    	                    AccertamentoBulk acc=new AccertamentoBulk(); 
	    	                    acc.setToBeCreated();
	    	                    acc.setEsercizio(riga.getEsercizio());
	    	                    acc.setEsercizio_originale(riga.getEsercizio());
	    	                    acc.setCd_cds(riga.getCd_cds());
	    	                    acc.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
	    	                    acc.setCd_cds_origine(testata.getCd_cds_origine());
	    	                    acc.setCd_uo_origine(testata.getCd_uo_origine());
	    	                    acc.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR);
	    	                    acc.setFl_pgiro(new Boolean(false));
	    	                    acc.setRiportato("N");
	    	                    acc.setFromDocAmm(new Boolean(true));
	    	                    acc.setFl_calcolo_automatico(new Boolean(false));
	    	                    acc.setUtcr(testata.getUtcr());
	    	                    par=RecuperaValoreParametro(RigheElement,"cd_voce");
	    	                    it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce=new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();
	    	                    if(par!=null){
	    	                    	voce.setEsercizio(acc.getEsercizio());
	    	                    	voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	    	                    	voce.setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	    	                    	voce.setCd_voce(par);
	    	                    	voce=((it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,voce)));
	    	                    }
	    	                    if (voce==null){
	    	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_107.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_107),0);
    	                    	}
	    	                    else{
	    	                    	acc.setCapitolo(new V_voce_f_partita_giroBulk(voce.getCd_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione()));
	    	                    	acc.setTi_appartenenza(voce.getTi_appartenenza());
	    	                    	acc.setTi_gestione(voce.getTi_gestione());
	    	                    	acc.setCd_elemento_voce(voce.getCd_elemento_voce());
	    	                    	acc.setCd_voce(voce.getCd_voce());
    	                    	}
	    	                    acc.setDt_registrazione(testata.getDt_registrazione()); 
	    	                    par=RecuperaValoreParametro(RigheElement,"ds_accertamento");
	    	                    if(par!=null)
	    	                    	acc.setDs_accertamento(par);
	    	                    //??? importo riga?????	
    	                    	acc.setIm_accertamento(riga.getIm_totale_divisa());	
	 	    	                acc.setCd_terzo(testata.getCd_terzo());    	
	 	    	                acc.setEsercizio_competenza(testata.getEsercizio());
	 	    	                
	 	    	                Accertamento_scadenzarioBulk acc_scadenza=new Accertamento_scadenzarioBulk();
	 	    	                acc_scadenza.setUtcr(acc.getUtcr());
	 	    	                acc_scadenza.setToBeCreated();
	 	    	               
	 	    	                acc_scadenza.setAccertamento(acc);
	 	    	                acc_scadenza.setDt_scadenza_incasso(acc.getDt_registrazione());
	 	    	                acc_scadenza.setDs_scadenza( acc.getDs_accertamento() );
	 	    	                acc.addToAccertamento_scadenzarioColl(acc_scadenza);
	 	    	                acc_scadenza.setIm_scadenza(acc.getIm_accertamento());
	 	    	                acc_scadenza.setIm_associato_doc_amm(riga.getIm_totale_divisa());
	 	    	                acc_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
	 	    	         
	 	    	                par=RecuperaValoreParametro(RigheElement,"esercizio_contratto");
	    	                    par1=RecuperaValoreParametro(RigheElement,"stato_contratto");
	    	                    par2=RecuperaValoreParametro(RigheElement,"pg_contratto");
	    	                    if(par!=null && par1!=null && par2!=null){
	    	                    	try{
	    	                    	acc.setContratto(new ContrattoBulk(new Integer(par),par1,new Long(par2)));
	    	                    	acc.setContratto((ContrattoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,acc.getContratto())));
	    	                    	if (acc.getContratto()==null){
	    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_104.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_104),0);
	    	                    	}else{
	    	                    		acc.setCheckDisponibilitaContrattoEseguito(true);
	    	                    	}
	    	                    	}catch (java.lang.NumberFormatException e) {
	    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_104.toString(),0);
	    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_104),0);
	    							}	
	    	                    }else{
	    	                    	if((par!=null || par1!=null || par2!=null) && (par==null || par1==null || par2==null)){
	    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_108.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_108),0);
	    	                    	}
	    	                    }
	    	                    
	    	                    NodeList listOfScad =RigheElement.getElementsByTagName("righescadvoc"); 
	    		                for(int v=0; v<listOfScad.getLength() ; v++){
	                        		Node ScadNode = listOfScad.item(v);
	        	                    Element ScadElement = (Element)ScadNode;
	        	                    if (RecuperaValoreParametro(ScadElement,"im_voce")==null||
	        	                    	RecuperaValoreParametro(ScadElement,"cdr")==null||
	      		                       	RecuperaValoreParametro(ScadElement,"gae")==null){
	        	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
	      	                         }
	        	                    else{
		        	                    Accertamento_scad_voceBulk acc_scad_voce=new Accertamento_scad_voceBulk();
		        	                    acc_scad_voce.setUtcr(testata.getUtcr());
		        	                    acc_scad_voce.setToBeCreated();
		        	                    acc_scad_voce.setAccertamento_scadenzario(acc_scadenza);
		        	                    par=RecuperaValoreParametro(ScadElement,"im_voce");
		        	                    try{
		        	                    	acc_scad_voce.setIm_voce(new BigDecimal(par));	
		        	                    }catch (java.lang.NumberFormatException e) {
		    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
		    							}	
	        	                    	par=RecuperaValoreParametro(ScadElement,"cdr");
	        	                    	//
	        	                    	CdrBulk cdr_db= new CdrBulk();
	        	            			cdr_db=(((CdrBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdrBulk(par)))));
	        	            			if (cdr_db==null){
	        	            				AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_136.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_136),0);
	        	            			}
	        	                    	par1=RecuperaValoreParametro(ScadElement,"gae");
	        	                    	acc_scad_voce.setLinea_attivita(((WorkpackageBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new WorkpackageBulk(par,par1)))));
	        	                    	
	        	                    	if(acc_scad_voce.getLinea_attivita()!=null){
	        	                    		if(acc_scad_voce.getLinea_attivita().getTi_gestione().compareTo(acc.getTi_gestione())!=0){
	        	                    			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_138.toString(),0);
		        	                    		AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_138),0);
	        	                    		}
	        	                         	acc_scadenza.getAccertamento_scad_voceColl().add((acc_scad_voce));
	        	                    	}else{
	        	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_137.toString(),0);
	        	                    		AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_137),0);
	        	                    	}
	        	                    }
	    		                }
	    	                    acc=(AccertamentoBulk) ((AccertamentoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession",AccertamentoComponentSession.class)).creaConBulk(userContext,acc);
	                    	    AssociaValoreParamentro(FatturaElement, "pg_accertamento_scadenzario", ((Accertamento_scadenzarioBulk)(acc.getAccertamento_scadenzarioColl().get(0))).getPg_accertamento_scadenzario().toString(),r);
			                    for (Iterator i=acc.getAccertamento_scadenzarioColl().iterator();i.hasNext();){
			                    	BulkList righesel=new BulkList();
			                    	righesel.add(riga);
			                    	Accertamento_scadenzarioBulk scad=(Accertamento_scadenzarioBulk)i.next();
			                        testata = (Fattura_attiva_IBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).contabilizzaDettagliSelezionati(userContext,testata,righesel,scad);
			                    	//testata.addToFattura_attiva_accertamentiHash(scad, riga);
			                    	testata.addToDefferredSaldi(scad.getAccertamento(), scad.getAccertamento().getSaldiInfo());
			                    }
	    	              }//fine non Nota Credito
	    	              else{ ///Nota Credito
	    	            	    ObbligazioneBulk obb=new ObbligazioneBulk(); 
	    	                    obb.setToBeCreated();
	    	                    obb.setEsercizio(riga.getEsercizio());
	    	                    obb.setEsercizio_originale(riga.getEsercizio());
	    	                    obb.setCds((it.cnr.contab.config00.sto.bulk.CdsBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdsBulk(riga.getCd_cds()))));
	    	                    obb.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
	    	                    obb.setCd_cds_origine(testata.getCd_cds_origine());
	    	                    obb.setCd_uo_origine(testata.getCd_uo_origine());
	    	                    obb.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB);
	    	                    obb.setFl_pgiro(new Boolean(false));
	    	                    obb.setRiportato("N");
	    	                    obb.setFromDocAmm(new Boolean(true));
	    	                    obb.setFl_calcolo_automatico(new Boolean(false));
	    	                    obb.setFl_spese_costi_altrui(new Boolean(false));
	    	                    obb.setFl_gara_in_corso(new Boolean(false));
	    	                    obb.setUtcr(testata.getUtcr());
	    	                    par=RecuperaValoreParametro(RigheElement,"cd_voce");
	    	                    it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce=new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();
	    	                    if(par!=null){
	    	                    	voce.setEsercizio(obb.getEsercizio());
	    	                    	voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
	    	                    	voce.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	    	                    	voce.setCd_voce(par);
	    	                    	voce.setCd_unita_organizzativa(testata.getCd_uo_origine());
	    	                    	voce=((it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,voce)));
	    	                    }
	    	                    if (voce==null){
	    	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_107.toString(),0);
		   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_107),0);
    	                    	}
	    	                    else{
	    	                    	obb.setElemento_voce(((Elemento_voceBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Elemento_voceBulk(voce.getCd_elemento_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())))));
	    	                    	obb=(ObbligazioneBulk) ((ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class)).listaCapitoliPerCdsVoce(userContext,obb);
	    	                    	obb.setCapitoliDiSpesaCdsSelezionatiColl(obb.getCapitoliDiSpesaCdsColl());
    	                    	}
	    	                    obb.setDt_registrazione(testata.getDt_registrazione()); 
	    	                    par=RecuperaValoreParametro(RigheElement,"ds_obbligazione");
	    	                    if(par!=null)
	    	                    	obb.setDs_obbligazione(par);
	    	                    par=RecuperaValoreParametro(RigheElement,"motivazione");
	    	                    if(par!=null)
	    	                    	obb.setMotivazione(par);
	    	                    //??? importo riga?????	
    	                    	obb.setIm_obbligazione(riga.getIm_totale_divisa());	
    	                    	obb.setIm_costi_anticipati( new java.math.BigDecimal(0));
    	                    	//??
    	                    	obb.setStato_obbligazione(ObbligazioneBulk.STATO_OBB_DEFINITIVO);
    	                    	obb.setUser(obb.getUtcr());
	 	    	                obb.setCd_terzo(testata.getCd_terzo());    	
	 	    	                obb.setEsercizio_competenza(testata.getEsercizio());
	 	    	                
	 	    	                Obbligazione_scadenzarioBulk obb_scadenza=new Obbligazione_scadenzarioBulk();
	 	    	                obb_scadenza.setUtcr(obb.getUtcr());
	 	    	                obb_scadenza.setToBeCreated();
	 	    	               
	 	    	                obb_scadenza.setObbligazione(obb);
	 	    	                obb_scadenza.setDt_scadenza(obb.getDt_registrazione());
	 	    	                obb_scadenza.setDs_scadenza( obb.getDs_obbligazione() );
	 	    	                obb.addToObbligazione_scadenzarioColl(obb_scadenza);
	 	    	                obb_scadenza.setIm_scadenza(obb.getIm_obbligazione());
	 	    	                obb_scadenza.setIm_associato_doc_amm(riga.getIm_totale_divisa());
	 	    	                obb_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
	 	    	         
	 	    	                par=RecuperaValoreParametro(RigheElement,"esercizio_contratto");
	    	                    par1=RecuperaValoreParametro(RigheElement,"stato_contratto");
	    	                    par2=RecuperaValoreParametro(RigheElement,"pg_contratto");
	    	                    if(par!=null && par1!=null && par2!=null){
	    	                    	try{
		    	                    	obb.setContratto(new ContrattoBulk(new Integer(par),par1,new Long(par2)));
		    	                    	obb.setContratto((ContrattoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,obb.getContratto())));
		    	                    	if (obb.getContratto()==null){
		    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_104.toString(),0);
				   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_104),0);
		    	                    	}
		    	                    	/// ????  
		    	                    	else{
		    	                    		obb.setCheckDisponibilitaContrattoEseguito(true);
		    	                    	}
	    	                    	}catch (java.lang.NumberFormatException e) {
	    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_104.toString(),0);
	    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_104),0);
	    							}		
	    	                    }else{
	    	                    	if((par!=null || par1!=null || par2!=null) && (par==null || par1==null || par2==null)){
	    	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_108.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_108),0);
	    	                    	}
	    	                    }
	 	    	          		NodeList listOfScad =RigheElement.getElementsByTagName("righescadvoc"); 
	    		                for(int v=0; v<listOfScad.getLength() ; v++){
	                        		Node ScadNode = listOfScad.item(v);
	        	                    Element ScadElement = (Element)ScadNode;
	        	                    if (RecuperaValoreParametro(ScadElement,"im_voce")==null||
	        	                    	RecuperaValoreParametro(ScadElement,"cdr")==null||
	      		                       	RecuperaValoreParametro(ScadElement,"gae")==null){
	        	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_101.toString(),0);
			   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_101),0);
	      	                        }
	        	                    else{
		        	                    Obbligazione_scad_voceBulk obb_scad_voce=new Obbligazione_scad_voceBulk();
	 	    	                		obb_scad_voce.setUtcr(testata.getUtcr());
	 	    	               			obb_scad_voce.setToBeCreated();
	 	    	              			obb_scad_voce.setObbligazione_scadenzario(obb_scadenza);
		        	                    par=RecuperaValoreParametro(ScadElement,"im_voce");
		        	                    try{
		        	                    	obb_scad_voce.setIm_voce(new BigDecimal(par));
		        	                    }catch (java.lang.NumberFormatException e) {
		    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
		    							}	
		        	                    obb_scad_voce.setCd_voce(voce.getCd_voce());
		        	                    obb_scad_voce.setTi_gestione(voce.getTi_gestione());
		        	                    obb_scad_voce.setTi_appartenenza(voce.getTi_appartenenza());
	        	                    	par=RecuperaValoreParametro(ScadElement,"cdr");
	        	                    	CdrBulk cdr_db= new CdrBulk();
	        	            			cdr_db=(((CdrBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdrBulk(par)))));
	        	            			if (cdr_db==null){
	        	            				AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_136.toString(),0);
		    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_136),0);
	        	            			}
	        	                    	par1=RecuperaValoreParametro(ScadElement,"gae");
	        	                    	obb_scad_voce.setLinea_attivita(((WorkpackageBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new WorkpackageBulk(par,par1)))));
	        	                    	
	        	                    	if(obb_scad_voce.getLinea_attivita()!=null){
	        	                    		if(obb_scad_voce.getLinea_attivita().getTi_gestione().compareTo(obb.getTi_gestione())!=0){
	        	                    			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_138.toString(),0);
		        	                    		AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_138),0);
	        	                    		}
		        	                    	Linea_attivitaBulk nuovaLatt = new it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk();
		        	            			nuovaLatt.setLinea_att( obb_scad_voce.getLinea_attivita());
		        	            			if(obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza().compareTo(new BigDecimal(0))!=0)
		        	            				nuovaLatt.setPrcImputazioneFin( obb_scad_voce.getIm_voce().multiply(new BigDecimal(100)).divide( obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP ));
		        	            			nuovaLatt.setObbligazione(obb);
		        	            			obb.getNuoveLineeAttivitaColl().add(nuovaLatt);
		        	            			obb_scadenza.getObbligazione_scad_voceColl().add((obb_scad_voce));
	        	                    	}else{
	        	                    		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_137.toString(),0);
	        	                    		AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_137),0);
	        	                    	}
	        	                    }
	    		                }
	    	                    obb=(ObbligazioneBulk) ((ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class)).creaConBulk(userContext,obb);
	                    	    AssociaValoreParamentro(FatturaElement, "pg_obbligazione_scadenzario", ((Obbligazione_scadenzarioBulk)(obb.getObbligazione_scadenzarioColl().get(0))).getPg_obbligazione_scadenzario().toString(),r);
	                    	    obb_scadenza.setObbligazione(obb);
		                    	Nota_di_credito_attivaBulk nc=(Nota_di_credito_attivaBulk)testata;
		                    	Nota_di_credito_attiva_rigaBulk riga_nc=(Nota_di_credito_attiva_rigaBulk)(Fattura_attiva_rigaBulk)riga;
		                    	riga_nc.setObbligazione_scadenzario(obb_scadenza);
		                    	java.util.Hashtable relationsHash = new java.util.Hashtable();
		            			relationsHash.put(riga_nc, riga_nc.getObbligazione_scadenzario());

		                    	nc=((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).stornaDettagli(userContext,nc,nc.getFattura_attiva_dettColl(),relationsHash);
			                    nc.addToDefferredSaldi(obb_scadenza.getObbligazione(), obb_scadenza.getObbligazione().getSaldiInfo());
			                    testata=nc;
	    	              }// Fine Nota Credito
		                }//fine else campi null
	                    }//fine righe
		                par=RecuperaValoreParametro(FatturaElement,"cod_errore");
		                if(par==null){
		                	testata = (Fattura_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).creaConBulk(userContext,testata);
		                	AssociaValoreParamentro(FatturaElement, "pg_fattura_attiva", testata.getPg_fattura_attiva().toString(),0);
		                	listOfRighe =FatturaElement.getElementsByTagName("righefat"); 
		                	for(int r=0; r<listOfRighe.getLength() ; r++){
		                		Node RigheNode = listOfRighe.item(r);
		                		Element RigheElement = (Element)RigheNode;
		                		try{
			                		if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){ 
			                			Fattura_attiva_rigaIBulk riga_fat = (Fattura_attiva_rigaIBulk)testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
	    	                    			new Fattura_attiva_rigaIBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),testata.getEsercizio(),testata.getPg_fattura_attiva(),new Long(RecuperaValoreParametro(RigheElement,"progressivo_riga")))));
			                			AssociaValoreParamentro(FatturaElement, "pg_accertamento", ((Accertamento_scadenzarioBulk)riga_fat.getAccertamento_scadenzario()).getPg_accertamento().toString(),r);
			                		}else{
			                			Nota_di_credito_attiva_rigaBulk riga_fat = (Nota_di_credito_attiva_rigaBulk)testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
	    	                    			new Nota_di_credito_attiva_rigaBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),testata.getEsercizio(),testata.getPg_fattura_attiva(),new Long(RecuperaValoreParametro(RigheElement,"progressivo_riga")))));
			                			AssociaValoreParamentro(FatturaElement, "pg_obbligazione", ((Obbligazione_scadenzarioBulk)riga_fat.getObbligazione_scadenzario()).getPg_obbligazione().toString(),r);
			                		}
		                		}catch (java.lang.NumberFormatException e) {
		                			((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
    	   	                    	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
    	   	                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
    							}		
		                	}
		                }
		               else
		            	   ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
	        	    }catch (java.lang.NumberFormatException e) {
	        	    	((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
	                  	AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_119.toString(),0);
	                   	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_119),0);
	        		}
                	catch (ApplicationException e){
           			   ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
               			AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_100.toString(),0);
                    	AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_100)+e.getMessage(),0);
                   	}
                	catch (Exception e){
             			((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
                		AssociaValoreParamentro(FatturaElement,"cod_errore", Costanti.ERRORE_FA_999.toString(),0);
                        AssociaValoreParamentro(FatturaElement,"desc_errore",Costanti.erroriFA.get(Costanti.ERRORE_FA_999),0);
                        
	   	          	}      
                 }//else campi not null
	           }//fine fattura      
	           DOMSource domSource = new DOMSource(doc);
	           StringWriter domWriter = new StringWriter();
	           StreamResult streamResult = new StreamResult(domWriter);
	           
	           TransformerFactory tf = TransformerFactory.newInstance();
	           Transformer serializer = tf.newTransformer();
	           serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
	           serializer.setOutputProperty(OutputKeys.INDENT,"yes");
	           serializer.setOutputProperty(OutputKeys.STANDALONE,"no");
	           serializer.transform(domSource, streamResult);
	           file_out=domWriter.toString().getBytes();
	           FileOutputStream fileOutputStream = new FileOutputStream(filexml);
			   fileOutputStream.write(file_out);
			
		}catch (Exception err) {
	       	throw new Exception(err);
        }
	return file_out;
	}
	private String RecuperaValoreParametro(Element fatturaElement,String tag){
		 NodeList List = fatturaElement.getElementsByTagName(tag);
		 Element FatturaParElement = (Element)List.item(0);
         if(FatturaParElement!=null){
        	 if ((FatturaParElement.getChildNodes()!=null) &&(FatturaParElement.getChildNodes().item(0)!=null))
        		 return ((Node)FatturaParElement.getChildNodes().item(0)).getNodeValue().trim().toString();
         }
        return null;
	}
	private void AssociaValoreParamentro(Element fatturaElement,String tag,String valore,int s){
		 NodeList List = fatturaElement.getElementsByTagName(tag);
		 Element FatturaParElement = (Element)List.item(s);
		 if(FatturaParElement.getTextContent()==null ||FatturaParElement.getTextContent().length()==0)
			 FatturaParElement.setTextContent(valore);
	}
	
*/	
	@RolesAllowed({"WSUserRole","IITRole"})
	public  FatturaAttiva  InserimentoFattura(FatturaAttiva  fattura) throws  FatturaAttivaException_Exception {
		java.util.ArrayList<FatturaAttiva> fatture = new ArrayList<FatturaAttiva>();
		fatture.add(fattura);
		try{
		fatture=InserimentoFatture(fatture);
		}catch (Exception e) {
			FatturaAttivaException fault=new FatturaAttivaException();
			throw new FatturaAttivaException_Exception(null,fault);
		}
		if(((FatturaAttiva)fatture.get(0)).getCod_errore()==null)
			return fatture.get(0);
		else{
			FatturaAttivaException fault=new FatturaAttivaException();
			fault.setCod_errore(((FatturaAttiva)fatture.get(0)).getCod_errore());
			fault.setDesc_errore(((FatturaAttiva)fatture.get(0)).getDesc_errore());
			throw new FatturaAttivaException_Exception(fault.getCod_errore()+" "+fault.getDesc_errore(),fault);
		}
				
			
	}
	@RolesAllowed({"WSUserRole","IITRole"})
	public  java.util.ArrayList<FatturaAttiva>  InserimentoFatture(java.util.ArrayList<FatturaAttiva>  fatture) throws  Exception {
		
	   UserContext context=new WSUserContext("System",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	   Fattura_attivaBulk testata;
	   Fattura_attiva_rigaBulk riga;          
	   java.util.ArrayList<FatturaAttiva> listOfFatture =fatture;  
	try{
	       for(int s=0; s<listOfFatture.size(); s++){
	    	 FatturaAttiva fat = (FatturaAttiva)listOfFatture.get(s);
	    	 String nome=Controllo_campo_errore(fat);
	         if (nome!=null){
	        	 fat=ValorizzaErrore(fat, Costanti.ERRORE_FA_101.toString(),nome);
	         }else{ 
	        	 if (fat.getTi_causale_emissione().compareTo("C")!=0 &&
	        			 fat.getTi_causale_emissione().compareTo("L")!=0	 &&
	        			 fat.getTi_causale_emissione().compareTo("T")!=0){
	        		 fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),"Causale emissione non valida");
	        	 }
	        	 if (fat.getTi_bene_servizio().compareTo("B")!=0 &&
	        			 fat.getTi_bene_servizio().compareTo("S")!=0	 &&
	        			 fat.getTi_bene_servizio().compareTo("*")!=0){
	        		 fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),"Tipologia bene/servizio non valida");
	        	 }
	        	 if (fat.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)!=0 &&
	        			 fat.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_FATTURA_ATTIVA)!=0)
	        		 fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),"Tipologia fattura non valida");
	        	 try{
	        			if(fat.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0){
	                    	testata=new Nota_di_credito_attivaBulk();
	                    	riga = new Nota_di_credito_attiva_rigaBulk();
	                    }
	                    else{
	                    	testata=new Fattura_attiva_IBulk();
	                    	riga = new Fattura_attiva_rigaIBulk();
	                    }
	                	testata.setToBeCreated();
	        	    	testata.setEsercizio(new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
	        	    	testata.setTi_fattura(fat.getTi_fattura());
	                    testata.setEsercizio(fat.getEsercizio());
	                    testata.setUtcr(fat.getUtcr());
	                    testata.setCd_cds_origine(fat.getCd_cds_origine());
	                    testata.setTi_bene_servizio(fat.getTi_bene_servizio());
	                    testata.setFl_pagamento_anticipato(fat.getFl_pagamento_anticipato());
	                    UserContext userContext = new WSUserContext(testata.getUtcr(),null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),testata.getCd_uo_origine(),testata.getCd_cds_origine(),null);
	        	    	CdsBulk cds=new CdsBulk(fat.getCd_cds_origine());
	        	    	cds=(CdsBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,cds));
	        	    	if(cds==null)
	        	    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_130.toString());
	                    
	                    Unita_organizzativaBulk uo=new Unita_organizzativaBulk(fat.getCd_uo_origine());
	                    uo=(Unita_organizzativaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,uo));
	                    if(uo==null)
	                    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_131.toString());
	                    
	                   	if(uo.getCd_cds().compareTo(testata.getCd_cds_origine())!=0)
	                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_135.toString());
	                    
	        	    	testata.setCd_uo_origine(fat.getCd_uo_origine());
	        	        testata.setPg_fattura_esterno(fat.getPg_fattura_esterno());
	                
	        	    	Fattura_attivaBulk fatturaAttiva;
	        	    	if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)!=0)){
	        	    		fatturaAttiva = new Fattura_attiva_IBulk();
	            			fatturaAttiva.setEsercizio(testata.getEsercizio());
	            			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
	            			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
	            			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
	        	    	}else
	        	    	{
	        	    		fatturaAttiva = new Nota_di_credito_attivaBulk();
	            			fatturaAttiva.setEsercizio(testata.getEsercizio());
	            			fatturaAttiva.setCd_cds_origine(testata.getCd_cds_origine());
	            			fatturaAttiva.setCd_uo_origine(testata.getCd_uo_origine());
	            			fatturaAttiva.setPg_fattura_esterno(testata.getPg_fattura_esterno());
	            			fatturaAttiva.setTi_fattura(testata.getTi_fattura());
	        	    	}
	        	        testata.setFl_intra_ue(fat.getFl_intra_ue());
	        	        testata.setFl_extra_ue(fat.getFl_extra_ue());
	        	        testata.setFl_san_marino(fat.getFl_san_marino());
	        	        
	        	    	if (!((fat.getFl_intra_ue() && !fat.getFl_extra_ue() && !fat.getFl_san_marino())||
	        	          (!fat.getFl_intra_ue() && fat.getFl_extra_ue() && !fat.getFl_san_marino())||
	        	          (!fat.getFl_intra_ue() && !fat.getFl_extra_ue() && fat.getFl_san_marino())||
	        	          (!fat.getFl_intra_ue() && !fat.getFl_extra_ue() && !fat.getFl_san_marino())))
	        	    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_129.toString());
	        	    	if((fat.getFl_extra_ue().booleanValue() || fat.getFl_intra_ue().booleanValue()) && fat.getTi_bene_servizio().compareTo("*")==0){
	        	    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_142.toString());
	        	    	}
	        	    		
	        	    	userContext.setTransactional(true);
	        		    // richiesta dall'inizializza (Fattura_attiva_IBulk)
	        	    	if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))
	        	    		testata = (Nota_di_credito_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).inizializzaBulkPerInserimento(userContext,testata);
	        	    	else
	        	    		testata = (Fattura_attiva_IBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).inizializzaBulkPerInserimento(userContext,testata);
	        	    	((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).setSavePoint(context,"Fattura_automatica");
	                    
	            		// potrebbe non essere univoca per tipologia	            		
	        	    	if ((((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).VerificaDuplicati(userContext,fatturaAttiva))){
	        	    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_102.toString());
	        	    	}
	                    
		            	testata.setTipo_sezionale(new Tipo_sezionaleBulk(fat.getCd_tipo_sezionale()));
		            	boolean esiste = false;
		            	for (Iterator i=testata.getSezionali().iterator();!esiste && i.hasNext();){
		            		Tipo_sezionaleBulk tipo = (Tipo_sezionaleBulk)i.next();
		            		if(tipo.getCd_tipo_sezionale().compareTo(testata.getTipo_sezionale().getCd_tipo_sezionale())==0)
		            			esiste=true;
		            	}
		            	if (!esiste)
		            		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_127.toString());
	                    
		                testata.setDt_registrazione(DateUtils.truncate(new Timestamp(fat.getDt_registrazione().getTime())));
		                testata.setTi_causale_emissione(fat.getTi_causale_emissione());
		                testata.setFl_liquidazione_differita(fat.getFl_liquidazione_differita());
		                testata.setDs_fattura_attiva(fat.getDs_fattura_attiva());
		                testata.setRiferimento_ordine(fat.getRif_ordine());
		                testata.setCliente(new TerzoBulk(fat.getCd_terzo()));
			            testata.setCliente((TerzoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,testata.getCliente())));
			            if (testata.getCliente()==null)
			            	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_105.toString());
			            
			            else{
		            	 if(testata.getCliente().getAnagrafico().getTi_italiano_estero().compareTo(testata.getSupplierNationType())!=0){
			                	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_140.toString());
		                 }
			                 	
		                 testata.setCd_terzo(fat.getCd_terzo());
		                 testata=(Fattura_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaTerzo(userContext,testata,testata.getCliente());
		                 
		                 if(testata.getCliente().isAnagraficoScaduto()||testata.getCliente().getTi_terzo().compareTo(TerzoBulk.CREDITORE)==0||testata.getCliente().getAnagrafico().getTi_entita().compareTo(AnagraficoBulk.DIVERSI)==0)
		                	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_125.toString());
	                   	 if((testata.getFl_liquidazione_differita().booleanValue()&& testata.getFl_liquidazione_differita().compareTo(testata.getCliente().getAnagrafico().getFl_fatturazione_differita())!=0))
	           	    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_124.toString());  
			            }
			            if(testata.getCd_terzo_uo_cds().compareTo(fat.getCd_terzo_uo_cds())!=0)
		                	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_133.toString());
	                       	
			            testata.setCd_modalita_pag_uo_cds(fat.getCd_modalita_pag_uo_cds());
			            testata.setModalita_pagamento_uo(((Rif_modalita_pagamentoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Rif_modalita_pagamentoBulk(fat.getCd_modalita_pag_uo_cds())))));
			            if(testata.getModalita_pagamento_uo()==null)
	                	  	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_132.toString());
	                
			            testata.setPg_banca_uo_cds(fat.getPg_banca_uo_cds());
			            testata.setBanca_uo(new BancaBulk(testata.getCd_terzo_uo_cds(),testata.getPg_banca_uo_cds()));
		                if (testata.getBanca_uo()==null)
		                   	  fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_134.toString());
	                   
		                java.util.List l=(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findListaBancheWS(userContext,testata.getCd_terzo_uo_cds().toString(),testata.getModalita_pagamento_uo().getCd_modalita_pag(),"","",""));
		                esiste=false;
		                for(Iterator i=l.iterator();!esiste && i.hasNext();){
		                	BancaBulk banca=(BancaBulk)i.next();
		                	if (banca.getPg_banca().compareTo(testata.getPg_banca_uo_cds())==0)
		                		esiste=true;
		                }
		                if(!esiste)
		                	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_134.toString());
		                if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
	                    	if(fat.getCd_modalita_pag()!=null){
	                    	    ((Nota_di_credito_attivaBulk)testata).setModalita_pagamento((Rif_modalita_pagamentoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Rif_modalita_pagamentoBulk(fat.getCd_modalita_pag()))));
	                    	    if(((Nota_di_credito_attivaBulk)testata).getModalita_pagamento()==null)
	                    	    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_111.toString());
	                    	    
	                    	    testata.setCd_modalita_pag(fat.getCd_modalita_pag());
	                    	}
	                    	else
	            	        	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_111.toString());
	            
	                    	if(fat.getPg_banca()!=null){
	                    		 ((Nota_di_credito_attivaBulk)testata).setBanca((new BancaBulk(testata.getCd_terzo(),fat.getPg_banca())));
	                     		  testata.setPg_banca(fat.getPg_banca());
	                     		 ((Nota_di_credito_attivaBulk)testata).setBanca((BancaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,((Nota_di_credito_attivaBulk)testata).getBanca())));
	                     		 if (((Nota_di_credito_attivaBulk)testata).getBanca()==null){
	    	        	        	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_118.toString());
	                     		 }
	                     		 l=(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findListaBancheWS(userContext,testata.getCd_terzo().toString(),((Nota_di_credito_attivaBulk)testata).getModalita_pagamento().getCd_modalita_pag(),"","",""));
	                     		 esiste=false;
	                     		 for(Iterator i=l.iterator();!esiste&&i.hasNext();){
	                     			 BancaBulk banca=(BancaBulk)i.next();
	                     			 if (banca.getPg_banca().compareTo(testata.getPg_banca())==0)
	                     				 esiste=true;
	                     		 }
	                     		 if(!esiste) 
	    	        	        	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_118.toString());
	                    	}
	                    	else
	                         	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_110.toString());
		    	      }
			          testata.setCd_divisa(fat.getCd_divisa());
			          testata.setCambio(fat.getCambio());
			     	  testata.setNote(fat.getNote());
			          testata.validate();
			          java.util.ArrayList<FatturaAttivaRiga> listOfRighe =fat.getRighefat(); 
			          for(int r=0; r<listOfRighe.size() ; r++){
			        	  FatturaAttivaRiga fatr = (FatturaAttivaRiga)listOfRighe.get(r);
			        	  if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))
			        		  riga=new Nota_di_credito_attiva_rigaBulk();
			        	  else
			        		  riga=new Fattura_attiva_rigaIBulk();
			                	 
			              testata.addToFattura_attiva_dettColl(riga);
			              riga.setToBeCreated();
			              //?? da aggiungere controlli
			              
			              nome=Controllo_campo_errore(fatr);
			            if (nome!=null){
					   	        fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),nome);
	    	          	}
			            else{
			             	if(fatr.getCd_bene_servizio()!=null){
		    	                   riga.setBene_servizio(new Bene_servizioBulk(fatr.getCd_bene_servizio()));
		    	                   riga.setCd_bene_servizio(fatr.getCd_bene_servizio());
			             	}
			             	riga.setBene_servizio((Bene_servizioBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getBene_servizio())));
			             	if (riga.getBene_servizio()==null){
	 	   	            	    fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_139.toString());
			             	}else if(fat.getTi_bene_servizio().compareTo("*")!=0 && riga.getBene_servizio().getTi_bene_servizio().compareTo(fat.getTi_bene_servizio())!=0){
			             	   fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_141.toString());
			             	}
			             	/*else{
			             		riga.setVoce_iva(riga.getBene_servizio().getVoce_iva());
					    	    riga.setVoce_iva((Voce_ivaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getVoce_iva())));
					    	    if (riga.getVoce_iva()==null){
					    	    	    fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_106.toString());
			    	          	}else
					    	    {
					    	         riga.setDs_riga_fattura(riga.getBene_servizio().getDs_bene_servizio());
					    	    }
			             	}*/
			            	if(testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO)){
			            		if(fatr.getCd_tariffario()!=null){
			            			riga.setCd_tariffario(fatr.getCd_tariffario());
				    	    //r.p.???
			            			riga.setTariffario(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).findTariffario(userContext,riga));
			            			if(riga.getTariffario()==null){
			            				fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_128.toString());
			            			}                	
			    	          	    riga.setVoce_iva(riga.getTariffario().getVoce_iva());
						    	    riga.setVoce_iva((Voce_ivaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getVoce_iva())));
						    	    if (riga.getVoce_iva()==null){
						    	    	    fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_106.toString());
				    	          	}else
						    	    {
						    	         riga.setPrezzo_unitario(riga.getTariffario().getIm_tariffario());
						    	         riga.setQuantita(new BigDecimal(riga.getTariffario().getUnita_misura()));
						    	         riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
						    	         if(fatr.getFl_iva_forzata().booleanValue())
						    	        	 riga.setIm_iva(fatr.getIm_iva());
						    	         else
						    	        	 riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).divide(new BigDecimal(100)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
						    	         riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
						    	         riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
						    	         riga.setDs_riga_fattura(riga.getTariffario().getDs_tariffario());
						    	   }
			            		}else
			            			fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_103.toString());
			            	}
			            	else{
		    	        	//if(testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO))
		    	             	if (fatr.getPrezzo_unitario()!=null)
		    	             		riga.setPrezzo_unitario(fatr.getPrezzo_unitario());
		    	             	if (fatr.getQuantita()!=null)	
			    	                riga.setQuantita(fatr.getQuantita());
		    	             	if(fatr.getCd_voce_iva()!=null){
			    	                   riga.setVoce_iva(new Voce_ivaBulk(fatr.getCd_voce_iva()));
			    	                   riga.setCd_voce_iva(fatr.getCd_voce_iva());
		    	             	}
		    	                riga.setVoce_iva((Voce_ivaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,riga.getVoce_iva())));
		    	                if (riga.getVoce_iva()==null||(riga.getVoce_iva().getDt_cancellazione()!=null && riga.getPrezzo_unitario().compareTo(BigDecimal.ZERO)!=0)){
		    	   	            	    fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_106.toString());
		    	                }
		    	                else{
		    	                	    riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
		    	                	    if(fatr.getFl_iva_forzata().booleanValue())
						    	        	 riga.setIm_iva(fatr.getIm_iva());
						    	         else
						    	        	 riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).divide(new BigDecimal(100)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			    	                    
			    	                    riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
			    	                    riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
			    	                    if(fatr.getDs_riga_fattura()!=null)
			  	    	                    	riga.setDs_riga_fattura(fatr.getDs_riga_fattura());            
		    	                }
		    	             }
		    	        	   //fine else if(testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO))
		    	             if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
		    	                	if(		fatr.getPg_fattura_assncna_fin()!=null && 
		    	                			fatr.getPg_riga_assncna_fin()!=null && 
		    	                			fatr.getEsercizio_assncna_fin()!=null){
		    	                		((Nota_di_credito_attiva_rigaBulk)riga).setRiga_fattura_associata((Fattura_attiva_rigaIBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Fattura_attiva_rigaIBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),fatr.getEsercizio_assncna_fin(),fatr.getPg_fattura_assncna_fin(),fatr.getPg_riga_assncna_fin()))));
		    	                		Fattura_attiva_rigaIBulk rigaFP = ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata();
			    	               		if (rigaFP!=null){
			    	               			java.math.BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(riga.getIm_totale_divisa());
			    	               			if(nuovoImportoDisponibile.compareTo(new BigDecimal("0"))<0)
			    	               				fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_117.toString());               
			    	               			else{
			    	               				rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			    	               				((Nota_di_credito_attiva_rigaBulk)riga).setRiga_fattura_associata(rigaFP);
			    	               			}
			    	               		}
		    	                	}
			    	            // non dovrebbe servire questo controllo!
			    	            else
			    	            	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_109.toString()); 
				   	    
			    	            if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata()==null)
			    	            	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_109.toString()); 
			    	            else{
			    	            //controllo cliente coerente fattura - NC
			    	                ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().setFattura_attivaI((Fattura_attiva_IBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,
			    	                    	((Fattura_attiva_IBulk)new Fattura_attiva_IBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getEsercizio(),((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getPg_fattura_attiva())))));
			    	                 ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().setCliente((TerzoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getCliente())));
			    	                 if (testata.getCliente().getCd_terzo().compareTo(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getCd_terzo())!=0)
			    	                    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_116.toString());
			    	                    	//Controllo codice iva coerente fattura - NC
			    	                 if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
			    	                   ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_voce_iva()!=null &&
			    	                   ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_voce_iva().compareTo(riga.getCd_voce_iva())!=0)
			    	                	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_120.toString());    
			    	                    	//controllo causale emissione coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    	  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTi_causale_emissione()!=null &&
				    	                  ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTi_causale_emissione().compareTo(riga.getFattura_attiva().getTi_causale_emissione())!=0)
				    	               	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_121.toString());
		    	                    	//controllo sezionale coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    			((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTipo_sezionale()!=null &&
		    	                    			((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getTipo_sezionale().getCd_tipo_sezionale().compareTo(riga.getFattura_attiva().getTipo_sezionale().getCd_tipo_sezionale())!=0)
				    	                   	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_122.toString());
		    	                    	//controllo tariffario coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    			((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_tariffario()!=null &&
		    	                    			((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getCd_tariffario().compareTo(riga.getCd_tariffario())!=0)
				    	               	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_123.toString());		    	                    	//controllo flag coerente fattura - Nc
		    	                    	if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata() !=null &&
		    	                    	   (((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_extra_ue().compareTo(riga.getFattura_attiva().getFl_extra_ue())!=0 ||
		    	                    	   ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_intra_ue().compareTo(riga.getFattura_attiva().getFl_intra_ue())!=0||
		    	                    	   ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_san_marino().compareTo(riga.getFattura_attiva().getFl_san_marino())!=0||
	                    		           ((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata().getFattura_attivaI().getFl_liquidazione_differita().compareTo(riga.getFattura_attiva().getFl_liquidazione_differita())!=0))
				    	               	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_126.toString());
			    	            	}
			    	            	//fine else if(((Nota_di_credito_attiva_rigaBulk)riga).getRiga_fattura_associata()==null)
		    	                    if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){
		    	                    		testata.setCd_cds(testata.getCd_cds_origine());
		    	                    		testata.setCd_unita_organizzativa(testata.getCd_uo_origine());
		    	                    }
		    	                }
		    	                //fine  if ((testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0))
			                    
			                    riga.setFl_iva_forzata(fatr.getFl_iva_forzata());
			                    riga.setUtcr(testata.getUtcr());
			                    fatr.setProgressivo_riga(riga.getProgressivo_riga());
			                    if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){      
		    	                    AccertamentoBulk acc=new AccertamentoBulk(); 
		    	                    acc.setToBeCreated();
		    	                    acc.setEsercizio(riga.getEsercizio());
		    	                    acc.setEsercizio_originale(riga.getEsercizio());
		    	                    acc.setCd_cds(riga.getCd_cds());
		    	                    acc.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
		    	                    acc.setCd_cds_origine(testata.getCd_cds_origine());
		    	                    acc.setCd_uo_origine(testata.getCd_uo_origine());
		    	                    acc.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR);
		    	                    acc.setFl_pgiro(new Boolean(false));
		    	                    acc.setRiportato("N");
		    	                    acc.setFromDocAmm(new Boolean(true));
		    	                    acc.setFl_calcolo_automatico(new Boolean(false));
		    	                    acc.setUtcr(testata.getUtcr());
		    	                    acc.setUser(testata.getUtcr());
		    	                    it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce=new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();
		    	                    voce.setEsercizio(acc.getEsercizio());
		    	                    voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
		    	                    voce.setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
		    	                    voce.setCd_voce(fatr.getCd_voce());
		    	                    voce=((it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,voce)));
		    	                    if (voce==null)
		    	                               	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_107.toString());
		    	                    else{
		    	                    	acc.setCapitolo(new V_voce_f_partita_giroBulk(voce.getCd_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione()));
		    	                    	acc.setTi_appartenenza(voce.getTi_appartenenza());
		    	                    	acc.setTi_gestione(voce.getTi_gestione());
		    	                    	acc.setCd_elemento_voce(voce.getCd_elemento_voce());
		    	                    	acc.setCd_voce(voce.getCd_voce());
	    	                    	}
		    	                    //acc.setDt_registrazione(testata.getDt_registrazione());
		    	                    ///?????
		    	                    acc.setDt_registrazione((DateUtils.truncate(new Timestamp(fatr.getDt_scadenza().getTime()))));
		    	                    acc.setDs_accertamento(fatr.getDs_accertamento());
		    	                    //??? importo riga?????	
	    	                    		acc.setIm_accertamento(riga.getIm_totale_divisa());	
		 	    	                acc.setCd_terzo(testata.getCd_terzo());    	
		 	    	                acc.setEsercizio_competenza(testata.getEsercizio());
		 	    	                
		 	    	                Accertamento_scadenzarioBulk acc_scadenza=new Accertamento_scadenzarioBulk();
		 	    	                acc_scadenza.setUtcr(acc.getUtcr());
		 	    	                acc_scadenza.setToBeCreated();
		 	    	               
		 	    	                acc_scadenza.setAccertamento(acc);
		 	    	                acc_scadenza.setDt_scadenza_incasso(acc.getDt_registrazione());
		 	    	                acc_scadenza.setDs_scadenza( acc.getDs_accertamento() );
		 	    	                acc.addToAccertamento_scadenzarioColl(acc_scadenza);
		 	    	                acc_scadenza.setIm_scadenza(acc.getIm_accertamento());
		 	    	                acc_scadenza.setIm_associato_doc_amm(riga.getIm_totale_divisa());
		 	    	                acc_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
		 	    	         
		    	                    if(fatr.getEsercizio_contratto()!=null && fatr.getStato_contratto()!=null && fatr.getPg_contratto()!=null){
		    	                    		acc.setContratto(new ContrattoBulk(fatr.getEsercizio_contratto(),fatr.getStato_contratto(),fatr.getPg_contratto()));
		    	                    		acc.setContratto((ContrattoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,acc.getContratto())));
		    	                    		if (acc.getContratto()==null){
		    	                    				fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_104.toString());
		    	                    		}else{
		    	                    			acc.setCheckDisponibilitaContrattoEseguito(true);
		    	                    		}
		    	                    }
		    	                    else{
		    	                    	    if((fatr.getEsercizio_contratto()!=null || fatr.getStato_contratto()!=null || fatr.getPg_contratto()!=null) && (fatr.getEsercizio_contratto()==null && fatr.getStato_contratto()==null && fatr.getPg_contratto()==null))	
		    	                    	    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_108.toString());
		    	                    }

		    	                    java.util.ArrayList<FatturaAttivaScad> listOfScad =fatr.getRighescadvoc(); 
	    	                        for(int v=0; v<listOfScad.size() ; v++){
	    	                        	FatturaAttivaScad fatrs=(FatturaAttivaScad) listOfScad.get(v);
	    	                        	nome=Controllo_campo_errore(fatrs);
	                                    if (nome!=null){
	        	                   	 		  fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),nome);
	    	                            }
	                                    else{
	                                    	Accertamento_scad_voceBulk acc_scad_voce=new Accertamento_scad_voceBulk();
	                                    	acc_scad_voce.setUtcr(testata.getUtcr());
	                                    	acc_scad_voce.setToBeCreated();
	                                    	acc_scad_voce.setAccertamento_scadenzario(acc_scadenza);
	                                    	acc_scad_voce.setIm_voce(fatrs.getIm_voce());	
		        	                    
			        	                   CdrBulk cdr_db= new CdrBulk();
			        	            	   cdr_db=(((CdrBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdrBulk(fatrs.getCdr())))));
			        	            	   if (cdr_db==null){
			        	            		   fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_136.toString());
		    	                           } 	
		    	                           acc_scad_voce.setLinea_attivita(((WorkpackageBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new WorkpackageBulk(fatrs.getCdr(),fatrs.getGae())))));
		    	                           if(acc_scad_voce.getLinea_attivita()!=null){
		    	                        	   if(acc_scad_voce.getLinea_attivita().getTi_gestione().compareTo(acc.getTi_gestione())!=0)
		    	                        		   fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_138.toString());
		    	                           		
		    	                           	acc_scadenza.getAccertamento_scad_voceColl().add((acc_scad_voce));
		    	                           }else	  	
		    	                        	   fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_137.toString());   
	                                    }
	    		                     }
	    	                        // fine for listOfScad 
	    	                    	acc=(AccertamentoBulk) ((AccertamentoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession",AccertamentoComponentSession.class)).creaConBulk(userContext,acc);
	                    	        fatr.setPg_accertamento_scadenzario(((Accertamento_scadenzarioBulk)(acc.getAccertamento_scadenzarioColl().get(0))).getPg_accertamento_scadenzario());
	                    	        for (Iterator i=acc.getAccertamento_scadenzarioColl().iterator();i.hasNext();){
				                    	BulkList righesel=new BulkList();
				                    	righesel.add(riga);
				                    	Accertamento_scadenzarioBulk scad=(Accertamento_scadenzarioBulk)i.next();
				                        testata = (Fattura_attiva_IBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).contabilizzaDettagliSelezionati(userContext,testata,righesel,scad);
				                    	//testata.addToFattura_attiva_accertamentiHash(scad, riga);
				                    	testata.addToDefferredSaldi(scad.getAccertamento(), scad.getAccertamento().getSaldiInfo());
				                    }
	                    	        //intrastat
	                    	        
	                    	        java.util.ArrayList<FatturaAttivaIntra> listOfIntra =fat.getRigheIntra();
	                    	        boolean obbligatorio=false;
	                    	        for (Iterator i = testata.getFattura_attiva_dettColl().iterator(); i.hasNext();) {
                    	    			Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk)i.next();
                    	    			if(dettaglio.getBene_servizio()!=null && dettaglio.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
                    	    				obbligatorio=true;
                    	    		}
	                    	        if(fat.getFl_intra_ue()!=null && fat.getFl_intra_ue().booleanValue()){ 
		                    	        if(listOfIntra!=null && !listOfIntra.isEmpty() && 
		                    	        		testata.getCliente()!=null && testata.getCliente().getAnagrafico()!=null && 
		                    	        		testata.getCliente().getAnagrafico().getPartita_iva()!=null && obbligatorio){
			                    	        for(int v=0; v<listOfIntra.size() ; v++){
			    	                        	FatturaAttivaIntra intra=(FatturaAttivaIntra) listOfIntra.get(v);
			    	                        	nome=Controllo_campo_errore(intra);
			                                    if (nome!=null){
			        	                   	 		  fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),nome);
			    	                            }
			                                    else{
			                                    	Fattura_attiva_intraBulk fa_intra = new Fattura_attiva_intraBulk();
			                                    	fa_intra.setUtcr(testata.getUtcr());
			                                    	fa_intra.setToBeCreated();
			                                    	fa_intra.setFattura_attiva(testata);
			                                    	if(testata.getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO)==0){
				                                    	
			                                    		fa_intra.setModalita_erogazione(((Modalita_erogazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_erogazioneBulk(testata.getEsercizio(),intra.getCod_erogazione())))));
				                                    	if(fa_intra.getModalita_erogazione()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_143.toString());
				                                    	
				                                    	fa_intra.setModalita_incasso(((Modalita_incassoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_incassoBulk(testata.getEsercizio(),intra.getCod_incasso())))));
				                                    	if(fa_intra.getModalita_incasso()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_144.toString());
				                                    	
				                                    	fa_intra.setCodici_cpa(((Codici_cpaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Codici_cpaBulk(intra.getId_cpa())))));
				                                    	if(fa_intra.getCodici_cpa()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_145.toString());
				                                    
			                                    	}else{
			                                    		// per il momento non richiesto da Pisa da testare totalmente anche i WS di servizio
			                                    		fa_intra.setNomenclatura_combinata(((Nomenclatura_combinataBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Nomenclatura_combinataBulk(intra.getId_nomenclatura_combinata())))));
				                                    	if(fa_intra.getNomenclatura_combinata()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_147.toString());
				                                    	
				                                    	fa_intra.setNatura_transazione(((Natura_transazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Natura_transazioneBulk(intra.getId_natura_transazione())))));
				                                    	if(fa_intra.getNatura_transazione()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_148.toString());
				                                    	
				                                    	fa_intra.setCondizione_consegna(((Condizione_consegnaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Condizione_consegnaBulk(intra.getCd_consegna(),testata.getEsercizio())))));
				                                    	if(fa_intra.getCondizione_consegna()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_149.toString());
				                                    	
				                                    	fa_intra.setModalita_trasporto(((Modalita_trasportoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_trasportoBulk(intra.getCd_trasporto(),testata.getEsercizio())))));
				                                    	if(fa_intra.getModalita_trasporto()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_150.toString());
				                                    	
				                                    	fa_intra.setProvincia_origine(((ProvinciaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new ProvinciaBulk(intra.getCd_provincia())))));
				                                    	if(fa_intra.getProvincia_origine()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_151.toString());
				                                    	if(intra.getMassa()!=null)
				                                    		fa_intra.setMassa_netta(intra.getMassa());
				                                    	if(intra.getValore_statistico()!=null)
				                                    		fa_intra.setValore_statistico(intra.getValore_statistico());
				                                    	if(intra.getUnita_supplementari()!=null)
				                                    		fa_intra.setUnita_supplementari(intra.getUnita_supplementari());
			                                    	}
			                                    	fa_intra.setNazione_destinazione(((NazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new NazioneBulk(intra.getPg_nazione())))));
			                                    	if(fa_intra.getNazione_destinazione()==null)
			                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_146.toString());
			                                    	
			                                    	fa_intra.setAmmontare_euro(intra.getAmmontare_euro());
			                                    	fa_intra.setFl_inviato(false);
			                                    	testata.getFattura_attiva_intrastatColl().add(fa_intra);
				    	                        }
			                    	        }// fine for listOfIntra
		                    	        }// fine if intra
	    	                        }//fine intrastat
		    	              }
		    	              //fine non Nota Credito
		    	              else{ 
		    	            	  //Nota Credito
		    	                    ObbligazioneBulk obb=new ObbligazioneBulk(); 
		    	                    obb.setToBeCreated();
		    	                    obb.setEsercizio(riga.getEsercizio());
		    	                    obb.setEsercizio_originale(riga.getEsercizio());
		    	                    obb.setCds((it.cnr.contab.config00.sto.bulk.CdsBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdsBulk(riga.getCd_cds()))));
		    	                    obb.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
		    	                    obb.setCd_cds_origine(testata.getCd_cds_origine());
		    	                    obb.setCd_uo_origine(testata.getCd_uo_origine());
		    	                    obb.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB);
		    	                    obb.setFl_pgiro(new Boolean(false));
		    	                    obb.setRiportato("N");
		    	                    obb.setFromDocAmm(new Boolean(true));
		    	                    obb.setFl_calcolo_automatico(new Boolean(false));
		    	                    obb.setFl_spese_costi_altrui(new Boolean(false));
		    	                    obb.setFl_gara_in_corso(new Boolean(false));
		    	                    obb.setUtcr(testata.getUtcr());
		    	                    obb.setUser(testata.getUtcr());
		    	                    it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce=new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();
		    	                    voce.setEsercizio(obb.getEsercizio());
		                    	    voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
		                    	    voce.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
		                    	    voce.setCd_voce(fatr.getCd_voce());
		                    	    voce.setCd_unita_organizzativa(testata.getCd_uo_origine());
		                    	    voce=((it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,voce)));
		                    	    if (voce==null)
		                    	    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_107.toString());   
		                    	    else{
		                    	    	obb.setElemento_voce(((Elemento_voceBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Elemento_voceBulk(voce.getCd_elemento_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())))));
		                    	    	obb=(ObbligazioneBulk) ((ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class)).listaCapitoliPerCdsVoce(userContext,obb);
		                    	    	obb.setCapitoliDiSpesaCdsSelezionatiColl(obb.getCapitoliDiSpesaCdsColl());
		                    	    }
		                    	    //obb.setDt_registrazione(testata.getDt_registrazione());
		    	                    ///?????
		                    	    obb.setDt_registrazione((DateUtils.truncate(new Timestamp(fatr.getDt_scadenza().getTime()))));  
		                    	    if(fatr.getDs_obbligazione()!=null)
		                    	    	obb.setDs_obbligazione(fatr.getDs_obbligazione());
		                    	    if(fatr.getMotivazione()!=null)
		                    	    	obb.setMotivazione(fatr.getMotivazione());
		                    	    //??? importo riga?????	
		                    	    obb.setIm_obbligazione(riga.getIm_totale_divisa());	
		                    	    obb.setIm_costi_anticipati( new java.math.BigDecimal(0));
		                    	    //??
		                    	    obb.setStato_obbligazione(ObbligazioneBulk.STATO_OBB_DEFINITIVO);
		                    	    obb.setUser(obb.getUtcr());
		                    	    obb.setCd_terzo(testata.getCd_terzo());    	
		                    	    obb.setEsercizio_competenza(testata.getEsercizio());
	 	    	                
			 	    	            Obbligazione_scadenzarioBulk obb_scadenza=new Obbligazione_scadenzarioBulk();
			 	    	            obb_scadenza.setUtcr(obb.getUtcr());
			 	    	            obb_scadenza.setToBeCreated();
			 	    	               
			 	    	            obb_scadenza.setObbligazione(obb);
			 	    	            obb_scadenza.setDt_scadenza(obb.getDt_registrazione());
			 	    	            obb_scadenza.setDs_scadenza( obb.getDs_obbligazione() );
			 	    	            obb.addToObbligazione_scadenzarioColl(obb_scadenza);
			 	    	            obb_scadenza.setIm_scadenza(obb.getIm_obbligazione());
			 	    	            obb_scadenza.setIm_associato_doc_amm(riga.getIm_totale_divisa());
			 	    	            obb_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
			 	    	            if(fatr.getEsercizio_contratto()!=null && fatr.getStato_contratto()!=null && fatr.getPg_contratto()!=null){
		    	                            obb.setContratto(new ContrattoBulk(fatr.getEsercizio_contratto(),fatr.getStato_contratto(),fatr.getPg_contratto()));
			    	                    obb.setContratto((ContrattoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,obb.getContratto())));
			    	                    if (obb.getContratto()==null)
			    	                    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_104.toString());   
			    	                    	/// ????  
			    	                    else
			    	                    	obb.setCheckDisponibilitaContrattoEseguito(true);
			    	                    
		    	                    }else{
		    	                    	if((fatr.getEsercizio_contratto()!=null || fatr.getStato_contratto()!=null || fatr.getPg_contratto()!=null) && (fatr.getEsercizio_contratto()==null && fatr.getStato_contratto()==null && fatr.getPg_contratto()==null))	
		    	                    	    	fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_108.toString());   
		    	                    }
			 	    	           java.util.ArrayList <FatturaAttivaScad> listOfScad =fatr.getRighescadvoc(); 
		    		                for(int v=0; v<listOfScad.size() ; v++){
		    		                   FatturaAttivaScad fatrs=(FatturaAttivaScad) listOfScad.get(v);
		    		                   nome = Controllo_campo_errore(fatrs);
		                                    if (nome!=null){
		        	                   	 		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),nome);
		    	                            }
		                                    else{
					        	                Obbligazione_scad_voceBulk obb_scad_voce=new Obbligazione_scad_voceBulk();
				 	    	                	obb_scad_voce.setUtcr(testata.getUtcr());
				 	    	               		obb_scad_voce.setToBeCreated();
				 	    	              		obb_scad_voce.setObbligazione_scadenzario(obb_scadenza);
					        	                obb_scad_voce.setIm_voce(fatrs.getIm_voce());
					        	                obb_scad_voce.setCd_voce(voce.getCd_voce());
					        	                obb_scad_voce.setTi_gestione(voce.getTi_gestione());
					        	                obb_scad_voce.setTi_appartenenza(voce.getTi_appartenenza());
				        	                    	CdrBulk cdr_db= new CdrBulk();
				        	            		cdr_db=(((CdrBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new CdrBulk(fatrs.getCdr())))));
				        	            		if (cdr_db==null)
				        	            		    fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_136.toString());
				        	                    	obb_scad_voce.setLinea_attivita(((WorkpackageBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new WorkpackageBulk(fatrs.getCdr(),fatrs.getGae())))));
				        	                    	if(obb_scad_voce.getLinea_attivita()!=null){
				        	                    		if(obb_scad_voce.getLinea_attivita().getTi_gestione().compareTo(obb.getTi_gestione())!=0)
				        	                    			fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_138.toString());
					        	                    	Linea_attivitaBulk nuovaLatt = new it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk();
					        	            		nuovaLatt.setLinea_att( obb_scad_voce.getLinea_attivita());
					        	            		if(obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza().compareTo(new BigDecimal(0))!=0)
					        	            			nuovaLatt.setPrcImputazioneFin( obb_scad_voce.getIm_voce().multiply(new BigDecimal(100)).divide( obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP ));
					        	            			nuovaLatt.setObbligazione(obb);
					        	            			obb.getNuoveLineeAttivitaColl().add(nuovaLatt);
					        	            			obb_scadenza.getObbligazione_scad_voceColl().add((obb_scad_voce));
				        	                    	}else
				        	                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_137.toString());
		                                    }
		    		                }
	                    	        //intrastat
	                    	        
	                    	        java.util.ArrayList<FatturaAttivaIntra> listOfIntra =fat.getRigheIntra();
	                    	        boolean obbligatorio=false;
	                    	        for (Iterator i = testata.getFattura_attiva_dettColl().iterator(); i.hasNext();) {
                    	    			Fattura_attiva_rigaBulk dettaglio = (Fattura_attiva_rigaBulk)i.next();
                    	    			if(dettaglio.getBene_servizio()!=null && dettaglio.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
                    	    				obbligatorio=true;
                    	    		}
	                    	        if(fat.getFl_intra_ue()!=null && fat.getFl_intra_ue().booleanValue()){
		                    	        if(listOfIntra!=null && !listOfIntra.isEmpty() && 
		                    	        		testata.getCliente()!=null && testata.getCliente().getAnagrafico()!=null && 
		                    	        		testata.getCliente().getAnagrafico().getPartita_iva()!=null && obbligatorio){
			                    	        for(int v=0; v<listOfIntra.size() ; v++){
			    	                        	FatturaAttivaIntra intra=(FatturaAttivaIntra) listOfIntra.get(v);
			    	                        	nome=Controllo_campo_errore(intra);
			                                    if (nome!=null){
			        	                   	 		  fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_101.toString(),nome);
			    	                            }
			                                    else{
			                                    	Fattura_attiva_intraBulk fa_intra = new Fattura_attiva_intraBulk();
			                                    	fa_intra.setUtcr(testata.getUtcr());
			                                    	fa_intra.setToBeCreated();
			                                    	fa_intra.setFattura_attiva(testata);
			                                    	if(testata.getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO)==0){
				                                    	
			                                    		fa_intra.setModalita_erogazione(((Modalita_erogazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_erogazioneBulk(testata.getEsercizio(),intra.getCod_erogazione())))));
				                                    	if(fa_intra.getModalita_erogazione()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_143.toString());
				                                    	
				                                    	fa_intra.setModalita_incasso(((Modalita_incassoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_incassoBulk(testata.getEsercizio(),intra.getCod_incasso())))));
				                                    	if(fa_intra.getModalita_incasso()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_144.toString());
				                                    	
				                                    	fa_intra.setCodici_cpa(((Codici_cpaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Codici_cpaBulk(intra.getId_cpa())))));
				                                    	if(fa_intra.getCodici_cpa()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_145.toString());
				                                    
			                                    	}else{
			                                    		// per il momento non richiesto da Pisa da testare totalmente anche i WS di servizio
			                                    		fa_intra.setNomenclatura_combinata(((Nomenclatura_combinataBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Nomenclatura_combinataBulk(intra.getId_nomenclatura_combinata())))));
				                                    	if(fa_intra.getNomenclatura_combinata()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_147.toString());
				                                    	
				                                    	fa_intra.setNatura_transazione(((Natura_transazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Natura_transazioneBulk(intra.getId_natura_transazione())))));
				                                    	if(fa_intra.getNatura_transazione()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_148.toString());
				                                    	
				                                    	fa_intra.setCondizione_consegna(((Condizione_consegnaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Condizione_consegnaBulk(intra.getCd_consegna(),testata.getEsercizio())))));
				                                    	if(fa_intra.getCondizione_consegna()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_149.toString());
				                                    	
				                                    	fa_intra.setModalita_trasporto(((Modalita_trasportoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new Modalita_trasportoBulk(intra.getCd_trasporto(),testata.getEsercizio())))));
				                                    	if(fa_intra.getModalita_trasporto()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_150.toString());
				                                    	
				                                    	fa_intra.setProvincia_origine(((ProvinciaBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new ProvinciaBulk(intra.getCd_provincia())))));
				                                    	if(fa_intra.getProvincia_origine()==null)
				                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_151.toString());
				                                    	if(intra.getMassa()!=null)
				                                    		fa_intra.setMassa_netta(intra.getMassa());
				                                    	if(intra.getValore_statistico()!=null)
				                                    		fa_intra.setValore_statistico(intra.getValore_statistico());
				                                    	if(intra.getUnita_supplementari()!=null)
				                                    		fa_intra.setUnita_supplementari(intra.getUnita_supplementari());
			                                    		
			                                    	}
			                                    	fa_intra.setNazione_destinazione(((NazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,new NazioneBulk(intra.getPg_nazione())))));
			                                    	if(fa_intra.getNazione_destinazione()==null)
			                                    		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_146.toString());
			                                    	
			                                    	fa_intra.setAmmontare_euro(intra.getAmmontare_euro());
			                                    	fa_intra.setFl_inviato(false);
			                                    	testata.getFattura_attiva_intrastatColl().add(fa_intra);
				    	                        }
			                    	        }// fine for listOfIntra
		                    	        }// fine if(fat.getFl_intra_ue()!=null && fat.getFl_intra_ue().booleanValue()){
		    	                        }//fine intrastat
	                    	        
		    		                //fine for listOfScad
		    	                    obb=(ObbligazioneBulk) ((ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class)).creaConBulk(userContext,obb);
		                    	    fatr.setPg_obbligazione_scadenzario(((Obbligazione_scadenzarioBulk)(obb.getObbligazione_scadenzarioColl().get(0))).getPg_obbligazione_scadenzario());
		                    	    obb_scadenza.setObbligazione(obb);
				                    Nota_di_credito_attivaBulk nc=(Nota_di_credito_attivaBulk)testata;
				                    Nota_di_credito_attiva_rigaBulk riga_nc=(Nota_di_credito_attiva_rigaBulk)(Fattura_attiva_rigaBulk)riga;
				                    riga_nc.setObbligazione_scadenzario(obb_scadenza);
				                    java.util.Hashtable relationsHash = new java.util.Hashtable();
				            	    relationsHash.put(riga_nc, riga_nc.getObbligazione_scadenzario());

				            	    nc=((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).stornaDettagli(userContext,nc,nc.getFattura_attiva_dettColl(),relationsHash);
				            	    nc.addToDefferredSaldi(obb_scadenza.getObbligazione(), obb_scadenza.getObbligazione().getSaldiInfo());
				            	    testata=nc;
		    	              }
		    	              // Fine Nota Credito
			            }
			      		//fine else campi null riga
		              }
			  		  //fine righe
		              if(fat.getCod_errore()==null){
		                	testata = (Fattura_attivaBulk)((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).creaConBulk(userContext,testata);
		                	fat.setPg_fattura_attiva(testata.getPg_fattura_attiva());
		                	fat.setIm_totale_imponibile(testata.getIm_totale_imponibile());
		                	fat.setIm_totale_iva(testata.getIm_totale_iva());
		                	for(int ra=0; ra<fat.getRighefat().size() ; ra++){
		                	    FatturaAttivaRiga fatr_agg = (FatturaAttivaRiga)listOfRighe.get(ra);
		                            if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)==0)){ 
		                            	Fattura_attiva_rigaIBulk riga_fat = (Fattura_attiva_rigaIBulk)testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
			                    			new Fattura_attiva_rigaIBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),testata.getEsercizio(),testata.getPg_fattura_attiva(),fatr_agg.getProgressivo_riga())));
		                            	fatr_agg.setPg_accertamento(((Accertamento_scadenzarioBulk)riga_fat.getAccertamento_scadenzario()).getPg_accertamento());
			                    }else{
			                    	Nota_di_credito_attiva_rigaBulk riga_fat = (Nota_di_credito_attiva_rigaBulk)testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
			                    			new Nota_di_credito_attiva_rigaBulk(testata.getCd_cds(),testata.getCd_unita_organizzativa(),testata.getEsercizio(),testata.getPg_fattura_attiva(),new Long(fatr_agg.getProgressivo_riga()))));
			                    	fatr_agg.setPg_obbligazione(((Obbligazione_scadenzarioBulk)riga_fat.getObbligazione_scadenzario()).getPg_obbligazione());
			                   }
		                	}
		               }else{
		            	   ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
		               }
	                   	
			     }catch (ApplicationException e){
	     		       ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
	         			fat.setCod_errore(Costanti.ERRORE_FA_100.toString());
	              	        fat.setDesc_errore(Costanti.erroriFA.get(Costanti.ERRORE_FA_100)+e.getMessage());
			     }catch (it.cnr.jada.bulk.ValidationException e){
	   		       ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
	       			fat.setCod_errore(Costanti.ERRORE_FA_100.toString());
	            	        fat.setDesc_errore(Costanti.erroriFA.get(Costanti.ERRORE_FA_100)+e.getMessage()); 	        
	             }catch (Exception e){
	             		((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).rollbackToSavePoint(context,"Fattura_automatica");
	             		fat=ValorizzaErrore(fat,Costanti.ERRORE_FA_999.toString());
	             }   
	           }
	           //else campi not null fattura
	         listOfFatture.set(s,fat);
	       }
	       	   //fine for fattura      
	      
		}catch (Exception err) {
	       	throw new Exception(err);
		}
		return listOfFatture;	
	}
	@RolesAllowed({"WSUserRole","IITRole"})
public FatturaAttiva RicercaFattura(String user,Long esercizio,String cds,String uo,Long pg) throws Exception {
	FatturaAttiva ritorno=new FatturaAttiva();
	java.util.ArrayList<FatturaAttivaRiga> righe=new java.util.ArrayList<FatturaAttivaRiga>();
	java.util.ArrayList<FatturaAttivaScad> righescad=new java.util.ArrayList<FatturaAttivaScad>();
	java.util.ArrayList<FatturaAttivaIntra> righeIntra=new java.util.ArrayList<FatturaAttivaIntra>();
	FatturaAttivaRiga rigaRitorno=new FatturaAttivaRiga();
	FatturaAttivaScad scad=new FatturaAttivaScad();
	FatturaAttivaIntra intra=new FatturaAttivaIntra();
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	 if(cds==null||uo==null||pg==null||esercizio==null)
		 throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
	try{	
		Fattura_attivaBulk fatturaAt =((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).ricercaFattura(userContext,esercizio, cds, uo, pg);
		ritorno.setCambio(fatturaAt.getCambio());
		ritorno.setCd_cds_origine(fatturaAt.getCd_cds_origine());
		ritorno.setCd_divisa(fatturaAt.getCd_divisa());
		
		ritorno.setCd_modalita_pag_uo_cds(fatturaAt.getModalita_pagamento_uo().getCd_modalita_pag());
		ritorno.setCd_terzo(fatturaAt.getCd_terzo());
		ritorno.setCd_terzo_uo_cds(fatturaAt.getCd_terzo_uo_cds());
		ritorno.setCd_tipo_sezionale(fatturaAt.getCd_tipo_sezionale());
		ritorno.setCd_uo_origine(fatturaAt.getCd_uo_origine());
		ritorno.setDs_fattura_attiva(fatturaAt.getDs_fattura_attiva());
		ritorno.setDt_registrazione(fatturaAt.getDt_registrazione());
		ritorno.setEsercizio(fatturaAt.getEsercizio());
		ritorno.setFl_extra_ue(fatturaAt.getFl_extra_ue());
		ritorno.setFl_intra_ue(fatturaAt.getFl_intra_ue());
		ritorno.setFl_liquidazione_differita(fatturaAt.getFl_liquidazione_differita());
		ritorno.setFl_san_marino(fatturaAt.getFl_san_marino());
		ritorno.setNote(fatturaAt.getNote());
		ritorno.setPg_banca_uo_cds(fatturaAt.getBanca_uo().getPg_banca());
		ritorno.setPg_fattura_attiva(fatturaAt.getPg_fattura_attiva());
		ritorno.setPg_fattura_esterno(fatturaAt.getPg_fattura_esterno());
		ritorno.setRif_ordine(fatturaAt.getRiferimento_ordine());
		ritorno.setTi_causale_emissione(fatturaAt.getTi_causale_emissione());
		ritorno.setTi_fattura(fatturaAt.getTi_fattura());
		ritorno.setUtcr(fatturaAt.getUtcr());
		ritorno.setNr_protocollo_iva(fatturaAt.getProtocollo_iva());
		ritorno.setDt_protocollo(fatturaAt.getDt_emissione());
		ritorno.setFl_pagamento_anticipato(fatturaAt.getFl_pagamento_anticipato());
		for (Iterator i=fatturaAt.getFattura_attiva_intrastatColl().iterator();i.hasNext();){
			Fattura_attiva_intraBulk riga_intra=(Fattura_attiva_intraBulk)i.next();
			intra=new FatturaAttivaIntra();
			intra.setAmmontare_euro(riga_intra.getAmmontare_euro());
			intra.setCod_erogazione(riga_intra.getCd_modalita_erogazione());
			intra.setCod_incasso(riga_intra.getCd_modalita_incasso());
			intra.setId_cpa(riga_intra.getId_cpa());
			intra.setPg_nazione(riga_intra.getPg_nazione_destinazione());
			righeIntra.add(intra);
		}
		ritorno.setRigheIntra(righeIntra);
		
		for (Iterator i=fatturaAt.getFattura_attiva_dettColl().iterator();i.hasNext();){
			Fattura_attiva_rigaBulk riga=(Fattura_attiva_rigaBulk)i.next();
			rigaRitorno=new FatturaAttivaRiga();
			rigaRitorno.setCd_tariffario(riga.getCd_tariffario());
			rigaRitorno.setCd_voce_iva(riga.getCd_voce_iva());
			rigaRitorno.setDs_riga_fattura(riga.getDs_riga_fattura());
			rigaRitorno.setFl_iva_forzata(riga.getFl_iva_forzata());
			rigaRitorno.setEsercizio_assncna_fin(riga.getEsercizio_assncna_fin());
			rigaRitorno.setPg_fattura_assncna_fin(riga.getPg_fattura_assncna_fin());
			rigaRitorno.setPg_riga_assncna_fin(riga.getPg_riga_assncna_fin());
			rigaRitorno.setPrezzo_unitario(riga.getPrezzo_unitario());
			rigaRitorno.setProgressivo_riga(riga.getProgressivo_riga());
			rigaRitorno.setQuantita(riga.getQuantita());
			if (fatturaAt instanceof Fattura_attiva_IBulk){ 
				AccertamentoBulk acc_db=new AccertamentoBulk(riga.getAccertamento_scadenzario().getAccertamento().getCd_cds(),riga.getAccertamento_scadenzario().getAccertamento().getEsercizio(),riga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale(),riga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento());
				acc_db=(((AccertamentoBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,acc_db))));
				rigaRitorno.setCd_voce(acc_db.getCapitolo().getCd_voce());
				rigaRitorno.setDs_accertamento(acc_db.getDs_accertamento());
				rigaRitorno.setEsercizio_contratto(acc_db.getEsercizio_contratto());
				rigaRitorno.setStato_contratto(acc_db.getStato_contratto());
				rigaRitorno.setPg_contratto(acc_db.getPg_contratto());
				rigaRitorno.setPg_accertamento(acc_db.getPg_accertamento());
				Accertamento_scadenzarioBulk acc_scad_db=new Accertamento_scadenzarioBulk(riga.getAccertamento_scadenzario().getAccertamento().getCd_cds(),riga.getAccertamento_scadenzario().getAccertamento().getEsercizio(),riga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale(),riga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento(),riga.getAccertamento_scadenzario().getPg_accertamento_scadenzario());
				acc_scad_db=(((Accertamento_scadenzarioBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,acc_scad_db))));
				rigaRitorno.setDt_scadenza(acc_scad_db.getDt_scadenza_incasso());
				rigaRitorno.setPg_accertamento_scadenzario(acc_scad_db.getPg_accertamento_scadenzario());
				java.util.List scadenzevoce =((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).recuperoScadVoce(userContext,riga.getAccertamento_scadenzario() );
				for(Iterator s=scadenzevoce.iterator();s.hasNext();){
					scad=new FatturaAttivaScad();
					Accertamento_scad_voceBulk scadVoce=(Accertamento_scad_voceBulk)s.next();
					scadVoce=(((Accertamento_scad_voceBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,scadVoce))));
		            scad.setCdr(scadVoce.getLinea_attivita().getCd_centro_responsabilita());
		            scad.setGae(scadVoce.getLinea_attivita().getCd_linea_attivita());
		            scad.setIm_voce(scadVoce.getIm_voce());
		            righescad.add(scad);
				}
				rigaRitorno.setRighescadvoc(righescad);				
			}
			else{
				Nota_di_credito_attivaBulk nc=(Nota_di_credito_attivaBulk)fatturaAt;
				Nota_di_credito_attiva_rigaBulk nc_riga=(Nota_di_credito_attiva_rigaBulk) riga;
				ritorno.setCd_modalita_pag(nc.getModalita_pagamento().getCd_modalita_pag()); 
				ritorno.setPg_banca(nc.getBanca().getPg_banca());
				ObbligazioneBulk obb_db=nc_riga.getObbligazione_scadenzario().getObbligazione();
				obb_db=(((ObbligazioneBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,obb_db))));
				rigaRitorno.setCd_voce(obb_db.getCd_elemento_voce());
				rigaRitorno.setDs_obbligazione(obb_db.getDs_obbligazione());
				rigaRitorno.setMotivazione(obb_db.getMotivazione());
				rigaRitorno.setEsercizio_contratto(obb_db.getEsercizio_contratto());
				rigaRitorno.setStato_contratto(obb_db.getStato_contratto());
				rigaRitorno.setPg_contratto(obb_db.getPg_contratto());
				rigaRitorno.setPg_obbligazione(obb_db.getPg_obbligazione());
				Obbligazione_scadenzarioBulk obb_scad_db=nc_riga.getObbligazione_scadenzario();
				obb_scad_db=(((Obbligazione_scadenzarioBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,obb_scad_db))));
				rigaRitorno.setDt_scadenza(obb_scad_db.getDt_scadenza());
				rigaRitorno.setPg_obbligazione_scadenzario(obb_scad_db.getPg_obbligazione_scadenzario());
				java.util.List scadenzevoce =((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).recuperoScadVoce(userContext,obb_scad_db );
				for(Iterator s=scadenzevoce.iterator();s.hasNext();){
					scad=new FatturaAttivaScad();
					Obbligazione_scad_voceBulk scadVoce=(Obbligazione_scad_voceBulk)s.next();
					scadVoce=(((Obbligazione_scad_voceBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(userContext,scadVoce))));
		            scad.setCdr(scadVoce.getLinea_attivita().getCd_centro_responsabilita());
		            scad.setGae(scadVoce.getLinea_attivita().getCd_linea_attivita());
		            scad.setIm_voce(scadVoce.getIm_voce());
		            righescad.add(scad);
				}
				rigaRitorno.setRighescadvoc(righescad);
			}
			righe.add(rigaRitorno);
			righescad=new java.util.ArrayList<FatturaAttivaScad>();
		}
		ritorno.setRighefat(righe);
		
	   return ritorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}
private SOAPFault faultChiaveFatturaNonCompleta() throws SOAPException{
	return generaFault("001","Identificativo Fattura non valido e/o incompleto");
}
private SOAPFault faultFatturaNonTrovata() throws SOAPException{
	return generaFault("002","Fattura non trovata");
}

private SOAPFault generaFault(String localName,String stringFault) throws SOAPException{
	MessageFactory factory = MessageFactory.newInstance();
	SOAPMessage message = factory.createMessage(); 
	SOAPFactory soapFactory = SOAPFactory.newInstance();
	SOAPBody body = message.getSOAPBody(); 
	SOAPFault fault = body.addFault();
	Name faultName = soapFactory.createName(localName,"", SOAPConstants.URI_NS_SOAP_ENVELOPE);
	fault.setFaultCode(faultName);
	fault.setFaultString(stringFault);
	return fault;
}
	private FatturaAttiva ValorizzaErrore(FatturaAttiva fat,String codice,String campo){
			fat.setCod_errore(codice);
			if(campo!= null )
				fat.setDesc_errore(Costanti.erroriFA.get(new Integer(codice))+" "+campo);
			else
				fat.setDesc_errore(Costanti.erroriFA.get(new Integer(codice)));
	      	return fat;
	}
	private FatturaAttiva ValorizzaErrore(FatturaAttiva fat,String codice){
		return ValorizzaErrore(fat,codice,null);
	}
	private  String Controllo_campo_errore(Object f){
		FatturaAttiva fat=null;
		FatturaAttivaRiga riga=null;
		FatturaAttivaScad scad=null;
		FatturaAttivaIntra intra=null;
	if(f instanceof FatturaAttiva )
		 fat=(FatturaAttiva)f;
	else if (f instanceof FatturaAttivaRiga )
		 riga=(FatturaAttivaRiga)f;
	else if (f instanceof FatturaAttivaScad )	
		 scad=(FatturaAttivaScad)f;
	else if(f instanceof FatturaAttivaIntra)
	     intra =(FatturaAttivaIntra)f;
	if (fat !=null){
		if (fat.getEsercizio()==null)
			return new String("Esercizio");
       	if (fat.getUtcr()==null )
       		return new String("Utcr");
       	if (fat.getCd_cds_origine()==null )
       		return new String("Cds origine");
       	if (fat.getCd_uo_origine()==null)
       		return new String("Uo origine");
       	if (fat.getPg_fattura_esterno()==null)
       	    return new String("Progressivo esterno");
       	if (fat.getTi_fattura()==null)
       		return new String("Tipo Fattura non inserito.");
    	if (fat.getTi_bene_servizio()==null)
       		return new String("Tipo bene/servizio non inserito.");
       	if (fat.getCd_tipo_sezionale()==null)
       		return new String("Tipo Sezionale non inserito.");
       	if (fat.getDt_registrazione()==null)
       		return new String("Data registrazione non inserita.");
       	if (fat.getTi_causale_emissione()==null)
       		return new String("Causale emissione non inserita.");
       	if (fat.getFl_liquidazione_differita()==null)
       		return new String("Flag Liquidazione differita non inserito.");
       	if (fat.getFl_intra_ue()==null)
   		   return new String("Flag Intra UE non inserito.");
       	if (fat.getFl_extra_ue()==null)
    	   return new String("Flag Extra UE non inserito.");
       	if (fat.getFl_san_marino()==null)
    	   return new String("Flag San Marino non inserito.");
       	if (fat.getCd_terzo()==null)
    	   return new String("Codice Terzo non inserito.");
       	if (fat.getCd_terzo_uo_cds()==null)
    	   return new String("Codice Terzo UO non inserito.");
       	if (fat.getPg_banca_uo_cds()==null)
       		return new String("Progressivo Banca UO non inserito.");
       	if (fat.getCd_modalita_pag_uo_cds()==null)
       		return new String("Modalità pagamento Uo non inserita.");
       	if (fat.getCd_divisa()==null)
       		return new String("Divisa non inserita.");
       	if (fat.getCambio()==null)
       		return new String("Cambio non inserito.");
     	if (fat.getFl_pagamento_anticipato()==null)
       		return new String("Flag pagamento non inserito.");
       	if(fat.getRighefat()==null||fat.getRighefat().size()==0)
       		return new String("Righe fattura non inserite.");
       	else
       		return null;
      }else if (riga!=null){
    	  if (riga.getFl_iva_forzata()==null)
    		  return new String("Flag Iva forzata non inserito.");
    	  if (riga.getFl_iva_forzata().booleanValue())
    		  if(riga.getIm_iva()==null)
    			  return new String("Importo Iva forzata non inserito.");
    	  if (riga.getCd_voce()==null)
    		  return new String("Codice voce non inserito.");
    	  if (riga.getCd_bene_servizio()==null)
    		  return new String("Codice bene/servizio non inserito.");
    	  if (riga.getCd_tariffario()==null &&
    		  riga.getPrezzo_unitario()==null)
    		  return new String("Tariffario o Prezzo unitario non inserito.");
    	  if (riga.getCd_tariffario()==null &&
    	      riga.getPrezzo_unitario()!=null &&
    		  riga.getCd_voce_iva()==null)
    		  return new String("Codice voce iva non inserito.");
    	  if (riga.getDt_scadenza()==null)
    		  return new String ("Data Scadenza non inserita.");
    	  if (riga.getCd_tariffario()==null &&
        	  riga.getPrezzo_unitario()!=null &&
        	  riga.getQuantita()==null)
        		  return new String("Quantità non inserita.");
    	  if(riga.getRighescadvoc()==null||riga.getRighescadvoc().size()==0)
         		return new String("Righe Scadenza voce non inserite.");      	
    	  else
    		  return  null;
      }else if (scad!=null){
    	  if(scad.getIm_voce()==null)
    		  return new String("Importo voce non inserito.");
    	  if(scad.getCdr()==null)
    		  return new String("Cdr non inserito.");
    	  if(scad.getGae()==null)
    		  return new String("Gae non insetita.");
    	  else
    		  return null;
      
      }else if (intra!=null){
    	  if(intra.getCod_erogazione()!=null ||intra.getCod_incasso()!=null||intra.getId_cpa()!=null ){
		  	  if(intra.getCod_erogazione()==null)
		  		  return new String("Modalità erogazione non inserita.");
		  	  if(intra.getCod_incasso()==null)
		  		  return new String("Modalità incasso non inserita.");
		  	  if(intra.getId_cpa()==null)
			  		return new String("Id servizio non inserito.");
    	  }
    	  else if(intra.getId_nomenclatura_combinata()!=null||intra.getId_natura_transazione()!=null||
    			  intra.getCd_provincia()!=null||intra.getCd_trasporto()!=null||intra.getCd_consegna()!=null ){
    		  if(intra.getId_nomenclatura_combinata()==null)
    			  return new String("Nomenclatura combinata non inserita.");
    		  if(intra.getId_natura_transazione()==null)
    			  return new String("Natura transazione non inserita.");
    		  if(intra.getCd_provincia()==null)
    			  return new String("Provincia non inserita.");
    		  if(intra.getCd_trasporto()==null)
    			  return new String("Modalità trasporto non inserita.");
    		  if(intra.getCd_consegna()==null)
    			  return new String("Condizione consegna non inserita.");
    	  }
    	  if(intra.getAmmontare_euro()==null)
	  		  return new String("Ammontare euro dettaglio intrastat non inserito");
	  	  if(intra.getPg_nazione()==null)
	  		return new String("Nazione pagamento non inserita.");
	  	  else
	  		  return null;
      }
	return null;
  }
}
                   

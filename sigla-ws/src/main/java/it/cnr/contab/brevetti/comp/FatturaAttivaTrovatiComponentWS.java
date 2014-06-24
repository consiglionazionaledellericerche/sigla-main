package it.cnr.contab.brevetti.comp;

import it.cnr.contab.brevetti.client.FatturaAttiva;
import it.cnr.contab.brevetti.client.FatturaAttivaBase;
import it.cnr.contab.brevetti.client.FatturaPassiva;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.FatturaNonTrovataException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;

import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;
@XmlSeeAlso({java.util.ArrayList.class}) 
@Stateless
@WebService(endpointInterface="it.cnr.contab.brevetti.ejb.FatturaAttivaTrovatiComponentSessionWS")
@DeclareRoles({"WSUserRole","BrevettiRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")
public class FatturaAttivaTrovatiComponentWS {
@RolesAllowed({"WSUserRole","BrevettiRole"})
private java.util.ArrayList<FatturaAttiva> ricercaFatturaAttiva(String user, Long esercizio, String cds, String uo, Long pg, boolean byKey) throws Exception {
	java.util.ArrayList listaRitorno=new ArrayList();
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	if(cds==null||uo==null||pg==null||esercizio==null)
		 throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
	try{	
		Fattura_attivaBulk fattura = null;
		if (byKey)
			fattura = ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).ricercaFatturaByKey(userContext,esercizio, cds, uo, pg);
		else
			fattura = ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).ricercaFattura(userContext,esercizio, cds, uo, pg);
		
		caricaFattura(userContext, listaRitorno, fattura, Boolean.FALSE);

		return listaRitorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaAttiva> ricercaFatturaAttiva(String user, Long esercizio, String cds, String uo, Long pg) throws Exception {
	return ricercaFatturaAttiva(user, esercizio, cds, uo, pg, false);
}
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaAttiva> ricercaFatturaAttivaByKey(String user, Long esercizio, String cds, String uo, Long pg) throws Exception {
	return ricercaFatturaAttiva(user, esercizio, cds, uo, pg, true);
}
/**
 * Restituisce le fatture legate al progressivo "trovato" con informazioni complete
 * 
 * @param user
 * @param trovato
 * @return
 * @throws Exception
 */
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaAttiva> ricercaFattureAttive(String user, Long trovato) throws Exception {
	return ricercaFatture(user, trovato, Boolean.FALSE);
}
/**
 * Restituisce le fatture legate al progressivo "trovato" con minori informazioni
 * 
 * @param user
 * @param trovato
 * @return
 * @throws Exception
 */
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaAttivaBase> ricercaFattureAttiveBase(String user, Long trovato) throws Exception {
	return ricercaFatture(user, trovato, Boolean.TRUE);
}
private java.util.ArrayList ricercaFatture(String user, Long trovato, Boolean base) throws Exception {
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	java.util.ArrayList listaRitorno=new ArrayList();
	if(trovato==null)
		 throw new SOAPFaultException(faultIdTrovatoNullo());
	try{	
		java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk> fatture =((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).ricercaFattureTrovato(userContext, trovato);
		for (Iterator<Fattura_attivaBulk> i = fatture.iterator(); i.hasNext(); ) {
			Fattura_attivaBulk fattura=(Fattura_attivaBulk)i.next();

			caricaFattura(userContext, listaRitorno, fattura, base);
		}

		return listaRitorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}
private void caricaFattura(UserContext userContext, java.util.ArrayList listaRitorno, Fattura_attivaBulk fattura, Boolean base) throws Exception {
	// serve per diminuire traffico di rete con oggetti più grandi
	// e informazioni che non sono necessarie (richiesto dai colleghi di brevetti)
	if (base.booleanValue()) {
		FatturaAttivaBase ritorno=new FatturaAttivaBase();
		ritorno.setCd_cds(fattura.getCd_cds());
		ritorno.setCd_unita_organizzativa(fattura.getCd_unita_organizzativa());
		ritorno.setEsercizio(fattura.getEsercizio());
		ritorno.setPg_fattura_attiva(fattura.getPg_fattura_attiva());
		ritorno.setDs_fattura_attiva(fattura.getDs_fattura_attiva());
		ritorno.setCd_cds_origine(fattura.getCd_cds_origine());
		ritorno.setCd_uo_origine(fattura.getCd_uo_origine());
		ritorno.setDt_emissione(fattura.getDt_emissione());
		ritorno.setCd_terzo(fattura.getCd_terzo());

		listaRitorno.add(ritorno);

	} else {
		BulkList<Fattura_attiva_rigaIBulk> dett = fattura.getFattura_attiva_dettColl();
		
		for (Iterator<Fattura_attiva_rigaIBulk> j = dett.iterator(); j.hasNext(); ) {
			Fattura_attiva_rigaIBulk det = (Fattura_attiva_rigaIBulk) j.next();
			FatturaAttiva ritorno=new FatturaAttiva();

			ritorno.setCd_cds(fattura.getCd_cds());
			ritorno.setCd_unita_organizzativa(fattura.getCd_unita_organizzativa());
			ritorno.setEsercizio(fattura.getEsercizio());
			ritorno.setPg_fattura_attiva(fattura.getPg_fattura_attiva());
			ritorno.setDs_fattura_attiva(fattura.getDs_fattura_attiva());
			ritorno.setCd_cds_origine(fattura.getCd_cds_origine());
			ritorno.setCd_uo_origine(fattura.getCd_uo_origine());
			ritorno.setTi_fattura(fattura.getTi_fattura());
			ritorno.setDs_fattura_attiva(fattura.getDs_fattura_attiva());
			ritorno.setDt_registrazione(fattura.getDt_registrazione());
			ritorno.setCd_terzo(fattura.getCd_terzo());
			ritorno.setCognome(fattura.getCognome());
			ritorno.setNome(fattura.getNome());
			ritorno.setRagione_sociale(fattura.getRagione_sociale());
			ritorno.setCambio(fattura.getCambio());
			ritorno.setCd_divisa(fattura.getCd_divisa());
			
			ritorno.setProgressivo_riga(det.getProgressivo_riga());
			ritorno.setCd_voce_iva(det.getCd_voce_iva());
			ritorno.setDs_voce_iva(det.getVoce_iva().getDs_voce_iva());
			ritorno.setDs_riga_fattura(det.getDs_riga_fattura());
			impostaImportiRiga(det, ritorno);
			ritorno.setPg_trovato(det.getPg_trovato());

			if (det.getAccertamento_scadenzario() != null && det.getAccertamento_scadenzario().getAccertamento() != null){
				AccertamentoBulk acc = det.getAccertamento_scadenzario().getAccertamento();
				ritorno.setPg_accertamento(acc.getPg_accertamento());
				ritorno.setEsercizio_accertamento(acc.getEsercizio());
				ritorno.setDt_emissione_accertamento(acc.getDt_registrazione());
			}

			List revs = det.getReversaliRighe();
			Reversale_rigaIBulk revr=null;
			if (revs!=null && !revs.isEmpty()) {
				revr=(Reversale_rigaIBulk) revs.get(0);
				ritorno.setEsercizio_reversale(revr.getEsercizio());
				ritorno.setPg_reversale(revr.getPg_reversale());
				ritorno.setDt_emissione_reversale(revr.getReversale().getDt_emissione());
			}
			listaRitorno.add(ritorno);
		}
		
	}
}

private void impostaImportiRiga(Fattura_attiva_rigaBulk det, FatturaAttiva ritorno) {
	if (det.getVoce_iva() != null){
		ritorno.setDs_voce_iva(det.getVoce_iva().getDs_voce_iva());
		if ("Y".equals(det.getVoce_iva().getFl_non_soggetto())){
			ritorno.setIm_imponibile(BigDecimal.ZERO);
			ritorno.setFcIva(det.getIm_imponibile());
		} else {
			ritorno.setIm_imponibile(det.getIm_imponibile());
			ritorno.setFcIva(BigDecimal.ZERO);
		}
	} else {
		ritorno.setIm_imponibile(det.getIm_imponibile());
		ritorno.setFcIva(BigDecimal.ZERO);
	}
	ritorno.setIm_iva(det.getIm_iva());
}

private SOAPFault faultChiaveFatturaNonCompleta() throws SOAPException{
	return generaFault("001","Identificativo Fattura non valido e/o incompleto");
}
private SOAPFault faultIdTrovatoNullo() throws SOAPException{
	return generaFault("001","Identificativo Trovato non valorizzato");
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
}
                   

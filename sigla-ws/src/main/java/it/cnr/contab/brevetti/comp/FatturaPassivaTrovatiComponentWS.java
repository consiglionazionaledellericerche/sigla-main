package it.cnr.contab.brevetti.comp;

import it.cnr.contab.brevetti.client.Compenso;
import it.cnr.contab.brevetti.client.FatturaPassiva;
import it.cnr.contab.brevetti.client.FatturaPassivaBase;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.FatturaNonTrovataException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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

import org.jboss.ws.annotation.EndpointConfig;
import org.jboss.wsf.spi.annotation.WebContext;
//@XmlSeeAlso({java.util.ArrayList.class,it.cnr.contab.brevetti.client.FatturaPassiva.class,it.cnr.contab.brevetti.client.FatturaPassivaBase.class}) 
@XmlSeeAlso({java.util.ArrayList.class}) 
@Stateless
@WebService(endpointInterface="it.cnr.contab.brevetti.ejb.FatturaPassivaTrovatiComponentSessionWS")
@DeclareRoles({"WSUserRole","BrevettiRole"})
// annotation proprietarie di JBoss, purtroppo in JBoss 4.2.2 non funzionano i corrispondenti tag in jboss.xml
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@WebContext(contextRoot="/SIGLA-SIGLAEJB")
public class FatturaPassivaTrovatiComponentWS {
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaPassiva> ricercaFatturaPassiva(String user, Long esercizio, String cds, String uo, Long pg, boolean byKey) throws Exception {
	java.util.ArrayList listaRitorno=new ArrayList();
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	if(cds==null||uo==null||pg==null||esercizio==null)
		 throw new SOAPFaultException(faultChiaveFatturaNonCompleta());
	try{	
		Fattura_passivaBulk fattura = null;
		if (byKey)
			fattura = ((FatturaPassivaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession",FatturaPassivaComponentSession.class)).ricercaFatturaByKey(userContext,esercizio, cds, uo, pg);
		else
			fattura = ((FatturaPassivaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession",FatturaPassivaComponentSession.class)).ricercaFatturaTrovato(userContext,esercizio, cds, uo, pg);

		caricaFattura(userContext, listaRitorno, fattura, Boolean.FALSE);

		return listaRitorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaPassiva> ricercaFatturaPassiva(String user, Long esercizio, String cds, String uo, Long pg) throws Exception {
	return ricercaFatturaPassiva(user, esercizio, cds, uo, pg, false);
}
@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<FatturaPassiva> ricercaFatturaPassivaByKey(String user, Long esercizio, String cds, String uo, Long pg) throws Exception {
	return ricercaFatturaPassiva(user, esercizio, cds, uo, pg, true);
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
public java.util.ArrayList<FatturaPassiva> ricercaFatturePassive(String user, Long trovato) throws Exception {
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
public java.util.ArrayList<FatturaPassivaBase> ricercaFatturePassiveBase(String user, Long trovato) throws Exception {
	return ricercaFatture(user, trovato, Boolean.TRUE);
}
private java.util.ArrayList ricercaFatture(String user, Long trovato, Boolean base) throws Exception {
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	java.util.ArrayList listaRitorno=new ArrayList();
	if(trovato==null)
		 throw new SOAPFaultException(faultIdTrovatoNullo());
	try{	
		java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk> fatture =((FatturaPassivaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession",FatturaPassivaComponentSession.class)).ricercaFattureTrovato(userContext, trovato);
		for (Iterator<Fattura_passivaBulk> i = fatture.iterator(); i.hasNext(); ) {
			Fattura_passivaBulk fattura=(Fattura_passivaBulk)i.next();

			caricaFattura(userContext, listaRitorno, fattura, base);
		}

		return listaRitorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}

private java.util.ArrayList ricercaCompensi(String user, Long trovato, Boolean base) throws Exception {
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	java.util.ArrayList listaRitorno=new ArrayList();
	if(trovato==null)
		 throw new SOAPFaultException(faultIdTrovatoNullo());
	try{	
		CompensoComponentSession compensoComponent = recuperoCompensoComponent();
		List<CompensoBulk> compensi = compensoComponent.ricercaCompensiTrovato(userContext, trovato);
		for (Iterator<CompensoBulk> i = compensi.iterator(); i.hasNext(); ) {
			CompensoBulk compenso=(CompensoBulk)i.next();

			caricaCompenso(userContext, listaRitorno, compenso, base);
		}

		return listaRitorno;  
	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultFatturaNonTrovata());
	}
}



@RolesAllowed({"WSUserRole","BrevettiRole"})
public Compenso ricercaCompenso(String user, Long esercizio, String cds, String uo, Long pg, boolean byKey) throws Exception {
	java.util.ArrayList listaRitorno=new ArrayList();
	UserContext userContext = new WSUserContext(user,null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
	if(cds==null||uo==null||pg==null||esercizio==null)
		 throw new SOAPFaultException(faultChiaveCompensoNonCompleta());
	try{	
		CompensoBulk compenso = null;
		
		CompensoComponentSession compensoComponent = recuperoCompensoComponent();
		if (byKey)
			compenso = compensoComponent.ricercaCompensoTrovato(userContext,esercizio, cds, uo, pg);
		else
			compenso = compensoComponent.ricercaCompensoByKey(userContext,esercizio, cds, uo, pg);

		caricaCompenso(userContext, listaRitorno, compenso, Boolean.FALSE);
		
		return (Compenso)listaRitorno.get(0);

	}catch(FatturaNonTrovataException e){
		throw new SOAPFaultException(faultCompensoNonTrovato());
	}
}
private CompensoComponentSession recuperoCompensoComponent() {
	CompensoComponentSession compensoComponent = ((CompensoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_CompensoComponentSession",CompensoComponentSession.class));
	return compensoComponent;
}

@RolesAllowed({"WSUserRole","BrevettiRole"})
public Compenso ricercaCompenso(String user,Long esercizio,String cds,String uo, Long pg)  throws Exception {
	return ricercaCompenso(user, esercizio, cds, uo, pg, false);
}

public Compenso ricercaCompensoByKey(String user,Long esercizio,String cds,String uo, Long pg)  throws Exception {
	return ricercaCompenso(user, esercizio, cds, uo, pg, true);
}


@RolesAllowed({"WSUserRole","BrevettiRole"})
public java.util.ArrayList<Compenso> ricercaCompensi(String user, Long trovato) throws Exception {
	return ricercaCompensi(user, trovato, Boolean.FALSE);
}

private void caricaFattura(UserContext userContext, java.util.ArrayList listaRitorno, Fattura_passivaBulk fattura, Boolean base) throws Exception {
	// serve per diminuire traffico di rete con oggetti più grandi
	// e informazioni che non sono necessarie (richiesto dai colleghi di brevetti)
	if (base.booleanValue()) {
		FatturaPassivaBase ritorno=new FatturaPassivaBase();
		ritorno.setCd_cds(fattura.getCd_cds());
		ritorno.setCd_unita_organizzativa(fattura.getCd_unita_organizzativa());
		ritorno.setEsercizio(fattura.getEsercizio());
		ritorno.setPg_fattura_passiva(fattura.getPg_fattura_passiva());
		ritorno.setCd_cds_origine(fattura.getCd_cds_origine());
		ritorno.setCd_uo_origine(fattura.getCd_uo_origine());
		ritorno.setDs_fattura_passiva(fattura.getDs_fattura_passiva());
		ritorno.setNr_fattura_fornitore(fattura.getNr_fattura_fornitore());
		ritorno.setDt_fattura_fornitore(fattura.getDt_fattura_fornitore());
		ritorno.setPartita_iva(fattura.getPartita_iva());
		ritorno.setCodice_fiscale(fattura.getCodice_fiscale());
		ritorno.setTipoFatturaCompenso(FatturaPassivaBase.TIPO_FATTURA);
		ritorno.setCd_terzo(fattura.getCd_terzo());

		listaRitorno.add(ritorno);
	} else {
		BulkList<Fattura_passiva_rigaBulk> dett = fattura.getFattura_passiva_dettColl();
		for (Iterator<Fattura_passiva_rigaBulk> j = dett.iterator(); j.hasNext(); ) {
			Fattura_passiva_rigaBulk det = (Fattura_passiva_rigaBulk) j.next();
			FatturaPassiva ritorno=new FatturaPassiva();
			
			ritorno.setCd_cds(fattura.getCd_cds());
			ritorno.setCd_unita_organizzativa(fattura.getCd_unita_organizzativa());
			ritorno.setEsercizio(fattura.getEsercizio());
			ritorno.setPg_fattura_passiva(fattura.getPg_fattura_passiva());
			ritorno.setCd_cds_origine(fattura.getCd_cds_origine());
			ritorno.setCd_uo_origine(fattura.getCd_uo_origine());
			ritorno.setTi_fattura(fattura.getTi_fattura());
			ritorno.setDs_fattura_passiva(fattura.getDs_fattura_passiva());
			ritorno.setNr_fattura_fornitore(fattura.getNr_fattura_fornitore());
			ritorno.setDt_fattura_fornitore(fattura.getDt_fattura_fornitore());
			ritorno.setPartita_iva(fattura.getPartita_iva());
			ritorno.setCodice_fiscale(fattura.getCodice_fiscale());
			ritorno.setDt_registrazione(fattura.getDt_registrazione());
			ritorno.setCd_terzo(fattura.getCd_terzo());
			ritorno.setCognome(fattura.getCognome());
			ritorno.setNome(fattura.getNome());
			ritorno.setRagione_sociale(fattura.getRagione_sociale());
			ritorno.setCambio(fattura.getCambio());
			ritorno.setCd_divisa(fattura.getCd_divisa());
			
			ritorno.setProgressivo_riga(det.getProgressivo_riga());
			ritorno.setCd_voce_iva(det.getCd_voce_iva());
			impostaImportiRiga(det, ritorno);
			ritorno.setDs_riga_fattura(det.getDs_riga_fattura());
			ritorno.setPg_trovato(det.getPg_trovato());
			ritorno.setDt_pagamento_fondo_eco(fattura.getDt_pagamento_fondo_eco());
			ritorno.setStato_pagamento_fondo_eco(fattura.getStato_pagamento_fondo_eco());
			
			if (det.getObbligazione_scadenziario() != null && det.getObbligazione_scadenziario().getObbligazione() != null){
				ObbligazioneBulk obbl = det.getObbligazione_scadenziario().getObbligazione();
				ritorno.setPg_obbligazione_impegno(obbl.getPg_obbligazione());
				ritorno.setEsercizio_obbligazione_impegno(obbl.getEsercizio());
				ritorno.setDt_emissione_obbligazione_impegno(obbl.getDt_registrazione());
			}
		
			if (det instanceof Fattura_passiva_rigaIBulk){
				Fattura_passiva_rigaIBulk fatturaPassivaRiga = (Fattura_passiva_rigaIBulk)det; 
				List mans = fatturaPassivaRiga.getMandatiRighe();
				Mandato_rigaIBulk manr=null;
				if (mans!=null && !mans.isEmpty()) {
					manr=(Mandato_rigaIBulk) mans.get(0);
					ritorno.setEsercizio_mandato(manr.getEsercizio());
					ritorno.setPg_mandato(manr.getPg_mandato());
					ritorno.setDt_emissione_mandato(manr.getMandato().getDt_emissione());
				}
			}
		
			listaRitorno.add(ritorno);
			
		}
	}
}

private void impostaImportiRiga(Fattura_passiva_rigaBulk det, FatturaPassiva ritorno) {
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

private void caricaCompenso(UserContext userContext, java.util.ArrayList listaRitorno, CompensoBulk compenso, Boolean base) throws Exception {
	// serve per diminuire traffico di rete con oggetti più grandi
	// e informazioni che non sono necessarie (richiesto dai colleghi di brevetti)
	if (base.booleanValue()) {
		FatturaPassivaBase ritorno=new FatturaPassivaBase();
		ritorno.setCd_cds(compenso.getCd_cds());
		ritorno.setCd_unita_organizzativa(compenso.getCd_unita_organizzativa());
		ritorno.setEsercizio(compenso.getEsercizio());
		ritorno.setPg_fattura_passiva(compenso.getPg_compenso());
		ritorno.setCd_cds_origine(compenso.getCd_cds_origine());
		ritorno.setCd_uo_origine(compenso.getCd_uo_origine());
		ritorno.setDs_fattura_passiva(compenso.getDs_compenso());
		ritorno.setNr_fattura_fornitore(compenso.getNr_fattura_fornitore());
		ritorno.setDt_fattura_fornitore(compenso.getDt_fattura_fornitore());
		ritorno.setPartita_iva(compenso.getPartita_iva());
		ritorno.setCodice_fiscale(compenso.getCodice_fiscale());
		ritorno.setTipoFatturaCompenso(FatturaPassivaBase.TIPO_COMPENSO);
		ritorno.setCd_terzo(compenso.getCd_terzo());
		listaRitorno.add(ritorno);
	} else {
		Compenso ritorno=new Compenso();

		ritorno.setCd_cds(compenso.getCd_cds());
		ritorno.setCd_unita_organizzativa(compenso.getCd_unita_organizzativa());
		ritorno.setEsercizio(compenso.getEsercizio());
		ritorno.setPg_fattura_passiva(compenso.getPg_compenso());
		ritorno.setCd_cds_origine(compenso.getCd_cds_origine());
		ritorno.setCd_uo_origine(compenso.getCd_uo_origine());
		ritorno.setDs_fattura_passiva(compenso.getDs_compenso());
		ritorno.setNr_fattura_fornitore(compenso.getNr_fattura_fornitore());
		ritorno.setDt_fattura_fornitore(compenso.getDt_fattura_fornitore());
		ritorno.setPartita_iva(compenso.getPartita_iva());
		ritorno.setCodice_fiscale(compenso.getCodice_fiscale());
		ritorno.setDt_registrazione(compenso.getDt_registrazione());
		ritorno.setCd_terzo(compenso.getCd_terzo());
		ritorno.setCognome(compenso.getCognome());
		ritorno.setNome(compenso.getNome());
		ritorno.setRagione_sociale(compenso.getRagione_sociale());

		ritorno.setCd_voce_iva(compenso.getCd_voce_iva());
		if (compenso.getVoceIva() != null){
			ritorno.setDs_voce_iva(compenso.getVoceIva().getDs_voce_iva());
		}
		ritorno.setImLordoPercipiente(compenso.getIm_lordo_percipiente());
		ritorno.setImNettoPercipiente(compenso.getIm_netto_percipiente());
		ritorno.setImTotaleCompenso(compenso.getIm_totale_compenso());
		ritorno.setPg_trovato(compenso.getPg_trovato());
		ritorno.setDt_pagamento_fondo_eco(compenso.getDt_pagamento_fondo_eco());
		ritorno.setStato_pagamento_fondo_eco(compenso.getStato_pagamento_fondo_eco());

		if (compenso.getObbligazioneScadenzario() != null && compenso.getObbligazioneScadenzario().getObbligazione() != null){
			ObbligazioneBulk obbl = compenso.getObbligazioneScadenzario().getObbligazione();
			ritorno.setPg_obbligazione_impegno(obbl.getPg_obbligazione());
			ritorno.setEsercizio_obbligazione_impegno(obbl.getEsercizio());
			ritorno.setDt_emissione_obbligazione_impegno(obbl.getDt_registrazione());
		}

		List mans = compenso.getDocContAssociati();
		
		Mandato_rigaIBulk manr=null;
		if (mans!=null && !mans.isEmpty()) {
			manr=(Mandato_rigaIBulk) mans.get(0);
			ritorno.setEsercizio_mandato(manr.getEsercizio());
			ritorno.setPg_mandato(manr.getPg_mandato());
			ritorno.setDt_emissione_mandato(manr.getMandato().getDt_emissione());
		}

		listaRitorno.add(ritorno);
	}
}
private SOAPFault faultChiaveFatturaNonCompleta() throws SOAPException{
	return generaFault("001","Identificativo Fattura non valido e/o incompleto");
}
private SOAPFault faultChiaveCompensoNonCompleta() throws SOAPException{
	return generaFault("004","Identificativo Compenso non valido e/o incompleto");
}
private SOAPFault faultIdTrovatoNullo() throws SOAPException{
	return generaFault("001","Identificativo Trovato non valorizzato");
}
private SOAPFault faultFatturaNonTrovata() throws SOAPException{
	return generaFault("002","Fattura non trovata");
}

private SOAPFault faultCompensoNonTrovato() throws SOAPException{
	return generaFault("003","Compenso non trovato");
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
                   

package it.cnr.contab.brevetti.ejb;

import it.cnr.contab.brevetti.client.Compenso;
import it.cnr.contab.brevetti.client.FatturaPassiva;
import it.cnr.contab.brevetti.client.FatturaPassivaBase;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService( name="FatturaPassivaTrovatiComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface FatturaPassivaTrovatiComponentSessionWS extends java.rmi.Remote{
	/**
	 * Il metodo restituisce la lista delle righe di fattura passiva selezionata in base ai parametri in ingresso,
	 * che indicano il CDS/UO che ha originato la fattura  (ESERCIZIO, CD_CDS_ORIGINE, CD_UO_ORIGINE, PG_FATTURA_ATTIVA)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS origine della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa di origine della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaPassiva> ricercaFatturaPassiva(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds_origine") String cds,
			 @WebParam (name="uo_origine") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce la lista delle righe di fattura passiva selezionata in base ai parametri in ingresso,
	 * che indicano la chiave della fattura (ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, PG_FATTURA_ATTIVA)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaPassiva> ricercaFatturaPassivaByKey(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce la lista di TUTTE le righe di fattura passiva se in una delle righe è presente il trovato
	 * richiesto come parametro in ingresso  
	 * 
	 * @param user	-	utente
	 * @param trovato	- progressivo del trovato ricercato
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaPassiva> ricercaFatturePassive(
			 @WebParam (name="user") String user,
			 @WebParam (name="trovato") Long trovato);			 
	/**
	 * Il metodo restituisce la lista delle sole testate di fattura passiva se in una delle righe è presente il trovato
	 * richiesto come parametro in ingresso
	 * 
	 * @param user	-	utente
	 * @param trovato	- progressivo del trovato ricercato
	 * @return	-	lista delle righe di fattura in versione BASE (ridotta)
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaPassivaBase> ricercaFatturePassiveBase(
			 @WebParam (name="user") String user,
			 @WebParam (name="trovato") Long trovato);			 

	/**
	 * Il metodo restituisce i dettagli del compenso selezionato in base ai parametri in ingresso,
	 * che indicano il CDS/UO che ha originato la fattura  (ESERCIZIO, CD_CDS_ORIGINE, CD_UO_ORIGINE, PG_COMPENSO)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS origine della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa di origine della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") Compenso ricercaCompenso(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds_origine") String cds,
			 @WebParam (name="uo_origine") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce i dettagli del compenso selezionato in base ai parametri in ingresso,
	 * che indicano la chiave della fattura (ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, PG_COMPENSO)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") Compenso ricercaCompensoByKey(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce la lista di TUTTi i compensi se in una delle righe è presente il trovato
	 * richiesto come parametro in ingresso  
	 * 
	 * @param user	-	utente
	 * @param trovato	- progressivo del trovato ricercato
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<Compenso> ricercaCompensi(
			 @WebParam (name="user") String user,
			 @WebParam (name="trovato") Long trovato);			 
}

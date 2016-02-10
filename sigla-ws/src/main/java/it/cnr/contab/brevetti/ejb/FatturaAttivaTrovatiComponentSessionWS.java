package it.cnr.contab.brevetti.ejb;

import it.cnr.contab.brevetti.client.FatturaAttiva;
import it.cnr.contab.brevetti.client.FatturaAttivaBase;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService( name="FatturaAttivaTrovatiComponentWS",targetNamespace="http://contab.cnr.it/sigla")
@Remote

public interface FatturaAttivaTrovatiComponentSessionWS extends java.rmi.Remote{
	/**
	 * Il metodo restituisce la lista delle righe di fattura attiva selezionata in base ai parametri in ingresso,
	 * che indicano il CDS/UO che ha originato la fattura  (ESERCIZIO, CD_CDS_ORIGINE, CD_UO_ORIGINE, PG_FATTURA_ATTIVA)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS origine della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa di origine della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaAttiva> ricercaFatturaAttiva(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds_origine") String cds,
			 @WebParam (name="uo_origine") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce la lista delle righe di fattura attiva selezionata in base ai parametri in ingresso,
	 * che indicano la chiave della fattura (ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, PG_FATTURA_ATTIVA)
	 * 
	 * @param user	-	utente
	 * @param esercizio	-	esercizio della fattura ricercata
	 * @param cds	-	codice CDS della fattura ricercata
	 * @param uo	-	codice Unità Organizzativa della fattura ricercata
	 * @param pg	-	numero progressivo della fattura ricercata
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaAttiva> ricercaFatturaAttivaByKey(
			 @WebParam (name="user") String user,
			 @WebParam (name="esercizio") Long esercizio,
			 @WebParam (name="cds") String cds,
			 @WebParam (name="uo") String uo,
			 @WebParam (name="pg") Long pg);			 
	/**
	 * Il metodo restituisce la lista di TUTTE le righe di fattura attiva se in una delle righe è presente il trovato
	 * richiesto come parametro in ingresso  
	 * 
	 * @param user	-	utente
	 * @param trovato	- progressivo del trovato ricercato
	 * @return	-	lista delle righe di fattura
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaAttiva> ricercaFattureAttive(
			 @WebParam (name="user") String user,
			 @WebParam (name="trovato") Long trovato);			 
	/**
	 * Il metodo restituisce la lista delle sole testate di fattura attiva se in una delle righe è presente il trovato
	 * richiesto come parametro in ingresso
	 * 
	 * @param user	-	utente
	 * @param trovato	- progressivo del trovato ricercato
	 * @return	-	lista delle righe di fattura in versione BASE (ridotta)
	 */
	@WebMethod @WebResult(targetNamespace="http://contab.cnr.it/sigla",name="result") java.util.ArrayList<FatturaAttivaBase> ricercaFattureAttiveBase(
			 @WebParam (name="user") String user,
			 @WebParam (name="trovato") Long trovato);			 
}

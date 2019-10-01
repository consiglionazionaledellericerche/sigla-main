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

package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (13/01/2003 12:14:26)
 * @author: Simonetta Costa
 */
public class Stampa_giornale_mandatiBulk extends MandatoBulk {
	
	private java.sql.Timestamp dataInizio;
	private java.sql.Timestamp dataFine;
	
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;

	private boolean isUOForPrintEnabled;

	private Boolean accreditamento= new Boolean(false);
	private Boolean mandato= new Boolean(false);
	private Boolean sospeso= new Boolean(false);
	private Boolean regolarizzazione= new Boolean(false);
	// Tipologie
	public final static String TIPO_TUTTI = "%";
	public final static java.util.Dictionary tipoMandatoKeys;

	// Stati
	public final static String STATO_MANDATO_TUTTI = "%";
	public final static java.util.Dictionary statoMandatoKeys;

	// Stati trasmissione
	public final static String STATO_TRASMISSIONE_TUTTI = "%";
	public final static java.util.Dictionary statoTrasmissioneKeys;

	static {
		tipoMandatoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoMandatoKeys.put(TIPO_ACCREDITAMENTO, "Accreditamento");
		tipoMandatoKeys.put(TIPO_PAGAMENTO, "Mandato di pagamento");
		tipoMandatoKeys.put(TIPO_REGOLARIZZAZIONE, "Regolarizzazione");
		tipoMandatoKeys.put(TIPO_REGOLAM_SOSPESO, "Sospeso");
		tipoMandatoKeys.put(TIPO_TUTTI, "Tutti");

		statoMandatoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoMandatoKeys.put(STATO_MANDATO_ANNULLATO, "Annullato");
		statoMandatoKeys.put(STATO_MANDATO_EMESSO, "Emesso");
		statoMandatoKeys.put(STATO_MANDATO_PAGATO, "Pagato");
		statoMandatoKeys.put(STATO_MANDATO_TUTTI, "Tutti");

		statoTrasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_NON_INSERITO, "Non inserito in distinta");
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_PREDISPOSTO,	"Predisposto alla Firma");
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_PRIMA_FIRMA,	"Prima Firma");		
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_INSERITO, "Inserito in distinta");
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_TRASMESSO, "Trasmesso");
		statoTrasmissioneKeys.put(STATO_TRASMISSIONE_TUTTI, "Tutti");
		statoTrasmissioneKeys.put("NT" , "Non tramessi");
	}
/**
 * Stampa_giornale_mandatiBulk constructor comment.
 */
public Stampa_giornale_mandatiBulk() {
	super();
}
/**
 * Stampa_giornale_mandatiBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_mandato java.lang.Long
 */
public Stampa_giornale_mandatiBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {

    if (super.getUnita_organizzativa() == null)
        return "%";
    if (super.getUnita_organizzativa().getCd_unita_organizzativa() == null)
        return "%";

    return super.getUnita_organizzativa().getCd_unita_organizzativa().toString();

}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOEmittenteForPrint() {
	
	return super.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (13/01/2003 12:54:35)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (13/01/2003 12:54:35)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.05.05)
 * @return java.lang.Long
 */
public java.lang.Long getPgFine() {
	return pgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.05.05)
 * @return java.lang.Long
 */
public java.lang.Long getPgInizio() {
	return pgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.53.37)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getStato_trasmissioneKeys() {
	return statoTrasmissioneKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.53.37)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getStatoKeys() {
	return statoMandatoKeys;
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>tipoMandatoKeys</code>
 * di tipo <code>Hashtable</code>.
 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori 
 * che può assumere il campo <code>ti_mandato</code>.
 * @return java.util.Dictionary tipoMandatoKeys I valori del campo <code>ti_mandato</code>.
 */
public java.util.Dictionary getTipoMandatoKeys() {
	return tipoMandatoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoEmittenteForPrint() {
	return super.getUnita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCdUOEmittenteForPrint() {
	return getUoEmittenteForPrint()==null || getUoEmittenteForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (08/04/2003 15.30.39)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return isUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (13/01/2003 12:54:35)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (13/01/2003 12:54:35)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (08/04/2003 15.30.39)
 * @param newIsUOForPrintEnabled boolean
 */
public void setIsUOForPrintEnabled(boolean newIsUOForPrintEnabled) {
	isUOForPrintEnabled = newIsUOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.05.05)
 * @param newPgFine java.lang.Long
 */
public void setPgFine(java.lang.Long newPgFine) {
	pgFine = newPgFine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/06/2003 11.05.05)
 * @param newPgInizio java.lang.Long
 */
public void setPgInizio(java.lang.Long newPgInizio) {
	pgInizio = newPgInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public void setUoEmittenteForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUO) {
	super.setUnita_organizzativa(newUO);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
	if(!accreditamento.booleanValue() && !mandato.booleanValue()&&
	   !regolarizzazione.booleanValue() && !sospeso.booleanValue()	)
		throw new it.cnr.jada.bulk.ValidationException("Specificare un tipo mandato!");
}
public Boolean getAccreditamento() {
	return accreditamento;
}
public void setAccreditamento(Boolean accreditamento) {
	this.accreditamento = accreditamento;
}
public Boolean getMandato() {
	return mandato;
}
public void setMandato(Boolean mandato) {
	this.mandato = mandato;
}
public Boolean getRegolarizzazione() {
	return regolarizzazione;
}
public void setRegolarizzazione(Boolean regolarizzazione) {
	this.regolarizzazione = regolarizzazione;
}
public Boolean getSospeso() {
	return sospeso;
}
public void setSospeso(Boolean sospeso) {
	this.sospeso = sospeso;
}
}

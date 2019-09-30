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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_ammortamentoBulk extends Tipo_ammortamentoBase {

	//private it.cnr.jada.bulk.SimpleBulkList ass_catBene_tiAmmort;
	private java.util.List catBeni;
	
	//private it.cnr.jada.bulk.SimpleBulkList ass_catBene_tiAmmortDisponibili;
	private java.util.List catBeniDisponibili;
	
	// TIPO AMMORTAMENTO usato per l'eventuale riassociazione
	private Tipo_ammortamentoBulk ammortamento_associato;

	// FL_ORDINARIO
	private Boolean fl_ordinario;
	// FL_ANTICIPATO
	private Boolean fl_anticipato;
	// FL_ALTRO
	private Boolean fl_altro;

	/* ID di transazione: è univoco e permette di identificare la fattura sulla
	 *	tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG (tabella di appoggio).
	*/ 
	private String local_transactionID;
	

	// PERC_PRIMO_ANNO ORDINARIO
	private java.math.BigDecimal perc_primo_anno_ord;
	// PERC_SUCCESSIVI ORDINARIO
	private java.math.BigDecimal perc_successivi_ord;

	// PERC_PRIMO_ANNO ANTICIPATO
	private java.math.BigDecimal perc_primo_anno_ant;
	// PERC_SUCCESSIVI ANTICIPATO
	private java.math.BigDecimal perc_successivi_ant;

	// PERC_PRIMO_ANNO ALTRO
	private java.math.BigDecimal perc_primo_anno_altro;
	// PERC_SUCCESSIVI ALTRO
	private java.math.BigDecimal perc_successivi_altro;
	
	// ESERCIZIO DI COMPETENZA
	private java.lang.Integer esercizio_competenza;

	// Flag che permette di sapere se è stata richietsa una operazione di RIASSOCIA
	private Boolean isPerRiassocia;
 	
	public final static java.lang.String TIPO_ORDINARIO = "O";
	public final static java.lang.String TIPO_ACCELERATO = "A";
	public final static java.lang.String TIPO_ALTRO = "L";
	private final static java.util.Dictionary tiAmmortamentoKeys;
	
	static
	{
		tiAmmortamentoKeys = new it.cnr.jada.util.OrderedHashtable();
		tiAmmortamentoKeys.put(TIPO_ORDINARIO, "Ordinario");
		tiAmmortamentoKeys.put(TIPO_ACCELERATO, "Accelerato");
		tiAmmortamentoKeys.put(TIPO_ALTRO, "Altro");
	}
public Tipo_ammortamentoBulk() {
	super();
}
public Tipo_ammortamentoBulk(java.lang.String cd_categoria_bene,java.lang.String cd_tipo_ammortamento) {
	super(cd_categoria_bene,cd_tipo_ammortamento);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 15.16.43)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk
 */
public Tipo_ammortamentoBulk getAmmortamento_associato() {
	return ammortamento_associato;
}
public BulkCollection[] getBulkLists() {

	// Metti solo le liste di oggetti che devono essere resi persistenti
	
	 //return new it.cnr.jada.bulk.BulkCollection[] { this.getCatBeni() };
	 return null;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 17.09.12)
 * @return java.util.List
 */
public java.util.List getCatBeni() {
	return catBeni;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 17.09.12)
 * @return java.util.List
 */
public java.util.List getCatBeniDisponibili() {
	return catBeniDisponibili;
}
/**
 * Restituisce una decodifica del Tipo di Ammortamento:
 *	O - Ordinario
 *	A - Anticipato
 *	L - Altro
 */
public java.lang.String getDs_tipo() {
	
	if (getTi_ammortamento()!=null){

		if (getTi_ammortamento().equals(TIPO_ORDINARIO))
			return "Ordinario";

		if (getTi_ammortamento().equals(TIPO_ACCELERATO))
			return "Accelerato";

		if (getTi_ammortamento().equals(TIPO_ALTRO))
			return "Altro";
	}

	return null;

}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 17.07.31)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio_competenza() {
	return esercizio_competenza;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_altro() {
	return fl_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_anticipato() {
	return fl_anticipato;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_ordinario() {
	return fl_ordinario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/05/2002 11.05.48)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getIsPerRiassocia() {
	return isPerRiassocia;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2002 11.55.36)
 * @return java.lang.String
 */
public java.lang.String getLocal_transactionID() {
	return local_transactionID;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_primo_anno_altro() {
	return perc_primo_anno_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_primo_anno_ant() {
	return perc_primo_anno_ant;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_primo_anno_ord() {
	return perc_primo_anno_ord;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_successivi_altro() {
	return perc_successivi_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_successivi_ant() {
	return perc_successivi_ant;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPerc_successivi_ord() {
	return perc_successivi_ord;
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 12.19.00)
 * @return java.util.Dictionary
 */
public final java.util.Dictionary getTiAmmortamentoKeys() {
	return tiAmmortamentoKeys;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>INSERT</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per l'inserimento di un OggettoBulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	
	setTi_ammortamento(TIPO_ORDINARIO);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isAltro() {

	if (getFl_altro()!= null)
		return getFl_altro().booleanValue();

	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isAnticipato() {

	if (getFl_anticipato()!= null)
		return getFl_anticipato().booleanValue();

	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isOrdinario() {

	if (getFl_ordinario()!= null)
		return getFl_ordinario().booleanValue();

	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (03/05/2002 11.03.51)
 * @return java.lang.Boolean
 */
public boolean isPerRiassocia() {
	if (getIsPerRiassocia()!=null)
		return isPerRiassocia.booleanValue();

	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROperc_primoAnno_altro() {
	
	if (getFl_altro() == null || !getFl_altro().booleanValue())
		return true;

	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROperc_primoAnno_ant() {
	
	if (getFl_anticipato() == null || !getFl_anticipato().booleanValue())
		return true;
		
	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROperc_primoAnno_ord() {

	if (getFl_ordinario() == null || !getFl_ordinario().booleanValue())
		return true;
		
	return false;
			
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 15.16.43)
 * @param newAmmortamento_associato it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk
 */
public void setAmmortamento_associato(Tipo_ammortamentoBulk newAmmortamento_associato) {
	ammortamento_associato = newAmmortamento_associato;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 17.09.12)
 * @param newCatBeni java.util.List
 */
public void setCatBeni(java.util.List newCatBeni) {
	catBeni = newCatBeni;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 17.09.12)
 * @param newCatBeniDisponibili java.util.List
 */
public void setCatBeniDisponibili(java.util.List newCatBeniDisponibili) {
	catBeniDisponibili = newCatBeniDisponibili;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 17.07.31)
 * @param newEsercizio_competenza java.lang.Integer
 */
public void setEsercizio_competenza(java.lang.Integer newEsercizio_competenza) {
	esercizio_competenza = newEsercizio_competenza;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @param newFl_altro java.lang.Boolean
 */
public void setFl_altro(java.lang.Boolean newFl_altro) {
	fl_altro = newFl_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @param newFl_anticipato java.lang.Boolean
 */
public void setFl_anticipato(java.lang.Boolean newFl_anticipato) {
	fl_anticipato = newFl_anticipato;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 14.46.16)
 * @param newFl_ordinario java.lang.Boolean
 */
public void setFl_ordinario(java.lang.Boolean newFl_ordinario) {
	fl_ordinario = newFl_ordinario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/05/2002 11.05.48)
 * @param newIsPerRiassocia java.lang.Boolean
 */
public void setIsPerRiassocia(java.lang.Boolean newIsPerRiassocia) {
	isPerRiassocia = newIsPerRiassocia;
}
/**
 * Insert the method's description here.
 * Creation date: (21/03/2002 11.55.36)
 * @param newLocal_transactionID java.lang.String
 */
public void setLocal_transactionID(java.lang.String newLocal_transactionID) {
	local_transactionID = newLocal_transactionID;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_primo_anno_altro java.math.BigDecimal
 */
public void setPerc_primo_anno_altro(java.math.BigDecimal newPerc_primo_anno_altro) {
	perc_primo_anno_altro = newPerc_primo_anno_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_primo_anno_ant java.math.BigDecimal
 */
public void setPerc_primo_anno_ant(java.math.BigDecimal newPerc_primo_anno_ant) {
	perc_primo_anno_ant = newPerc_primo_anno_ant;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_primo_anno_ord java.math.BigDecimal
 */
public void setPerc_primo_anno_ord(java.math.BigDecimal newPerc_primo_anno_ord) {
	perc_primo_anno_ord = newPerc_primo_anno_ord;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_successivi_altro java.math.BigDecimal
 */
public void setPerc_successivi_altro(java.math.BigDecimal newPerc_successivi_altro) {
	perc_successivi_altro = newPerc_successivi_altro;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_successivi_ant java.math.BigDecimal
 */
public void setPerc_successivi_ant(java.math.BigDecimal newPerc_successivi_ant) {
	perc_successivi_ant = newPerc_successivi_ant;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 9.20.51)
 * @param newPerc_successivi_ord java.math.BigDecimal
 */
public void setPerc_successivi_ord(java.math.BigDecimal newPerc_successivi_ord) {
	perc_successivi_ord = newPerc_successivi_ord;
}
}

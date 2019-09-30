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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Trattamento_coriBulk extends Trattamento_coriBase {

	private Tipo_trattamentoBulk tipoTrattamento = new Tipo_trattamentoBulk();
	private Tipo_contributo_ritenutaBulk tipoContributoRitenuta = new Tipo_contributo_ritenutaBulk();

	private final static java.util.Collection SEGNI;
	private final static String MENO = "-";
	private final static String PIU = "+";

	static{
		SEGNI = new java.util.Vector();
		SEGNI.add(MENO);
		SEGNI.add(PIU);
	}

	private it.cnr.jada.bulk.BulkList righe = new it.cnr.jada.bulk.BulkList();
	
public Trattamento_coriBulk() {
	super();
}
public Trattamento_coriBulk(java.lang.String cd_contributo_ritenuta,java.lang.String cd_trattamento,java.sql.Timestamp dt_inizio_validita) {
	super(cd_contributo_ritenuta,cd_trattamento,dt_inizio_validita);
	tipoContributoRitenuta = new Tipo_contributo_ritenutaBulk(cd_contributo_ritenuta,null);
	tipoTrattamento = new Tipo_trattamentoBulk(cd_trattamento,null);
}
public java.lang.String getCd_contributo_ritenuta() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk tipoContributoRitenuta = this.getTipoContributoRitenuta();
	if (tipoContributoRitenuta == null)
		return null;
	return tipoContributoRitenuta.getCd_contributo_ritenuta();
}
public java.lang.String getCd_trattamento() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento = this.getTipoTrattamento();
	if (tipoTrattamento == null)
		return null;
	return tipoTrattamento.getCd_trattamento();
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.00.45)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 14.44.42)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getRighe() {
	return righe;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 10.52.21)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public java.util.Collection getSegni() {
	return SEGNI;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 11.39.06)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk
 */
public Tipo_contributo_ritenutaBulk getTipoContributoRitenuta() {
	return tipoContributoRitenuta;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 10.52.21)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public Tipo_trattamentoBulk getTipoTrattamento() {
	return tipoTrattamento;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);
	setSegno(MENO);
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.01.09)
 * @param newData java.sql.Timestamp
 */
public boolean isROContributoRintenuta() {

	return (getTipoContributoRitenuta() == null || getTipoContributoRitenuta().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.01.09)
 * @param newData java.sql.Timestamp
 */
public boolean isROTipoTrattamento() {

	return (getTipoTrattamento() == null || getTipoTrattamento().getCrudStatus() == OggettoBulk.NORMAL);
}
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.getTipoContributoRitenuta().setCd_contributo_ritenuta(cd_contributo_ritenuta);
}
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.getTipoTrattamento().setCd_trattamento(cd_trattamento);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 17.01.09)
 * @param newData java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newData) {

	this.setDt_fine_validita(newData);
}
/**
 * Insert the method's description here.
 * Creation date: (15/03/2002 14.44.42)
 * @param newRighe it.cnr.jada.bulk.BulkList
 */
public void setRighe(it.cnr.jada.bulk.BulkList newRighe) {
	righe = newRighe;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 11.39.06)
 * @param newTipoContributoRitenuta it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk
 */
public void setTipoContributoRitenuta(Tipo_contributo_ritenutaBulk newTipoContributoRitenuta) {
	tipoContributoRitenuta = newTipoContributoRitenuta;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 10.52.21)
 * @param newTipoTrattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public void setTipoTrattamento(Tipo_trattamentoBulk newTipoTrattamento) {
	tipoTrattamento = newTipoTrattamento;
}
public void validaDate() throws it.cnr.jada.comp.ApplicationException{

	if (getDt_inizio_validita()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo DATA INIZIO VALIDITA non può essere vuoto");
		
	if (getDt_fine_validita()==null)
		setDt_fine_validita(EsercizioHome.DATA_INFINITO);

	if (getDt_inizio_validita().compareTo(getDt_fine_validita()) > 0)
		throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non può essere superiore alla Data Fine Validita");
}
public void validaTrattamento() throws it.cnr.jada.comp.ApplicationException{

	if (getCd_trattamento()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo TRATTAMENTO non può essere vuoto");
	if (getCd_contributo_ritenuta()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo CODICE CO/RI non può essere vuoto");
	if (getId_riga()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo ID RIGA non può essere vuoto");
	if (getSegno()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo SEGNO non può essere vuoto");
	if (getCalcolo_imponibile()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo ALGORITMO non può essere vuoto");

	validaDate();
}
}

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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class Sospeso_det_etrBulk extends Sospeso_det_etrBase {

	protected ReversaleBulk reversale = new ReversaleBulk();
	protected SospesoBulk sospeso = new SospesoBulk();

	public final static String TI_SOSPESO   = "S";
	public final static String TI_RISCONTRO = "R";

	public final static Dictionary ti_sospeso_riscontroKeys;
	static 
	{
		ti_sospeso_riscontroKeys = new Hashtable();
		ti_sospeso_riscontroKeys.put(TI_SOSPESO,	"Sospeso");
		ti_sospeso_riscontroKeys.put(TI_RISCONTRO,	"Riscontro");
	};

	public final static String STATO_DEFAULT	= "N";
	public final static String STATO_ANNULLATO	= "A";

	public final static Dictionary statoKeys;
	static 
	{
		statoKeys = new Hashtable();
		statoKeys.put(STATO_DEFAULT,	"Default");
		statoKeys.put(STATO_ANNULLATO,	"Annullato");
	};
public Sospeso_det_etrBulk() {
	super();
}
public Sospeso_det_etrBulk(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.Long pg_reversale,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super(cd_cds,cd_sospeso,esercizio,pg_reversale,ti_entrata_spesa,ti_sospeso_riscontro);
	setSospeso(new it.cnr.contab.doccont00.core.bulk.SospesoBulk(cd_cds,cd_sospeso,esercizio,ti_entrata_spesa,ti_sospeso_riscontro));
}
/**
 * Restituisce un array di <code>OggettoBulk</code> da rendere persistenti 
 * insieme al ricevente.
 * L'implementazione standard restituisce <code>null</code>.
 * @see it.cnr.jada.comp.GenericComponent#makeBulkPersistent
 */ 
public OggettoBulk[] getBulksForPersistentcy() {
	 return new OggettoBulk[] { 
			sospeso };

}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = this.getSospeso();
	if (sospeso == null)
		return null;
	return sospeso.getCd_cds();
}
public java.lang.String getCd_sospeso() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = this.getSospeso();
	if (sospeso == null)
		return null;
	return sospeso.getCd_sospeso();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = this.getSospeso();
	if (sospeso == null)
		return null;
	return sospeso.getEsercizio();
}
public java.lang.Long getPg_reversale() {
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
	if (reversale == null)
		return null;
	return reversale.getPg_reversale();
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public ReversaleBulk getReversale() {
	return reversale;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public SospesoBulk getSospeso() {
	return sospeso;
}
public java.lang.String getTi_entrata_spesa() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = this.getSospeso();
	if (sospeso == null)
		return null;
	return sospeso.getTi_entrata_spesa();
}
public java.lang.String getTi_sospeso_riscontro() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso = this.getSospeso();
	if (sospeso == null)
		return null;
	return sospeso.getTi_sospeso_riscontro();
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getSospeso().setCd_cds(cd_cds);
}
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.getSospeso().setCd_sospeso(cd_sospeso);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getSospeso().setEsercizio(esercizio);
}
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.getReversale().setPg_reversale(pg_reversale);
}
/**
 * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public void setReversale(ReversaleBulk newReversale) {
	reversale = newReversale;
}
/**
 * @param newSospeso it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public void setSospeso(SospesoBulk newSospeso) {
	sospeso = newSospeso;
}
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.getSospeso().setTi_entrata_spesa(ti_entrata_spesa);
}
public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
	this.getSospeso().setTi_sospeso_riscontro(ti_sospeso_riscontro);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();

	// controllo su campo IMPORTO SOSPESO
	if ( getIm_associato() == null  )
		throw new ValidationException( "Il campo IMPORTO del Sospeso non può essere nullo." );
	if ( getIm_associato().compareTo( new java.math.BigDecimal(0)) <= 0 )
		throw new ValidationException( "Il campo IMPORTO del Sospeso deve essere maggiore di zero." );
	if ( getIm_associato().compareTo( getReversale().getIm_reversale()) > 0 )
		throw new ValidationException( "Il campo IMPORTO del Sospeso deve essere inferiore o uguale all'importo della reversale." );
}
public java.lang.String getCd_cds_reversale() {
	
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
	if (reversale == null)
		return null;
	return reversale.getCd_cds();
}
public void setCd_cds_reversale(java.lang.String cd_cds_reversale) {
	this.getReversale().setCd_cds(cd_cds_reversale);
}
}

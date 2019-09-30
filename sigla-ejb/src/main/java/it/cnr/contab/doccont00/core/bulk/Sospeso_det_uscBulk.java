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

public class Sospeso_det_uscBulk extends Sospeso_det_uscBase {
	protected MandatoBulk mandato = new MandatoBulk();
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
public Sospeso_det_uscBulk() {
	super();
}
public Sospeso_det_uscBulk(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.Long pg_mandato,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super(cd_cds,cd_sospeso,esercizio,pg_mandato,ti_entrata_spesa,ti_sospeso_riscontro);
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
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoBulk getMandato() {
	return mandato;
}
public java.lang.Long getPg_mandato() {
	it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	return mandato.getPg_mandato();
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
	this.getSospeso().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.getSospeso().setCd_sospeso(cd_sospeso);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getSospeso().setEsercizio(esercizio);
}
/**
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoBulk newMandato) {
	mandato = newMandato;
}
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.getMandato().setPg_mandato(pg_mandato);
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
	if ( getIm_associato().compareTo( getMandato().getIm_mandato()) > 0 )
		throw new ValidationException( "Il campo IMPORTO del Sospeso deve essere inferiore o uguale all'importo del mandato." );
}
public java.lang.String getCd_cds_mandato() {
	
	it.cnr.contab.doccont00.core.bulk.MandatoBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	return mandato.getCd_cds();
}
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.getMandato().setCd_cds(cd_cds_mandato);
}
}

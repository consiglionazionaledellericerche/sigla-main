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

package it.cnr.contab.doccont00.intcass.bulk;

import java.math.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiere_detBulk extends Distinta_cassiere_detBase {
	private Distinta_cassiereBulk distinta;
	public final static java.util.Dictionary cd_tipo_documento_contKeys;
	static 
	{
		cd_tipo_documento_contKeys = new java.util.Hashtable();
		cd_tipo_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN,	"Mandato");
		cd_tipo_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV,	"Reversale");				
	}
	
	public final static java.util.Dictionary ti_documento_contKeys;
	static 
	{
		ti_documento_contKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.TIPO_PAGAMENTO,				"Pagamento");
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.TIPO_ACCREDITAMENTO,		"Accreditamento");						
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_REGOLAM_SOSPESO,		"Regolamento Sospeso");		
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_REGOLARIZZAZIONE,	"Regolarizzazione");
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_TRASFERIMENTO, 		"Trasferimento");		
	}
	
	public final static java.util.Dictionary statoKeys;
	static 
	{
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_ANNULLATO,	"Annullato");
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_EMESSO,		"Emesso");
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_PAGATO,		"Pagato/Incassato");										
	}
	public final static java.util.Dictionary stato_trasmissioneKeys;
	static 
	{
		stato_trasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO,	"Non inserito in distinta");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_INSERITO,		"Inserito in distinta");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_TRASMESSO,		"Trasmesso");
	}

public Distinta_cassiere_detBulk() {
	super();
}
public Distinta_cassiere_detBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_distinta) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_dettaglio,pg_distinta);
	setDistinta(new it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk(cd_cds, cd_unita_organizzativa,esercizio,pg_distinta));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk distinta = this.getDistinta();
	if (distinta == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = distinta.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk distinta = this.getDistinta();
	if (distinta == null)
		return null;
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = distinta.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * @return it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk
 */
public Distinta_cassiereBulk getDistinta() {
	return distinta;
}
public Integer getEsercizio() {
	it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk distinta = this.getDistinta();
	if (distinta == null)
		return null;
	return distinta.getEsercizio();
}
public java.lang.Long getPg_distinta() {
	it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk distinta = this.getDistinta();
	if (distinta == null)
		return null;
	return distinta.getPg_distinta();
}
public void insertingUsing(Persister persister) 
{
	/*
	if ( isMandato() )
		setPg_mandato( docContabile.getPg_documento_cont());
	if ( isReversale() )
		setPg_reversale( docContabile.getPg_documento_cont());
	*/	
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getDistinta().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getDistinta().getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * @param newDistinta it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk
 */
public void setDistinta(Distinta_cassiereBulk newDistinta) {
	distinta = newDistinta;
}
public void setEsercizio(Integer esercizio) {
	this.getDistinta().setEsercizio(esercizio);
}
public void setPg_distinta(java.lang.Long pg_distinta) {
	this.getDistinta().setPg_distinta(pg_distinta);
}
}

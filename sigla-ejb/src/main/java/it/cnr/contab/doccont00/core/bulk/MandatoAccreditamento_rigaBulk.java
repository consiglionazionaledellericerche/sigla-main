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

import java.sql.Timestamp;

public class MandatoAccreditamento_rigaBulk extends Mandato_rigaBulk {
	protected MandatoAccreditamentoBulk mandatoAccreditamento;
	protected V_impegnoBulk impegno = new V_impegnoBulk();
public MandatoAccreditamento_rigaBulk() {
	super();
}
public MandatoAccreditamento_rigaBulk(java.lang.String cd_cds,java.lang.String cd_cds_doc_amm,java.lang.String cd_tipo_documento_amm,java.lang.String cd_uo_doc_amm,java.lang.Integer esercizio,java.lang.Integer esercizio_doc_amm,java.lang.Integer esercizio_obbligazione,java.lang.Long pg_doc_amm,java.lang.Long pg_mandato,java.lang.Integer esercizio_ori_obbligazione,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) 
{
	super(cd_cds, cd_cds_doc_amm, cd_tipo_documento_amm, cd_uo_doc_amm, esercizio, esercizio_doc_amm, esercizio_obbligazione, pg_doc_amm, pg_mandato, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario);
	setImpegno(new V_impegnoBulk(cd_cds,esercizio_obbligazione,esercizio_ori_obbligazione,pg_obbligazione,pg_obbligazione_scadenzario));		
}
public java.lang.Integer getEsercizio_obbligazione() {
	V_impegnoBulk impegno = this.getImpegno();
	if (impegno == null)
		return null;
	return impegno.getEsercizio();
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.Impegno_scad_voceBulk
 */
public V_impegnoBulk getImpegno() {
	return impegno;
}
public MandatoBulk getMandato() 
{
	return mandatoAccreditamento;
}	
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoAccreditamentoBulk getMandatoAccreditamento() {
	return mandatoAccreditamento;
}
public Integer getEsercizio_ori_obbligazione() {
	V_impegnoBulk impegno = this.getImpegno();
	if (impegno == null)
		return null;
	return impegno.getEsercizio_originale();
}
public java.lang.Long getPg_obbligazione() {
	V_impegnoBulk impegno = this.getImpegno();
	if (impegno == null)
		return null;
	return impegno.getPg_obbligazione();
}
public java.lang.Long getPg_obbligazione_scadenzario() {
	V_impegnoBulk impegno = this.getImpegno();
	if (impegno == null)
		return null;
	return impegno.getPg_obbligazione_scadenzario();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOFindVoce'
 *
 * @return Il valore della proprietà 'rOFindVoce'
 */
public boolean isROFindVoce() 
{
	/*
	if ( impegno == null  )
		return true;
	if ( getCrudStatus() != UNDEFINED && getCrudStatus() != TO_BE_CREATED )
		return true;
	return false;
	*/
	return true;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOVoce'
 *
 * @return Il valore della proprietà 'rOVoce'
 */
public boolean isROVoce() 
{
	/*
	if ( impegno == null  )
		return true;
	if ( getCrudStatus() != UNDEFINED && getCrudStatus() != TO_BE_CREATED )
		return true;
	if ( impegno.getCrudStatus() == NORMAL )
		return true;
	return false;
	*/
	return true;
}
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.getImpegno().setEsercizio(esercizio_obbligazione);
}
/**
 * @param newImpegno it.cnr.contab.doccont00.core.bulk.Impegno_scad_voceBulk
 */
public void setImpegno(V_impegnoBulk newImpegno) {
	impegno = newImpegno;
}
public void setMandato(MandatoBulk newMandato)
{
	setMandatoAccreditamento( (MandatoAccreditamentoBulk) newMandato);
}	
/**
 * @param newMandatoAccreditamento it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public void setMandatoAccreditamento(MandatoAccreditamentoBulk newMandatoAccreditamento) {
	mandatoAccreditamento = newMandatoAccreditamento;
}
public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
	this.getImpegno().setEsercizio_originale(esercizio_ori_obbligazione);
}
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.getImpegno().setPg_obbligazione(pg_obbligazione);
}
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.getImpegno().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
/*	simona 2/8 : non e' possibile modificare l'importo o l'impegno di una riga di mandato
	if ( impegno.getCrudStatus() != NORMAL )
		throw new ValidationException( "E' necessario effettuare una ricerca dell'impegno." );
	if ( getIm_mandato_riga() == null )
		throw new ValidationException( "E' necessario specificare un importo per ogni riga del mandato!" );
	if ( getIm_mandato_riga().compareTo( new java.math.BigDecimal(0) ) < 0 )
		throw new ValidationException( "E' necessario specificare un importo maggiore di zero per ogni riga del mandato!" );	
	if ( impegno.getIm_disponibile().compareTo( getIm_mandato_riga() ) < 0 )
			throw new ValidationException( "Voce " + impegno.getCd_voce() + ": non e' possibile specificare un importo superiore all'importo disponibile!" );
*/			
	
		

}
}

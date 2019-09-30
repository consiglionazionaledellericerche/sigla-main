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

/*
* Creted by Generator 1.0
* Date 19/04/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_obbl_acc_contrattiBulk extends OggettoBulk implements Persistent {
//    TIPO CHAR(3)
	private java.lang.String tipo;
 
//    ESERCIZIO_COMPETENZA DECIMAL(4,0)
	private java.lang.Integer esercizio_competenza;

//	cd_unita_organizzativa VARCHAR2(30)
  private java.lang.String cd_unita_organizzativa;
 
//    PG_DOC DECIMAL(10,0)
	private java.lang.Long pg_doc;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    DS_DOC VARCHAR(300)
	private java.lang.String ds_doc;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(400)
	private java.lang.String ds_commessa;
 
//    ESERCIZIO_CONTRATTO_PADRE DECIMAL(4,0)
	private java.lang.Integer esercizio_contratto_padre;
	
//	STATO_CONTRATTO_PADRE VARCHAR(1)
  	private java.lang.String stato_contratto_padre;
 
//    PG_CONTRATTO_PADRE DECIMAL(10,0)
	private java.lang.Long pg_contratto_padre;
 
//    ESERCIZIO_CONTRATTO DECIMAL(4,0)
	private java.lang.Integer esercizio_contratto;

//	STATO_CONTRATTO VARCHAR(1)
	private java.lang.String stato_contratto;
 
//    PG_CONTRATTO DECIMAL(10,0)
	private java.lang.Long pg_contratto;
 
//    OGGETTO_CONTRATTO VARCHAR(500)
	private java.lang.String oggetto_contratto;
 
//    IM_ACCERTAMENTO DECIMAL(15,2)
	private java.math.BigDecimal im_accertamento;
 
//    IM_OBBLIGAZIONE DECIMAL(15,2)
	private java.math.BigDecimal im_obbligazione;
 
	public V_cons_obbl_acc_contrattiBulk() {
		super();
	}
	public java.lang.String getTipo () {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.Integer getEsercizio_competenza () {
		return esercizio_competenza;
	}
	public void setEsercizio_competenza(java.lang.Integer esercizio_competenza)  {
		this.esercizio_competenza=esercizio_competenza;
	}
	public java.lang.Long getPg_doc () {
		return pg_doc;
	}
	public void setPg_doc(java.lang.Long pg_doc)  {
		this.pg_doc=pg_doc;
	}
	public java.sql.Timestamp getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.String getDs_doc () {
		return ds_doc;
	}
	public void setDs_doc(java.lang.String ds_doc)  {
		this.ds_doc=ds_doc;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.Integer getEsercizio_contratto_padre () {
		return esercizio_contratto_padre;
	}
	public void setEsercizio_contratto_padre(java.lang.Integer esercizio_contratto_padre)  {
		this.esercizio_contratto_padre=esercizio_contratto_padre;
	}
	public java.lang.Long getPg_contratto_padre () {
		return pg_contratto_padre;
	}
	public void setPg_contratto_padre(java.lang.Long pg_contratto_padre)  {
		this.pg_contratto_padre=pg_contratto_padre;
	}
	public java.lang.Integer getEsercizio_contratto () {
		return esercizio_contratto;
	}
	public void setEsercizio_contratto(java.lang.Integer esercizio_contratto)  {
		this.esercizio_contratto=esercizio_contratto;
	}
	public java.lang.Long getPg_contratto () {
		return pg_contratto;
	}
	public void setPg_contratto(java.lang.Long pg_contratto)  {
		this.pg_contratto=pg_contratto;
	}
	public java.lang.String getOggetto_contratto () {
		return oggetto_contratto;
	}
	public void setOggetto_contratto(java.lang.String oggetto_contratto)  {
		this.oggetto_contratto=oggetto_contratto;
	}
	public java.math.BigDecimal getIm_accertamento () {
		return im_accertamento;
	}
	public void setIm_accertamento(java.math.BigDecimal im_accertamento)  {
		this.im_accertamento=im_accertamento;
	}
	public java.math.BigDecimal getIm_obbligazione () {
		return im_obbligazione;
	}
	public void setIm_obbligazione(java.math.BigDecimal im_obbligazione)  {
		this.im_obbligazione=im_obbligazione;
	}
	/**
	 * @return
	 */
	public java.lang.String getStato_contratto() {
		return stato_contratto;
	}
	
	/**
	 * @return
	 */
	public java.lang.String getStato_contratto_padre() {
		return stato_contratto_padre;
	}
	
	/**
	 * @param string
	 */
	public void setStato_contratto(java.lang.String string) {
		stato_contratto = string;
	}
	
	/**
	 * @param string
	 */
	public void setStato_contratto_padre(java.lang.String string) {
		stato_contratto_padre = string;
	}

/**
 * @return
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}

/**
 * @param string
 */
public void setCd_unita_organizzativa(java.lang.String string) {
	cd_unita_organizzativa = string;
}

}
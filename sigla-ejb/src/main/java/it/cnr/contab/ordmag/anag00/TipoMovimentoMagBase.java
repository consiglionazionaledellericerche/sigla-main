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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class TipoMovimentoMagBase extends TipoMovimentoMagKey implements Keyed {
//    DS_TIPO_MOVIMENTO VARCHAR(100) NOT NULL
	private java.lang.String dsTipoMovimento;
 
//    TIPO VARCHAR(2) NOT NULL
	private java.lang.String tipo;
 
//    SEGNO CHAR(1)
	private java.lang.String segno;
 
//    CD_CDS_STORNO VARCHAR(30)
	private java.lang.String cdCdsStorno;
 
//    CD_TIPO_MOVIMENTO_STORNO VARCHAR(3)
	private java.lang.String cdTipoMovimentoStorno;
 
//    CD_CDS_ALT VARCHAR(30)
	private java.lang.String cdCdsAlt;
 
//    CD_TIPO_MOVIMENTO_ALT VARCHAR(3)
	private java.lang.String cdTipoMovimentoAlt;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG
	 **/
	public TipoMovimentoMagBase() {
		super();
	}
	public TipoMovimentoMagBase(java.lang.String cdCds, java.lang.String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoMovimento]
	 **/
	public java.lang.String getDsTipoMovimento() {
		return dsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoMovimento]
	 **/
	public void setDsTipoMovimento(java.lang.String dsTipoMovimento)  {
		this.dsTipoMovimento=dsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public java.lang.String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [segno]
	 **/
	public java.lang.String getSegno() {
		return segno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [segno]
	 **/
	public void setSegno(java.lang.String segno)  {
		this.segno=segno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsStorno]
	 **/
	public java.lang.String getCdCdsStorno() {
		return cdCdsStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsStorno]
	 **/
	public void setCdCdsStorno(java.lang.String cdCdsStorno)  {
		this.cdCdsStorno=cdCdsStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoStorno]
	 **/
	public java.lang.String getCdTipoMovimentoStorno() {
		return cdTipoMovimentoStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoStorno]
	 **/
	public void setCdTipoMovimentoStorno(java.lang.String cdTipoMovimentoStorno)  {
		this.cdTipoMovimentoStorno=cdTipoMovimentoStorno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsAlt]
	 **/
	public java.lang.String getCdCdsAlt() {
		return cdCdsAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsAlt]
	 **/
	public void setCdCdsAlt(java.lang.String cdCdsAlt)  {
		this.cdCdsAlt=cdCdsAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoAlt]
	 **/
	public java.lang.String getCdTipoMovimentoAlt() {
		return cdTipoMovimentoAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoAlt]
	 **/
	public void setCdTipoMovimentoAlt(java.lang.String cdTipoMovimentoAlt)  {
		this.cdTipoMovimentoAlt=cdTipoMovimentoAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}
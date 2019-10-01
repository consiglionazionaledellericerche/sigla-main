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
 * Date 28/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;
public class TipoMovimentoMagAzBulk extends TipoMovimentoMagAzBase {
	public final static String AZIONE_AZZERA = "0";
	public final static String AZIONE_SOSTITUISCE = "S";
	public final static String AZIONE_SOTTRAE = "-";
	public final static String AZIONE_SOMMA = "+";
	
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [TIPO_MOVIMENTO_MAG Anagrafica delle Tipologie dei Movimenti.]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMagRif =  new TipoMovimentoMagBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG_AZ
	 **/
	public TipoMovimentoMagAzBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG_AZ
	 **/
	public TipoMovimentoMagAzBulk(java.lang.String cdCds, java.lang.String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public TipoMovimentoMagBulk getTipoMovimentoMagRif() {
		return tipoMovimentoMagRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public void setTipoMovimentoMagRif(TipoMovimentoMagBulk tipoMovimentoMagRif)  {
		this.tipoMovimentoMagRif=tipoMovimentoMagRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRif]
	 **/
	public java.lang.String getCdCdsRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRif]
	 **/
	public void setCdCdsRif(java.lang.String cdCdsRif)  {
		this.getTipoMovimentoMagRif().setCdCds(cdCdsRif);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRif]
	 **/
	public java.lang.String getCdTipoMovimentoRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRif]
	 **/
	public void setCdTipoMovimentoRif(java.lang.String cdTipoMovimentoRif)  {
		this.getTipoMovimentoMagRif().setCdTipoMovimento(cdTipoMovimentoRif);
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}
}
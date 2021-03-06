/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public abstract class MovimentiMagazzinoBulk extends AbilitazioneMagazzinoBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private MagazzinoBulk magazzinoAbilitato =  new MagazzinoBulk();
	private UnitaOperativaOrdBulk unitaOperativaAbilitata = new UnitaOperativaOrdBulk();
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	private java.sql.Timestamp dataCompetenza; 
	private java.sql.Timestamp dataMovimento;
	
	public MagazzinoBulk getMagazzinoAbilitato() {
		return magazzinoAbilitato;
	}
	
	public void setMagazzinoAbilitato(MagazzinoBulk magazzinoAbilitato) {
		this.magazzinoAbilitato = magazzinoAbilitato;
	}
	
	public UnitaOperativaOrdBulk getUnitaOperativaAbilitata() {
		return unitaOperativaAbilitata;
	}
	
	public void setUnitaOperativaAbilitata(UnitaOperativaOrdBulk unitaOperativaAbilitata) {
		this.unitaOperativaAbilitata = unitaOperativaAbilitata;
	}
	
	public TipoMovimentoMagBulk getTipoMovimentoMag() {
		return tipoMovimentoMag;
	}
	
	public void setTipoMovimentoMag(TipoMovimentoMagBulk tipoMovimentoMag) {
		this.tipoMovimentoMag = tipoMovimentoMag;
	}
	
	public java.sql.Timestamp getDataCompetenza() {
		return dataCompetenza;
	}

	public void setDataCompetenza(java.sql.Timestamp dataCompetenza) {
		this.dataCompetenza = dataCompetenza;
	}
	
	public java.sql.Timestamp getDataMovimento() {
		return dataMovimento;
	}
	
	public void setDataMovimento(java.sql.Timestamp dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	
}
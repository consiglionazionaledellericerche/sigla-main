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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.util.DateUtils;

public class ScaricoMagazzinoBulk extends OggettoBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private MagazzinoBulk magazzinoAbilitato =  new MagazzinoBulk();
	private UnitaOperativaOrdBulk unitaOperativaAbilitata = new UnitaOperativaOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaRicevente = new UnitaOperativaOrdBulk();
	private TipoMovimentoMagBulk tipoMovimentoMag =  new TipoMovimentoMagBulk();
	private java.sql.Timestamp dataCompetenza; 
	private java.sql.Timestamp dataMovimento;
	
	private List<ScaricoMagazzinoRigaBulk> scaricoMagazzinoRigaColl = new BulkList<>();
	private List<BollaScaricoMagBulk> bolleScaricoList;

	public ScaricoMagazzinoBulk() {
		super();
	}

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
	
	public UnitaOperativaOrdBulk getUnitaOperativaRicevente() {
		return unitaOperativaRicevente;
	}
	
	public void setUnitaOperativaRicevente(UnitaOperativaOrdBulk unitaOperativaRicevente) {
		this.unitaOperativaRicevente = unitaOperativaRicevente;
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

	public int addToScaricoMagazzinoRigaColl( ScaricoMagazzinoRigaBulk bulk ) {
		bulk.setUnitaOperativaRicevente(this.getUnitaOperativaRicevente());
		bulk.setScaricoMagazzino(this);
		scaricoMagazzinoRigaColl.add(bulk);
		return scaricoMagazzinoRigaColl.size()-1;
	}

	public ScaricoMagazzinoRigaBulk removeFromScaricoMagazzinoRigaColl(int index) {
		return (ScaricoMagazzinoRigaBulk)scaricoMagazzinoRigaColl.remove(index);
	}
	
	public List<ScaricoMagazzinoRigaBulk> getScaricoMagazzinoRigaColl() {
		return scaricoMagazzinoRigaColl;
	}
	
	public void setScaricoMagazzinoRigaColl(List<ScaricoMagazzinoRigaBulk> scaricoMagazzinoRigaColl) {
		this.scaricoMagazzinoRigaColl = scaricoMagazzinoRigaColl;
	}
	
	public List<BollaScaricoMagBulk> getBolleScaricoColl() {
		return bolleScaricoList;
	}
	
	public void setBolleScaricoColl(List<BollaScaricoMagBulk> bolleScaricoColl) {
		this.bolleScaricoList = bolleScaricoColl;
	}
	
	public boolean isROTestata(){
		return Optional.ofNullable(this.getScaricoMagazzinoRigaColl())
				.map(e->!e.isEmpty())
				.orElse(Boolean.FALSE);
	}

	public void validaDate() throws ValidationException {
		if (this.getDataCompetenza()!=null && 
				DateUtils.truncate(this.getDataCompetenza()).after(DateUtils.truncate(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))) 
			throw new ValidationException( "La \"Data di competenza\" dello scarico non deve essere successiva alla data odierna!");
	}
}
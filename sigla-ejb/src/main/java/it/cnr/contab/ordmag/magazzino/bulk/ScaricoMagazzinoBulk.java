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

public class ScaricoMagazzinoBulk extends MovimentiMagazzinoBulk{
	private static final long serialVersionUID = 1L;

	private UnitaOperativaOrdBulk unitaOperativaRicevente = new UnitaOperativaOrdBulk();
	
	private List<BollaScaricoMagBulk> bolleScaricoList;

	private List<ScaricoMagazzinoRigaBulk> scaricoMagazzinoRigaColl = new BulkList<>();

	public ScaricoMagazzinoBulk() {
		super();
	}

	public UnitaOperativaOrdBulk getUnitaOperativaRicevente() {
		return unitaOperativaRicevente;
	}
	
	public void setUnitaOperativaRicevente(UnitaOperativaOrdBulk unitaOperativaRicevente) {
		this.unitaOperativaRicevente = unitaOperativaRicevente;
	}
	
	public int addToScaricoMagazzinoRigaColl( ScaricoMagazzinoRigaBulk bulk ) {
		bulk.setUnitaOperativaRicevente(this.getUnitaOperativaRicevente());
		bulk.setMovimentiMagazzinoBulk(this);
		getScaricoMagazzinoRigaColl().add(bulk);
		return getScaricoMagazzinoRigaColl().size()-1;
	}

	public ScaricoMagazzinoRigaBulk removeFromScaricoMagazzinoRigaColl(int index) {
		return (ScaricoMagazzinoRigaBulk)getScaricoMagazzinoRigaColl().remove(index);
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

	public List<BollaScaricoMagBulk> getBolleScaricoList() {
		return bolleScaricoList;
	}

	public void setBolleScaricoList(List<BollaScaricoMagBulk> bolleScaricoList) {
		this.bolleScaricoList = bolleScaricoList;
	}

	public List<ScaricoMagazzinoRigaBulk> getScaricoMagazzinoRigaColl() {
		return scaricoMagazzinoRigaColl;
	}

	public void setScaricoMagazzinoRigaColl(List<ScaricoMagazzinoRigaBulk> scaricoMagazzinoRigaColl) {
		this.scaricoMagazzinoRigaColl = scaricoMagazzinoRigaColl;
	}
}
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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Bonus_nucleo_famBulk extends Bonus_nucleo_famBase {
	public final static java.util.Dictionary TIPO_COMPONENTE_NUCLEO;

	public final static String ALTRO   = "A";
	public final static String CONIUGE = "C";
	public final static String FIGLIO  = "F";
	
	static {
		TIPO_COMPONENTE_NUCLEO = new it.cnr.jada.util.OrderedHashtable();
		TIPO_COMPONENTE_NUCLEO.put(ALTRO,"Altro");
		TIPO_COMPONENTE_NUCLEO.put(CONIUGE,"Coniuge");
		TIPO_COMPONENTE_NUCLEO.put(FIGLIO,"Figlio");
	}
	private BonusBulk bonus;
	
	public Bonus_nucleo_famBulk() {
		super();
	}
	public Bonus_nucleo_famBulk(java.lang.Integer esercizio, java.lang.Long pg_bonus, java.lang.String cf_componente_nucleo) {
		super(esercizio,pg_bonus,cf_componente_nucleo);
		setBonus(new BonusBulk(esercizio,pg_bonus));
		setCf_componente_nucleo(cf_componente_nucleo);
	}
	public java.util.Dictionary getTipo_componente_nucleoKeys() {
		return TIPO_COMPONENTE_NUCLEO;
	}
	public BonusBulk getBonus() {
		return bonus;
	}
	public void setBonus(BonusBulk bonus) {
		this.bonus = bonus;
	}
	@Override
	public Integer getEsercizio() {
		if(getBonus()!=null)
			return getBonus().getEsercizio();
		return null;
	}
	@Override
	public void setEsercizio(Integer esercizio) {
		getBonus().setEsercizio(esercizio);
	}
	@Override
	public Long getPg_bonus() {
		if(getBonus()!=null)
			return getBonus().getPg_bonus();
		return null;
	}
	@Override
	public void setPg_bonus(Long pg_bonus) {
		getBonus().setPg_bonus(pg_bonus);
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		this.setFl_handicap(false);
		return super.initializeForInsert(crudbp, actioncontext);
	}
	
}
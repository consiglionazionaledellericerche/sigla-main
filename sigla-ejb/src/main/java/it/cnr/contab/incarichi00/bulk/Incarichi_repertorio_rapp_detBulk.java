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

package it.cnr.contab.incarichi00.bulk;

import java.io.File;
import java.util.Dictionary;


public class Incarichi_repertorio_rapp_detBulk extends Incarichi_repertorio_rapp_detBase {
	private Incarichi_repertorioBulk incarichi_repertorio = new Incarichi_repertorioBulk();
	public final static Dictionary tipoRapportoKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String TIPO_RAPP_INCARICO = "INC";
	final public static String TIPO_RAPP_PUBBLICO = "PUB";
	final public static String TIPO_RAPP_PRIVATO = "PRV";

	static {
		tipoRapportoKeys.put(TIPO_RAPP_INCARICO,"Incarico");
		tipoRapportoKeys.put(TIPO_RAPP_PUBBLICO,"Impiego di natura pubblicistica");
		tipoRapportoKeys.put(TIPO_RAPP_PRIVATO,"Impiego di diritto privato");
	}

	public static final java.util.Dictionary statoKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String STATO_VALIDO = "V";
	final public static String STATO_ANNULLATO = "A";

	static {
		statoKeys.put(STATO_VALIDO,"Valido");
		statoKeys.put(STATO_ANNULLATO,"Annullato");
	}

	public Incarichi_repertorio_rapp_detBulk() {
		super();
	}
	public Incarichi_repertorio_rapp_detBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
		setIncarichi_repertorio(new Incarichi_repertorioBulk(esercizio,pg_repertorio));
	}

	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}
	public void setIncarichi_repertorio(Incarichi_repertorioBulk incarichi_repertorio) {
		this.incarichi_repertorio = incarichi_repertorio;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setEsercizio(esercizio);
	}	
	public Long getPg_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_repertorio();
	}
	public void setPg_repertorio(Long pg_repertorio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
	}	

	public boolean isValido(){
		return getStato()!=null && getStato().equals(STATO_VALIDO);
	}
	public boolean isAnnullato(){
		return getStato()!=null && getStato().equals(STATO_ANNULLATO);
	}
}

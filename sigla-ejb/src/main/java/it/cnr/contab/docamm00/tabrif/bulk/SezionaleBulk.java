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

package it.cnr.contab.docamm00.tabrif.bulk;

import java.util.Dictionary;
import java.util.Optional;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SezionaleBulk extends SezionaleBase {	
	protected Tipo_sezionaleBulk tipo_sezionale;
	public final static Dictionary TIPO_FATTURA;
	public final static String FATTURA 		= 	"F";
	public final static String NOTACREDITO 	= 	"C";
	public final static String NOTADEBITO 	= 	"D";
	public final static String GENERALE 	= 	"T";
	protected ContoBulk contoIva = new ContoBulk();
	protected ContoBulk contoIvaSplit = new ContoBulk();

	static {
		TIPO_FATTURA = new it.cnr.jada.util.OrderedHashtable();
		TIPO_FATTURA.put(FATTURA,"Fattura");
		TIPO_FATTURA.put(NOTACREDITO,"Nota a Credito");
		TIPO_FATTURA.put(NOTADEBITO,"Nota a Debito");
		TIPO_FATTURA.put(GENERALE,"Protocollo Generale");	
	}
	public SezionaleBulk() {
		super();
	}
	public SezionaleBulk(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.String ti_fattura) {
		super(cd_cds,cd_tipo_sezionale,cd_unita_organizzativa,esercizio,ti_fattura);
		setTipo_sezionale(new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(cd_tipo_sezionale));
	}
	public java.lang.String getCd_tipo_sezionale() {
		it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale = this.getTipo_sezionale();
		if (tipo_sezionale == null)
			return null;
		return tipo_sezionale.getCd_tipo_sezionale();
	}
	/*
	 * Getter dell'attributo ti_fattura
	 */
	public java.lang.String getTi_fattura() {
		return ti_fattura;
	}
		/**
		 * Restituisce il <code>Dictionary</code> per la gestione dei tipi fattura.
		 *
		 * @return java.util.Dictionary
		 */

		public java.util.Dictionary getTi_fatturaKeys() {
			return TIPO_FATTURA;
		}
	/**
	 * @return Tipo_sezionaleBulk
	 */
	public Tipo_sezionaleBulk getTipo_sezionale() {
		return tipo_sezionale;
	}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		tipo_sezionale = new Tipo_sezionaleBulk();
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());

		setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
		return this;
	}
	/**
	 * @return Tipo_sezionaleBulk
	 */
	public boolean isROTipo_sezionale() {

		return getTipo_sezionale() == null ||
				getTipo_sezionale().getCrudStatus() == OggettoBulk.NORMAL;
	}
	public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
		this.getTipo_sezionale().setCd_tipo_sezionale(cd_tipo_sezionale);
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setTipo_sezionale(Tipo_sezionaleBulk newTipo_sezionale) {
		tipo_sezionale=newTipo_sezionale;
	}
	public void validate() throws ValidationException {

		if (getPrimo() == null)
			throw new ValidationException("Specificare l'intero per il primo sezionale!");
		if (getUltimo() == null)
			throw new ValidationException("Specificare l'intero per l'ultimo sezionale!");

		Long primoSezionale = null;
		Long ultimoSezionale = null;

		try {
			primoSezionale = getPrimo();
			ultimoSezionale = getUltimo();
		} catch (java.lang.NumberFormatException nfe){
			throw new ValidationException("Attenzione! Inserire solo numeri interi.");
		}


		if (primoSezionale.longValue() <= 0)
			throw new ValidationException("Attenzione! Il primo sezionale deve essere un numero intero positivo.");
		if (ultimoSezionale.longValue() <= 0)
			throw new ValidationException("Attenzione! L'ultimo sezionale deve essere un numero intero positivo.");


		if (ultimoSezionale.longValue() < primoSezionale.longValue())
			throw new ValidationException("Attenzione! L' \"ultimo\" intero per il sezionale non può essere minore del \"primo\".");

		Long correnteSezionale = null;
		if (getCorrente() == null)
			setCorrente(new Long(getPrimo().longValue()-1));
		try {
			correnteSezionale = getCorrente();
		if (correnteSezionale.longValue() < 0)
			throw new ValidationException("Attenzione! Il sezionale corrente deve essere un numero intero positivo.");
		} catch (java.lang.NumberFormatException nfe){
			throw new ValidationException("Attenzione! Inserire solo numeri interi per il sezionale corrente.");
		}

		if (primoSezionale.longValue() > correnteSezionale.longValue()+1)
				throw new ValidationException("Attenzione! Il Sezionale corrente può essere, al più, uguale al Primo meno uno!");
		if (correnteSezionale.longValue() > ultimoSezionale.longValue())
				throw new ValidationException("Attenzione! Il Sezionale corrente non può essere maggiore dell' Ultimo");

	}

	public ContoBulk getContoIva() {
		return contoIva;
	}

	public void setContoIva(ContoBulk contoIva) {
		this.contoIva = contoIva;
	}

	public String getCd_voce_ep_iva() {
		return Optional.ofNullable(this.getContoIva()).map(ContoBulk::getCd_voce_ep).orElse(null);
	}

	@Override
	public void setCd_voce_ep_iva(String cd_voce_ep_iva) {
		Optional.ofNullable(this.getContoIva()).ifPresent(el->el.setCd_voce_ep(cd_voce_ep_iva));
	}

	public ContoBulk getContoIvaSplit() {
		return contoIvaSplit;
	}

	public void setContoIvaSplit(ContoBulk contoIvaSplit) {
		this.contoIvaSplit = contoIvaSplit;
	}

	@Override
	public String getCd_voce_ep_iva_split() {
		return Optional.ofNullable(this.getContoIvaSplit()).map(ContoBulk::getCd_voce_ep).orElse(null);
	}

	@Override
	public void setCd_voce_ep_iva_split(String cd_voce_ep_iva_split) {
		Optional.ofNullable(this.getContoIvaSplit()).ifPresent(el->el.setCd_voce_ep(cd_voce_ep_iva_split));
	}
}

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
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.pdg00.bp.CRUDRicostruzioneResiduiBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_residuoBulk extends Pdg_residuoBase {

	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;
	private BigDecimal imTotale;

	private BulkList dettagli = new BulkList();
	public static java.util.Dictionary statiKeys = null;
	public final static java.util.Dictionary STATI_COMPLETO;
	public final static java.util.Dictionary STATI_APERTO;
	public final static java.util.Dictionary STATI_CREAZIONE;
	public final static java.util.Dictionary STATI_MODIFICA;

	public final static String STATO_APERTO = "A";
	public final static String STATO_CHIUSO = "C";
	public final static String STATO_MODIFICA = "M";

	static {
		STATI_COMPLETO = new it.cnr.jada.util.OrderedHashtable();
		STATI_COMPLETO.put(STATO_APERTO,"Aperto");
		STATI_COMPLETO.put(STATO_CHIUSO,"Chiuso");
		STATI_COMPLETO.put(STATO_MODIFICA,"Aperto per modifiche");
	}

	static {
		STATI_APERTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_APERTO.put(STATO_APERTO,"Aperto");
		STATI_APERTO.put(STATO_CHIUSO,"Chiuso");
		//STATI_APERTO.put(STATO_MODIFICA,"Aperto per modifiche");
	}

	static {
		STATI_CREAZIONE = new it.cnr.jada.util.OrderedHashtable();
		STATI_CREAZIONE.put(STATO_APERTO,"Aperto");
		//statiKeysCreazione.put(STATO_CHIUSO,"Chiuso");
		//statiKeysCreazione.put(STATO_MODIFICA,"Aperto per modifiche");
	}

	static {
		STATI_MODIFICA = new it.cnr.jada.util.OrderedHashtable();
		//statiKeysModifica.put(STATO_APERTO,"Aperto");
		STATI_MODIFICA.put(STATO_CHIUSO,"Chiuso");
		STATI_MODIFICA.put(STATO_MODIFICA,"Aperto per modifiche");
	}

	private boolean abilitaModificaStatoTestata = true;
	private boolean abilitaModificaMassaSpendibile = true;
	
	public Pdg_residuoBulk() {
		super();
		setStatiKeys(STATI_COMPLETO);
	}
	public Pdg_residuoBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super(esercizio, cd_centro_responsabilita);
		setEsercizio(esercizio);
		setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk( cd_centro_responsabilita ));
		setStatiKeys(STATI_COMPLETO);
	}

	public it.cnr.jada.bulk.BulkList getDettagli() {
		return dettagli;
	}
	public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
		dettagli = newDettagli;
	}
	public int addToDettagli(Pdg_residuo_detBulk dett) {
		dett.setPdg_residuo(this);
		dettagli.add(dett);
		aggiornaImportiTotali();
		return dettagli.size()-1;
	}
	public Pdg_residuo_detBulk removeFromDettagli(int index) {
		Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk)dettagli.remove(index);
		aggiornaImportiTotali();
		return dett;
	}
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {dettagli};
	}
	public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
			centro_responsabilita = newCentro_responsabilita;
		}
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
			return centro_responsabilita;
		}
	public java.lang.String getCd_centro_responsabilita() {
		if(centro_responsabilita == null) return null;
		return centro_responsabilita.getCd_centro_responsabilita();
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		centro_responsabilita.setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public static java.util.Dictionary getStatiKeys() {
		return statiKeys;
	}
	public static void setStatiKeys(java.util.Dictionary dictionary) {
		statiKeys = dictionary;
	}
	public BigDecimal getImTotale() {
		if (getIm_massa_spendibile()==null)
			return null;
		if (imTotale==null)
			aggiornaImportiTotali();
		return imTotale;
	}

	public void setImTotale(BigDecimal decimal) {
		imTotale = decimal;
	}
	
	public void aggiornaImportiTotali() {
		BulkList dett = getDettagli();
		if (dett!=null) {
			Iterator it = dett.iterator();
			BigDecimal totale = new BigDecimal("0");	
			totale = totale.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			while (it.hasNext()) {
				Pdg_residuo_detBulk riga = (Pdg_residuo_detBulk)it.next();
				if (!riga.getStato().equals(riga.STATO_ANNULLATO)&&riga.getIm_residuo()!=null)
					totale = totale.add(riga.getIm_residuo());
			}
			setImTotale(totale);
		}
	}

	public BigDecimal getImTotaleDifferenza() {
		if (getIm_massa_spendibile()!=null)
			return getIm_massa_spendibile().subtract(getImTotale());
		else
			return null;
	}	

	public boolean isROStato() {
			 
		if (!isAbilitaModificaStatoTestata())
			return true;

		return false;
	}

	public boolean isROMassaSpendibile() {

		if (!isAbilitaModificaMassaSpendibile())
			return true;

		return false;
	}

	/**
	 * @return
	 */
	public boolean isAbilitaModificaStatoTestata() {
		return abilitaModificaStatoTestata;
	}

	/**
	 * @param b
	 */
	public void setAbilitaModificaStatoTestata(boolean b) {
		abilitaModificaStatoTestata = b;
	}

	/**
	 * @return
	 */
	public boolean isAbilitaModificaMassaSpendibile() {
		return abilitaModificaMassaSpendibile;
	}

	/**
	 * @param b
	 */
	public void setAbilitaModificaMassaSpendibile(boolean b) {
		abilitaModificaMassaSpendibile = b;
	}

}
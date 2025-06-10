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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.SimpleBulkList;

import java.io.File;

/**
 * Insert the type's description here.
 * @author: RPucciarelli
 */
public class Aggiornamento_inventarioBulk extends Buono_carico_scaricoBulk {
	 
	// Ubicazione Destinazione
private it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk ubicazione_destinazione;
	
private TerzoBulk assegnatario;	
	 
private Utilizzatore_CdrVBulk vUtilizzatore;

private SimpleBulkList utilizzatori = new SimpleBulkList();

private SimpleBulkList v_utilizzatoriColl = new SimpleBulkList();

private SimpleBulkList dettagli= new SimpleBulkList();

	private String stato;

	private java.lang.String blob;

	private File file;

	/**
 * Aggiornamento_inventarioBulk constructor comment.
 */
public Aggiornamento_inventarioBulk() {
	super();
}

public int addToV_utilizzatoriColl (Utilizzatore_CdrVBulk nuovoVUtilizzatore)
{
		getV_utilizzatoriColl().add(nuovoVUtilizzatore);
		return getV_utilizzatoriColl().size()-1;
}

public BulkCollection[] getBulkLists() {
	
	 return new it.cnr.jada.bulk.BulkCollection[] { 
			getV_utilizzatoriColl(),
			getUtilizzatori(),
			getDettagli()
			};
}

public boolean isROUbicazioneDestinazione() {
	return getUbicazione_destinazione()==null || getUbicazione_destinazione().getCrudStatus()==NORMAL;
}

public boolean isROassegnatarioBene() {
	return getassegnatario()==null || getassegnatario().getCrudStatus()==NORMAL;
}

public void setUbicazione_destinazione(it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk newUbicazione_destinazione) {
	ubicazione_destinazione = newUbicazione_destinazione;
}
public void setassegnatario(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newassegnatario) {
	assegnatario = newassegnatario;
}

public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getassegnatario() {
	return assegnatario;
}

public it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk getUbicazione_destinazione() {
	return ubicazione_destinazione;
}
	

public it.cnr.jada.bulk.SimpleBulkList getUtilizzatori() {
	return utilizzatori;
}

public Utilizzatore_CdrVBulk getVUtilizzatore() {
	return vUtilizzatore;
}

public void setVUtilizzatore(Utilizzatore_CdrVBulk newVUtilizzatore) {
	vUtilizzatore = newVUtilizzatore;
}
public it.cnr.jada.bulk.SimpleBulkList getV_utilizzatoriColl() {
	return v_utilizzatoriColl;
}
public void setUtilizzatori(it.cnr.jada.bulk.SimpleBulkList newUtilizzatori) {
	utilizzatori = newUtilizzatori;
}
public void setV_utilizzatoriColl(SimpleBulkList newV_utilizzatoriColl) {
	
	v_utilizzatoriColl = newV_utilizzatoriColl;
}

public Utilizzatore_CdrVBulk removeFromV_utilizzatoriColl( int indiceDiLinea ) {

	Utilizzatore_CdrVBulk element = (Utilizzatore_CdrVBulk)v_utilizzatoriColl.get(indiceDiLinea);

	return (Utilizzatore_CdrVBulk)v_utilizzatoriColl.remove(indiceDiLinea);
}

public Inventario_utilizzatori_laBulk removeFromUtilizzatori( int indiceDiLinea ) {

	Inventario_utilizzatori_laBulk element = (Inventario_utilizzatori_laBulk)utilizzatori.get(indiceDiLinea);

	return (Inventario_utilizzatori_laBulk)utilizzatori.remove(indiceDiLinea);
}

public int addToUtilizzatori (Inventario_utilizzatori_laBulk nuovoUtilizzatore_la)
{
		getUtilizzatori().add(nuovoUtilizzatore_la);
		return getUtilizzatori().size()-1;
}
public boolean hasUtilizzatori() {
	return v_utilizzatoriColl.size()>0;
}
/**
 * @return
 */
public SimpleBulkList getDettagli() {
	return dettagli;
}

/**
 * @param list
 */
public void setDettagli(SimpleBulkList list) {
	dettagli = list;
}
public Inventario_beniBulk removeFromDettagli( int indiceDiLinea ) {

	Inventario_beniBulk element = (Inventario_beniBulk)dettagli.get(indiceDiLinea);

	return (Inventario_beniBulk)dettagli.remove(indiceDiLinea);
}

public int addToDettagli (Inventario_beniBulk nuovobene)
{
		getDettagli().add(nuovobene);
		return getDettagli().size()-1;
}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public final java.util.Dictionary getStatoKeys() {
		return Inventario_beniBulk.statoKeys;
	}

	public java.lang.String getBlob () {
		return blob;
	}

	public void setBlob(java.lang.String blob)  {
		this.blob=blob;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isBeneSmarrito() {
		return Inventario_beniBulk.STATO_SMARRITO.equals(this.getStato());
	}
}

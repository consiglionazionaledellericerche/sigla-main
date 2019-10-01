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

/**
 * Classe utilizzata per tenere traccia dell'associazione di  un CdR utilizzatore e 
 *	le linee di attività ad esso associate.
 * @author: Gennaro Borriello
 *
 */
//import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

public class Utilizzatore_CdrVBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	// CdR UTILIZZATORE
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
	
	// PERCENTUALE_UTILIZZO_CDR DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_utilizzo_cdr;
	
	private Inventario_beniBulk bene;
	private Buono_carico_scarico_dettBulk dettaglio;

	/* COLLECTION DI UTIIZZATORI  */	
	private SimpleBulkList buono_cs_utilizzatoriColl = new BulkList();

	
/**
 * UtilizzatoreCdrBulk constructor comment.
 */
public Utilizzatore_CdrVBulk() {
	super();
}
public int addToBuono_cs_utilizzatoriColl (Inventario_utilizzatori_laBulk nuovoUtilizzatore)
{

	buono_cs_utilizzatoriColl.add(nuovoUtilizzatore);
	nuovoUtilizzatore.setVUtilizzatore_cdr(this);
	if (getCdr()!=null){
		nuovoUtilizzatore.setCd_utilizzatore_cdr(getCdCdr());
		nuovoUtilizzatore.setCdr(getCdr());
	}
	nuovoUtilizzatore.setPercentuale_utilizzo_cdr(getPercentuale_utilizzo_cdr());
	nuovoUtilizzatore.setPercentuale_utilizzo_la(new java.math.BigDecimal(0));
	nuovoUtilizzatore.setBene(getBene());
	nuovoUtilizzatore.setDettaglio(getDettaglio());

	nuovoUtilizzatore.setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk());
	return buono_cs_utilizzatoriColl.size()-1;
}
/**
 * Metodo richiamato da Inventario_beniComponent.caricaUtilizzatoriFor(): è simile
 *	al metodo addToBuono_cs_utilizzatoriColl(), ma quì NON viene creato una nuova istanza
 *	di Linea di attività, bensì aggiunge semplicemente la Linea di Attività alla Collection.
*/
public int addToUtilizzatoriColl (Inventario_utilizzatori_laBulk utilizzatore_la)
{

	buono_cs_utilizzatoriColl.add(utilizzatore_la);
	utilizzatore_la.setVUtilizzatore_cdr(this);
	if (getCdr()!=null){
		utilizzatore_la.setCd_utilizzatore_cdr(getCdCdr());
		utilizzatore_la.setCdr(getCdr());
	}
	utilizzatore_la.setPercentuale_utilizzo_cdr(getPercentuale_utilizzo_cdr());
	utilizzatore_la.setBene(getBene());
	utilizzatore_la.setDettaglio(getDettaglio());
	
	return buono_cs_utilizzatoriColl.size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public Inventario_beniBulk getBene() {
	return bene;
}
public BulkCollection[] getBulkLists() {
	
	 return new it.cnr.jada.bulk.BulkCollection[] { 
			buono_cs_utilizzatoriColl
			};
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 11.07.07)
 * @return it.cnr.jada.bulk.SimpleBulkList
 */
public it.cnr.jada.bulk.SimpleBulkList getBuono_cs_utilizzatoriColl() {
	return buono_cs_utilizzatoriColl;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public String getCdCdr() {
	return cdr.getCd_centro_responsabilita();
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}

/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getPercentuale_utilizzo_cdr() {
	
	if (percentuale_utilizzo_cdr==null){
		percentuale_utilizzo_cdr = new java.math.BigDecimal("0");
	}
	
	return percentuale_utilizzo_cdr;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROcdr() {
	
	return getCdr() == null ||
			getCdr().getCrudStatus() == OggettoBulk.NORMAL;
}
public Inventario_utilizzatori_laBulk removeFromBuono_cs_utilizzatoriColl( int indiceDiLinea ) {

	Inventario_utilizzatori_laBulk element = (Inventario_utilizzatori_laBulk)buono_cs_utilizzatoriColl.get(indiceDiLinea);

	return (Inventario_utilizzatori_laBulk)buono_cs_utilizzatoriColl.remove(indiceDiLinea);

}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @param newBene it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public void setBene(Inventario_beniBulk newBene) {
	bene = newBene;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2001 14.41.50)
 * @param newFattura_attiva_dettColl it.cnr.jada.bulk.SimpleBulkList
 */
public void setBuono_cs_utilizzatoriColl(it.cnr.jada.bulk.SimpleBulkList newBuono_cs_utilizzatoriColl) {
	buono_cs_utilizzatoriColl = newBuono_cs_utilizzatoriColl;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}

/**
 * Insert the method's description here.
 * Creation date: (12/11/2001 5:03:54 PM)
 * @param newPercentuale_utilizzo_cdr java.math.BigDecimal
 */
public void setPercentuale_utilizzo_cdr(java.math.BigDecimal newPercentuale_utilizzo_cdr) {
	percentuale_utilizzo_cdr = newPercentuale_utilizzo_cdr;
}
	
public Buono_carico_scarico_dettBulk getDettaglio() {
	return dettaglio;
}

public void setDettaglio(Buono_carico_scarico_dettBulk bulk) {
	dettaglio = bulk;
}

}

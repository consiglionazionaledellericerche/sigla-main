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
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;
import java.util.List;
import java.util.Vector;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Tipo_ruoloBulk extends Tipo_ruoloBase {
	private it.cnr.jada.bulk.BulkList	tipo_ruolo_privilegi = new it.cnr.jada.bulk.BulkList();
	private List tipo_ruolo_privilegi_disponibili = new Vector();

	private List privilegi             = new it.cnr.jada.util.Collect(tipo_ruolo_privilegi,"privilegio");
	private List privilegi_disponibili = new it.cnr.jada.util.Collect(tipo_ruolo_privilegi_disponibili,"privilegio");
	private UtenteBulk gestore;

	public Tipo_ruoloBulk() {
		super();
	}
	public Tipo_ruoloBulk(java.lang.String tipo) {
		super(tipo);
	}
	public OggettoBulk initializeForInsert(CRUDBP crudbp,ActionContext actioncontext) {
		return super.initializeForInsert(crudbp, actioncontext);
	}

	/**
	 * Aggiunge una nuova associazione ruolo-privilegio (Ass_tipo_ruolo_privilegioBulk) alla lista di privilegi definiti per il tipo ruolo
	 * inizializzandone alcuni campi e rimuove tale associazione dalla lista di quelle ancora disponibili per il tipo ruolo
	 * @param index indice della collezione di ruoli-privilegi disponibili da cui rimuovere l'associazione
	 * @return Ass_tipo_ruolo_privilegioBulk l'associazione ruolo-privilegio aggiunta
	 */
	public Ass_tipo_ruolo_privilegioBulk addToTipo_ruolo_privilegi(int index) 
	{
		Ass_tipo_ruolo_privilegioBulk ra = (Ass_tipo_ruolo_privilegioBulk)this.tipo_ruolo_privilegi_disponibili.remove(index);
		ra.setTipo_ruolo( this );
		this.tipo_ruolo_privilegi.add(ra);
		return ra;
	}

	/**
	 * Aggiunge una nuova associazione ruolo-privilegio (Ass_tipo_ruolo_privilegioBulk) alla lista di accessi definiti per il tipo ruolo
	 * @param ra Ass_tipo_ruolo_privilegioBulk da aggiungere
	 * @return int numero di associazioni ruolo-privilegi
	 */

	public int addToTipo_ruolo_privilegi(Ass_tipo_ruolo_privilegioBulk ra) {
		this.tipo_ruolo_privilegi.add(ra);
		return this.tipo_ruolo_privilegi.size()-1;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getPrivilegi() {
		return privilegi;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getPrivilegi_disponibili() {
		return privilegi_disponibili;
	}
	/**
	 * Restituisce la collezione di Ass_tipo_ruolo_privilegioBulk associata al tipo ruolo per 
	 * renderla persistente contestualmente alla gestione della persistenza del ruolo stesso
	 */

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				tipo_ruolo_privilegi
				};
	}
	/**
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getTipo_ruolo_privilegi() {
		return tipo_ruolo_privilegi;
	}
	/**
	 * Rimuove una associazione ruolo-privilegi (Ass_tipo_ruolo_privilegioBulk) alla lista di accessi definiti per il tipo ruolo
	 * e aggiunge tale associazione alla lista di quelle ancora disponibili per il tipo ruolo
	 * @param index indice della collezione di ruoli-privilegi assegnati al tipo ruolo da cui rimuovere l'associazione
	 * @return Ass_tipo_ruolo_privilegioBulk l'associazione rimossa
	 */
	 
	public Ass_tipo_ruolo_privilegioBulk removeFromTipo_ruolo_privilegi(int index) {
		Ass_tipo_ruolo_privilegioBulk ra = (Ass_tipo_ruolo_privilegioBulk)this.tipo_ruolo_privilegi.remove(index);
		this.tipo_ruolo_privilegi_disponibili.add(ra);
		return ra;
	}
	/**
	 * Imposta la lista di associazioni ruolo-accesso (Ruolo_accessoBulk) disponibili ad un ruolo a partire 
	 * da una lista di accessi (AccessoBulk)
	 * @param newAccessi_disponibili lista di AccessoBulk
	 */

	public void setPrivilegi_disponibili(java.util.List newPrivilegi_disponibili) 
	{
		for (java.util.Iterator i = newPrivilegi_disponibili.iterator();i.hasNext();) 
		{
			PrivilegioBulk privilegio = (PrivilegioBulk)i.next();
			if (this.privilegi.contains(privilegio)) continue;
			Ass_tipo_ruolo_privilegioBulk ra = new Ass_tipo_ruolo_privilegioBulk();
			ra.setPrivilegio( privilegio );
			tipo_ruolo_privilegi_disponibili.add(ra);
		}
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'gestore'
	 *
	 * @return Il valore della proprietà 'gestore'
	 */
	public UtenteBulk getGestore() {
		return gestore;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'gestore'
	 *
	 * @param newGestore	Il valore da assegnare a 'gestore'
	 */
	public void setGestore(UtenteBulk newGestore) {
		gestore = newGestore;
	}
	
}

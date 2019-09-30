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
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Tipo_contrattoBulk extends Tipo_contrattoBase implements ICancellatoLogicamente{
	private static final java.util.Dictionary ti_natura_contabileKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String NATURA_CONTABILE_ATTIVO = "A";
	final public static String NATURA_CONTABILE_PASSIVO = "P";
	final public static String NATURA_CONTABILE_ATTIVO_E_PASSIVO = "E";
	final public static String NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI = "N";
	static {
		ti_natura_contabileKeys.put(NATURA_CONTABILE_ATTIVO,"Attivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_PASSIVO,"Passivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_ATTIVO_E_PASSIVO,"Attivo e Passivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI,"Senza flussi finanziari");
	}
	
	public Tipo_contrattoBulk() {
		super();
	}
	public Tipo_contrattoBulk(java.lang.String cd_tipo_contratto) {
		super(cd_tipo_contratto);
	}
    /**
     * Inizializza per l'inserimento i flag
     */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setFl_cancellato(new Boolean(false));
		setFl_cig(Boolean.FALSE);
		return super.initializeForInsert(bp,context);
	}	
    /**
     * Insert the method's description here.
     * Creation date: (11/04/2005 12:34:48)
     * @return java.util.Dictionary
     */
    public final java.util.Dictionary getTi_natura_contabileKeys() {
    	return ti_natura_contabileKeys;
    }
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#isCancellatoLogicamente()
	 */
	public boolean isCancellatoLogicamente() {		
		return getFl_cancellato().booleanValue();
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#cancellaLogicamente()
	 */
	public void cancellaLogicamente() {
		setFl_cancellato(new Boolean(true));		
	}
}
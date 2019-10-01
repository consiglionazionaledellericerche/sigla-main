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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Tipo_limiteBulk extends Tipo_limiteBase {
	private BulkList repertorio_limitiColl = new BulkList();
	
	private boolean fl_raggruppa = Boolean.TRUE;

	public Tipo_limiteBulk() {
		super();
	}
	public Tipo_limiteBulk(java.lang.String cd_tipo_limite) {
		super(cd_tipo_limite);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}
	public BulkList getRepertorio_limitiColl() {
		return repertorio_limitiColl;
	}
	public void setRepertorio_limitiColl(BulkList repertorio_limitiColl) {
		this.repertorio_limitiColl = repertorio_limitiColl;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 repertorio_limitiColl};
	}
	public int addToRepertorio_limitiColl( Repertorio_limitiBulk repertorio ) 
	{
		repertorio_limitiColl.add(repertorio);
		repertorio.setTipo_limite(this);
		repertorio.setImporto_limite( new java.math.BigDecimal(0));
		repertorio.setImporto_utilizzato( new java.math.BigDecimal(0));
		repertorio.setFl_raggiunto_limite(Boolean.FALSE);	
		
		return repertorio_limitiColl.size()-1;
	}
	public Repertorio_limitiBulk removeFromRepertorio_limitiColl(int index) 
	{
		// Gestisce la selezione del bottone cancella repertorio
		return (Repertorio_limitiBulk)repertorio_limitiColl.remove(index);
	}
	public boolean getFl_raggruppa() {
		return fl_raggruppa;
	}
	public void setFl_raggruppa(boolean fl_raggruppa) {
		this.fl_raggruppa = fl_raggruppa;
	}
}
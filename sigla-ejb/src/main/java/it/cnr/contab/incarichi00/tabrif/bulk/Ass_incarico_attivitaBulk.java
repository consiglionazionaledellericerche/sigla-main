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
import java.util.Dictionary;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_incarico_attivitaBulk extends Ass_incarico_attivitaBase {
	private Tipo_incaricoBulk tipo_incarico;
	private Tipo_attivitaBulk tipo_attivita;
	private Tipo_limiteBulk   tipo_limite;
	
	public static final String TIPO_NATURA_FONTI_INTERNE ="FIN";
	public static final String TIPO_NATURA_FONTI_ESTERNE ="FES";
	
	public final static Dictionary tipo_naturaKeys;
	static {
		tipo_naturaKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_naturaKeys.put(TIPO_NATURA_FONTI_INTERNE,"Fonti Interne");
		tipo_naturaKeys.put(TIPO_NATURA_FONTI_ESTERNE,"Fonti Esterne");
	};

	public Ass_incarico_attivitaBulk() {
		super();
	}
	public Ass_incarico_attivitaBulk(java.lang.Integer esercizio, java.lang.String cd_tipo_incarico, java.lang.String cd_tipo_attivita, java.lang.String tipo_natura) {
		super(esercizio, cd_tipo_incarico, cd_tipo_attivita, tipo_natura);
		setTipo_incarico(new Tipo_incaricoBulk(cd_tipo_incarico));
		setTipo_attivita(new Tipo_attivitaBulk(cd_tipo_attivita));
	}
	public Tipo_attivitaBulk getTipo_attivita() {
		return tipo_attivita;
	}
	public void setTipo_attivita(Tipo_attivitaBulk tipo_attivita) {
		this.tipo_attivita = tipo_attivita;
	}
	public Tipo_incaricoBulk getTipo_incarico() {
		return tipo_incarico;
	}
	public void setTipo_incarico(Tipo_incaricoBulk tipo_incarico) {
		this.tipo_incarico = tipo_incarico;
	}
	public Tipo_limiteBulk getTipo_limite() {
		return tipo_limite;
	}
	public void setTipo_limite(Tipo_limiteBulk tipo_limite) {
		this.tipo_limite = tipo_limite;
	}
	public java.lang.String getCd_tipo_attivita() {
		if (getTipo_attivita()==null) return null;
		return getTipo_attivita().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita) {
		if (getTipo_attivita()!=null) 
			getTipo_attivita().setCd_tipo_attivita(cd_tipo_attivita);
	}
	public java.lang.String getCd_tipo_incarico() {
		if (getTipo_incarico()==null) return null;
		return getTipo_incarico().getCd_tipo_incarico();
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico) {
		if (getTipo_incarico()!=null) 
			getTipo_incarico().setCd_tipo_incarico(cd_tipo_incarico);
	}
	public java.lang.String getCd_tipo_limite() {
		if (getTipo_limite()==null) return null;
		return getTipo_limite().getCd_tipo_limite();
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite) {
		if (getTipo_limite()!=null) 
			getTipo_limite().setCd_tipo_limite(cd_tipo_limite);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
		return this;
	}
}
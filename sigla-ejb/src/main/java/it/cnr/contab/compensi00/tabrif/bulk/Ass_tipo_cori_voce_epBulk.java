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
 * Date 16/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.GregorianCalendar;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

public class Ass_tipo_cori_voce_epBulk extends Ass_tipo_cori_voce_epBase {
	public final static Dictionary TI_ENTE_PERCIPIENTE;
	public final static String ENTE = "E";
	public final static String PERCIPIENTE = "P";
	static
	{
		TI_ENTE_PERCIPIENTE = new OrderedHashtable();
		TI_ENTE_PERCIPIENTE.put(ENTE, "Ente");
		TI_ENTE_PERCIPIENTE.put(PERCIPIENTE, "Percipiente");		
	}	
	public final static Dictionary SEZIONE;
	public final static String DARE = "D";
	public final static String AVERE = "A";
	
	static
	{
		SEZIONE = new OrderedHashtable();
		SEZIONE.put(DARE, "Dare");
		SEZIONE.put(AVERE,"Avere");		
	}
	public java.util.Dictionary getTi_entePercipienteKeys() {
		return TI_ENTE_PERCIPIENTE;
	}
	public java.util.Dictionary getSezioneKeys() {
		return SEZIONE;
	}
	private Tipo_contributo_ritenutaBulk contributo_ritenuta = new Tipo_contributo_ritenutaBulk();
	private ContoBulk voce_ep=new ContoBulk();
	private ContoBulk voce_ep_contr=new ContoBulk();
	public Ass_tipo_cori_voce_epBulk() {
		super();
	}
	public Ass_tipo_cori_voce_epBulk(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.lang.String ti_ente_percepiente, java.lang.String sezione) {
		super(esercizio, cd_contributo_ritenuta, ti_ente_percepiente, sezione);
	}
	public Tipo_contributo_ritenutaBulk getContributo_ritenuta() {
		return contributo_ritenuta;
	}
	public void setContributo_ritenuta(
			Tipo_contributo_ritenutaBulk contributo_ritenuta) {
		this.contributo_ritenuta = contributo_ritenuta;
	}
	public ContoBulk getVoce_ep() {
		return voce_ep;
	}
	public void setVoce_ep(ContoBulk voce_ep) {
		this.voce_ep = voce_ep;
	}
	public ContoBulk getVoce_ep_contr() {
		return voce_ep_contr;
	}
	public void setVoce_ep_contr(ContoBulk voce_ep_contr) {
		this.voce_ep_contr = voce_ep_contr;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
		this.getContributo_ritenuta().setCd_contributo_ritenuta(cd_contributo_ritenuta);
	}
	public java.lang.String getCd_contributo_ritenuta() {
		it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk contributo_ritenuta = this.getContributo_ritenuta();
		if (contributo_ritenuta == null)
			return null;
		return contributo_ritenuta.getCd_contributo_ritenuta();
	}
	
	public void setCd_voce_ep(String cd_voce_ep) {
		
		this.getVoce_ep().setCd_voce_ep(cd_voce_ep);
	}
	
	public void setCd_voce_ep_contr(String cd_voce_ep_contr) {
		
		this.getVoce_ep_contr().setCd_voce_ep(cd_voce_ep_contr);
	}
	
	public String getCd_voce_ep() {
		ContoBulk voce_ep = this.getVoce_ep();
		if (voce_ep == null)
			return null;
		return voce_ep.getCd_voce_ep();
	}
	public String getCd_voce_ep_contr() {
		ContoBulk voce_ep_contr = this.getVoce_ep_contr();
		if (voce_ep_contr == null)
			return null;
		return voce_ep_contr.getCd_voce_ep();
	}
	
	public void setEsercizio(Integer esercizio) {
		this.getVoce_ep().setEsercizio(esercizio);
	}
	
	public Integer getEsercizio() {
		ContoBulk voce_ep = this.getVoce_ep();
		if (voce_ep == null)
			return null;
		return voce_ep.getEsercizio();
	}
	protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		return super.initialize(crudbp, actioncontext);
	}
}
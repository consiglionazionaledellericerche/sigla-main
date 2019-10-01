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
 * Date 11/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

public class Ass_tipo_cori_evBulk extends Ass_tipo_cori_evBase {
	public final static Dictionary TI_ENTE_PERCIPIENTE;
	public final static Dictionary TI_GESTIONE_D;
	public final static String INDIFFERENTE = "*";
	public final static String ENTE = "E";
	public final static String PERCIPIENTE = "P";
	public final static String ENTRATA = "E";
	public final static String SPESA = "S";
	
	static
	{
		TI_ENTE_PERCIPIENTE = new OrderedHashtable();
		TI_ENTE_PERCIPIENTE.put(INDIFFERENTE, "Entrambi");
		TI_ENTE_PERCIPIENTE.put(ENTE, "Ente");
		TI_ENTE_PERCIPIENTE.put(PERCIPIENTE, "Percipiente");		
	}	
	static
	{
		TI_GESTIONE_D = new OrderedHashtable();
		TI_GESTIONE_D.put(ENTRATA, "Entrata");
		TI_GESTIONE_D.put(SPESA, "Spesa");
	}	
	private Tipo_contributo_ritenutaBulk contributo_ritenuta = new Tipo_contributo_ritenutaBulk();
	private Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	
	public Ass_tipo_cori_evBulk() {
		 
		super();
		//setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
	}
	public Ass_tipo_cori_evBulk(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String ti_ente_percepiente, java.lang.String cd_contributo_ritenuta) {
		super(esercizio, ti_appartenenza, ti_gestione, ti_ente_percepiente, cd_contributo_ritenuta);
	}
	public java.util.Dictionary getTi_entePercipienteKeys() {
		return TI_ENTE_PERCIPIENTE;
	}
	public Tipo_contributo_ritenutaBulk getContributo_ritenuta() {
		return contributo_ritenuta;
	}
	public void setContributo_ritenuta(
			Tipo_contributo_ritenutaBulk contributo_ritenuta) {
		this.contributo_ritenuta = contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk contributo_ritenuta = this.getContributo_ritenuta();
		if (contributo_ritenuta == null)
			return null;
		return contributo_ritenuta.getCd_contributo_ritenuta();
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
		this.getContributo_ritenuta().setCd_contributo_ritenuta(cd_contributo_ritenuta);
	}
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}
	public java.lang.String getCd_elemento_voce() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	public java.lang.String getTi_appartenenza() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	
	public java.lang.String getTi_gestione() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	@Override
	public void setEsercizio(Integer esercizio) {
		this.getElemento_voce().setEsercizio(esercizio);
	}
	@Override
	public Integer getEsercizio() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);
		setTi_ente_percepiente(INDIFFERENTE);
		//setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
		return this;
	}
	public void validate() throws ValidationException 
	{
		// controllo su campo CONTRIBUTO RITENUTA
		if ((getCd_contributo_ritenuta() == null ) || (getCd_contributo_ritenuta().trim().length() == 0))
			throw new ValidationException( "Il campo Contributo Ritenuta deve essere valorizzato!" );

//		 controllo su campo TIPO ENTE PERCIPIENTE
		if (getTi_ente_percepiente() == null ) 
			throw new ValidationException( "Il campo Tipo Ente Percipiente deve essere valorizzato" );	

		// controllo su campo elemento voce
		if ((getCd_elemento_voce() == null ) || (getCd_contributo_ritenuta().trim().length() == 0))
			throw new ValidationException( "Il campo Elemento voce deve essere valorizzato" );

	}
	public java.util.Dictionary getTi_GestioneKeys() {
		return TI_GESTIONE_D;
	}
	protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		//setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		return super.initialize(crudbp, actioncontext);
	}
	public boolean isROTi_gestione(){
		return getElemento_voce()!=null && getElemento_voce().getCd_elemento_voce() != null ;
	}
	
}
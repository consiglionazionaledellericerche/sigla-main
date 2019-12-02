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
 * Date 27/09/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Timestamp;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_tipo_contr_ritenuta_siopeBulk extends Ass_tipo_contr_ritenuta_siopeBase {
	public Ass_tipo_contr_ritenuta_siopeBulk() {
		super();
	}
	public Ass_tipo_contr_ritenuta_siopeBulk(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.sql.Timestamp dt_ini_validita) {
		super(esercizio, cd_contributo_ritenuta, dt_ini_validita);
	}
	public boolean isROGiaAssociato() {
		if (tipo_contributo!=null ||siope_spesa!=null ||siope_entrata!=null)
	return true;
		return false;
}
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	Tipo_contributo_ritenutaBulk tipo_contributo=new Tipo_contributo_ritenutaBulk();
	Codici_siopeBulk siope_spesa=new Codici_siopeBulk();
	Codici_siopeBulk siope_entrata=new Codici_siopeBulk();
	
	public Tipo_contributo_ritenutaBulk getTipo_contributo() {
		return tipo_contributo;
	}
	public void setTipo_contributo(Tipo_contributo_ritenutaBulk tipo_contributo) {
		this.tipo_contributo = tipo_contributo;
	}
	public Codici_siopeBulk getSiope_entrata() {
		return siope_entrata;
	}
	public void setSiope_entrata(Codici_siopeBulk siope_entrata) {
		this.siope_entrata = siope_entrata;
	}
	public Codici_siopeBulk getSiope_spesa() {
		return siope_spesa;
	}
	public void setSiope_spesa(Codici_siopeBulk siope_spesa) {
		this.siope_spesa = siope_spesa;
	}
	@Override
	public void setCd_contributo_ritenuta(String cd_contributo_ritenuta) {
		this.getTipo_contributo().setCd_contributo_ritenuta(cd_contributo_ritenuta);
	}
	@Override
	public String getCd_contributo_ritenuta() {
		if(this.getTipo_contributo()!=null)
			return this.getTipo_contributo().getCd_contributo_ritenuta();
		else
			return null;
	}
	@Override
	public Timestamp getDt_ini_validita() {
		if (this.getTipo_contributo()!=null)
			return this.getTipo_contributo().getDt_ini_validita();
		else
			return null;
	}
	@Override
	public void setDt_ini_validita(Timestamp dt_ini_validita) {
		this.getTipo_contributo().setDt_ini_validita(dt_ini_validita);
	}
	@Override
	public Integer getEsercizio_siope_e() {
		if (this.getSiope_entrata()!=null)
			return this.getSiope_entrata().getEsercizio();
		else
			return null;
	}
	@Override
	public String getTi_gestione_siope_e() {
		if (this.getSiope_entrata()!=null)
			return this.getSiope_entrata().getTi_gestione();
		else
			return null;
	}
	@Override
	public String getCd_siope_e() {
		if (this.getSiope_entrata()!=null)
			return this.getSiope_entrata().getCd_siope();
		else
			return null;
	}
	@Override
	public void setEsercizio_siope_e(Integer esercizio_siope_e) {
		this.getSiope_entrata().setEsercizio(esercizio_siope_e);
	}
	@Override
	public void setTi_gestione_siope_e(String ti_gestione_siope_e) {
		this.getSiope_entrata().setTi_gestione(ti_gestione_siope_e);
	}
	@Override
	public void setCd_siope_e(String cd_siope_e) {
		this.getSiope_entrata().setCd_siope(cd_siope_e);
	}
	@Override
	public Integer getEsercizio_siope_s() {
		if (this.getSiope_spesa()!=null)
			return this.getSiope_spesa().getEsercizio();
		else
			return null;
	}
	@Override
	public String getTi_gestione_siope_s() {
		if (this.getSiope_spesa()!=null)
			return this.getSiope_spesa().getTi_gestione();
		else
			return null;
	}
	@Override
	public String getCd_siope_s() {
		if (this.getSiope_spesa()!=null)
			return this.getSiope_spesa().getCd_siope();
		else
			return null;
	}
	@Override
	public void setEsercizio_siope_s(Integer esercizio_siope_s) {
		this.getSiope_spesa().setEsercizio(esercizio_siope_s);
	}
	@Override
	public void setTi_gestione_siope_s(String ti_gestione_siope_s) {
		this.getSiope_spesa().setTi_gestione(ti_gestione_siope_s);
	}
	@Override
	public void setCd_siope_s(String cd_siope_s) {
		this.getSiope_spesa().setCd_siope(cd_siope_s);
	}
	protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(actioncontext));
		return super.initialize(crudbp, actioncontext);
	}
	public void validate() throws ValidationException 
	{
//		 controllo su campo CONTRIBUTO RITENUTA
		if ((getCd_contributo_ritenuta() == null ) || (getCd_contributo_ritenuta().trim().length() == 0))
			throw new ValidationException( "Il campo CONTRIBUTO RITENUTA deve essere valorizzato!" );
//		 controllo su campo Siope spesa
		if ((getCd_siope_s() == null ) || (getCd_siope_s().trim().length() == 0))
			throw new ValidationException( "Il campo Codice Siope di spesa deve essere valorizzato!" );
//		 controllo su campo Siope entrata
		if ((getCd_siope_e() == null ) || (getCd_siope_e().trim().length() == 0))
			throw new ValidationException( "Il campo Codice Siope di entrata deve essere valorizzato!" );
	
	}
}
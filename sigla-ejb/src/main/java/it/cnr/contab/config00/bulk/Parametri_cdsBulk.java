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

package it.cnr.contab.config00.bulk;

import java.util.Dictionary;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Creation date: (12/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cdsBulk extends Parametri_cdsBase {
	private CdsBulk centro_spesa = new CdsBulk();
	private DipartimentoBulk  dipartimento = new DipartimentoBulk();
	public static final String BLOCCO_IMPEGNI_ERROR   ="E";
	public static final String BLOCCO_IMPEGNI_WARNING ="W";
	public static final String BLOCCO_IMPEGNI_NOTHING ="N";
	
	public final static Dictionary blocco_impegniKeys;
	static {
		blocco_impegniKeys = new it.cnr.jada.util.OrderedHashtable();
		blocco_impegniKeys.put(BLOCCO_IMPEGNI_ERROR,"Bloccante");
		blocco_impegniKeys.put(BLOCCO_IMPEGNI_WARNING,"Non Bloccante");
		blocco_impegniKeys.put(BLOCCO_IMPEGNI_NOTHING,"Nessuno");	
    };

	public final static Dictionary abilProgettiKeys;
	static {
		abilProgettiKeys = new it.cnr.jada.util.OrderedHashtable();
		abilProgettiKeys.put(V_struttura_organizzativaHome.LIVELLO_UO,"Unità Organizzativa");
		abilProgettiKeys.put(V_struttura_organizzativaHome.LIVELLO_CDS,"Centro di Spesa");
	};

	public Parametri_cdsBulk() {
		super();
	}
	public Parametri_cdsBulk(String cd_cds, java.lang.Integer esercizio ) {
	  super(cd_cds, esercizio );
	  setCd_cds(cd_cds);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context){
		super.initializeForInsert(bp, context);
		setEsercizio(CNRUserInfo.getEsercizio(context) );
		setFl_commessa_obbligatoria( new Boolean( false ) );
		setFl_ribaltato( new Boolean( false ) );
		setFl_progetto_numeratore(new Boolean( false ) );
		setFl_obbligo_protocollo_inf(new Boolean( false ) );
		setFl_contratto_cessato(new Boolean( false ) );
		setFl_approva_var_pdg(new Boolean( false ) );
		setFl_approva_var_stanz_res(new Boolean( false ) );
		setFl_blocco_iban(new Boolean(false));
		setFl_blocco_impegni_natfin(Boolean.FALSE);
		setFl_allega_determina_obblig(Boolean.FALSE);
		return this;
	}
	
	public String getCd_cds() {
		return getCentro_spesa().getCd_unita_organizzativa();
	}

	public void setCd_cds(String string) {
		getCentro_spesa().setCd_unita_organizzativa(string);
	}

	public CdsBulk getCentro_spesa() {
		return centro_spesa;
	}

	public void setCentro_spesa(CdsBulk bulk) {
		centro_spesa = bulk;
	}
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}
	public void setDipartimento(DipartimentoBulk dipartimento) {
		this.dipartimento = dipartimento;
	}
	@Override
	public String getCd_dipartimento() {
		if(getDipartimento() == null)
			  return null;
			return getDipartimento().getCd_dipartimento();
	}
	@Override 
	public void setCd_dipartimento(String cd_dipartimento) {
		  getDipartimento().setCd_dipartimento(cd_dipartimento);
	}
}

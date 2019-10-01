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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.math.BigDecimal;

public class V_pdg_obbligazione_speBulk extends V_pdg_obbligazione_speBase {
	private BigDecimal prcImputazioneFin;	
public V_pdg_obbligazione_speBulk() {
	super();
}
public V_pdg_obbligazione_speBulk(java.lang.String categoria_dettaglio,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(categoria_dettaglio,cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,stato,ti_appartenenza,ti_gestione);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'importo'
 *
 * @return Il valore della proprietà 'importo'
 */
public BigDecimal getImporto() 
{
	if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO.equals( getCategoria_dettaglio()) ||
	     Pdg_modulo_spese_gestBulk.CAT_DIRETTA.equals( getCategoria_dettaglio()))
		return getIm_ri_ccs_spese_odc().add( getIm_rk_ccs_spese_ogc() ).add( getIm_rq_ssc_costi_odc() ).add( getIm_rs_ssc_costi_ogc() ).add( getIm_ru_spese_costi_altrui() );
	else if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SCARICO.equals( getCategoria_dettaglio()))
		return getIm_rj_ccs_spese_odc_altra_uo().add( getIm_rl_ccs_spese_ogc_altra_uo()).add( getIm_rr_ssc_costi_odc_altra_uo()).add( getIm_rt_ssc_costi_ogc_altra_uo());
	return new BigDecimal(0);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'importoPrimoAnno'
 *
 * @return Il valore della proprietà 'importoPrimoAnno'
 */
public BigDecimal getImportoPrimoAnno() 
{
	if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO.equals( getCategoria_dettaglio()) ||
		 Pdg_modulo_spese_gestBulk.CAT_DIRETTA.equals( getCategoria_dettaglio()))
		return getIm_rac_a2_spese_odc().add( getIm_rae_a2_spese_ogc() ).add( getIm_rag_a2_spese_costi_altrui() );
	else if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SCARICO.equals( getCategoria_dettaglio()))
		return getIm_rad_a2_spese_odc_altra_uo().add( getIm_raf_a2_spese_ogc_altra_uo() );
	return new BigDecimal(0);		
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'importoSecondoAnno'
 *
 * @return Il valore della proprietà 'importoSecondoAnno'
 */
public BigDecimal getImportoSecondoAnno() 
{
	if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO.equals( getCategoria_dettaglio()) ||
		 Pdg_modulo_spese_gestBulk.CAT_DIRETTA.equals( getCategoria_dettaglio()))
		return getIm_ral_a3_spese_odc().add( getIm_ran_a3_spese_ogc() ).add( getIm_rap_a3_spese_costi_altrui() );
	else if ( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SCARICO.equals( getCategoria_dettaglio()))
		return getIm_ram_a3_spese_odc_altra_uo().add( getIm_rao_a3_spese_ogc_altra_uo() );
	return new BigDecimal(0);		
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'prcImputazioneFin'
 *
 * @return Il valore della proprietà 'prcImputazioneFin'
 */
public BigDecimal getPrcImputazioneFin() {
	return prcImputazioneFin;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'prcImputazioneFin'
 *
 * @param newPrcImputazioneFin	Il valore da assegnare a 'prcImputazioneFin'
 */
public void setPrcImputazioneFin(BigDecimal newPrcImputazioneFin) {
	prcImputazioneFin = newPrcImputazioneFin;
}
}

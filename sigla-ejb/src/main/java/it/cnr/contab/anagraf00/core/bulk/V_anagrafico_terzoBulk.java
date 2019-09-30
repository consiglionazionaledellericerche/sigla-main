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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_anagrafico_terzoBulk extends V_anagrafico_terzoBase {

public V_anagrafico_terzoBulk() {
	super();
}
public V_anagrafico_terzoBulk(Integer terzo) {
	super(terzo);
}
public AnagraficoBulk getAnagrafico() {
	
	AnagraficoBulk anagrafico = new AnagraficoBulk();
	anagrafico.setCd_anag( getCd_anag());
	anagrafico.setAliquota_fiscale( getAliquota_fiscale());
	anagrafico.setAltra_ass_previd_inps( getAltra_ass_previd_inps());
	anagrafico.setCap_comune_fiscale( getCap_comune_fiscale());
	anagrafico.setCausale_fine_rapporto( getCausale_fine_rapporto());
	anagrafico.setCd_attivita_inps( getCd_attivita_inps());
//	anagrafico.setCd_classific_anag( getCd_classific_anag()); null pointer exception
//	anagrafico.setCd_ente_appartenenza( getCd_ente_appartenenza());
	anagrafico.setCodice_fiscale( getCodice_fiscale());
	anagrafico.setCodice_fiscale_caf( getCodice_fiscale_caf());
	anagrafico.setCognome( getCognome());
	anagrafico.setConto_numerario_credito( getConto_numerario_credito());
	anagrafico.setConto_numerario_debito( getConto_numerario_debito());
	anagrafico.setDenominazione_caf( getDenominazione_caf());
	anagrafico.setDt_antimafia( getDt_antimafia());
	anagrafico.setDt_canc( getDt_canc());
	anagrafico.setDt_fine_rapporto( getDt_fine_rapporto());
	anagrafico.setDt_nascita( getDt_nascita());
	anagrafico.setFl_fatturazione_differita( getFl_fatturazione_differita());
	anagrafico.setFl_occasionale( getFl_occasionale());
	anagrafico.setFl_soggetto_iva( getFl_soggetto_iva());
	anagrafico.setFrazione_fiscale( getFrazione_fiscale());
	anagrafico.setId_fiscale_estero( getId_fiscale_estero());
	anagrafico.setMatricola_inail( getMatricola_inail());
	anagrafico.setNome( getNome());
	anagrafico.setNote( getNote());
	anagrafico.setNum_civico_fiscale( getNum_civico_fiscale());
	anagrafico.setNum_iscriz_albo( getNum_iscriz_albo());
	anagrafico.setNum_iscriz_cciaa( getNum_iscriz_cciaa());
	anagrafico.setPartita_iva( getPartita_iva());
//	anagrafico.setPg_comune_fiscale( getPg_comune_fiscale());
//	anagrafico.setPg_comune_nascita( getPg_comune_nascita());
	anagrafico.setPg_nazione_fiscale( getPg_nazione_fiscale());
//	anagrafico.setPg_nazione_nazionalita( getPg_nazione_nazionalita());
	anagrafico.setRagione_sociale( getRagione_sociale());
	anagrafico.setSede_inail( getSede_inail());
	anagrafico.setTi_entita( getTi_entita());
	anagrafico.setTi_entita_fisica( getTi_entita_fisica());
	anagrafico.setTi_entita_giuridica( getTi_entita_giuridica());
	anagrafico.setTi_italiano_estero( getTi_italiano_estero());
	anagrafico.setTi_sesso( getTi_sesso());
	anagrafico.setVia_fiscale( getVia_fiscale());

	
	return anagrafico;
}
public TerzoBulk getTerzo() {
	TerzoBulk terzo = new TerzoBulk();
	terzo.setCd_terzo( getCd_terzo());
	terzo.setCap_comune_sede( getCap_comune_sede());
	terzo.setCd_precedente( getCd_precedente());
	terzo.setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(getCd_unita_organizzativa()) );
	terzo.setDenominazione_sede( getDenominazione_sede());
	terzo.setDt_canc( getDt_canc());
	terzo.setDt_fine_rapporto( getDt_fine_rapporto());
	terzo.setFrazione_sede( getFrazione_sede());
	terzo.setNome_unita_organizzativa( getNome_unita_organizzativa());
	terzo.setNote( getNote());
	terzo.setNumero_civico_sede( getNumero_civico_sede());
//	terzo.setPg_comune_sede( getPg_comune_sede());   null pointer exception
//	terzo.setPg_rapp_legale( getPg_rapp_legale());
	terzo.setTi_terzo( getTi_terzo());
	terzo.setVia_sede( getVia_sede());

	terzo.setAnagrafico( getAnagrafico() );
	
	return terzo;
}
public java.util.Dictionary getTi_sessoKeys() {
	return AnagraficoBulk.SESSO;
}
}

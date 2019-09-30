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

package it.cnr.contab.pdg01.bulk;

/**
 * Classe di appoggio per la componente.
 * Permette di gestire in maniera paritaria {@link it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk } e {@link it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk }
 *
 */

public interface Pdg_modulo_gestBulk {
	//Origine del dettaglio
	/* 
	 *	Imputazione utente
	*/
	public static final String OR_UTENTE   = "DIR"; 
	/* 
	 * Dettaglio di previsione
	*/
	public static final String OR_PREVISIONE = "PRE"; 
	/* 
	 *	Proposta di variazione
	*/
	public static final String OR_PROPOSTA_VARIAZIONE = "PDV"; 
	/* 
	 *	Variazione approvata
	*/
	public static final String OR_APPROVATA = "APP"; 

	//Categoria di dettaglio
	/* 
	 * Diretta 
	 * 	  Previsione propria dell'Istituto, 
	 *    Previsione dell'Istituto per l'Area (prima del ribaltamento)
	 *    Previsione dell'Area scaricata dall'Istituto
	*/
	public static final String CAT_DIRETTA    = "DIR"; 
	/* 
	 * Scarico 
	 *    Previsione dell'Istituto per l'Area dopo il ribaltamento
	*/
	public static final String CAT_SCARICO    = "SCR";
	/* 
	 * Stipendi e simili
	*/
	public static final String CAT_STIPENDI = "STI"; 

	public void setEsercizio(java.lang.Integer esercizio);
	
	public java.lang.Integer getEsercizio();
		
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita);

	public java.lang.String getCd_centro_responsabilita ();
	
	public void setPg_progetto(java.lang.Integer pg_progetto);

	public java.lang.Integer getPg_progetto ();
	
	public void setId_classificazione(java.lang.Integer id_classificazione);

	public java.lang.Integer getId_classificazione ();

	public void setCd_cds_area(java.lang.String cd_cds_area);

	public java.lang.String getCd_cds_area ();

	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario);

	public java.lang.String getCd_cdr_assegnatario();

	public void setCd_linea_attivita(java.lang.String cd_linea_attivita);

	public java.lang.String getCd_linea_attivita ();

	public void setTi_appartenenza(java.lang.String ti_appartenenza);

	public java.lang.String getTi_appartenenza ();

	public void setTi_gestione(java.lang.String ti_gestione);

	public java.lang.String getTi_gestione ();

	public void setCd_elemento_voce(java.lang.String cd_elemento_voce);

	public java.lang.String getCd_elemento_voce ();

	public java.sql.Timestamp getDt_registrazione ();

	public void setDt_registrazione(java.sql.Timestamp dt_registrazione);

	public java.lang.String getDescrizione ();
	
	public void setDescrizione(java.lang.String descrizione);

	public java.lang.String getOrigine ();

	public void setOrigine(java.lang.String origine);

	public java.lang.String getCategoria_dettaglio ();

	public void setCategoria_dettaglio(java.lang.String categoria_dettaglio);

	public java.lang.Boolean getFl_sola_lettura ();

	public void setFl_sola_lettura(java.lang.Boolean fl_sola_lettura);

	public java.lang.Integer getEsercizio_pdg_variazione ();

	public void setEsercizio_pdg_variazione(java.lang.Integer esercizio_pdg_variazione);

	public java.lang.Long getPg_variazione_pdg ();

	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg);
}
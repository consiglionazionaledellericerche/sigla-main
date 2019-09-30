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

package it.cnr.contab.pdg00.bulk;

/**
 * Classe di appoggio per la componente {@link it.cnr.contab.pdg00.bulk.PdGComponent }.
 * Permette di gestire in maniera paritaria {@link it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk } e {@link it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk }
 *
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */

public interface Pdg_preventivo_detBulk {

	//Stati del dettaglio
	public final String ST_NESSUNA_AZIONE = "X";
	public final String ST_CONFERMA       = "Y";
	public final String ST_ANNULLA        = "N";

	//Origine del dettaglio
	public final String OR_UTENTE   = "DIR"; // Imputazione utente
	public final String OR_STIPENDI = "STI"; // Stipendi e simili
	public final String OR_PROPOSTA_VARIAZIONE = "PDV"; // Proposta di variazione

	//Categoria di dettaglio
	public final String CAT_SINGOLO    = "SIN"; // Singolo - non ha dettagli collegati
	public final String CAT_CARICO     = "CAR"; // Carico - dettaglio caricato sul centro servente da centro servito
	public final String CAT_SCARICO    = "SCR"; // Scarico - dettglio scaricato dal centro servito sul servente

	public final java.math.BigDecimal IM_ZERO = new java.math.BigDecimal("0.00");
void completaImportiNulli();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'categoria_dettaglio'
 *
 * @return Il valore della proprietà 'categoria_dettaglio'
 */
public java.lang.String getCategoria_dettaglio();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_centro_responsabilita'
 *
 * @return Il valore della proprietà 'cd_centro_responsabilita'
 */
public java.lang.String getCd_centro_responsabilita();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_centro_responsabilita_clgs'
 *
 * @return Il valore della proprietà 'cd_centro_responsabilita_clgs'
 */
public java.lang.String getCd_centro_responsabilita_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_elemento_voce'
 *
 * @return Il valore della proprietà 'cd_elemento_voce'
 */
public java.lang.String getCd_elemento_voce();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_elemento_voce_clgs'
 *
 * @return Il valore della proprietà 'cd_elemento_voce_clgs'
 */
public java.lang.String getCd_elemento_voce_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_linea_attivita'
 *
 * @return Il valore della proprietà 'cd_linea_attivita'
 */
public java.lang.String getCd_linea_attivita();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_linea_attivita_clgs'
 *
 * @return Il valore della proprietà 'cd_linea_attivita_clgs'
 */
public java.lang.String getCd_linea_attivita_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'centro_responsabilita'
 *
 * @return Il valore della proprietà 'centro_responsabilita'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'centro_responsabilita_clgs'
 *
 * @return Il valore della proprietà 'centro_responsabilita_clgs'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'descrizione'
 *
 * @return Il valore della proprietà 'descrizione'
 */
public java.lang.String getDescrizione();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dt_registrazione'
 *
 * @return Il valore della proprietà 'dt_registrazione'
 */
public java.sql.Timestamp getDt_registrazione();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elemento_voce'
 *
 * @return Il valore della proprietà 'elemento_voce'
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'elemento_voce_clgs'
 *
 * @return Il valore della proprietà 'elemento_voce_clgs'
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio'
 *
 * @return Il valore della proprietà 'esercizio'
 */
public java.lang.Integer getEsercizio();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'fl_sola_lettura'
 *
 * @return Il valore della proprietà 'fl_sola_lettura'
 */
public java.lang.Boolean getFl_sola_lettura();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'linea_attivita'
 *
 * @return Il valore della proprietà 'linea_attivita'
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'linea_attivita_clgs'
 *
 * @return Il valore della proprietà 'linea_attivita_clgs'
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'origine'
 *
 * @return Il valore della proprietà 'origine'
 */
public java.lang.String getOrigine();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'pg_spesa_clgs'
 *
 * @return Il valore della proprietà 'pg_spesa_clgs'
 */
public java.lang.Long getPg_spesa_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'stato'
 *
 * @return Il valore della proprietà 'stato'
 */
public java.lang.String getStato();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_appartenenza'
 *
 * @return Il valore della proprietà 'ti_appartenenza'
 */
public java.lang.String getTi_appartenenza();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_appartenenza_clgs'
 *
 * @return Il valore della proprietà 'ti_appartenenza_clgs'
 */
public java.lang.String getTi_appartenenza_clgs();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_gestione'
 *
 * @return Il valore della proprietà 'ti_gestione'
 */
public java.lang.String getTi_gestione();
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_gestione_clgs'
 *
 * @return Il valore della proprietà 'ti_gestione_clgs'
 */
public java.lang.String getTi_gestione_clgs();
boolean isOrigineDefinitivo();
boolean isOriginePropostaVariazione();
boolean isOrigineStipendi();
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'categoria_dettaglio'
 *
 * @param categoria_dettaglio	Il valore da assegnare a 'categoria_dettaglio'
 */
public void setCategoria_dettaglio(java.lang.String categoria_dettaglio);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_centro_responsabilita'
 *
 * @param cd_centro_responsabilita	Il valore da assegnare a 'cd_centro_responsabilita'
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_centro_responsabilita_clgs'
 *
 * @param cd_centro_responsabilita_clgs	Il valore da assegnare a 'cd_centro_responsabilita_clgs'
 */
public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_elemento_voce'
 *
 * @param cd_elemento_voce	Il valore da assegnare a 'cd_elemento_voce'
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_elemento_voce_clgs'
 *
 * @param cd_elemento_voce_clgs	Il valore da assegnare a 'cd_elemento_voce_clgs'
 */
public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_linea_attivita'
 *
 * @param cd_linea_attivita	Il valore da assegnare a 'cd_linea_attivita'
 */
public void setCd_linea_attivita(java.lang.String cd_linea_attivita);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_linea_attivita_clgs'
 *
 * @param cd_linea_attivita_clgs	Il valore da assegnare a 'cd_linea_attivita_clgs'
 */
public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'centro_responsabilita'
 *
 * @param cdrBulk	Il valore da assegnare a 'centro_responsabilita'
 */
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk cdrBulk);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'centro_responsabilita_clgs'
 *
 * @param newCentro_responsabilita_clgs	Il valore da assegnare a 'centro_responsabilita_clgs'
 */
public void setCentro_responsabilita_clgs(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'descrizione'
 *
 * @param descrizione	Il valore da assegnare a 'descrizione'
 */
public void setDescrizione(java.lang.String descrizione);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'dt_registrazione'
 *
 * @param timestamp	Il valore da assegnare a 'dt_registrazione'
 */
public void setDt_registrazione(java.sql.Timestamp timestamp);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elemento_voce'
 *
 * @param elemento_voceBulk	Il valore da assegnare a 'elemento_voce'
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voceBulk);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'elemento_voce_clgs'
 *
 * @param newElemento_voce_clgs	Il valore da assegnare a 'elemento_voce_clgs'
 */
public void setElemento_voce_clgs(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'esercizio'
 *
 * @param esercizio	Il valore da assegnare a 'esercizio'
 */
public void setEsercizio(java.lang.Integer esercizio);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'fl_sola_lettura'
 *
 * @param fl_sola_lettura	Il valore da assegnare a 'fl_sola_lettura'
 */
public void setFl_sola_lettura(java.lang.Boolean fl_sola_lettura);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'linea_attivita'
 *
 * @param linea_attivita	Il valore da assegnare a 'linea_attivita'
 */
public void setLinea_attivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'linea_attivita_clgs'
 *
 * @param newLinea_attivita_clgs	Il valore da assegnare a 'linea_attivita_clgs'
 */
public void setLinea_attivita_clgs(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'origine'
 *
 * @param origine	Il valore da assegnare a 'origine'
 */
public void setOrigine(java.lang.String origine);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'pg_spesa_clgs'
 *
 * @param pg_spesa_clgs	Il valore da assegnare a 'pg_spesa_clgs'
 */
public void setPg_spesa_clgs(java.lang.Long pg_spesa_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'stato'
 *
 * @param stato	Il valore da assegnare a 'stato'
 */
public void setStato(java.lang.String stato);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'ti_appartenenza'
 *
 * @param ti_appartenenza	Il valore da assegnare a 'ti_appartenenza'
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'ti_appartenenza_clgs'
 *
 * @param ti_appartenenza_clgs	Il valore da assegnare a 'ti_appartenenza_clgs'
 */
public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'ti_gestione'
 *
 * @param ti_gestione	Il valore da assegnare a 'ti_gestione'
 */
public void setTi_gestione(java.lang.String ti_gestione);
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'ti_gestione_clgs'
 *
 * @param ti_gestione_clgs	Il valore da assegnare a 'ti_gestione_clgs'
 */
public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs);
/**
 * Restituisce il valore della proprietà 'pdg_variazione'
 *
 * @return Il valore della proprietà 'pdg_variazione'
 */
public Pdg_variazioneBulk getPdg_variazione();
/**
 * Imposta il valore della proprietà 'pdg_variazione'
 *
 * @param bulk	Il valore da assegnare a 'pdg_variazione'
 */
public void setPdg_variazione(Pdg_variazioneBulk bulk);
/**
 * verifica se il dettaglio del PDG è da variazione o meno
 *
 */
public boolean isDaVariazione();

public java.lang.String getCd_funzione();

public void setCd_funzione(java.lang.String cd_funzione);
}

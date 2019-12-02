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
 * Insert the type's description here.
 * Creation date: (23/03/2004 16.47.52)
 * @author: Gennaro Borriello
 */
public class StampaRendFinCNRVBulk extends it.cnr.jada.bulk.OggettoBulk {

	
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds;

	private Integer esercizio;

	private Boolean stampaArticolo;
	
/**
 * StampaRendFinCNRVBulk constructor comment.
 */
public StampaRendFinCNRVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (25/03/2004 11.58.01)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCds() {
	return getCds()==null || getCds().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (25/03/2004 11.58.01)
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
/**
 * Valore dell'Esercizio utilizzato per la stampa
 * @return Integer
 */
public Integer getEsercizio() {
	return esercizio;
}
/**
 * Parametro da settare per passare il valore dell'Esercizio necessario per
 * la stampa
 * @param esercizio
 */
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
/**
 * Valore utilizzato dalla stampa per visualizzare i dettagli a livello di 
 * articolo o capitolo
 * @return Boolean
 */
public Boolean getStampaArticolo() {
	return stampaArticolo;
}
/**
 * Parametro da settare per far stampare i valori a livello di articolo 
 * @param stampaArticolo
 */
public void setStampaArticolo(Boolean stampaArticolo) {
	this.stampaArticolo = stampaArticolo;
}

}

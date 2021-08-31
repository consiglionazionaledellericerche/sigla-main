/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;

import java.util.Optional;

public class EvasioneOrdineRigaBulk extends EvasioneOrdineRigaBase {
	public MovimentiMagBulk getMovimentiMag() {
		return movimentiMag;
	}

	public void setMovimentiMag(MovimentiMagBulk movimentiMag) {
		this.movimentiMag = movimentiMag;
	}

	/**
	 * [EVASIONE_ORDINE Evasione ordine, con indicazione dei dati della bolla di consegna.]
	 **/
	private MovimentiMagBulk movimentiMag;
	private EvasioneOrdineBulk evasioneOrdine =  new EvasioneOrdineBulk();
	/**
	 * [ORDINE_ACQ_CONSEGNA Consegna Ordine d'Acquisto]
	 **/
	private OrdineAcqConsegnaBulk ordineAcqConsegna =  new OrdineAcqConsegnaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE_RIGA
	 **/
	public EvasioneOrdineRigaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE_RIGA
	 **/
	public EvasioneOrdineRigaBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Long numero, java.lang.Long riga) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, numero, riga);
		setEvasioneOrdine( new EvasioneOrdineBulk(cdCds,cdMagazzino,esercizio,cdNumeratoreMag,numero) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Evasione ordine, con indicazione dei dati della bolla di consegna.]
	 **/
	public EvasioneOrdineBulk getEvasioneOrdine() {
		return evasioneOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Evasione ordine, con indicazione dei dati della bolla di consegna.]
	 **/
	public void setEvasioneOrdine(EvasioneOrdineBulk evasioneOrdine)  {
		this.evasioneOrdine=evasioneOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Consegna Ordine d'Acquisto]
	 **/
	public OrdineAcqConsegnaBulk getOrdineAcqConsegna() {
		return ordineAcqConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Consegna Ordine d'Acquisto]
	 **/
	public void setOrdineAcqConsegna(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegna=ordineAcqConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return Optional.ofNullable(this.getEvasioneOrdine())
				.map(EvasioneOrdineBulk::getCdCds)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getEvasioneOrdine().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return Optional.ofNullable(this.getEvasioneOrdine())
				.map(EvasioneOrdineBulk::getCdMagazzino)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getEvasioneOrdine().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return Optional.ofNullable(this.getEvasioneOrdine())
				.map(EvasioneOrdineBulk::getEsercizio)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getEvasioneOrdine().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public java.lang.String getCdNumeratoreMag() {
		return Optional.ofNullable(this.getEvasioneOrdine())
				.map(EvasioneOrdineBulk::getCdNumeratoreMag)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getEvasioneOrdine().setCdNumeratoreMag(cdNumeratoreMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public java.lang.Long getNumero() {
		return Optional.ofNullable(this.getEvasioneOrdine())
				.map(EvasioneOrdineBulk::getNumero)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public void setNumero(java.lang.Long numero)  {
		this.getEvasioneOrdine().setNumero(numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getCdCds)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getCdUnitaOperativa)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getEsercizio)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public java.lang.String getCdNumeratoreOrdine() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getCdNumeratore)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public java.lang.Integer getNumeroOrdine() {
        return Optional.ofNullable(this.getOrdineAcqConsegna())
                .map(OrdineAcqConsegnaBulk::getNumero)
                .orElse(null);
    }
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public java.lang.Integer getRigaOrdine() {
        return Optional.ofNullable(this.getOrdineAcqConsegna())
                .map(OrdineAcqConsegnaBulk::getRiga)
                .orElse(null);
    }
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
        return Optional.ofNullable(this.getOrdineAcqConsegna())
                .map(OrdineAcqConsegnaBulk::getConsegna)
                .orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.getOrdineAcqConsegna().setConsegna(consegna);
	}
}
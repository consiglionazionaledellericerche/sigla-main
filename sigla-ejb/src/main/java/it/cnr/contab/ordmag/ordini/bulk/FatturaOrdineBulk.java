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
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

public class FatturaOrdineBulk extends FatturaOrdineBase {
	/**
	 * [FATTURA_PASSIVA_RIGA Rappresenta le righe di dettaglio della fattura. Ogni fattura ha sempre almeno una riga di dettaglio]
	 **/
	private Fattura_passiva_rigaBulk fatturaPassivaRiga;
	/**
	 * [ORDINE_ACQ_CONSEGNA Consegna Ordine d'Acquisto]
	 **/
	private OrdineAcqConsegnaBulk ordineAcqConsegna = new OrdineAcqConsegnaBulk();
	/**
	 * [VOCE_IVA La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entitè  si riferisce alla normativa vigente sull'iva.]
	 **/
	private Voce_ivaBulk voceIva =  new Voce_ivaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ORDINE
	 **/
	public FatturaOrdineBulk() {
		super();
	}
	BigDecimal imponibilePerNotaCredito;
	BigDecimal importoIvaPerNotaCredito;
	BigDecimal totaleConsegnaPerNotaCredito;

	public BigDecimal getTotaleConsegnaPerNotaCredito() {
		return totaleConsegnaPerNotaCredito;
	}

	public void setTotaleConsegnaPerNotaCredito(BigDecimal totaleConsegnaPerNotaCredito) {
		this.totaleConsegnaPerNotaCredito = totaleConsegnaPerNotaCredito;
	}

	public BigDecimal getImponibilePerNotaCredito() {
		return imponibilePerNotaCredito;
	}

	public void setImponibilePerNotaCredito(BigDecimal imponibilePerNotaCredito) {
		this.imponibilePerNotaCredito = imponibilePerNotaCredito;
	}

	public BigDecimal getImportoIvaPerNotaCredito() {
		return importoIvaPerNotaCredito;
	}

	public void setImportoIvaPerNotaCredito(BigDecimal importoIvaPerNotaCredito) {
		this.importoIvaPerNotaCredito = importoIvaPerNotaCredito;
	}

	public BigDecimal getImportoIvaDetraibilePerNotaCredito() {
		return importoIvaDetraibilePerNotaCredito;
	}

	public void setImportoIvaDetraibilePerNotaCredito(BigDecimal importoIvaDetraibilePerNotaCredito) {
		this.importoIvaDetraibilePerNotaCredito = importoIvaDetraibilePerNotaCredito;
	}

	public BigDecimal getImportoIvaIndPerNotaCredito() {
		return importoIvaIndPerNotaCredito;
	}

	public void setImportoIvaIndPerNotaCredito(BigDecimal importoIvaIndPerNotaCredito) {
		this.importoIvaIndPerNotaCredito = importoIvaIndPerNotaCredito;
	}

	BigDecimal importoIvaDetraibilePerNotaCredito;
	BigDecimal importoIvaIndPerNotaCredito;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ORDINE
	 **/
	public FatturaOrdineBulk(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaPassiva, java.lang.Long progressivoRiga, java.lang.String cdCdsOrdine, java.lang.String cdUnitaOperativa, java.lang.Integer esercizioOrdine, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOrganizzativa, esercizio, pgFatturaPassiva, progressivoRiga, cdCdsOrdine, cdUnitaOperativa, esercizioOrdine, cdNumeratore, numero, riga, consegna);
		setFatturaPassivaRiga( new Fattura_passiva_rigaIBulk(cdCds,cdUnitaOrganizzativa,esercizio,pgFatturaPassiva,progressivoRiga) );
		setOrdineAcqConsegna( new OrdineAcqConsegnaBulk(cdCdsOrdine,cdUnitaOperativa,esercizioOrdine,cdNumeratore,numero,riga,consegna) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta le righe di dettaglio della fattura. Ogni fattura ha sempre almeno una riga di dettaglio]
	 **/
	public Fattura_passiva_rigaBulk getFatturaPassivaRiga() {
		return Optional.ofNullable(fatturaPassivaRiga)
				.orElseGet(() -> {
					return new Fattura_passiva_rigaIBulk();
				});
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le righe di dettaglio della fattura. Ogni fattura ha sempre almeno una riga di dettaglio]
	 **/
	public void setFatturaPassivaRiga(Fattura_passiva_rigaBulk fatturaPassivaRiga)  {
		this.fatturaPassivaRiga=fatturaPassivaRiga;
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
	 * Restituisce il valore di: [La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entitè  si riferisce alla normativa vigente sull'iva.]
	 **/
	public Voce_ivaBulk getVoceIva() {
		return voceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [La tabella iva contiene i codici e le aliquote dell'iva, commerciale o istituzionale, registrata nei dettagli della fattura attiva e passiva. Questa entitè  si riferisce alla normativa vigente sull'iva.]
	 **/
	public void setVoceIva(Voce_ivaBulk voceIva)  {
		this.voceIva=voceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return Optional.ofNullable(this.getFatturaPassivaRiga())
				.map(Fattura_passiva_rigaBulk::getCd_cds)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getFatturaPassivaRiga().setCd_cds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return Optional.ofNullable(this.getFatturaPassivaRiga())
				.map(Fattura_passiva_rigaBulk::getCd_unita_organizzativa)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.getFatturaPassivaRiga().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return Optional.ofNullable(this.getFatturaPassivaRiga())
				.map(Fattura_passiva_rigaBulk::getEsercizio)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getFatturaPassivaRiga().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgFatturaPassiva]
	 **/
	public java.lang.Long getPgFatturaPassiva() {
		return Optional.ofNullable(this.getFatturaPassivaRiga())
				.map(Fattura_passiva_rigaBulk::getPg_fattura_passiva)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgFatturaPassiva]
	 **/
	public void setPgFatturaPassiva(java.lang.Long pgFatturaPassiva)  {
		this.getFatturaPassivaRiga().setPg_fattura_passiva(pgFatturaPassiva);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivoRiga]
	 **/
	public java.lang.Long getProgressivoRiga() {
		return Optional.ofNullable(this.getFatturaPassivaRiga())
				.map(Fattura_passiva_rigaBulk::getProgressivo_riga)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivoRiga]
	 **/
	public void setProgressivoRiga(java.lang.Long progressivoRiga)  {
		this.getFatturaPassivaRiga().setProgressivo_riga(progressivoRiga);
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
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getCdNumeratore)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public java.lang.Integer getNumero() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getNumero)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public void setNumero(java.lang.Integer numero)  {
		this.getOrdineAcqConsegna().setNumero(numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riga]
	 **/
	public java.lang.Integer getRiga() {
		return Optional.ofNullable(this.getOrdineAcqConsegna())
				.map(OrdineAcqConsegnaBulk::getRiga)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riga]
	 **/
	public void setRiga(java.lang.Integer riga)  {
		this.getOrdineAcqConsegna().setRiga(riga);
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIvaRett]
	 **/
	public java.lang.String getCdVoceIvaRett() {
		return Optional.ofNullable(this.getVoceIva())
				.map(Voce_ivaBulk::getCd_voce_iva)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIvaRett]
	 **/
	public void setCdVoceIvaRett(java.lang.String cdVoceIvaRett)  {
		this.getVoceIva().setCd_voce_iva(cdVoceIvaRett);
	}

	public String getImpegnoDisplay() {
		return Optional.ofNullable(ordineAcqConsegna)
				.map(OrdineAcqConsegnaBulk::getObbligazioneScadenzario)
				.map(obbligazione_scadenzarioBulk -> {
					return String.valueOf(obbligazione_scadenzarioBulk.getEsercizio())
							.concat("/")
							.concat(obbligazione_scadenzarioBulk.getCd_cds())
							.concat("/")
							.concat(String.valueOf(obbligazione_scadenzarioBulk.getPg_obbligazione()));
				})
				.orElse(null);
	}

	public String getCssClassNotaRiga() {
		return "table-cell-ellipsis";
	}


	public void calcolaRettifiche() {
		final BigDecimal prezzoUnitario = Optional.ofNullable(getPrezzoUnitarioRett())
				.orElseGet(() -> getOrdineAcqConsegna().getOrdineAcqRiga().getPrezzoUnitario());
        final BigDecimal percentualeIva = Optional.ofNullable(getVoceIva())
                .filter(voce_ivaBulk -> Optional.ofNullable(voce_ivaBulk.getPercentuale()).isPresent())
                .map(voce_ivaBulk -> voce_ivaBulk.getPercentuale())
                .orElseGet(() -> getOrdineAcqConsegna().getOrdineAcqRiga().getVoce_iva().getPercentuale());
        setImImponibile(
                prezzoUnitario.multiply(getOrdineAcqConsegna().getQuantita()).setScale(2, RoundingMode.HALF_UP)
        );
        setImIva(getImImponibile().multiply(percentualeIva)
				.divide(BigDecimal.TEN.multiply(BigDecimal.TEN)).setScale(2, RoundingMode.HALF_UP));
        setImTotaleConsegna(getImImponibile().add(getImIva()));
	}
	public BigDecimal getImponibilePerRigaFattura() {
		if (getImponibilePerNotaCredito() != null && getImponibilePerNotaCredito().compareTo(BigDecimal.ZERO) > 0){
			return getImponibilePerNotaCredito();
		}
		return getImImponibile();
	}
	public BigDecimal getIvaPerRigaFattura() {
		if (getImportoIvaPerNotaCredito() != null && getImportoIvaPerNotaCredito().compareTo(BigDecimal.ZERO) > 0){
			return getImportoIvaPerNotaCredito();
		}
		return getImIva();
	}
}
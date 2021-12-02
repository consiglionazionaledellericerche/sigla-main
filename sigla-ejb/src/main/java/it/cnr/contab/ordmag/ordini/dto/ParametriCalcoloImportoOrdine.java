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

package it.cnr.contab.ordmag.ordini.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;

public class ParametriCalcoloImportoOrdine implements Cloneable, Serializable{
	private static final long serialVersionUID = 672612150974377581L;
	
	BigDecimal percProrata;
	BigDecimal prezzo;

	public BigDecimal getImponibileErratoPerNotaCredito() {
		return imponibileErratoPerNotaCredito;
	}

	public void setImponibileErratoPerNotaCredito(BigDecimal imponibileErratoPerNotaCredito) {
		this.imponibileErratoPerNotaCredito = imponibileErratoPerNotaCredito;
	}

	BigDecimal qtaOrd;
	BigDecimal coefacq;
	Voce_ivaBulk voceIva;
    DivisaBulk divisa;
    DivisaBulk divisaRisultato;
    BigDecimal cambio;
    BigDecimal sconto1;
    BigDecimal sconto2;
    BigDecimal sconto3;
    BigDecimal prezzoRet; 
    Voce_ivaBulk voceIvaRet;
    BigDecimal sconto1Ret;
    BigDecimal sconto2Ret; 
    BigDecimal sconto3Ret;
    BigDecimal cambioRet; 
    BigDecimal arrAliIva;
	BigDecimal imponibileErratoPerNotaCredito;

	public Voce_ivaBulk getVoceIva() {
		return voceIva;
	}

	public void setVoceIva(Voce_ivaBulk voceIva) {
		this.voceIva = voceIva;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	public BigDecimal getQtaOrd() {
		return qtaOrd;
	}

	public void setQtaOrd(BigDecimal qtaOrd) {
		this.qtaOrd = qtaOrd;
	}

	public BigDecimal getCoefacq() {
		return coefacq;
	}

	public void setCoefacq(BigDecimal coefacq) {
		this.coefacq = coefacq;
	}

	public BigDecimal getCambio() {
		return cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	public BigDecimal getSconto1() {
		return sconto1;
	}

	public void setSconto1(BigDecimal sconto1) {
		this.sconto1 = sconto1;
	}

	public BigDecimal getSconto2() {
		return sconto2;
	}

	public void setSconto2(BigDecimal sconto2) {
		this.sconto2 = sconto2;
	}

	public BigDecimal getSconto3() {
		return sconto3;
	}

	
	
	public void setSconto3(BigDecimal sconto3) {
		this.sconto3 = sconto3;
	}

	public BigDecimal getPrezzoRet() {
		return prezzoRet;
	}

	public void setPrezzoRet(BigDecimal prezzoRet) {
		this.prezzoRet = prezzoRet;
	}

	public BigDecimal getSconto1Ret() {
		return sconto1Ret;
	}

	public void setSconto1Ret(BigDecimal sconto1Ret) {
		this.sconto1Ret = sconto1Ret;
	}

	public BigDecimal getSconto2Ret() {
		return sconto2Ret;
	}

	public void setSconto2Ret(BigDecimal sconto2Ret) {
		this.sconto2Ret = sconto2Ret;
	}

	public BigDecimal getSconto3Ret() {
		return sconto3Ret;
	}

	public void setSconto3Ret(BigDecimal sconto3Ret) {
		this.sconto3Ret = sconto3Ret;
	}

	public BigDecimal getCambioRet() {
		return cambioRet;
	}

	public void setCambioRet(BigDecimal cambioRet) {
		this.cambioRet = cambioRet;
	}

	public BigDecimal getArrAliIva() {
		return arrAliIva;
	}

	public void setArrAliIva(BigDecimal arrAliIva) {
		this.arrAliIva = arrAliIva;
	}

	public Voce_ivaBulk getVoceIvaRet() {
		return voceIvaRet;
	}

	public void setVoceIvaRet(Voce_ivaBulk voceIvaRet) {
		this.voceIvaRet = voceIvaRet;
	}

	public BigDecimal getPercProrata() {
		return percProrata;
	}

	public void setPercProrata(BigDecimal percProrata) {
		this.percProrata = percProrata;
	}

	public DivisaBulk getDivisaRisultato() {
		return divisaRisultato;
	}

	public void setDivisaRisultato(DivisaBulk divisaRisultato) {
		this.divisaRisultato = divisaRisultato;
	}

	public DivisaBulk getDivisa() {
		return divisa;
	}

	public void setDivisa(DivisaBulk divisa) {
		this.divisa = divisa;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}
	}
}

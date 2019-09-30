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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/11/2013
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class VSpesometroBase extends VSpesometroKey implements Keyed {
//    QUADRO CHAR(2)
	private java.lang.String quadro;
 
//    TIPO VARCHAR(7)
	private java.lang.String tipo;
 
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Integer esercizio;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    TI_BENE_SERVIZIO VARCHAR(1)
	private java.lang.String tiBeneServizio;
 
//    TIPO_FISCALITA VARCHAR(2)
	private java.lang.String tipoFiscalita;
 
//    MESE DECIMAL(22,0)
	private java.lang.Integer mese;
 
//    NR_ATTIVE DECIMAL(22,0)
	private java.lang.Integer nrAttive;
 
//    NR_PASSIVE DECIMAL(22,0)
	private java.lang.Integer nrPassive;
 
//    IMPONIBILE_FA DECIMAL(22,0)
	private java.math.BigDecimal imponibileFa;
 
//    IVA_FA DECIMAL(22,0)
	private java.math.BigDecimal ivaFa;
 
//    IMPONIBILE_ND_FA DECIMAL(22,0)
	private java.math.BigDecimal imponibileNdFa;
 
//    IVA_ND_FA DECIMAL(22,0)
	private java.math.BigDecimal ivaNdFa;
 
//    IMPONIBILE_FP DECIMAL(22,0)
	private java.math.BigDecimal imponibileFp;
 
//    IVA_FP DECIMAL(22,0)
	private java.math.BigDecimal ivaFp;
 
//    IMPONIBILE_ND_FP DECIMAL(22,0)
	private java.math.BigDecimal imponibileNdFp;
 
//    IVA_ND_FP DECIMAL(22,0)
	private java.math.BigDecimal ivaNdFp;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    DT_NASCITA TIMESTAMP(7)
	private java.sql.Timestamp dtNascita;
 
//    STATO_NASCITA VARCHAR(100)
	private java.lang.String statoNascita;
 
//    PROVINCIA VARCHAR(10)
	private java.lang.String provincia;
 
//    CODICE_STATO_ESTERO VARCHAR(10)
	private java.lang.String codiceStatoEstero;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragioneSociale;
 
//    INDIRIZZO_SEDE VARCHAR(106)
	private java.lang.String indirizzoSede;
 
//    COMUNE_SEDE VARCHAR(100)
	private java.lang.String comuneSede;
 
//    PROG DECIMAL(22,0)
	private java.lang.Long prog;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SPESOMETRO
	 **/
	
	public VSpesometroBase() {
		super();
	}
	public VSpesometroBase(java.lang.Long pg) {
		super(pg);
	}
	public java.lang.String getQuadro() {
		return quadro;
	}

	public void setQuadro(java.lang.String quadro) {
		this.quadro = quadro;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(java.lang.String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public java.lang.String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(java.lang.String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public java.lang.String getTiBeneServizio() {
		return tiBeneServizio;
	}

	public void setTiBeneServizio(java.lang.String tiBeneServizio) {
		this.tiBeneServizio = tiBeneServizio;
	}

	public java.lang.String getTipoFiscalita() {
		return tipoFiscalita;
	}

	public void setTipoFiscalita(java.lang.String tipoFiscalita) {
		this.tipoFiscalita = tipoFiscalita;
	}

	public java.lang.Integer getMese() {
		return mese;
	}

	public void setMese(java.lang.Integer mese) {
		this.mese = mese;
	}

	public java.lang.Integer getNrAttive() {
		return nrAttive;
	}

	public void setNrAttive(java.lang.Integer nrAttive) {
		this.nrAttive = nrAttive;
	}

	public java.lang.Integer getNrPassive() {
		return nrPassive;
	}

	public void setNrPassive(java.lang.Integer nrPassive) {
		this.nrPassive = nrPassive;
	}
 
	public java.math.BigDecimal getImponibileFa() {
		return imponibileFa;
	}

	public void setImponibileFa(java.math.BigDecimal imponibileFa) {
		this.imponibileFa = imponibileFa;
	}

	public java.math.BigDecimal getIvaFa() {
		return ivaFa;
	}

	public void setIvaFa(java.math.BigDecimal ivaFa) {
		this.ivaFa = ivaFa;
	}

	public java.math.BigDecimal getImponibileNdFa() {
		return imponibileNdFa;
	}

	public void setImponibileNdFa(java.math.BigDecimal imponibileNdFa) {
		this.imponibileNdFa = imponibileNdFa;
	}

	public java.math.BigDecimal getIvaNdFa() {
		return ivaNdFa;
	}

	public void setIvaNdFa(java.math.BigDecimal ivaNdFa) {
		this.ivaNdFa = ivaNdFa;
	}

	public java.math.BigDecimal getImponibileFp() {
		return imponibileFp;
	}

	public void setImponibileFp(java.math.BigDecimal imponibileFp) {
		this.imponibileFp = imponibileFp;
	}

	public java.math.BigDecimal getIvaFp() {
		return ivaFp;
	}

	public void setIvaFp(java.math.BigDecimal ivaFp) {
		this.ivaFp = ivaFp;
	}

	public java.math.BigDecimal getImponibileNdFp() {
		return imponibileNdFp;
	}

	public void setImponibileNdFp(java.math.BigDecimal imponibileNdFp) {
		this.imponibileNdFp = imponibileNdFp;
	}

	public java.math.BigDecimal getIvaNdFp() {
		return ivaNdFp;
	}

	public void setIvaNdFp(java.math.BigDecimal ivaNdFp) {
		this.ivaNdFp = ivaNdFp;
	}

	public java.lang.String getCognome() {
		return cognome;
	}

	public void setCognome(java.lang.String cognome) {
		this.cognome = cognome;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.sql.Timestamp getDtNascita() {
		return dtNascita;
	}

	public void setDtNascita(java.sql.Timestamp dtNascita) {
		this.dtNascita = dtNascita;
	}

	public java.lang.String getStatoNascita() {
		return statoNascita;
	}

	public void setStatoNascita(java.lang.String statoNascita) {
		this.statoNascita = statoNascita;
	}

	public java.lang.String getProvincia() {
		return provincia;
	}

	public void setProvincia(java.lang.String provincia) {
		this.provincia = provincia;
	}

	public java.lang.String getCodiceStatoEstero() {
		return codiceStatoEstero;
	}

	public void setCodiceStatoEstero(java.lang.String codiceStatoEstero) {
		this.codiceStatoEstero = codiceStatoEstero;
	}

	public java.lang.String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(java.lang.String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public java.lang.String getIndirizzoSede() {
		return indirizzoSede;
	}

	public void setIndirizzoSede(java.lang.String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}

	public java.lang.String getComuneSede() {
		return comuneSede;
	}

	public void setComuneSede(java.lang.String comuneSede) {
		this.comuneSede = comuneSede;
	}

	public java.lang.Long getProg() {
		return prog;
	}

	public void setProg(java.lang.Long prog) {
		this.prog = prog;
	}
	
}
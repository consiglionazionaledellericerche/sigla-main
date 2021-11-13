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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class MagazzinoBase extends MagazzinoKey implements Keyed {
//    DS_MAGAZZINO VARCHAR(100) NOT NULL
	private String dsMagazzino;
 
//    CD_UNITA_OPERATIVA VARCHAR(12)
	private String cdUnitaOperativa;
 
//    CD_CDS_LUOGO VARCHAR(30) NOT NULL
	private String cdCdsLuogo;
 
//    CD_LUOGO_CONSEGNA VARCHAR(10) NOT NULL
	private String cdLuogoConsegna;
 
//    CD_CDS_CAR_MAG VARCHAR(30) NOT NULL
	private String cdCdsCarMag;
 
//    CD_TIPO_MOVIMENTO_CAR_MAG VARCHAR(3) NOT NULL
	private String cdTipoMovimentoCarMag;
 
//    CD_CDS_CAR_TRA VARCHAR(30) NOT NULL
	private String cdCdsCarTra;
 
//    CD_TIPO_MOVIMENTO_CAR_TRA VARCHAR(3) NOT NULL
	private String cdTipoMovimentoCarTra;
 
//    CD_CDS_CAR_FMA VARCHAR(30) NOT NULL
	private String cdCdsCarFma;
 
//    CD_TIPO_MOVIMENTO_CAR_FMA VARCHAR(3) NOT NULL
	private String cdTipoMovimentoCarFma;
 
//    CD_CDS_SCA_UO VARCHAR(30) NOT NULL
	private String cdCdsScaUo;
 
//    CD_TIPO_MOVIMENTO_SCA_UO VARCHAR(3) NOT NULL
	private String cdTipoMovimentoScaUo;
 
//    CD_CDS_TRA_SCA VARCHAR(30) NOT NULL
	private String cdCdsTraSca;
 
//    CD_TIPO_MOVIMENTO_TRA_SCA VARCHAR(3) NOT NULL
	private String cdTipoMovimentoTraSca;
 
//    CD_CDS_TRA_CAR VARCHAR(30) NOT NULL
	private String cdCdsTraCar;
 
//    CD_TIPO_MOVIMENTO_TRA_CAR VARCHAR(3) NOT NULL
	private String cdTipoMovimentoTraCar;
 
//    CD_CDS_RV_POS VARCHAR(30) NOT NULL
	private String cdCdsRvPos;
 
//    CD_TIPO_MOVIMENTO_RV_POS VARCHAR(3) NOT NULL
	private String cdTipoMovimentoRvPos;
 
//    CD_CDS_RV_NEG VARCHAR(30) NOT NULL
	private String cdCdsRvNeg;
 
//    CD_TIPO_MOVIMENTO_RV_NEG VARCHAR(3) NOT NULL
	private String cdTipoMovimentoRvNeg;
 
//    CD_CDS_CHI VARCHAR(30) NOT NULL
	private String cdCdsChi;
 
//    CD_TIPO_MOVIMENTO_CHI VARCHAR(3) NOT NULL
	private String cdTipoMovimentoChi;
 
//    DT_ULT_VAL_SCA TIMESTAMP(7)
	private java.sql.Timestamp dtUltValSca;
 
//    ESERCIZIO_VAL_SCA DECIMAL(4,0)
	private Integer esercizioValSca;
 
//    CD_CDS_RAGGR_SCA VARCHAR(30)
	private String cdCdsRaggrSca;
 
//    CD_RAGGR_MAGAZZINO_SCA VARCHAR(3)
	private String cdRaggrMagazzinoSca;
 
//    METODO_VAL_SCA VARCHAR(1)
	private String metodoValSca;
 
//    DT_ULT_VAL_RIM TIMESTAMP(7)
	private java.sql.Timestamp dtUltValRim;

	public String getTipoGestione() {
		return tipoGestione;
	}

	public void setTipoGestione(String tipoGestione) {
		this.tipoGestione = tipoGestione;
	}

	//    ESERCIZIO_VAL_RIM DECIMAL(4,0)
	private Integer esercizioValRim;
 
//    CD_CDS_RAGGR_RIM VARCHAR(30)
	private String cdCdsRaggrRim;
 
//    CD_RAGGR_MAGAZZINO_RIM VARCHAR(3)
	private String cdRaggrMagazzinoRim;
 
//    METODO_VAL_RIM VARCHAR(1)
	private String metodoValRim;
 
//    ABIL_TUTTI_BENI_SERV VARCHAR(1) NOT NULL
	private Boolean abilTuttiBeniServ;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;

	private java.lang.String tipoGestione;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MAGAZZINO
	 **/
	public MagazzinoBase() {
		super();
	}
	public MagazzinoBase(String cdCds, String cdMagazzino) {
		super(cdCds, cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsMagazzino]
	 **/
	public String getDsMagazzino() {
		return dsMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsMagazzino]
	 **/
	public void setDsMagazzino(String dsMagazzino)  {
		this.dsMagazzino=dsMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsLuogo]
	 **/
	public String getCdCdsLuogo() {
		return cdCdsLuogo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsLuogo]
	 **/
	public void setCdCdsLuogo(String cdCdsLuogo)  {
		this.cdCdsLuogo=cdCdsLuogo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLuogoConsegna]
	 **/
	public String getCdLuogoConsegna() {
		return cdLuogoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLuogoConsegna]
	 **/
	public void setCdLuogoConsegna(String cdLuogoConsegna)  {
		this.cdLuogoConsegna=cdLuogoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarMag]
	 **/
	public String getCdCdsCarMag() {
		return cdCdsCarMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarMag]
	 **/
	public void setCdCdsCarMag(String cdCdsCarMag)  {
		this.cdCdsCarMag=cdCdsCarMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarMag]
	 **/
	public String getCdTipoMovimentoCarMag() {
		return cdTipoMovimentoCarMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarMag]
	 **/
	public void setCdTipoMovimentoCarMag(String cdTipoMovimentoCarMag)  {
		this.cdTipoMovimentoCarMag=cdTipoMovimentoCarMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarTra]
	 **/
	public String getCdCdsCarTra() {
		return cdCdsCarTra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarTra]
	 **/
	public void setCdCdsCarTra(String cdCdsCarTra)  {
		this.cdCdsCarTra=cdCdsCarTra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarTra]
	 **/
	public String getCdTipoMovimentoCarTra() {
		return cdTipoMovimentoCarTra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarTra]
	 **/
	public void setCdTipoMovimentoCarTra(String cdTipoMovimentoCarTra)  {
		this.cdTipoMovimentoCarTra=cdTipoMovimentoCarTra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsCarFma]
	 **/
	public String getCdCdsCarFma() {
		return cdCdsCarFma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsCarFma]
	 **/
	public void setCdCdsCarFma(String cdCdsCarFma)  {
		this.cdCdsCarFma=cdCdsCarFma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoCarFma]
	 **/
	public String getCdTipoMovimentoCarFma() {
		return cdTipoMovimentoCarFma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoCarFma]
	 **/
	public void setCdTipoMovimentoCarFma(String cdTipoMovimentoCarFma)  {
		this.cdTipoMovimentoCarFma=cdTipoMovimentoCarFma;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsScaUo]
	 **/
	public String getCdCdsScaUo() {
		return cdCdsScaUo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsScaUo]
	 **/
	public void setCdCdsScaUo(String cdCdsScaUo)  {
		this.cdCdsScaUo=cdCdsScaUo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoScaUo]
	 **/
	public String getCdTipoMovimentoScaUo() {
		return cdTipoMovimentoScaUo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoScaUo]
	 **/
	public void setCdTipoMovimentoScaUo(String cdTipoMovimentoScaUo)  {
		this.cdTipoMovimentoScaUo=cdTipoMovimentoScaUo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTraSca]
	 **/
	public String getCdCdsTraSca() {
		return cdCdsTraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTraSca]
	 **/
	public void setCdCdsTraSca(String cdCdsTraSca)  {
		this.cdCdsTraSca=cdCdsTraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoTraSca]
	 **/
	public String getCdTipoMovimentoTraSca() {
		return cdTipoMovimentoTraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoTraSca]
	 **/
	public void setCdTipoMovimentoTraSca(String cdTipoMovimentoTraSca)  {
		this.cdTipoMovimentoTraSca=cdTipoMovimentoTraSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTraCar]
	 **/
	public String getCdCdsTraCar() {
		return cdCdsTraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTraCar]
	 **/
	public void setCdCdsTraCar(String cdCdsTraCar)  {
		this.cdCdsTraCar=cdCdsTraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoTraCar]
	 **/
	public String getCdTipoMovimentoTraCar() {
		return cdTipoMovimentoTraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoTraCar]
	 **/
	public void setCdTipoMovimentoTraCar(String cdTipoMovimentoTraCar)  {
		this.cdTipoMovimentoTraCar=cdTipoMovimentoTraCar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRvPos]
	 **/
	public String getCdCdsRvPos() {
		return cdCdsRvPos;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRvPos]
	 **/
	public void setCdCdsRvPos(String cdCdsRvPos)  {
		this.cdCdsRvPos=cdCdsRvPos;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRvPos]
	 **/
	public String getCdTipoMovimentoRvPos() {
		return cdTipoMovimentoRvPos;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRvPos]
	 **/
	public void setCdTipoMovimentoRvPos(String cdTipoMovimentoRvPos)  {
		this.cdTipoMovimentoRvPos=cdTipoMovimentoRvPos;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRvNeg]
	 **/
	public String getCdCdsRvNeg() {
		return cdCdsRvNeg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRvNeg]
	 **/
	public void setCdCdsRvNeg(String cdCdsRvNeg)  {
		this.cdCdsRvNeg=cdCdsRvNeg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRvNeg]
	 **/
	public String getCdTipoMovimentoRvNeg() {
		return cdTipoMovimentoRvNeg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRvNeg]
	 **/
	public void setCdTipoMovimentoRvNeg(String cdTipoMovimentoRvNeg)  {
		this.cdTipoMovimentoRvNeg=cdTipoMovimentoRvNeg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsChi]
	 **/
	public String getCdCdsChi() {
		return cdCdsChi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsChi]
	 **/
	public void setCdCdsChi(String cdCdsChi)  {
		this.cdCdsChi=cdCdsChi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoChi]
	 **/
	public String getCdTipoMovimentoChi() {
		return cdTipoMovimentoChi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoChi]
	 **/
	public void setCdTipoMovimentoChi(String cdTipoMovimentoChi)  {
		this.cdTipoMovimentoChi=cdTipoMovimentoChi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtUltValSca]
	 **/
	public java.sql.Timestamp getDtUltValSca() {
		return dtUltValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtUltValSca]
	 **/
	public void setDtUltValSca(java.sql.Timestamp dtUltValSca)  {
		this.dtUltValSca=dtUltValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioValSca]
	 **/
	public Integer getEsercizioValSca() {
		return esercizioValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioValSca]
	 **/
	public void setEsercizioValSca(Integer esercizioValSca)  {
		this.esercizioValSca=esercizioValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRaggrSca]
	 **/
	public String getCdCdsRaggrSca() {
		return cdCdsRaggrSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRaggrSca]
	 **/
	public void setCdCdsRaggrSca(String cdCdsRaggrSca)  {
		this.cdCdsRaggrSca=cdCdsRaggrSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdRaggrMagazzinoSca]
	 **/
	public String getCdRaggrMagazzinoSca() {
		return cdRaggrMagazzinoSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdRaggrMagazzinoSca]
	 **/
	public void setCdRaggrMagazzinoSca(String cdRaggrMagazzinoSca)  {
		this.cdRaggrMagazzinoSca=cdRaggrMagazzinoSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [metodoValSca]
	 **/
	public String getMetodoValSca() {
		return metodoValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [metodoValSca]
	 **/
	public void setMetodoValSca(String metodoValSca)  {
		this.metodoValSca=metodoValSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtUltValRim]
	 **/
	public java.sql.Timestamp getDtUltValRim() {
		return dtUltValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtUltValRim]
	 **/
	public void setDtUltValRim(java.sql.Timestamp dtUltValRim)  {
		this.dtUltValRim=dtUltValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioValRim]
	 **/
	public Integer getEsercizioValRim() {
		return esercizioValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioValRim]
	 **/
	public void setEsercizioValRim(Integer esercizioValRim)  {
		this.esercizioValRim=esercizioValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRaggrRim]
	 **/
	public String getCdCdsRaggrRim() {
		return cdCdsRaggrRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRaggrRim]
	 **/
	public void setCdCdsRaggrRim(String cdCdsRaggrRim)  {
		this.cdCdsRaggrRim=cdCdsRaggrRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdRaggrMagazzinoRim]
	 **/
	public String getCdRaggrMagazzinoRim() {
		return cdRaggrMagazzinoRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdRaggrMagazzinoRim]
	 **/
	public void setCdRaggrMagazzinoRim(String cdRaggrMagazzinoRim)  {
		this.cdRaggrMagazzinoRim=cdRaggrMagazzinoRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [metodoValRim]
	 **/
	public String getMetodoValRim() {
		return metodoValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [metodoValRim]
	 **/
	public void setMetodoValRim(String metodoValRim)  {
		this.metodoValRim=metodoValRim;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [abilTuttiBeniServ]
	 **/
	public Boolean getAbilTuttiBeniServ() {
		return abilTuttiBeniServ;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [abilTuttiBeniServ]
	 **/
	public void setAbilTuttiBeniServ(Boolean abilTuttiBeniServ)  {
		this.abilTuttiBeniServ=abilTuttiBeniServ;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}
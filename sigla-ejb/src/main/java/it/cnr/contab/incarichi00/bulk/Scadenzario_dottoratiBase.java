/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Scadenzario_dottoratiBase extends Scadenzario_dottoratiKey implements Keyed {
//    ID_ANAGRAFICA_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idAnagraficaDottorati;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private String cdCds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private String cdUnitaOrganizzativa;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private Integer esercizio;
 
//    PG_IMPEGNO DECIMAL(38,0)
	private Long pgImpegno;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtRegistrazione;
 
//    DS_SCAD_DOTT VARCHAR(300)
	private String dsScadDott;
 
//    TI_ANAGRAFICO CHAR(1)
	private String tiAnagrafico;
 
//    CD_TERZO DECIMAL(38,0) NOT NULL
	private Integer cdTerzo;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private String ragioneSociale;
 
//    NOME VARCHAR(50)
	private String nome;
 
//    COGNOME VARCHAR(50)
	private String cognome;
 
//    CODICE_FISCALE VARCHAR(20)
	private String codiceFiscale;
 
//    PARTITA_IVA VARCHAR(20)
	private String partitaIva;
 
//    CD_TERMINI_PAG VARCHAR(10)
	private String cdTerminiPag;
 
//    CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private String cdModalitaPag;
 
//    PG_BANCA DECIMAL(38,0) NOT NULL
	private Long pgBanca;
 
//    CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL
	private String cdTipoRapporto;
 
//    CD_TRATTAMENTO VARCHAR(10) NOT NULL
	private String cdTrattamento;
 
//    IM_TOTALE_SCAD_DOTT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleScadDott;
 
//    NUMERO_RATE DECIMAL(3,0) NOT NULL
	private Integer numeroRate;
 
//    TI_ANTICIPO_POSTICIPO CHAR(1) NOT NULL
	private String tiAnticipoPosticipo;
 
//    MESI_ANTICIPO_POSTICIPO DECIMAL(3,0) NOT NULL
	private Integer mesiAnticipoPosticipo;
 
//    DT_INIZIO_SCAD_DOTT TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtInizioScadDott;
 
//    DT_FINE_SCAD_DOTT TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtFineScadDott;
 
//    STATO_ASS_COMPENSO CHAR(1)
	private String statoAssCompenso;
 
//    STATO CHAR(1)
	private String stato;
 
//    DT_SOSPENSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtSospensione;
 
//    DT_RIPRISTINO TIMESTAMP(7)
	private java.sql.Timestamp dtRipristino;
 
//    DT_RINNOVO TIMESTAMP(7)
	private java.sql.Timestamp dtRinnovo;
 
//    DT_CESSAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCessazione;
 
//    CD_CDS_SCAD_DOTT_ORI VARCHAR(30)
	private String cdCdsScadDottOri;
 
//    CD_UO_SCAD_DOTT_ORI VARCHAR(30)
	private String cdUoScadDottOri;
 
//    ESERCIZIO_SCAD_DOTT_ORI DECIMAL(4,0)
	private Integer esercizioScadDottOri;
 
//    TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private String tiIstituzCommerc;
 
//    FL_TASSAZIONE_SEPARATA CHAR(1) NOT NULL
	private Boolean flTassazioneSeparata;
 
//    IMPONIBILE_IRPEF_ESEPREC2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibileIrpefEseprec2;
 
//    IMPONIBILE_IRPEF_ESEPREC1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imponibileIrpefEseprec1;
 
//    ALIQUOTA_IRPEF_MEDIA DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquotaIrpefMedia;
 
//    FL_ESCLUDI_QVARIA_DEDUZIONE CHAR(1) NOT NULL
	private Boolean flEscludiQvariaDeduzione;
 
//    ESERCIZIO_REP DECIMAL(4,0)
	private Integer esercizioRep;
 
//    PG_REPERTORIO DECIMAL(38,0)
	private Long pgRepertorio;
 
//    TI_PRESTAZIONE VARCHAR(5)
	private String tiPrestazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI
	 **/
	public Scadenzario_dottoratiBase() {
		super();
	}
	public Scadenzario_dottoratiBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo anagrafica dottorati.]
	 **/
	public Long getIdAnagraficaDottorati() {
		return idAnagraficaDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo anagrafica dottorati.]
	 **/
	public void setIdAnagraficaDottorati(Long idAnagraficaDottorati)  {
		this.idAnagraficaDottorati=idAnagraficaDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgImpegno]
	 **/
	public Long getPgImpegno() {
		return pgImpegno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgImpegno]
	 **/
	public void setPgImpegno(Long pgImpegno)  {
		this.pgImpegno=pgImpegno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRegistrazione]
	 **/
	public java.sql.Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRegistrazione]
	 **/
	public void setDtRegistrazione(java.sql.Timestamp dtRegistrazione)  {
		this.dtRegistrazione=dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsScadDott]
	 **/
	public String getDsScadDott() {
		return dsScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsScadDott]
	 **/
	public void setDsScadDott(String dsScadDott)  {
		this.dsScadDott=dsScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAnagrafico]
	 **/
	public String getTiAnagrafico() {
		return tiAnagrafico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAnagrafico]
	 **/
	public void setTiAnagrafico(String tiAnagrafico)  {
		this.tiAnagrafico=tiAnagrafico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ragioneSociale]
	 **/
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ragioneSociale]
	 **/
	public void setRagioneSociale(String ragioneSociale)  {
		this.ragioneSociale=ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognome]
	 **/
	public String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscale]
	 **/
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscale]
	 **/
	public void setCodiceFiscale(String codiceFiscale)  {
		this.codiceFiscale=codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partitaIva]
	 **/
	public String getPartitaIva() {
		return partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIva]
	 **/
	public void setPartitaIva(String partitaIva)  {
		this.partitaIva=partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerminiPag]
	 **/
	public String getCdTerminiPag() {
		return cdTerminiPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerminiPag]
	 **/
	public void setCdTerminiPag(String cdTerminiPag)  {
		this.cdTerminiPag=cdTerminiPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaPag]
	 **/
	public String getCdModalitaPag() {
		return cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaPag]
	 **/
	public void setCdModalitaPag(String cdModalitaPag)  {
		this.cdModalitaPag=cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBanca]
	 **/
	public Long getPgBanca() {
		return pgBanca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBanca]
	 **/
	public void setPgBanca(Long pgBanca)  {
		this.pgBanca=pgBanca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoRapporto]
	 **/
	public String getCdTipoRapporto() {
		return cdTipoRapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoRapporto]
	 **/
	public void setCdTipoRapporto(String cdTipoRapporto)  {
		this.cdTipoRapporto=cdTipoRapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTrattamento]
	 **/
	public String getCdTrattamento() {
		return cdTrattamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTrattamento]
	 **/
	public void setCdTrattamento(String cdTrattamento)  {
		this.cdTrattamento=cdTrattamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleScadDott]
	 **/
	public java.math.BigDecimal getImTotaleScadDott() {
		return imTotaleScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleScadDott]
	 **/
	public void setImTotaleScadDott(java.math.BigDecimal imTotaleScadDott)  {
		this.imTotaleScadDott=imTotaleScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroRate]
	 **/
	public Integer getNumeroRate() {
		return numeroRate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroRate]
	 **/
	public void setNumeroRate(Integer numeroRate)  {
		this.numeroRate=numeroRate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAnticipoPosticipo]
	 **/
	public String getTiAnticipoPosticipo() {
		return tiAnticipoPosticipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAnticipoPosticipo]
	 **/
	public void setTiAnticipoPosticipo(String tiAnticipoPosticipo)  {
		this.tiAnticipoPosticipo=tiAnticipoPosticipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mesiAnticipoPosticipo]
	 **/
	public Integer getMesiAnticipoPosticipo() {
		return mesiAnticipoPosticipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mesiAnticipoPosticipo]
	 **/
	public void setMesiAnticipoPosticipo(Integer mesiAnticipoPosticipo)  {
		this.mesiAnticipoPosticipo=mesiAnticipoPosticipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtInizioScadDott]
	 **/
	public java.sql.Timestamp getDtInizioScadDott() {
		return dtInizioScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtInizioScadDott]
	 **/
	public void setDtInizioScadDott(java.sql.Timestamp dtInizioScadDott)  {
		this.dtInizioScadDott=dtInizioScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFineScadDott]
	 **/
	public java.sql.Timestamp getDtFineScadDott() {
		return dtFineScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFineScadDott]
	 **/
	public void setDtFineScadDott(java.sql.Timestamp dtFineScadDott)  {
		this.dtFineScadDott=dtFineScadDott;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoAssCompenso]
	 **/
	public String getStatoAssCompenso() {
		return statoAssCompenso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoAssCompenso]
	 **/
	public void setStatoAssCompenso(String statoAssCompenso)  {
		this.statoAssCompenso=statoAssCompenso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtSospensione]
	 **/
	public java.sql.Timestamp getDtSospensione() {
		return dtSospensione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtSospensione]
	 **/
	public void setDtSospensione(java.sql.Timestamp dtSospensione)  {
		this.dtSospensione=dtSospensione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRipristino]
	 **/
	public java.sql.Timestamp getDtRipristino() {
		return dtRipristino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRipristino]
	 **/
	public void setDtRipristino(java.sql.Timestamp dtRipristino)  {
		this.dtRipristino=dtRipristino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRinnovo]
	 **/
	public java.sql.Timestamp getDtRinnovo() {
		return dtRinnovo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRinnovo]
	 **/
	public void setDtRinnovo(java.sql.Timestamp dtRinnovo)  {
		this.dtRinnovo=dtRinnovo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCessazione]
	 **/
	public java.sql.Timestamp getDtCessazione() {
		return dtCessazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCessazione]
	 **/
	public void setDtCessazione(java.sql.Timestamp dtCessazione)  {
		this.dtCessazione=dtCessazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cds scadenzario dottorati]
	 **/
	public String getCdCdsScadDottOri() {
		return cdCdsScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cds scadenzario dottorati]
	 **/
	public void setCdCdsScadDottOri(String cdCdsScadDottOri)  {
		this.cdCdsScadDottOri=cdCdsScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [uo scadenzario dottorati]
	 **/
	public String getCdUoScadDottOri() {
		return cdUoScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [uo scadenzario dottorati]
	 **/
	public void setCdUoScadDottOri(String cdUoScadDottOri)  {
		this.cdUoScadDottOri=cdUoScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio scadenzario dottorati]
	 **/
	public Integer getEsercizioScadDottOri() {
		return esercizioScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio scadenzario dottorati]
	 **/
	public void setEsercizioScadDottOri(Integer esercizioScadDottOri)  {
		this.esercizioScadDottOri=esercizioScadDottOri;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiIstituzCommerc]
	 **/
	public String getTiIstituzCommerc() {
		return tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiIstituzCommerc]
	 **/
	public void setTiIstituzCommerc(String tiIstituzCommerc)  {
		this.tiIstituzCommerc=tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flTassazioneSeparata]
	 **/
	public Boolean getFlTassazioneSeparata() {
		return flTassazioneSeparata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flTassazioneSeparata]
	 **/
	public void setFlTassazioneSeparata(Boolean flTassazioneSeparata)  {
		this.flTassazioneSeparata=flTassazioneSeparata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imponibileIrpefEseprec2]
	 **/
	public java.math.BigDecimal getImponibileIrpefEseprec2() {
		return imponibileIrpefEseprec2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imponibileIrpefEseprec2]
	 **/
	public void setImponibileIrpefEseprec2(java.math.BigDecimal imponibileIrpefEseprec2)  {
		this.imponibileIrpefEseprec2=imponibileIrpefEseprec2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imponibileIrpefEseprec1]
	 **/
	public java.math.BigDecimal getImponibileIrpefEseprec1() {
		return imponibileIrpefEseprec1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imponibileIrpefEseprec1]
	 **/
	public void setImponibileIrpefEseprec1(java.math.BigDecimal imponibileIrpefEseprec1)  {
		this.imponibileIrpefEseprec1=imponibileIrpefEseprec1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aliquotaIrpefMedia]
	 **/
	public java.math.BigDecimal getAliquotaIrpefMedia() {
		return aliquotaIrpefMedia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aliquotaIrpefMedia]
	 **/
	public void setAliquotaIrpefMedia(java.math.BigDecimal aliquotaIrpefMedia)  {
		this.aliquotaIrpefMedia=aliquotaIrpefMedia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flEscludiQvariaDeduzione]
	 **/
	public Boolean getFlEscludiQvariaDeduzione() {
		return flEscludiQvariaDeduzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flEscludiQvariaDeduzione]
	 **/
	public void setFlEscludiQvariaDeduzione(Boolean flEscludiQvariaDeduzione)  {
		this.flEscludiQvariaDeduzione=flEscludiQvariaDeduzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioRep]
	 **/
	public Integer getEsercizioRep() {
		return esercizioRep;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioRep]
	 **/
	public void setEsercizioRep(Integer esercizioRep)  {
		this.esercizioRep=esercizioRep;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgRepertorio]
	 **/
	public Long getPgRepertorio() {
		return pgRepertorio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgRepertorio]
	 **/
	public void setPgRepertorio(Long pgRepertorio)  {
		this.pgRepertorio=pgRepertorio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiPrestazione]
	 **/
	public String getTiPrestazione() {
		return tiPrestazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiPrestazione]
	 **/
	public void setTiPrestazione(String tiPrestazione)  {
		this.tiPrestazione=tiPrestazione;
	}
}
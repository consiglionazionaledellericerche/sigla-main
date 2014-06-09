package it.cnr.contab.brevetti.client;

import java.io.Serializable;
import java.util.Date;

public class FatturaPassivaBase  implements Serializable {

	private static final long serialVersionUID = 5460675313659773472L;

	public FatturaPassivaBase() {
		super();
	}
	public static final String TIPO_COMPENSO = "C";
	public static final String TIPO_FATTURA = "F";
	private String cd_cds;
	private String cd_unita_organizzativa;
	private Integer esercizio;
	private Long pg_fattura_passiva;
	private java.lang.String ds_fattura_passiva;
	private String cd_cds_origine;
	private String cd_uo_origine;
	private String nr_fattura_fornitore;
	private Date dt_fattura_fornitore;
	private String partita_iva;
	private String codice_fiscale;
	private String tipoFatturaCompenso;

	public String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(String cdCds) {
		cd_cds = cdCds;
	}
	public String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(String cdUnitaOrganizzativa) {
		cd_unita_organizzativa = cdUnitaOrganizzativa;
	}
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}
	public void setPg_fattura_passiva(Long pg_documento_amm) {
		this.pg_fattura_passiva = pg_documento_amm;
	}
	public Long getPg_fattura_passiva() {
		return pg_fattura_passiva;
	}
	public void setDs_fattura_passiva(java.lang.String ds_fattura_passiva) {
		this.ds_fattura_passiva = ds_fattura_passiva;
	}
	public java.lang.String getDs_fattura_passiva() {
		return ds_fattura_passiva;
	}
	public String getCd_cds_origine() {
		return cd_cds_origine;
	}
	public void setCd_cds_origine(String cdCdsOrigine) {
		cd_cds_origine = cdCdsOrigine;
	}
	public String getCd_uo_origine() {
		return cd_uo_origine;
	}
	public void setCd_uo_origine(String cdUoOrigine) {
		cd_uo_origine = cdUoOrigine;
	}
	public void setNr_fattura_fornitore(String nr_fattura_fornitore) {
		this.nr_fattura_fornitore = nr_fattura_fornitore;
	}
	public String getNr_fattura_fornitore() {
		return nr_fattura_fornitore;
	}
	public void setDt_fattura_fornitore(Date dt_fattura_fornitore) {
		this.dt_fattura_fornitore = dt_fattura_fornitore;
	}
	public Date getDt_fattura_fornitore() {
		return dt_fattura_fornitore;
	}
	public String getPartita_iva() {
		return partita_iva;
	}
	public void setPartita_iva(String partita_iva) {
		this.partita_iva = partita_iva;
	}
	public String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}
	public String getTipoFatturaCompenso() {
		return tipoFatturaCompenso;
	}
	public void setTipoFatturaCompenso(String tipoFatturaCompenso) {
		this.tipoFatturaCompenso = tipoFatturaCompenso;
	}
}

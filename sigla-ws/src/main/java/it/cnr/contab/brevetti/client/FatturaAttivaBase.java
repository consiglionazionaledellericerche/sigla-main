package it.cnr.contab.brevetti.client;

import java.io.Serializable;
import java.util.Date;

public class FatturaAttivaBase  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6446663213447514016L;
	
	public FatturaAttivaBase() {
		super();
	}

	private String cd_cds;
	private String cd_unita_organizzativa;
	private Integer esercizio;
	private Long pg_fattura_attiva;
	private java.lang.String ds_fattura_attiva;
	private String cd_cds_origine;
	private String cd_uo_origine;
	private Date dt_emissione;
	private Integer cd_terzo;

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
	public void setDs_fattura_attiva(java.lang.String ds_fattura_attiva) {
		this.ds_fattura_attiva = ds_fattura_attiva;
	}
	public java.lang.String getDs_fattura_attiva() {
		return ds_fattura_attiva;
	}
	public void setPg_fattura_attiva(Long pg_fattura_attiva) {
		this.pg_fattura_attiva = pg_fattura_attiva;
	}
	public Long getPg_fattura_attiva() {
		return pg_fattura_attiva;
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
	public Date getDt_emissione() {
		return dt_emissione;
	}
	public void setDt_emissione(Date dt_emissione) {
		this.dt_emissione = dt_emissione;
	}
	public Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
}

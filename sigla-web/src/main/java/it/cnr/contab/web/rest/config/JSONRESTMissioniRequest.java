package it.cnr.contab.web.rest.config;

public class JSONRESTMissioniRequest extends JSONRESTRequest{

	String data;
	Long nazione;
	Long inquadramento;
	String cdTipoSpesa;
	String cdTipoPasto;
	String divisa;
	String km;
	String importoSpesa;
	public JSONRESTMissioniRequest() {
		super();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getNazione() {
		return nazione;
	}

	public void setNazione(Long nazione) {
		this.nazione = nazione;
	}

	public Long getInquadramento() {
		return inquadramento;
	}

	public void setInquadramento(Long inquadramento) {
		this.inquadramento = inquadramento;
	}

	public String getCdTipoSpesa() {
		return cdTipoSpesa;
	}

	public void setCdTipoSpesa(String cdTipoSpesa) {
		this.cdTipoSpesa = cdTipoSpesa;
	}

	public String getCdTipoPasto() {
		return cdTipoPasto;
	}

	public void setCdTipoPasto(String cdTipoPasto) {
		this.cdTipoPasto = cdTipoPasto;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public String getKm() {
		return km;
	}

	public void setKm(String km) {
		this.km = km;
	}

	public String getImportoSpesa() {
		return importoSpesa;
	}

	public void setImportoSpesa(String importoSpesa) {
		this.importoSpesa = importoSpesa;
	}
}

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;

import java.util.Dictionary;
import java.util.Iterator;

public class Anagrafica_dottoratiBulk extends Anagrafica_dottoratiBase {
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
	/**
	 * [PHDTIPO_DOTTORATI ]
	 **/
	private Phdtipo_dottoratiBulk phdtipoDottorati =  new Phdtipo_dottoratiBulk();
	/**
	 * [CICLO_DOTTORATI ]
	 **/
	private Ciclo_dottoratiBulk cicloDottorati =  new Ciclo_dottoratiBulk();
	/**
	 * [TIPOCORSO_DOTTORATI ]
	 **/
	private Tipocorso_dottoratiBulk tipocorsoDottorati =  new Tipocorso_dottoratiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI
	 **/
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_SI = "S";
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_NO = "N";
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_NON_PRESENTE = "X";

	public static Dictionary<String, String> tipoCofinanziamentoEffettuato = new it.cnr.jada.util.OrderedHashtable();
	static {
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_SI, "Si");
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_NO, "No");
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_NON_PRESENTE, "Non presente");
	}

	public Anagrafica_dottoratiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI
	 **/
	public Anagrafica_dottoratiBulk(Long id) {
		super(id);
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	public Phdtipo_dottoratiBulk getPhdtipoDottorati() {
		return phdtipoDottorati;
	}
	public void setPhdtipoDottorati(Phdtipo_dottoratiBulk phdtipoDottorati)  {
		this.phdtipoDottorati=phdtipoDottorati;
	}
	public Ciclo_dottoratiBulk getCicloDottorati() {
		return cicloDottorati;
	}
	public void setCicloDottorati(Ciclo_dottoratiBulk cicloDottorati)  {
		this.cicloDottorati=cicloDottorati;
	}
	public Tipocorso_dottoratiBulk getTipocorsoDottorati() {
		return tipocorsoDottorati;
	}
	public void setTipocorsoDottorati(Tipocorso_dottoratiBulk tipocorsoDottorati)  {
		this.tipocorsoDottorati=tipocorsoDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 *
	 * @return*/
	public Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del tipo dottorati.]
	 **/
	public Long getIdPhdtipoDottorati() {
		Phdtipo_dottoratiBulk phdtipoDottorati = this.getPhdtipoDottorati();
		if (phdtipoDottorati == null)
			return null;
		return getPhdtipoDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del tipo dottorati.]
	 **/
	public void setIdPhdtipoDottorati(Long idPhdtipoDottorati)  {
		this.getPhdtipoDottorati().setId(idPhdtipoDottorati);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public Long getIdCicloDottorati() {
		Ciclo_dottoratiBulk cicloDottorati = this.getCicloDottorati();
		if (cicloDottorati == null)
			return null;
		return getCicloDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public void setIdCicloDottorati(Long idCicloDottorati)  {
		this.getCicloDottorati().setId(idCicloDottorati);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del tipocorso_dottorati.]
	 **/
	public Long getIdTipocorsoDottorati() {
		Tipocorso_dottoratiBulk tipocorsoDottorati = this.getTipocorsoDottorati();
		if (tipocorsoDottorati == null)
			return null;
		return getTipocorsoDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del tipocorso_dottorati.]
	 **/
	public void setIdTipocorsoDottorati(Long idTipocorsoDottorati)  {
		this.getTipocorsoDottorati().setId(idTipocorsoDottorati);
	}

	public static Dictionary<String, String> getTipoCofinanziamentoEffettuato() {
		return tipoCofinanziamentoEffettuato;
	}
}
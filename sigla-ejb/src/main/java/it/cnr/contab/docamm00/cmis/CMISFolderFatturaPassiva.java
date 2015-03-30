package it.cnr.contab.docamm00.cmis;

import java.math.BigInteger;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.jada.bulk.OggettoBulk;

@CMISType(name="F:sigla_fatture:fatture_passive")
public class CMISFolderFatturaPassiva extends OggettoBulk {
	private static final long serialVersionUID = 4110702628275029148L;

	private final DocumentoEleTestataBulk documentoEleTestata;
	private final BigInteger identificativoSdI;
	
	public CMISFolderFatturaPassiva(DocumentoEleTestataBulk documentoEleTestata, BigInteger identificativoSdI) {
    	super();
    	this.documentoEleTestata = documentoEleTestata;
    	this.identificativoSdI = identificativoSdI;
    }
	
	@CMISProperty(name="sigla_fatture:identificativoSdI")
	public Long getIdentificativoSdI() {
		return this.identificativoSdI.longValue();
	}
	
	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_cds();
	}
	
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	
	@CMISPolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@CMISProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		return "SDI";
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi", property=@CMISProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
	public String getCodiceTerzo() {
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo().toString();
	}
}
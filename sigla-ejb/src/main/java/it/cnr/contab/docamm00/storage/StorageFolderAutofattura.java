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

package it.cnr.contab.docamm00.storage;

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.dp.DigitalPreservationProperties;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.Utility;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@StorageType(name="F:sigla_fatture:autofatture")
public class StorageFolderAutofattura extends StorageFolderFattura {
	private static final long serialVersionUID = 4110702628275029148L;

	private AutofatturaBulk autofatturaBulk;
	private DigitalPreservationProperties dpProperties;

	public StorageFolderAutofattura(AutofatturaBulk autofatturaBulk) {
    	super();
    	setAutofatturaBulk(autofatturaBulk);
    	loadProperties();
	}
	
	public void loadProperties(){
		if (dpProperties == null)
			dpProperties = SpringUtil.getBean("digitalPreservationProperties",DigitalPreservationProperties.class);
	}

	@StorageProperty(name="sigla_fatture:esercizio")
    public Integer getEsercizioAutofattura() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getEsercizio)
				.orElse(null);
    }

	@StorageProperty(name="sigla_fatture:pg_fattura", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getPgAutofattura() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getPg_autofattura)
				.orElse(null);
    }
	
	@StorageProperty(name="sigla_fatture:prot_iva", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloIva() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getProtocollo_iva)
				.orElse(null);
    }

	@StorageProperty(name="sigla_fatture:prot_gen", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloGenerale() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getProtocollo_iva)
				.orElse(null);
    }

	@StorageProperty(name="sigla_fatture:data_reg", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataRegistrazione(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getDt_registrazione)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:descrizione")
	public String getDescrizione() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getDs_fattura_passiva)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:imponibile")
	public BigDecimal getImponibile(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getIm_totale_imponibile)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:iva")
	public BigDecimal getIva(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getIm_totale_iva)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:importo_totale")
	public BigDecimal getImportoTotale(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getIm_totale_fattura)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:divisa")
	public String getDivisa() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCd_divisa)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:cambio")
	public BigDecimal getCambio(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCambio)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_dal", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaDal(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getDt_da_competenza_coge)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_al", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaAl(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getDt_a_competenza_coge)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:data_emissione", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataEmissione(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getDt_registrazione)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:codice_ipa")
	public String getCodiceIpa() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCodiceUnivocoUfficioIpa)
				.orElse(Optional.ofNullable(this.getAutofatturaBulk())
								.map(AutofatturaBulk::getCodiceDestinatarioFatt)
								.orElse(null));
	}
	
	@StorageProperty(name="sigla_fatture:codice_invio_sdi")
	public String getCodiceInvioSdi() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCodiceInvioSdi)
				.orElse(null);
	}
	
	@StorageProperty(name="sigla_fatture:stato_invio_sdi")
	public String getStatoInvioSdi() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getStatoInvioSdi)
				.map(el-> VDocammElettroniciAttiviBulk.getStatoInvioSdiKeys().get(el))
				.map(String::valueOf)
				.orElse(null);
	}
		
	@StorageProperty(name="sigla_fatture:note_invio_sdi")
	public String getNoteInvioSdi() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getNoteInvioSdi)
				.orElse(null);
	}
		
	@StorageProperty(name="sigla_fatture:data_consegna_sdi", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataConsegnaSdi(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getDtConsegnaSdi)
				.orElse(null);
	}
	
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCd_cds)
				.orElse(null);
	}
	
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCd_unita_organizzativa)
				.orElse(null);
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@StorageProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getUtuv)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi", property=@StorageProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
	public String getCodiceTerzo() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCd_terzo)
				.map(String::valueOf)
				.orElse(null);
	}

	@StorageProperty(name="sigla_fatture:modalita_incasso")
	public String getModalitaIncasso() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCd_modalita_pag_uo_cds)
				.orElse(null);
	}

	@StorageProperty(name="sigla_fatture:prog_univoco_anno", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProgrUnivocoAnno() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getProgrUnivocoAnno)
				.orElse(null);
    }
	
	@StorageProperty(name="sigla_fatture:tipo_documento")
	public String getTipoDocumento() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getTi_fattura)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_denominazione"))
	public String getRagioneSociale() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getRagione_sociale)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_pariva"))
	public String getPariva() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getPartita_iva)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_cognome"))
	public String getCognome() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCognome)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_nome"))
	public String getNome() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getNome)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_codfis"))
	public String getCodfis() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(el.getFattura_passiva()))
				.map(Fattura_passivaBase::getCodice_fiscale)
				.orElse(null);
	}

	@StorageProperty(name="sigla_fatture:cod_amministrazione")
	public String getCodiceAmministrazione() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(dpProperties))
				.map(DigitalPreservationProperties::getDigitalPreservationCodAmm)
				.orElse(null);
	}

	@StorageProperty(name="sigla_fatture:cod_registro")
	public String getCodiceRegistro() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.flatMap(el->Optional.ofNullable(dpProperties))
				.map(DigitalPreservationProperties::getDigitalPreservationCodRegFA)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:cds_origine", property=@StorageProperty(name="sigla_commons_aspect:cds_origine_codice"))
	public String getCdsOrigine() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCd_cds_origine)
				.orElse(null);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:uo_origine", property=@StorageProperty(name="sigla_commons_aspect:uo_origine_codice"))
	public String getUoOrigine() {
		return Optional.ofNullable(this.getAutofatturaBulk())
				.map(AutofatturaBulk::getCd_uo_origine)
				.orElse(null);
	}


	public String getCMISPrincipalPath() {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
                this.getAutofatturaBulk().getCd_uo_origine(),
                "AutoFatture"
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
	}

	public String getCMISPath(){
		return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
						getPathFolderAutofattura(),
						getLastFolderAutofattura(),
						null, null, this);
	}

	public String getPathFolderAutofattura() {
		return getCMISPrincipalPath().concat(StorageDriver.SUFFIX).concat(
				Optional.ofNullable(getEsercizioAutofattura())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0")
		);
	}

	private String getLastFolderAutofattura() {
		return "Autofattura " + this.getEsercizioAutofattura().toString() +
				Utility.lpad(this.getPgAutofattura().toString(),10,'0');
	}
	
	public String getCMISPathForSearch(){
        return Arrays.asList(
        		getPathFolderAutofattura(),
        		getLastFolderAutofattura()
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
	}

	public AutofatturaBulk getAutofatturaBulk() {
		return autofatturaBulk;
	}

	public void setAutofatturaBulk(AutofatturaBulk autofatturaBulk) {
		this.autofatturaBulk = autofatturaBulk;
	}
}
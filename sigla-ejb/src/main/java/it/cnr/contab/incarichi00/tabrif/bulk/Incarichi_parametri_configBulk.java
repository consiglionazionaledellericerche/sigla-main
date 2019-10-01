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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.InputType;

public class Incarichi_parametri_configBulk extends Incarichi_parametri_configBase {

	private Procedure_amministrativeBulk procedura_amministrativa;
	private Tipo_incaricoBulk tipo_incarico;
	private Tipo_attivitaBulk tipo_attivita;
	private Incarichi_parametriBulk incarichi_parametri;

	public Incarichi_parametri_configBulk() {
		super();
	}
	public Incarichi_parametri_configBulk(java.lang.String cd_config) {
		super(cd_config);
	}

// Procedura Amministrativa	
	@FieldPropertyAnnotation(name="procedura_amministrativa",
			inputType=InputType.SEARCHTOOL,
			formName="searchtool",
			enabledOnSearch=true,
			nullable=false,
			label="Procedura amministrativa" )
	public Procedure_amministrativeBulk getProcedura_amministrativa() {
		return procedura_amministrativa;
	}
	public void setProcedura_amministrativa(
			Procedure_amministrativeBulk procedura_amministrativa) {
		this.procedura_amministrativa = procedura_amministrativa;
	}

	@FieldPropertyAnnotation(name="cd_proc_amm",
			inputType=InputType.TEXT,
			inputSize=5,
			maxLength=5,
			enabledOnSearch=true,
			nullable=false,
			label="Procedura amministrativa" )
	public String getCd_proc_amm() {
		if (this.getProcedura_amministrativa() == null)
			return null;
		return this.getProcedura_amministrativa().getCd_proc_amm();
	}
	public void setCd_proc_amm(String cd_proc_amm) {
		if (this.getProcedura_amministrativa() != null)
			this.getProcedura_amministrativa().setCd_proc_amm(cd_proc_amm);
	}
	
// Tipo Attività Richiesta
	@FieldPropertyAnnotation(name="find_tipo_attivita",
			inputType=InputType.SEARCHTOOL,
			formName="searchtool",
			enabledOnSearch=true,
			nullable=false,
			label="Tipo Attivita" )
	public Tipo_attivitaBulk getTipo_attivita() {
		return tipo_attivita;
	}
	public void setTipo_attivita(Tipo_attivitaBulk tipo_attivita) {
		this.tipo_attivita = tipo_attivita;
	}
	@FieldPropertyAnnotation(name="cd_tipo_attivita",
			inputType=InputType.TEXT,
			inputSize=5,
			maxLength=5,
			enabledOnSearch=true,
			nullable=false,
			label="Tipo Attivita" )
	public java.lang.String getCd_tipo_attivita() {
		if (this.getTipo_attivita() == null)
			return null;
		return this.getTipo_attivita().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita) {
		if (this.getTipo_attivita() != null)
			this.getTipo_attivita().setCd_tipo_attivita(cd_tipo_attivita);
	}

// Tipo Incarico Richiesto
	@FieldPropertyAnnotation(name="find_tipo_incarico",
		inputType=InputType.SEARCHTOOL,
		formName="searchtool",
		enabledOnSearch=true,
		nullable=false,
		label="Tipo Incarico" )
	public Tipo_incaricoBulk getTipo_incarico() {
		return tipo_incarico;
	}
	public void setTipo_incarico(Tipo_incaricoBulk tipo_incarico) {
		this.tipo_incarico = tipo_incarico;
	}
	@FieldPropertyAnnotation(name="cd_tipo_incarico",
			inputType=InputType.TEXT,
			inputSize=5,
			maxLength=5,
			enabledOnSearch=true,
			nullable=false,
			label="Tipo Incarico" )
	public java.lang.String getCd_tipo_incarico() {
		if (this.getTipo_incarico() == null)
			return null;
		return this.getTipo_incarico().getCd_tipo_incarico();
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico) {
		if (this.getTipo_incarico() != null)
			this.getTipo_incarico().setCd_tipo_incarico(cd_tipo_incarico);
	}

	// Parametri	
	@FieldPropertyAnnotation(name="find_parametri",
			inputType=InputType.SEARCHTOOL,
			formName="searchtool",
			enabledOnSearch=true,
			nullable=false,
			label="Parametri Incarichi" )
	public Incarichi_parametriBulk getIncarichi_parametri() {
		return incarichi_parametri;
	}
	public void setIncarichi_parametri(Incarichi_parametriBulk incarichi_parametri) {
		this.incarichi_parametri = incarichi_parametri;
	}

	@FieldPropertyAnnotation(name="cd_parametri",
			inputType=InputType.TEXT,
			inputSize=5,
			maxLength=5,
			enabledOnSearch=true,
			nullable=false,
			label="Parametri Incarichi" )
	public String getCd_parametri() {
		if (this.getIncarichi_parametri() == null)
			return null;
		return this.getIncarichi_parametri().getCd_parametri();
	}
	public void setCd_parametri(String cd_parametri) {
		if (this.getIncarichi_parametri() != null)
			this.getIncarichi_parametri().setCd_parametri(cd_parametri);
	}
}
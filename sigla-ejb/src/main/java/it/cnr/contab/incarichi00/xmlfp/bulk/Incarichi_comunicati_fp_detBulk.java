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
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;

import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;

public class Incarichi_comunicati_fp_detBulk extends Incarichi_comunicati_fp_detBase {
	private Incarichi_comunicati_fpBulk incarichi_comunicati_fp;

	public Incarichi_comunicati_fp_detBulk() {
		super();
	}

	public Incarichi_comunicati_fp_detBulk(java.lang.Integer esercizio_repertorio, java.lang.Long pg_repertorio, java.lang.String tipo_record, java.lang.Long pg_record, java.lang.Integer anno_pag, java.lang.Integer semestre_pag) {
		super(esercizio_repertorio, pg_repertorio, tipo_record, pg_record, anno_pag, semestre_pag);
		setIncarichi_comunicati_fp(new Incarichi_comunicati_fpBulk(esercizio_repertorio, pg_repertorio, tipo_record, pg_record));
	}

	public Incarichi_comunicati_fpBulk getIncarichi_comunicati_fp() {
		return incarichi_comunicati_fp;
	}

	public void setIncarichi_comunicati_fp(Incarichi_comunicati_fpBulk incarichiComunicatiFp) {
		incarichi_comunicati_fp = incarichiComunicatiFp;
	}
	
	@Override
	public void setEsercizio_repertorio(Integer esercizio_repertorio) {
		if (this.getIncarichi_comunicati_fp() != null)
			this.getIncarichi_comunicati_fp().setEsercizio_repertorio(esercizio_repertorio);
	}
	
	@Override
	public Integer getEsercizio_repertorio() {
		if (this.getIncarichi_comunicati_fp() == null)
			return null;
		return this.getIncarichi_comunicati_fp().getEsercizio_repertorio();
	}
	
	@Override
	public void setPg_repertorio(Long pg_repertorio) {
		if (this.getIncarichi_comunicati_fp() != null)
			this.getIncarichi_comunicati_fp().setPg_repertorio(pg_repertorio);
	}
	
	@Override
	public Long getPg_repertorio() {
		if (this.getIncarichi_comunicati_fp() == null)
			return null;
		return this.getIncarichi_comunicati_fp().getPg_repertorio();
	}

	@Override
	public void setTipo_record(String tipoRecord) {
		if (this.getIncarichi_comunicati_fp() != null)
			this.getIncarichi_comunicati_fp().setTipo_record(tipoRecord);
	}

	@Override
	public String getTipo_record() {
		if (this.getIncarichi_comunicati_fp() == null)
			return null;
		return this.getIncarichi_comunicati_fp().getTipo_record();
	}

	@Override
	public void setPg_record(Long pgRecord) {
		if (this.getIncarichi_comunicati_fp() != null)
			this.getIncarichi_comunicati_fp().setPg_record(pgRecord);
	}

	@Override
	public Long getPg_record() {
		if (this.getIncarichi_comunicati_fp() == null)
			return null;
		return this.getIncarichi_comunicati_fp().getPg_record();
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, Comunicazione.Consulenti.NuovoIncarico.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);
			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}
	
	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType.Pagamenti.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);
			if (incComunicatoFP.getId_incarico()==null)
				incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW);
			else
				incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno().intValue());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre().intValue());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, EsitoComunicazione.Consulenti.NuovoIncarico.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);
			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setEsito_incarico_pagamento(nuovoPagamento.getEsito()!=null?nuovoPagamento.getEsito().toString():null);
			incComunicatoDetFP.setErr_anno_pag(nuovoPagamento.getErrAnno());
			incComunicatoDetFP.setErr_semestre_pag(nuovoPagamento.getErrSemetre());
			incComunicatoDetFP.setErr_importo_pag(nuovoPagamento.getErrImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, Comunicazione.Consulenti.ModificaIncarico.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ConsulenteType.Pagamenti.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);
			if (incComunicatoFP.getId_incarico()==null)
				incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW);
			else
				incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno().intValue());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre().intValue());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, EsitoComunicazione.Consulenti.ModificaIncarico.NuovoPagamento nuovoPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW);
			incComunicatoDetFP.setAnno_pag(nuovoPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(nuovoPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(nuovoPagamento.getImporto());

			incComunicatoDetFP.setEsito_incarico_pagamento(nuovoPagamento.getEsito()!=null?nuovoPagamento.getEsito().toString():null);
			incComunicatoDetFP.setErr_anno_pag(nuovoPagamento.getErrAnno());
			incComunicatoDetFP.setErr_semestre_pag(nuovoPagamento.getErrSemetre());
			incComunicatoDetFP.setErr_importo_pag(nuovoPagamento.getErrImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, Comunicazione.Consulenti.ModificaIncarico.ModificaPagamento modificaPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_UPD);
			incComunicatoDetFP.setAnno_pag(modificaPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(modificaPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(modificaPagamento.getImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, EsitoComunicazione.Consulenti.ModificaIncarico.ModificaPagamento modificaPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_UPD);
			incComunicatoDetFP.setAnno_pag(modificaPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(modificaPagamento.getSemestre());
			incComunicatoDetFP.setImporto_pag(modificaPagamento.getImporto());

			incComunicatoDetFP.setEsito_incarico_pagamento(modificaPagamento.getEsito()!=null?modificaPagamento.getEsito().toString():null);
			incComunicatoDetFP.setErr_anno_pag(modificaPagamento.getErrAnno());
			incComunicatoDetFP.setErr_semestre_pag(modificaPagamento.getErrSemetre());
			incComunicatoDetFP.setErr_importo_pag(modificaPagamento.getErrImporto());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, Comunicazione.Consulenti.ModificaIncarico.CancellaPagamento cancellaPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_DEL);
			incComunicatoDetFP.setAnno_pag(cancellaPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(cancellaPagamento.getSemestre());
			
			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public static Incarichi_comunicati_fp_detBulk copyFrom(UserContext userContext, Incarichi_comunicati_fpBulk incComunicatoFP, EsitoComunicazione.Consulenti.ModificaIncarico.CancellaPagamento cancellaPagamento) throws it.cnr.jada.comp.ApplicationException {
		try {
			Incarichi_comunicati_fp_detBulk incComunicatoDetFP = new Incarichi_comunicati_fp_detBulk();
			incComunicatoDetFP.setIncarichi_comunicati_fp(incComunicatoFP);

			incComunicatoDetFP.setTipo_record_det(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_DEL);
			incComunicatoDetFP.setAnno_pag(cancellaPagamento.getAnno());
			incComunicatoDetFP.setSemestre_pag(cancellaPagamento.getSemestre());

			incComunicatoDetFP.setEsito_incarico_pagamento(cancellaPagamento.getEsito()!=null?cancellaPagamento.getEsito().toString():null);
			incComunicatoDetFP.setErr_anno_pag(cancellaPagamento.getErrAnno());
			incComunicatoDetFP.setErr_semestre_pag(cancellaPagamento.getErrSemetre());

			incComunicatoDetFP.setToBeCreated();
			return incComunicatoDetFP;
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public void updateFrom(UserContext userContext, Incarichi_comunicati_fp_detBulk incaricoDetFP) throws it.cnr.jada.comp.ApplicationException {
		try {
			if (incaricoDetFP.getIncarichi_comunicati_fp()!=null) this.setIncarichi_comunicati_fp(incaricoDetFP.getIncarichi_comunicati_fp());
			if (incaricoDetFP.getAnno_pag()!=null) this.setAnno_pag(incaricoDetFP.getAnno_pag());
			if (incaricoDetFP.getSemestre_pag()!=null) this.setSemestre_pag(incaricoDetFP.getSemestre_pag());
			if (incaricoDetFP.getImporto_pag()!=null) this.setImporto_pag(incaricoDetFP.getImporto_pag());
			
			this.setEsito_incarico_pagamento(incaricoDetFP.getEsito_incarico_pagamento());
			this.setErr_anno_pag(incaricoDetFP.getErr_anno_pag());
			this.setErr_semestre_pag(incaricoDetFP.getErr_semestre_pag());
			this.setErr_importo_pag(incaricoDetFP.getErr_importo_pag());
		} catch (Exception e) {
			throw new it.cnr.jada.comp.ApplicationException(e.toString());
		}
	}

	public boolean similar(Incarichi_comunicati_fp_detBulk bulk) {
		return this.getEsercizio_repertorio().equals(bulk.getEsercizio_repertorio()) &&
		       this.getPg_repertorio().equals(bulk.getPg_repertorio()) &&
		       this.getTipo_record().equals(bulk.getTipo_record()) &&
		       this.getPg_record().equals(bulk.getPg_record()) &&
		       this.getAnno_pag().equals(bulk.getAnno_pag()) &&
		       this.getSemestre_pag().equals(bulk.getSemestre_pag()) &&
		       Utility.equalsNull(this.getTipo_record_det(), bulk.getTipo_record_det()) &&	       
		       Utility.equalsNull(this.getImporto_pag(),bulk.getImporto_pag()) &&
		       Utility.equalsNull(this.getEsito_incarico_pagamento(), bulk.getEsito_incarico_pagamento()) &&
		       Utility.equalsNull(this.getErr_anno_pag(), bulk.getErr_anno_pag()) &&
		       Utility.equalsNull(this.getErr_semestre_pag(), bulk.getErr_semestre_pag()) &&
		       Utility.equalsNull(this.getErr_importo_pag(), bulk.getErr_importo_pag());
	}
}

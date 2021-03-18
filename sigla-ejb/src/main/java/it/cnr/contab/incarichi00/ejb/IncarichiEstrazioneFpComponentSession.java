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

package it.cnr.contab.incarichi00.ejb;

import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
import java.math.BigDecimal;

@Remote
public interface IncarichiEstrazioneFpComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk completaIncaricoElencoFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> param1) throws it.cnr.jada.comp.ComponentException;
	void aggiornaIncarichiComunicatiFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk param1) throws it.cnr.jada.comp.ComponentException;
	java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk> getIncarichiComunicatiEliminatiFP(it.cnr.jada.UserContext param0, Integer param1, Integer param2) throws it.cnr.jada.comp.ComponentException;
	java.util.List<it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk> getPagatoPerSemestre(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException;
	BigDecimal getPagatoIncarico(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException;
	void comunicaPerla2018(UserContext userContext, Incarichi_repertorioBulk incaricoRepertorio) throws ComponentException;
	void comunicaPerla2018(UserContext userContext, V_incarichi_elenco_fpBulk incaricoElenco) throws ComponentException;
}
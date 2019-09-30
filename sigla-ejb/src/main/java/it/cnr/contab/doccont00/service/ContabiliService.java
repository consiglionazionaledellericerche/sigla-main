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

package it.cnr.contab.doccont00.service;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.jada.comp.ApplicationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContabiliService extends StoreService {
	private transient static final Logger logger = LoggerFactory.getLogger(ContabiliService.class);

	public List<String> getNodeRefContabile(MandatoBulk mandato) throws ApplicationException{
		return getNodeRefContabile(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato(), "MAN");
	}

	public List<String> getNodeRefContabile(ReversaleBulk reversale) throws ApplicationException{
		return getNodeRefContabile(reversale.getEsercizio(), reversale.getCd_cds(), reversale.getPg_reversale(), "REV");
	}

	public List<String> getNodeRefContabile(Integer esercizio, String cds, Long pgMandato, String tipo) throws ApplicationException{
		List<String> ids = new ArrayList<String>();
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" join sigla_contabili_aspect:document contabili on doc.cmis:objectId = contabili.cmis:objectId");
		query.append(" where contabili.sigla_contabili_aspect:esercizio = ").append(esercizio);
		query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		if (esercizio.compareTo(2016) >= 0)
			query.append(" and contabili.sigla_contabili_aspect:tipo = '").append(tipo).append("'");
		else {
			if (tipo.equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_REV)) {
				query.append(" and contabili.sigla_contabili_aspect:tipo = '").append(tipo).append("'");
				query.append(" and contabili.sigla_contabili_aspect:cds = '").append("Rev").append("'");
			} else {
				query.append(" and contabili.sigla_contabili_aspect:cds = '").append(cds).append("'");
			}
		}
		return search(query.toString()).stream()
				.map(StorageObject::getKey)
				.collect(Collectors.toList());
	}
	public InputStream getStreamContabile(Integer esercizio, String cds, Long pgMandato, String tipo) throws Exception{
		List<String> ids = getNodeRefContabile(esercizio, cds, pgMandato, tipo);
		if (ids != null){
			if (ids.size() == 1){
				return getResource(ids.get(0));
			}else{
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.setDestinationStream(new ByteArrayOutputStream());
				try {
					for (String id : ids) {
						ut.addSource(getResource(id));
					}
					ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
				}  catch (IOException e) {
					throw e;
				}
			}
		}
		return null;
	}

	public InputStream getStreamContabile(MandatoBulk mandato) throws Exception{
		return getStreamContabile(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato(), "MAN");
	}
	public InputStream getStreamContabile(ReversaleBulk reversale) throws Exception{
		return getStreamContabile(reversale.getEsercizio(), reversale.getCd_cds(), reversale.getPg_reversale(), "REV");
	}
}
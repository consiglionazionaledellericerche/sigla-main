package it.cnr.contab.doccont00.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;

public class ContabiliService extends CMISService {
	
	public List<String> getNodeRefContabile(MandatoBulk mandato){
		return getNodeRefContabile(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato());
	}

	public List<String> getNodeRefContabile(Integer esercizio, String cds, Long pgMandato){
		List<String> ids = new ArrayList<String>();
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" join sigla_contabili_aspect:document contabili on doc.cmis:objectId = contabili.cmis:objectId");
		query.append(" where contabili.sigla_contabili_aspect:esercizio = ").append(esercizio);
		query.append(" and contabili.sigla_contabili_aspect:cds = '").append(cds).append("'");
		query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		query.append(" order by doc.cmis:creationDate DESC");
		ListNodePage<Node> results = search(query, Boolean.FALSE);
		if (results.isEmpty())
			return null;
		else {
			for (Node node : results) {
				ids.add((String) node.getPropertyValue("cmis:objectId"));
			}
			return ids;
		}
	}
	public InputStream getStreamContabile(Integer esercizio, String cds, Long pgMandato) throws Exception{
		List<String> ids = getNodeRefContabile(esercizio, cds, pgMandato);
		if (ids != null){
			if (ids.size() == 1){
				return getResource(getNodeByNodeRef(ids.get(0)));
			}else{
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.setDestinationStream(new ByteArrayOutputStream());
				for (String id : ids) {
					ut.addSource(getResource(getNodeByNodeRef(id)));
				}
				try {
					ut.mergeDocuments();
					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
				} catch (COSVisitorException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return null;
	}
	
	public InputStream getStreamContabile(MandatoBulk mandato) throws Exception{
		return getStreamContabile(mandato.getEsercizio(), mandato.getCd_cds(), mandato.getPg_mandato());
	}
}

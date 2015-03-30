/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.PasswordAuthentication;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis2.builder.unknowncontent.InputStreamDataSource;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.cmis.CMISDocAmmAspect;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.gov.fatturapa.sdi.messaggi.v1.EsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.RiferimentoFatturaType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.FileSdIType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.ObjectFactory;
public class DocumentoEleTrasmissioneHome extends BulkHome {
	public DocumentoEleTrasmissioneHome(Connection conn) {
		super(DocumentoEleTrasmissioneBulk.class, conn);
	}
	public DocumentoEleTrasmissioneHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleTrasmissioneBulk.class, conn, persistentCache);
	}	
}
package it.cnr.contab.docamm00.docs.bulk;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.cmis.acl.Role;
import it.cnr.contab.cmis.acl.SIGLAGroups;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.service.DocAmmCMISService;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.service.LDAPService;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

public class Documento_genericoHome extends BulkHome implements
		IDocumentoAmministrativoSpesaHome {
	public Documento_genericoHome(Class aClass, java.sql.Connection conn) {
		super(aClass, conn);
	}

	public Documento_genericoHome(Class aClass, java.sql.Connection conn,
			PersistentCache persistentCache) {
		super(aClass, conn, persistentCache);
	}

	public Documento_genericoHome(java.sql.Connection conn) {
		super(Documento_genericoBulk.class, conn);
	}

	public Documento_genericoHome(java.sql.Connection conn,
			PersistentCache persistentCache) {
		super(Documento_genericoBulk.class, conn, persistentCache);
	}

	/**
	 * Inizializza la chiave primaria di un OggettoBulk per un inserimento. Da
	 * usare principalmente per riempire i progressivi automatici.
	 * 
	 * @param bulk
	 *            l'OggettoBulk da inizializzare
	 */
	public java.sql.Timestamp findForMaxDataRegistrazione(
			it.cnr.jada.UserContext userContext, Documento_genericoBulk doc)
			throws PersistencyException, it.cnr.jada.comp.ComponentException {

		if (doc == null)
			return null;
		try {
			java.sql.Connection contact = getConnection();
			String query = "SELECT MAX(DATA_REGISTRAZIONE) FROM "
					+ it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "DOCUMENTO_GENERICO " + "WHERE CD_UO_ORIGINE= '"
					+ doc.getCd_uo_origine() + "' AND ESERCIZIO = "
					+ doc.getEsercizio().intValue();

			java.sql.ResultSet rs = contact.createStatement().executeQuery(
					query);

			if (rs.next())
				return rs.getTimestamp(1);
			else
				return null;
		} catch (java.sql.SQLException sqle) {
			throw new PersistencyException(sqle);
		}
	}

	public SQLBuilder selectValuta(Documento_genericoBulk documentoBulk,
			it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome,
			it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {

		SQLBuilder sql = divisaHome.createSQLBuilder();

		sql.addTableToHeader("CAMBIO");
		sql.addSQLJoin("DIVISA.CD_DIVISA", "CAMBIO.CD_DIVISA");

		return sql;
	}

	/**
	 * Insert the method's description here. Creation date: (5/10/2002 3:27:22
	 * PM)
	 */
	public void updateFondoEconomale(Fondo_spesaBulk spesa)
			throws it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.bulk.OutdatedResourceException,
			it.cnr.jada.bulk.BusyResourceException {

		if (spesa == null)
			return;

		Documento_genericoBulk doc = (Documento_genericoBulk) spesa
				.getDocumento();

		lock(doc);

		StringBuffer stm = new StringBuffer("UPDATE  ");
		stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
		stm.append(getColumnMap().getTableName());
		stm
				.append(" SET STATO_PAGAMENTO_FONDO_ECO = ?, DT_PAGAMENTO_FONDO_ECO = ?, PG_VER_REC = PG_VER_REC+1, DUVA = ?, UTUV = ?");
		stm.append(" WHERE (");
		stm
				.append("CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_DOCUMENTO_GENERICO = ? AND CD_TIPO_DOCUMENTO_AMM = ?)");

		try {
			LoggableStatement loggablestatement = new LoggableStatement(
					getConnection(), stm.toString(), true, this.getClass());
			try {
				loggablestatement.setString(1, (spesa.isToBeCreated() || spesa
						.isToBeUpdated()) ? doc.REGISTRATO_IN_FONDO_ECO
						: doc.FONDO_ECO);
				if (spesa.isToBeCreated() || spesa.isToBeUpdated())
					loggablestatement.setTimestamp(2, spesa.getDt_spesa());
				else
					loggablestatement.setNull(2, java.sql.Types.TIMESTAMP);

				loggablestatement.setTimestamp(3, getServerTimestamp());
				loggablestatement.setString(4, spesa.getUser());

				loggablestatement.setString(5, doc.getCd_cds());
				loggablestatement.setString(6, doc.getCd_unita_organizzativa());
				loggablestatement.setInt(7, doc.getEsercizio().intValue());
				loggablestatement.setLong(8, doc.getPg_documento_generico()
						.longValue());
				loggablestatement.setString(9, doc.getCd_tipo_doc_amm());

				loggablestatement.executeUpdate();
			} finally {
				try {
					loggablestatement.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException e) {
			throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance()
					.handleSQLException(e, spesa);
		}
	}

	public TerzoBulk findTerzo(UserContext userContext, Documento_genericoBulk docGen) throws PersistencyException{
		PersistentHome docRigaHome = getHomeCache().getHome(Documento_generico_rigaBulk.class);
		SQLBuilder sql = docRigaHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"documento_generico",SQLBuilder.EQUALS,docGen);
		List<Documento_generico_rigaBulk> righe = docRigaHome.fetchAll(sql);
		for (Documento_generico_rigaBulk riga : righe) {
			return riga.getTerzo();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public Boolean archiviaStampa(UserContext userContext,
			MandatoBulk mandato,
			IDocumentoAmministrativoSpesaBulk docAmm)
			throws IntrospectionException, PersistencyException {
		Documento_genericoBulk docGen = (Documento_genericoBulk)docAmm;
		Boolean isDipendente = Boolean.FALSE;
		Integer matricola = null;
		TerzoBulk terzo = findTerzo(userContext, docGen);
		if (terzo == null || terzo.getAnagrafico() == null)
			return Boolean.FALSE;
		
		Collection<RapportoBulk> rapporti = ((AnagraficoHome) getHomeCache()
				.getHome(AnagraficoBulk.class)).findRapporti(terzo.getAnagrafico());
		for (RapportoBulk rapporto : rapporti) {
			if (rapporto.getMatricola_dipendente() != null) {
				isDipendente = Boolean.TRUE;
				matricola = rapporto.getMatricola_dipendente();
				break;
			}
		}
		if (isDipendente) {
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);
			print.setReport("/docamm/docamm/vpg_doc_generico.jasper");
			print.setNomeFile("Documento Generico n. "
					+ docGen.getPg_documento_generico()
					+ " della UO "
					+ docGen.getCd_unita_organizzativa()
					+ " del "
					+ new SimpleDateFormat("dd-MM-yyyy").format(docGen
							.getDt_documento()) + ".pdf");
			print.setUtcr(userContext.getUser());
			print.addParam("aCd_cds", docGen.getCd_cds(), String.class);
			print.addParam("aCd_uo", docGen.getCd_unita_organizzativa(), String.class);
			print.addParam("aEs", docGen.getEsercizio(), Integer.class);
			print.addParam("aCd_tipo_doc_amm", docGen.getTipo_documento().getCd_tipo_documento_amm(), String.class);
			print.addParam("aPg_da", docGen.getPg_documento_generico(), Long.class);
			print.addParam("aPg_a", docGen.getPg_documento_generico(), Long.class);
			print.addParam("aDt_da", DateUtils.truncate(docGen.getDt_documento()) , Date.class);
			print.addParam("aDt_a", DateUtils.truncate(docGen.getDt_documento()), Date.class);
			print.addParam("aCd_terzo", "%", String.class);
			try {
				docGen
				.setUnitaOrganizzativa((Unita_organizzativaBulk) getHomeCache()
						.getHome(Unita_organizzativaBulk.class)
						.findByPrimaryKey(
								new Unita_organizzativaBulk(
										docGen
												.getCd_unita_organizzativa())));
				docGen.setMandatoPagamento(mandato);
				docGen.setTypePayment("NonConcFormazReddito");
				LDAPService ldapService = SpringUtil.getBean("ldapService",
						LDAPService.class);
				String[] uidMail = ldapService.getLdapUserFromMatricola(
						userContext, matricola);
				
				DocAmmCMISService docAmmCMISService = SpringUtil.getBean("docAmmCMISService",
						DocAmmCMISService.class);

				CMISPath cmisPath = SpringUtil
					.getBean("cmisPathNonConcFormazReddito",
							CMISPath.class);
				cmisPath = docAmmCMISService.createFolderIfNotPresent(cmisPath, String.valueOf(mandato.getEsercizio()), 
						"Esercizio "+mandato.getEsercizio(), "Esercizio "+mandato.getEsercizio());
				cmisPath = docAmmCMISService.createFolderIfNotPresent(cmisPath, "Dipendenti", 
						"Dipendenti", "Dipendenti");
				cmisPath = docAmmCMISService.createFolderIfNotPresent(cmisPath, "Matricola "+matricola, 
						uidMail[0], uidMail[0]);

				Report report = SpringUtil.getBean("printService",
						PrintService.class).executeReport(userContext,
						print);
				Node node = docAmmCMISService.storePrintDocument(docGen, report, cmisPath, 
						Permission.construct(uidMail[0], docAmmCMISService.getRoleConsumer()),
						Permission.construct(SIGLAGroups.GROUP_EMPPAY_GROUP.name(), docAmmCMISService.getRoleCoordinator()));
				/**
				 * Add another parent to Node
				 */
				docAmmCMISService.addAnotherParentToNode(mandato, docAmm, uidMail[0], node, "cmisPathNonConcFormazReddito");
			}catch (CmisConstraintException e) {
				throw new PersistencyException(e);
			}catch (Exception e) {
				throw new PersistencyException(e);
			}
		}
		return Boolean.TRUE;
	}
}

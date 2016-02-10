package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

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
}

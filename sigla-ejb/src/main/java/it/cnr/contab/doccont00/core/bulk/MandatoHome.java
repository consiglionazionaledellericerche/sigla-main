package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaHome;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaHome;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.service.LDAPService;
import it.cnr.contab.util.service.MailService;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public abstract class MandatoHome extends BulkHome {
	public MandatoHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public MandatoHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un MandatoHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public MandatoHome(java.sql.Connection conn) {
		super(MandatoBulk.class,conn);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un MandatoHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public MandatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(MandatoBulk.class,conn,persistentCache);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param mandato	
	 * @return 
	 * @throws PersistencyException	
	 */
	public Timestamp findDataUltimoMandatoPerCds( MandatoBulk mandato ) throws PersistencyException
	{
		try
		{
			LoggableStatement ps = new LoggableStatement(getConnection(),
				"SELECT TRUNC(MAX(DT_EMISSIONE)) " +			
				"FROM " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
				"MANDATO WHERE " +
				"ESERCIZIO = ? AND CD_CDS = ?",true,this.getClass());
			try
			{
				ps.setObject( 1, mandato.getEsercizio() );
				ps.setString( 2, mandato.getCds().getCd_unita_organizzativa() );
			
				ResultSet rs = ps.executeQuery();
				try
				{
					if(rs.next())
						return rs.getTimestamp(1);
					else
						return null;
				}
				catch( SQLException e )
				{
					throw new PersistencyException( e );
				}
				finally
				{
					try{rs.close();}catch( java.sql.SQLException e ){};
				}
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{ps.close();}catch( java.sql.SQLException e ){};	
			}
		}
		catch ( SQLException e )
		{
				throw new PersistencyException( e );
		}
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param mandato	
	 * @return 
	 * @throws PersistencyException	
	 * @throws IntrospectionException	
	 */
	public abstract Collection findMandato_riga( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException, IntrospectionException;
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param mandato	
	 * @return 
	 * @throws PersistencyException	
	 * @throws IntrospectionException	
	 */
	public abstract Mandato_terzoBulk findMandato_terzo( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException, IntrospectionException;
	/**
	 * Metodo per cercare i sospesi associati al mandato.
	 *
	 * @param mandato <code>MandatoBulk</code> il mandato
	 *
	 * @return result i sospesi associati al mandato
	 *
	 */
	public Collection findSospeso_det_usc( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException, IntrospectionException
	{
		PersistentHome home = getHomeCache().getHome( Sospeso_det_uscBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		sql.addClause( "AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
		sql.addClause( "AND", "ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_SOSPESO);
	//	sql.addClause( "AND", "stato", sql.EQUALS, Sospeso_det_uscBulk.STATO_DEFAULT);	
		Collection result = home.fetchAll( sql);
		getHomeCache().fetchAll(userContext);
		return result;
	}	
	
	public MandatoBulk findMandato(UserContext userContext, IDocumentoAmministrativoSpesaBulk docAmm) throws PersistencyException{
		PersistentHome homeMandatoRiga = getHomeCache().getHome( Mandato_rigaBulk.class );
		SQLBuilder sql = homeMandatoRiga.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, docAmm.getEsercizio());
		sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, docAmm.getCd_cds());
		sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, docAmm.getCd_uo());
		sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, docAmm.getCd_tipo_doc_amm());
		sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, docAmm.getPg_doc_amm());
		List<Mandato_rigaBulk> righe = homeMandatoRiga.fetchAll(sql);
		for (Mandato_rigaBulk mandato_rigaBulk : righe) {
			return mandato_rigaBulk.getMandato();
		}
		return null;
		
	}
		/**
		 * Imposta il pg_mandato di un oggetto <code>MandatoBulk</code>.
		 *
		 * @param mandato <code>OggettoBulkBulk</code>
		 *
		 * @exception PersistencyException
		 */
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
	{
		try
		{
			MandatoBulk mandato = (MandatoBulk) bulk;
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome( Numerazione_doc_contBulk.class );
			Long pg = numHome.getNextPg(userContext, mandato.getEsercizio(), mandato.getCd_cds(), Numerazione_doc_contBulk.TIPO_MAN, mandato.getUser());		
			mandato.setPg_mandato( pg );
		}catch ( IntrospectionException e ){
			throw new PersistencyException( e );
		}
		catch ( ApplicationException e ){
			throw new ComponentException( e );
		}
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param bulk	
	 * @return 
	 * @throws PersistencyException	
	 */
	public java.util.Hashtable loadTipoDocumentoKeys( MandatoBulk bulk ) throws PersistencyException
	{
		SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
		sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "S" );
		List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
		Hashtable ht = new Hashtable();
		Tipo_documento_ammBulk tipo;
		for (Iterator i = result.iterator(); i.hasNext(); )
		{
			tipo = (Tipo_documento_ammBulk) i.next();
			ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
		}	
		return ht;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @param bulk	
	 * @return 
	 * @throws PersistencyException	
	 */
	public java.util.Hashtable loadTipoDocumentoPerRicercaKeys( MandatoBulk bulk ) throws PersistencyException
	{
		SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	//	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "S" );
		sql.openParenthesis( "AND");
		sql.addSQLClause( "AND", "fl_manrev_utente", sql.EQUALS, "M" );
		sql.addSQLClause( "OR", "fl_manrev_utente", sql.EQUALS, "E" );
		sql.closeParenthesis();	
		List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
		Hashtable ht = new Hashtable();
		Tipo_documento_ammBulk tipo;
		for (Iterator i = result.iterator(); i.hasNext(); )
		{
			tipo = (Tipo_documento_ammBulk) i.next();
			ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
		}	
		return ht;
	}
	
	private Date findDataDiCompetenzaDocumentoAmm(UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException{
		Date result = null; 
		Collection<Mandato_rigaBulk>  righeMandato = findMandato_riga(userContext, mandato);
		for (Mandato_rigaBulk mandatoRiga : righeMandato) {
			try{
				IDocumentoAmministrativoSpesaBulk docAmm = SpringUtil.getBean(mandatoRiga.getCd_tipo_documento_amm(), IDocumentoAmministrativoSpesaBulk.class);
				docAmm.setCd_cds(mandatoRiga.getCd_cds_doc_amm());
				docAmm.setCd_uo(mandatoRiga.getCd_uo_doc_amm());
				docAmm.setEsercizio(mandatoRiga.getEsercizio_doc_amm());
				docAmm.setPg_doc_amm(mandatoRiga.getPg_doc_amm());
				docAmm.setCd_tipo_doc_amm(mandatoRiga.getCd_tipo_documento_amm());
				docAmm = (IDocumentoAmministrativoSpesaBulk)getHomeCache().getHome(docAmm.getClass()).findByPrimaryKey(docAmm);
				if (result == null)
					result = docAmm.getDt_da_competenza_coge();
				else{
					if (docAmm.getDt_da_competenza_coge() != null && result.before(docAmm.getDt_da_competenza_coge()) )
						result = docAmm.getDt_da_competenza_coge();
				}
			}catch(NoSuchBeanDefinitionException _ex){
			}
		}
		if (result == null)
			result = mandato.getDt_emissione();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean isAvvisoDiPagamentoMandato(UserContext userContext, MandatoBulk mandato, Boolean bonifico) throws ComponentException{
		try {
			TerzoHome terzoHome = (TerzoHome) getHomeCache().getHome( TerzoBulk.class );
			Fondo_spesaHome fondo_spesaHome = (Fondo_spesaHome) getHomeCache().getHome( Fondo_spesaBulk.class );
			SQLBuilder sqlFondoSpesa = fondo_spesaHome.createSQLBuilder();
			sqlFondoSpesa.addClause(FindClause.AND, "esercizio_mandato", SQLBuilder.EQUALS, mandato.getEsercizio());
			sqlFondoSpesa.addClause(FindClause.AND, "cd_cds_mandato", SQLBuilder.EQUALS, mandato.getCd_cds());
			sqlFondoSpesa.addClause(FindClause.AND, "pg_mandato", SQLBuilder.EQUALS, mandato.getPg_mandato());
			boolean existFondoSpesa = fondo_spesaHome.fetchAll(sqlFondoSpesa).isEmpty();
			if (!existFondoSpesa)
				return Boolean.FALSE;
			Mandato_terzoBulk mandatoTerzo = findMandato_terzo(userContext, mandato);
			Integer matricola = terzoHome.findMatricolaDipendente(userContext, mandatoTerzo.getTerzo(),findDataDiCompetenzaDocumentoAmm(userContext,mandato));
			if (matricola != null && (mandato.getTi_mandato().equalsIgnoreCase( MandatoBulk.TIPO_PAGAMENTO) ||
					mandato.getTi_mandato().equalsIgnoreCase( MandatoBulk.TIPO_REGOLAM_SOSPESO))){
				Collection<Mandato_rigaBulk>  righeMandato = findMandato_riga(userContext, mandato);
				for (Mandato_rigaBulk mandatoRiga : righeMandato) {
					Modalita_pagamentoBulk modalitaPagamento = mandatoRiga.getModalita_pagamento();
					if (bonifico){
						return Boolean.TRUE;
					}else{
						if (modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.ALTRO) ||
								modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.QUIETANZA)){
							return Boolean.TRUE;
						}
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw new ComponentException( e );
		}		
	}
	
	@SuppressWarnings("unchecked")
	public Boolean isAvvisoDiPagamentoMandatoReintegroFondo(UserContext userContext, MandatoBulk mandato) throws ComponentException{
		try {
			TerzoHome terzoHome = (TerzoHome) getHomeCache().getHome( TerzoBulk.class );
			Mandato_terzoBulk mandatoTerzo = findMandato_terzo(userContext, mandato);
			Integer matricola = terzoHome.findMatricolaDipendente(userContext, mandatoTerzo.getTerzo(),findDataDiCompetenzaDocumentoAmm(userContext,mandato));
			if (matricola != null && (mandato.getTi_mandato().equalsIgnoreCase( MandatoBulk.TIPO_PAGAMENTO) ||
					mandato.getTi_mandato().equalsIgnoreCase( MandatoBulk.TIPO_REGOLAM_SOSPESO))){
				Collection<Mandato_rigaBulk>  righeMandato = findMandato_riga(userContext, mandato);
				for (Mandato_rigaBulk mandatoRiga : righeMandato) {
					Modalita_pagamentoBulk modalitaPagamento = mandatoRiga.getModalita_pagamento();
					if (!(modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.ALTRO) ||
							modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.QUIETANZA))){
						return Boolean.TRUE;
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw new ComponentException( e );
		}		
	}
	
	private void gestioneAvvisoDiPagamento(MandatoBulk mandato, Mandato_rigaBulk mandatoRiga, StringBuffer text, String innerTextBean) throws ComponentException, PersistencyException{
		MailService mailService = SpringUtil.getBean("avviso.di.pagamento.mail.service", MailService.class);
		IDocumentoAmministrativoSpesaBulk docAmm = SpringUtil.getBean(mandatoRiga.getCd_tipo_documento_amm(), IDocumentoAmministrativoSpesaBulk.class);
		docAmm.setCd_cds(mandatoRiga.getCd_cds_doc_amm());
		docAmm.setCd_uo(mandatoRiga.getCd_uo_doc_amm());
		docAmm.setEsercizio(mandatoRiga.getEsercizio_doc_amm());
		docAmm.setPg_doc_amm(mandatoRiga.getPg_doc_amm());
		docAmm.setCd_tipo_doc_amm(mandatoRiga.getCd_tipo_documento_amm());
		docAmm = (IDocumentoAmministrativoSpesaBulk)getHomeCache().getHome(docAmm.getClass()).findByPrimaryKey(docAmm);
						
		Properties prop = new Properties();
		prop.put("ds_tipo_documento_amm", mandato.getTipoDocumentoKeys().get(mandatoRiga.getCd_tipo_documento_amm()));
		prop.put("pg_doc_amm", String.valueOf(docAmm.getPg_doc_amm()));
		prop.put("data_doc_amm", SpringUtil.getBean("dateShortFormat", DateFormat.class).format(docAmm.getDt_documento()));
		prop.put("cd_unita_organizzativa",docAmm.getCd_uo());
		prop.put("ds_doc_amm",docAmm.getDescrizione_spesa()==null?"":docAmm.getDescrizione_spesa());
		text.append(mailService.resolvePalceHolder(SpringUtil.getBean(innerTextBean, String.class), prop));
	}

	private Boolean archiviaAvvisoDiPagamento(UserContext userContext, Mandato_rigaBulk mandatoRiga) throws ComponentException, PersistencyException, IntrospectionException{
		IDocumentoAmministrativoSpesaBulk docAmm = SpringUtil.getBean(mandatoRiga.getCd_tipo_documento_amm(), IDocumentoAmministrativoSpesaBulk.class);
		docAmm.setCd_cds(mandatoRiga.getCd_cds_doc_amm());
		docAmm.setCd_uo(mandatoRiga.getCd_uo_doc_amm());
		docAmm.setEsercizio(mandatoRiga.getEsercizio_doc_amm());
		docAmm.setPg_doc_amm(mandatoRiga.getPg_doc_amm());
		docAmm.setCd_tipo_doc_amm(mandatoRiga.getCd_tipo_documento_amm());
		docAmm = (IDocumentoAmministrativoSpesaBulk)getHomeCache().getHome(docAmm.getClass()).findByPrimaryKey(docAmm);
		return ((IDocumentoAmministrativoSpesaHome)getHomeCache().getHome((Persistent) docAmm)).archiviaStampa(userContext, mandatoRiga.getMandato(), docAmm);
	}
	
	@SuppressWarnings("unchecked")
	public String sendAvvisoDiPagamentoPerBonifico(UserContext userContext, MandatoBulk mandato) throws ComponentException{
		try {
			TerzoHome terzoHome = (TerzoHome) getHomeCache().getHome( TerzoBulk.class );
			MailService mailService = SpringUtil.getBean("avviso.di.pagamento.mail.service", MailService.class);
			String subject;
			StringBuffer text = new StringBuffer();
			subject = SpringUtil.getBean("avviso.di.pagamento.mail.subject", String.class);
			text.append(SpringUtil.getBean("avviso.di.pagamento.per.bonifico.mail.text.header", String.class));
			mandato.setTipoDocumentoKeys(loadTipoDocumentoKeys(mandato));
			Mandato_terzoBulk mandatoTerzo = findMandato_terzo(userContext, mandato);
			Collection<Mandato_rigaBulk>  righeMandato = findMandato_riga(userContext, mandato);
			Boolean archiviato = Boolean.FALSE;
			Boolean inviaMail = Boolean.TRUE;
			for (Mandato_rigaBulk mandatoRiga : righeMandato) {
				gestioneAvvisoDiPagamento(mandato, mandatoRiga, text, "avviso.di.pagamento.mail.text.inner");
				archiviato = archiviaAvvisoDiPagamento(userContext, mandatoRiga);
				Modalita_pagamentoBulk modalitaPagamento = mandatoRiga.getModalita_pagamento();
				if (modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.ALTRO) ||
						modalitaPagamento.getRif_modalita_pagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.QUIETANZA))
					inviaMail = Boolean.FALSE;
			}
			if (archiviato)
				text.append(SpringUtil.getBean("avviso.di.pagamento.per.bonifico.mail.text.footer", String.class));
			Integer matricola = terzoHome.findMatricolaDipendente(userContext, mandatoTerzo.getTerzo(),findDataDiCompetenzaDocumentoAmm(userContext,mandato));
			if (matricola != null){
				String mailAddress = SpringUtil.getBean("ldapService", LDAPService.class).getLdapUserFromMatricola(userContext, matricola)[1];
				if (inviaMail)
					mailService.send(Arrays.asList(mailAddress), subject, text.toString());
			}
			return MandatoBulk.STATO_INVIO_AVV_PAG_N;
		}catch (NoSuchBeanDefinitionException e) {
			return MandatoBulk.STATO_INVIO_AVV_PAG_N;
		}catch (Exception e) {
			SpringUtil.getBean("mailService", MailService.class).sendErrorMessage(
					"Errore durante l'archiviazione del Mandato di Pagamento: "+
					mandato.getEsercizio()+
					"/"+mandato.getCd_unita_organizzativa()+
					"/"+mandato.getPg_mandato(), 
					e);
			return MandatoBulk.STATO_INVIO_AVV_PAG_E;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void sendAvvisoDiPagamento(UserContext userContext, MandatoBulk mandato) throws ComponentException{
		try {
			TerzoHome terzoHome = (TerzoHome) getHomeCache().getHome( TerzoBulk.class );
			MailService mailService = SpringUtil.getBean("avviso.di.pagamento.mail.service", MailService.class);
			String stato = mandato.getStato();
			String subject;
			StringBuffer text = new StringBuffer();
			if (stato.equalsIgnoreCase(MandatoBulk.STATO_MANDATO_ANNULLATO)){
				subject = SpringUtil.getBean("avviso.di.annullamento.mail.subject", String.class);
				text.append(SpringUtil.getBean("avviso.di.annullamento.mail.text.header", String.class));
			}else{
				subject = SpringUtil.getBean("avviso.di.pagamento.mail.subject", String.class);
				text.append(SpringUtil.getBean("avviso.di.pagamento.mail.text.header", String.class));
			}
			mandato.setTipoDocumentoKeys(loadTipoDocumentoKeys(mandato));
			Mandato_terzoBulk mandatoTerzo = findMandato_terzo(userContext, mandato);
			Collection<Mandato_rigaBulk>  righeMandato = findMandato_riga(userContext, mandato);
			for (Mandato_rigaBulk mandatoRiga : righeMandato) {
				gestioneAvvisoDiPagamento(mandato, mandatoRiga, text, "avviso.di.pagamento.mail.text.inner");
			}
			if (stato.equalsIgnoreCase(MandatoBulk.STATO_MANDATO_ANNULLATO))
				text.append(SpringUtil.getBean("avviso.di.annullamento.mail.text.footer", String.class));
			else
				text.append(SpringUtil.getBean("avviso.di.pagamento.mail.text.footer", String.class));
			Integer matricola = terzoHome.findMatricolaDipendente(userContext, mandatoTerzo.getTerzo(),findDataDiCompetenzaDocumentoAmm(userContext,mandato));
			String mailAddress = SpringUtil.getBean("ldapService", LDAPService.class).getLdapUserFromMatricola(userContext, matricola)[1];
			mailService.send(Arrays.asList(mailAddress), subject, text.toString());
		} catch (Exception e) {
			throw new ComponentException( e );
		}		
	}
}

package it.cnr.contab.incarichi00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.file.bulk.Gruppo_fileBulk;
import it.cnr.contab.config00.file.bulk.Gruppo_fileHome;
import it.cnr.contab.config00.file.bulk.Tipo_fileBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioHome;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiEstrazioneFpComponentSession;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametri_configBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametri_configHome;
import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpHome;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fpHome;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detBulk;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_comunicati_fp_detHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import oracle.sql.BLOB;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IncarichiEstrazioneFpComponent extends CRUDComponent {
	public V_incarichi_elenco_fpBulk completaIncaricoElencoFP(UserContext userContext, V_incarichi_elenco_fpBulk bulk) throws ComponentException{
		try {
			Incarichi_repertorioBulk incaricoRepertorio = (Incarichi_repertorioBulk)getHome(userContext, Incarichi_repertorioBulk.class).findByPrimaryKey(new Incarichi_repertorioBulk(bulk.getEsercizio(), bulk.getPg_repertorio()));
			Incarichi_proceduraBulk incaricoProcedura = (Incarichi_proceduraBulk)getHome(userContext, Incarichi_proceduraBulk.class).findByPrimaryKey(new Incarichi_proceduraBulk(bulk.getEsercizio_procedura(), bulk.getPg_procedura()));

			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
			incaricoRepertorio.setV_terzo(home.loadVTerzo(userContext,Tipo_rapportoBulk.ALTRO, incaricoRepertorio.getCd_terzo(), incaricoRepertorio.getDt_inizio_validita(), incaricoRepertorio.getDt_inizio_validita()));
			
			incaricoRepertorio.setIncarichi_procedura(incaricoProcedura);
			bulk.setIncaricoRepertorio(incaricoRepertorio);
			
			return bulk;
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		}
	}

	public void aggiornaIncarichiComunicatiFP(UserContext userContext, List<Incarichi_comunicati_fpBulk> list) throws ComponentException{
		for (Iterator<Incarichi_comunicati_fpBulk> iterator = list.iterator(); iterator.hasNext();)
			aggiornaIncarichiComunicatiFP(userContext, iterator.next());
	}

	public void aggiornaIncarichiComunicatiFP(UserContext userContext, Incarichi_comunicati_fpBulk bulk) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fp_detHome incFpDetHome = (Incarichi_comunicati_fp_detHome)getTempHome(userContext, Incarichi_comunicati_fp_detBulk.class);
			Incarichi_comunicati_fpBulk incaricoPrincFpDB = null;
			
			if (!bulk.getTipo_record().equals(Incarichi_comunicati_fpBulk.TIPO_RECORD_INVIATO_NEW) &&  
				!bulk.getTipo_record().equals(Incarichi_comunicati_fpBulk.TIPO_RECORD_RICEVUTO_NEW)){

				incaricoPrincFpDB = (Incarichi_comunicati_fpBulk)incFpHome.findIncaricoComunicatoAggFP(bulk);
				
				if (incaricoPrincFpDB!=null){
					bulk.setEsercizio_repertorio(incaricoPrincFpDB.getEsercizio_repertorio());
					bulk.setPg_repertorio(incaricoPrincFpDB.getPg_repertorio());
				} else
					throw new ComponentException("Record di tipo aggiornamento ma manca il record principale.");
			} else {
				incaricoPrincFpDB = (Incarichi_comunicati_fpBulk)incFpHome.findByPrimaryKey(new Incarichi_comunicati_fpBulk(bulk.getEsercizio_repertorio(), bulk.getPg_repertorio(), Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO, new Long(1)));
			}
				
			//verifico che non sia già stato inserito un record identico
			Long maxProg = (Long)incFpHome.findMax(bulk,"pg_record");
			
			Incarichi_comunicati_fpBulk incFpLast = null;
			if (maxProg!=null)
				incFpLast = (Incarichi_comunicati_fpBulk)incFpHome.findByPrimaryKey(new Incarichi_comunicati_fpBulk(bulk.getEsercizio_repertorio(), bulk.getPg_repertorio(), bulk.getTipo_record(), maxProg));

			if (incFpLast==null || !incFpLast.similar(bulk)){
				bulk.setUser(CNRUserContext.getUser(userContext));
				bulk.setToBeCreated();
				insertBulk(userContext,bulk);
				
				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();
					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						pagam.setUser(CNRUserContext.getUser(userContext));
						pagam.setToBeCreated();
						insertBulk(userContext, pagam);
					}
				}
			}
				
			//Recupero il record principale
			if (incaricoPrincFpDB!=null){
				//è stato già comunicato un nuovo incarico......devo aggiornarlo
				incaricoPrincFpDB.updateFrom(userContext, bulk);
				incaricoPrincFpDB.setToBeUpdated();
				updateBulk(userContext,incaricoPrincFpDB);

				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();

					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						Incarichi_comunicati_fp_detBulk pagamPrincDB =  (Incarichi_comunicati_fp_detBulk)incFpDetHome.findByPrimaryKey(new Incarichi_comunicati_fp_detBulk(incaricoPrincFpDB.getEsercizio_repertorio(), incaricoPrincFpDB.getPg_repertorio(), Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO, incaricoPrincFpDB.getPg_record(), pagam.getAnno_pag(), pagam.getSemestre_pag())); 
						
						if (pagamPrincDB!=null){
							if (pagam.getImporto_pag()!=null) pagamPrincDB.setImporto_pag(pagam.getImporto_pag());
							pagamPrincDB.setToBeUpdated();
							pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
							updateBulk(userContext,pagamPrincDB);
						} else {
							pagamPrincDB = (Incarichi_comunicati_fp_detBulk)pagam.clone();
							pagamPrincDB.setIncarichi_comunicati_fp(incaricoPrincFpDB);
							pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
							pagamPrincDB.setToBeCreated();
							insertBulk(userContext,pagamPrincDB);
						}
					}
				}
			} else {
				incaricoPrincFpDB = (Incarichi_comunicati_fpBulk)bulk.clone();
				incaricoPrincFpDB.setEsercizio_repertorio(bulk.getEsercizio_repertorio());
				incaricoPrincFpDB.setPg_repertorio(bulk.getPg_repertorio());
				incaricoPrincFpDB.setTipo_record(Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
				incaricoPrincFpDB.setPg_record(new Long(1));
				incaricoPrincFpDB.setToBeCreated();
				insertBulk(userContext,incaricoPrincFpDB);
				
				for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = bulk.getIncarichi_comunicati_fp_detColl().iterator(); iterator.hasNext();) {
					Incarichi_comunicati_fp_detBulk pagam = iterator.next();
					if (pagam.getAnno_pag()!=null && pagam.getSemestre_pag()!=null && pagam.getImporto_pag()!=null){
						Incarichi_comunicati_fp_detBulk pagamPrincDB = (Incarichi_comunicati_fp_detBulk)pagam.clone();
						pagamPrincDB.setIncarichi_comunicati_fp(incaricoPrincFpDB);
						pagamPrincDB.setUser(CNRUserContext.getUser(userContext));
						pagamPrincDB.setToBeCreated();
						insertBulk(userContext, pagamPrincDB);
					}
				}
			}
		} catch (EJBException ex) {
			throw handleException(ex);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(UserContext userContext, V_incarichi_elenco_fpBulk incaricoElenco) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fpBulk bulk = new Incarichi_comunicati_fpBulk();
			bulk.setIncarichi_repertorio(incaricoElenco.getIncaricoRepertorio());
			Incarichi_comunicati_fpBulk incaricoComunicatoAgg = incFpHome.findIncaricoComunicatoAggFP(bulk);
			if (incaricoComunicatoAgg!=null)
				incaricoComunicatoAgg.setIncarichi_comunicati_fp_detColl(new BulkList(incFpHome.findIncarichiComunicatiFpDet(incaricoComunicatoAgg)));
			return incaricoComunicatoAgg;
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public Incarichi_comunicati_fpBulk getIncarichiComunicatiAggFP(UserContext userContext, Incarichi_comunicati_fpBulk incaricoComunicato) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			Incarichi_comunicati_fpBulk incaricoComunicatoAgg = incFpHome.findIncaricoComunicatoAggFP(incaricoComunicato);
			if (incaricoComunicatoAgg!=null)
				incaricoComunicatoAgg.setIncarichi_comunicati_fp_detColl(new BulkList(incFpHome.findIncarichiComunicatiFpDet(incaricoComunicatoAgg)));
			return incaricoComunicatoAgg;
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	public List<Incarichi_comunicati_fpBulk> getIncarichiComunicatiEliminatiFP(UserContext userContext, Integer esercizio, Integer semestre) throws ComponentException{
		try {
			Incarichi_comunicati_fpHome incFpHome = (Incarichi_comunicati_fpHome)getTempHome(userContext, Incarichi_comunicati_fpBulk.class);
			return incFpHome.findIncarichiComunicatiEliminatiFP(esercizio, semestre);
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (IntrospectionException ex) {
			throw handleException(ex);
		}
	}
	@Override
	protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		oggettobulk = super.eseguiCreaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk)
			archiviaAllegati(usercontext, (Incarichi_archivio_xml_fpBulk)oggettobulk);
		return oggettobulk;
	}
	
	private void archiviaAllegati(UserContext userContext, Incarichi_archivio_xml_fpBulk incaricoArchivioXmlFp) throws ComponentException{
		Incarichi_archivio_xml_fpHome archiveHome = (Incarichi_archivio_xml_fpHome)getHome(userContext,Incarichi_archivio_xml_fpBulk.class);
		
		if (!(incaricoArchivioXmlFp.getFile_inv() == null || incaricoArchivioXmlFp.getFile_inv().getName().equals("") ||
			  incaricoArchivioXmlFp.getFile_ric() == null || incaricoArchivioXmlFp.getFile_ric().getName().equals(""))) {
			incaricoArchivioXmlFp.setToBeUpdated();
			try {
				oracle.sql.BLOB blobInv = (oracle.sql.BLOB)archiveHome.getSQLBlob(incaricoArchivioXmlFp,"BDATA_INV");
				java.io.InputStream inInv = new java.io.BufferedInputStream(new FileInputStream(incaricoArchivioXmlFp.getFile_inv()));
				byte[] byteArrInv = new byte[1024];
				java.io.OutputStream osInv = new java.io.BufferedOutputStream(blobInv.getBinaryOutputStream());
				int lenInv;			
				while ((lenInv = inInv.read(byteArrInv))>0){
					osInv.write(byteArrInv,0,lenInv);
				}
				osInv.close();
				inInv.close();

				oracle.sql.BLOB blobRic = (oracle.sql.BLOB)archiveHome.getSQLBlob(incaricoArchivioXmlFp,"BDATA_RIC");
				java.io.InputStream inRic = new java.io.BufferedInputStream(new FileInputStream(incaricoArchivioXmlFp.getFile_ric()));
				byte[] byteArrRic = new byte[1024];
				java.io.OutputStream osRic = new java.io.BufferedOutputStream(blobRic.getBinaryOutputStream());
				int lenRic;			
				while ((lenRic = inRic.read(byteArrRic))>0){
					osRic.write(byteArrRic,0,lenRic);
				}
				osRic.close();
				inRic.close();
			} catch (PersistencyException e) {
				throw new ComponentException(e);	
			} catch (FileNotFoundException e) {
				throw new ComponentException(e);	
			} catch (IOException e) {
				throw new ComponentException(e);	
			} catch (SQLException e) {
				throw new ComponentException(e);	
			}
			
			try{
				JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
				
				Comunicazione comunicazione = (Comunicazione)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_inv());
				EsitoComunicazione esitoComunicazione = (EsitoComunicazione)jc.createUnmarshaller().unmarshal(incaricoArchivioXmlFp.getFile_ric());
	
				List<Incarichi_comunicati_fpBulk> listIncarichiComunicati = new ArrayList<Incarichi_comunicati_fpBulk>(); 
				for (Iterator iterator = comunicazione.getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
					Comunicazione.Consulenti.NuovoIncarico nuovoIncarico = (Comunicazione.Consulenti.NuovoIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, nuovoIncarico);
					listIncarichiComunicati.add(incaricoComunicato);
				}
				for (Iterator iterator = comunicazione.getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
					Comunicazione.Consulenti.ModificaIncarico modificaIncarico = (Comunicazione.Consulenti.ModificaIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, modificaIncarico);
					listIncarichiComunicati.add(incaricoComunicato);
				}
				for (Iterator iterator = comunicazione.getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
					Comunicazione.Consulenti.CancellaIncarico cancellaIncarico = (Comunicazione.Consulenti.CancellaIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoComunicato = Incarichi_comunicati_fpBulk.copyFrom(userContext, cancellaIncarico);
					listIncarichiComunicati.add(incaricoComunicato);
				}
				for (Iterator iterator = esitoComunicazione.getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
					EsitoComunicazione.Consulenti.NuovoIncarico nuovoIncarico = (EsitoComunicazione.Consulenti.NuovoIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, nuovoIncarico);
					listIncarichiComunicati.add(incaricoRicevuto);
				}
				for (Iterator iterator = esitoComunicazione.getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
					EsitoComunicazione.Consulenti.ModificaIncarico modificaIncarico = (EsitoComunicazione.Consulenti.ModificaIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, modificaIncarico);
					listIncarichiComunicati.add(incaricoRicevuto);
				}
				for (Iterator iterator = esitoComunicazione.getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
					EsitoComunicazione.Consulenti.CancellaIncarico cancellaIncarico = (EsitoComunicazione.Consulenti.CancellaIncarico) iterator.next();
					Incarichi_comunicati_fpBulk incaricoRicevuto = Incarichi_comunicati_fpBulk.copyFrom(userContext, cancellaIncarico);
					listIncarichiComunicati.add(incaricoRicevuto);
				}
				aggiornaIncarichiComunicatiFP(userContext, listIncarichiComunicati);
			} catch (Exception e){
				throw new ComponentException(e);
			}
		}
	}
	
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_archivio_xml_fpBulk) {
			try{
				JAXBContext jc = JAXBContext.newInstance("it.cnr.contab.incarichi00.xmlfp");
	
				Incarichi_archivio_xml_fpBulk allegato = (Incarichi_archivio_xml_fpBulk)oggettobulk;
	
				if (allegato.getNome_file_inv()!=null) {
					File fileInv = getFile(usercontext, allegato, "BDATA_INV", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_inv());
					allegato.setFile_inv(fileInv);
					allegato.setComunicazione((Comunicazione)jc.createUnmarshaller().unmarshal(fileInv));
				}
				if (allegato.getNome_file_ric()!=null) {
					File fileRic = getFile(usercontext, allegato, "BDATA_RIC", System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", allegato.getNome_file_ric());
					allegato.setFile_ric(fileRic);
					allegato.setEsitoComunicazione((EsitoComunicazione)jc.createUnmarshaller().unmarshal(fileRic));
				}
			} catch(JAXBException e){
				throw new ComponentException(e);
			}
		}
		return oggettobulk;
	}
	
	public File getFile(UserContext usercontext, Incarichi_archivio_xml_fpBulk allegato, String tipoBlob, String pathFile, String nomeFile) throws ComponentException {
		try{
			Incarichi_archivio_xml_fpHome home = (Incarichi_archivio_xml_fpHome)getHome(usercontext,Incarichi_archivio_xml_fpBulk.class);
	
			BLOB blob = (BLOB)home.getSQLBlob(allegato, tipoBlob);
	
	        long position;
	        int bytesRead = 0, totbytesRead = 0, totbytesWritten = 0;

            File outputBinaryFile   = new File(pathFile, nomeFile);
            FileOutputStream outputFileOutputStream  = new FileOutputStream(outputBinaryFile);

            long   blobLength = blob.length();
            int    chunkSize = blob.getChunkSize();
            byte[] binaryBuffer = new byte[chunkSize];

            for (position = 1; position <= blobLength; position += chunkSize) {
                // Loop through while reading a chunk of data from the BLOB
                // column using the getBytes() method. This data will be stored
                // in a temporary buffer that will be written to disk.
                bytesRead = blob.getBytes(position, chunkSize, binaryBuffer);

                // Now write the buffer to disk.
                outputFileOutputStream.write(binaryBuffer, 0, bytesRead);
               
                totbytesRead += bytesRead;
                totbytesWritten += bytesRead;
            }
            
            outputFileOutputStream.close();
			return outputBinaryFile;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (SQLException e) {
			throw new ComponentException(e);
		} catch (FileNotFoundException e) {
			throw new ComponentException(e);
		} catch (IOException e) {
			throw new ComponentException(e);
		}
	}
	/**
	 * Ritorna il pagato di un incarico nell'anno e semestre indicato
	 * @param userContext
	 * @param incarico_anno
	 * @param annoPag
	 * @param semestrePag
	 * @return
	 * @throws ComponentException
	 */
	public List<Incarichi_comunicati_fp_detBulk> getPagatoPerSemestre(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException
	{
		List<Incarichi_comunicati_fp_detBulk> listPagato = new ArrayList<Incarichi_comunicati_fp_detBulk>();
		CompensoHome cHome = (CompensoHome)getHome(userContext, CompensoBulk.class);
		Mandato_rigaHome mrHome = (Mandato_rigaHome)getHome(userContext, Mandato_rigaIBulk.class);
		Iterator listacomp_incarico;
		try {
			listacomp_incarico = cHome.findCompensoIncaricoList(userContext,incarico).iterator();

			Calendar cal = Calendar.getInstance();
			for (Iterator x =listacomp_incarico;x.hasNext();){
				CompensoBulk dett =(CompensoBulk)x.next();
				
				SQLBuilder sqlMr = mrHome.createSQLBuilder();
				sqlMr.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, dett.getPg_obbligazione());
				sqlMr.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, dett.getPg_obbligazione_scadenzario());
				sqlMr.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, dett.getEsercizio_ori_obbligazione());
				sqlMr.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, dett.getCd_cds());
				sqlMr.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, dett.getCd_unita_organizzativa());
				sqlMr.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, dett.getEsercizio());
				sqlMr.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, dett.getPg_compenso());

				List listMr =  mrHome.fetchAll(sqlMr);
				getHomeCache(userContext).fetchAll(userContext);

				Timestamp dataPagamento = null;
				for (Iterator iterator = listMr.iterator(); iterator.hasNext();) {
					Mandato_rigaBulk mrBulk = (Mandato_rigaBulk) iterator.next();
					if (!mrBulk.getMandato().getStato().equals(MandatoBulk.STATO_MANDATO_ANNULLATO) &&
						mrBulk.getMandato().getDt_trasmissione()!=null) {
						dataPagamento=mrBulk.getMandato().getDt_trasmissione();
						break;
					}
				}
				if (dataPagamento!=null) {
					cal.setTime(dataPagamento);
				
					boolean trovato=false;
					int annoCompenso = cal.get(Calendar.YEAR);
					int meseCompenso = cal.get(Calendar.MONTH);
					int semestreCompenso;
					if (meseCompenso==Calendar.JANUARY || meseCompenso==Calendar.FEBRUARY || meseCompenso==Calendar.MARCH||
						meseCompenso==Calendar.APRIL || meseCompenso==Calendar.MAY || meseCompenso==Calendar.JUNE)
						semestreCompenso=1;
					else
						semestreCompenso=2;
					
					for (Iterator<Incarichi_comunicati_fp_detBulk> iterator = listPagato.iterator(); iterator.hasNext();) {
						Incarichi_comunicati_fp_detBulk pagato = iterator.next();
						if (annoCompenso==pagato.getAnno_pag().intValue() && semestreCompenso==pagato.getSemestre_pag().intValue()) {
							pagato.setImporto_pag(pagato.getImporto_pag().add(dett.getIm_lordo_percipiente()));
							trovato=true;
							break;
						}
					}
					if (!trovato){
						Incarichi_comunicati_fp_detBulk pagato = new Incarichi_comunicati_fp_detBulk();
						pagato.setAnno_pag(annoCompenso);
						pagato.setSemestre_pag(semestreCompenso);
						pagato.setImporto_pag(dett.getIm_lordo_percipiente());
						listPagato.add(pagato);
					}
				}
			}
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		}
		return listPagato;
	}
}
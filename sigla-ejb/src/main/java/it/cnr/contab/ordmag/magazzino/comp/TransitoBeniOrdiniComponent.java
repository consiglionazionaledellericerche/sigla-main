/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.comp;

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.ordmag.magazzino.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.*;
import it.cnr.contab.ordmag.ordini.dto.ImportoOrdine;
import it.cnr.contab.ordmag.ordini.dto.ParametriCalcoloImportoOrdine;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TransitoBeniOrdiniComponent extends CRUDComponent implements ICRUDMgr, Cloneable, Serializable {
	@Override
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
		try{
			bene.setCondizioni(getHome(usercontext, Condizione_beneBulk.class).findAll());
		} catch ( PersistencyException pe){
			throw new it.cnr.jada.comp.ComponentException(pe);
		}
		try{
			bene.setInventario(caricaInventario(usercontext));
		}
		catch(it.cnr.jada.persistency.PersistencyException pe){
		}
		catch (it.cnr.jada.persistency.IntrospectionException ie){
		}
		return bene;
	}
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		try{
			Tipo_ammortamentoHome tipo_ammortamentoHome = (Tipo_ammortamentoHome)getHome(usercontext, Tipo_ammortamentoBulk.class);
			bene.setTi_ammortamenti(((Tipo_ammortamentoHome)getHome(usercontext, Tipo_ammortamentoBulk.class)).findTipiAmmortamentoFor(usercontext, bene.getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo()));
			if (bene.getTi_ammortamento() != null && bene.getTi_ammortamenti() != null){
				for (Object obj : bene.getTi_ammortamenti()){
					Tipo_ammortamentoBulk tipo = (Tipo_ammortamentoBulk)obj;
					if (tipo.getTi_ammortamento().equals(bene.getTi_ammortamento())){
						bene.setTipo_ammortamento(tipo);
						break;
					}
				}
			}
			try {
				if (Utility.createConfigurazioneCnrComponentSession().isGestioneEtichettaInventarioBeneAttivo(usercontext))
				{
					if(bene.getCd_condizione_bene() == null){
						if(bene.getCondizioneBene()==null) {
							bene.setCondizioneBene(new Condizione_beneBulk());
						}
						bene.setCd_condizione_bene("4");
					}


				}
			} catch (RemoteException | ComponentException e) {

			}

		} catch (PersistencyException | IntrospectionException pe){
			throw new it.cnr.jada.comp.ComponentException(pe);
		}
		return bene;
	}
	public Id_inventarioBulk caricaInventario(UserContext aUC)
			throws ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.persistency.IntrospectionException
	{

		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
		Id_inventarioBulk inventario = inventarioHome.findInventarioFor(aUC,false);

		// NON ESISTE UN INVENTARIO ASSOCIATO ALLA UO DI SCRIVANIA
		if (inventario == null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO");


		return inventario;
	}

	@Override
	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		Transito_beni_ordiniBulk bulk = (Transito_beni_ordiniBulk)oggettobulk;
		if (bulk.getTipo_ammortamento() != null){
			bulk.setTi_ammortamento(bulk.getTipo_ammortamento().getTi_ammortamento());
		}
		bulk = (Transito_beni_ordiniBulk)super.modificaConBulk(usercontext, oggettobulk);
		return bulk;
	}

	public Transito_beni_ordiniBulk  gestioneTransitoInventario(UserContext userContext, MovimentiMagBulk movimentoCarico)  throws ComponentException, PersistencyException, RemoteException, ApplicationException  {
		try {
			Bene_servizioHome bene_servizioHome = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
			Bene_servizioBulk bene_servizioBulk = (Bene_servizioBulk)bene_servizioHome.findByPrimaryKey(movimentoCarico.getLottoMag().getBeneServizio());
			if (bene_servizioBulk.getFl_gestione_inventario()){
				for (int i = 0; i < movimentoCarico.getQuantita().intValue(); i++){
					Transito_beni_ordiniBulk bene = new Transito_beni_ordiniBulk();
					Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(userContext, Id_inventarioBulk.class);
					Id_inventarioBulk inventario = inventarioHome.findInventarioFor(userContext,false);

					// NON ESISTE UN INVENTARIO ASSOCIATO ALLA UO DI SCRIVANIA
					if (inventario == null)
						throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO");
					bene.setInventario(inventario);
					bene.setMovimentiMag(movimentoCarico);
					bene.setStato(Transito_beni_ordiniBulk.STATO_INSERITO);
					LottoMagHome LottoMagHome = (LottoMagHome)getHome(userContext, LottoMagBulk.class);
					LottoMagBulk LottoMagBulk = (LottoMagBulk) LottoMagHome.findByPrimaryKey(movimentoCarico.getLottoMag());
					bene.setValore_unitario(LottoMagBulk.getCostoUnitario());
					bene.setDs_bene(bene_servizioBulk.getDs_bene_servizio());
					bene.setDt_acquisizione(movimentoCarico.getDtRiferimento());
					bene.setValore_iniziale(bene.getValore_unitario());
					OrdineAcqHome ordineAcqHome = (OrdineAcqHome)getHome(userContext, OrdineAcqBulk.class);
					OrdineAcqBulk ordineAcqBulk = (OrdineAcqBulk) ordineAcqHome.findByPrimaryKey(movimentoCarico.getLottoMag().getOrdineAcqConsegna().getOrdineAcqRiga().getOrdineAcq());
					bene.setTi_commerciale_istituzionale(ordineAcqBulk.getTiAttivita());

					Categoria_gruppo_inventHome catHome = (Categoria_gruppo_inventHome)getHome(userContext, Categoria_gruppo_inventBulk.class);
					Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk) catHome.findByPrimaryKey(bene.getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo());

					if (cat != null && cat.getFl_ammortamento()){
						Tipo_ammortamentoHome tipo_ammortamentoHome = (Tipo_ammortamentoHome)getHome(userContext, Tipo_ammortamentoBulk.class);
						Collection tiAmmortamenti = ((Tipo_ammortamentoHome)getHome(userContext, Tipo_ammortamentoBulk.class)).findTipiAmmortamentoFor(userContext, cat);
						if (tiAmmortamenti != null && tiAmmortamenti.size() == 1){
							Tipo_ammortamentoBulk tipo = (Tipo_ammortamentoBulk)tiAmmortamenti.iterator().next();
							bene.setTi_ammortamento(tipo.getTi_ammortamento());
							bene.setFl_ammortamento(true);
						}
					}

					bene.setToBeCreated();
					creaConBulk(userContext, bene);

				}
				return null;
			}
		} catch (ComponentException | PersistencyException | IntrospectionException e) {
			throw new ComponentException(e);
		}
		return null;
	}

}
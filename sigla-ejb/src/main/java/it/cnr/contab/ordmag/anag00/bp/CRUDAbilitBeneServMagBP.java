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

package it.cnr.contab.ordmag.anag00.bp;

import it.cnr.contab.ordmag.anag00.AbilitBeneServMagBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CRUDAbilitBeneServMagBP extends SimpleCRUDBP{

	private final SimpleDetailCRUDController abilitBeneServMagController = new SimpleDetailCRUDController("AbilitBeneServMagController",AbilitBeneServMagBulk.class,"categoriaGruppoColl",this);	

	/**
	 * Primo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 */
	public CRUDAbilitBeneServMagBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDConfigParametriCnrBP</code>.
	 * @param String function
	 */
	public CRUDAbilitBeneServMagBP(String function) {
		super(function);
	}

	public SimpleDetailCRUDController getAbilitBeneServMagController() {
		return abilitBeneServMagController;
	}

	public boolean isEditingAbilitazione() 
	{
		AbilitBeneServMagBulk abilit = (AbilitBeneServMagBulk) getAbilitBeneServMagController().getModel();
		if(abilit != null && abilit.isToBeCreated())
			return true;
			
		return false;
	}

	@Override
	public OggettoBulk initializeModelForFreeSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		MagazzinoBulk mag = (MagazzinoBulk)oggettobulk;
		mag.setInQuery(true);
		return super.initializeModelForFreeSearch(actioncontext, mag);
	}

	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		MagazzinoBulk mag = (MagazzinoBulk)oggettobulk;
		mag.setInQuery(true);
		return super.initializeModelForSearch(actioncontext, mag);
	}	
}


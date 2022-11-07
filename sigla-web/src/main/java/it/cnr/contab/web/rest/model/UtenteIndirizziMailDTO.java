/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General private License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General private License for more details.
 *
 *     You should have received a copy of the GNU Affero General private License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.web.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UtenteIndirizziMailDTO {
    private String cd_utente;
    private String indirizzo_mail;
    private Boolean fl_err_appr_var_bil_cnr_res;
    private Boolean fl_com_app_var_stanz_res;
    private Boolean fl_err_appr_var_bil_cnr_comp;
    private Boolean fl_com_app_var_stanz_comp;
    private Boolean flEsitoPosFattElettr;
    private Boolean flEsitoNegFattElettr;
    private Boolean flFepNotificaRicezione;
    private int crudStatus;
    private Long pgVerRec;
    public UtenteIndirizziMailDTO() {
        super();
    }

    public UtenteIndirizziMailDTO(Utente_indirizzi_mailBulk utente_indirizzi_mailBulk) {
        this.cd_utente = utente_indirizzi_mailBulk.getCd_utente();
        this.indirizzo_mail = utente_indirizzi_mailBulk.getIndirizzo_mail();
        this.fl_err_appr_var_bil_cnr_res = utente_indirizzi_mailBulk.getFl_err_appr_var_bil_cnr_res();
        this.fl_com_app_var_stanz_res = utente_indirizzi_mailBulk.getFl_com_app_var_stanz_res();
        this.fl_err_appr_var_bil_cnr_comp = utente_indirizzi_mailBulk.getFl_err_appr_var_bil_cnr_comp();
        this.fl_com_app_var_stanz_comp = utente_indirizzi_mailBulk.getFl_com_app_var_stanz_comp();
        this.flEsitoPosFattElettr = utente_indirizzi_mailBulk.getFlEsitoPosFattElettr();
        this.flEsitoNegFattElettr = utente_indirizzi_mailBulk.getFlEsitoNegFattElettr();
        this.flFepNotificaRicezione = utente_indirizzi_mailBulk.getFlFepNotificaRicezione();
        this.crudStatus = utente_indirizzi_mailBulk.getCrudStatus();
        this.pgVerRec = utente_indirizzi_mailBulk.getPg_ver_rec();
    }

    public Utente_indirizzi_mailBulk create() {
        final Utente_indirizzi_mailBulk bulk = new Utente_indirizzi_mailBulk();
        bulk.setUtente(new UtenteBulk(this.getCd_utente()));
        bulk.setIndirizzo_mail(this.getIndirizzo_mail());
        return bulk;
    }

    public Utente_indirizzi_mailBulk toUtente_indirizzi_mailBulk(Utente_indirizzi_mailBulk bulk) {
        bulk.setFl_err_appr_var_bil_cnr_res(this.getFl_err_appr_var_bil_cnr_res());
        bulk.setFl_com_app_var_stanz_res(this.getFl_com_app_var_stanz_res());
        bulk.setFl_err_appr_var_bil_cnr_comp(this.getFl_err_appr_var_bil_cnr_comp());
        bulk.setFl_com_app_var_stanz_comp(this.getFl_com_app_var_stanz_comp());
        bulk.setFlEsitoPosFattElettr(this.getFlEsitoPosFattElettr());
        bulk.setFlEsitoNegFattElettr(this.getFlEsitoNegFattElettr());
        bulk.setFlFepNotificaRicezione(this.getFlFepNotificaRicezione());
        bulk.setPg_ver_rec(this.getPgVerRec());
        bulk.setCrudStatus(this.getCrudStatus());
        return bulk;
    }

    public String getCd_utente() {
        return cd_utente;
    }

    public void setCd_utente(String cd_utente) {
        this.cd_utente = cd_utente;
    }

    public String getIndirizzo_mail() {
        return indirizzo_mail;
    }

    public void setIndirizzo_mail(String indirizzo_mail) {
        this.indirizzo_mail = indirizzo_mail;
    }

    public Boolean getFl_err_appr_var_bil_cnr_res() {
        return fl_err_appr_var_bil_cnr_res;
    }

    public void setFl_err_appr_var_bil_cnr_res(Boolean fl_err_appr_var_bil_cnr_res) {
        this.fl_err_appr_var_bil_cnr_res = fl_err_appr_var_bil_cnr_res;
    }

    public Boolean getFl_com_app_var_stanz_res() {
        return fl_com_app_var_stanz_res;
    }

    public void setFl_com_app_var_stanz_res(Boolean fl_com_app_var_stanz_res) {
        this.fl_com_app_var_stanz_res = fl_com_app_var_stanz_res;
    }

    public Boolean getFl_err_appr_var_bil_cnr_comp() {
        return fl_err_appr_var_bil_cnr_comp;
    }

    public void setFl_err_appr_var_bil_cnr_comp(Boolean fl_err_appr_var_bil_cnr_comp) {
        this.fl_err_appr_var_bil_cnr_comp = fl_err_appr_var_bil_cnr_comp;
    }

    public Boolean getFl_com_app_var_stanz_comp() {
        return fl_com_app_var_stanz_comp;
    }

    public void setFl_com_app_var_stanz_comp(Boolean fl_com_app_var_stanz_comp) {
        this.fl_com_app_var_stanz_comp = fl_com_app_var_stanz_comp;
    }

    public Boolean getFlEsitoPosFattElettr() {
        return flEsitoPosFattElettr;
    }

    public void setFlEsitoPosFattElettr(Boolean flEsitoPosFattElettr) {
        this.flEsitoPosFattElettr = flEsitoPosFattElettr;
    }

    public Boolean getFlEsitoNegFattElettr() {
        return flEsitoNegFattElettr;
    }

    public void setFlEsitoNegFattElettr(Boolean flEsitoNegFattElettr) {
        this.flEsitoNegFattElettr = flEsitoNegFattElettr;
    }

    public Boolean getFlFepNotificaRicezione() {
        return flFepNotificaRicezione;
    }

    public void setFlFepNotificaRicezione(Boolean flFepNotificaRicezione) {
        this.flFepNotificaRicezione = flFepNotificaRicezione;
    }

    public int getCrudStatus() {
        return crudStatus;
    }

    public void setCrudStatus(int crudStatus) {
        this.crudStatus = crudStatus;
    }

    public Long getPgVerRec() {
        return pgVerRec;
    }

    public void setPgVerRec(Long pgVerRec) {
        this.pgVerRec = pgVerRec;
    }
}

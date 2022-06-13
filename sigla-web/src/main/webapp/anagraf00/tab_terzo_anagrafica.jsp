<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.UserContext,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.contab.anagraf00.core.bulk.*,
		it.cnr.contab.utenze00.bulk.CNRUserInfo,
		it.cnr.contab.config00.sto.bulk.*"
%>

<%
	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);
	UserContext uc = HttpActionContext.getUserContext(session);
	TerzoBulk terzo = (TerzoBulk)bp.getModel();
%>

<table class="card p-2">
    <% if(bp.getAnagrafico() == null || !bp.getAnagrafico().isStrutturaCNR() ) { %>
    <tr>
      <td><%bp.writeFormLabel(out,"ti_terzo");%></td>
      <td><%bp.writeFormInput(out,"ti_terzo");%></td>
    </tr>
    <%}%>
    <tr>
      <td><% bp.writeFormLabel(out,"default","denominazione_sede"); %></td>
      <td colspan="3"><% bp.writeFormInput(out,"default","denominazione_sede"); %></td>
    </tr>
    <% if(bp.getAnagrafico() != null && bp.getAnagrafico().isStrutturaCNR() ) { %>
    <tr>
        <td><% bp.writeFormLabel(out,"ds_unita_organizzativa");%></td>
        <td colspan="3"><% bp.writeFormInput(out,null,"cd_unita_org",!bp.isInserting(),null,null);%>
            <% bp.writeFormInput(out,"ds_unita_organizzativa");%>
            <% bp.writeFormInput(out,null,"find_unita_organizzativa",!bp.isInserting(),null,null);%></td>
    </tr>
    <% }%>
    <tr>
      <td><% bp.writeFormLabel(out,"default","cd_precedente"); %></td>
      <td><% bp.writeFormInput(out,"default","cd_precedente"); %></td>
    </tr>
    <% if(bp.getAnagrafico() == null || !bp.getAnagrafico().isStrutturaCNR() ) { %>
    <tr>
        <td>
            <% bp.writeFormLabel(out,"nome_rapp_legale");%>
        </td>
        <td>
            <% bp.writeFormInput(out,"cognome_rapp_legale");%>
            <% bp.writeFormInput(out,"nome_rapp_legale");%>
            <% bp.writeFormInput(out,"find_rapp_legale");%>
        </td>
    </tr>
    <% }%>
    <tr>
        <% bp.writeFormField(out,"note");%>
    </tr>
    <tr>
      <% if(terzo != null && terzo.getAnagrafico() != null && terzo.getAnagrafico().getCodiceAmministrazioneIpa() != null ){ %>
        <td><%bp.writeFormLabel(out,"default","codiceUnivocoUfficioIpa"); %></td>
        <td><% bp.writeFormInput(out,"default","codiceUnivocoUfficioIpa",bp.isEnableChangeCodIpa(uc,terzo),null,null);%></td>
      <%} else {%>
        <%if(terzo != null && terzo.getAnagrafico() != null
            && terzo.getAnagrafico().getDataAvvioFattElettr() != null){ %>
            <% bp.writeFormField(out,"codiceDestinatarioFatt");%>
        <%} %>
        <%if(bp.isGestoreIstat(uc, terzo)){ %>
            <% bp.writeFormField(out,"flSbloccoFatturaElettronica");%>
        <%} %>
      <%} %>
      <%if(bp.getAnagrafico() != null && bp.getAnagrafico().isStrutturaCNR() ){ %>
             <td><%bp.writeFormLabel(out,"default","codiceUnivocoPcc"); %></td>
            <td><% bp.writeFormInput(out,"default","codiceUnivocoPcc",bp.isEnableChangePCC(uc,terzo),null,null);%></td>
            <td><%bp.writeFormLabel(out,"default","denominazionePcc"); %></td>
            <td><% bp.writeFormInput(out,"default","denominazionePcc",bp.isEnableChangePCC(uc,terzo),null,null);%></td>
      <%} %>
    </tr>
    <tr>
        <%	if(bp.getAnagrafico() != null && bp.getAnagrafico().isStrutturaCNR() ){ %>
            <td><%bp.writeFormLabel(out,"default","dt_fine_validita"); %></td>
            <td><%bp.writeFormInput(out,"default","dt_fine_validita",false,null,null); %></td>
        <% }else { %>
            <td><%bp.writeFormLabel(out,"default","dt_fine_rapporto"); %></td>
            <td><%bp.writeFormInput(out,"default","dt_fine_rapporto",true,null,null); %></td>
        <%} %>
    </tr>
</table>

<fieldset class="mt-2">
    <legend class="GroupLabel text-primary h3 ml-2">Indirizzo sede</legend>
    <table class="card p-2">
        <tr>
            <td><% bp.writeFormLabel(out,"comune_sede");%></td>
            <td><% bp.writeFormInput(out,"comune_sede");%>
                <% bp.writeFormInput(out,"find_comune_sede");%></td>
            <% bp.writeFormField(out,"cap_comune_sede");%>
        </tr>
        <tr>
            <% bp.writeFormField(out,"frazione_sede");%>
            <% bp.writeFormField(out,"ds_provincia_sede");%>
        </tr>
        <tr>
            <% bp.writeFormField(out,"via_sede");%>
            <% bp.writeFormField(out,"numero_civico_sede");%>
        </tr>
        <tr>
            <% bp.writeFormField(out,"ds_nazione_sede");%>
            <% bp.writeFormField(out,"codice_iso_sede");%>
        </tr>
    </table>
</fieldset>
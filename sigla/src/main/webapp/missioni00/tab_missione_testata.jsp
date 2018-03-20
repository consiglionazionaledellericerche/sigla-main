<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.anagraf00.tabrif.bulk.*,it.cnr.contab.docamm00.bp.IDocumentoAmministrativoSpesaBP"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	MissioneBulk missione = (MissioneBulk) bp.getModel();

	if(missione == null)
		missione = new MissioneBulk();
%>

<div class="Group card p-2" style="width:100%">
    <table width="100%">
        <tr>
            <td><% bp.getController().writeFormLabel(out, "dt_registrazione"); %></td>
            <td><% bp.getController().writeFormInput(out, "dt_registrazione"); %></td>
            <%if(missione.getDt_cancellazione()!=null)
              {%>
                <td><% bp.getController().writeFormLabel(out, "dt_cancellazione"); %></td>
                <td><% bp.getController().writeFormInput(out, "dt_cancellazione"); %></td>
            <%}
              else
              {%>
                <td colspan="2"></td>
            <%}%>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "dt_inizio_missione"); %></td>
            <td><% bp.getController().writeFormInput( out, "dt_inizio_missione");%></td>
            <td class="text-right"><% bp.getController().writeFormLabel( out, "dt_fine_missione"); %></td>
            <td class="text-right"><% bp.getController().writeFormInput( out, "dt_fine_missione");%></td>
        </tr>

        <tr>
            <td><% bp.getController().writeFormLabel( out, "ds_missione"); %></td>
            <td colspan="3"><% bp.getController().writeFormInput( out, "ds_missione"); %></td>
        </tr>

        <tr>
            <td><% bp.getController().writeFormLabel( out, "tipo_missione"); %></td>
            <td><% bp.getController().writeFormInput( out, "tipo_missione"); %></td>
            <td colspan="2" class="text-right"><% bp.getController().writeFormInput( out, null, "ti_istituz_commerc", false, null, "onClick=\"submitForm('doOnIstituzionaleCommercialeChange')\"");%></td>
        </tr>
    </table>
</div>

<div class="Group card p-2 mt-2" style="width:100%">
    <table width="100%">
     <% if (bp.isSearching())
        {
             boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"ti_provvisorio_definitivoForSearch"); %></td>
            <td><% bp.getController().writeFormInput(out,"ti_provvisorio_definitivoForSearch"); %></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_cofiForSearch"); %></td>
            <td><% bp.getController().writeFormInput(out,null,"stato_cofiForSearch", isInSpesaMode, null, ""); %></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"ti_associato_manrevForSearch"); %></td>
            <td><% bp.getController().writeFormInput(out,"ti_associato_manrevForSearch"); %></td>
            <td>
                <% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
                <% bp.getController().writeFormInput(out,null,"stato_liquidazione",isInSpesaMode,null,"");%>
            </td>

            </tr>
      <% } else { %>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"ti_provvisorio_definitivo"); %></td>
            <td><% bp.getController().writeFormInput(out,"ti_provvisorio_definitivo"); %></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_cofi"); %></td>
            <td><% bp.getController().writeFormInput(out,"stato_cofi"); %></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"ti_associato_manrev"); %></td>
            <td><% bp.getController().writeFormInput(out,"ti_associato_manrev"); %></td>

            <td>
                <% bp.getController().writeFormLabel(out,"stato_liquidazione");%>
                <% bp.getController().writeFormInput(out,null,"stato_liquidazione",(missione.getCompenso()!=null && missione.getCompenso().isPagato()),null,"");%>
            </td>
            </tr>

      <% } %>
    </table>
</div>
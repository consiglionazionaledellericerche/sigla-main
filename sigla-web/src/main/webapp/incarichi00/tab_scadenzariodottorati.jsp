<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 %>

<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Testata
    </div>
    <table width="100%">
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_registrazione"); %></td>
        <td colspan=3><% bp.getController().writeFormInput(out,"dt_registrazione"); %></td>
      </tr>
        <tr>
            <% bp.getController().writeFormField(out,"dt_inizio_scad_dott");%>
            <% bp.getController().writeFormField(out,"dt_fine_scad_dott");%>
        </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"ds_scad_dott"); %></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"ds_scad_dott"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"stato"); %></td>
        <td><% bp.getController().writeFormInput(out,"stato"); %></td>
        <td><% bp.getController().writeFormLabel(out,"stato_ass_compenso"); %></td>
        <td><% bp.getController().writeFormInput(out,"stato_ass_compenso"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_sospensione"); %></td>
        <td><% bp.getController().writeFormInput(out,"dt_sospensione"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dt_ripristino"); %></td>
        <td><% bp.getController().writeFormInput(out,"dt_ripristino"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dt_rinnovo"); %></td>
        <td><% bp.getController().writeFormInput(out,"dt_rinnovo"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dt_cessazione"); %></td>
        <td><% bp.getController().writeFormInput(out,"dt_cessazione"); %></td>
      </tr>
    </table>
</div>
<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Mini carriera di origine
    </div>
    <table width="100%">
      <tr>
        <% bp.getController().writeFormField(out,"id"); %>
        <% bp.getController().writeFormField(out,"esercizio_scad_dott_ori"); %>
      </tr>
    </table>
</div>
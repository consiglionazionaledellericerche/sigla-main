script:{
	var json = jsonUtils.toObject(requestbody.content),
	    nodeRefSource = json.nodeRefSource,
	    nodoDoc = search.findNode(nodeRefSource),
	    username = json.username,
	    password = json.password,
	    otp = json.otp, mimetypeDoc, nameDoc, service, estensione, nameDocFirmato, docFirmato;
	mimetypeDoc = nodoDoc.properties.content.mimetype;
	nameDoc = nodoDoc.name;
	service = cnrutils.getBean('mimetypeService');
	estensione = "." + service.getExtension(nodoDoc.mimetype);
	if (nodoDoc.name.indexOf(estensione) === -1) {
	  nameDoc = nameDoc + estensione;
	}
	nameDocFirmato = nameDoc + ".p7m";
	//crea il doc firmato
	docFirmato = nodoDoc.parent.createFile(nameDocFirmato, "cnr:envelopedDocument");
	docFirmato.mimetype = "application/p7m";
	docFirmato.save();
	try{
		arubaSign.pkcs7SignV2(username, password, otp, nodoDoc.nodeRef, docFirmato.nodeRef);
	} catch(ex) {
		docFirmato.remove();
        status.code = 500;
        status.message = ex.javaException.message;
		status.redirect = true;
		break script;
	}  
	nodoDoc.createAssociation(docFirmato, "varpianogest:allegatiVarBilancio");
	nodoDoc.addAspect("cnr:signedDocument");
	nodoDoc.save();
	docFirmato.createAssociation(nodoDoc, "cnr:signedDocumentAss");  
	model.nodeRef = docFirmato.nodeRef.toString();
}
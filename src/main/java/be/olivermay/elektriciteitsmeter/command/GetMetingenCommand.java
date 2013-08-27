/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package be.olivermay.elektriciteitsmeter.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandRequest;

import be.olivermay.elektriciteitsmeter.command.dto.GetMetingenResult;


/**
 * @author Oliver May
 *
 */
public class GetMetingenCommand implements Command<CommandRequest, GetMetingenResult>{

	/* (non-Javadoc)
	 * @see org.geomajas.command.Command#getEmptyCommandResponse()
	 */
	@Override
	public GetMetingenResult getEmptyCommandResponse() {
		return new GetMetingenResult();
	}

	/* (non-Javadoc)
	 * @see org.geomajas.command.Command#execute(org.geomajas.command.CommandRequest, org.geomajas.command.CommandResponse)
	 */
	@Override
	public void execute(CommandRequest request, GetMetingenResult response) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

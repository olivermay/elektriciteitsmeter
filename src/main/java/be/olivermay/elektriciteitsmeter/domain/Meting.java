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
package be.olivermay.elektriciteitsmeter.domain;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;


/**
 * @author Oliver May
 *
 */
@Entity
public class Meting {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Meting [id=" + id + ", datum=" + datum + ", pulses=" + pulses + "]";
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id; // UUID

	private Date datum;
	
	private int pulses;
	
	
	/**
	 * @return the datum
	 */
	public Date getDatum() {
		return datum;
	}

	
	/**
	 * @param datum the datum to set
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	
	/**
	 * @return the pulses
	 */
	public int getPulses() {
		return pulses;
	}

	
	/**
	 * @param pulses the pulses to set
	 */
	public void setPulses(int pulses) {
		this.pulses = pulses;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
}

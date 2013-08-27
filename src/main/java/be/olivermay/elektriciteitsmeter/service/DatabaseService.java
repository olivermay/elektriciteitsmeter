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
package be.olivermay.elektriciteitsmeter.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import be.olivermay.elektriciteitsmeter.domain.Meting;

/**
 * @author Oliver May
 * 
 */
@Component
@Transactional
public class DatabaseService {

	@Autowired
	SessionFactory sessionFactory;

	public void saveOrUpdate(Meting meting) {
		sessionFactory.getCurrentSession().saveOrUpdate(meting);
	}

	public List<Meting> getMetingen(int limit) {
		return sessionFactory.getCurrentSession().createCriteria(Meting.class).setMaxResults(limit)
				.addOrder(Property.forName("datum").desc()).list();
	}

	public List<Meting> getMetingen() {
		return sessionFactory.getCurrentSession().createCriteria(Meting.class).addOrder(Property.forName("datum").
				desc()).list();
	}

}

package com.client.quickstart.hibernate4.data;

// Generated Jul 14, 2011 10:51:59 AM by Hibernate Tools 3.4.0.CR1

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.client.quickstart.hibernate4.model.Product;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class ProductListProducer {
	@Inject
	private EntityManager em;

	private List<Product> products;

	// @Named provides access the return value via the EL variable name
	// "products" in the UI (e.g.,
	// Facelets or JSP view)
	@Produces
	@Named
	public List<Product> getProducts() {
		return products;
	}

	public void onProductListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Product product) {
		retrieveAllProductsOrderedByName();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void retrieveAllProductsOrderedByName() {

		// using Hibernate Session and Criteria Query via Hibernate Native API
		Session session = (Session) em.getDelegate();
		Criteria cb = session.createCriteria(Product.class);
		cb.addOrder(Order.asc("companyName"));
		products = (List<Product>) cb.list();

	}
}

package cl.votainteligente.inspector.server.services;

import cl.votainteligente.inspector.client.services.CategoryService;
import cl.votainteligente.inspector.model.*;

import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

public class CategoryServiceImpl implements CategoryService {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Category> getAllCategories() throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();
			Criteria criteria = hibernate.createCriteria(Category.class);
			criteria.addOrder(Order.asc("name"));
			List<Category> categories = criteria.list();
			hibernate.getTransaction().commit();
			return categories;
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}

	@Override
	public Category getCategory(Long categoryId) throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();
			Category category = (Category) hibernate.get(Category.class, categoryId);
			hibernate.getTransaction().commit();
			return category;
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}

	@Override
	public Category saveCategory(Category category) throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();
			hibernate.saveOrUpdate(category);
			hibernate.getTransaction().commit();
			return category;
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}

	@Override
	public void deleteCategory(Category category) throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();
			hibernate.delete(category);
			hibernate.getTransaction().commit();
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}

	@Override
	public List<Category> searchCategory(String keyWord) throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();
			String query = "select distinct c from Category c where ";
			Query hQuery;
			String filters = "";

			if (keyWord != null && !keyWord.equals("")) {
				filters += "(";
				String[] keyWords = keyWord.split("[ ]");

				for (int i = 0; i < keyWords.length; i++) {
					keyWords[i]  = keyWords[i].replaceAll("[ÁÀáà]","a");
					keyWords[i]  = keyWords[i].replaceAll("[ÉÈéè]","e");
					keyWords[i]  = keyWords[i].replaceAll("[ÍÌíì]","i");
					keyWords[i]  = keyWords[i].replaceAll("[ÓÒóò]","o");
					keyWords[i]  = keyWords[i].replaceAll("[ÚÙúù]","u");
					keyWords[i]  = keyWords[i].replaceAll("[Ññ]", "n");
					keyWords[i]  = keyWords[i].replaceAll("\\W", "");
					filters += " lower(TRANSLATE(c.name,'ÁáÉéÍíÓóÚúÑñ','AaEeIiOoUuNn')) like lower('%" + keyWords[i] + "%')";
					if (i + 1 < keyWords.length) {
						filters += " OR";
					}
				}
				filters += ")";
			}
			query += filters;

			hQuery = hibernate.createQuery(query);
			List<Category> categories = hQuery.list();
			hibernate.getTransaction().commit();
			return categories;
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}

	@Override
	public List<Category> searchCategory(Parlamentarian parlamentarian) throws Exception {
		Session hibernate = sessionFactory.getCurrentSession();

		try {
			hibernate.beginTransaction();

			List<Category> resultList = new ArrayList<Category>();
			parlamentarian = (Parlamentarian) hibernate.get(Parlamentarian.class, parlamentarian.getId());

			Set<Bill> bills = new HashSet<Bill>();
			Set<Category> societyCategories = new HashSet<Category>();
			Set<Category> resultSet = new HashSet<Category>();

			for (Bill bill : parlamentarian.getAuthoredBills()) {
				bills.add(bill);
			}

			for (Bill bill : parlamentarian.getVotedBills()) {
				bills.add(bill);
			}

			for (Society society : parlamentarian.getSocieties().keySet()) {
				for (Category category : society.getCategories()) {
					societyCategories.add(category);
				}
			}

			for (Bill bill : bills) {
				for (Category category : bill.getCategories()) {
					if (societyCategories.contains(category)) {
						resultSet.add(category);
					}
				}
			}

			resultList = new ArrayList<Category>(resultSet);
			Collections.sort(resultList, new Comparator<Category>() {

				@Override
				public int compare(Category o1, Category o2) {
					return o1.compareTo(o2);
				}
			});

			hibernate.getTransaction().commit();
			return resultList;
		} catch (Exception ex) {
			if (hibernate.isOpen() && hibernate.getTransaction().isActive()) {
				hibernate.getTransaction().rollback();
			}

			throw ex;
		}
	}
}

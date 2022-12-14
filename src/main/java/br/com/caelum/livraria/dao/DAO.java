package br.com.caelum.livraria.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

public class DAO<T> {

	private final Class<T> classe;

	public DAO(Class<T> classe) {
		this.classe = classe;
	}

	public void adiciona(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
		em.close();
	}

	public void remove(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(t));
		em.getTransaction().commit();
		em.close();
	}

	public void atualiza(T t) {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
		em.close();
	}

	public List<T> listaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));
		List<T> lista = em.createQuery(query).getResultList();
		em.close();
		return lista;
	}

	public T buscaPorId(Integer id) {
		EntityManager em = new JPAUtil().getEntityManager();
		T instancia = em.find(classe, id);
		em.close();
		return instancia;
	}

	public int contaTodos() {
		EntityManager em = new JPAUtil().getEntityManager();
		long result = (Long) em.createQuery("select count(n) from Livro n").getSingleResult();
		em.close();
		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults, Map<String, Object> filtros, String campoOrdenacao, SortOrder sentidoOrdenacao) {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(classe);
		Root<T> root = query.from(classe);

		if (filtros != null && filtros.size() > 0) {

			List<Predicate> predicates = montarFiltros(root, cb, filtros);

			if (!predicates.isEmpty()) {
				query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			}
		}

		if (campoOrdenacao != null) {
			query.orderBy(sentidoOrdenacao == SortOrder.ASCENDING ? cb.asc(root.get(campoOrdenacao)) : cb.desc(root.get(campoOrdenacao)));
		}

		List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
		em.close();
		return lista;
	}

	public int quantidadeDeElementos() {
		EntityManager em = new JPAUtil().getEntityManager();
		long result = (Long) em.createQuery("select count(n) from " + classe.getSimpleName() + " n")
				.getSingleResult();
		em.close();
		return (int) result;
	}

	private List<Predicate> montarFiltros(Root<T> root, CriteriaBuilder cb, Map<String, Object> filtros) {
		List<Predicate> predicates = new ArrayList<>();
		for (Map.Entry<String, Object> entry : filtros.entrySet()) {
			String campo = entry.getKey();
			Object valor = entry.getValue();
			if (valor == null) {
				continue;
			}

			Expression<String> expr = root.get(campo).as(String.class);
			Predicate p = cb.like(cb.lower(expr),
					"%" + valor.toString().toLowerCase() + "%");
			predicates.add(p);
		}
		return predicates;
	}

	public int quantidadeDeElementosFiltrados(Map<String, Object> filtros) {
		EntityManager em = new JPAUtil().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(classe);
		CriteriaQuery<Long> select = query.select(cb.count(root));

		if (filtros != null && filtros.size() > 0) {
			List<Predicate> predicates = montarFiltros(root, cb, filtros);

			if (!predicates.isEmpty()) {
				query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			}
		}

		Long count = em.createQuery(select).getSingleResult();
		return count.intValue();
	}

}

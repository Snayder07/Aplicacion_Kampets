package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Especies;
import org.example.util.JPAUtil;
import java.util.List;

public class EspecieRepositoryImpl implements EspecieRepository {

    @Override
    public List<Especies> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Especies> lista = em.createQuery("SELECT e FROM Especies e", Especies.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public Especies buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Especies e = em.find(Especies.class, id);
        em.close();
        return e;
    }

    @Override
    public Especies buscarPorNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Especies e WHERE LOWER(e.nombre) = LOWER(:n)", Especies.class)
                    .setParameter("n", nombre.trim())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void guardar(Especies e) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        em.close();
    }
}
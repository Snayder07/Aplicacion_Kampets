package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Productos;
import org.example.util.JPAUtil;
import java.util.List;

public class ProductoRepositoryImpl implements ProductoRepository {

    @Override
    public void guardar(Productos producto) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(producto);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Productos buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Productos p = em.find(Productos.class, id);
        em.close();
        return p;
    }

    @Override
    public List<Productos> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Productos> lista = em.createQuery("SELECT p FROM Productos p", Productos.class).getResultList();
        em.close();
        return lista;
    }

    @Override
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Productos p = em.find(Productos.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void actualizar(Productos producto) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(producto);
        em.getTransaction().commit();
        em.close();
    }
}
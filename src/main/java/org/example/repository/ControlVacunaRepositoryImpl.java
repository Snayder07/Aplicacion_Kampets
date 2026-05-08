package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Control_vacunas;
import org.example.util.JPAUtil;
import java.util.List;

public class ControlVacunaRepositoryImpl implements ControlVacunaRepository {

    @Override
    public void guardar(Control_vacunas cv) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cv);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Control_vacunas buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Control_vacunas cv = em.find(Control_vacunas.class, id);
        em.close();
        return cv;
    }

    @Override
    public List<Control_vacunas> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Control_vacunas> lista = em.createQuery(
                "SELECT cv FROM Control_vacunas cv", Control_vacunas.class
        ).getResultList();
        em.close();
        return lista;
    }

    @Override
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Control_vacunas cv = em.find(Control_vacunas.class, id);
        if (cv != null) em.remove(cv);
        em.getTransaction().commit();
        em.close();
    }
}
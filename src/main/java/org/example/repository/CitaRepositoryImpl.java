package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Citas;
import org.example.util.JPAUtil;
import java.util.List;

public class CitaRepositoryImpl implements CitaRepository {

    @Override
    public void guardar(Citas cita) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cita);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Citas buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Citas cita = em.find(Citas.class, id);
        em.close();
        return cita;
    }

    @Override
    public List<Citas> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Citas> citas = em.createQuery("SELECT c FROM Citas c", Citas.class).getResultList();
        em.close();
        return citas;
    }

    @Override
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Citas cita = em.find(Citas.class, id);
        if (cita != null) em.remove(cita);
        em.getTransaction().commit();
        em.close();
    }
}
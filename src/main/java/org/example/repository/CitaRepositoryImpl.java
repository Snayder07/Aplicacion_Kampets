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
    public List<Citas> buscarPorCliente(Integer clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Citas> citas = em.createQuery(
                "SELECT c FROM Citas c WHERE c.mascota.cliente.id = :clienteId", Citas.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
        em.close();
        return citas;
    }

    @Override
    public List<Citas> buscarPasadasPorCliente(Integer clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Citas> citas = em.createQuery(
                "SELECT c FROM Citas c WHERE c.mascota.cliente.id = :clienteId AND c.fechaCita < CURRENT_DATE ORDER BY c.fechaCita DESC", Citas.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
        em.close();
        return citas;
    }

    @Override
    public List<Citas> buscarDeHoy() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Citas> citas = em.createQuery(
                "SELECT c FROM Citas c WHERE c.fechaCita = CURRENT_DATE ORDER BY c.horaCita ASC", Citas.class)
                .getResultList();
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
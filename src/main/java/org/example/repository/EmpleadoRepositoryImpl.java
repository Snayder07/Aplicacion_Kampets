package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.model.Empleados;
import org.example.util.JPAUtil;
import java.util.List;

public class EmpleadoRepositoryImpl implements EmpleadoRepository {

    @Override
    public void guardar(Empleados empleado) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(empleado);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Empleados buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Empleados empleado = em.find(Empleados.class, id);
        em.close();
        return empleado;
    }

    @Override
    public Empleados buscarPorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Empleados e WHERE e.correo = :correo", Empleados.class)
                    .setParameter("correo", correo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Empleados> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Empleados> empleados = em.createQuery(
                "SELECT e FROM Empleados e", Empleados.class).getResultList();
        em.close();
        return empleados;
    }

    @Override
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Empleados empleado = em.find(Empleados.class, id);
        if (empleado != null) em.remove(empleado);
        em.getTransaction().commit();
        em.close();
    }
}

package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.model.Cliente;
import org.example.util.JPAUtil;
import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepository {

    @Override
    public void guardar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        Cliente cliente = em.find(Cliente.class, id);
        em.close();
        return cliente;
    }

    @Override
    public List<Cliente> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        em.close();
        return clientes;
    }

    @Override
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente != null) em.remove(cliente);
        em.getTransaction().commit();
        em.close();
    }
}

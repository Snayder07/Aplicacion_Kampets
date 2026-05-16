package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

public class ClienteService {

    private final ClienteRepositoryImpl repositorio = new ClienteRepositoryImpl();

    public void registrar(String nombre, String apellido, String correo,
                          String telefono, String contrasena,
                          String confirmar) throws Exception {

        if (nombre == null || nombre.trim().isEmpty())
            throw new Exception("El nombre es obligatorio.");
        if (correo == null || correo.trim().isEmpty())
            throw new Exception("El correo es obligatorio.");
        if (contrasena == null || contrasena.trim().isEmpty())
            throw new Exception("La contraseña es obligatoria.");
        if (contrasena.length() < 8)
            throw new Exception("La contraseña debe tener al menos 8 caracteres.");
        if (!contrasena.equals(confirmar))
            throw new Exception("Las contraseñas no coinciden.");
        if (buscarPorCorreo(correo) != null)
            throw new Exception("Ya existe una cuenta con ese correo.");

        // ── Crear cliente asignando directamente los campos de BD ──
        Cliente cliente = new Cliente();

        // setNombreC, setCorreoC, setTelefonoC son los métodos
        // que mapean directamente a las columnas nombre_c, correo_c, telefono_c
        String nombreCompleto = (nombre.trim() + " " + apellido.trim()).trim();
        String correoLimpio   = correo.trim().toLowerCase();
        String telefonoLimpio = telefono != null ? telefono.trim() : "";

        // Llamamos a los setters de Cliente directamente
        // que sobreescriben los de Persona y mapean a las columnas reales
        cliente.setNombre(nombreCompleto);
        cliente.setCorreo(correoLimpio);
        cliente.setTelefono(telefonoLimpio);
        cliente.setDireccion("");
        cliente.setFechaRegistro(LocalDate.now());
        cliente.setContrasena(contrasena);

        // Debug — verificar que los valores son correctos antes de guardar
        System.out.println("=== REGISTRANDO CLIENTE ===");
        System.out.println("Nombre:   " + cliente.getNombre());
        System.out.println("Correo:   " + cliente.getCorreo());
        System.out.println("Telefono: " + cliente.getTelefono());

        repositorio.guardar(cliente);
    }

    public Cliente loginCliente(String correo, String contrasena) throws Exception {

        if (correo == null || correo.trim().isEmpty())
            throw new Exception("Ingresa tu correo.");
        if (contrasena == null || contrasena.trim().isEmpty())
            throw new Exception("Ingresa tu contraseña.");

        Cliente cliente = buscarPorCorreo(correo.trim().toLowerCase());

        if (cliente == null)
            throw new Exception("El correo no está registrado.");

        if (!cliente.getContrasena().equals(contrasena))
            throw new Exception("Contraseña incorrecta.");

        return cliente;
    }

    public Cliente buscarPorCorreo(String correo) {
        List<Cliente> todos = repositorio.buscarTodos();
        for (Cliente c : todos) {
            if (c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(correo)) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        return repositorio.buscarTodos();
    }
}
package org.example.service;

import org.example.model.Cliente;
import org.example.model.Empleados;
import org.example.repository.ClienteRepositoryImpl;
import org.example.repository.EmpleadoRepositoryImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Servicio que gestiona la logica de recuperacion de contrasena.
 * Soporta tanto Clientes como Empleados (administradores).
 * Genera un codigo de 6 digitos, lo almacena en memoria y lo envia
 * al correo del usuario mediante CorreoService.
 */
public class RecuperacionService {

    private final ClienteService   clienteService   = new ClienteService();
    private final EmpleadoService  empleadoService  = new EmpleadoService();

    // Mapa temporal: correo → codigo generado (valido durante la sesion)
    private final Map<String, String> codigosPendientes = new HashMap<>();

    // Indica si el correo pertenece a un empleado (true) o cliente (false)
    private final Map<String, Boolean> esEmpleado = new HashMap<>();

    /**
     * Paso 1: verifica que el correo existe (en clientes o admins) y envia el codigo.
     * @throws Exception si el correo no esta registrado o falla el envio.
     */
    public void enviarCodigoRecuperacion(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("Ingresa tu correo electronico.");
        }

        String correoNorm = correo.trim().toLowerCase();
        String nombre;

        // Buscar primero en clientes, luego en empleados
        Cliente cliente = clienteService.buscarPorCorreo(correoNorm);
        if (cliente != null) {
            nombre = cliente.getNombre();
            esEmpleado.put(correoNorm, false);
        } else {
            Empleados empleado = empleadoService.buscarPorCorreo(correo.trim());
            if (empleado == null) {
                // Intentar con lowercase tambien
                empleado = empleadoService.buscarPorCorreo(correoNorm);
            }
            if (empleado == null) {
                throw new Exception("No existe ninguna cuenta con ese correo.");
            }
            nombre = empleado.getNombre();
            esEmpleado.put(correoNorm, true);
        }

        // Generar codigo de 6 digitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosPendientes.put(correoNorm, codigo);

        // Enviar correo
        CorreoService.enviarCodigoRecuperacion(correo.trim(), nombre, codigo);
    }

    /**
     * Paso 2: verifica que el codigo ingresado coincide con el generado.
     */
    public boolean verificarCodigo(String correo, String codigoIngresado) {
        String codigoEsperado = codigosPendientes.get(correo.trim().toLowerCase());
        return codigoEsperado != null && codigoEsperado.equals(codigoIngresado.trim());
    }

    /**
     * Paso 3: cambia la contrasena del usuario (cliente o admin) y limpia el codigo.
     * @throws Exception si las contrasenas no coinciden o el usuario no existe.
     */
    public void cambiarContrasena(String correo, String nuevaContrasena, String confirmar) throws Exception {
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new Exception("La nueva contrasena no puede estar vacia.");
        }
        if (!nuevaContrasena.equals(confirmar)) {
            throw new Exception("Las contrasenas no coinciden.");
        }
        if (nuevaContrasena.length() < 6) {
            throw new Exception("La contrasena debe tener al menos 6 caracteres.");
        }

        String correoNorm = correo.trim().toLowerCase();
        Boolean esEmp = esEmpleado.get(correoNorm);

        if (Boolean.TRUE.equals(esEmp)) {
            // Cambiar contrasena de empleado/admin
            Empleados empleado = empleadoService.buscarPorCorreo(correo.trim());
            if (empleado == null) empleado = empleadoService.buscarPorCorreo(correoNorm);
            if (empleado == null) throw new Exception("Administrador no encontrado.");

            empleado.setContrasena(nuevaContrasena);
            EmpleadoRepositoryImpl repo = new EmpleadoRepositoryImpl();
            repo.actualizar(empleado);
        } else {
            // Cambiar contrasena de cliente
            Cliente cliente = clienteService.buscarPorCorreo(correoNorm);
            if (cliente == null) throw new Exception("Cliente no encontrado.");

            cliente.setContrasena(nuevaContrasena);
            ClienteRepositoryImpl repo = new ClienteRepositoryImpl();
            repo.actualizar(cliente);
        }

        // Limpiar codigo usado
        codigosPendientes.remove(correoNorm);
        esEmpleado.remove(correoNorm);
    }
}

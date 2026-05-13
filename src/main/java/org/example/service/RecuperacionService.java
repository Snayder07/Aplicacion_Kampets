package org.example.service;

import org.example.model.Cliente;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Servicio que gestiona la lógica de recuperación de contraseña.
 * Genera un código de 6 dígitos, lo almacena en memoria y lo envía
 * al correo del cliente mediante CorreoService.
 */
public class RecuperacionService {

    private final ClienteService clienteService = new ClienteService();

    // Mapa temporal: correo → código generado (en memoria, válido durante la sesión)
    private final Map<String, String> codigosPendientes = new HashMap<>();

    /**
     * Paso 1: verifica que el correo existe y envía el código.
     * @throws Exception si el correo no está registrado o falla el envío.
     */
    public void enviarCodigoRecuperacion(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("Ingresa tu correo electrónico.");
        }

        Cliente cliente = clienteService.buscarPorCorreo(correo.trim().toLowerCase());
        if (cliente == null) {
            throw new Exception("No existe ninguna cuenta con ese correo.");
        }

        // Generar código de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosPendientes.put(correo.trim().toLowerCase(), codigo);

        // Enviar correo
        CorreoService.enviarCodigoRecuperacion(
                correo.trim(),
                cliente.getNombre(),
                codigo
        );
    }

    /**
     * Paso 2: verifica que el código ingresado coincide con el generado.
     */
    public boolean verificarCodigo(String correo, String codigoIngresado) {
        String codigoEsperado = codigosPendientes.get(correo.trim().toLowerCase());
        return codigoEsperado != null && codigoEsperado.equals(codigoIngresado.trim());
    }

    /**
     * Paso 3: cambia la contraseña del cliente y limpia el código.
     * @throws Exception si las contraseñas no coinciden o el cliente no existe.
     */
    public void cambiarContrasena(String correo, String nuevaContrasena, String confirmar) throws Exception {
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new Exception("La nueva contraseña no puede estar vacía.");
        }
        if (!nuevaContrasena.equals(confirmar)) {
            throw new Exception("Las contraseñas no coinciden.");
        }
        if (nuevaContrasena.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres.");
        }

        Cliente cliente = clienteService.buscarPorCorreo(correo.trim().toLowerCase());
        if (cliente == null) {
            throw new Exception("Cliente no encontrado.");
        }

        cliente.setContrasena(nuevaContrasena);

        // Persistir el cambio usando el repositorio directamente
        org.example.repository.ClienteRepositoryImpl repo =
                new org.example.repository.ClienteRepositoryImpl();
        repo.actualizar(cliente);

        // Limpiar código usado
        codigosPendientes.remove(correo.trim().toLowerCase());
    }
}

package parqueaderocarros.modelo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Registro {
    private Map<String, Long> horaIngreso;
    private Map<String, Long> horaSalida;
    private Map<String, Integer> cupoAsignado;
    private static final String ARCHIVO_REGISTRO = "registro.dat";
    private static final String ARCHIVO_NUMERO_FACTURA = "numero_factura.dat";
    private int numeroFactura;
    private static final double TARIFA_POR_MINUTO = 100.0; // Tarifa en pesos por minuto
    private static final double IVA = 0.19; // IVA del 19%

    public Registro() {
        horaIngreso = new HashMap<>();
        horaSalida = new HashMap<>();
        cupoAsignado = new HashMap<>();
        cargarDatos();
        cargarNumeroFactura();
    }

    public void registrarIngreso(String placa, long horaIngresoValor, int cupo) {
        horaIngreso.put(placa, horaIngresoValor);
        cupoAsignado.put(placa, cupo);
        horaSalida.put(placa, -1L); // -1 indica que no ha salido aún
        guardarDatos();
    }

    public void registrarSalida(String placa, long horaSalidaValor) {
        horaSalida.put(placa, horaSalidaValor);
        incrementarNumeroFactura(); // Incrementar el número de factura
        guardarDatos();
    }

    public long obtenerHoraIngreso(String placa) {
        return horaIngreso.getOrDefault(placa, -1L);
    }

    public long obtenerHoraSalida(String placa) {
        return horaSalida.getOrDefault(placa, -1L);
    }

    public int obtenerCupoAsignado(String placa) {
        return cupoAsignado.getOrDefault(placa, -1);
    }

    public double obtenerTotalConIva(String placa) {
        long horaIngresoValor = obtenerHoraIngreso(placa);
        long horaSalidaValor = obtenerHoraSalida(placa);

        if (horaIngresoValor == -1L || horaSalidaValor == -1L) {
            throw new IllegalArgumentException("La placa no está registrada o la salida no está registrada.");
        }

        long tiempoEstancia = (horaSalidaValor - horaIngresoValor) / (1000 * 60); // Tiempo en minutos
        double total = tiempoEstancia * TARIFA_POR_MINUTO;
        double totalConIva = total + (total * IVA);
        return totalConIva;
    }

    public Map<String, Long> getHoraIngreso() {
        return horaIngreso;
    }

    public int obtenerNumeroFactura() {
        return numeroFactura;
    }

    private void incrementarNumeroFactura() {
        numeroFactura++;
        guardarNumeroFactura();
    }

    private void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_REGISTRO))) {
            oos.writeObject(horaIngreso);
            oos.writeObject(horaSalida);
            oos.writeObject(cupoAsignado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        File archivo = new File(ARCHIVO_REGISTRO);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_REGISTRO))) {
                horaIngreso = (Map<String, Long>) ois.readObject();
                horaSalida = (Map<String, Long>) ois.readObject();
                cupoAsignado = (Map<String, Integer>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarNumeroFactura() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(ARCHIVO_NUMERO_FACTURA))) {
            dos.writeInt(numeroFactura);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarNumeroFactura() {
        File archivo = new File(ARCHIVO_NUMERO_FACTURA);
        if (archivo.exists()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(ARCHIVO_NUMERO_FACTURA))) {
                numeroFactura = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            numeroFactura = 1; // Inicializar en 1 si no existe el archivo
        }
    }
}

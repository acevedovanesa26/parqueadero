package parqueaderocarros.controlador;

import parqueaderocarros.modelo.Parqueadero;
import parqueaderocarros.modelo.Registro;
import parqueaderocarros.vistas.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Controlador {
    private Parqueadero parqueadero;
    private Registro registro;
    private VistaPrincipal vistaPrincipal;
    private VistaIngreso vistaIngreso;
    private VistaSalida vistaSalida;
    private VistaFactura vistaFactura;

    private int numeroFactura = 1;

    public Controlador(Parqueadero parqueadero, Registro registro, VistaPrincipal vistaPrincipal, VistaIngreso vistaIngreso, VistaSalida vistaSalida, VistaFactura vistaFactura) {
        this.parqueadero = parqueadero;
        this.registro = registro;
        this.vistaPrincipal = vistaPrincipal;
        this.vistaIngreso = vistaIngreso;
        this.vistaSalida = vistaSalida;
        this.vistaFactura = vistaFactura;

        try {
            cargarNumeroFactura(); // Cargar el número de factura al iniciar
        } catch (IOException e) {
            e.printStackTrace();
        }

        initListeners();
    }

    private void initListeners() {
        vistaPrincipal.getBotonIngreso().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = vistaPrincipal.getPlaca();
                if (placa.matches("[A-Z]{3}\\d{3}")) {
                    int cupoAsignado = parqueadero.asignarCupo();
                    if (cupoAsignado != -1) {
                        long horaIngreso = System.currentTimeMillis();
                        registro.registrarIngreso(placa, horaIngreso, cupoAsignado);

                        vistaIngreso.setPlaca(placa);
                        vistaIngreso.setHoraIngreso(horaIngreso);
                        vistaIngreso.setCupoAsignado(cupoAsignado);
                        vistaIngreso.setVisible(true);
                    } else {
                        vistaPrincipal.mostrarMensaje("No hay cupos disponibles.");
                    }
                } else {
                    vistaPrincipal.mostrarMensaje("Placa inválida. Debe tener el formato AAA000.");
                }
            }
        });

        vistaPrincipal.getBotonSalida().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = vistaPrincipal.getPlaca();
                long horaSalida = System.currentTimeMillis();
                long horaIngreso = registro.obtenerHoraIngreso(placa);
                int cupoAsignado = registro.obtenerCupoAsignado(placa);

                if (horaIngreso != -1 && cupoAsignado != -1) {
                    long tiempoEstancia = horaSalida - horaIngreso;
                    registro.registrarSalida(placa, horaSalida);
                    parqueadero.liberarCupo(cupoAsignado);

                    vistaSalida.setPlaca(placa);
                    vistaSalida.setHoraSalida(horaSalida);
                    vistaSalida.setTiempoEstancia(tiempoEstancia);
                    vistaSalida.setVisible(true);
                } else {
                    vistaPrincipal.mostrarMensaje("La placa ingresada no se encuentra en el sistema.");
                }
            }
        });

        vistaSalida.getBotonFactura().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = vistaSalida.getPlaca();
                long tiempoEstancia = vistaSalida.getTiempoEstancia();
                int cupoAsignado = registro.obtenerCupoAsignado(placa);
                double totalConIva = calcularTotalConIva(tiempoEstancia);

                vistaFactura.setPlaca(placa);
                vistaFactura.setTotalConIva(totalConIva);
                vistaFactura.setNumeroFactura(numeroFactura++);
                vistaFactura.setCupoAsignado(cupoAsignado);
                vistaFactura.setTiempoTranscurrido(tiempoEstancia);
                vistaFactura.setVisible(true);
            }
        });

        vistaFactura.getBotonGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCompleto = vistaFactura.getNombreCompleto();
                String numeroDocumento = vistaFactura.getNumeroDocumento();
                String numeroCelular = vistaFactura.getNumeroCelular();
                String correoElectronico = vistaFactura.getCorreoElectronico();

                if (correoElectronico.matches("^[\\w-.]+@[\\w-]+\\.(com|co)$")) {
                    try {
                        generarArchivoFactura(nombreCompleto, numeroDocumento, numeroCelular, correoElectronico);
                        vistaFactura.mostrarMensaje("Factura generada correctamente.");
                    } catch (IOException ex) {
                        vistaFactura.mostrarMensaje("Error al generar la factura.");
                        ex.printStackTrace();
                    }
                } else {
                    vistaFactura.mostrarMensaje("Correo no válido.");
                }
            }
        });

        vistaPrincipal.getBotonInforme().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarInforme();
            }
        });
    }

    private double calcularTotalConIva(long tiempoEstancia) {
        double tarifaPorMinuto = 100.0 / 60.0;
        double total = tarifaPorMinuto * (tiempoEstancia / 60000); // Convertir ms a minutos
        return total * 1.19; // 19% IVA
    }

    private double calcularTotalConIva(long horaIngreso, long horaSalida) {
        double tarifaPorMinuto = 100.0 / 60.0;
        long tiempoEstancia = horaSalida - horaIngreso;
        double total = tarifaPorMinuto * (tiempoEstancia / 60000); // Convertir ms a minutos
        return total * 1.19; // 19% IVA
    }

    private void generarInforme() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("informe_general.txt"))) {
            Map<String, Long> horaIngreso = registro.getHoraIngreso();
            Set<String> placas = horaIngreso.keySet();

            for (String placa : placas) {
                long horaIngresoValor = registro.obtenerHoraIngreso(placa);
                long horaSalidaValor = registro.obtenerHoraSalida(placa);
                int cupoAsignado = registro.obtenerCupoAsignado(placa);

                // Cálculo del tiempo de estancia y total
                String tiempoEstancia = horaSalidaValor == -1 ? "Pendiente" : calcularTiempoEstancia(horaIngresoValor, horaSalidaValor);
                double total = horaSalidaValor == -1 ? 0 : calcularTotalConIva(horaIngresoValor, horaSalidaValor);

                writer.write("Placa: " + placa + "\n");
                writer.write("Hora de Ingreso: " + formatoHora(horaIngresoValor) + "\n");
                writer.write("Hora de Salida: " + (horaSalidaValor == -1 ? "Pendiente" : formatoHora(horaSalidaValor)) + "\n");
                writer.write("Cupo Asignado: " + cupoAsignado + "\n");
                writer.write("Tiempo de Estancia: " + tiempoEstancia + "\n");
                writer.write("Total con IVA: $" + total + "\n");
                writer.write("\n--------------------------\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String calcularTiempoEstancia(long horaIngreso, long horaSalida) {
        long tiempoEstancia = horaSalida - horaIngreso;
        long minutos = tiempoEstancia / 60000;
        long segundos = (tiempoEstancia % 60000) / 1000;
        return minutos + " minutos y " + segundos + " segundos";
    }

    private String formatoHora(long tiempo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date(tiempo));
    }

    private void generarArchivoFactura(String nombreCompleto, String numeroDocumento, String numeroCelular, String correoElectronico) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("factura_" + (numeroFactura - 1) + ".txt"))) {
            writer.write("Fecha: " + formatoHora(System.currentTimeMillis()) + "\n");
            writer.write("Número de Factura: " + (numeroFactura - 1) + "\n");
            writer.write("Placa: " + vistaFactura.getPlaca() + "\n");
            writer.write("Concepto: Factura por uso de parqueadero\n");
            writer.write("Nombre Completo: " + nombreCompleto + "\n");
            writer.write("Número de Documento: " + numeroDocumento + "\n");
            writer.write("Número de Celular: " + numeroCelular + "\n");
            writer.write("Correo Electrónico: " + correoElectronico + "\n");
            writer.write("Cupo Asignado: " + vistaFactura.getCupoAsignado() + "\n");
            writer.write("Tiempo Transcurrido: " + vistaFactura.getTiempoTranscurrido() + "\n");
            writer.write("Total con IVA: $" + vistaFactura.getTotalConIva() + "\n");
        }
    }

    private void cargarNumeroFactura() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("numero_factura.txt"))) {
            String linea = reader.readLine();
            if (linea != null) {
                numeroFactura = Integer.parseInt(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarNumeroFactura() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("numero_factura.txt"))) {
            writer.write(String.valueOf(numeroFactura));
        }
    }
}

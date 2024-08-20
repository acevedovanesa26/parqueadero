package parqueaderocarros.vistas;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VistaSalida extends JFrame {
    private JLabel placaLabel;
    private JLabel horaSalidaLabel;
    private JLabel tiempoEstanciaLabel;
    private JLabel totalConIvaLabel;
    private JButton botonFactura;
    private long tiempoEstancia;
    private double totalConIva;
    private static final double TARIFA_POR_MINUTO = 100.0; // Tarifa estándar por minuto
    private static final double IVA = 0.19; // IVA del 19%

    public VistaSalida() {
        setTitle("Salida de Vehículo");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Placa:"));
        placaLabel = new JLabel();
        add(placaLabel);

        add(new JLabel("Hora de Salida:"));
        horaSalidaLabel = new JLabel();
        add(horaSalidaLabel);

        add(new JLabel("Tiempo de Estancia:"));
        tiempoEstanciaLabel = new JLabel();
        add(tiempoEstanciaLabel);
        
        add(new JLabel("Total con IVA:"));
        totalConIvaLabel = new JLabel();
        add(totalConIvaLabel);

        botonFactura = new JButton("Generar Factura");
        add(botonFactura);

        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setPlaca(String placa) {
        placaLabel.setText(placa);
    }

    public String getPlaca() {
        return placaLabel.getText();
    }

    public void setHoraSalida(long horaSalida) {
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        horaSalidaLabel.setText(formatoHora.format(new Date(horaSalida)));
    }

    public void setTiempoEstancia(long tiempoEstancia) {
        this.tiempoEstancia = tiempoEstancia;
        tiempoEstanciaLabel.setText(formatTiempoEstancia(tiempoEstancia));
        calcularTotalConIva();
    }

    public void setTotalConIva(double totalConIva) {
        this.totalConIva = totalConIva;
        totalConIvaLabel.setText(String.format("$%.2f", totalConIva));
    }

    public long getTiempoEstancia() {
        return tiempoEstancia;
    }

    public double getTotalConIva() {
        return totalConIva;
    }

    public JButton getBotonFactura() {
        return botonFactura;
    }

    private String formatTiempoEstancia(long tiempoEstancia) {
        long minutos = (tiempoEstancia / 60000) % 60;
        long horas = (tiempoEstancia / 3600000);
        return String.format("%02d:%02d", horas, minutos);
    }

    private void calcularTotalConIva() {
        // Calcula el total con IVA
        double total = (tiempoEstancia / 60000) * TARIFA_POR_MINUTO; // Cálculo del total sin IVA
        totalConIva = total * (1 + IVA); // Aplicar IVA
        setTotalConIva(totalConIva);
    }
}

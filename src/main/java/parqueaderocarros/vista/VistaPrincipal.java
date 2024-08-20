package parqueaderocarros.vistas;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {
    private JTextField placaField;
    private JButton botonIngreso;
    private JButton botonSalida;
    private JButton botonInforme;
    private JButton botonFactura;

    public VistaPrincipal() {
        setTitle("PARQUEADERO SOLO CARROS");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Placa (AAA000):"));
        placaField = new JTextField();
        add(placaField);

        botonIngreso = new JButton("Registrar Ingreso");
        add(botonIngreso);

        botonSalida = new JButton("Registrar Salida");
        add(botonSalida);

        botonInforme = new JButton("Generar Informe");
        add(botonInforme);

        

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getPlaca() {
        return placaField.getText().toUpperCase();
    }

    public JButton getBotonIngreso() {
        return botonIngreso;
    }

    public JButton getBotonSalida() {
        return botonSalida;
    }

    public JButton getBotonInforme() {
        return botonInforme;
    }

    public JButton getBotonFactura() {
        return botonFactura;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}

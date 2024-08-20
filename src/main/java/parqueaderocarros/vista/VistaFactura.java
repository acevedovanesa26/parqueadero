package parqueaderocarros.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VistaFactura extends JFrame {
    private JTextField placaField;
    private JTextField totalField;
    private JTextField numeroFacturaField;
    private JTextField tiempoField;
    private JTextField cupoField;
    private JTextField nombreField;
    private JTextField documentoField;
    private JTextField celularField;
    private JTextField correoField;
    private JButton botonGuardar;

    public VistaFactura() {
        setTitle("Generar Factura");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize and configure components
        placaField = createTextField(false);
        numeroFacturaField = createTextField(false);
        totalField = createTextField(false);
        tiempoField = createTextField(false);
        cupoField = createTextField(false);
        nombreField = createTextField(true);
        documentoField = createTextField(true);
        celularField = createTextField(true);
        correoField = createTextField(true);

        botonGuardar = new JButton("Guardar Factura");

        // Add components to the frame
        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        add(placaField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Número de Factura:"), gbc);
        gbc.gridx = 1;
        add(numeroFacturaField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Total con IVA:"), gbc);
        gbc.gridx = 1;
        add(totalField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Tiempo de Estancia:"), gbc);
        gbc.gridx = 1;
        add(tiempoField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Cupo Asignado:"), gbc);
        gbc.gridx = 1;
        add(cupoField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        add(nombreField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Número de Documento:"), gbc);
        gbc.gridx = 1;
        add(documentoField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Número de Celular:"), gbc);
        gbc.gridx = 1;
        add(celularField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1;
        add(correoField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonGuardar, gbc);
    }

    private JTextField createTextField(boolean editable) {
        JTextField textField = new JTextField(20);
        textField.setEditable(editable);
        return textField;
    }

    public String getPlaca() {
        return placaField.getText();
    }

    public String getTotalConIva() {
        return totalField.getText();
    }

    public String getNumeroFactura() {
        return numeroFacturaField.getText();
    }

    public String getTiempoTranscurrido() {
        return tiempoField.getText();
    }

    public String getCupoAsignado() {
        return cupoField.getText();
    }

    public String getNombreCompleto() {
        return nombreField.getText();
    }

    public String getNumeroDocumento() {
        return documentoField.getText();
    }

    public String getNumeroCelular() {
        return celularField.getText();
    }

    public String getCorreoElectronico() {
        return correoField.getText();
    }

    public void setPlaca(String placa) {
        placaField.setText(placa);
    }

    public void setTotalConIva(double total) {
        totalField.setText(String.format("%.2f", total));
    }

    public void setNumeroFactura(int numero) {
        numeroFacturaField.setText(String.valueOf(numero));
    }

    public void setTiempoTranscurrido(long tiempo) {
        tiempoField.setText(String.valueOf(tiempo));
    }

    public void setCupoAsignado(int cupo) {
        cupoField.setText(String.valueOf(cupo));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void generarArchivoFactura() {
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String numeroFactura = numeroFacturaField.getText();
        String placa = placaField.getText();
        String nombreCompleto = nombreField.getText();
        String numeroDocumento = documentoField.getText();
        String numeroCelular = celularField.getText();
        String correoElectronico = correoField.getText();
        String cupoAsignado = cupoField.getText();
        String tiempoEstancia = tiempoField.getText();
        String total = totalField.getText();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("factura_" + numeroFactura + ".txt"))) {
            writer.write("Fecha: " + fecha + "\n");
            writer.write("Número de Factura: " + numeroFactura + "\n");
            writer.write("Placa del Vehículo: " + placa + "\n");
            writer.write("Concepto: Factura por uso de parqueadero\n");
            writer.write("Nombre Completo: " + nombreCompleto + "\n");
            writer.write("Número de Documento: " + numeroDocumento + "\n");
            writer.write("Número de Celular: " + numeroCelular + "\n");
            writer.write("Correo Electrónico: " + correoElectronico + "\n");
            writer.write("Cupo Asignado: " + cupoAsignado + "\n");
            writer.write("Tiempo de Estancia: " + tiempoEstancia + "\n");
            writer.write("Total con IVA: $" + total + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrarMensaje("Factura guardada correctamente.");
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }
}

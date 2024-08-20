package parqueaderocarros.vistas;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VistaIngreso extends JFrame {
    private JLabel placaLabel;
    private JLabel horaIngresoLabel;
    private JLabel cupoAsignadoLabel;

    public VistaIngreso() {
        setTitle("Ingreso de Vehículo");
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Placa:"));
        placaLabel = new JLabel();
        add(placaLabel);

        add(new JLabel("Hora de Ingreso:"));
        horaIngresoLabel = new JLabel();
        add(horaIngresoLabel);

        add(new JLabel("Cupo Asignado:"));
        cupoAsignadoLabel = new JLabel();
        add(cupoAsignadoLabel);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setPlaca(String placa) {
        placaLabel.setText(placa);
    }

    public void setHoraIngreso(long horaIngreso) {
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        horaIngresoLabel.setText(formatoHora.format(new Date(horaIngreso)));
    }

    public void setCupoAsignado(int cupoAsignado) {
        cupoAsignadoLabel.setText(String.valueOf(cupoAsignado));
    }
}

package parqueaderocarros;

import parqueaderocarros.controlador.Controlador;
import parqueaderocarros.modelo.Parqueadero;
import parqueaderocarros.modelo.Registro;
import parqueaderocarros.vistas.*;

public class ParqueaderoCarros {
    public static void main(String[] args) {
        Parqueadero parqueadero = new Parqueadero(3);
        Registro registro = new Registro();
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        VistaIngreso vistaIngreso = new VistaIngreso();
        VistaSalida vistaSalida = new VistaSalida();
        VistaFactura vistaFactura = new VistaFactura();

        Controlador controlador = new Controlador(parqueadero, registro, vistaPrincipal, vistaIngreso, vistaSalida, vistaFactura);
        vistaPrincipal.setVisible(true);
    }
}



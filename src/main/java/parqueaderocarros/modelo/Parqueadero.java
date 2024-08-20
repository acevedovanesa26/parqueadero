package parqueaderocarros.modelo;

public class Parqueadero {
    private boolean[] cupos;
    

    public Parqueadero(int totalCupos) {
        cupos = new boolean[totalCupos];
    }

    public int asignarCupo() {
        for (int i = 0; i < cupos.length; i++) {
            if (!cupos[i]) {
                cupos[i] = true;
                return i + 1;
            }
        }
        return -1; // No hay cupos disponibles
    }

    public void liberarCupo(int cupo) {
        if (cupo > 0 && cupo <= cupos.length) {
            cupos[cupo - 1] = false;
        }
    }

    public int obtenerCuposDisponibles() {
        int disponibles = 0;
        for (boolean ocupado : cupos) {
            if (!ocupado) {
                disponibles++;
            }
        }
        return disponibles;
    }
}

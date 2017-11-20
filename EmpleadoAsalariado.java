/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nomina;

/**
 *
 * @author alex
 */
public class EmpleadoAsalariado extends Empleado {

    private double salarioSemanal;

    public EmpleadoAsalariado(String nombre, String apellido, String numeroSeguroSocial, double salarioSemanal) {
        super(nombre, apellido, numeroSeguroSocial);

        if (salarioSemanal < 0.0) {
            throw new IllegalArgumentException(
                    "El salario semanal debe ser >= 0.0");
        }

        this.salarioSemanal = salarioSemanal;
    }

    public void setSalarioSemanal(double salarioSemanal) {
        if (salarioSemanal < 0.0) {
            throw new IllegalArgumentException(
                    "El salario semanal debe ser >= 0.0");
        }

        this.salarioSemanal = salarioSemanal;
    }

    public double getSalarioSemanal() {
        return salarioSemanal;
    }

    @Override
    public double ganancias() {
        return getSalarioSemanal();
    }

    @Override
    public String toString() {
        return String.format("empleado asalareado: %s%n%s: $%,.2f",
                super.toString(), "salario semanal", getSalarioSemanal());
    }
}

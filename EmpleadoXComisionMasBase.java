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
public class EmpleadoXComisionMasBase extends EmpleadoXComision {

    private double salarioBase; // salario base por semana 

    public EmpleadoXComisionMasBase(double salarioBase, double ventasBrutas, double porcentageComision, String nombre, String apellido, String numeroSeguroSocial) {
        super(ventasBrutas, porcentageComision, nombre, apellido, numeroSeguroSocial);
        this.salarioBase = salarioBase;
        if (salarioBase < 0.0) {
            throw new IllegalArgumentException("El salario base debe ser >= 0.0");
        }

        this.salarioBase = salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        if (salarioBase < 0.0) // validate salarioBase
        {
            throw new IllegalArgumentException("El salario base debe ser >= 0.0");
        }
        this.salarioBase = salarioBase;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    @Override
    public double ganancias() {
        return getSalarioBase() + super.ganancias();
    }

    @Override
    public String toString() {
        return String.format("%s %s; %s: $%,.2f",
                "base-asalareado", super.toString(),
                "salario Base", getSalarioBase());
    }
}

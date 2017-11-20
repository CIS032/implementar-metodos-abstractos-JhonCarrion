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
public class EmpleadoXComision extends Empleado {

    private double ventasBrutas; // ventas semanales brutas
    private double porcentageComision; // porcentaje de comisión

    public EmpleadoXComision(double ventasBrutas, double porcentageComision, String nombre, String apellido, String numeroSeguroSocial) {
        super(nombre, apellido, numeroSeguroSocial);
        this.ventasBrutas = ventasBrutas;
        this.porcentageComision = porcentageComision;

        if (porcentageComision <= 0.0 || porcentageComision >= 1.0) // validate
        {
            throw new IllegalArgumentException(
                    "el porcentaje de comisión debe ser > 0.0 y < 1.0");
        }

        if (ventasBrutas < 0.0) // validate
        {
            throw new IllegalArgumentException("las ventas brutas deben ser >= 0.0");
        }
        this.ventasBrutas = ventasBrutas;
        this.porcentageComision = porcentageComision;
    }

    public void setVentasBrutas(double ventasBrutas) {
        if (ventasBrutas < 0.0) {
            throw new IllegalArgumentException("las ventas brutas deben ser >= 0.0");
        }

        this.ventasBrutas = ventasBrutas;
    }

    public double getVentasBrutas() {
        return ventasBrutas;
    }

    public void setPorcentageComision(double porcentageComision) {
        if (porcentageComision <= 0.0 || porcentageComision >= 1.0) {
            throw new IllegalArgumentException(
                    "el porcentaje de comisión debe ser > 0.0 y < 1.0");
        }

        this.porcentageComision = porcentageComision;
    }

    public double getPorcentageComision() {
        return porcentageComision;
    }

    @Override
    public double ganancias() {
        return getPorcentageComision() * getVentasBrutas();
    }

    @Override
    public String toString() {
        return String.format("%s: %s%n%s: $%,.2f; %s: %.2f",
                "empleado por comisión", super.toString(),
                "Ventas Brutas", getVentasBrutas(),
                "porcentaje de Comisión", getPorcentageComision());
    }
}

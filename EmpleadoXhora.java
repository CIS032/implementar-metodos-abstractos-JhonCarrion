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
public class EmpleadoXhora extends Empleado {

    private double valorHora; // valor por hora
    private double horasTrabajadas; // horas trabajadas a las emana

    public EmpleadoXhora(double valorHora, double horasTrabajadas, String nombre, String apellido, String numeroSeguroSocial) {
        super(nombre, apellido, numeroSeguroSocial);
        this.valorHora = valorHora;
        this.horasTrabajadas = horasTrabajadas;
        if (valorHora < 0.0) // validar salario
        {
            throw new IllegalArgumentException(
                    "El salario por hora debe ser >= 0.0");
        }

        if ((horasTrabajadas < 0.0) || (horasTrabajadas > 168.0)) // validar Horas
        {
            throw new IllegalArgumentException(
                    "Las horas trabajadas deben ser >= 0.0 y <= 168.0");
        }

        this.valorHora = valorHora;
        this.horasTrabajadas = horasTrabajadas;
    }

    public void setValorHora(double valorHora) {
        if (valorHora < 0.0) // validar salario
        {
            throw new IllegalArgumentException(
                    "El salario por hora debe ser >= 0.0");
        }

        this.valorHora = valorHora;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setHorasTrabajadas(double horasTrabajadas) {
        if ((horasTrabajadas < 0.0) || (horasTrabajadas > 168.0)) // validar horas Trabajadas
        {
            throw new IllegalArgumentException(
                    "Las horas trabajadas deben ser >= 0.0 y <= 168.0");
        }

        this.horasTrabajadas = horasTrabajadas;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

//calcular ganancias; anula las ganancias del método abstracto en Empleado
    @Override
    public double ganancias() {
        if (getHorasTrabajadas() <= 40) // sin horas extras
        {
            return getValorHora() * getHorasTrabajadas();
        } else {
            return 40 * getValorHora() + (getHorasTrabajadas() - 40) * getValorHora() * 1.5;
        }
    }

    @Override
    public String toString() {
        return String.format("Empleado por horas: %s%n%s: $%,.2f; %s: %,.2f",
                super.toString(), "Salario por hora", getValorHora(),
                "horas trabajadas", getHorasTrabajadas());
    }
}

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
public class Nomina {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EmpleadoAsalariado ea
                = new EmpleadoAsalariado("John", "Smith", "111-11-1111", 800.00);

        EmpleadoXhora eh
                = new EmpleadoXhora(16.75, 40, "Karen", "Price", "222-22-2222");

        EmpleadoXComision ec
                = new EmpleadoXComision(10000, .06, "Sue", "Jones", "333-33-3333");

        EmpleadoXComisionMasBase ecmb
                = new EmpleadoXComisionMasBase(5000, 300, .04, "Bob", "Lewis", "444-44-4444");

        System.out.println("Empleados procesados ​​individualmente:");

        System.out.printf("%n%s%n%s: $%,.2f%n%n",
                ea, "ganancias", ea.ganancias());
        System.out.printf("%s%n%s: $%,.2f%n%n",
                eh, "ganancias", eh.ganancias());
        System.out.printf("%s%n%s: $%,.2f%n%n",
                ec, "ganancias", ec.ganancias());
        System.out.printf("%s%n%s: $%,.2f%n%n",
                ecmb,
                "ganancias", ecmb.ganancias());

        Empleado[] empleados = new Empleado[4];
        // inicializar areglo de Empleados
        empleados[0] = ea;
        empleados[1] = eh;
        empleados[2] = ec;

        empleados[3] = ecmb;
        System.out.printf("Empleados procesados ​​polimórficamente:%n%n");
        for (Empleado empleadoActual : empleados) {
            System.out.println(empleadoActual); // invokes toString
            if (empleadoActual instanceof EmpleadoXComisionMasBase) {
                EmpleadoXComisionMasBase employee = (EmpleadoXComisionMasBase) empleadoActual;
                employee.setSalarioBase(1.10 * employee.getSalarioBase());
                System.out.printf(
                        "nuevo salario base con 10%% el aumento es: $%,.2f%n",
                        employee.getSalarioBase());
            } // end if
            System.out.printf("ganancia $%,.2f%n%n", empleadoActual.ganancias());
        } // end for 
// obtener el nombre de tipo de cada objeto en la matriz de empleados
        for (int j = 0; j < empleados.length; j++) {
            System.out.printf("El Empleado %d es un %s%n", j,
                    empleados[j].getClass().getName());
        }
    }
}

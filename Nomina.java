/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nomina;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author alex
 */
public class Nomina {

    static String titulos[];
    private static ArrayList<Empleado> empleados = new ArrayList<>();
    static String path;

    private static void archivo() {
        try {
            String contenido = "TIPO EMPLEADO;NOMBRE;APELLIDO;NUMERO SEGURO SOCIAL;SALARIO SEMANAL;SALARIO POR HORAS;HORAS TRABAJADAS;VENTAS BRUTAS;PORCENTAJE COMISION;SALARIO BASE;GANACIA\n";
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccione el directorio de Destino");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("csv", "csv");
            chooser.setFileFilter(filtro);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                BufferedWriter salida = new BufferedWriter(new FileWriter(chooser.getSelectedFile().getAbsolutePath() + "/" + path));
                EmpleadoAsalariado ea;
                EmpleadoXhora eh;
                EmpleadoXComision ec;
                EmpleadoXComisionMasBase ecmb;
                for (Empleado emp : empleados) {
                    String nombre = emp.getNombre();
                    String apellido = emp.getApellido();
                    String nss = emp.getNumeroSeguroSocial();
                    String salario = "";
                    String valorHora = "";
                    String horasTrabajadas = "";
                    String ventasBrutas = "";
                    String comision = "";
                    String base = "";
                    double ganancia = emp.ganancias();
                    int tipo = 0;
                    if (emp instanceof EmpleadoAsalariado) {
                        ea = (EmpleadoAsalariado) emp;
                        tipo = 1;
                        salario = String.valueOf(ea.getSalarioSemanal());
                    } else if (emp instanceof EmpleadoXhora) {
                        eh = (EmpleadoXhora) emp;
                        tipo = 2;
                        valorHora = String.valueOf(eh.getValorHora());
                        horasTrabajadas = String.valueOf(eh.getHorasTrabajadas());
                    } else if (emp instanceof EmpleadoXComisionMasBase) {
                        ecmb = (EmpleadoXComisionMasBase) emp;
                        tipo = 4;
                        ventasBrutas = String.valueOf(ecmb.getVentasBrutas());
                        comision = String.valueOf(ecmb.getPorcentageComision());
                        base = String.valueOf(ecmb.getSalarioBase());
                    } else if (emp instanceof EmpleadoXComision) {
                        ec = (EmpleadoXComision) emp;
                        tipo = 3;
                        ventasBrutas = String.valueOf(ec.getVentasBrutas());
                        comision = String.valueOf(ec.getPorcentageComision());
                    }
                    contenido += tipo + ";" + nombre + ";" + apellido + ";" + nss + ";" + salario + ";" + valorHora + ";" + horasTrabajadas + ";" + ventasBrutas + ";" + comision + ";" + base + ";" + ganancia + "\n";
                }

                salida.write(contenido);
                salida.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Nomina.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String cargar() {
        String contenido = "";
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccione el archivo CSV");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("csv", "csv");
            chooser.setFileFilter(filtro);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                String s;
                String linea[];
                BufferedReader lector = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                path = chooser.getName(chooser.getSelectedFile());
                titulos = lector.readLine().split(";");
                while ((s = lector.readLine()) != null) {
                    contenido += s + "\n";
                    linea = s.split(";");
                    String nombre = linea[1];
                    String apellido = linea[2];
                    String nss = linea[3];
                    double ganancia = Double.parseDouble(linea[linea.length - 1].replace(',', '.'));
                    if (linea[0].equals("1")) {
                        double salario = Double.parseDouble(linea[4].replace(',', '.'));
                        empleados.add(new EmpleadoAsalariado(nombre, apellido, nss, salario));
                    } else if (linea[0].equals("2")) {
                        double valorHora = Double.parseDouble(linea[5].replace(',', '.'));
                        double horasTrabajadas = Double.parseDouble(linea[6].replace(',', '.'));
                        empleados.add(new EmpleadoXhora(valorHora, horasTrabajadas, nombre, apellido, nss));
                    } else if (linea[0].equals("3")) {
                        double ventasBrutas = Double.parseDouble(linea[7].replace(',', '.'));
                        double comision = Double.parseDouble(linea[8].replace(',', '.'));
                        empleados.add(new EmpleadoXComision(ventasBrutas, comision, nombre, apellido, nss));
                    } else if (linea[0].equals("4")) {
                        double ventasBrutas = Double.parseDouble(linea[7].replace(',', '.'));
                        double comision = Double.parseDouble(linea[8].replace(',', '.'));
                        double base = Double.parseDouble(linea[9].replace(',', '.'));
                        empleados.add(new EmpleadoXComisionMasBase(base, ventasBrutas, comision, nombre, apellido, nss));
                    }
                }

                lector.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Nomina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contenido;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//
//        EmpleadoAsalariado ea
//                = new EmpleadoAsalariado("John", "Smith", "111-11-1111", 800.00);
//
//        EmpleadoXhora eh
//                = new EmpleadoXhora(16.75, 40, "Karen", "Price", "222-22-2222");
//
//        EmpleadoXComision ec
//                = new EmpleadoXComision(10000, .06, "Sue", "Jones", "333-33-3333");
//
//        EmpleadoXComisionMasBase ecmb
//                = new EmpleadoXComisionMasBase(300, 5000, .04, "Bob", "Lewis", "444-44-4444");
        Nomina.cargar();
        int opt;
        do {
            opt = Integer.parseInt(JOptionPane.showInputDialog("0.- SALIR.\n1.- "
                    + "Ingresa Nuevo.\n2.- Modificar.\n3.- Eliminar.\n4.- Listar."));
            switch (opt) {
                case 1:
                    String tipos[] = {"Empleado Asalareado", "Empleado por Horas", "Empleado por Comisión", "Empleado por Comisión más sueldo Base"};
                    String nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
                    String apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
                    String nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                    String select = ((String) JOptionPane.showInputDialog(null, "<<SELECCIONE EL TIPO DE EMPLEADO>>", "Tipo de Empleado", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]));
                    switch (select) {
                        case "Empleado Asalareado":
                            double salario = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Sueldo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                            empleados.add(new EmpleadoAsalariado(nombre, apellido, nss, salario));
                            break;
                        case "Empleado por Horas":
                            double valorHora = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Valor de la Hora de trabajo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                            double horasTrabajadas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Número de Horas que el Empleado ha Trabajado"));
                            empleados.add(new EmpleadoXhora(valorHora, horasTrabajadas, nombre, apellido, nss));
                            break;
                        case "Empleado por Comisión":
                            double ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                            String comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                            if (comisionstr.contains("%")) {
                                comisionstr = comisionstr.replace("%", "");
                            }
                            double comision = Double.parseDouble(comisionstr);
                            empleados.add(new EmpleadoXComision(ventasBrutas, comision, nombre, apellido, nss));
                            break;
                        case "Empleado por Comisión más sueldo Base":
                            ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                            comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                            if (comisionstr.contains("%")) {
                                comisionstr = comisionstr.replace("%", "");
                            }
                            comision = Double.parseDouble(comisionstr);
                            double base = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Salario base del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                            empleados.add(new EmpleadoXComisionMasBase(base, ventasBrutas, comision, nombre, apellido, nss));
                            break;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                    for (Empleado emp : empleados) {
                        if (emp.getNumeroSeguroSocial().equals(nss)) {
                            empleados.remove(emp);
                        }
                    }
                    break;
                case 4:
                    String out = "";
                    for (Empleado emp : empleados) {
                        out += emp + "\nganancias: " + emp.ganancias() + "\n";
                    }
                    JOptionPane.showMessageDialog(null, out);
                    break;
                case 0:
                    Nomina.archivo();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Ingrese una opción valida\n"
                            + "numeros del 1 al 5 o 0 para salir", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } while (opt != 0);
        System.out.println("Empleados procesados ​​individualmente:");
        for (Empleado emp : empleados) {
            System.out.printf("%n%s%n%s: $%,.2f%n%n",
                    emp, "ganancias", emp.ganancias());
        }
//        System.out.printf("%s%n%s: $%,.2f%n%n",
//                eh, "ganancias", eh.ganancias());
//        System.out.printf("%s%n%s: $%,.2f%n%n",
//                ec, "ganancias", ec.ganancias());
//        System.out.printf("%s%n%s: $%,.2f%n%n",
//                ecmb,
//                "ganancias", ecmb.ganancias());
//
//        Empleado[] empleados = new Empleado[4];
//        // inicializar areglo de Empleados
//        empleados[0] = ea;
//        empleados[1] = eh;
//        empleados[2] = ec;
//
//        empleados[3] = ecmb;
//        System.out.printf("Empleados procesados ​​polimórficamente:%n%n");
//        for (Empleado empleadoActual : empleados) {
//            System.out.println(empleadoActual); // invokes toString
//            if (empleadoActual instanceof EmpleadoXComisionMasBase) {
//                EmpleadoXComisionMasBase employee = (EmpleadoXComisionMasBase) empleadoActual;
//                employee.setSalarioBase(1.10 * employee.getSalarioBase());
//                System.out.printf(
//                        "nuevo salario base con 10%% el aumento es: $%,.2f%n",
//                        employee.getSalarioBase());
//            } // end if
//            System.out.printf("ganancia $%,.2f%n%n", empleadoActual.ganancias());
//        } // end for 
// obtener el nombre de tipo de cada objeto en la matriz de empleados
        for (int j = 0; j < empleados.size(); j++) {
            System.out.printf("El Empleado %d es un %s%n", j,
                    empleados.get(j).getClass().getName());
        }
    }
}

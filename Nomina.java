/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nomina;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    static File path;
    static String archivo;

    private static void archivo() {
        try {
            String contenido = "TIPO EMPLEADO;NOMBRE;APELLIDO;NUMERO SEGURO SOCIAL;SALARIO SEMANAL;SALARIO POR HORAS;HORAS TRABAJADAS;VENTAS BRUTAS;PORCENTAJE COMISION;SALARIO BASE;GANACIA\n";
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccione el directorio de Destino");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(path);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("csv", "csv");
            chooser.setFileFilter(filtro);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                BufferedWriter salida = new BufferedWriter(new FileWriter(chooser.getSelectedFile().getAbsolutePath() + "/" + archivo));
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
                path = chooser.getCurrentDirectory();
                archivo = chooser.getName(chooser.getSelectedFile());
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

    public static boolean nuevo() {
        String tipos[] = {"Empleado Asalareado", "Empleado por Horas", "Empleado por Comisión", "Empleado por Comisión más sueldo Base"};
        String nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
        String apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
        String nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
        String select = ((String) JOptionPane.showInputDialog(null, "<<SELECCIONE EL TIPO DE EMPLEADO>>", "Tipo de Empleado", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]));
        switch (select) {
            case "Empleado Asalareado":
                double salario = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Sueldo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                return empleados.add(new EmpleadoAsalariado(nombre, apellido, nss, salario));
            case "Empleado por Horas":
                double valorHora = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Valor de la Hora de trabajo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                double horasTrabajadas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Número de Horas que el Empleado ha Trabajado"));
                return empleados.add(new EmpleadoXhora(valorHora, horasTrabajadas, nombre, apellido, nss));
            case "Empleado por Comisión":
                double ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                String comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                if (comisionstr.contains("%")) {
                    comisionstr = comisionstr.replace("%", "");
                }
                double comision = Double.parseDouble(comisionstr) / 100;
                return empleados.add(new EmpleadoXComision(ventasBrutas, comision, nombre, apellido, nss));
            case "Empleado por Comisión más sueldo Base":
                ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                if (comisionstr.contains("%")) {
                    comisionstr = comisionstr.replace("%", "");
                }
                comision = Double.parseDouble(comisionstr) / 100;
                double base = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Salario base del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                return empleados.add(new EmpleadoXComisionMasBase(base, ventasBrutas, comision, nombre, apellido, nss));
        }
        return false;
    }

    public static String cargarEditar() {
        EmpleadoAsalariado ea;
        EmpleadoXhora eh;
        EmpleadoXComision ec;
        EmpleadoXComisionMasBase ecmb;
        String nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
        String menu = "Modificar..\n0.- REGRESAR.\n1.- Nombre.\n2.- Apellido.\n3.- Número de Seguro Social.";
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getNumeroSeguroSocial().equals(nss)) {
                Empleado emp = empleados.get(i);
                if (emp instanceof EmpleadoAsalariado) {
                    ea = (EmpleadoAsalariado) emp;
                    menu += "\n4.- Salario.";
                    editar(menu, ea, i);
                } else if (emp instanceof EmpleadoXhora) {
                    eh = (EmpleadoXhora) emp;
                    menu += "\n4.- Valor de la Hora.\n5.- Horas Trabajadas.";
                    editar(menu, eh, i);
                } else if (emp instanceof EmpleadoXComisionMasBase) {
                    ecmb = (EmpleadoXComisionMasBase) emp;
                    menu += "\n4.- Ventas Brutas.\n5.- Porcentaje de Comisión.\n7.- Salario Base.";
                    editar(menu, ecmb, i);
                } else if (emp instanceof EmpleadoXComision) {
                    ec = (EmpleadoXComision) emp;
                    menu += "\n4.- Ventas Brutas.\n5.- Porcentaje de Comisión.";
                    editar(menu, ec, i);
                }
                break;
            }
        }

        return menu;
    }

    private static void editar(String menu, Empleado emp, int posicion) {
        String opt;
        do {
            opt = JOptionPane.showInputDialog(menu);
            if (opt.equals("0")) {
                break;
            }
            EmpleadoAsalariado ea;
            EmpleadoXhora eh;
            EmpleadoXComision ec;
            EmpleadoXComisionMasBase ecmb;
            String nombre = emp.getNombre();
            String apellido = emp.getApellido();
            String nss = emp.getNumeroSeguroSocial();
            if (emp instanceof EmpleadoAsalariado) {
                ea = (EmpleadoAsalariado) emp;
                double salario = ea.getSalarioSemanal();
                switch (opt) {
                    case "1":
                        nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
                        break;
                    case "2":
                        apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
                        break;
                    case "3":
                        nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                        break;
                    case "4":
                        salario = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Salario del Empleado"));
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Ingrese una opción válida.", "Selección Incorrecta", JOptionPane.ERROR_MESSAGE);
                }
                ea = new EmpleadoAsalariado(nombre, apellido, nss, salario);
                empleados.set(posicion, ea);
            } else if (emp instanceof EmpleadoXhora) {
                eh = (EmpleadoXhora) emp;
                double valorHora = eh.getValorHora();
                double horasTrabajadas = eh.getHorasTrabajadas();
                switch (opt) {
                    case "1":
                        nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
                        break;
                    case "2":
                        apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
                        break;
                    case "3":
                        nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                        break;
                    case "4":
                        valorHora = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Valor de la Hora de trabajo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                        break;
                    case "5":
                        horasTrabajadas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Número de Horas que el Empleado ha Trabajado"));
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Ingrese una opción válida.", "Selección Incorrecta", JOptionPane.ERROR_MESSAGE);
                }
                eh = new EmpleadoXhora(valorHora, horasTrabajadas, nombre, apellido, nss);
                empleados.set(posicion, eh);

            } else if (emp instanceof EmpleadoXComisionMasBase) {
                ecmb = (EmpleadoXComisionMasBase) emp;
                double comision = ecmb.getPorcentageComision();
                double ventasBrutas = ecmb.getVentasBrutas();
                double base = ecmb.getSalarioBase();
                switch (opt) {
                    case "1":
                        nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
                        break;
                    case "2":
                        apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
                        break;
                    case "3":
                        nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                        break;
                    case "4":
                        ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                        break;
                    case "5":
                        String comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                        if (comisionstr.contains("%")) {
                            comisionstr = comisionstr.replace("%", "");
                        }
                        comision = Double.parseDouble(comisionstr) / 100;
                        break;
                    case "6":
                        base = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Salario base del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Ingrese una opción válida.", "Selección Incorrecta", JOptionPane.ERROR_MESSAGE);
                }
                ecmb = new EmpleadoXComisionMasBase(base, ventasBrutas, comision, nombre, apellido, nss);
                empleados.set(posicion, ecmb);

            } else if (emp instanceof EmpleadoXComision) {
                ec = (EmpleadoXComision) emp;
                double comision = ec.getPorcentageComision();
                double ventasBrutas = ec.getVentasBrutas();
                switch (opt) {
                    case "1":
                        nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Empleado");
                        break;
                    case "2":
                        apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Empleado");
                        break;
                    case "3":
                        nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                        break;
                    case "4":
                        ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                        break;
                    case "5":
                        String comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                        if (comisionstr.contains("%")) {
                            comisionstr = comisionstr.replace("%", "");
                        }
                        comision = Double.parseDouble(comisionstr) / 100;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Ingrese una opción válida.", "Selección Incorrecta", JOptionPane.ERROR_MESSAGE);
                }
                ec = new EmpleadoXComision(ventasBrutas, comision, nombre, apellido, nss);
                empleados.set(posicion, ec);

            }

        } while (!opt.equals("0"));
    }

    public static Empleado editar() {
        String nssb = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
        Empleado emp = null;
        int pos = 0;
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getNumeroSeguroSocial().equals(nssb)) {
                emp = empleados.get(i);
                pos = i;
                break;
            }
        }
        if (emp != null) {
            String tipos[] = {"Empleado Asalareado", "Empleado por Horas", "Empleado por Comisión", "Empleado por Comisión más sueldo Base"};
            String nombre = emp.getNombre();
            String apellido = emp.getApellido();
            String nss = emp.getNumeroSeguroSocial();
            String select = ((String) JOptionPane.showInputDialog(null, "<<SELECCIONE EL NUEVO TIPO DE EMPLEADO>>", "Tipo de Empleado", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]));
            switch (select) {
                case "Empleado Asalareado":
                    double salario = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Sueldo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                    return empleados.set(pos, new EmpleadoAsalariado(nombre, apellido, nss, salario));
                case "Empleado por Horas":
                    double valorHora = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Valor de la Hora de trabajo del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                    double horasTrabajadas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Número de Horas que el Empleado ha Trabajado"));
                    return empleados.set(pos, new EmpleadoXhora(valorHora, horasTrabajadas, nombre, apellido, nss));
                case "Empleado por Comisión":
                    double ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                    String comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                    if (comisionstr.contains("%")) {
                        comisionstr = comisionstr.replace("%", "");
                    }
                    double comision = Double.parseDouble(comisionstr) / 100;
                    return empleados.set(pos, new EmpleadoXComision(ventasBrutas, comision, nombre, apellido, nss));
                case "Empleado por Comisión más sueldo Base":
                    ventasBrutas = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el valor Bruto de las ventas del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                    comisionstr = JOptionPane.showInputDialog("Ingrese el porcentaje de comisión Empleado\nIngrese el numero del porcentaje EJ. 4%");
                    if (comisionstr.contains("%")) {
                        comisionstr = comisionstr.replace("%", "");
                    }
                    comision = Double.parseDouble(comisionstr)/100;
                    double base = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el Salario base del Empleado\nSi tiene decimales favor use . (punto) no , (coma)"));
                    return empleados.set(pos, new EmpleadoXComisionMasBase(base, ventasBrutas, comision, nombre, apellido, nss));
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Nomina.cargar();
        int opt;
        do {
            opt = Integer.parseInt(JOptionPane.showInputDialog("0.- SALIR.\n1.- "
                    + "Ingresa Nuevo.\n2.- Modificar.\n3.- Eliminar.\n4.- Listar.\n5.- Cambiar tipo de Empleado."));
            switch (opt) {
                case 1:
                    Nomina.nuevo();
                    break;
                case 2:
                    Nomina.cargarEditar();
                    break;
                case 3:
                    String nss = JOptionPane.showInputDialog("Ingrese el Número de Seguro Social del Empleado");
                    for (int i = 0; i < empleados.size(); i++) {
                        if (empleados.get(i).getNumeroSeguroSocial().equals(nss)) {
                            empleados.remove(i);
                            break;
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
                case 5:
                    Nomina.editar();
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

        for (int j = 0; j < empleados.size(); j++) {
            System.out.printf("El Empleado %d es un %s%n", j,
                    empleados.get(j).getClass().getName());
        }
    }
}

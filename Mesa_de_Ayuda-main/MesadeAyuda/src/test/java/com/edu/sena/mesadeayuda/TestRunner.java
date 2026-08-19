package com.edu.sena.mesadeayuda;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicketTest;
import com.edu.sena.mesadeayuda.servicio.TicketServiceTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("EJECUTANDO PRUEBAS UNITARIAS DE MESA DE AYUDA");
        System.out.println("==================================================");

        int pasadas = 0;
        int falladas = 0;

        Class<?>[] testClasses = new Class<?>[]{
                EstadoTicketTest.class,
                TicketServiceTest.class
        };

        for (Class<?> testClass : testClasses) {
            System.out.println("\nClase de Prueba: " + testClass.getSimpleName());
            try {
                Object instance = testClass.getDeclaredConstructor().newInstance();
                for (Method m : testClass.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Test.class)) {
                        try {
                            // Ejecutar @BeforeEach si existe
                            for (Method bm : testClass.getDeclaredMethods()) {
                                if (bm.isAnnotationPresent(org.junit.jupiter.api.BeforeEach.class)) {
                                    bm.invoke(instance);
                                }
                            }

                            m.invoke(instance);
                            System.out.println("  [OK] " + m.getName());
                            pasadas++;
                        } catch (Exception e) {
                            Throwable cause = e.getCause() != null ? e.getCause() : e;
                            System.out.println("  [FAIL] " + m.getName() + " -> " + cause.getMessage());
                            cause.printStackTrace(System.out);
                            falladas++;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("  [ERROR] No se pudo instanciar " + testClass.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("\n==================================================");
        System.out.println("RESUMEN DE EJECUCIÓN:");
        System.out.println("Pruebas pasadas: " + pasadas);
        System.out.println("Pruebas fallidas: " + falladas);
        System.out.println("==================================================");

        if (falladas > 0) {
            System.exit(1);
        }
    }
}

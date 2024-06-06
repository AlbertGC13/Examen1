package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelMatrixSearch {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_COUNT = 4;
    private static final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int TARGET = 256; // Número a buscar
    private static final AtomicBoolean found = new AtomicBoolean(false);

    public static void main(String[] args) {
        // Inicializar la matriz con valores aleatorios
        Random random = new Random();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                matrix[i][j] = random.nextInt(1000);
            }
        }

        // Medir el tiempo de ejecución de la búsqueda secuencial
        long inicio = System.currentTimeMillis();
        boolean resultado = sequentialSearch();
        long fin = System.currentTimeMillis();
        System.out.println("El Resultado de la busqueda secuencial es: " + resultado);
        System.out.println("El tiempo de busqueda secuencial es: " + (fin - inicio)+ "ms");

        // Medir el tiempo de ejecución de la búsqueda paralela
        inicio = System.currentTimeMillis();
        parallelSearch();
        fin = System.currentTimeMillis();
        System.out.println("El Resultado búsqueda paralela: " + found.get());
        System.out.println("El Tiempo búsqueda paralela: " + (fin - inicio) + "ms");
    }

    private static boolean sequentialSearch() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrix[i][j] == TARGET) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void parallelSearch() {
        int chunkSize = (MATRIX_SIZE / THREAD_COUNT);
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int inicioH = i * chunkSize;
            final int finH = (i + 1) * chunkSize;
            threads[i] = new Thread(() -> {
                for (int row = inicioH; row < finH && !found.get(); row++) {
                    for (int col = 0; col < chunkSize; col++) {
                        if (matrix[row][col] == TARGET) {
                            found.set(true);
                            return;
                        }
                    }

                }
            });
            threads[i].start();
        }


        for (Thread thread : threads) {
            try{
                thread.join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
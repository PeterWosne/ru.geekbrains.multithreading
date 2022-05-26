package ru.geekbrains.multithreading;

import java.util.Arrays;

public class Main {

    static final int size = 10000000;
    static final int h = size/2;
    static final int q = size/4;
    static final int s = (int) Math.floor(size/6);
    static final int o = size/8;

    public static void main(String[] args) {
        float[] a1 = calcDefault();
        float[] a2 = calcInTwoThreads();
        float[] a2_1 = calcInTwoThreadsVers2_0();
        float[] a4 = calcInFourThreads();
        float[] a6 = calcInSixThreads();
        float[] a8 = calcInEightThreads();

        System.out.println("Arrays a1 and a2 equal? " + Arrays.equals(a1, a2));
        System.out.println("Arrays a1 and a2_1 equal? " + Arrays.equals(a1, a2_1));
        System.out.println("Arrays a1 and a4 equal? " + Arrays.equals(a1, a4));
        System.out.println("Arrays a1 and a6 equal? " + Arrays.equals(a1, a6));
        System.out.println("Arrays a1 and a8 equal? " + Arrays.equals(a1, a8));
    }

    private static float[] calcDefault() {
        float[] arr = new float[size];
        Arrays.fill(arr,1f);
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 1 потоке: %dмс\n",testTimeMS);
        return arr;
    }

    private static float[] calcInTwoThreads() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < h; i++) {
                    arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = h; i < size; i++) {
                    arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 2 потоках: %dмс\n",testTimeMS);
        return arr;
    }

    private static float[] calcInTwoThreadsVers2_0() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0,h);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < h; i++) {
                a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < h; i++) {
                a2[i] = (float)(a2[i] * Math.sin(0.2f + (i + h)/ 5) * Math.cos(0.2f + (i + h) / 5) * Math.cos(0.4f + (i + h) / 2));
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 2 потоках(с разбивкой и склейкой массива): %dмс\n",testTimeMS);
        return arr;
    }

    private static float[] calcInFourThreads() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < q; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = q; i < 2*q; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 2*q; i < 3*q; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 3*q; i < size; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 4 потоках: %dмс\n",testTimeMS);
        return arr;
    }

    private static float[] calcInSixThreads() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < s; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = s; i < 2*s; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 2*s; i < 3*s; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 3*s; i < 4*s; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t5 = new Thread(() -> {
            for (int i = 4*s; i < 5*s; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t6 = new Thread(() -> {
            for (int i = 5*s; i < size; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 6 потоках: %dмс\n",testTimeMS);
        return arr;
    }

    private static float[] calcInEightThreads() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = o; i < 2*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 2*o; i < 3*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t4 = new Thread(() -> {
            for (int i = 3*o; i < 4*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t5 = new Thread(() -> {
            for (int i = 4*o; i < 5*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t6 = new Thread(() -> {
            for (int i = 5*o; i < 6*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t7 = new Thread(() -> {
            for (int i = 6*o; i < 7*o; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread t8 = new Thread(() -> {
            for (int i = 7*o; i < size; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long testTimeMS = System.currentTimeMillis() - a;
        System.out.printf("Время выполнения в 8 потоках: %dмс\n",testTimeMS);
        return arr;
    }
}

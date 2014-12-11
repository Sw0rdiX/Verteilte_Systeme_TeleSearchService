package ue1;

/**
 * Author   : Nils Kienöl
 * Date     : 07.10.2014
 */
class Work {
    public static void main(String[] args) {
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();
        Thread t1 = new Thread(mt1);       // Das ist der Thread
        Thread t2 = new Thread(mt2);       // Das ist der Thread
        t1.start();
        System.out.println("ich bin " + Thread.currentThread().getName());
        t2.start();
        System.out.println("ich bin " + Thread.currentThread().getName());
    }
}
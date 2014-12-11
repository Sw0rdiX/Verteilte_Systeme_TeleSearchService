package ue1;

/**
 * Author   : Nils Kienöl
 * Date     : 07.10.2014
 */
class MyThread implements Runnable {

    @Override
    public void run() {
        System.out.println("Ich bin " + Thread.currentThread().getName());
    }
}
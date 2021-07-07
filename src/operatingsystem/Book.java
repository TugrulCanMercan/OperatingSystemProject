/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystem;

import java.util.concurrent.Semaphore;

/**
 *
 * @author tugrulcanmercan
 */
public class Book {
    private Semaphore bookSemaphore = new Semaphore(1);
    private int bookId;
    public Book(int bookId){
        this.bookId = bookId;
    }
    public int getBookId(){
        return bookId;
    }
    public boolean take(){
       return bookSemaphore.tryAcquire();
    }
    public void put(){
        bookSemaphore.release();
    }
    
}

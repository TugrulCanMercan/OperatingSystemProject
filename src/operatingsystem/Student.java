/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author tugrulcanmercan
 */
public class Student{
    private int studentId;
    private Semaphore stdtSemaphore;
    List<Book> learnedBooks;
    List<Book> ihtiyacımOlanKitaplar;
    public Student(int studentId){
        this.studentId = studentId;
        this.stdtSemaphore = new Semaphore(1);
        this.learnedBooks = Collections.synchronizedList(new ArrayList());
        this.ihtiyacımOlanKitaplar = Collections.synchronizedList(new ArrayList());
    }
    public int getStudentId(){
        return studentId;
    }
    public boolean selectedStudent(){
        if (learnedBooks.size()!=6) {
            return stdtSemaphore.tryAcquire();
        }
        System.out.println(getStudentId()+" nolu ö mezun oldu veya zaten kütüphanede ve kitap okuyor");
        return false;
    }
    public void putStudent(){
        stdtSemaphore.release();
    }
    public void addLearnBook(Book book){
        if (!learnedBooks.contains(book)) {
            learnedBooks.add(book);
        }
    }
    public void ihtiyacımaEkle(Book book){
        if (!ihtiyacımOlanKitaplar.contains(book)) {
            ihtiyacımOlanKitaplar.add(book);
        }
    }
    

   
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package operatingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author tugrulcanmercan
 */
public class OperatingSystem {
    
    /**
     * @param args the command line arguments
     */
    
    
    static List<Student> studentList = Collections.synchronizedList(new ArrayList());
    static List<Student> studentMezunList = Collections.synchronizedList(new ArrayList());
    public static void main(String[] args) throws InterruptedException {
        
        
        
        List<Book> booklList = new ArrayList<>();
       //burda öğrenciler ve kitap objeleri yaratılıyor
        for (int i = 0; i < 40; i++) {
            studentList.add(new Student(i));
            if (booklList.size()<6) {
                booklList.add(new Book(i));
            }
            
        }
        //burda threadler oluşturuluyor ve hedeflerine aynı referansı tutan kitap listeleri veriliyor
        Thread thread1 = new Thread(new Librarian(booklList), "T1");
        Thread thread2 = new Thread(new Librarian(booklList), "T2");
        Thread thread3 = new Thread(new Librarian(booklList), "T3");
        thread1.start();
        thread2.start();
        thread3.start();
        
        thread1.join();
        thread2.join();
        thread3.join();
        
        //burada main thread child threadleri bekliyor hepsinin işi bitince giriyor
        System.out.println("######################################################################");
        System.out.println("######################################################################");
        System.out.println("######################################################################");
        
        System.out.println("toplam mezun öğrenci sayısı : " + studentMezunList.size());
        
        
        boolean kontrol = true;
        while (kontrol) {
            Scanner selectedStdScanner = new Scanner(System.in);
            int select = selectedStdScanner.nextInt();
            
            switch (select) {
                case 1:
                    System.out.println("öğrenci seç");
                    
                    System.out.println("okuduğu kitap listesi");
                    int selectt = selectedStdScanner.nextInt();
                    studentMezunList.get(selectt).learnedBooks.forEach((t) -> {
                        System.out.println("okuduğu kitaplar : "+ t.getBookId());
                        
                    });
                    
                    break;
                case 2:
                    System.out.println("çıkış");
                    kontrol = false;
                    break;
                default:
                    System.out.println("yanlış seçim");
            }
            
            
            
        }
    }
}

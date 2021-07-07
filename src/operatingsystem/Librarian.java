/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package operatingsystem;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tugrulcanmercan
 */
public class Librarian implements Runnable{
    volatile boolean check = true;
    private List<Book> bookList;
    
    public Librarian(List<Book> bookList){
        this.bookList = bookList;
    }
    //hocam burda start fonksiyonu runu çalıştırıyor ve ben kütüphaneci olarak adlandırdım işleri herkes mezun olunca döngüden çıkıyolar
    @Override
    public void run() {
        while (check) {
            //burda mezun sayısına göre threadlerin işi sonlanıyor ve else düşmüyor
            if (OperatingSystem.studentMezunList.size()==40) {
                check = false;
            }else{
//burda bütün olayların gerçekleştiği kısmı try catch içine aldım çünkü öğrenci listesinden mezun olanı remove ettim
//ve mezun listesine ekledim bunu gereksiz yere random sayılarla aradığım için sürekli mezun olmuş öğrencileri çağırmayayım
//diye yaptım ama sonucunda herkes mezun olunca random sayı ile mezun olmayanları aradığım için herkes mezun olunca null olunca hata
//olduğu için catche düşsün ve orda check değişkenini false ettim
          
                try {
                    //bahsettiğim yer burası studentList boşalınca nextInt null exaption oluyor onun için try catch içinde
                    int random = ThreadLocalRandom.current().nextInt(OperatingSystem.studentList.size());
                    Student selectedStudent = OperatingSystem.studentList.get(random);
                    //yukarıdaki satırda random öğrenci seçildi
                    if (selectedStudent.selectedStudent()) {
                        //if in içine ilk giren thread seçilen öğrencinin semapore değerini değiştirdi ve öğrenci kütüphaneciyle konuşmaya başladı
                        //ayrıca burası seçilen öğrencinin okuduğu kitap sayısına göre semapore tryAcquire u dönüyor mezun değilse giriyor
                        System.out.println(selectedStudent.getStudentId()+" numaralı öğrenci şuan" + Thread.currentThread().getName() + " nolu kütüphaneciyle konuşuyor");
                        //burda kütüphaneden ihityacıma göre listemi kontrol ediyorum ve listeme ekliyorum
                        //burda ihityaç listem boş olduğu için ilk sefer else if e düşüyor
                        bookList.forEach((book) -> {
                            
                            if (!selectedStudent.learnedBooks.contains(book)) {
                                
                                selectedStudent.ihtiyacımaEkle(book);
                            }else if(selectedStudent.ihtiyacımOlanKitaplar.isEmpty()){
                                selectedStudent.ihtiyacımaEkle(book);
                            }
                            
                        });
                        //burda ihtiyaç listeme göre random bir kitap soruyorum sorduğum kitap başkasında değilse semapore unu tryAcquire fonksiyonuyla
                        //kontrol edip aktif ediyorum
                        
                        int bookRandom = (int) (Math.random()*selectedStudent.ihtiyacımOlanKitaplar.size());
                        Book ihtiyacListemdekiRandomKitap = selectedStudent.ihtiyacımOlanKitaplar.get(bookRandom);
                        System.out.println(selectedStudent.getStudentId()+" numaralı öğrenci "+Thread.currentThread().getName()+"numaralı kütüphaneciye "+ihtiyacListemdekiRandomKitap.getBookId()+"numaralı kitabı alabilirmiyim şuan elinizde var mı diye soruyor ?");
                        System.out.println(Thread.currentThread().getName()+" nolu kütüphaneci "+ihtiyacListemdekiRandomKitap.getBookId() +" kontrol ediyor");
                        if (ihtiyacListemdekiRandomKitap.take()) {
                            //yukarda bahsettiğim yer burası alabirse girer alamaz ise giremez else düşer
                            System.out.println(Thread.currentThread().getName()+" kitabı alabilirsin ");
                            //burda yeni bir thread açtım aslında burda olan şey kütüphanecinin öğrenci ilen işi burda bitiyor ve
                            //kütüphaneci yeni bir müşteri alabilecek duruma geçebiliyor
                            Thread student = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //burda öğrenci kitabını okuyor
                                        System.out.println(selectedStudent.getStudentId()+" nolu öğrenci kitabı okuyor :");
                                        Thread.sleep(100);
                                        selectedStudent.addLearnBook(ihtiyacListemdekiRandomKitap);
                                        selectedStudent.ihtiyacımOlanKitaplar.remove(ihtiyacListemdekiRandomKitap);
                                        System.out.println(selectedStudent.getStudentId()+" nolu öğrenci "+ ihtiyacListemdekiRandomKitap.getBookId()+" nolu kitabı okudu ve bıraktı");
                                        if (selectedStudent.learnedBooks.size()==6) {
                                            System.out.println(selectedStudent.getStudentId()+" nolu öğrenci mezun olduu !!!");
                                            OperatingSystem.studentMezunList.add(selectedStudent);
                                            OperatingSystem.studentList.remove(selectedStudent);
                                        }
                                        //öğrencini ve kitabın semapore değeri açılıyor
                                        selectedStudent.putStudent();
                                        ihtiyacListemdekiRandomKitap.put();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Librarian.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            student.start();
                           //burda kütühane threadi işlemlere devam ediyor ve yeni biröğrenciyle ilgilenebiliyor
                           
                            
                        }else{
                            System.out.println("şuanda almak istediğiniz "+ ihtiyacListemdekiRandomKitap.getBookId()+" nolu kitap "+selectedStudent.getStudentId()+"nolu öğrencide");
                            
                            
                        }
                    }else{
                        try {
                            Thread.sleep(100);
                            selectedStudent.putStudent();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Librarian.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (Exception e) {
                    check = false;
                    System.out.println("artık herkes mezun");
                }
                
                
            }
        }
        
        
        
    }
    
}

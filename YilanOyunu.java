
package yilanoyunu;

import java.util.Scanner;
//Aslıhan Coşkun 210303038
//Cansu DEMİR 210303041

public class YilanOyunu {

    // Oyun tahtasının boyutunu tutan değişken
    private static int BOARD_SIZE ;

    // Boş hücre sembolü
    private static final char EMPTY_CELL = '.';

    // Yılanın vücut hücreleri sembolü
    private static final char SNAKE_BODY = 'O';

    // Yılanın başı sembolü
    private static final char SNAKE_HEAD = 'X';

    // Yem sembolü
    private static final char FOOD = '$';

    // Bomba sembolü
    private static final char BOMB = '*';

    // Yılanın konumlarını tutan bağlı liste
    private static SimpleLinkedList snake;

    // Yem konumu
    private static Node food;

    // Bomba konumu
    private static Node bomb;

    // Oyuncu skoru
    private static int score = 0;

    // Hamle sayısı
    public static int step = 0;

    // Bomba durumu
    public static boolean situation=false;

    // Bombanın patlamasını kontrol etmek için geçici adım sayısı
    public static int tempStep=0;

    public static void main(String[] args) {
        // Kullanıcıdan tahta boyutunu al
        Scanner scanner = new Scanner(System.in);
        System.out.print("Lütfen tahtanın boyutunu girin: ");
        BOARD_SIZE =scanner.nextInt();

        // Oyun tahtasını temsil eden iki boyutlu bir dizi
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];

        // Yılan, yem ve bombayı oluştur
        snake = new SimpleLinkedList();
        food = createFood();
        bomb = createBomb();

        // Tahtayı başlat
        initializeBoard(board);

        // Oyun döngüsü
        while (true) {
            // Tahtayı yazdır
            printBoard(board);

            // Bomba durumunu kontrol et ve gerekirse patlat
            bombaPatlat();

            // Skoru ve hamle sayısını yazdır
            System.out.println("Score: " + score);
            System.out.println("Hamle: " + step);
            System.out.println("Bomba Durum: " + situation);

            // Kullanıcıdan hareket yönünü al
            System.out.print("Hareket yönünü seçin (W(up)/A(left)/S(down)/D(right)): ");
            char input = scanner.next().charAt(0);

            // Yılanı hareket ettir
            moveSnake(input);

            // Çarpışma kontrolü
            if (checkCollision()) {
                System.out.println("Oyun bitti! Skorun: " + score);
                break;
            }

            // Tahtayı güncelle
            updateBoard(board);

            // Yem yenildiyse skoru arttır
            if (snake.getFirst().x == food.x && snake.getFirst().y == food.y) {
                snake.addLast(food.x, food.y);
                food = createFood();
                score += 10; // Yem yendiğinde skoru arttır
            }

            // Bombaya çarpıldıysa durumu güncelle
            if (snake.getFirst().x == bomb.x && snake.getFirst().y == bomb.y) {
                situation=true;   //Bombanın patlama durumunu kontrol eder
                tempStep=step + 3;  //Bomba yendikten 3 adım sonra yılanın boyu azalır.
                score = Math.max(0, score - 5); // Bomba yendiğinde skoru azalt, minimum 0 olacak
                bomb = createBomb();
            }

            // Yılan uzunluğu 3'ten küçükse veya skor negatifse oyunu bitir
            if (snakeLength() < 3 || score < 0) {
                System.out.println("Oyun bitti! Skorun : " + score);
                break;
            }
        }
    }

    // Bombanın patlamasını kontrol et ve gerekirse yılanın son hücresini kaldır
    private static void bombaPatlat(){
        if(situation){
            if(tempStep==step){
                snake.removeLast();
                situation=false;
            }
        }
    }

    // Oyun tahtasını başlat
    private static void initializeBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }

        // Başlangıçta yılanı rastgele konumlandır
        int initialX = (int) (Math.random() * (BOARD_SIZE - 5));    //Rastgele üretilen sayı ile çarptığımızda x koordinatının max değeri 
        int initialY = (int) (Math.random() * BOARD_SIZE);         //Yılanın başlangıç yüksekliği

        for (int i = 0; i < 5; i++) {                            //Yılanın başlangıç pozisyonundan 5 hücre ekliyoruz
            snake.addFirst(initialX + i, initialY);            // x koordinatı her döngüde bir birim artay y sabit kalır
        }

        board[food.y][food.x] = FOOD;   //yiyeceğin x ve y koordinatları
        board[bomb.y][bomb.x] = BOMB;   //Bombanın x ve y koordinatları
    }

    // Oyun tahtasını yazdır
    private static void printBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }

        Node status = snake.getFirst();
        while (status != null) {
            if (status == snake.getFirst()) {
                board[status.y][status.x] = SNAKE_HEAD;    // Tahtadaki ilgili hücre güncellenir
            } else {
                board[status.y][status.x] = SNAKE_BODY;
            }
            status = status.next;
        }

        // Yem yerine ulaştığımızda FOOD sembolünü X ile değiştir
        if (snake.getFirst().x == food.x && snake.getFirst().y == food.y) {
            board[food.y][food.x] = SNAKE_HEAD;
        } else {
            board[food.y][food.x] = FOOD;
        }

        // Bomba yerine ulaştığımızda BOMB sembolünü X ile değiştir
        if (snake.getFirst().x == bomb.x && snake.getFirst().y == bomb.y) {
            board[bomb.y][bomb.x] = SNAKE_HEAD;
        } else {
            board[bomb.y][bomb.x] = BOMB;
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Yılanı belirtilen yöne hareket ettir
    private static void moveSnake(char direction) {
        Node head = snake.getFirst();
        Node newHead;
        step++;

       if(direction=='w' || direction=='W'){
            newHead = new Node(head.x, (head.y - 1 + BOARD_SIZE) % BOARD_SIZE);
        }
        else if(direction=='a' || direction=='A'){
            newHead = new Node((head.x - 1 + BOARD_SIZE) % BOARD_SIZE, head.y);
        }
        else if(direction=='s' || direction=='S'){
            newHead = new Node(head.x, (head.y + 1 ) % BOARD_SIZE);
        }
        else if(direction=='d' || direction=='D'){
            newHead = new Node((head.x + 1 ) % BOARD_SIZE, head.y);
        }
        else{
            newHead = head;
            System.out.println("Gecersiz deger!!");
        }

        snake.addFirst(newHead.x, newHead.y);
        snake.removeLast();
    }

    // Çarpışma kontrolü
    private static boolean checkCollision() {
        Node head = snake.getFirst();

        Node status = snake.getFirst().next;
        while (status != null) {         // Yılanın başından sonraki düğümleri kontrol et
            if (head.x == status.x && head.y == status.y) {  //Yılanın başı sonraki düğümle aynı konumdaysa çarpışma vardır
                return true;
            }
            status = status.next;
        }

        return false;       //Hiçbir çarpışma yoksa false döndür
    }

    // Oyun tahtasını güncelle
    private static void updateBoard(char[][] board) {
        Node status = snake.getFirst();
        while (status != null) {         
            if (status == snake.getFirst()) {
                board[status.y][status.x] = SNAKE_HEAD;
            } else {
                board[status.y][status.x] = SNAKE_BODY;
            }
            status = status.next;
        }

        // Yem yerine ulaştığımızda FOOD sembolünü X ile değiştir
        if (snake.getFirst().x == food.x && snake.getFirst().y == food.y) {
            board[food.y][food.x] = SNAKE_HEAD;
        } else {
            board[food.y][food.x] = FOOD;
        }

        // Bomba yerine ulaştığımızda BOMB sembolünü X ile değiştir
        if (snake.getFirst().x == bomb.x && snake.getFirst().y == bomb.y) {
            board[bomb.y][bomb.x] = SNAKE_HEAD;
        } else {
            board[bomb.y][bomb.x] = BOMB;
        }
    }

    // Yılan uzunluğunu hesapla
    private static int snakeLength() {
        int length = 0;
        Node status = snake.getFirst();
        while (status != null) {     //Yılanın tüm düğümlerini gezerek uzunluğu hesapla
            length++;           //Her düğümde bir arttır.
            status = status.next;
        }
        return length;     //Hesaplanan yılan uzunluğu döner
    }

    // Yeni bir yem oluştur
    private static Node createFood() { //Rastgele gelen sayı ile tahta boyutu çarpıldığında yemin x ve y koordinatını belirleriz
        return new Node((int) (Math.random() * BOARD_SIZE), (int) (Math.random() * BOARD_SIZE));
    }

    // Yeni bir bomba oluştur
    private static Node createBomb() {
        return new Node((int) (Math.random() * BOARD_SIZE), (int) (Math.random() * BOARD_SIZE));
    }
}

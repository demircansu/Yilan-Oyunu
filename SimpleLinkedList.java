
package yilanoyunu;

public class SimpleLinkedList {
     private Node head;
    public SimpleLinkedList() {
        this.head = null;    //Liste başlangıçta boş olsun
    }
    public void addFirst(int x, int y) {   //Lİsteye yeni bir düğüm başa ekleriz
        Node newNode = new Node(x, y);
        newNode.next = head;
        head = newNode;
    }
    public void removeLast() {   //Liste sonundan bir düğüm kaldırırız
        if (head == null || head.next == null) {
            head = null;
            return;
        }
        //Boş değilse veya bir düğüm varsa
        Node secondLast = head;
        while (secondLast.next.next != null) {
            secondLast = secondLast.next;
        }
        secondLast.next = null;
    }
    //Liste içinden x,y koordinatı içeren bir düğüm olup olmadığını kontrol eder
    public boolean contains(int x, int y) {
        Node current = head;
        while (current != null) {
            if (current.x == x && current.y == y) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    public Node getFirst() {   //Listenin başındaki düğümü alır
        return head;
    }
    public void print() {
        Node current = head;
        while (current != null) {
         //   System.out.print("(" + current.x + "," + current.y + ") ");
            current = current.next;
        }
        System.out.println();
    }
    public void addLast(int x, int y) {   //Liste sonuna yeni düğüm eklenir
        Node newNode = new Node(x, y);
        if (head == null) {    //Liste boşsa head yeni düğümü gösterir
            head = newNode;
            return;
        }
          
        //Son düğümü bulmak için
        Node last = head;
        while (last.next != null) {
            last = last.next;
        }
        last.next = newNode;
    }
}

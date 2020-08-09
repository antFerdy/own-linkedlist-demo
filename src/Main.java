import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Main {


    public static void main(String[] args) {
        OwnLinkedList<String> l = new OwnLinkedList<String>();

//        LinkedList<String> l = new LinkedList<String>();
        l.add("Buzz");
        l.add("Feed");
        l.add("News");
        System.out.println(l.size());
//        System.out.println(l.get(l.size() - 1));

        ListIterator<String> it = l.listIterator(l.size() - 1);
        it.next();
        it.set("news");

        System.out.println("Remained nodes:");
        l.forEach(s -> {
            System.out.println(s);
        });

    }
}

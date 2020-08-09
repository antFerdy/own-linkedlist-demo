public class Main {


    public static void main(String[] args) {
        OwnLinkedList<String> l = new OwnLinkedList<>();
        l.add("Buzz");
        l.add("Feed");
        l.add("News");
        System.out.println(l.size());
        System.out.println(l.get(1));
    }
}

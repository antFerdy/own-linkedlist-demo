import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class OwnLinkedList<E> extends AbstractSequentialList {

    private Node<E> firstNode;
    private Node<E> lastNode;
    private int size = 0;
    private OwnListIterator iterator;

    @Override
    public ListIterator<E> listIterator(int index) {
        return new OwnListIterator(index);
    }

    @Override
    public int size() {
        return size;
    }


    private class OwnListIterator implements ListIterator<E> {
        private int cursor; //index следующего элемента который вернет метод next()
        private Node<E> nextNode;
        private Node<E> prevNode;
        private Node<E> lastReturned;

        //инициализируем итератор таким образом чтобы следующим был выдан элемент с заданным индексом
        public OwnListIterator(int index) {
            //когда инициализируем с пустого листа
                //выставить на prev null, next null
                //проставить новый элемент как первую ноду, последний элемент как last


            //если с нулевой позиции инициализируем
                //выставляем nextNode = firstNode
                //prevNode как null
                //добавление нового идет уже в nextNode.prev


            //если инициализируем в конце листа
                //выставляем nextNode = null
                //prevNode будет на последней ноде
                //добавляем новую ноду в prevNode

            //если инициализируем в середине
                //двигаем с нуля до нужной позиции

            if(index > size)
                throw new IndexOutOfBoundsException("fuck yourself bitch");

            changePosition(index);
            cursor = index;
        }

        private void changePosition(int index) {
            if(index == 0) {
                nextNode = firstNode;
                prevNode = null;
                return;
            }

            if(size > 0 && index == size) {
                prevNode = lastNode;
                nextNode = null;
                return;
            }

            for(int i = 0; i <= index; i++) {
                if(i == 0) {
                    nextNode = firstNode;
                    prevNode = null;
                    continue;
                }

                nextNode = hasNext() ? nextNode.next : null;
                prevNode = nextNode.prev;
            }
        }

        /**
         * добавляет элемент после prevNode и перед nextNode на позицию курсора итератора
         *
         * The new element is inserted before the implicit
         *      * cursor: a subsequent call to {@code next} would be unaffected, and a
         *      * subsequent call to {@code previous} would return the new element.
         */
        @Override
        public void add(E element) {
            Node newNode = new Node(element);

            //когда с нуля создали пустой лист
            if(size == 0) {
                firstNode = newNode;
                lastNode = newNode;
                prevNode = null;
            }

            //когда курсор равен 0
            //добавление нового идет уже в nextNode.prev
            if(size > 0 && cursor == 0) {
                nextNode = firstNode;
                nextNode.prev = newNode;
                newNode.next = nextNode;
            }

            //когда добавляем в конец
            if(size > 0 && cursor == size) {
                prevNode.next = newNode;
                newNode.prev = prevNode;
                lastNode = newNode;
            }

            //когда добавляем в середину
            if(hasNext() && hasPrevious()) {
                nextNode.prev = newNode;
                prevNode.next = newNode;
                newNode.next = nextNode;
                newNode.prev = prevNode;
                prevNode = newNode;
            }

            size++;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if(hasNext()) {
                prevNode = nextNode;
                nextNode = nextNode.next;
                cursor++;
                lastReturned = prevNode;
                return prevNode.element;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return prevNode != null;
        }

        @Override
        public E previous() {
            if(hasPrevious()) {
                nextNode = prevNode;
                prevNode = prevNode.prev;
                cursor--;
                lastReturned = prevNode;
                return prevNode.element;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            if(hasPrevious())
                return cursor - 1;
            return 0;
        }

        @Override
        public void remove() {
            if(lastReturned == null)
                throw new IllegalStateException("No returned node found");

            //link leftovers
            Optional.ofNullable(lastReturned.prev).ifPresent(prev -> prev.next = lastReturned.next);
            Optional.ofNullable(lastReturned.next).ifPresent(next -> next.prev = lastReturned.prev);

            //change references to first and last node if exists
            if(firstNode == lastReturned) {
                firstNode = lastReturned.next;
            }
            if(lastNode == lastReturned) {
                lastNode = lastReturned.prev;
            }

            //remove references inside removed node
            lastReturned.prev = null;
            lastReturned.next = null;

            size--;

        }

        @Override
        public void set(E o) {
            if(lastReturned == null)
                throw new IllegalStateException("No returned node found");

            Node newNode = new Node(o);

            //change references to new node
            Optional.ofNullable(lastReturned.prev).ifPresent(prev -> prev.next = newNode);
            Optional.ofNullable(lastReturned.next).ifPresent(next -> next.prev = newNode);

            //change references to first and last node if exists
            if(firstNode == lastReturned) {
                firstNode = newNode;
            }
            if(lastNode == lastReturned) {
                lastNode = newNode;
            }

            //make lastReturned without elements
            lastReturned.prev = null;
            lastReturned.next = null;
        }


    }

    private class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        Node(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "element=" + element +
                    '}';
        }
    }
}

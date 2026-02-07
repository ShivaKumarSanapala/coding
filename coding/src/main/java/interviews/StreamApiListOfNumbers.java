package interviews;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApiListOfNumbers {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        // {10,15,8,49,25,98,32}
        list.add(10);
        list.add(15);
        list.add(20);
        list.add(110);
        list.add(-120);
        list.stream().filter( a -> {
            String currentValue = String.valueOf(a);
            // identify the first char is 1 or not keeping negative numbers in mind
            char first = currentValue.charAt(0);
            if(first == '-'){
                first = currentValue.charAt(1);
            }
            return first == '1';
        }).collect(Collectors.toCollection(ArrayList::new)).forEach(System.out::println);
    }
}

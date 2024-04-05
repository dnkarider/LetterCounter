import java.util.*;

public class Main {
    final static String LETTERS = "RLRFR";
    final static int LENGTH = 100;
    final static int AMOUNT_OF_ROUTES = 1000;
    public final static Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException, ConcurrentModificationException {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < AMOUNT_OF_ROUTES; i++) {
            threadList.add(getNewThread());
        }

        for(Thread thread : threadList){
            thread.start();
            thread.join();
        }
        Map.Entry<Integer, Integer> max = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество R на строку - " + max.getKey() + " и повторилось " + max.getValue() + " раз");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(mapPair -> System.out.println("- " + mapPair.getKey() + " (" + mapPair.getValue() + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
    public static Thread getNewThread(){
        return new Thread(() -> {
            String route = generateRoute(LETTERS, LENGTH);
            int frequency = (int) route.chars().filter(ch -> ch == 'R').count();
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(frequency)) {
                    sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                } else {
                    sizeToFreq.put(frequency, 1);
                }
            }
        });
    }
}

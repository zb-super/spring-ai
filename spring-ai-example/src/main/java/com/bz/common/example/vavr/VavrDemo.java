//package com.bz.common.example.vavr;
//
//
//import io.vavr.Tuple2;
//import io.vavr.collection.List;
//import io.vavr.collection.Queue;
//import io.vavr.collection.Stream;
//import io.vavr.control.Option;
//import io.vavr.control.Try;
//
//public class VavrDemo {
//
//    public static void main(String[] args) {
//
//        VavrDemo.testTry();
//    }
//
//    private void testList(){
//        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7).append(8);
//        for (Integer integer : integers) {
//
//        }
//
//    }
//
//    private static void testQueue(){
//        Queue<Integer> queue = Queue.of(1, 2, 3);
//        Tuple2<Integer, Queue<Integer>> dequeued = queue.dequeue();
//
//
//        Option<Tuple2<Integer, Queue<Integer>>> tuple2s = Queue.of(1).dequeueOption();
//
//        Option<Tuple2<Object, Queue<Object>>> tuple2s1 = Queue.empty().dequeueOption();
//        System.out.println(tuple2s1.isEmpty());
////        Tuple2<Object, Queue<Object>> dequeue = Queue.empty().dequeue();
//
//        Option<Integer> map = tuple2s.map(item -> item._1);
//        Option<Queue<Integer>> map1 = tuple2s.map(item -> item._2);
//
//        System.out.println(1);
//    }
//
//    private static void testStream(){
//        // = Stream("1", "2", "3") 在 Vavr 中
//        Stream.of(1, 2, 3).map(Object::toString).forEach(System.out::println);
//    }
//
//    private void testTuple(){
////        Tuple.
//
//
//
//    }
//
//    private static void testTry(){
//        Try.of(() -> 1/0)
//                .onFailure(ex -> System.out.println(ex.getMessage()))
//
//                .get();
//
//    }
//
//}

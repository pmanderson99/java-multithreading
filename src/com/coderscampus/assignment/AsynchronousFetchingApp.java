package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsynchronousFetchingApp {

	public static void main(String[] args) {

		Assignment8 assignment = new Assignment8();
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Integer> numberList = Collections.synchronizedList(new ArrayList<>(1000));
		List<CompletableFuture<Void>> tasks = new ArrayList<>(1000);

		for (int i = 0; i < 1000; i++) {
			CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> assignment.getNumbers(), executorService)
					.thenAccept(numbers -> numberList.addAll(numbers));
			tasks.add(task);
		}
		
		while (tasks.stream().filter(CompletableFuture::isDone).count() < 1000) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Map<Integer, Integer> numberCount = new HashMap<Integer, Integer>();
		for (Integer i : numberList) {
			Integer j = numberCount.get(i);
			numberCount.put(i, (j == null)? 1 : j + 1);
		}
		
		int maxNumber = Collections.max(numberCount.keySet());
		for(int i = 0; i <= maxNumber; i++) {
			int count = numberCount.getOrDefault(i, 0);
			System.out.println(i + " occurs " + count + " times.");
		}
	}

}

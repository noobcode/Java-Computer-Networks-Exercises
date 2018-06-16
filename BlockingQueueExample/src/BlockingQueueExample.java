import java.util.concurrent.*;

public class BlockingQueueExample {

	public static void main(String[] args) throws Exception {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		producer.start();
		consumer.start();
		Thread.sleep(4000);
	}

}

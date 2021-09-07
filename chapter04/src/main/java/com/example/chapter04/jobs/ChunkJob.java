package com.example.chapter04.jobs;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class ChunkJob {
//
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//
//	@Bean
//	public Job chunkBasedJob() {
//		return this.jobBuilderFactory.get("chunkBasedJob")
//				.start(chunkStep())
//				.build();
//	}
//
//	@Bean
//	public Step chunkStep() {
//		return this.stepBuilderFactory.get("chunkStep")
////				.<String, String>chunk(1000)
//				.<String, String>chunk(randomCompletionPolicy())
//				.reader(itemReader())
//				.writer(itemWriter())
//				.listener(new LoggingStepStartStopListener())
//				.build();
//	}
//
//	@Bean
//	public ListItemReader<String> itemReader() {
//		List<String> items = new ArrayList<>(100000);
//
//		for (int i = 0; i < 100000; i++) {
//			items.add(UUID.randomUUID().toString());
//		}
//
//		return new ListItemReader<>(items);
//	}
//
//	@Bean
//	public ItemWriter<String> itemWriter() {
//		return items -> {
//			for (String item : items) {
//				System.out.println(">> current item = " + item);
//			}
//		};
//	}
//
//	@Bean
//	public CompletionPolicy completionPolicy() {
//		CompositeCompletionPolicy policy =
//				new CompositeCompletionPolicy();
//
//		policy.setPolicies(
//				new CompletionPolicy[] {
//						new TimeoutTerminationPolicy(3),
//						new SimpleCompletionPolicy(1000)});
//
//		return policy;
//	}
//
//	@Bean
//	public CompletionPolicy randomCompletionPolicy() {
//		return new RandomChunkSizePolicy();
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(ChunkJob.class, args);
//	}
}

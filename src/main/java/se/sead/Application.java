package se.sead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Importer;
import se.sead.configuration.ApplicationArgumentManager;
import se.sead.repositories.impl.CreateAndReadRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CreateAndReadRepositoryImpl.class)
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static class LazyAutowireHelper implements ApplicationContextAware {

		private static final LazyAutowireHelper INSTANCE = new LazyAutowireHelper();

		private ApplicationContext applicationContext;

		private LazyAutowireHelper(){}

		public void autowire(Object itemToAutowire, Object... autowireableFields){
			if(anyFieldNotWired(Arrays.asList(autowireableFields))){
				applicationContext.getAutowireCapableBeanFactory().autowireBean(itemToAutowire);
			}
		}

		private boolean anyFieldNotWired(List autowireableFields){
			return autowireableFields.stream()
					.anyMatch( field -> field == null);
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

		@Bean
		public static LazyAutowireHelper getInstance(){
			return INSTANCE;
		}
	}

	@Component
	public static class DefaultRunner implements ApplicationRunner {

        @Autowired
        private ImportRunner runner;
        private ApplicationArgumentManager argumentManager;
        @Autowired
		private List<Importer> importers;

        @Override
        public void run(ApplicationArguments applicationArguments) throws Exception {
        	argumentManager = new ApplicationArgumentManager(applicationArguments);
            logger.info("using database: {}", applicationArguments.getOptionValues("file"));
            if(argumentManager.shouldRun()){
                runner.run(getImporters());
            }
        }

		private List<Importer> getImporters(){
			return argumentManager.filter(importers);
		}
    }
}

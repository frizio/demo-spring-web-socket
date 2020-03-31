package cloud.frizio.dev.demospringwebsocket.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import cloud.frizio.dev.demospringwebsocket.tablou.TablouService;

/**
 * SchedulerConfig
 */
@Configuration
@EnableScheduling
public class SchedulerConfig  implements SchedulingConfigurer {
  
  // To avoide exception
  // Bean named 'defaultSockJsTaskScheduler' is expected to be of type 'org.springframework.scheduling.TaskScheduler' 
  // but was actually of type 'org.springframework.beans.factory.support.NullBean'
  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler( taskExecutor() );
  }
  
  @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(3);
    }

  @Bean
  public TablouService tablouService() {
    return new TablouService();
  }
}
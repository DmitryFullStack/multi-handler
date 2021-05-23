package kirilin.dev.sparkstarter.unsafe;


import kirilin.dev.multihandler.SparkRepository;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.beans.Introspector;
import java.lang.reflect.Proxy;
import java.util.Set;

public class SparkMultiHandlerInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        AnnotationConfigApplicationContext tempContext = new AnnotationConfigApplicationContext(InternalConfiguration.class);
        SparkInvocationHandlerFactory factory = tempContext.getBean(SparkInvocationHandlerFactory.class);

        addSparkExtractorResolverToContext(context, tempContext);

        factory.setRealContext(context);
        tempContext.close();

        registerSparkBeans(context);
        Reflections scanner = new Reflections(context.getEnvironment().getProperty("spark.packages2-scan"));
        Set<Class<? extends SparkRepository>> sparkRepositories = scanner.getSubTypesOf(SparkRepository.class);
        for (Class<? extends SparkRepository> sparkRepository : sparkRepositories) {
            Object sparkRepositoryProxy = Proxy.newProxyInstance(sparkRepository.getClassLoader(),
                    new Class[]{sparkRepository},
                    factory.create(sparkRepository));

            context.getBeanFactory().registerSingleton(Introspector.decapitalize(sparkRepository.getSimpleName()), sparkRepositoryProxy);
        }
    }

    private void addSparkExtractorResolverToContext(ConfigurableApplicationContext context, AnnotationConfigApplicationContext tempContext) {
        DataExtractorResolver extractorResolver = tempContext.getBean(DataExtractorResolver.class);
        context.getBeanFactory().registerSingleton("extractorResolverForSpark", extractorResolver);
    }

    private void registerSparkBeans(ConfigurableApplicationContext context) {
        String appName = context.getEnvironment().getProperty("spark.app-name");
        SparkSession sparkSession = SparkSession.builder()
                .master("local[*]").appName(appName)
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        context.getBeanFactory().registerSingleton("sparkSession", sparkSession);
        context.getBeanFactory().registerSingleton("sparkContext", sparkContext);
    }
}

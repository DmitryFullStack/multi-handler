package kirilin.dev.sparkstarter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@Aspect
public class LazyListSupportingAspect {

    @Autowired
    private FirstLevelCacheService cacheService;

    @Autowired
    private ConfigurableApplicationContext context;

    @Before("execution(* kirilin.dev.sparkstarter.LazySparkList.*(..)) && execution(* java.util.*.*(..))")
    public void beforeEachMethodInvocationCheckAndFillContent(JoinPoint jp){
        LazySparkList lazyList = (LazySparkList) jp.getTarget();
        if(!lazyList.initialized()){
            List list = cacheService.getDataFor(lazyList.getOwnerId(), lazyList.getForeignKeyName(), lazyList.getModelClass(), lazyList.getPath2Source(), context);
            lazyList.setContent(list);
        }
    }

}

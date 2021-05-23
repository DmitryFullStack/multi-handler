package kirilin.dev.sparkstarter.unsafe;

import kirilin.dev.multihandler.ForeignKeyName;
import kirilin.dev.multihandler.Source;
import kirilin.dev.sparkstarter.LazySparkList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class LazyInitializationFinalizerPostProcessor implements FinalizerPostProcessor {

    private final ConfigurableApplicationContext context;

    @SneakyThrows
    @Override
    public Object postFinalize(Object retVal) {
        if(Collection.class.isAssignableFrom(retVal.getClass())){
            List list = (List) retVal;
            for (Object model : list) {

                Field idField = model.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                String ownerId = idField.get(model).toString();
                Field[] declaredFields = model.getClass().getDeclaredFields();

                for (Field field : declaredFields) {
                    if(List.class.isAssignableFrom(field.getType())){
                        LazySparkList sparkList = context.getBean(LazySparkList.class);
                        sparkList.setOwnerId(ownerId);
                        String foreignKeyName = field.getAnnotation(ForeignKeyName.class).value();
                        sparkList.setForeignKeyName(foreignKeyName);
                        Class<?> embeddedModel = getEmbeddedModel(field);
                        sparkList.setModelClass(embeddedModel);
                        String path2Data = embeddedModel.getAnnotation(Source.class).value();
                        sparkList.setPath2Source(path2Data);

                        field.setAccessible(true);
                        field.set(model, sparkList);
                    }
                }
            }
        }
        return retVal;
    }

    private Class<?> getEmbeddedModel(Field field) {
        ParameterizedType genericType = (ParameterizedType)field.getGenericType();
        return (Class<?>)genericType.getActualTypeArguments()[0];
    }
}

package com.cq.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 程崎应用程序上下文
 *
 * @author 程崎
 * @version 1.0.0
 * @since 2022/06/15
 */
public class CqApplicationContext {
    private Class<?> configClass;

    private final ConcurrentMap<String, CqBeanDefinition> beanDefinitionConcurrentMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final List<CqBeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * 单例
     */
    private static final String SINGLETON = "singleton";
    /**
     * 原型（多例）
     */
    private static final String PROTOTYPE = "prototype";

    public CqApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        //扫描Component
        if (configClass.isAnnotationPresent(CqComponentScan.class)) {
            CqComponentScan cqComponentScanAnnotation = configClass.getAnnotation(CqComponentScan.class);

            //扫描的路径(path不是真实的路径)
            String path = cqComponentScanAnnotation.value();
            path = path.replace(".", "/");

            //region 通过相对路径path得到真实路径
            ClassLoader classLoader = CqApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            //endregion

            File file = new File(Objects.requireNonNull(resource).getFile());

            //是否为目录
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                assert files != null;

                for (File singleFile : files) {
                    String fileName = singleFile.getAbsolutePath();

                    //是否为Java类
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf(path), fileName.indexOf(".class")).replace("/", ".");

                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            //判断是否为bean
                            if (clazz.isAnnotationPresent(CqComponent.class)) {

                                //判断是否为当前这个类是否实现了CqBeanPostprocessor接口
                                if (CqBeanPostProcessor.class.isAssignableFrom(clazz)) {
                                    CqBeanPostProcessor instance = (CqBeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                    beanPostProcessorList.add(instance);
                                }

                                String beanName = clazz.getAnnotation(CqComponent.class).value();

                                //如果未编写注解里的value值，则默认为类名的小写
                                if ("".equals(beanName)) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }

                                CqBeanDefinition beanDefinition = new CqBeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(CqScope.class)) {
                                    CqScope scopeAnnotation = clazz.getAnnotation(CqScope.class);
                                    beanDefinition.setScope(scopeAnnotation.value());
                                } else {
                                    beanDefinition.setScope(SINGLETON);
                                }
                                beanDefinitionConcurrentMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                                 InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        for (String beanName : beanDefinitionConcurrentMap.keySet()) {
            CqBeanDefinition beanDefinition = beanDefinitionConcurrentMap.get(beanName);
            if (SINGLETON.equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object createBean(String beanName, CqBeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getType();

        try {
            Object instance = clazz.getConstructor().newInstance();

            //依赖注入
            for (Field field : clazz.getDeclaredFields()) {

                if (field.isAnnotationPresent(CqAutowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            //告诉实现CqBeanNameAware接口的类，你的bean名称
            if (instance instanceof CqBeanNameAware) {
                ((CqBeanNameAware) instance).setBeanName(beanName);
            }

            //初始化前执行的方法
            for (CqBeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }

            //初始化机制，直接调用该实例的方法，框架本身不需要关心此方法逻辑
            if (instance instanceof CqInitializingBean) {
                ((CqInitializingBean) instance).afterPropertiesSet();
            }

            //初始化后执行的方法
            for (CqBeanPostProcessor beanPostProcessor : beanPostProcessorList) {
               instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            //初始化后 AOP

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        CqBeanDefinition beanDefinition = beanDefinitionConcurrentMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException("该bean名称未注册入容器中");
        }
        String scope = beanDefinition.getScope();
        if (SINGLETON.equals(scope)) {
            Object bean = singletonObjects.get(beanName);
            if (bean == null) {
                Object newBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, newBean);
                return newBean;
            }
            return bean;
        }
        return createBean(beanName, beanDefinition);
    }
}

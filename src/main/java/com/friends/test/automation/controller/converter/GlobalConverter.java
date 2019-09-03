package com.friends.test.automation.controller.converter;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class GlobalConverter {

    Map<Integer, Object> visitedList = new HashMap<>();

    public void clearVisitedList() {
        visitedList.clear();
    }

    public <T, E> T convert(E sourcePojo,
            Class<T> clazz) {

        if (sourcePojo == null || visitedList.get(sourcePojo.hashCode()) != null) {
            return null;
        }

        visitedList.put(sourcePojo.hashCode(), new Object());

        try {
            Field[] fields = sourcePojo.getClass().getDeclaredFields();
            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            T targetPojo = (T) constructor.newInstance();
            constructor.setAccessible(false);

            Field[] targetFields = targetPojo.getClass().getDeclaredFields();
            for (Field source : fields) {
                for (Field target : targetFields) {
                    if (source.getName().equals(target.getName())) {

                        if (String.class.equals(source.getType()) || BeanUtils
                                .isSimpleValueType(source.getType())) {
                            source.setAccessible(true);
                            target.setAccessible(true);

                            target.set(targetPojo,
                                    sourcePojo.getClass().getDeclaredMethod(getterOfField(source.getName()))
                                            .invoke(sourcePojo));
                            source.setAccessible(false);
                            target.setAccessible(false);
                        } else if (isCollection(source.getType())) {
                            source.setAccessible(true);
                            target.setAccessible(true);
                            setCollectionInstance(targetPojo, target);
                            for (Object sourceP : (Collection) sourcePojo.getClass()
                                    .getDeclaredMethod(getterOfField(source.getName()))
                                    .invoke(sourcePojo)) {
                                ParameterizedType modelParameterizedType = (ParameterizedType) target.getGenericType();
                                Class<?> genericType = (Class<?>) modelParameterizedType.getActualTypeArguments()[0];
                                Object obj = convert(sourceP, genericType);
                                Collection col = (Collection) target.get(targetPojo);
                                col.add(obj);
                            }
                            target.set(targetPojo, target.get(targetPojo));
                            source.setAccessible(false);
                            target.setAccessible(false);
                        } else {
                            source.setAccessible(true);
                            Object obj = convert(
                                    sourcePojo.getClass().getDeclaredMethod(getterOfField(source.getName()))
                                            .invoke(sourcePojo), target.getType());
                            source.setAccessible(false);
                            target.setAccessible(true);
                            target.set(targetPojo, obj);
                            target.setAccessible(false);
                        }

                    }
                }

            }

            return targetPojo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getterOfField(String name) {
        String upperChar = name.substring(0, 1).toUpperCase();
        String restChars = name.substring(1, name.length());
        return "get" + upperChar + restChars;
    }

    private <T> void setCollectionInstance(T targetPojo, Field target) throws IllegalAccessException {
        if (target.getType().getSimpleName().equals("List")) {
            target.set(targetPojo, new ArrayList<>());
        } else if (target.getType().getSimpleName().equals("Map")) {
            target.set(targetPojo, new HashMap<>());
        } else if (target.getType().getSimpleName().equals("Set")) {
            target.set(targetPojo, new HashSet<>());
        }
    }

    private <E> boolean isCollection(E sourcePojo) {
        String simpleName = ((Class) sourcePojo).getSimpleName();
        return simpleName.equals("List") || simpleName.equals("Map") || simpleName.equals("Set");
    }
}
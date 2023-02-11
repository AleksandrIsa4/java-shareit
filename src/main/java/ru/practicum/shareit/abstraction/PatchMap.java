package ru.practicum.shareit.abstraction;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;

@Component
public class PatchMap <T> {

    public T patchObject(T oBefore, T oAfter) {
        try {
            for (Field field : oBefore.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(oBefore)!=null){
                    field.set(oAfter, field.get(oBefore));
                }
            }
            return oAfter;
        } catch (IllegalAccessException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "нет доступа к объекту");
        }
    }
}

package com.example.demo.db.model;

import com.example.demo.util.HibernateUtil;

import java.io.Serializable;

public interface Persistable extends Serializable {

    Long getId();

    default boolean persistableEquals(Persistable entitySelf, Object entity2) {
        /* Принцип сравнения такой:
         * Если объект не сохранен (не имеет id), то сравнивать его можно только по ссылке - если вдруг существует
         *  другой объект с такими же полями, то это все равно две разных сущности.
         * Если же объект был сохранен (имеет id), то даже при сравнении
         *  двух разных версий этого объекта нужно определять, что это одна и та же сущность
         *  (и ее актуальная версия лежит в бд, независимо от состояния объектов в памяти).
         *  При этом для сравнения достаточно знать таблицу и id - это гарантирует уникальность.
         */
        if (entitySelf == entity2) {
            // это один и тот же объект
            return true;
        }
        if (entity2 == null) {
            return false;
        }
        if (!(entity2 instanceof Persistable)) {
            return false;
        }
        var entity2p = (Persistable) entity2;

        if (entitySelf.getId() == null || entity2p.getId() == null) {
            // хотя бы один объект не сохранен в бд и это не один и тот же объект
            return false;
        }

        var e1 = HibernateUtil.initializeAndUnproxy(entitySelf);
        var e2 = HibernateUtil.initializeAndUnproxy(entity2p);

        // здесь можно было бы сравнивать имя таблицы, но имя класса достать проще
        if (!e1.getClass().getName().equals(e2.getClass().getName())) {
            // разные типы
            return false;
        }

        // если типы одинаковые, то осталось сравнить их id
        return (e1.getId().equals(e2.getId()));
    }

    default int persistableHashCode(Persistable entity) {
        // у сохраненного и несохраненного объекта будут разные хешкоды
        if (entity != null && entity.getId() != null) {
            return entity.getId().intValue();
        } else {
            return System.identityHashCode(entity);
        }
    }

}

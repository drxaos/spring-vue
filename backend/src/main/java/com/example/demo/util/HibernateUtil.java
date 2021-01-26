package com.example.demo.util;

import com.example.demo.db.model.Persistable;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {

    public static <T extends Persistable> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            return null;
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            var hibernateProxy = (HibernateProxy) entity;
            Object unroxiedEntity = hibernateProxy.getHibernateLazyInitializer().getImplementation();
            if (unroxiedEntity instanceof Persistable) {
                return (T) unroxiedEntity;
            } else {
                throw new RuntimeException("unreachable");
            }
        } else {
            return entity;
        }
    }

}

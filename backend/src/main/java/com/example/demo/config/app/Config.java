package com.example.demo.config.app;

import com.example.demo.util.DateUtils;
import lombok.SneakyThrows;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class Config {

    @Autowired
    Environment environment;

    Properties props = new Properties();

    @PostConstruct
    protected void init() {
        MutablePropertySources propSrcs = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, environment.getProperty(propName)));

    }

    public <T> T getValue(Enum<?> key, Class<T> type) {
        String realKey = getKey(key);
        return to(type, props.getProperty(realKey));
    }

    public String getString(Enum<?> key) {
        return getValue(key, String.class);
    }

    public Long getLong(Enum<?> key) {
        return getValue(key, Long.class);
    }

    public Integer getInteger(Enum<?> key) {
        return getValue(key, Integer.class);
    }

    public Boolean getBoolean(Enum<?> key) {
        return getValue(key, Boolean.class);
    }

    public Date getDate(Enum<?> key) {
        return getValue(key, Date.class);
    }

    public Map<String, String> getMap(Enum<?> key) {
        return getMap(key, String.class);
    }

    public <T> Map<String, T> getMap(Enum<?> key, Class<T> type) {
        String realKey = getKey(key);
        Map<String, T> result = props.stringPropertyNames().stream()
                .filter(name -> name.startsWith(realKey + "."))
                .collect(Collectors.toMap(
                        name -> name.replace(realKey + ".", ""),
                        name -> to(type, props.getProperty(name))));
        return result;
    }

    public List<String> getList(Enum<?> key) {
        return getList(key, String.class);
    }

    public <T> List<T> getList(Enum<?> key, Class<T> type) {
        String realKey = getKey(key);
        List result = props.stringPropertyNames().stream()
                .filter(name -> name.startsWith(realKey + "["))
                .map(name -> name.replace(realKey + "[", "").replace("]", ""))
                .map(Long::parseLong)
                .sorted()
                .map(n -> to(type, props.getProperty(realKey + "[" + n + "]")))
                .collect(Collectors.toList());
        return result;
    }

    public <T> T to(Class<? extends T> cls, Object value) {
        if (cls.isInstance(value)) {
            return (T) value; // no conversion needed
        }

        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) {
            return (T) PropertyConverter.toBoolean(value);
        } else if (Character.class.equals(cls) || Character.TYPE.equals(cls)) {
            return (T) PropertyConverter.toCharacter(value);
        } else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()) {
            if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                return (T) PropertyConverter.toInteger(value);
            } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                return (T) PropertyConverter.toLong(value);
            } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                return (T) PropertyConverter.toByte(value);
            } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                return (T) PropertyConverter.toShort(value);
            } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                return (T) PropertyConverter.toFloat(value);
            } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                return (T) PropertyConverter.toDouble(value);
            } else if (BigInteger.class.equals(cls)) {
                return (T) PropertyConverter.toBigInteger(value);
            } else if (BigDecimal.class.equals(cls)) {
                return (T) PropertyConverter.toBigDecimal(value);
            }
        } else if (Date.class.equals(cls)) {
            return (T) DateUtils.parseDate("" + value);
        } else if (Duration.class.equals(cls)) {
            return (T) Duration.parse("" + value);
        } else if (cls.isEnum()) {
            return (T) Enum.valueOf((Class<? extends Enum>) cls, "" + value);
        }

        throw new ConversionException("The value '" + value + "' (" + value.getClass() + ")"
                + " can't be converted to a " + cls.getName() + " object");
    }

    private String getKey(Enum<?> key) {
        String prefix = this.getClass().getCanonicalName() + ".";
        String fullPath = key.getClass().getCanonicalName();

        if (!fullPath.startsWith(prefix)) {
            throw new IllegalArgumentException("key should be declared inside " + this.getClass().getCanonicalName() + " class");
        }

        String path = fullPath.replace(prefix, "");
        String realKey = path.replace("$", "") + "." + key.name();
        return realKey;
    }

    @SneakyThrows
    public String exportConfig() {
        try (var sw = new StringWriter()) {
            props.store(sw, "");
            return sw.toString();
        }
    }

    @SneakyThrows
    public void importConfig(String data) {
        props.load(new StringReader(data));
    }

    public void set(Enum<?> key, Object value) {
        String realKey = getKey(key);
        props.setProperty(realKey, to(String.class, value));
    }
}

/**********************************************************\
|                                                          |
|                          hprose                          |
|                                                          |
| Official WebSite: http://www.hprose.com/                 |
|                   http://www.hprose.org/                 |
|                                                          |
\**********************************************************/
/**********************************************************\
 *                                                        *
 * HashSetUnserializer.java                               *
 *                                                        *
 * HashSet unserializer class for Java.                   *
 *                                                        *
 * LastModified: Jul 30, 2015                             *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagList;
import static hprose.io.HproseTags.TagNull;
import static hprose.io.HproseTags.TagOpenbrace;
import static hprose.io.HproseTags.TagRef;
import hprose.util.ClassUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashSet;

final class HashSetUnserializer implements HproseUnserializer {

    public final static HashSetUnserializer instance = new HashSetUnserializer();

    @SuppressWarnings({"unchecked"})
    private static <T> HashSet<T> readHashSet(HproseReader reader, ByteBuffer buffer, Class<?> cls, Class<T> componentClass, Type componentType) throws IOException {
        int tag = buffer.get();
        switch (tag) {
            case TagNull: return null;
            case TagList: {
                int count = ValueReader.readInt(buffer, TagOpenbrace);
                HashSet<T> a = new HashSet<T>(count);
                reader.refer.set(a);
                HproseUnserializer unserializer = UnserializerFactory.get(componentClass);
                for (int i = 0; i < count; ++i) {
                    a.add((T)unserializer.read(reader, buffer, componentClass, componentType));
                }
                buffer.get();
                return a;
            }
            case TagRef: return (HashSet<T>)reader.readRef(buffer);
            default: throw ValueReader.castError(reader.tagToString(tag), cls);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T> HashSet<T> readHashSet(HproseReader reader, InputStream stream, Class<?> cls, Class<T> componentClass, Type componentType) throws IOException {
        int tag = stream.read();
        switch (tag) {
            case TagNull: return null;
            case TagList: {
                int count = ValueReader.readInt(stream, TagOpenbrace);
                HashSet<T> a = new HashSet<T>(count);
                reader.refer.set(a);
                HproseUnserializer unserializer = UnserializerFactory.get(componentClass);
                for (int i = 0; i < count; ++i) {
                    a.add((T)unserializer.read(reader, stream, componentClass, componentType));
                }
                stream.read();
                return a;
            }
            case TagRef: return (HashSet<T>)reader.readRef(stream);
            default: throw ValueReader.castError(reader.tagToString(tag), cls);
        }
    }

    public final Object read(HproseReader reader, ByteBuffer buffer, Class<?> cls, Type type) throws IOException {
        Type componentType;
        Class<?> componentClass;
        if (type instanceof ParameterizedType) {
            componentType = ((ParameterizedType)type).getActualTypeArguments()[0];
            componentClass = ClassUtil.toClass(componentType);
        }
        else {
            componentType = Object.class;
            componentClass = Object.class;
        }
        return readHashSet(reader, buffer, cls, componentClass, componentType);
    }

    public final Object read(HproseReader reader, InputStream stream, Class<?> cls, Type type) throws IOException {
        Type componentType;
        Class<?> componentClass;
        if (type instanceof ParameterizedType) {
            componentType = ((ParameterizedType)type).getActualTypeArguments()[0];
            componentClass = ClassUtil.toClass(componentType);
        }
        else {
            componentType = Object.class;
            componentClass = Object.class;
        }
        return readHashSet(reader, stream, cls, componentClass, componentType);
    }

}

/*
 * Copyright 2011 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.list.fixed;

import java.util.Collections;
import java.util.Iterator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.SynchronizedMutableList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SingletonList}.
 */
public class SingletonListTest extends AbstractMemoryEfficientMutableListTestCase
{
    @Override
    protected int getSize()
    {
        return 1;
    }

    @Override
    protected Class<?> getListType()
    {
        return SingletonList.class;
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.list, FastList.newList(this.list));
        Verify.assertPostSerializedEqualsAndHashCode(this.list);
    }

    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, this.list.asSynchronized());
    }

    @Test
    public void testClone()
    {
        MutableList<String> clone = this.list.clone();
        Verify.assertEqualsAndHashCode(this.list, clone);
        Verify.assertInstanceOf(SingletonList.class, clone);
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(this.list.contains("1"));
        Assert.assertFalse(this.list.contains("2"));
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.remove(0);
            }
        });
    }

    @Test
    public void addAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.add(0, "1");
            }
        });
    }

    @Test
    public void add()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.add("1");
            }
        });
    }

    @Test
    public void addingAllToOtherList()
    {
        MutableList<String> newList = FastList.newList(this.list);
        newList.add("2");
        Verify.assertItemAtIndex("1", 0, newList);
        Verify.assertItemAtIndex("2", 1, newList);
    }

    @Test
    public void get()
    {
        Verify.assertItemAtIndex("1", 0, this.list);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.get(1);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.get(-1);
            }
        });
    }

    @Test
    public void forEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = newWith(1);
        collection.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith(1), result);
    }

    private static <T> MutableList<T> newWith(T item)
    {
        return Lists.fixedSize.of(item);
    }

    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = newWith(1);
        collection.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Assert.assertEquals(FastList.newListWith(1), result);
    }

    @Test
    public void forEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = newWith(1);
        collection.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        Verify.assertContainsAll(result, 1);
    }

    @Test
    public void set()
    {
        Assert.assertEquals("1", this.list.set(0, "2"));
        Assert.assertEquals(FastList.newListWith("2"), this.list);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                SingletonListTest.this.list.set(1, "2");
            }
        });
    }

    @Test
    public void select()
    {
        Verify.assertContainsAll(newWith(1).select(Predicates.lessThan(3)), 1);
        Verify.assertEmpty(newWith(1).select(Predicates.greaterThan(3)));
    }

    @Test
    public void selectWith()
    {
        Verify.assertContainsAll(newWith(1).selectWith(Predicates2.<Integer>lessThan(), 3), 1);
        Verify.assertEmpty(newWith(1).selectWith(Predicates2.<Integer>greaterThan(), 3));
    }

    @Test
    public void reject()
    {
        Verify.assertEmpty(newWith(1).reject(Predicates.lessThan(3)));
        Verify.assertContainsAll(newWith(1).reject(Predicates.greaterThan(3), UnifiedSet.<Integer>newSet()), 1);
    }

    @Test
    public void rejectWith()
    {
        Verify.assertEmpty(newWith(1).rejectWith(Predicates2.<Integer>lessThan(), 3));
        Verify.assertContainsAll(newWith(1).rejectWith(Predicates2.<Integer>greaterThan(),
                3,
                UnifiedSet.<Integer>newSet()),
                1);
    }

    @Test
    public void collect()
    {
        Verify.assertContainsAll(newWith(1).collect(Functions.getToString()), "1");
        Verify.assertContainsAll(newWith(1).collect(Functions.getToString(),
                UnifiedSet.<String>newSet()),
                "1");
    }

    @Test
    public void flatCollect()
    {
        Function<Integer, MutableSet<String>> function =
                new Function<Integer, MutableSet<String>>()
                {
                    public MutableSet<String> valueOf(Integer object)
                    {
                        return UnifiedSet.newSetWith(object.toString());
                    }
                };
        Verify.assertListsEqual(FastList.newListWith("1"), newWith(1).flatCollect(function));
        Verify.assertSetsEqual(
                UnifiedSet.newSetWith("1"),
                newWith(1).flatCollect(function, UnifiedSet.<String>newSet()));
    }

    @Test
    public void detect()
    {
        Assert.assertEquals(Integer.valueOf(1), newWith(1).detect(Predicates.equal(1)));
        Assert.assertNull(newWith(1).detect(Predicates.equal(6)));
    }

    @Test
    public void detectWith()
    {
        Assert.assertEquals(Integer.valueOf(1), newWith(1).detectWith(Predicates2.equal(), 1));
        Assert.assertNull(newWith(1).detectWith(Predicates2.equal(), 6));
    }

    @Test
    public void detectIfNoneWithBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(1), newWith(1).detectIfNone(Predicates.equal(1), function));
        Assert.assertEquals(Integer.valueOf(6), newWith(1).detectIfNone(Predicates.equal(6), function));
    }

    @Test
    public void detectWithIfNone()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(1), newWith(1).detectWithIfNone(Predicates2.equal(), 1, function));
        Assert.assertEquals(Integer.valueOf(6), newWith(1).detectWithIfNone(Predicates2.equal(), 6, function));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(newWith(1).allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(newWith(1).allSatisfy(Predicates.equal(2)));
    }

    @Test
    public void allSatisfyWith()
    {
        Assert.assertTrue(newWith(1).allSatisfyWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(newWith(1).allSatisfyWith(Predicates2.equal(), 2));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertFalse(newWith(1).anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(newWith(1).anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void anySatisfyWith()
    {
        Assert.assertFalse(newWith(1).anySatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertTrue(newWith(1).anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(newWith(1).noneSatisfy(Predicates.instanceOf(String.class)));
        Assert.assertFalse(newWith(1).noneSatisfy(Predicates.equal(1)));
    }

    @Test
    public void noneSatisfyWith()
    {
        Assert.assertTrue(newWith(1).noneSatisfyWith(Predicates2.instanceOf(), String.class));
        Assert.assertFalse(newWith(1).noneSatisfyWith(Predicates2.equal(), 1));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(1, newWith(1).count(Predicates.instanceOf(Integer.class)));
        Assert.assertEquals(0, newWith(1).count(Predicates.instanceOf(String.class)));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(1, newWith(1).countWith(Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, newWith(1).countWith(Predicates2.instanceOf(), String.class));
    }

    @Test
    public void collectIf()
    {
        Verify.assertContainsAll(newWith(1).collectIf(Predicates.instanceOf(Integer.class),
                Functions.getToString()), "1");
        Verify.assertContainsAll(newWith(1).collectIf(Predicates.instanceOf(Integer.class),
                Functions.getToString(),
                FastList.<String>newList()), "1");
    }

    @Test
    public void collectWith()
    {
        Assert.assertEquals(
                FastList.newListWith(2),
                newWith(1).collectWith(AddFunction.INTEGER, 1));
        Assert.assertEquals(
                FastList.newListWith(2),
                newWith(1).collectWith(AddFunction.INTEGER, 1, FastList.<Integer>newList()));
    }

    @Test
    public void getFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), newWith(1).getFirst());
    }

    @Test
    public void getLast()
    {
        Assert.assertEquals(Integer.valueOf(1), newWith(1).getLast());
    }

    @Test
    public void isEmpty()
    {
        Verify.assertNotEmpty(newWith(1));
        Assert.assertTrue(newWith(1).notEmpty());
    }

    @Test
    public void removeAll()
    {
        final MutableList<Integer> objects = newWith(1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                objects.removeAll(Lists.fixedSize.of(1, 2));
            }
        });
    }

    @Test
    public void retainAll()
    {
        final MutableList<Integer> objects = newWith(1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                objects.retainAll(newWith(2));
            }
        });
    }

    @Test
    public void clear()
    {
        final MutableList<Integer> objects = newWith(1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                objects.clear();
            }
        });
    }

    @Test
    public void iterator()
    {
        MutableList<Integer> objects = newWith(1);
        Iterator<Integer> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            Assert.assertEquals(1, integer.intValue() + i);
        }
    }

    @Test
    public void injectInto()
    {
        MutableList<Integer> objects = newWith(1);
        Integer result = objects.injectInto(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void injectIntoWith()
    {
        MutableList<Integer> objects = newWith(1);
        Integer result =
                objects.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
                {
                    public Integer value(Integer injectedValued, Integer item, Integer parameter)
                    {
                        return injectedValued + item + parameter;
                    }
                }, 0);
        Assert.assertEquals(Integer.valueOf(2), result);
    }

    @Test
    public void toArray()
    {
        MutableList<Integer> objects = newWith(1);
        Object[] array = objects.toArray();
        Verify.assertSize(1, array);
        Integer[] array2 = objects.toArray(new Integer[1]);
        Verify.assertSize(1, array2);
    }

    @Test
    public void selectAndRejectWith()
    {
        MutableList<Integer> objects = newWith(1);
        Twin<MutableList<Integer>> result = objects.selectAndRejectWith(Predicates2.equal(), 1);
        Verify.assertSize(1, result.getOne());
        Verify.assertEmpty(result.getTwo());
    }

    @Test
    public void removeWithPredicate()
    {
        final MutableList<Integer> objects = newWith(1);
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                objects.removeIf(Predicates.isNull());
            }
        });
    }

    @Test
    public void toList()
    {
        MutableList<Integer> list = newWith(1).toList();
        list.add(2);
        list.add(3);
        list.add(4);
        Verify.assertContainsAll(list, 1, 2, 3, 4);
    }

    @Test
    public void toSortedList()
    {
        MutableList<Integer> integers = newWith(1);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Verify.assertStartsWith(list, 1);
        Assert.assertNotSame(integers, list);
        MutableList<Integer> list2 = integers.toSortedList();
        Verify.assertStartsWith(list2, 1);
        Assert.assertNotSame(integers, list2);
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<Integer> integers = newWith(1);
        MutableList<Integer> list = integers.toSortedListBy(Functions.getIntegerPassThru());
        Assert.assertEquals(FastList.newListWith(1), list);
        Assert.assertNotSame(integers, list);
    }

    @Test
    public void toSet()
    {
        MutableList<Integer> integers = newWith(1);
        MutableSet<Integer> set = integers.toSet();
        Verify.assertContainsAll(set, 1);
    }

    @Test
    public void toMap()
    {
        MutableList<Integer> integers = newWith(1);
        MutableMap<Integer, Integer> map =
                integers.toMap(Functions.getIntegerPassThru(), Functions.getIntegerPassThru());
        Verify.assertContainsAll(map.keySet(), 1);
        Verify.assertContainsAll(map.values(), 1);
    }

    @Test
    public void forLoop()
    {
        MutableList<String> list = newWith("one");
        MutableList<String> upperList = newWith("ONE");
        for (String each : list)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
    }

    @Test
    public void subList()
    {
        MutableList<String> list = newWith("one");
        MutableList<String> subList = list.subList(0, 1);
        MutableList<String> upperList = newWith("ONE");
        for (String each : subList)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
        Assert.assertEquals("one", subList.getFirst());
        Assert.assertEquals("one", subList.getLast());
    }

    @Test
    public void testToString()
    {
        MutableList<MutableList<?>> list = Lists.fixedSize.<MutableList<?>>of(Lists.fixedSize.of());
        list.set(0, list);
        Assert.assertEquals("[(this SingletonList)]", list.toString());
    }

    private MutableList<Integer> newList()
    {
        return Lists.fixedSize.of(1);
    }

    private MutableList<Integer> classUnderTestWithNull()
    {
        return Lists.fixedSize.of((Integer) null);
    }

    @Test
    public void min_null_throws()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        this.classUnderTestWithNull().min(Comparators.naturalOrder());
    }

    @Test
    public void max_null_throws()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        this.classUnderTestWithNull().max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().max(Comparators.reverse(Comparators.naturalOrder())));
    }

    @Test
    public void min_null_throws_without_comparator()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        this.classUnderTestWithNull().min();
    }

    @Test
    public void max_null_throws_without_comparator()
    {
        // Collections with one element should not throw to emulate the JDK Collections behavior
        this.classUnderTestWithNull().max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(this.newList().size()), this.newList().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newList().maxBy(Functions.getToString()));
    }

    @Test
    public void without()
    {
        MutableList<Integer> list = new SingletonList<Integer>(2);
        Assert.assertSame(list, list.without(9));
        list = list.without(2);
        Verify.assertListsEqual(Lists.mutable.of(), list);
        Verify.assertInstanceOf(EmptyList.class, list);
    }
}

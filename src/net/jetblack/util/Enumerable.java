package net.jetblack.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import net.jetblack.util.comparers.EqualityComparer;
import net.jetblack.util.invokables.BinaryFunction;
import net.jetblack.util.invokables.UnaryAction;
import net.jetblack.util.invokables.UnaryFunction;
import net.jetblack.util.selectors.IdentitySelector;
import net.jetblack.util.types.TypeLiteral;

public abstract class Enumerable<T> implements Iterator<T>, Iterable<T> {

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		return this;
	}

	public static <T> Enumerable<T> create(final T[] array) {
		return new Enumerable<T>() {

			int i = 0;

			@Override
			public boolean hasNext() {
				return i < array.length;
			}

			@Override
			public T next() {
				return array[i++];
			}

		};
	}

	public static <T> Enumerable<T> createReverse(final T[] array) {
		return new Enumerable<T>() {

			int i = array.length;

			@Override
			public boolean hasNext() {
				return i > 0;
			}

			@Override
			public T next() {
				return array[--i];
			}

		};
	}

	public static <T> Enumerable<T> create(final Iterator<T> iterator) {
		return new Enumerable<T>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public T next() {
				return iterator.next();
			}

		};
	}

	public static <T> Enumerable<T> createReverse(final ListIterator<T> iterator) {
		return new Enumerable<T>() {

			{
				while (iterator.hasNext()) {
					next();
				}
			}

			@Override
			public boolean hasNext() {
				return iterator.hasPrevious();
			}

			@Override
			public T next() {
				return iterator.previous();
			}

		};
	}

	public static <T> Enumerable<T> create(final Iterable<T> iterable) {
		return create(iterable.iterator());
	}

	public static <K, V> Enumerable<Map.Entry<K, V>> create(final Map<K, V> map) {
		return create(map.entrySet().iterator());
	}

	public <U> Enumerable<U> select(final UnaryFunction<T, U> projector) {
		return new Enumerable<U>() {

			@Override
			public boolean hasNext() {
				return Enumerable.this.hasNext();
			}

			@Override
			public U next() {
				return projector.invoke(Enumerable.this.next());
			}

		};
	}

	public Enumerable<T> where(final UnaryFunction<T, Boolean> predicate) {

		return new Enumerable<T>() {

			private T nextItem = null;

			{
				nextItem = findNext();
			}

			private T findNext() {
				while (Enumerable.this.hasNext()) {
					T i = Enumerable.this.next();
					if (predicate.invoke(i)) {
						return i;
					}
				}
				return null;
			}

			@Override
			public boolean hasNext() {
				return nextItem != null;
			}

			@Override
			public T next() {
				if (nextItem == null) {
					throw new NoSuchElementException();
				}

				T value = nextItem;
				nextItem = findNext();

				return value;
			}

			@Override
			public void remove() {
				Enumerable.this.remove();
			}
		};
	}

	public boolean all(final UnaryFunction<T, Boolean> predicate) {
		while (hasNext()) {
			if (!predicate.invoke(next())) {
				return false;
			}
		}
		return true;
	}

	public boolean any(final UnaryFunction<T, Boolean> predicate) {
		while (hasNext()) {
			if (predicate.invoke(next())) {
				return true;
			}
		}
		return false;
	}

	public void forEach(final UnaryAction<T> action) {
		while (hasNext()) {
			action.invoke(next());
		}
	}

	public Enumerable<T> take(final int size) {
		return new Enumerable<T>() {

			private int i = size;

			@Override
			public boolean hasNext() {
				return Enumerable.this.hasNext() && i > 0;
			}

			@Override
			public T next() {
				if (i-- <= 0)
					throw new NoSuchElementException();
				return Enumerable.this.next();
			}

			@Override
			public void remove() {
				Enumerable.this.remove();
			}

		};
	}

	public Enumerable<T> skip(final int size) {
		return new Enumerable<T>() {

			{
				int i = 0;
				while (Enumerable.this.hasNext() && i < size) {
					next();
					++i;
				}
			}
			
			@Override
			public boolean hasNext() {
				return Enumerable.this.hasNext();
			}

			@Override
			public T next() {
				return Enumerable.this.next();
			}
			
		};
	}
	public Enumerable<Enumerable<T>> buffer(final int size) {

		return new Enumerable<Enumerable<T>>() {

			@Override
			public boolean hasNext() {
				return Enumerable.this.hasNext();
			}

			@Override
			public Enumerable<T> next() {
				return Enumerable.this.take(size);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public <U> Enumerable<U> ofType(TypeLiteral<U> type) {
		return ofType(type.asClass());
	}

	public <U> Enumerable<U> ofType(final Class<U> clazz) {
		return where(new UnaryFunction<T, Boolean>() {

			@Override
			public Boolean invoke(T arg) {
				return clazz.isInstance(arg);
			}

		}).select(new UnaryFunction<T, U>() {

			@Override
			public U invoke(T arg) {
				return clazz.cast(arg);
			}

		});
	}

	public <U> Enumerable<U> cast(final Class<U> clazz) {
		return select(new UnaryFunction<T, U>() {

			@Override
			public U invoke(T arg) {
				return clazz.cast(arg);
			}

		});
	}

	public <U> Enumerable<U> cast(TypeLiteral<U> type) {
		return cast(type.asClass());
	}

	public <U> U aggregate(final U initialValue, BinaryFunction<T, U, U> aggregator) {
		U value = initialValue;
		while (hasNext()) {
			value = aggregator.invoke(next(), value);
		}
		return value;
	}

	public <K, V> Map<K, V> toMap(UnaryFunction<T, K> keySelector, UnaryFunction<T, V> valueSelector, Map<K, V> map) {

		while (Enumerable.this.hasNext()) {
			T item = Enumerable.this.next();
			map.put(keySelector.invoke(item), valueSelector.invoke(item));
		}

		return map;
	}

	public <K, V> Map<K, V> toMap(UnaryFunction<T, K> keySelector, UnaryFunction<T, V> valueSelector) {
		return toMap(keySelector, valueSelector, new HashMap<K, V>());
	}

	public <K, V> Map<K, Collection<V>> groupBy(UnaryFunction<T, K> keySelector, UnaryFunction<T, V> valueSelector, Map<K,Collection<V>> map) {

		while (Enumerable.this.hasNext()) {

			T item = Enumerable.this.next();

			final K key = keySelector.invoke(item);

			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<V>());
			}

			map.get(key).add(valueSelector.invoke(item));
		}

		return map;
	}

	public <K, V> Map<K, Collection<V>> groupBy(UnaryFunction<T, K> keySelector, UnaryFunction<T, V> valueSelector) {
		return groupBy(keySelector, valueSelector, new HashMap<K,Collection<V>>());
	}

	public <K> Map<K, Collection<T>> groupBy(UnaryFunction<T, K> keySelector) {
		return groupBy(keySelector, new IdentitySelector<T>());
	}

	public int size() {
		int i = 0;
		while (hasNext()) {
			++i;
			next();
		}
		return i;
	}

	public int size(UnaryFunction<T, Boolean> predicate) {

		int i = 0;

		while (hasNext()) {
			if (predicate.invoke(next())) {
				++i;
			}
		}

		return i;
	}

	public <C extends Collection<T>> C toCollection(C collection) {
		for (T item : Enumerable.this) {
			collection.add(item);
		}
		return collection;
	}

	public List<T> toList(List<T> list) {
		return toCollection(list);
	}

	public List<T> toList() {
		return toList(new ArrayList<T>());
	}

	public Set<T> toSet(Set<T> set) {
		return toCollection(set);
	}

	public Set<T> toSet() {
		return toSet(new HashSet<T>());
	}

	public T min(final Comparator<T> comparator) {
		return aggregate(null, new BinaryFunction<T, T, T>() {

			@Override
			public T invoke(T arg1, T arg2) {
				return arg2 == null ? arg1 : comparator.compare(arg2, arg1) > 0 ? arg1 : arg2;
			}

		});
	}

	public T max(final Comparator<T> comparator) {
		return aggregate(null, new BinaryFunction<T, T, T>() {

			@Override
			public T invoke(T arg1, T arg2) {
				return arg2 == null ? arg1 : comparator.compare(arg2, arg1) <= 0 ? arg1 : arg2;
			}

		});
	}

	public <R> Enumerable<R> selectMany(final UnaryFunction<T, Enumerable<R>> selector) {
		return new Enumerable<R>() {

			Enumerable<R> buffer = null;

			@Override
			public boolean hasNext() {
				while ((buffer == null || !buffer.hasNext()) && Enumerable.this.hasNext()) {
					buffer = selector.invoke(Enumerable.this.next());
				}
				return buffer != null && buffer.hasNext();
			}

			@Override
			public R next() {

				if (!hasNext()) {
					throw new NoSuchElementException();
				}

				return buffer.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public <U> boolean sequenceEquals(Iterator<U> iterator, BinaryFunction<T, U, Boolean> comparer) {
		while (hasNext() && iterator.hasNext()) {
			if (comparer.invoke(next(), iterator.next()) != true) {
				return false;
			}
		}
		return !(hasNext() || iterator.hasNext());
	}

	public boolean sequenceEquals(Iterator<T> iterator) {
		return sequenceEquals(iterator, new EqualityComparer<T>());
	}

	public <U> boolean sequenceEquals(Iterable<U> iterable, BinaryFunction<T, U, Boolean> comparer) {
		return sequenceEquals(iterable.iterator(), comparer);
	}

	public boolean sequenceEquals(Iterable<T> iterable) {
		return sequenceEquals(iterable.iterator());
	}

	public <U> boolean sequenceEquals(U[] array, BinaryFunction<T, U, Boolean> comparer) {
		return sequenceEquals(Enumerable.create(array).iterator(), comparer);
	}

	public boolean sequenceEquals(T[] array) {
		return sequenceEquals(Enumerable.create(array).iterator());
	}

	public T firstOrDefault(UnaryFunction<T, Boolean> predicate) {

		while (hasNext()) {
			final T value = next();
			if (predicate.invoke(value)) {
				return value;
			}
		}

		return null;
	}

	public T firstOrDefault() {

		return hasNext() ? next() : null;
	}

	public T lastOrDefault(UnaryFunction<T, Boolean> predicate) {

		T result = null;

		while (hasNext()) {
			final T value = next();
			if (predicate.invoke(value)) {
				result = value;
			}
		}

		return result;
	}

	public T lastOrDefault() {

		T result = null;

		while (hasNext()) {
			result = next();
		}

		return result;
	}

	public T first(UnaryFunction<T, Boolean> predicate) {

		while (hasNext()) {
			final T value = next();
			if (predicate.invoke(value)) {
				return value;
			}
		}

		throw new NoSuchElementException();
	}

	public T first() {

		if (hasNext()) {
			return next();
		}

		throw new NoSuchElementException();
	}

	public T last(UnaryFunction<T,Boolean> predicate) {
		
		boolean hasResult = false;
		
		T result = null;
		
		while (hasNext()) {
			final T value = next();
			if (predicate.invoke(value)) {
				hasResult = true;
				result = value;
			}
		}
		
		if (!hasResult) {
			throw new NoSuchElementException();
		}
		
		return result;
	}

	public T last() {

		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		T result = null;

		while (hasNext()) {
			result = next();
		}

		return result;
	}

}

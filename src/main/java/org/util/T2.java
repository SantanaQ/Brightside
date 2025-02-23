package org.util;

import java.util.Map;
import java.util.function.Function;

public record T2<E1, E2>(E1 e1, E2 e2)
{
	public static <E1, E2> T2<E1, E2> fromMapEntry(Map.Entry<E1, E2> entry)
	{
		return new T2(entry.getKey(), entry.getValue());
	}

	T2<E2, E1> reverse()
	{
		return new T2(this.e2(), this.e1());
	}

	<N> T2<N, E2> map1(Function<E1, N> mapping)
	{
		return new T2(mapping.apply(this.e1()), this.e2());
	}

	<N> T2<E1, N> map2(Function<E2, N> mapping)
	{
		return new T2(this.e1(), mapping.apply(this.e2()));
	}
}

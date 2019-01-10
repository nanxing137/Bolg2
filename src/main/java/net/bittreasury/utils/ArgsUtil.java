package net.bittreasury.utils;

public final class ArgsUtil {
	private ArgsUtil() {
	}

	public static <T> T getArg(Object[] objects, Class<T> target) {

		for (Object object : objects) {
			if (target.isInstance(object)) {
				return (T) object;
			}
		}

		return null;
	}
}

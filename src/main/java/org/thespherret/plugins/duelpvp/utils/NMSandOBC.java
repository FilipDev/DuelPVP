package org.thespherret.plugins.duelpvp.utils;

import org.bukkit.Bukkit;

/*
 * Created by TheSpherret. (www.thespherret.org)
 * Taken from https://bitbucket.org/_Filip_/utilities/src/
 */

public class NMSandOBC {

	private static String version;

	public static Class getNMSClass(String className)
	{
		return getTheClass("net.minecraft.server.", className);
	}

	public static Class getOBCClass(String className)
	{
		return getTheClass("org.bukkit.craftbukkit.", className);
	}

	public static Class getTheClass(String path, String className)
	{
		if (NMSandOBC.version == null)
			NMSandOBC.version = Bukkit.getServer().getClass().getName().split("\\.")[3];
		try {
			return Class.forName(path + NMSandOBC.version + "." + className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}

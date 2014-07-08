package org.thespherret.plugins.duelpvp.utils;

import org.bukkit.Bukkit;

public class NMSandOBC {

	public static Class getNMSClass(String s){
		String path = "net.minecraft.server.v";
		String version = Bukkit.getBukkitVersion();
		String moddedVersion = version.replaceAll("\\.", "_");
		String releaseVersion = "";

		String[] vrs = moddedVersion.split("-");

		String[] versionArgs = vrs[0].split("_");
		vrs[0] = versionArgs[0] + "_" + versionArgs[1] + "_";

		if (vrs.length > 1){
			releaseVersion = vrs[1].replaceAll("0_", "");
		}
		releaseVersion = vrs[0] + releaseVersion;
		try {
			return Class.forName(path + releaseVersion + "." + s);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Class getOBCClass(String s){
		String path = "org.bukkit.craftbukkit.v";
		String version = Bukkit.getBukkitVersion();
		String moddedVersion = version.replaceAll("\\.", "_");
		String releaseVersion = "";

		String[] vrs = moddedVersion.split("-");

		String[] versionArgs = vrs[0].split("_");
		vrs[0] = versionArgs[0] + "_" + versionArgs[1] + "_";

		if (vrs.length > 1){
			releaseVersion = vrs[1].replaceAll("0_", "");
		}
		releaseVersion = vrs[0] + releaseVersion;
		try {
			return Class.forName(path + releaseVersion + "." + s);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}

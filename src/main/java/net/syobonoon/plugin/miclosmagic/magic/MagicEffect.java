package net.syobonoon.plugin.miclosmagic.magic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MagicEffect {
	private Plugin plugin;

	public MagicEffect(Plugin plugin) {
		this.plugin = plugin;
	}

	//スパイラル
	public void spiral(Player p, Particle particle_type) {
		BukkitRunnable task = new BukkitRunnable() {
			double t = 0;
			double r = 0.5;
			Location loc = p.getLocation();

			public void run() {
				t = t + Math.PI/8;
				double x = r*Math.cos(t);
				double y = t;
				double z = r*Math.sin(t);
				loc.add(x, y, z);
				loc.getWorld().spawnParticle(particle_type, loc, 0, 0, 0, 0, 1);
				loc.subtract(x, y, z);
				if (t > Math.PI*4) this.cancel();
			}
		};
		task.runTaskTimer(plugin, 0, 1);
	}

	//波打ちながら広がっていく
	public void expand_wave(Player p, Particle particle_type) {
		BukkitRunnable task = new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();

			public void run() {
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32) {
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x, y, z);
					loc.getWorld().spawnParticle(particle_type, loc, 0, 0, 0, 0, 1);
					loc.subtract(x, y, z);
				}
				if (t > 20) this.cancel();
			}
		};
		task.runTaskTimer(plugin, 0, 1);
	}
}

package fr.grapho0ver.compte;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GraphCompte extends JavaPlugin implements Listener{
	public FileConfiguration config;
	public String l1;
	public String l2;
	public String l3; 
	public String l4;
	public static ArrayList<SignCompte> signComptes= new ArrayList<SignCompte>();
	public SaveRunnable task;
	
	@Override
	public void onDisable() {
		this.save();
		this.saveConfig();
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(this, this);
		config = getConfig();
		this.CreateBase();
		config.options().copyDefaults(true);
		this.saveConfig();
		l1 = config.getString("Line1");
		l2 = config.getString("Line2");
		l3 = config.getString("Line3");
		l4 = config.getString("Line4");
		this.load();
		task = new SaveRunnable(this);
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, task, 500, 500);
	}
	
	public void CreateBase()
	{
		config.addDefault("Line1", "<account>");
		config.addDefault("Line2", "possède");
		config.addDefault("Line3", "<money>");
		config.addDefault("Line4", "euros");
	}
	
	@EventHandler
	public void change(SignChangeEvent event)
	{
		Player p = event.getPlayer();
		if(event.getLine(0).equalsIgnoreCase("[compte]"))
		{
			CAccount as = new CAccount(event.getLine(1));
			SignCompte sc = new SignCompte(as, event.getBlock().getLocation());
			event.setLine(0, this.getSentence("Line1", sc.getAcc()));
			event.setLine(1, this.getSentence("Line2", sc.getAcc()));
			event.setLine(2, this.getSentence("Line3", sc.getAcc()));
			event.setLine(3, this.getSentence("Line4", sc.getAcc()));
		}
		
		return;
	}
	
	public void updateSigns()
	{
		for(SignCompte sc : this.signComptes)
		{
			Block b = sc.getLocation().getBlock();
			if(b.getState() instanceof Sign)
			{
				Sign sign = (Sign)sc.getLocation().getBlock().getState();
				CAccount as = sc.getAcc();
				sign.setLine(0, this.getSentence("Line1", as));
				sign.setLine(1, this.getSentence("Line2", as));
				sign.setLine(2, this.getSentence("Line3", as));
				sign.setLine(3, this.getSentence("Line4", as));
				sign.update();
			}
		}
	}
	
	public void load()
	{
		if (getConfig().getConfigurationSection("CompteSigns") != null) {
			Set<String> keys = getConfig().getConfigurationSection("CompteSigns").getKeys(false);

			for (String key : keys) {
				ConfigurationSection cs = getConfig().getConfigurationSection("CompteSigns." + key);
		        World w = this.getServer().getWorld(cs.getString("loc.world"));

		        if (w != null) {
		        	CAccount acc = new CAccount(cs.getString("account.name"));
		        	new SignCompte(acc, new Location(w, cs.getInt("loc.x"), cs.getInt("loc.y"), cs.getInt("loc.z")));
		        }
		      }  
		    }
	}
	
	public void save()
	{
		for(SignCompte sc : this.signComptes)
		{
			Location loc = sc.getLocation();
			String node = "CompteSigns." + loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
			config.set(node + ".account.name", sc.getAcc().getName());
		    config.set(node + ".loc.world", loc.getWorld().getName());
		    config.set(node + ".loc.x", Integer.valueOf(loc.getBlockX()));
		    config.set(node + ".loc.y", Integer.valueOf(loc.getBlockY()));
		    config.set(node + ".loc.z", Integer.valueOf(loc.getBlockZ()));
		}
	}
	
	public String getSentence(String key, CAccount ca)
	{
		String ret = config.getString(key);
		if(ret.contains("<account>"))
		{
			ret = ret.replace("<account>", ca.getName());
		}
		
		if(ret.contains("<money>"))
		{
			ret = ret.replace("<money>", ca.getAcc().getHoldings().getBalance().toString());
		}
		
		return ret;
	}
	
	public class SaveRunnable implements Runnable
	{
	    public GraphCompte plugin;

	    public SaveRunnable(GraphCompte plugin) {
	      this.plugin = plugin;
	    }

	    public void run()
	    {
	    	plugin.updateSigns();
	    	System.out.println("Actualisation !");
	    }
	}
}
